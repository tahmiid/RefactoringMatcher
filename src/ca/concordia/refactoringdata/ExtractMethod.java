package ca.concordia.refactoringdata;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.Refactoring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.concordia.java.ast.ConstructorObject;
import ca.concordia.java.ast.MethodObject;
import ca.concordia.java.ast.decomposition.cfg.CFG;
import ca.concordia.java.ast.decomposition.cfg.Graph;
import ca.concordia.java.ast.decomposition.cfg.PDG;
import ca.concordia.refactoringmatcher.Cache;
import ca.concordia.refactoringmatcher.ExtendedGitServiceImpl;
import gr.uom.java.xmi.LocationInfo;
import gr.uom.java.xmi.diff.ExtractOperationRefactoring;

public class ExtractMethod implements IRefactoringData, Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(ExtractMethod.class);

	ExtractOperationRefactoring refactoring;
	String projectLink;
	String previousCommitId;
	String commitId;

	private String extractedMethodCode;
	private String sourceMethodBeforeExtractionCode;
	private String sourceMethodAfterExtractionCode;

	private Graph extractedMethodPDG;
	private Graph sourceMethodBeforeExtractionPDG;
	private Graph sourceMethodAfterExtractionPDG;

	public ExtractMethod(ExtractOperationRefactoring refactoring, String projectLink, String commitId, Repository repository) throws IOException, Exception {
		this.refactoring = refactoring;
		this.projectLink = projectLink;
		this.commitId = commitId;
		this.previousCommitId = new ExtendedGitServiceImpl().getParentCommit(repository, commitId);
		// retrieveCode(repository);
	}

	public void retrieveCode(Repository repository) throws Exception {
		try {
			Cache.currentFile = refactoring.getExtractedOperation().getLocationInfo().getFilePath();
			Cache.currentFileText = getWholeTextFromFile(refactoring.getExtractedOperation().getLocationInfo(),
					repository, commitId);
			this.extractedMethodCode = extractText(refactoring.getExtractedOperation().getLocationInfo());
			this.extractedMethodPDG = new PDG(createMethodObject(getMethodDeclaration(Cache.currentFileText,
					refactoring.getExtractedOperation().getLocationInfo())));
		} catch (Exception e) {
			logger.error("Could not retrieve info about the extracted method! " + toString());
			e.printStackTrace();
		}
		try {
			Cache.currentFile = refactoring.getSourceOperationAfterExtraction().getLocationInfo().getFilePath();
			Cache.currentFileText = getWholeTextFromFile(
					refactoring.getSourceOperationAfterExtraction().getLocationInfo(), repository, commitId);
			this.sourceMethodAfterExtractionCode = extractText(
					refactoring.getSourceOperationAfterExtraction().getLocationInfo());
			this.sourceMethodAfterExtractionPDG = new PDG(createMethodObject(getMethodDeclaration(Cache.currentFileText,
					refactoring.getSourceOperationAfterExtraction().getLocationInfo())));
		} catch (Exception e) {
			logger.error("Could not retrieve info about the source method after extraction! " + toString());
			e.printStackTrace();
		}
		try {
			Cache.currentFile = refactoring.getSourceOperationBeforeExtraction().getLocationInfo().getFilePath();
			Cache.currentFileText = getWholeTextFromFile(
					refactoring.getSourceOperationBeforeExtraction().getLocationInfo(), repository, previousCommitId);
			this.sourceMethodBeforeExtractionCode = extractText(
					refactoring.getSourceOperationBeforeExtraction().getLocationInfo());
			this.sourceMethodBeforeExtractionPDG = new PDG(createMethodObject(getMethodDeclaration(
					Cache.currentFileText, refactoring.getSourceOperationBeforeExtraction().getLocationInfo())));
		} catch (Exception e) {
			logger.error("Could not retrieve info about the source method before extraction! " + toString());
			e.printStackTrace();
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
		String text = methodDeclaration.toString();
		return text;
	}

	public MethodDeclaration getMethodDeclaration(String wholeText, LocationInfo locationInfo)
			throws IOException, Exception {
		MethodDeclaration methodDeclaration;
		try {
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			Map options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
			parser.setCompilerOptions(options);
			parser.setResolveBindings(false);
			parser.setEnvironment(new String[0], new String[] { Cache.currentFile }, null, false);
			parser.setSource(wholeText.toCharArray());
			parser.setResolveBindings(true);
			CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
			ASTNode block = NodeFinder.perform(compilationUnit, locationInfo.getStartOffset(),
					locationInfo.getLength());
			methodDeclaration = (MethodDeclaration) block;
		} catch (Exception e) {
			logger.error("Can not extract method from file.");
			throw e;
		}
		return methodDeclaration;
	}

	private String getWholeTextFromFile(LocationInfo locationInfo, Repository repository, String commitId)
			throws IOException, Exception {
		try {
			if (!repository.getFullBranch().equals(commitId))
				new ExtendedGitServiceImpl().checkout(repository, commitId);

			String wholeText = readFile(new String(repository.getDirectory().getAbsolutePath().replaceAll("\\.git", "")
					+ "/" + locationInfo.getFilePath()), StandardCharsets.UTF_8);
			return wholeText;
		} catch (Exception e) {
			logger.error("Could not read file: " + locationInfo.getFilePath());
			throw e;
		}
	}

	private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}

	public Refactoring getRefactoring() {
		return refactoring;
	}

	public String getCommitId() {
		return commitId;
	}

	public String getCommitIdShort() {
		if (commitId != null && commitId.length() > 6)
			return commitId.substring(0, 5);
		else
			return commitId;
	}

	public String getPreviousCommitId() {
		return previousCommitId;
	}

	public String getPreviousCommitIdShort() {
		if (previousCommitId != null && previousCommitId.length() > 6)
			return previousCommitId.substring(0, 6);
		else
			return previousCommitId;
	}

	public String getProjectLink() {
		return projectLink;
	}

	public String getExtractedOperationCode() {
		return extractedMethodCode;
	}

	public String getSourceOperationBeforeExtractionCode() {
		return sourceMethodBeforeExtractionCode;
	}

	public String getSourceOperationAfterExtractionCode() {
		return sourceMethodAfterExtractionCode;
	}

	public Graph getExtractedOperationPDG() {
		return extractedMethodPDG;
	}

	public Graph getSourceOperationBeforeExtractionPDG() {
		return sourceMethodBeforeExtractionPDG;
	}

	public Graph getSourceOperationAfterExtractionPDG() {
		return sourceMethodAfterExtractionPDG;
	}

	public boolean isPartofSameRefactoring(ExtractMethod extractMethod) {
		if (extractMethod.refactoring.getExtractedOperation().getClassName()
				.equals(refactoring.getExtractedOperation().getClassName())
				&& extractMethod.refactoring.getExtractedOperation().getName()
						.equals(refactoring.getExtractedOperation().getName())
				&& extractMethod.getProjectLink().equals(getProjectLink())
				&& extractMethod.getCommitIdShort().equals(getCommitIdShort())) {
			return true;
		}
		return false;
	}

	public String toString() {
		return refactoring.getName() + ": " + refactoring.getExtractedOperation().getName() + " ["
				+ refactoring.getExtractedOperation().statementCount() + "] ["
				+ refactoring.getExtractedOperation().getClassName() + "] " + getProjectName() + " ("
				+ getCommitIdShort() + ")";
	}

	public int getExtractionSize() {
		return refactoring.getExtractedOperation().statementCount();
	}

	public String getProjectName() {
		int splitIndex1 = projectLink.lastIndexOf('/');
		projectLink = projectLink.substring(splitIndex1 + 1);
		int splitIndex2 = projectLink.lastIndexOf('.');
		if (splitIndex2 != -1) {
			projectLink = projectLink.substring(0, splitIndex2);
		}
		return projectLink;
	}

	@Override
	public void prepareForSerialization() {
		if(extractedMethodPDG!=null)
			extractedMethodPDG.removeCyclicReferences();
		if(sourceMethodAfterExtractionPDG!=null)
			sourceMethodAfterExtractionPDG.removeCyclicReferences();
		if(sourceMethodBeforeExtractionPDG!=null)
			sourceMethodBeforeExtractionPDG.removeCyclicReferences();
	}

	@Override
	public void recoverAfterDeserialization() {
		if(extractedMethodPDG!=null)
			extractedMethodPDG.recoverCyclicReferences(extractedMethodPDG);
		if(sourceMethodAfterExtractionPDG!=null)
			sourceMethodAfterExtractionPDG.recoverCyclicReferences(sourceMethodAfterExtractionPDG);
		if(sourceMethodBeforeExtractionPDG!=null)
			sourceMethodBeforeExtractionPDG.recoverCyclicReferences(sourceMethodBeforeExtractionPDG);
	}
}
