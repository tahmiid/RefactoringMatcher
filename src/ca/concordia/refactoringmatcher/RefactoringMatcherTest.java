package ca.concordia.refactoringmatcher;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.lib.PersonIdent;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.RefactoringType;
import org.refactoringminer.util.GitServiceImpl;

public class RefactoringMatcherTest {

	private JFrame frame;
	private JTree tree;
	private JTextArea beforeRef1;
	private JTextArea beforeRef2;
	private JTextArea afterRef1;
	private JTextArea afterRef2;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JScrollPane scrollPane_3;
	private JScrollPane scrollPane_4;
	private JPanel panel_1;
	private JLabel lblNewLabel;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_5;
	private JLabel lblCommit;
	private JLabel lblCommit_1;
	private JLabel lblRefactoring;
	private JLabel lblRefactoring_1;

//	 String projectLink = "https://github.com/danilofes/refactoring-toy-example.git";
//	 String projectLink = "https://github.com/romuloceccon/jedit.git";
//	 String projectLink = "https://github.com/elastic/elasticsearch.git";
//	 String projectLink = "https://github.com/google/google-java-format.git";
//	 String projectLink = "https://github.com/google/ExoPlayer.git";
//	 String projectLink = "https://github.com/jfree/jfreechart.git";
//	 String projectLink = "https://github.com/apache/commons-lang.git";
	 String projectLink = "https://github.com/google/guava.git";
	 

	protected void runTest() throws Exception {

		Path outputDirectory = Files.createDirectories(Paths.get("output"));
		Path projectsDirectory = Files.createDirectories(Paths.get("projects"));
		GitService gitService = new GitServiceImpl();

		Project project = new Project(projectLink, projectsDirectory, outputDirectory, gitService);

		List<RefactoringData> refactorings = project.loadRefactoringsFromFile(gitService);

		RefactoringPatternFinder patternFinder = new TokenBasedRefactoringPatternFinder(refactorings, project.getOutputDirectory());

		List<HashSet<RefactoringData>> similarRefactorings = patternFinder.getSimilarRefactorings();

		printReport(refactorings, project);	
		
		printReport(similarRefactorings);
		
		createTreeView(similarRefactorings);
	}

private void printReport(List<HashSet<RefactoringData>> similarRefactorings) {
	System.out.println();
	System.out.println("Similar Refactorings\t" + similarRefactorings.size());
	
	int i = 0;
	int totalRefactoringsInSets = 0;
	int sameDaySets = 0;
	int sameDevSets = 0;
	int sumOfDayDifference = 0;
	for (HashSet<RefactoringData> hashSet : similarRefactorings) {
		totalRefactoringsInSets += hashSet.size();
		i++;
		String name = "Set " + i;
		int size = hashSet.size();
		long timeDifferenceInDays, highestTime = 0, lowestTime = 0;
		Date now = new Date();
		lowestTime = now.getTime();
		boolean sameCommitter = true;
		ArrayList<RefactoringData> list = new ArrayList<RefactoringData>(hashSet);
		PersonIdent author = list.get(0).getCommit().getCommiter();

		for (RefactoringData refactoringData : hashSet) {
			if(sameCommitter)
			{
				if(!author.getEmailAddress().equals(refactoringData.getCommit().getCommiter().getEmailAddress()))
				{
					sameCommitter = false;
				}
			}
			long time = refactoringData.getCommitTime().getTime();
			if(time >= highestTime)
				highestTime = time;
			if(time <= lowestTime)
				lowestTime = time;
		}
		
		timeDifferenceInDays = (highestTime - lowestTime) ;
		timeDifferenceInDays = timeDifferenceInDays / (1000*60*60*24);
		sumOfDayDifference += timeDifferenceInDays;
		
		if(timeDifferenceInDays == 0)
			sameDaySets++;
		if(sameCommitter)
			sameDevSets++;
			
		System.out.println(name + "\t" + size + "\t" + new SimpleDateFormat("yyyy-MM-dd").format(lowestTime) + "\t" + new SimpleDateFormat("yyyy-MM-dd").format(highestTime) + "\t" + timeDifferenceInDays + "\t" + sameCommitter);
	}
	
	System.out.println();
	System.out.println( similarRefactorings.size() + "\t" + 
						(float)totalRefactoringsInSets/similarRefactorings.size() + "\t" +
						sameDaySets + "\t" + 
						(similarRefactorings.size()-sameDaySets) + "\t" + 
						(float)sumOfDayDifference/similarRefactorings.size() + "\t" +
						sameDevSets + "\t" +
						(similarRefactorings.size() - sameDevSets));
}
	
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

