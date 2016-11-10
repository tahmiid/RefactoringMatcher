package ca.concordia.refactoringmatcher;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CodeBlock {
	private int id;
	private Path file;
	private int start;
	private int end;
	
	public CodeBlock(int id, String file, int start, int end) {
		this.id = id;
		this.file = Paths.get(file);
		this.start = start;
		this.end = end;
	}
	
	public CodeBlock(String line) {
		String[] headerParts = line.split(",");
		id = Integer.parseInt(headerParts[0]);
		file = Paths.get(headerParts[1]);
		start = Integer.parseInt(headerParts[2]);
		end = Integer.parseInt(headerParts[3]);
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
