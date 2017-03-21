package gr.uom.java.ast;

import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.dom.ASTNode;

public class ASTInformation {

	private ITypeRoot iTypeRoot;
	private int startPosition;
	private int length;
	private int nodeType;
	private volatile int hashCode = 0;
	private ASTNode astNode;
	
	public ASTInformation(ITypeRoot iTypeRoot, ASTNode astNode) {
		this.iTypeRoot = iTypeRoot;
		this.startPosition = astNode.getStartPosition();
		this.length = astNode.getLength();
		this.nodeType = astNode.getNodeType();
		this.astNode = astNode;
	}

	public ASTNode recoverASTNode() {
//        CompilationUnit compilationUnit = CompilationUnitCache.getInstance().getCompilationUnit(iTypeRoot);
//        ASTNode astNode = NodeFinder.perform(compilationUnit, startPosition, length);
		return astNode;
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
		if(this == o) {
			return true;
		}
		
		if(o instanceof ASTInformation) {
			ASTInformation astInformation = (ASTInformation)o;
			return this.iTypeRoot.equals(astInformation.iTypeRoot) &&
					this.startPosition == astInformation.startPosition &&
					this.length == astInformation.length &&
					this.nodeType == astInformation.nodeType;
		}
		return false;
	}
	
	public int hashCode() {
		if(hashCode == 0) {
			int result = 17;
			result = 37*result + iTypeRoot.hashCode();
			result = 37*result + startPosition;
			result = 37*result + length;
			result = 37*result + nodeType;
		}
		return hashCode;
	}
}
