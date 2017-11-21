package ca.concordia.refactoringmatcher;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitService;

import gr.uom.java.xmi.LocationInfo;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

	public Code(Commit commit, Path directory, LocationInfo astInformation, ExtendedGitService gitService,
			Repository repository) throws Exception {
		this.commit = commit;
		this.filePath = new String(directory + "/" + astInformation.getFilePath());
		this.startOffset = new Integer(astInformation.getStartOffset());
		this.length = new Integer(astInformation.getLength());
		this.text = extractText(gitService, repository);
	}

	private String extractText(ExtendedGitService gitService, Repository repository) throws Exception {
		gitService.checkout(repository, commit.getId());
		String wholeText = getWholeTextFromFile(gitService, repository);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(wholeText.toCharArray());
		parser.setResolveBindings(true);
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
		ASTNode block = NodeFinder.perform(compilationUnit, startOffset, length);
		MethodDeclaration methodDeclaration = (MethodDeclaration) block;
		this.methodName = extractMethodSignature(methodDeclaration);
		int absoluteStartOffset = methodDeclaration.getName().getStartPosition();
		int absoluteLength = methodDeclaration.getLength()
				+ (methodDeclaration.getStartPosition() - absoluteStartOffset);
		String text = wholeText.subSequence(absoluteStartOffset, absoluteStartOffset + absoluteLength).toString()
				+ "\n";
		return text;
	}

	public MethodDeclaration getMethodDeclaration(GitService gitService, Repository repository)
			throws IOException, Exception {
		String wholeText = getWholeTextFromFile(gitService, repository);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(wholeText.toCharArray());
		parser.setResolveBindings(true);
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
		ASTNode block = NodeFinder.perform(compilationUnit, startOffset, length);
		// MethodDeclaration methodDeclaration = (MethodDeclaration)
		// block.getParent();
		MethodDeclaration methodDeclaration = (MethodDeclaration) block;
		return methodDeclaration;
	}

	private String getWholeTextFromFile(GitService gitService, Repository repository) throws IOException, Exception {
		if (!repository.getFullBranch().equals(commit.getId()))
			gitService.checkout(repository, commit.getId());
		String wholeText = readFile(filePath, StandardCharsets.UTF_8);
		return wholeText;
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

	public String getCommitShort() {
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
		try {
			if (code.methodName.equals(methodName) && code.filePath.equals(filePath)
					&& code.commit.getId().equals(commit.getId()))
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	public String getMethodBody() {
		return text.substring(text.indexOf('{'));
	}

	public String toString() {
		return methodName + " in " + getFileName();
	}

}
