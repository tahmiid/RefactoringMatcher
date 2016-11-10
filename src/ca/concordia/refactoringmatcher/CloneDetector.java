package ca.concordia.refactoringmatcher;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;


public interface CloneDetector {
		
	List<Pair<CodeBlock, CodeBlock>> detectClonePairs(Path path) throws IOException, InterruptedException, ParseException;
}
