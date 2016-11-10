package ca.concordia.refactoringmatcher;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class TokenBasedRefactoringPatternFinder implements RefactoringPatternFinder{

	public TokenBasedRefactoringPatternFinder(List<RefactoringData> refactorings, Path outputDirectory) throws IOException, InterruptedException, ParseException {
		Path beforeCodePath = createEmptyFile( Paths.get(outputDirectory + "/beforeCode.java") );
		Path afterCodePath = createEmptyFile( Paths.get(outputDirectory + "/afterCode.java") );	
		
		Code code;
		String text;
		int beforeLineCount = 1, afterLineCount = 1;
		for (RefactoringData refactoringData : refactorings) {

			code = refactoringData.getBeforeCode();
			text = code.getText();
			addToCodeDatabase(beforeCodePath, text);

			code.setStartLocationInCodeDatabase(beforeLineCount);
			beforeLineCount += countLines(text);
			code.setEndLocationInCodeDatabase(beforeLineCount - 1);

			code = refactoringData.getAfterCode();
			text = code.getText();
			addToCodeDatabase(afterCodePath, text);

			code.setStartLocationInCodeDatabase(afterLineCount);
			afterLineCount += countLines(text);
			code.setEndLocationInCodeDatabase(afterLineCount - 1);
		}

		CloneDetector detector = new SourcererCCDetector();
		List<Pair<CodeBlock, CodeBlock>> clones = detector.detectClonePairs(beforeCodePath);
		
		for (Pair<CodeBlock, CodeBlock> pair : clones) {
			System.out.println(pair.getLeft() + " - " + pair.getRight());
		}
	}

	@Override
	public List<HashSet<RefactoringData>> findSimilarRefactorings(List<RefactoringData> refactorings) {
		return null;

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

	private void addToCodeDatabase(Path codePath, String text) {

		try {
			Files.write(codePath, text.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
