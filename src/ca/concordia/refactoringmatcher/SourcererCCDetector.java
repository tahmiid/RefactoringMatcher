package ca.concordia.refactoringmatcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import input.logic.InputBuilderClassic;

public class SourcererCCDetector implements CloneDetector {
	
	@Override
	public List<Pair<CodeLocation, CodeLocation>> detectClonePairs(Path path) throws IOException, InterruptedException, ParseException {
		Path sourcererCCOutputPath = Paths.get("output8.0/tokensclones_index_WITH_FILTER.txt");
		
		Path detectionDirectory = Paths.get("cloneDetectionTmp");
		Path queryDirectory = Paths.get(detectionDirectory.toString() + "/query");
		Path datasetDirectory = Paths.get(detectionDirectory.toString() + "/dataset");
		Path searchDirectory = Paths.get(detectionDirectory.toString() + "/search");
		Path tokenDirectory = Paths.get(detectionDirectory.toString() + "/tokens");
		Path headerDirectory = Paths.get(detectionDirectory.toString() + "/headers");

		Path tokensPath = Paths.get(tokenDirectory.toString() + "/tokens.file");
		Path headersPath = Paths.get(headerDirectory.toString() + "/headers.file");
		Path searchFilePath = Paths.get(searchDirectory.toString() + "/search.java");

		if(Files.exists(detectionDirectory))
			deleteDir(detectionDirectory);
		Files.createDirectories(detectionDirectory);
		Files.createDirectories(queryDirectory);
		Files.createDirectories(datasetDirectory);
		Files.createDirectories(searchDirectory);
		Files.createDirectories(tokenDirectory);
		Files.createDirectories(headerDirectory);

		Files.copy(path, searchFilePath, StandardCopyOption.REPLACE_EXISTING);
		InputBuilderClassic.build(searchDirectory, tokensPath, headersPath, "functions", "java", 8, 0, 0, 1, 0, false,
				false, false);

		Files.copy(tokensPath, queryDirectory.resolve(tokensPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(tokensPath, datasetDirectory.resolve(tokensPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
		String[] arg = new String[2];
		arg[0] = "index";
		arg[1] = "8";
		indexbased.SearchManager.main(arg);
		arg[0] = "search";
		indexbased.SearchManager.main(arg);
		
		SourceCCOutputParser sccParser = new SourceCCOutputParser(headersPath, sourcererCCOutputPath);
		return sccParser.parseClonePairs();

	}
	
	public void deleteDir(Path dir) {
	    File[] files = dir.toFile().listFiles();

	    for (File myFile: files) {
	        if (myFile.isDirectory()) {  
	            deleteDir(myFile.toPath());
	        } 
	        myFile.delete();

	    }
	}
}
