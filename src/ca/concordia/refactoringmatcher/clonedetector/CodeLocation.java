package ca.concordia.refactoringmatcher.clonedetector;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Path;

public class CodeLocation  implements Serializable{
	private int id;
	private Path file;
	private int start;
	private int end;
	private String code;
	
	public CodeLocation(int id, Path file, int start, int end, Path searchFilePath) throws FileNotFoundException, IOException {
		this.id = id;
		this.file = file;
		this.start = start;
		this.end = end;
		this.code = "";
		String line;
		int lineNumber = 1;
//		try (InputStream fis = new FileInputStream(searchFilePath.toString());
//				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
//				BufferedReader br = new BufferedReader(isr);) {
//			while ((line = br.readLine()) != null) {
//				if(lineNumber>= start && lineNumber <= end)
//					code += line+"\n";
//				
//				lineNumber++;
//			}
//			
//		}
	}

	
	
	public String getCode() {
		return code;
	}

	public int getId() {
		return id;
	}

	public Path getFile() {
		return file;
	}
	
	public int getFileNumber() {
		String filename = file.getFileName().toString();
		return Integer.parseInt(filename.subSequence(0, filename.lastIndexOf('.')).toString());
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
