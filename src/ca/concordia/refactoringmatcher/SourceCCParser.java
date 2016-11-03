package ca.concordia.refactoringmatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

public class SourceCCParser {

	File beforeHeader;
	File afterHeader;
	File beforeClones;
	File afterClones;

	public SourceCCParser(List<RefactoringData> allRefactoringData, String parentCodeDir, String refactoredCodeDir) {
		beforeHeader = new File(parentCodeDir + "/headers.file");
		afterHeader = new File(refactoredCodeDir + "/headers.file");
		beforeClones = new File(parentCodeDir + "/tokensclones_index_WITH_FILTER.txt");
		afterClones = new File(refactoredCodeDir + "/tokensclones_index_WITH_FILTER.txt");

		parseBeforeHeader();
		parseDuplicateRefactorings(allRefactoringData);
	}

	private void parseBeforeHeader() {
		String line;
		try (InputStream fis = new FileInputStream(beforeHeader.getPath());
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				BufferedReader br = new BufferedReader(isr);) {
			while ((line = br.readLine()) != null) {
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void parseDuplicateRefactorings(List<RefactoringData> allRefactoringData) {
		for (RefactoringData refactoringData : allRefactoringData) {
		}
	}

}
