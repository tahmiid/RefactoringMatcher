package ca.concordia.refactoringmatcher;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.lib.Repository;

import ca.concordia.refactoringmatcher.clonedetector.CloneDetector;
import ca.concordia.refactoringmatcher.clonedetector.CodeLocation;
import ca.concordia.refactoringmatcher.clonedetector.SourcererCCDetector;

public class TokenBasedSimilarRefactoringFinder implements SimilarRefactoringFinder {

	public List<RefactoringPair> getSimilarRefactoringPairs(List<RefactoringData> refactorings) {
		try {
			Path refactoredCodeDirectory = Paths.get("tmp" + "/refactoredCode");
			if (Files.exists(refactoredCodeDirectory))
				deleteDir(refactoredCodeDirectory);
			Files.createDirectories(refactoredCodeDirectory);
			ArrayList<Pair<Integer, RefactoringData>> refactoredCodeMaping = new ArrayList<Pair<Integer, RefactoringData>>();
			String text;
			int fileNumber = 1;
			for (RefactoringData refactoringData : refactorings) {
				text = refactoringData.getRefactoredCode().getText();
				addToFile(Paths.get(refactoredCodeDirectory.toString() + "/" + fileNumber + ".java"), text);
				refactoredCodeMaping.add(Pair.of(fileNumber, refactoringData));
				fileNumber++;
			}
			CloneDetector detector = new SourcererCCDetector();
			List<Pair<CodeLocation, CodeLocation>> clonePairs = detector.detectClonePairs(refactoredCodeDirectory);

			return findSimilarRefactoringsBasedOnExtractedCode(clonePairs, refactoredCodeMaping);
		} catch (Exception e) {
			return null;
		}
	}

	private List<RefactoringPair> findSimilarRefactoringsBasedOnExtractedCode(
			List<Pair<CodeLocation, CodeLocation>> clonePairs,
			ArrayList<Pair<Integer, RefactoringData>> refactoredCodeMaping) {

		List<RefactoringPair> similarRefactoringPairs = new ArrayList<RefactoringPair>();
		for (Pair<CodeLocation, CodeLocation> pair : clonePairs) {
			RefactoringData left = null;
			RefactoringData right = null;

			for (Pair<Integer, RefactoringData> maping : refactoredCodeMaping) {
				if (maping.getKey() == pair.getLeft().getFileNumber())
					left = maping.getRight();
				if (maping.getKey() == pair.getRight().getFileNumber())
					right = maping.getRight();
			}

			if (left == null || right == null) {
				System.out.println(pair.getLeft().getEndLocation() + " " + pair.getLeft().getFile());
				System.out.println(pair.getRight().getEndLocation() + " " + pair.getRight().getFile());
				continue;
			}

			if (left != right) {
				RefactoringPair refactoringPair = new RefactoringPair(left, right);
				similarRefactoringPairs.add(refactoringPair);
			}
		}
		return similarRefactoringPairs;
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

	private void addToFile(Path codePath, String text) throws IOException {
		if (!Files.exists(codePath))
			createEmptyFile(codePath);

		try {
			Files.write(codePath, text.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void deleteDir(Path dir) {
		File[] files = dir.toFile().listFiles();

		for (File myFile : files) {
			if (myFile.isDirectory()) {
				deleteDir(myFile.toPath());
			}
			myFile.delete();

		}
		dir.toFile().delete();
	}

	@Override
	public List<RefactoringPair> getSimilarRefactoringPairs(ArrayList<Pair<RefactoringData, Repository>> refactorings,
			ExtendedGitService gitService) throws IOException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
