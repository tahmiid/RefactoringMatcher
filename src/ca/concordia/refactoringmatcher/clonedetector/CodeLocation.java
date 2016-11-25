package ca.concordia.refactoringmatcher.clonedetector;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CodeLocation {
	private int id;
	private Path file;
	private int start;
	private int end;
	
	public CodeLocation(int id, Path file, int start, int end) {
		this.id = id;
		this.file = file;
		this.start = start;
		this.end = end;
	}

	public int getId() {
		return id;
	}

	public Path getFile() {
		return file;
	}

	public int getStartLocation() {
		return start;
	}

	public int getEndLocation() {
		return end;
	}
	
	public String toString()
	{
		return id+ " " +file.toString()+ "(" +start+ "," +end+ ")";
	}
}
