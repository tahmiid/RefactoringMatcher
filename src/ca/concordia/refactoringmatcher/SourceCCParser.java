package ca.concordia.refactoringmatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SourceCCParser {

	File beforeHeader;
	File afterHeader;
	File beforeClones;
	File afterClones;
	List<RefactoringData> allRefactoringData;
	List<HashSet<RefactoringData>> clones;

	public SourceCCParser(List<RefactoringData> allRefactoringData, String parentCodeDir, String refactoredCodeDir) {
		clones = new ArrayList<HashSet<RefactoringData>>();
		beforeHeader = new File(parentCodeDir + "/headers.file");
		afterHeader = new File(refactoredCodeDir + "/headers.file");
		beforeClones = new File(parentCodeDir + "/tokensclones_index_WITH_FILTER.txt");
		afterClones = new File(refactoredCodeDir + "/tokensclones_index_WITH_FILTER.txt");
		this.allRefactoringData = allRefactoringData;

		parseBeforeHeader();
		parseAfterHeader();
		parseBeforeClones();
		
		printReport();
	}

	private void printReport() {
		System.out.println("Similar Refactorings: " + clones.size());
		System.out.print("Sizes: ");
		for (HashSet<RefactoringData> set : clones) {
			System.out.print(set.size() + " ");
		}
		System.out.println();
		
	}

	private void parseBeforeClones() {
		String line;
		try (InputStream fis = new FileInputStream(beforeClones.getPath());
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				BufferedReader br = new BufferedReader(isr);) {
			while ((line = br.readLine()) != null) {
				Integer r = Integer.parseInt(line.substring(line.lastIndexOf(',') + 1));
				Integer l = Integer.parseInt(line.substring(0, line.lastIndexOf(',')));
				RefactoringData data1;
				RefactoringData data2;
				try {
					data1 = allRefactoringData.stream().filter(ref -> r == ref.getParentCode().headerNumber).findFirst()
							.get();
					data2 = allRefactoringData.stream().filter(ref -> l == ref.getParentCode().headerNumber).findFirst()
							.get();
				} catch (Exception e) {
					continue;
				}
				
				if (data1.getRefactoring().getRefactoringType() != data2.getRefactoring().getRefactoringType()) {
					continue;
				}
				boolean added = false;
				for (HashSet<RefactoringData> set : clones) {
					if (set.contains(data1) || set.contains(data2)) {
						if(set.iterator().next().getRefactoring().getRefactoringType() != data1.getRefactoring().getRefactoringType())
						{
							continue;
						}
						set.add(data1);
						set.add(data2);
						added = true;
						break;
					}
				}
				if (added == false) {
					HashSet hs = new HashSet<RefactoringData>();
					hs.add(data1);
					hs.add(data2);
					clones.add(hs);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (HashSet<RefactoringData> refSet : clones) {
			System.out.print("\nSimilar Refactorings:");
			for (RefactoringData refactoringData : refSet) {
				System.out.print(refactoringData + " ");
			}
			System.out.println();
			System.out.println();
		}
	}

	private void parseAfterHeader() {
		String line;
		try (InputStream fis = new FileInputStream(afterHeader.getPath());
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				BufferedReader br = new BufferedReader(isr);) {
			while ((line = br.readLine()) != null) {
				int s = Integer.parseInt(line.substring(line.lastIndexOf(',') + 1));
				for (RefactoringData refactoringData : allRefactoringData) {
					if (refactoringData.getRefactoredCode().getEndLocationInCodeDatabase() == s) {
						refactoringData.getRefactoredCode().headerNumber = Integer
								.parseInt(line.substring(0, line.indexOf(',')));
//						System.out.println(refactoringData.getRefactoredCode().headerNumber);
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void parseBeforeHeader() {
		String line;
		try (InputStream fis = new FileInputStream(beforeHeader.getPath());
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				BufferedReader br = new BufferedReader(isr);) {
			while ((line = br.readLine()) != null) {
				int s = Integer.parseInt(line.substring(line.lastIndexOf(',') + 1));
				for (RefactoringData refactoringData : allRefactoringData) {
					if (refactoringData.getParentCode().getEndLocationInCodeDatabase() == s) {
						refactoringData.getParentCode().headerNumber = Integer
								.parseInt(line.substring(0, line.indexOf(',')));
//						System.out.println(refactoringData.getParentCode().headerNumber);
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
