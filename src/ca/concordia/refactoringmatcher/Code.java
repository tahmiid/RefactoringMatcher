package ca.concordia.refactoringmatcher;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitService;

import gr.uom.java.xmi.decomposition.ASTInformation;

public class Code implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Commit commit;
	private String filePath;
	private int startOffset;
	private int length;
	private String text;
	private String methodName;

	public Code(Commit commit, Path directory, ASTInformation astInformation, GitService gitService,
			Repository repository) throws Exception {
		this.commit = commit;
		this.filePath = new String(directory + "/" + astInformation.getFilePath());
		this.startOffset = new Integer(astInformation.getStartOffset());
		this.length = new Integer(astInformation.getLength());
		this.text = extractText(gitService, repository);
	}

	private String extractText(GitService gitService, Repository repository) throws Exception {
		String text;
		gitService.checkout(repository, commit.getId());
		text = readFile(filePath, StandardCharsets.UTF_8);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(text.toCharArray());
		parser.setResolveBindings(true);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		ASTNode block = NodeFinder.perform(cu, startOffset, length);
		MethodDeclaration parent = (MethodDeclaration) block.getParent();
		this.methodName = extractMethodSignature(parent);
		startOffset = parent.getName().getStartPosition();
		length = parent.getLength() + (parent.getStartPosition() - startOffset);
		text = text.subSequence(startOffset, startOffset + length).toString() + "\n";
		return text;
	}

	private String extractMethodSignature(MethodDeclaration parent) {
		String methodName = parent.getReturnType2() + " ";
		methodName += parent.getName().toString();
		List<SingleVariableDeclaration> parameters = parent.parameters();
		methodName = methodName + "(";
		boolean start = true;
		for (SingleVariableDeclaration parameter : parameters) {
			if (!start) {
				methodName = methodName + ", ";
			}
			start = false;
			methodName = methodName + parameter.getStructuralProperty(parameter.TYPE_PROPERTY);
		}
		return methodName = methodName + ")";
	}

	public Commit getCommit() {
		return commit;
	}
	
	public String getCommitShort()
	{
		return commit.getId().substring(0, 5);
	}

	public String getFileName() {
		return filePath.substring(filePath.lastIndexOf('/') + 1);
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

	public boolean equals(Code code) {
		if (code.methodName.equals(methodName) && code.filePath.equals(filePath) && code.commit.getId().equals(commit.getId()))
			return true;
		else
			return false;
	}
	
	public String toString(){
		return methodName + " in " + getFileName();
	}

}
