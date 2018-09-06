package ca.concordia.refactoringmatcher;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ca.concordia.jaranalyzer.ClassInfo;
import ca.concordia.jaranalyzer.JarAnalyzer;
import ca.concordia.jaranalyzer.JarInfo;
import ca.concordia.jaranalyzer.MethodInfo;

public class Release  implements Serializable{
	public Release(Ref ref, ExtendedGitService gitService, Repository repository, Path directory) throws Exception {
		commitId = ref.getObjectId().getName();
		gitService.checkout(repository, commitId);
		tag = ref.getName();
		if (Files.exists(Paths.get(directory.toString() + "\\pom.xml"))) {
			System.out.println("Pom found");
			getReleaseDetailFromPom(directory.toString() + "\\pom.xml");

			System.out.println("groupId : " + groupId);
			System.out.println("artifactId : " + artifactId);
			System.out.println("version : " + version);
			jarUrl = "http://central.maven.org/maven2/" + groupId.replace(".", "/") + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".jar";
			System.out.println("jarUrl : " + jarUrl);
			System.out.println(tag);
			
			JarAnalyzer jarAnalyzer = new JarAnalyzer();
			jarInfo = jarAnalyzer.AnalyzeJar(jarUrl, "", "", "");
		}

	}

	private void getReleaseDetailFromPom(String pomLocation)
			throws ParserConfigurationException, SAXException, IOException {
		File inputFile = new File(pomLocation);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("project");
		Node nNode = nList.item(0);
		try {
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				groupId = eElement.getElementsByTagName("groupId").item(0).getTextContent().replaceAll(" ", "").replaceAll("\n", "").replaceAll("\r", "");
				Node cNode = eElement.getFirstChild();
				while (cNode.getNextSibling() != null) {
					cNode = cNode.getNextSibling();
					if (cNode.getNodeType() == Node.ELEMENT_NODE) {
						Element cElement = (Element) cNode;
						if (cElement.getTagName().equals("artifactId"))
							artifactId = cElement.getTextContent().replaceAll(" ", "").replaceAll("\n", "").replaceAll("\r", "");
						if (cElement.getTagName().equals("version"))
							version = cElement.getTextContent().replaceAll(" ", "").replaceAll("\n", "").replaceAll("\r", "");
					}
				}
			}
		} catch (Exception e) {

		}
	}

	private String tag;
	private String commitId;
	private String version;
	private String artifactId;
	private String groupId;
	private String jarUrl;
	private JarInfo jarInfo;
	
	
	public JarInfo getJarInfo() {
		return jarInfo;
	}

	public String getTag() {
		return tag;
	}

	public String getVersion() {
		return version;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getJarUrl() {
		return jarUrl;
	}

	public String getCommitId() {
		return commitId;
	}

	public boolean contains(MethodInvocation methodInvocation) {
		CompilationUnit cu = getRoot(methodInvocation);
		List<String> imports = new ArrayList<String>();
		for (Object imp : cu.imports()) {
			ImportDeclaration importDeclaration = (ImportDeclaration)imp;
			imports.add(importDeclaration.getName().toString());
		}
		
		for (ClassInfo classInfo : jarInfo.getClasses()) {
			for (String string : imports) {
				if(classInfo.getName().contains(string)){
					for (MethodInfo methodInfo : classInfo.getMethods()) {
						if(methodInfo.getName().contains(methodInvocation.getName().toString()))
						{
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	private CompilationUnit getRoot(ASTNode element) {
		if(element.getParent() == null)
			return (CompilationUnit) element;
		else 
			return getRoot(element.getParent());
	}
	
}
