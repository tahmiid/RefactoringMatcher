package ca.concordia.refactoringmatcher;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitService;

import gr.uom.java.xmi.decomposition.ASTInformation;

public class Code implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String commit;
	private String filePath;
	private int startOffset;
	private int length;
	private String text;
	private String methodName;
	
	public Code(String commit, Path directory, ASTInformation astInformation, GitService gitService, Repository repository) throws Exception {
		this.commit = new String( commit.toCharArray() );
		this.filePath = new String( directory + "/" + astInformation.getFilePath() );
		this.startOffset = new Integer( astInformation.getStartOffset() );
		this.length = new Integer( astInformation.getLength() );
		this.text = extractText(gitService, repository);
	}

	private String extractText(GitService gitService, Repository repository) throws Exception {
		String text;
		gitService.checkout(repository, commit);
		text = readFile(filePath, StandardCharsets.UTF_8);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(text.toCharArray());
		parser.setResolveBindings(true);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		ASTNode block = NodeFinder.perform(cu, startOffset, length);
		MethodDeclaration parent = (MethodDeclaration) block.getParent();
		methodName = parent.getName().toString();
		startOffset = parent.getName().getStartPosition();
		length = parent.getLength() + (parent.getStartPosition() - startOffset);
		text = text.subSequence(startOffset, startOffset + length).toString() + "\n";
		return text;
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

	public String getText() {
		return text;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public boolean equals (Code code)
	{
		if(code.methodName.equals(methodName) && code.filePath.equals(filePath) && code.commit.equals(commit))
			return true;
		else 
			return false;
	}

}
