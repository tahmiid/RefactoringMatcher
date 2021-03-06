package ca.concordia.refactoringmatcher.clonedetector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.mondego.indexbased.SearchManager;

import input.logic.InputBuilderClassic;

//import indexbased.SearchManager;
//import input.logic.InputBuilderClassic;

public class SourcererCCDetector implements CloneDetector {

	static final String MATCHING_LANGUAGE = "java";
	static final int MATCHING_THREADS = 8;
	
	static final String DETECTION_LEVEL = "functions";
	static final int MIN_TOKENS = 0;
	static final int MAX_TOKENS = 0;
	static final int MIN_LINES = 0;
	static final int MAX_LINES = 0;
	static final boolean IGNORE_SEPERATORS = false;
	static final boolean IGNORE_OPERATORS = false;
	static final boolean IGNORE_CASE = false;
	
	static final int MATCHING_THRESHOLD = 8;
	
	@Override
	public List<Pair<CodeLocation, CodeLocation>> detectClonePairs(Path path)
			throws ParseException, InterruptedException, IOException {
		Path sourcererCCOutputPath = Paths.get("output" + MATCHING_THRESHOLD + ".0/tokensclones_index_WITH_FILTER.txt");

		Path detectionDirectory = Paths.get("cloneDetectionTmp");
		Path queryDirectory = Paths.get(detectionDirectory.toString() + "/query");
		Path datasetDirectory = Paths.get(detectionDirectory.toString() + "/dataset");
		Path searchDirectory = Paths.get(detectionDirectory.toString() + "/search");
		Path tokenDirectory = Paths.get(detectionDirectory.toString() + "/tokens");
		Path headerDirectory = Paths.get(detectionDirectory.toString() + "/headers");

		Path tokensPath = Paths.get(tokenDirectory.toString() + "/tokens.file");
		Path headersPath = Paths.get(headerDirectory.toString() + "/headers.file");
		Path searchFilePath = Paths.get(searchDirectory.toString() + "/search.java");

		if (Files.exists(detectionDirectory))
			deleteDir(detectionDirectory);
		Files.createDirectories(detectionDirectory);
		Files.createDirectories(queryDirectory);
		Files.createDirectories(datasetDirectory);
		Files.createDirectories(searchDirectory);
		Files.createDirectories(tokenDirectory);
		Files.createDirectories(headerDirectory);

		FileUtils.copyDirectory(path.toFile(), searchDirectory.toFile());
//		Files.copy(path, searchFilePath, StandardCopyOption.REPLACE_EXISTING);
		InputBuilderClassic.build(searchDirectory, tokensPath, headersPath, DETECTION_LEVEL, MATCHING_LANGUAGE, MATCHING_THREADS, MIN_TOKENS, MAX_TOKENS, MIN_LINES, MAX_LINES, IGNORE_SEPERATORS, IGNORE_OPERATORS, IGNORE_CASE);

		Files.copy(tokensPath, queryDirectory.resolve(tokensPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(tokensPath, datasetDirectory.resolve(tokensPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
		String[] arg = new String[2];
		arg[0] = "index";
		arg[1] = MATCHING_THRESHOLD + "";
		SearchManager.main(arg);
		arg[0] = "search";
		SearchManager.main(arg);

		SourceCCOutputParser sccParser = new SourceCCOutputParser(headersPath, sourcererCCOutputPath, searchFilePath);
		List<Pair<CodeLocation, CodeLocation>> sccResults = sccParser.parseClonePairs();

		deleteDir(detectionDirectory);
		deleteDir(Paths.get("fwdindex"));
		deleteDir(Paths.get("gtpm"));
		deleteDir(Paths.get("index"));
		deleteDir(Paths.get("output" + MATCHING_THRESHOLD + ".0"));
		deleteDir(Paths.get("output/sortedFiles"));
		return sccResults;

	}
	
	@Override
	public List<Pair<CodeLocation, CodeLocation>> detectClones(Path datasetPath, Path queryPath)
			throws ParseException, InterruptedException, IOException {
		Path sourcererCCOutputPath = Paths.get("output" + MATCHING_THRESHOLD + ".0/tokensclones_index_WITH_FILTER.txt");

		Path detectionDirectory = Paths.get("cloneDetectionTmp");
		Path queryDirectory = Paths.get(detectionDirectory.toString() + "/query");
		Path datasetDirectory = Paths.get(detectionDirectory.toString() + "/dataset");
		Path searchDirectory = Paths.get(detectionDirectory.toString() + "/search");
		Path tokenDirectory = Paths.get(detectionDirectory.toString() + "/tokens");
		Path headerDirectory = Paths.get(detectionDirectory.toString() + "/headers");

		Path tokensPath = Paths.get(tokenDirectory.toString() + "/tokens.file");
		Path headersPath = Paths.get(headerDirectory.toString() + "/headers.file");
		Path searchFilePath = Paths.get(searchDirectory.toString() + "/search.java");

		if (Files.exists(detectionDirectory))
			deleteDir(detectionDirectory);
		Files.createDirectories(detectionDirectory);
		Files.createDirectories(queryDirectory);
		Files.createDirectories(datasetDirectory);
		Files.createDirectories(searchDirectory);
		Files.createDirectories(tokenDirectory);
		Files.createDirectories(headerDirectory);

		Files.copy(queryPath, searchFilePath, StandardCopyOption.REPLACE_EXISTING);
		InputBuilderClassic.build(searchDirectory, tokensPath, headersPath, DETECTION_LEVEL, MATCHING_LANGUAGE, MATCHING_THREADS, MIN_TOKENS, MAX_TOKENS, MIN_LINES, MAX_LINES, IGNORE_SEPERATORS, IGNORE_OPERATORS, IGNORE_CASE);

		Files.copy(tokensPath, queryDirectory.resolve(tokensPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
		
		Files.copy(datasetPath, searchFilePath, StandardCopyOption.REPLACE_EXISTING);
		InputBuilderClassic.build(searchDirectory, tokensPath, headersPath, DETECTION_LEVEL, MATCHING_LANGUAGE, MATCHING_THREADS, MIN_TOKENS, MAX_TOKENS, MIN_LINES, MAX_LINES, IGNORE_SEPERATORS, IGNORE_OPERATORS, IGNORE_CASE);

		Files.copy(tokensPath, datasetDirectory.resolve(tokensPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
		String[] arg = new String[2];
		arg[0] = "index";
		arg[1] = MATCHING_THRESHOLD + "";
		SearchManager.main(arg);
		arg[0] = "search";
		SearchManager.main(arg);

		SourceCCOutputParser sccParser = new SourceCCOutputParser(headersPath, sourcererCCOutputPath, searchFilePath);
		List<Pair<CodeLocation, CodeLocation>> sccResults = sccParser.parseClonePairs();

		deleteDir(detectionDirectory);
		deleteDir(Paths.get("fwdindex"));
		deleteDir(Paths.get("gtpm"));
		deleteDir(Paths.get("index"));
		deleteDir(Paths.get("output" + MATCHING_THRESHOLD + ".0"));
		deleteDir(Paths.get("output/sortedFiles"));
		return sccResults;

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
}
