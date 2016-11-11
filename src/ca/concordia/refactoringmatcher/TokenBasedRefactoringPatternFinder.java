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
		similarRefactoringPairs = new ArrayList<Pair<RefactoringData, RefactoringData>>();
		similarRefactorings = new ArrayList<HashSet<RefactoringData>>();

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
		List<Pair<CodeLocation, CodeLocation>> clonePairs = detector.detectClonePairs(beforeCodePath);

		findSimilarRefactorings(clonePairs, codeMapingBefore, codeMapingAfter);
	}

	private void findSimilarRefactorings(List<Pair<CodeLocation, CodeLocation>> clonePairs,
			HashMap<Integer, RefactoringData> codeMapingBefore, HashMap<Integer, RefactoringData> codeMapingAfter) {
		for (Pair<CodeLocation, CodeLocation> pair : clonePairs) {
			RefactoringData left = codeMapingBefore.get(pair.getLeft().getStartLocation());
			RefactoringData right = codeMapingBefore.get(pair.getRight().getStartLocation());

			if (left.getType() != right.getType()) {
				continue;
			}

			if (left.getAfterCode().equals(right.getAfterCode())) {
				continue;
			}

			// if
			// (left.getCommit().toString().equals(right.getCommit().toString()))
			// {
			// if(left.getAfterCode().getFilePath().equals(right.getAfterCode().getFilePath()))
			// if(left.getAfterCode().getMethodName().equals(right.getAfterCode().getMethodName()))
			// continue;
			// }

			Pair<RefactoringData, RefactoringData> refactoringPair = Pair.of(left, right);
			similarRefactoringPairs.add(refactoringPair);

		}

		for (Pair<RefactoringData, RefactoringData> pair : similarRefactoringPairs) {
			addToRefactoringSets(pair.getLeft(), pair.getRight());
		}
	}

	private void addToRefactoringSets(RefactoringData left, RefactoringData right) {
		for (HashSet<RefactoringData> set : similarRefactorings) {

			if (set.contains(left) && set.contains(right)) {
				return;
			}

			if (set.contains(left) && !set.contains(right)) {
				set.add(right);
				return;
			} else if (set.contains(right) && !set.contains(left)) {
				set.add(left);
				return;
			}
		}

		HashSet<RefactoringData> group = new HashSet<RefactoringData>();
		group.add(left);
		group.add(right);
		similarRefactorings.add(group);
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
