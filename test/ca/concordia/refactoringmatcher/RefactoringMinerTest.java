package ca.concordia.refactoringmatcher;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.refactoringminer.util.GitServiceImpl;

public class RefactoringMinerTest {
	
	public static void main(String[] args) {
		try {
			loadSerializedRefactorings();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	@Test
	public static List<Refactoring> detectAll() throws Exception {
		List<Refactoring> refactorings = new ArrayList<Refactoring>();
		try {
			GitService gitService = new GitServiceImpl();
			GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();

			Repository repo = gitService.cloneIfNotExists("tmp/refactoring-toy-example",
					"https://github.com/danilofes/refactoring-toy-example.git");

			miner.detectAll(repo, "master", new RefactoringHandler() {
				@Override
				public void handle(String commitId, List<Refactoring> refs) {
					refactorings.addAll(refs);
				}

				@Override
				public void onFinish(int refactoringsCount, int commitsCount, int errorCommitsCount) {
					System.out.println("onFinish() - " + refactoringsCount + " refactorings found");
				}
			});
			return refactorings;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Test
	public static void loadSerializedRefactorings() throws Exception {
		List<Refactoring> refactorings = new ArrayList<Refactoring>();
		String outputPathString = "tmp/refactoring-toy-example" + "/" + "refactorings";
		if (Files.exists(Paths.get(outputPathString))) {
			FileInputStream fis = new FileInputStream(outputPathString);
			ObjectInputStream ois = new ObjectInputStream(fis);
			try {
				refactorings = (List<Refactoring>) ois.readObject();
			} catch (Exception e) {
				refactorings = detectAll();
				writeToFile(refactorings);
			}
			ois.close();
		} else {
			refactorings = detectAll();
			writeToFile(refactorings);
		}
	}
	
	@Test
	public static void writeToFile(List<Refactoring> refactorings) throws IOException {
		Files.deleteIfExists(Paths.get("tmp/refactoring-toy-example" + "/" + "refactorings"));
		FileOutputStream fos = new FileOutputStream("tmp/refactoring-toy-example" + "/" + "refactorings");
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(fos);
			oos.writeObject(refactorings);
			oos.close();
		} catch (IOException e) {
			System.out.println("Failed to serialize");
			System.out.println(e);
		}

	}

	@Test
	public void detectBetweenCommits() throws Exception {
		try {
			GitService gitService = new GitServiceImpl();
			GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();

			Repository repo = gitService.cloneIfNotExists("tmp/refactoring-toy-example",
					"https://github.com/danilofes/refactoring-toy-example.git");

			miner.detectBetweenCommits(repo, "d4bce13a443cf12da40a77c16c1e591f4f985b47",
					"dde3ef036bdddae550c4e98373db4c81d77b5043", new RefactoringHandler() {
						@Override
						public void handle(RevCommit commitData, List<Refactoring> refactorings) {
							System.out.println("Refactorings at " + commitData.getId().getName());
							for (Refactoring ref : refactorings) {
								System.out.println(ref.toString());
							}
						}

						@Override
						public void handle(String commitId, List<Refactoring> refactorings) {
							System.out.println("handle() - " + refactorings.size() + " refactorings found");
						}
					});
			fail("");

		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception Thrown");
		}
	}

	@Test
	public void detectAtCommit() throws Exception {
		try {
			GitService gitService = new GitServiceImpl();
			GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();
			Repository repo = gitService.cloneIfNotExists("tmp/refactoring-toy-example",
					"https://github.com/danilofes/refactoring-toy-example.git");

			miner.detectAtCommit(repo, "https://github.com/danilofes/refactoring-toy-example.git",
					"05c1e773878bbacae64112f70964f4f2f7944398", new RefactoringHandler() {
						@Override
						public void handle(RevCommit commitData, List<Refactoring> refactorings) {
							System.out.println("Refactorings at " + commitData.getId().getName());
							for (Refactoring ref : refactorings) {
								System.out.println(ref.toString());
							}
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception Thrown");
		}
	}
}
