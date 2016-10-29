package ca.concordia.refactoringmatcher;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitService;

public class Code {
	private String commit;
	private String filePath;
	private int startOffset;
	private int length;
	private String text;

	public Code(String commit, String filePath, int startOffset, int length) {
		this.commit = commit;
		this.filePath = filePath;
		this.startOffset = startOffset;
		this.length = length;
	}

	public String getCommit() {
		return commit;
	}

	public String getFilePath() {
		return filePath;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public int getLength() {
		return length;
	}

	public String extractSourceCode(GitService gitService, Repository repo) throws Exception {
		if (text == null) {
			gitService.checkout(repo, commit);
			text = readFile(filePath, StandardCharsets.UTF_8);
			text = text.subSequence(startOffset, startOffset + length).toString();
		}
		return text;
	}

	private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
