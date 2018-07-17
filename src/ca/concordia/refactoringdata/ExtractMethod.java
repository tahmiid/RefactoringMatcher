package ca.concordia.refactoringdata;

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
import org.refactoringminer.api.Refactoring;
import ca.concordia.java.ast.ConstructorObject;
import ca.concordia.java.ast.MethodObject;
import ca.concordia.java.ast.decomposition.cfg.PDG;
import ca.concordia.refactoringmatcher.Cache;
import ca.concordia.refactoringmatcher.ExtendedGitService;
import gr.uom.java.xmi.LocationInfo;
import gr.uom.java.xmi.UMLOperation;
import gr.uom.java.xmi.diff.ExtractOperationRefactoring;

public class ExtractMethod implements IRefactoringData, Serializable {
	private static final long serialVersionUID = 1L;
	
	ExtractOperationRefactoring refactoring;
	String projectLink;
	String commitId;

	private String extractedMethodCode;
//	private String sourceMethodBeforeExtractionCode;
	private String sourceMethodAfterExtractionCode;
	
	private PDG extractedMethodPDG;
//	private PDG sourceMethodBeforeExtractionPDG;
	private PDG sourceMethodAfterExtractionPDG;
	
	public ExtractMethod (ExtractOperationRefactoring refactoring, String projectLink, String commitId, Repository repository, ExtendedGitService gitService) throws IOException, Exception {
		this.refactoring = refactoring;
		this.projectLink = projectLink;
		this.commitId = commitId;

		try {
//			Cache.currentFileText = getWholeTextFromFile(refactoring.getExtractedOperation().getLocationInfo(), gitService, repository, commitId);
//			this.extractedMethodCode = extractText(refactoring.getExtractedOperation().getLocationInfo());
//			Cache.currentFileText = getWholeTextFromFile(refactoring.getExtractedOperation().getLocationInfo(), gitService, repository, commitId);
////			this.extractedMethodPDG = new PDG(createMethodObject(getMethodDeclaration(Cache.currentFileText, refactoring.getExtractedOperation().getLocationInfo())));
//			Cache.currentFileText = getWholeTextFromFile(refactoring.getExtractedOperation().getLocationInfo(), gitService, repository, commitId);
//			this.sourceMethodAfterExtractionCode = extractText(refactoring.getSourceOperationAfterExtraction().getLocationInfo());
//			Cache.currentFileText = getWholeTextFromFile(refactoring.getExtractedOperation().getLocationInfo(), gitService, repository, commitId);
//			this.sourceMethodAfterExtractionPDG = new PDG(createMethodObject(getMethodDeclaration(Cache.currentFileText, refactoring.getSourceOperationAfterExtraction().getLocationInfo())));
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			throw e;
		}	
	}
	
	private MethodObject createMethodObject(MethodDeclaration methodDeclaration) {
		final ConstructorObject constructorObject = new ConstructorObject(methodDeclaration);
		MethodObject methodObject = new MethodObject(constructorObject);
		return methodObject;
	}
	
	private String extractText(LocationInfo locationInfo) throws IOException, Exception {
		String wholeText = Cache.currentFileText;
		MethodDeclaration methodDeclaration = getMethodDeclaration(wholeText, locationInfo);
		int absoluteStartOffset = methodDeclaration.getName().getStartPosition();
		int absoluteLength = methodDeclaration.getLength()
				+ (methodDeclaration.getStartPosition() - absoluteStartOffset);
		String text = wholeText.subSequence(absoluteStartOffset, absoluteStartOffset + absoluteLength).toString()
				+ "\n";
		return text;
	}
	
	public MethodDeclaration getMethodDeclaration(String wholeText, LocationInfo locationInfo)
			throws IOException, Exception {
		MethodDeclaration methodDeclaration;
		try {
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setSource(wholeText.toCharArray());
			parser.setResolveBindings(true);
			CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
			ASTNode block = NodeFinder.perform(compilationUnit, locationInfo.getStartOffset(), locationInfo.getLength());
			methodDeclaration = (MethodDeclaration) block;
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			throw e;
		}
		return methodDeclaration;
	}
	
	private String getWholeTextFromFile(LocationInfo locationInfo, ExtendedGitService gitService, Repository repository, String commitId) throws IOException, Exception {
		if (!repository.getFullBranch().equals(commitId))
			gitService.checkout(repository, commitId);
		String wholeText = readFile(new String(repository.getDirectory().getAbsolutePath().replaceAll("\\.git", "") + "/" + locationInfo.getFilePath()), StandardCharsets.UTF_8);
		return wholeText;
	}
	
	private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public Refactoring getRefactoring() {
		return refactoring;
	}

	public String getCommitId() {
		return commitId;
	}

	public String getProjectLink() {
		return projectLink;
	}
	
	public String getExtractedOperationCode() {
		return extractedMethodCode;
	}

//	public String getSourceOperationBeforeExtractionCode() {
//		return sourceMethodBeforeExtractionCode;
//	}

	public String getSourceOperationAfterExtractionCode() {
		return sourceMethodAfterExtractionCode;
	}
	
	public PDG getExtractedOperationPDG() {
		return extractedMethodPDG;
	}

//	public PDG getSourceOperationBeforeExtractionPDG() {
//		return sourceMethodBeforeExtractionPDG;
//	}

	public PDG getSourceOperationAfterExtractionPDG() {
		return sourceMethodAfterExtractionPDG;
	}
}
