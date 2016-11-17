package ca.concordia.refactoringmatcher;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang3.tuple.Pair;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.RefactoringType;
import org.refactoringminer.util.GitServiceImpl;
import java.awt.Color;

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
	// String projectLink = "https://github.com/junit-team/junit5.git";
	 String projectLink = "https://github.com/jfree/jfreechart.git";
//	String projectLink = "https://github.com/romuloceccon/jedit.git";
//	 String projectLink = "https://github.com/apache/commons-lang.git";
//	 String projectLink = "https://github.com/qos-ch/slf4j.git";
	 
	protected void runTest() throws Exception {


		Path outputDirectory = Files.createDirectories(Paths.get("output"));
		Path projectsDirectory = Files.createDirectories(Paths.get("projects"));
		GitService gitService = new GitServiceImpl();

		Project project = new Project(projectLink, projectsDirectory, outputDirectory, gitService);

		List<RefactoringData> refactorings = project.loadRefactoringsFromFile(gitService);

		printReport(refactorings, project);

		RefactoringPatternFinder patternFinder = new TokenBasedRefactoringPatternFinder(refactorings,
				project.getOutputDirectory());

		// List<HashSet<RefactoringData>> similarRefactorings =
		// patternFinder.getSimilarRefactorings();

		List<Pair<RefactoringData, RefactoringData>> similarRefactoringPairs = patternFinder
				.getSimilarRefactoringPairs();

		printpairs(similarRefactoringPairs);

		// printGroups(similarRefactorings);
	}

	private void printpairs(List<Pair<RefactoringData, RefactoringData>> similarRefactoringPairs) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Refactoring Pairs");
		for (Pair<RefactoringData, RefactoringData> pair : similarRefactoringPairs) {
			DefaultMutableTreeNode refactoringPairNode = new DefaultMutableTreeNode("Pair " + (similarRefactoringPairs.indexOf(pair) + 1));
					
			refactoringPairNode.add(createTreeNode(pair.getRight()));
			refactoringPairNode.add(createTreeNode(pair.getLeft()));

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
		        
		        if(selectedNode.getLevel() == 1)
		        {        	
		        	lblNewLabel.setText(selectedNode.toString());
		        	Pair<RefactoringData, RefactoringData> pair = (Pair<RefactoringData, RefactoringData>) similarRefactoringPairs.get(selectedNode.getParent().getIndex(selectedNode));
		        	
		        	TitledBorder border;
		        	
		        	lblCommit.setToolTipText("Commit: " + pair.getLeft().getCommit() );
		        	lblCommit_1.setToolTipText("Commit: " + pair.getRight().getCommit() );
		        	
		        	border = new TitledBorder(pair.getLeft().getBeforeCode().getFileName());
		            border.setTitleJustification(TitledBorder.CENTER);
		            border.setTitlePosition(TitledBorder.TOP);
		        	
		            panel_2.setBorder(border);
		        	beforeRef1.setText(pair.getLeft().getBeforeCodeText());
		        	
		        	border = new TitledBorder(pair.getRight().getBeforeCode().getFileName());
		            border.setTitleJustification(TitledBorder.CENTER);
		            border.setTitlePosition(TitledBorder.TOP);
		            
		        	panel_3.setBorder(border);
		        	beforeRef2.setText(pair.getRight().getBeforeCodeText());
		        	
		        	border = new TitledBorder(pair.getLeft().getAfterCode().getFileName());
		            border.setTitleJustification(TitledBorder.CENTER);
		            border.setTitlePosition(TitledBorder.TOP);
		            
		        	panel_4.setBorder(border);
		        	afterRef1.setText(pair.getLeft().getAfterCodeText());
		        	
		        	border = new TitledBorder(pair.getRight().getAfterCode().getFileName());
		            border.setTitleJustification(TitledBorder.CENTER);
		            border.setTitlePosition(TitledBorder.TOP);
		            
		        	panel_5.setBorder(border);
		        	afterRef2.setText(pair.getRight().getAfterCodeText());
		        }
		    }
		});
		System.out.println("Refactorings Pairs: " + similarRefactoringPairs.size());
		System.out.println();
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
		System.out.println("Project: " + project.getName());
		System.out.println(project.getLink());
		System.out.println("Analyzed Commits: " + project.getCommitCount());
		System.out.println("Refactorings Found: " + allRefactoringData.size());
		System.out.println("Inlined Method: " + inlineMethod);
		System.out.println("Extract Method: " + extractMethod);
		System.out.println("Extract and Move Method: " + extractAndMoveMethod);
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
		String link = commitLink + text.substring(text.indexOf(' ')+1 , text.length()-1);
		
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