	private void createTreeView(List<HashSet<RefactoringData>> similarRefactorings) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Similar Refactorings");
		for (HashSet<RefactoringData> set : similarRefactorings) {
			DefaultMutableTreeNode refactoringSetNode = new DefaultMutableTreeNode("Set " + (similarRefactorings.indexOf(set) + 1));

			for (RefactoringData refactoringData : set) {
				refactoringSetNode.add(createTreeNode(refactoringData));
			}
			root.add(refactoringSetNode);
		}

		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 400, 788);
		frame.getContentPane().add(scrollPane);
		tree = new JTree(root);
		scrollPane.setViewportView(tree);
		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

				if(selectedNode == null)
					return;
				
				if (selectedNode.getLevel() == 1) {
					lblNewLabel.setText(selectedNode.toString());
					HashSet<RefactoringData> set = (HashSet<RefactoringData>) similarRefactorings.get(selectedNode.getParent().getIndex(selectedNode));
					ArrayList<RefactoringData> list = new ArrayList<RefactoringData>(set);
					
					TitledBorder border;

					lblCommit.setToolTipText("Commit: " + list.get(0).getCommit().getId());
					lblCommit_1.setToolTipText("Commit: " + list.get(1).getCommit().getId());

					border = new TitledBorder(list.get(0).getBeforeCode().getFileName());
					border.setTitleJustification(TitledBorder.CENTER);
					border.setTitlePosition(TitledBorder.TOP);

					panel_2.setBorder(border);
					beforeRef1.setText(list.get(0).getBeforeCodeText());

					border = new TitledBorder(list.get(1).getBeforeCode().getFileName());
					border.setTitleJustification(TitledBorder.CENTER);
					border.setTitlePosition(TitledBorder.TOP);

					panel_3.setBorder(border);
					beforeRef2.setText(list.get(1).getBeforeCodeText());

					border = new TitledBorder(list.get(0).getAfterCode().getFileName());
					border.setTitleJustification(TitledBorder.CENTER);
					border.setTitlePosition(TitledBorder.TOP);

					panel_4.setBorder(border);
					afterRef1.setText(list.get(0).getAfterCodeText());

					border = new TitledBorder(list.get(1).getAfterCode().getFileName());
					border.setTitleJustification(TitledBorder.CENTER);
					border.setTitlePosition(TitledBorder.TOP);

					panel_5.setBorder(border);
					afterRef2.setText(list.get(1).getAfterCodeText());
				}
				else if (selectedNode.getLevel() == 2) {
					lblNewLabel.setText(selectedNode.getParent().toString());
					HashSet<RefactoringData> set = (HashSet<RefactoringData>) similarRefactorings.get(selectedNode.getParent().getParent().getIndex(selectedNode.getParent()));
					ArrayList<RefactoringData> list = new ArrayList<RefactoringData>(set);
					RefactoringData refactoring = (RefactoringData) list.get(selectedNode.getParent().getIndex(selectedNode));
					
					TitledBorder border;

					lblCommit_1.setToolTipText("Commit: " + refactoring.getCommit().getId());

					border = new TitledBorder(refactoring.getBeforeCode().getFileName());
					border.setTitleJustification(TitledBorder.CENTER);
					border.setTitlePosition(TitledBorder.TOP);

					panel_3.setBorder(border);
					beforeRef2.setText(refactoring.getBeforeCodeText());

					border = new TitledBorder(refactoring.getAfterCode().getFileName());
					border.setTitleJustification(TitledBorder.CENTER);
					border.setTitlePosition(TitledBorder.TOP);

					panel_5.setBorder(border);
					afterRef2.setText(refactoring.getAfterCodeText());
				}
			}
		});
	}

	private void printpairs(List<RefactoringPair> similarRefactoringPairs) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Refactoring Pairs");
		for (RefactoringPair pair : similarRefactoringPairs) {
			DefaultMutableTreeNode refactoringPairNode = new DefaultMutableTreeNode(
					"Pair " + (similarRefactoringPairs.indexOf(pair) + 1));

			refactoringPairNode.add(createTreeNode(pair.getRefactoringOne()));
			refactoringPairNode.add(createTreeNode(pair.getRefactoringTwo()));

			root.add(refactoringPairNode);
		}

		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 400, 788);
		frame.getContentPane().add(scrollPane);
		tree = new JTree(root);
		scrollPane.setViewportView(tree);
		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

				if (selectedNode.getLevel() == 1) {
					lblNewLabel.setText(selectedNode.toString());
					RefactoringPair pair = (RefactoringPair) similarRefactoringPairs
							.get(selectedNode.getParent().getIndex(selectedNode));

					TitledBorder border;

					lblCommit.setToolTipText("Commit: " + pair.getRefactoringOne().getCommit().getId());
					lblCommit_1.setToolTipText("Commit: " + pair.getRefactoringTwo().getCommit().getId());

					border = new TitledBorder(pair.getRefactoringOne().getBeforeCode().getFileName());
					border.setTitleJustification(TitledBorder.CENTER);
					border.setTitlePosition(TitledBorder.TOP);

					panel_2.setBorder(border);
					beforeRef1.setText(pair.getRefactoringOne().getBeforeCodeText());

					border = new TitledBorder(pair.getRefactoringTwo().getBeforeCode().getFileName());
					border.setTitleJustification(TitledBorder.CENTER);
					border.setTitlePosition(TitledBorder.TOP);

					panel_3.setBorder(border);
					beforeRef2.setText(pair.getRefactoringTwo().getBeforeCodeText());

					border = new TitledBorder(pair.getRefactoringOne().getAfterCode().getFileName());
					border.setTitleJustification(TitledBorder.CENTER);
					border.setTitlePosition(TitledBorder.TOP);

					panel_4.setBorder(border);
					afterRef1.setText(pair.getRefactoringOne().getAfterCodeText());

					border = new TitledBorder(pair.getRefactoringTwo().getAfterCode().getFileName());
					border.setTitleJustification(TitledBorder.CENTER);
					border.setTitlePosition(TitledBorder.TOP);

					panel_5.setBorder(border);
					afterRef2.setText(pair.getRefactoringTwo().getAfterCodeText());
				}
			}
		});
		System.out.println();
		System.out.println("Refactorings Pairs: " + similarRefactoringPairs.size());
	}

	private DefaultMutableTreeNode createTreeNode(RefactoringData refactoring) {
		DefaultMutableTreeNode refactoringNode = new DefaultMutableTreeNode(refactoring.getCommitShort());
		refactoringNode.add(createTreeNode(refactoring.getBeforeCode()));
		refactoringNode.add(createTreeNode(refactoring.getAfterCode()));
		return refactoringNode;
	}

	private DefaultMutableTreeNode createTreeNode(Code code) {
		DefaultMutableTreeNode before = new DefaultMutableTreeNode(code);
		return before;
	}

	private void printReport(List<RefactoringData> allRefactoringData, Project project) {
		int inlineMethod = 0;
		int extractMethod = 0;
		int extractAndMoveMethod = 0;
		for (RefactoringData refactoringData : allRefactoringData) {
			if (refactoringData.getType() == RefactoringType.INLINE_OPERATION) {
				inlineMethod++;
			} else if (refactoringData.getType() == RefactoringType.EXTRACT_OPERATION) {
				extractMethod++;
			} else if (refactoringData.getType() == RefactoringType.EXTRACT_AND_MOVE_OPERATION) {
				extractAndMoveMethod++;
			}
		}

		System.out.println();
		System.out.println("Project\t" + project.getName());
		System.out.println("Link\t" + project.getLink());
		System.out.println("Commits\t" + project.getCommitCount());
		System.out.println("Refactorings\t" + allRefactoringData.size());
		System.out.println("Inlined Methods\t" + inlineMethod);
		System.out.println("Extract Methods\t" + extractMethod);
		System.out.println("Extract and Move Methods\t" + extractAndMoveMethod);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RefactoringMatcherTest window = new RefactoringMatcherTest();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public RefactoringMatcherTest() throws Exception {
		initialize();
		runTest();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1200, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		panel_1 = new JPanel();
		panel_1.setBounds(412, 0, 776, 788);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 55, 776, 729);
		panel_1.add(panel);
		panel.setLayout(new GridLayout(2, 2, 0, 0));

		panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "JPanel title", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));

		scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1);

		beforeRef1 = new JTextArea();
		beforeRef1.setEditable(false);
		scrollPane_1.setViewportView(beforeRef1);

		panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "JPanel title", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_3);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));

		scrollPane_2 = new JScrollPane();
		panel_3.add(scrollPane_2);

		beforeRef2 = new JTextArea();
		beforeRef2.setEditable(false);
		scrollPane_2.setViewportView(beforeRef2);

		panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "JPanel title", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));

		scrollPane_3 = new JScrollPane();
		panel_4.add(scrollPane_3);

		afterRef1 = new JTextArea();
		afterRef1.setEditable(false);
		scrollPane_3.setViewportView(afterRef1);

		panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "JPanel title", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 1, 0, 0));

		scrollPane_4 = new JScrollPane();
		panel_5.add(scrollPane_4);

		afterRef2 = new JTextArea();
		afterRef2.setEditable(false);
		scrollPane_4.setViewportView(afterRef2);

		lblNewLabel = new JLabel("Pair");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setBounds(355, 12, 70, 15);
		panel_1.add(lblNewLabel);

		lblCommit = new JLabel("View Commit");
		lblCommit.setForeground(Color.BLUE);
		lblCommit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JLabel l = (JLabel) e.getSource();
				openLink(l.getToolTipText());
			}
		});
		lblCommit.setBounds(291, 33, 90, 15);
		panel_1.add(lblCommit);

		lblCommit_1 = new JLabel("View Commit");
		lblCommit_1.setForeground(Color.BLUE);
		lblCommit_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JLabel l = (JLabel) e.getSource();
				openLink(l.getToolTipText());
			}
		});
		lblCommit_1.setBounds(674, 33, 95, 15);
		panel_1.add(lblCommit_1);

		lblRefactoring = new JLabel("Refactoring 1");
		lblRefactoring.setBounds(5, 32, 107, 15);
		panel_1.add(lblRefactoring);

		lblRefactoring_1 = new JLabel("Refactoring 2");
		lblRefactoring_1.setBounds(393, 32, 107, 15);
		panel_1.add(lblRefactoring_1);
	}

	protected void openLink(String text) {
		String commitLink = projectLink.substring(0, projectLink.lastIndexOf('.')) + "/commit/";
		String link = commitLink + text.substring(text.indexOf(' ') + 1, text.length() - 1);

		try {
			openWebpage(new URI(link));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
