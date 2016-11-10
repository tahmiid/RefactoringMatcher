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

public class TokenBasedRefactoringPatternFinder implements RefactoringPatternFinder{ 
	
	public TokenBasedRefactoringPatternFinder(List<RefactoringData> refactorings, Path outputDirectory) throws IOException, InterruptedException, ParseException {
		HashMap <Integer, Code> CodeLocationMapingBefore = new HashMap<Integer, Code> ();
		HashMap <Integer, Code> CodeLocationMapingAfter = new HashMap<Integer, Code> ();
		List<Pair<Code, Code>> cloneCodePairs  = new ArrayList<Pair<Code, Code>>();
		Path beforeCodePath = createEmptyFile( Paths.get(outputDirectory + "/beforeCode.java") );
		Path afterCodePath = createEmptyFile( Paths.get(outputDirectory + "/afterCode.java") );	
		
		String text;
		int start;
//		int end;
		int beforeLineCount = 1, afterLineCount = 1;
//		CodeLocation codeLocation;
		for (RefactoringData refactoringData : refactorings) {

			text = refactoringData.getBeforeCodeText();
			addToFile(beforeCodePath, text);
			start = beforeLineCount;
			beforeLineCount += countLines(text);
//			end = beforeLineCount - 1;
//			codeLocation = new CodeLocation(0, beforeCodePath, start, end);
			CodeLocationMapingBefore.put(start, refactoringData.getBeforeCode());
			
			text = refactoringData.getAfterCodeText();
			addToFile(afterCodePath, text);
			start = afterLineCount;
			afterLineCount += countLines(text);
//			end = afterLineCount - 1;
//			codeLocation = new CodeLocation(0, afterCodePath, start, end);
			CodeLocationMapingAfter.put(start, refactoringData.getAfterCode());
		}

		CloneDetector detector = new SourcererCCDetector();
		List<Pair<CodeLocation, CodeLocation>> clonesPairs = detector.detectClonePairs(beforeCodePath);
		
		for (Pair<CodeLocation, CodeLocation> pair : clonesPairs) {
			Code left = CodeLocationMapingBefore.get(pair.getLeft().getStartLocation());
			Code right = CodeLocationMapingBefore.get(pair.getRight().getStartLocation());
			Pair<Code, Code> codePair = Pair.of(left, right);
			cloneCodePairs.add(codePair);
		}
		
		for (Pair<Code, Code> pair : cloneCodePairs) {
			System.out.println(pair.getLeft().getFilePath() +" --- "+pair.getRight().getFilePath());
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

	private void addToFile(Path codePath, String text) {
		try {
			Files.write(codePath, text.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
