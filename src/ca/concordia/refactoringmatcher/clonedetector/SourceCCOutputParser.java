package ca.concordia.refactoringmatcher.clonedetector;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class SourceCCOutputParser {
	List<CodeLocation> codeBlocks;
	List<Pair<Integer, Integer>> rawClonePairs;
	Path searchFile;

	public SourceCCOutputParser(Path headersPath, Path sourcererCCOutputPath, Path searchFilePath) throws IOException {
		ParseHeaderFile(headersPath,searchFilePath);
		ParseOutputFile(sourcererCCOutputPath);
	}

	public List<Pair<CodeLocation, CodeLocation>> parseClonePairs() {
		List<Pair<CodeLocation, CodeLocation>> clonePairs = new ArrayList<Pair<CodeLocation, CodeLocation>>();
		for (Pair<Integer, Integer> rawClonePair : rawClonePairs) {
			CodeLocation left = codeBlocks.stream().filter(block->block.getId()==rawClonePair.getLeft()).findFirst().get();
			CodeLocation right = codeBlocks.stream().filter(block->block.getId()==rawClonePair.getRight()).findFirst().get();
			Pair<CodeLocation, CodeLocation> clonePair = Pair.of(left, right);
			clonePairs.add(clonePair);
		}
		return clonePairs;
	}
	
	private void ParseHeaderFile(Path headersPath, Path searchFilePath) throws IOException {
		codeBlocks = new ArrayList<CodeLocation>();
		String line;
		try (InputStream fis = new FileInputStream(headersPath.toString());
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				BufferedReader br = new BufferedReader(isr);) {
			while ((line = br.readLine()) != null) {
				String[] headerParts = line.split(",");
				int id = Integer.parseInt(headerParts[0]);
				Path file = Paths.get(headerParts[1]);
				int start = Integer.parseInt(headerParts[2]);
				int end = Integer.parseInt(headerParts[3]);
				CodeLocation codeLocation = new CodeLocation(id, file, start, end,searchFilePath);
				codeBlocks.add(codeLocation);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void ParseOutputFile(Path sourcererCCOutputPath) throws IOException {
		rawClonePairs = new ArrayList<Pair<Integer, Integer>>();
		String line;
		try (InputStream fis = new FileInputStream(sourcererCCOutputPath.toString());
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				BufferedReader br = new BufferedReader(isr);) {
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				Integer left = Integer.parseInt(parts[0]);
				Integer right = Integer.parseInt(parts[1]);
				Pair<Integer, Integer> pair = Pair.of(left, right);
				rawClonePairs.add(pair);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
