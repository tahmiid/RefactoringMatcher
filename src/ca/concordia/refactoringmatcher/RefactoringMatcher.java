package ca.concordia.refactoringmatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.api.RefactoringType;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.refactoringminer.util.GitServiceImpl;

import gr.uom.java.xmi.decomposition.ASTInformation;
import gr.uom.java.xmi.diff.ExtractAndMoveOperationRefactoring;
import gr.uom.java.xmi.diff.ExtractOperationRefactoring;
import gr.uom.java.xmi.diff.InlineOperationRefactoring;
import gr.uom.java.xmi.diff.PullUpOperationRefactoring;
import gr.uom.java.xmi.diff.PushDownOperationRefactoring;

public class RefactoringMatcher {
	public static void main(String[] args) throws Exception {
//		String projectLink = "https://github.com/danilofes/refactoring-toy-example.git"; 
		String projectLink = "https://github.com/junit-team/junit5";
//		String projectLink = "https://github.com/jfree/jfreechart.git";
		
		
		String projectName = getProjectName(projectLink);
		String clonePath = "tmp/" + projectName;
		String parentCodeDir = "output/parentCode";
		String refactoredCodeDir = "output/refactoredCode";
//		String refactoringDataDir = "output/refactoringData";
		String parentCodePath = parentCodeDir + "/" + projectName + ".java";
		String refactoredCodePath = refactoredCodeDir + "/" + projectName + ".java";

		Files.createDirectories(Paths.get(parentCodeDir)); 
		Files.createDirectories(Paths.get(refactoredCodeDir)); 
		//Files.createDirectories(Paths.get(refactoringDataDir)); 
		
		new File(parentCodePath).createNewFile();
		new File(refactoredCodePath).createNewFile();
		PrintWriter writer = new PrintWriter(parentCodePath);
		writer.print("");
		writer.close();
		writer = new PrintWriter(refactoredCodePath);
		writer.print("");
		writer.close();
		
		
		GitService gitService = new GitServiceImpl();
		Repository repo = gitService.cloneIfNotExists(clonePath, projectLink);		
		List<RefactoringData> allRefactoringData = getAllRefactoringData(clonePath, repo);	//		allRefactoringData = loadRefactoringDataFromFile(projectName, clonePath, refactoringDataDir, repo);
		
		Code code;
		String text;
		
		int parentLineCount = 1, refactoredLineCount = 1;
		for (RefactoringData refactoringData : allRefactoringData) {
			
			code =  refactoringData.getParentCode();
			text = code.extractSourceCode(gitService,repo) + "\n";
			addToCodeDatabase(parentCodePath, text);
			
			code.setStartLocationInCodeDatabase(parentLineCount); 
			parentLineCount += countLines(text);
			code.setEndLocationInCodeDatabase(parentLineCount - 1);
			
			code = refactoringData.getRefactoredCode();
			text = code.extractSourceCode(gitService,repo)+ "\n";
			addToCodeDatabase(refactoredCodePath, text);
			
			code.setStartLocationInCodeDatabase(refactoredLineCount); 
			refactoredLineCount += countLines(text);
			code.setEndLocationInCodeDatabase(refactoredLineCount - 1);
			
			System.out.println(refactoringData);
		}
		
//		SourceCCParser sccParser = new SourceCCParser (allRefactoringData, parentCodeDir, refactoredCodeDir);
	}

