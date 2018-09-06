package ca.concordia.refactoringmatcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.refactoringminer.api.Refactoring;

import com.thoughtworks.xstream.XStream;

import ca.concordia.java.ast.ConstructorObject;
import ca.concordia.java.ast.MethodObject;
import ca.concordia.java.ast.decomposition.cfg.CFG;
import ca.concordia.java.ast.decomposition.cfg.GraphEdge;
import ca.concordia.java.ast.decomposition.cfg.GraphNode;
import ca.concordia.java.ast.decomposition.cfg.PDG;
import ca.concordia.java.ast.decomposition.cfg.PDGNode;
import ca.concordia.refactoringdata.IRefactoringData;

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
				XStream xstream = new XStream();
				String xml = null;
				try {
					for (GraphEdge edge : pdg.getEdges()) {
						edge.removeCyclicReferences();
					}
					serialize(pdg);
					xml = xstream.toXML(pdg);
					try (PrintStream out = new PrintStream(new FileOutputStream("xml"))) {
					    out.print(xml);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					pdg = (PDG) xstream.fromXML(xml);
					pdg = deserialize();
					for (GraphEdge edge : pdg.getEdges()) {
						edge.recoverCyclicReferences(pdg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println();
				return false;
			}
		});
	}
	
	private static PDG deserialize() throws Exception {
		PDG object = null;
		String path = "object";
		if(Files.exists(Paths.get(path))) {
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			try {
				object = (PDG) ois.readObject();
			} catch (Exception e) {
				ois.close();
				fis.close();
			}
			ois.close();
			fis.close();
		}
		return object;
	}
	
	private static void serialize (PDG object) throws IOException {
		Files.deleteIfExists(Paths.get("object"));
		FileOutputStream fos = new FileOutputStream("object");
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.close();
			fos.close();
		} catch (IOException e) {
			oos.close();
			fos.close();
			Files.deleteIfExists(Paths.get("object"));
		}
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
