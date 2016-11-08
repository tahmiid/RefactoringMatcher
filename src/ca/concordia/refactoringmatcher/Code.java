package ca.concordia.refactoringmatcher;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitService;

public class Code implements Serializable{
	private String commit;
	private String filePath;
	private int startOffset;
	private int length;
	private String text;
	private int startLocationInCodeDatabase;
	private int endLocationInCodeDatabase;
	public int headerNumber;

	public Code(String commit, String filePath, int startOffset, int length) {
		this.commit = commit;
		this.filePath = filePath;
		this.startOffset = startOffset;
		this.length = length;
	}

	
	
	public int getStartLocationInCodeDatabase() {
		return startLocationInCodeDatabase;
	}

	public void setStartLocationInCodeDatabase(int startLocationInCodeDatabase) {
		this.startLocationInCodeDatabase = startLocationInCodeDatabase;
	}

	public int getEndLocationInCodeDatabase() {
		return endLocationInCodeDatabase;
	}

	public void setEndLocationInCodeDatabase(int endLocationInCodeDatabase) {
		this.endLocationInCodeDatabase = endLocationInCodeDatabase;
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
			ASTParser parser = ASTParser.newParser(AST.JLS3);
		    parser.setKind(ASTParser.K_COMPILATION_UNIT);
		    parser.setSource(text.toCharArray());
		    parser.setResolveBindings(true);
		    CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		    ASTNode block = NodeFinder.perform(cu, startOffset, length);
		    MethodDeclaration parent = (MethodDeclaration) block.getParent();
		    startOffset = parent.getName().getStartPosition();
		    length = parent.getLength() + (parent.getStartPosition() - startOffset);
			text = text.subSequence(startOffset, startOffset + length).toString();
		}
		return text;
	}

	private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
