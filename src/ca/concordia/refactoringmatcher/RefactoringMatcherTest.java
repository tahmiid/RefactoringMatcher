package ca.concordia.refactoringmatcher;

import ca.concordia.java.ast.ConstructorObject;
import ca.concordia.java.ast.MethodObject;
import ca.concordia.java.ast.decomposition.cfg.CFG;
import ca.concordia.java.ast.decomposition.cfg.PDG;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jgit.lib.Repository;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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

	String[] projectLinks = {

			// "https://github.com/iluwatar/java-design-patterns.git",
			// "https://github.com/JakeWharton/ActionBarSherlock.git",
			// "https://github.com/alibaba/dubbo.git",
			// "https://github.com/chrisbanes/Android-PullToRefresh.git",
//			 "https://github.com/alibaba/fastjson.git",
			// "https://github.com/jfeinstein10/SlidingMenu.git",
			// "https://github.com/JakeWharton/ViewPagerIndicator.git",
			// "https://github.com/square/retrofit.git",
			// "https://github.com/square/okhttp.git",
			// "https://github.com/zxing/zxing.git",
//			 "https://github.com/google/guava.git",

			// "https://github.com/danilofes/refactoring-toy-example.git",
			// "https://github.com/elastic/elasticsearch.git",
			// "https://github.com/google/google-java-format.git",
			// "https://github.com/google/ExoPlayer.git",
			// "https://github.com/romuloceccon/jedit.git",
			"https://github.com/jfree/jfreechart.git",
			 "https://github.com/apache/commons-lang.git",
//			"https://github.com/apache/log4j",
			// "https://github.com/apache/nifi-minifi.git",
			// "https://github.com/apache/knox.git",
			// "https://github.com/apache/zeppelin.git",
			// "https://github.com/apache/cassandra.git",
//			 "https://github.com/apache/storm.git",
			// "https://github.com/apache/hadoop.git",
			// "https://github.com/apache/kafka.git",
			// "https://github.com/apache/zookeeper.git",
			// "https://github.com/apache/camel.git",
			// "https://github.com/apache/hive.git"
			// "https://github.com/spring-projects/spring-framework.git",
//			 "https://github.com/google/guava.git"
	};

	protected void runTest() throws Exception {
		int found =0, notfount = 0;
		Path outputDirectory = Files.createDirectories(Paths.get("output"));
		Path projectsDirectory = Files.createDirectories(Paths.get("projects"));
		ExtendedGitService gitService = new ExtendedGitServiceImpl();

		ArrayList<GithubProject> projects = new ArrayList<GithubProject>();

		for (String projectLink : projectLinks) {
			GithubProject project = new GithubProject(projectLink, projectsDirectory, outputDirectory, gitService);
			project.printReport();
			projects.add(project);
			
			for (RefactoringData refactoringData : project.getRefactorings()) {
				Repository repo = project.getRepository();

				MethodDeclaration methodDeclaration = refactoringData.getRefactoredCode().getMethodDeclaration(gitService, repo);
				if (methodDeclaration != null) {
					System.out.println(methodDeclaration.toString());

					MethodObject methodObject = createMethodObject(methodDeclaration);
					PDG pdg = getPDG(methodObject);
					
//					Release  release = project.getRelease(refactoringData.getAfterCode().getCommit().getId());
					Release  release = project.getReleases().get(1);
					
					/*for (MethodInvocation methodInvocation : methodObject.getMethodBody().getCompositeStatement().getMethodInvocationList()) {
						

						if(release.contains(methodInvocation))
							found++;
						else 
							notfount++;
					}*/
				}
			}
			System.out.println("Methods found in jar: " + found + " " + (found+notfount));
		}
	}
	// findSimilarRefactorings(outputDirectory, refactorings);

	// readFromDeckardOutput();

	// countUnusedOpportunities(projectsDirectory, gitService,
	// project,refactoringSets);
	
	public PDG getPDG(MethodObject methodObject) {
		CFG cfg = new CFG(methodObject);
		PDG pdg = new PDG(cfg);
		System.out.println(pdg.getNodes().size());
		System.out.println("Done");	
		
		return pdg;
	}

	private MethodObject createMethodObject(MethodDeclaration methodDeclaration) {
		final ConstructorObject constructorObject = new ConstructorObject(methodDeclaration);
		MethodObject methodObject = new MethodObject(constructorObject);
		return methodObject;
	}

	// private void findSimilarRefactorings(Path outputDirectory,
	// List<RefactoringData> refactorings)
	// throws IOException, InterruptedException, ParseException {
	// RefactoringPatternFinder patternFinder = new
	// TokenBasedRefactoringPatternFinder(refactorings, outputDirectory);
	// List<RefactoringSet> refactoringSets =
	// patternFinder.getSimilarRefactorings();
	// printReport(refactoringSets);
	//
	// createTreeView(refactoringSets);
	// }

	// private void readFromDeckardOutput() throws IOException {
	// String line;
	// try (InputStream fis = new FileInputStream(
	// "/home/tahmiid/Downloads/RefactoringMatcher/clusters/cluster_vdb_30_0_allg_1.0_30");
	// InputStreamReader isr = new InputStreamReader(fis,
	// Charset.forName("UTF-8"));
	// BufferedReader br = new BufferedReader(isr);) {
	// while ((line = br.readLine()) != null) {
	// if (line.equals("")) {
	// System.out.println("Bla");
	// continue;
	// }
	// String[] headerParts = line.split("\t");
	// String detail = headerParts[2];
	// String[] detailParts = detail.split(" ");
	// Path file = Paths.get(detailParts[1]);
	// String location = detailParts[2];
	// String[] locationParts = location.split(":");
	// int start = Integer.parseInt(locationParts[1]);
	// int size = Integer.parseInt(locationParts[2]);
	// int end = start + size;
	//
	// }
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// }
	// }

	// private void countUnusedOpportunities(Path projectsDirectory, GitService
	// gitService, Project project,
	// List<RefactoringSet> refactoringSets) throws Exception, ParseException,
	// InterruptedException, IOException {
	// for (RefactoringSet refactoringSet : refactoringSets) {
	// for (Commit commit : refactoringSet.getCommits()) {
	// gitService.checkout(project.getRepository(), commit.getId());
	// SourcererCCDetector sccd = new SourcererCCDetector();
	// sccd.detectClonePairs(projectsDirectory);
	// }
	// }
	// }

	// private void printReport(List<RefactoringSet> refactoringSets) {
	// System.out.println();
	// System.out.println("Similar Refactorings\t" + refactoringSets.size());
	//
	// int i = 0;
	// int totalRefactoringsInSets = 0;
	// int sameDaySets = 0;
	// int sameDevSets = 0;
	// int sumOfDayDifference = 0;
	// for (RefactoringSet refactoringSet : refactoringSets) {
	// i++;
	//
	// totalRefactoringsInSets += refactoringSet.size();
	// sumOfDayDifference += refactoringSet.getDuration();
	//
	// if (refactoringSet.isSameDay())
	// sameDaySets++;
	// if (refactoringSet.isSameDeveloper())
	// sameDevSets++;
	//
	// System.out.println("Set " + i + "\t" + refactoringSet.size() + "\t"
	// + new
	// SimpleDateFormat("yyyy-MM-dd").format(refactoringSet.getFirstRefactoringDate())
	// + "\t"
	// + new
	// SimpleDateFormat("yyyy-MM-dd").format(refactoringSet.getLastRefactoringDate())
	// + "\t"
	// + refactoringSet.getDuration() + "\t" + refactoringSet.isSameDeveloper()
	// + "\t"
	// + refactoringSet.isSameProject());
	// // System.out.println(hashSet.getSimilarCode());
	// }
	//
	// System.out.println();
	// System.out.println(refactoringSets.size() + "\t" + (float)
	// totalRefactoringsInSets / refactoringSets.size()
	// + "\t" + sameDaySets + "\t" + (refactoringSets.size() - sameDaySets) +
	// "\t"
	// + (float) sumOfDayDifference / refactoringSets.size() + "\t" +
	// sameDevSets + "\t"
	// + (refactoringSets.size() - sameDevSets));
	// }

	// private void createTreeView(List<RefactoringSet> similarRefactorings) {
	// DefaultMutableTreeNode root = new DefaultMutableTreeNode("Similar
	// Refactorings");
	// for (HashSet<RefactoringData> set : similarRefactorings) {
	// DefaultMutableTreeNode refactoringSetNode = new DefaultMutableTreeNode(
	// "Set " + (similarRefactorings.indexOf(set) + 1));
	//
	// for (RefactoringData refactoringData : set) {
	// refactoringSetNode.add(createTreeNode(refactoringData));
	// }
	// root.add(refactoringSetNode);
	// }
	//
	// scrollPane = new JScrollPane();
	// scrollPane.setBounds(0, 0, 400, 788);
	// frame.getContentPane().add(scrollPane);
	// tree = new JTree(root);
	// scrollPane.setViewportView(tree);
	// tree.getSelectionModel().addTreeSelectionListener(new
	// TreeSelectionListener() {
	// @Override
	// public void valueChanged(TreeSelectionEvent e) {
	// DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)
	// tree.getLastSelectedPathComponent();
	//
	// if (selectedNode == null)
	// return;
	//
	// if (selectedNode.getLevel() == 1) {
	// lblNewLabel.setText(selectedNode.toString());
	// HashSet<RefactoringData> set = (HashSet<RefactoringData>)
	// similarRefactorings
	// .get(selectedNode.getParent().getIndex(selectedNode));
	// ArrayList<RefactoringData> list = new ArrayList<RefactoringData>(set);
	//
	// TitledBorder border;
	//
	// lblCommit.setToolTipText("Commit: " + list.get(0).getCommit().getId());
	// lblCommit_1.setToolTipText("Commit: " + list.get(1).getCommit().getId());
	//
	// border = new TitledBorder(list.get(0).getBeforeCode().getFileName());
	// border.setTitleJustification(TitledBorder.CENTER);
	// border.setTitlePosition(TitledBorder.TOP);
	//
	// panel_2.setBorder(border);
	// beforeRef1.setText(list.get(0).getBeforeCodeText());
	//
	// border = new TitledBorder(list.get(1).getBeforeCode().getFileName());
	// border.setTitleJustification(TitledBorder.CENTER);
	// border.setTitlePosition(TitledBorder.TOP);
	//
	// panel_3.setBorder(border);
	// beforeRef2.setText(list.get(1).getBeforeCodeText());
	//
	// border = new TitledBorder(list.get(0).getAfterCode().getFileName());
	// border.setTitleJustification(TitledBorder.CENTER);
	// border.setTitlePosition(TitledBorder.TOP);
	//
	// panel_4.setBorder(border);
	// afterRef1.setText(list.get(0).getAfterCodeText());
	//
	// border = new TitledBorder(list.get(1).getAfterCode().getFileName());
	// border.setTitleJustification(TitledBorder.CENTER);
	// border.setTitlePosition(TitledBorder.TOP);
	//
	// panel_5.setBorder(border);
	// afterRef2.setText(list.get(1).getAfterCodeText());
	// } else if (selectedNode.getLevel() == 2) {
	// lblNewLabel.setText(selectedNode.getParent().toString());
	// HashSet<RefactoringData> set = (HashSet<RefactoringData>)
	// similarRefactorings
	// .get(selectedNode.getParent().getParent().getIndex(selectedNode.getParent()));
	// ArrayList<RefactoringData> list = new ArrayList<RefactoringData>(set);
	// RefactoringData refactoring = (RefactoringData) list
	// .get(selectedNode.getParent().getIndex(selectedNode));
	//
	// TitledBorder border;
	//
	// lblCommit_1.setToolTipText("Commit: " + refactoring.getCommit().getId());
	//
	// border = new TitledBorder(refactoring.getBeforeCode().getFileName());
	// border.setTitleJustification(TitledBorder.CENTER);
	// border.setTitlePosition(TitledBorder.TOP);
	//
	// panel_3.setBorder(border);
	// beforeRef2.setText(refactoring.getBeforeCodeText());
	//
	// border = new TitledBorder(refactoring.getAfterCode().getFileName());
	// border.setTitleJustification(TitledBorder.CENTER);
	// border.setTitlePosition(TitledBorder.TOP);
	//
	// panel_5.setBorder(border);
	// afterRef2.setText(refactoring.getAfterCodeText());
	// }
	// }
	// });
	// }

	// private void printpairs(List<RefactoringPair> similarRefactoringPairs) {
	// DefaultMutableTreeNode root = new DefaultMutableTreeNode("Refactoring
	// Pairs");
	// for (RefactoringPair pair : similarRefactoringPairs) {
	// DefaultMutableTreeNode refactoringPairNode = new DefaultMutableTreeNode(
	// "Pair " + (similarRefactoringPairs.indexOf(pair) + 1));
	//
	// refactoringPairNode.add(createTreeNode(pair.getRefactoringOne()));
	// refactoringPairNode.add(createTreeNode(pair.getRefactoringTwo()));
	//
	// root.add(refactoringPairNode);
	// }
	//
	// scrollPane = new JScrollPane();
	// scrollPane.setBounds(0, 0, 400, 788);
	// frame.getContentPane().add(scrollPane);
	// tree = new JTree(root);
	// scrollPane.setViewportView(tree);
	// tree.getSelectionModel().addTreeSelectionListener(new
	// TreeSelectionListener() {
	// @Override
	// public void valueChanged(TreeSelectionEvent e) {
	// DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)
	// tree.getLastSelectedPathComponent();
	//
	// if (selectedNode.getLevel() == 1) {
	// lblNewLabel.setText(selectedNode.toString());
	// RefactoringPair pair = (RefactoringPair) similarRefactoringPairs
	// .get(selectedNode.getParent().getIndex(selectedNode));
	//
	// TitledBorder border;
	//
	// lblCommit.setToolTipText("Commit: " +
	// pair.getRefactoringOne().getCommit().getId());
	// lblCommit_1.setToolTipText("Commit: " +
	// pair.getRefactoringTwo().getCommit().getId());
	//
	// border = new
	// TitledBorder(pair.getRefactoringOne().getBeforeCode().getFileName());
	// border.setTitleJustification(TitledBorder.CENTER);
	// border.setTitlePosition(TitledBorder.TOP);
	//
	// panel_2.setBorder(border);
	// beforeRef1.setText(pair.getRefactoringOne().getBeforeCodeText());
	//
	// border = new
	// TitledBorder(pair.getRefactoringTwo().getBeforeCode().getFileName());
	// border.setTitleJustification(TitledBorder.CENTER);
	// border.setTitlePosition(TitledBorder.TOP);
	//
	// panel_3.setBorder(border);
	// beforeRef2.setText(pair.getRefactoringTwo().getBeforeCodeText());
	//
	// border = new
	// TitledBorder(pair.getRefactoringOne().getAfterCode().getFileName());
	// border.setTitleJustification(TitledBorder.CENTER);
	// border.setTitlePosition(TitledBorder.TOP);
	//
	// panel_4.setBorder(border);
	// afterRef1.setText(pair.getRefactoringOne().getAfterCodeText());
	//
	// border = new
	// TitledBorder(pair.getRefactoringTwo().getAfterCode().getFileName());
	// border.setTitleJustification(TitledBorder.CENTER);
	// border.setTitlePosition(TitledBorder.TOP);
	//
	// panel_5.setBorder(border);
	// afterRef2.setText(pair.getRefactoringTwo().getAfterCodeText());
	// }
	// }
	// });
	// System.out.println();
	// System.out.println("Refactorings Pairs: " +
	// similarRefactoringPairs.size());
	// }

	// private DefaultMutableTreeNode createTreeNode(RefactoringData
	// refactoring) {
	// DefaultMutableTreeNode refactoringNode = new
	// DefaultMutableTreeNode(refactoring.getCommitShort());
	// refactoringNode.add(createTreeNode(refactoring.getBeforeCode()));
	// refactoringNode.add(createTreeNode(refactoring.getAfterCode()));
	// return refactoringNode;
	// }

	// private DefaultMutableTreeNode createTreeNode(Code code) {
	// DefaultMutableTreeNode before = new DefaultMutableTreeNode(code);
	// return before;
	// }

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Tests window = new Tests();
					// window.frame.setVisible(true);
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
		// TODO fix it. Works only for one project
		String commitLink = projectLinks[0].substring(0, projectLinks[0].lastIndexOf('.')) + "/commit/";
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
