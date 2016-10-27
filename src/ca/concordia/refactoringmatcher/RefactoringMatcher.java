package ca.concordia.refactoringmatcher;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.api.RefactoringType;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.refactoringminer.util.GitServiceImpl;

import gr.uom.java.xmi.UMLOperation;
import gr.uom.java.xmi.diff.ExtractAndMoveOperationRefactoring;
import gr.uom.java.xmi.diff.ExtractOperationRefactoring;
import gr.uom.java.xmi.diff.InlineOperationRefactoring;
import gr.uom.java.xmi.diff.PullUpOperationRefactoring;
import gr.uom.java.xmi.diff.PushDownOperationRefactoring;
import gr.uom.java.xmi.diff.RenameOperationRefactoring;

public class RefactoringMatcher {

	public static void main(String[] args) throws Exception {
		GitService gitService = new GitServiceImpl();
		Repository repo = gitService.cloneIfNotExists("tmp/refactoring-toy-example",
				"https://github.com/danilofes/refactoring-toy-example.git");

		List<RefactoringData> allRefactoringData = getAllRefactoringData(repo);

		for (RefactoringData refactoringData : allRefactoringData) {
			System.out.println(refactoringData);
	
//			String parentCommit = refactoringData.getCommitData().getParent(0).getName();
//			gitService.checkout(repo, refactoringData.getCommitData().getId().getName());
//			String code = getFileText(refactoringData.getTo().getFilePath());
//			code = code.subSequence(refactoringData.getFrom().getStartOffset(), refactoringData.getFrom().getStartOffset() + refactoringData.getFrom().getLength()).toString();
//			System.out.println(code);
		}
	}

	private static String getFileText(String path) throws IOException, FileNotFoundException {
		try (BufferedReader br = new BufferedReader(new FileReader("tmp/refactoring-toy-example/" + path))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			return everything;
		}
	}

	private static List<RefactoringData> getAllRefactoringData(Repository repo) throws Exception {
		List<RefactoringData> allRefactoringData = new ArrayList<RefactoringData>();

		GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();
		miner.detectAll(repo, "master", new RefactoringHandler() {
			@Override
			public void handle(RevCommit commitData, List<Refactoring> refactorings) {
				for (Refactoring ref : refactorings) {

					UMLOperation umlOperationBeforeChange = null;
					UMLOperation umlOperationAfterChange = null;

					if (ref.getRefactoringType() == RefactoringType.RENAME_METHOD) {
						umlOperationBeforeChange = ((RenameOperationRefactoring) ref).getOriginalOperation();
						umlOperationAfterChange = ((RenameOperationRefactoring) ref).getRenamedOperation();
					} else if (ref.getRefactoringType() == RefactoringType.PULL_UP_OPERATION) {
						umlOperationBeforeChange = ((PullUpOperationRefactoring) ref).getOriginalOperation();
						umlOperationAfterChange = ((PullUpOperationRefactoring) ref).getMovedOperation();
					} else if (ref.getRefactoringType() == RefactoringType.PUSH_DOWN_OPERATION) {
						umlOperationBeforeChange = ((PushDownOperationRefactoring) ref).getOriginalOperation();
						umlOperationAfterChange = ((PushDownOperationRefactoring) ref).getMovedOperation();
					} else if (ref.getRefactoringType() == RefactoringType.INLINE_OPERATION) {
						umlOperationBeforeChange = ((InlineOperationRefactoring) ref).getInlinedOperation();
						umlOperationAfterChange = ((InlineOperationRefactoring) ref).getInlinedToOperation();
					} else if (ref.getRefactoringType() == RefactoringType.EXTRACT_OPERATION) {
						umlOperationBeforeChange = ((ExtractOperationRefactoring) ref).getExtractedFromOperation();
						umlOperationAfterChange = ((ExtractOperationRefactoring) ref).getExtractedOperation();
					} else if (ref.getRefactoringType() == RefactoringType.EXTRACT_AND_MOVE_OPERATION) {
						umlOperationBeforeChange = ((ExtractAndMoveOperationRefactoring) ref)
								.getExtractedFromOperation();
						umlOperationAfterChange = ((ExtractAndMoveOperationRefactoring) ref).getExtractedOperation();
					} else {
						continue;
					}

					RefactoringData refactoringData = new RefactoringData(commitData, ref,
							umlOperationBeforeChange.getBody().getCompositeStatement().getAstInformation(),
							umlOperationAfterChange.getBody().getCompositeStatement().getAstInformation());
					allRefactoringData.add(refactoringData);
				}
			}
		});
		return allRefactoringData;
	}
}