	private static int countLines(String str){
		   String[] lines = str.split("\r\n|\r|\n");
		   return  lines.length;
	}
	
	
	private static void addToCodeDatabase(String parentCodePath, String text) {
	
		try {				 
		    Files.write(Paths.get(parentCodePath), text.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
			System.out.println(e);
		}
	}

	private static List<RefactoringData> loadRefactoringDataFromFile(String projectName, String clonePath,
			String refactoringDataDir, Repository repo)
			throws FileNotFoundException, IOException, ClassNotFoundException, Exception {
		List<RefactoringData> allRefactoringData;
		if(Files.exists(Paths.get(refactoringDataDir + "/" + projectName)))
		{
			FileInputStream fis = new FileInputStream(refactoringDataDir + "/" + projectName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			allRefactoringData = (List<RefactoringData>) ois.readObject();
			ois.close();
		}
		else
		{
			allRefactoringData = getAllRefactoringData(clonePath, repo);
			
			FileOutputStream fos = new FileOutputStream(refactoringDataDir + "/" + projectName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(allRefactoringData);
			oos.close();
		}
		return allRefactoringData;
	}

	private static String getProjectName(String projectLink) {
		int splitIndex1 = projectLink.lastIndexOf('/');
		projectLink = projectLink.substring(splitIndex1+1);
		int splitIndex2 = projectLink.lastIndexOf('.');
		if (splitIndex2 != -1) {
			projectLink = projectLink.substring(0, splitIndex2);
		}

		return projectLink;
	}
	
	private static List<RefactoringData> getAllRefactoringData(String clonePath, Repository repo) throws Exception {
		List<RefactoringData> allRefactoringData = new ArrayList<RefactoringData>();

		GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();
		miner.detectAll(repo, "master", new RefactoringHandler() {
			@Override
			public void handle(RevCommit commitData, List<Refactoring> refactorings) {
				for (Refactoring ref : refactorings) {

					ASTInformation astBeforeChange; 
					ASTInformation astAfterChange;

					/*if (ref.getRefactoringType() == RefactoringType.PULL_UP_OPERATION) {
						astBeforeChange = ((PullUpOperationRefactoring) ref).getOriginalOperation().getBody().getCompositeStatement().getAstInformation();
						astAfterChange = ((PullUpOperationRefactoring) ref).getMovedOperation().getBody().getCompositeStatement().getAstInformation();
					} else if (ref.getRefactoringType() == RefactoringType.PUSH_DOWN_OPERATION) {
						astBeforeChange = ((PushDownOperationRefactoring) ref).getOriginalOperation().getBody().getCompositeStatement().getAstInformation();
						astAfterChange = ((PushDownOperationRefactoring) ref).getMovedOperation().getBody().getCompositeStatement().getAstInformation();
					} else */ if (ref.getRefactoringType() == RefactoringType.INLINE_OPERATION) {
						astBeforeChange = ((InlineOperationRefactoring) ref).getInlinedOperation().getBody().getCompositeStatement().getAstInformation();
						astAfterChange = ((InlineOperationRefactoring) ref).getInlinedToOperation().getBody().getCompositeStatement().getAstInformation();
					} /*else  if (ref.getRefactoringType() == RefactoringType.EXTRACT_OPERATION) {
						astBeforeChange = ((ExtractOperationRefactoring) ref).getExtractedFromOperation().getBody().getCompositeStatement().getAstInformation();
						astAfterChange = ((ExtractOperationRefactoring) ref).getExtractedOperation().getBody().getCompositeStatement().getAstInformation();
					} else  if (ref.getRefactoringType() == RefactoringType.EXTRACT_AND_MOVE_OPERATION) {
						astBeforeChange = ((ExtractAndMoveOperationRefactoring) ref).getExtractedFromOperation().getBody().getCompositeStatement().getAstInformation();
						astAfterChange = ((ExtractAndMoveOperationRefactoring) ref).getExtractedOperation().getBody().getCompositeStatement().getAstInformation();
					} */else {
						continue;
					}

					Code parentCode = new Code(commitData.getParent(0).getName(), clonePath + "/" + astBeforeChange.getFilePath(), astBeforeChange.getStartOffset(), astBeforeChange.getLength());
					Code refactoredCode = new Code(commitData.getName(), clonePath + "/" + astAfterChange.getFilePath(), astAfterChange.getStartOffset(), astAfterChange.getLength());
					
					RefactoringData refactoringData = new RefactoringData(ref, parentCode, refactoredCode);
					allRefactoringData.add(refactoringData);
				}
			}
		});
		return allRefactoringData;
	}
}
