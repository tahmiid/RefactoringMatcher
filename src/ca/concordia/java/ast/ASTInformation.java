package ca.concordia.java.ast;

import java.io.Serializable;

import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NodeFinder;

import ca.concordia.refactoringmatcher.Cache;

public class ASTInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ITypeRoot iTypeRoot;
	private int startPosition;
	private int length;
	private int nodeType;
	private volatile int hashCode = 0;
//	private ASTNode astNode;

	public ASTInformation(ITypeRoot iTypeRoot, ASTNode astNode) {
		this.iTypeRoot = iTypeRoot;
		this.startPosition = astNode.getStartPosition();
		this.length = astNode.getLength();
		this.nodeType = astNode.getNodeType();
//		this.astNode = astNode;
	}

	public ASTNode recoverASTNode() {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(Cache.currentFileText.toCharArray());
		parser.setResolveBindings(true);
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
		ASTNode block = NodeFinder.perform(compilationUnit, startPosition, length);
		// CompilationUnit compilationUnit =
		// CompilationUnitCache.getInstance().getCompilationUnit(iTypeRoot);
		// ASTNode astNode = NodeFinder.perform(compilationUnit, startPosition,
		// length);
		return block;
	}

	public ITypeRoot getITypeRoot() {
		return iTypeRoot;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public int getLength() {
		return length;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o instanceof ASTInformation) {
			ASTInformation astInformation = (ASTInformation) o;
			return this.iTypeRoot.equals(astInformation.iTypeRoot) && this.startPosition == astInformation.startPosition
					&& this.length == astInformation.length && this.nodeType == astInformation.nodeType;
		}
		return false;
	}

	public int hashCode() {
		if (hashCode == 0) {
			int result = 17;
			// result = 37*result + iTypeRoot.hashCode();
			result = 37 * result + startPosition;
			result = 37 * result + length;
			result = 37 * result + nodeType;
			result = 37 * result + recoverASTNode().hashCode();
			hashCode = result;
		}
		return hashCode;
	}
}
