package ca.concordia.refactoringmatcher;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import ca.concordia.java.ast.ConstructorObject;
import ca.concordia.java.ast.MethodObject;
import ca.concordia.java.ast.decomposition.cfg.CFG;
import ca.concordia.java.ast.decomposition.cfg.PDG;

public class PDGTester {

	public static void main(String[] args) throws IOException {
		testFile();
	}

	private static void testFile() throws IOException {   
		String path = "E:\\Repositories\\RefactoringMatcher\\src\\ca\\concordia\\refactoringmatcher\\TestClass.java";
		String wholeText = readFile(path,
				StandardCharsets.UTF_8);
		Cache.currentFile = path;
		Cache.currentFileText = wholeText;

		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(wholeText.toCharArray());
		parser.setResolveBindings(true);
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);

		compilationUnit.accept(new ASTVisitor() {

			public boolean visit(MethodDeclaration methodDeclaration) {
				System.out.println(methodDeclaration.toString());
				MethodObject methodObject = createMethodObject(methodDeclaration);
				CFG cfg = new CFG(methodObject);
				PDG pdg = new PDG(cfg);
				System.out.println();
				return false;
			}
		});
	}

	private static MethodObject createMethodObject(MethodDeclaration methodDeclaration) {
		final ConstructorObject constructorObject = new ConstructorObject(methodDeclaration);
		MethodObject methodObject = new MethodObject(constructorObject);
		return methodObject;
	}

	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
