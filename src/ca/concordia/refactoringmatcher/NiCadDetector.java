package ca.concordia.refactoringmatcher;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class NiCadDetector implements CloneDetector{

	@Override
	public List<Pair<CodeLocation, CodeLocation>> detectClonePairs(Path path)
			throws IOException, InterruptedException, ParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
