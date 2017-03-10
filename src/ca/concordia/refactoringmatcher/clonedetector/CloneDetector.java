package ca.concordia.refactoringmatcher.clonedetector;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;


public interface CloneDetector {
		
	List<Pair<CodeLocation, CodeLocation>> detectClonePairs(Path path) throws IOException, InterruptedException, ParseException;

	List<Pair<CodeLocation, CodeLocation>> detectClones(Path datasetPath, Path queryPath)
			throws ParseException, InterruptedException, IOException;
}
