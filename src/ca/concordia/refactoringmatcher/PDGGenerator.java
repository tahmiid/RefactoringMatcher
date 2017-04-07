package ca.concordia.refactoringmatcher;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import ca.concordia.java.ast.ConstructorObject;
import ca.concordia.java.ast.MethodObject;
import ca.concordia.java.ast.decomposition.cfg.CFG;
import ca.concordia.java.ast.decomposition.cfg.PDG;

public class PDGGenerator {

	public PDG getPDG(MethodDeclaration methodDeclaration) {
		MethodObject methodObject = createMethodObject(methodDeclaration);
		CFG cfg = new CFG(methodObject);
		PDG pdg = new PDG(cfg);
		
		System.out.println(pdg.getNodes().size());
		System.out.println("Done");
		
		return pdg;
	}

	private MethodObject createMethodObject(MethodDeclaration methodDeclaration) {
		ConstructorObject constructorObject = new ConstructorObject(methodDeclaration);
		MethodObject methodObject = new MethodObject(constructorObject);
		return methodObject;
	}
}
