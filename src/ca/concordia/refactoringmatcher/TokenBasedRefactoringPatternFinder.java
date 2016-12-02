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

import ca.concordia.refactoringmatcher.clonedetector.CloneDetector;
import ca.concordia.refactoringmatcher.clonedetector.CodeLocation;
import ca.concordia.refactoringmatcher.clonedetector.SourcererCCDetector;

public class TokenBasedRefactoringPatternFinder implements RefactoringPatternFinder {

	List<RefactoringSet> similarRefactorings;
	List<RefactoringPair> similarRefactoringPairs;

	public TokenBasedRefactoringPatternFinder(List<RefactoringData> refactorings, Path outputDirectory)
			throws IOException, InterruptedException, ParseException {

		Path beforeCodePath = createEmptyFile(Paths.get(outputDirectory + "/beforeCode.java"));
		Path afterCodePath = createEmptyFile(Paths.get(outputDirectory + "/afterCode.java"));
		HashMap<Integer, RefactoringData> codeMapingBefore = new HashMap<Integer, RefactoringData>();
		ArrayList<Pair<Integer, RefactoringData>> beforeCodeMaping = new ArrayList<Pair<Integer, RefactoringData>>();
		ArrayList<Pair<Integer, RefactoringData>> afterCodeMaping = new ArrayList<Pair<Integer, RefactoringData>>();
		HashMap<Integer, RefactoringData> codeMapingAfter = new HashMap<Integer, RefactoringData>();
		
		String text;
		int start;
		int beforeLineCount = 1, afterLineCount = 1;
		for (RefactoringData refactoringData : refactorings) {

			text = refactoringData.getBeforeCodeBody();
			addToFile(beforeCodePath, text);
			start = beforeLineCount;
			beforeLineCount += countLines(text);
			codeMapingBefore.put(start, refactoringData);
			beforeCodeMaping.add(Pair.of(start, refactoringData));

			text = refactoringData.getAfterCodeBody();
			addToFile(afterCodePath, text);
			start = afterLineCount;
			afterLineCount += countLines(text);
			codeMapingAfter.put(start, refactoringData);
			afterCodeMaping.add(Pair.of(start, refactoringData));
		}

		CloneDetector detector = new SourcererCCDetector();

		List<Pair<CodeLocation, CodeLocation>> afterClonePairs = detector.detectClonePairs(afterCodePath);

		findSimilarRefactoringsBasedOnExtractedCode(afterClonePairs, codeMapingAfter, afterCodeMaping);
	}

	private void findSimilarRefactoringsBasedOnExtractedCode(List<Pair<CodeLocation, CodeLocation>> afterClonePairs,
			HashMap<Integer, RefactoringData> codeMapingAfter,
			ArrayList<Pair<Integer, RefactoringData>> afterCodeMaping) {

		similarRefactoringPairs = new ArrayList<RefactoringPair>();
		for (Pair<CodeLocation, CodeLocation> pair : afterClonePairs) {
			RefactoringData left = null; 
			RefactoringData right = null;

			for (int i = afterCodeMaping.size() - 1; i >= 0; i--) {
				if (pair.getLeft().getStartLocation() >= afterCodeMaping.get(i).getLeft()) {
					left = afterCodeMaping.get(i).getRight();
					break;
				}
			}

			for (int i = afterCodeMaping.size() - 1; i >= 0; i--) {
				if (pair.getRight().getStartLocation() >= afterCodeMaping.get(i).getLeft()) {
					right = afterCodeMaping.get(i).getRight();
					break;
				}
			}

			if (left == null || right == null) {
				System.out.println(pair.getLeft().getEndLocation() + " " + pair.getLeft().getFile());
				System.out.println(pair.getRight().getEndLocation() + " " + pair.getRight().getFile());
				continue;
			}

			if (left != right) {
				RefactoringPair refactoringPair = new RefactoringPair(left, right, pair.getLeft().getCode());
				similarRefactoringPairs.add(refactoringPair);
			}
		}

		createSimilarRefactoringSets();
	}

	private void createSimilarRefactoringSets() {
		List<RefactoringSet> sets = new ArrayList<RefactoringSet>();

		for (RefactoringPair pair : similarRefactoringPairs) {
			RefactoringSet set = new RefactoringSet();
			set.addAll(pair.getRefactorings());
			set.setSimilarCode(pair.getSimilarBlock());
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

		similarRefactorings = new ArrayList<RefactoringSet>();
		for (RefactoringSet hashSet : sets) {
			if (hashSet.size() > 0) {
				similarRefactorings.add(hashSet);
			}
		}
	}

	@Override
	public List<RefactoringPair> getSimilarRefactoringPairs() {
		return similarRefactoringPairs;
	}

	@Override
	public List<RefactoringSet> getSimilarRefactorings() {
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
