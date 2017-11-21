package ca.concordia.java.ast.decomposition.cfg;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NodeFinder;

public class Groum extends Graph {

	public Groum(PDG pdg) {
		for (GraphNode graphNode : pdg.nodes) {
			PDGNode pdgNode = (PDGNode) graphNode;
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setSource(pdgNode.toString().toCharArray());
			parser.setResolveBindings(true);
			CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
			ASTNode node = NodeFinder.perform(compilationUnit, 0, pdgNode.toString().toCharArray().length-1);
//			MethodDeclaration methodDeclaration = (MethodDeclaration) node;
			System.out.println(node.getNodeType());
			
			processNode(node);
		}
	}

	private void processNode(ASTNode node) {
		
	}

}
