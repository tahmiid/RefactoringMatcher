package ca.concordia.refactoringmatcher;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class TokenBasedRefactoringPatternFinder implements RefactoringPatternFinder {

	List<HashSet<RefactoringData>> similarRefactorings;
	List<Pair<RefactoringData, RefactoringData>> similarRefactoringPairs;

	public TokenBasedRefactoringPatternFinder(List<RefactoringData> refactorings, Path outputDirectory)
			throws IOException, InterruptedException, ParseException {

		Path beforeCodePath = createEmptyFile(Paths.get(outputDirectory + "/beforeCode.java"));
		Path afterCodePath = createEmptyFile(Paths.get(outputDirectory + "/afterCode.java"));
		HashMap<Integer, RefactoringData> codeMapingBefore = new HashMap<Integer, RefactoringData>();
		HashMap<Integer, RefactoringData> codeMapingAfter = new HashMap<Integer, RefactoringData>();
		String text;
		int start;
		int beforeLineCount = 1, afterLineCount = 1;
		for (RefactoringData refactoringData : refactorings) {

			text = refactoringData.getBeforeCodeText();
			addToFile(beforeCodePath, text);
			start = beforeLineCount;
			beforeLineCount += countLines(text);
			codeMapingBefore.put(start, refactoringData);

			text = refactoringData.getAfterCodeText();
			addToFile(afterCodePath, text);
			start = afterLineCount;
			afterLineCount += countLines(text);
			codeMapingAfter.put(start, refactoringData);
		}

		CloneDetector detector = new SourcererCCDetector();

//		List<Pair<CodeLocation, CodeLocation>> beforeClonePairs = detector.detectClonePairs(beforeCodePath);

		List<Pair<CodeLocation, CodeLocation>> afterClonePairs = detector.detectClonePairs(afterCodePath);

//		findSimilarRefactorings(beforeClonePairs, codeMapingBefore, afterClonePairs, codeMapingAfter);

		findSimilarRefactoringsBasedOnExtractedCode(afterClonePairs, codeMapingAfter);
	}

	private void findSimilarRefactoringsBasedOnExtractedCode(List<Pair<CodeLocation, CodeLocation>> afterClonePairs,
			HashMap<Integer, RefactoringData> codeMapingAfter) {

		similarRefactoringPairs = new ArrayList<Pair<RefactoringData, RefactoringData>>();
		for (Pair<CodeLocation, CodeLocation> pair : afterClonePairs) {
			RefactoringData left = codeMapingAfter.get(pair.getLeft().getStartLocation());
			RefactoringData right = codeMapingAfter.get(pair.getRight().getStartLocation());

			if (left == null || right == null) {
				// TODO handle methods inside innerclasses
				System.out.println(pair.getLeft().getEndLocation() + " " + pair.getLeft().getFile());
				System.out.println(pair.getRight().getEndLocation() + " " + pair.getRight().getFile());
				continue;
			}

			Pair<RefactoringData, RefactoringData> refactoringPair = Pair.of(left, right);
			similarRefactoringPairs.add(refactoringPair);
		}
		createSimilarRefactoringSets();
	}

	private void findSimilarRefactorings(List<Pair<CodeLocation, CodeLocation>> beforeClonePairs,
			HashMap<Integer, RefactoringData> codeMapingBefore, List<Pair<CodeLocation, CodeLocation>> afterClonePairs,
			HashMap<Integer, RefactoringData> codeMapingAfter) {

		List<Pair<RefactoringData, RefactoringData>> afterRefactoringPairs = new ArrayList<Pair<RefactoringData, RefactoringData>>();
		for (Pair<CodeLocation, CodeLocation> pair : afterClonePairs) {
			RefactoringData left = codeMapingAfter.get(pair.getLeft().getStartLocation());
			RefactoringData right = codeMapingAfter.get(pair.getRight().getStartLocation());

			if (left == null || right == null) {
				// TODO handle methods inside innerclasses
				System.out.println(pair.getLeft().getEndLocation() + " " + pair.getLeft().getFile());
				System.out.println(pair.getRight().getEndLocation() + " " + pair.getRight().getFile());
				continue;
			}

			Pair<RefactoringData, RefactoringData> refactoringPair = Pair.of(left, right);
			afterRefactoringPairs.add(refactoringPair);
		}

		similarRefactoringPairs = new ArrayList<Pair<RefactoringData, RefactoringData>>();
		for (Pair<CodeLocation, CodeLocation> pair : beforeClonePairs) {
			RefactoringData left = codeMapingBefore.get(pair.getLeft().getStartLocation());
			RefactoringData right = codeMapingBefore.get(pair.getRight().getStartLocation());

			if (left == null || right == null) {
				// TODO handle methods inside innerclasses
				System.out.println(pair.getLeft().getEndLocation() + " " + pair.getLeft().getFile());
				System.out.println(pair.getRight().getEndLocation() + " " + pair.getRight().getFile());
				continue;
			}

			// if (left.getAfterCode().equals(right.getAfterCode())) {
			// continue;
			// }

			for (Pair<RefactoringData, RefactoringData> afterPair : afterRefactoringPairs) {
				if ((afterPair.getLeft() == left && afterPair.getRight() == right)
						|| (afterPair.getLeft() == right && afterPair.getRight() == left)) {
					Pair<RefactoringData, RefactoringData> refactoringPair = Pair.of(left, right);
					similarRefactoringPairs.add(refactoringPair);
					break;
				}
			}
		}

		createSimilarRefactoringSets();
	}

	private void createSimilarRefactoringSets() {
		List<HashSet<RefactoringData>> sets = new ArrayList<HashSet<RefactoringData>>();

		for (Pair<RefactoringData, RefactoringData> pair : similarRefactoringPairs) {
			HashSet<RefactoringData> set = new HashSet<>();
			set.add(pair.getLeft());
			set.add(pair.getRight());
			sets.add(set);
		}

		for (int i = 0; i < sets.size() - 1; i++) {
			if (sets.get(i).size() == 0)
				continue;

			for (int j = i + 1; j < sets.size(); j++) {
				HashSet<RefactoringData> intersection = new HashSet<>(sets.get(i));
				intersection.retainAll(sets.get(j));

				if (intersection.size() > 0) {
					sets.get(i).addAll(sets.get(j));
					sets.get(j).clear();
				}
			}
		}

		similarRefactorings = new ArrayList<HashSet<RefactoringData>>();
		for (HashSet<RefactoringData> hashSet : sets) {
			if (hashSet.size() > 0) {
				similarRefactorings.add(hashSet);
			}
		}
	}

	@Override
	public List<Pair<RefactoringData, RefactoringData>> getSimilarRefactoringPairs() {
		return similarRefactoringPairs;
	}

	@Override
	public List<HashSet<RefactoringData>> getSimilarRefactorings() {
		return similarRefactorings;
	}

	private Path createEmptyFile(Path path) throws IOException {
		Files.createDirectories(path.getParent());
		PrintWriter writer;
		new File(path.toString()).createNewFile();
		writer = new PrintWriter(path.toString());
		writer.print("");
		writer.close();
		return path;
	}

	private int countLines(String str) {
		String[] lines = str.split("\r\n|\r|\n");
		return lines.length;
	}

	private void addToFile(Path codePath, String text) {
		try {
			Files.write(codePath, text.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
