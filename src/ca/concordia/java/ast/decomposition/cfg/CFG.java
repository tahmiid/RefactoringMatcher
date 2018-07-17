package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import ca.concordia.java.ast.AbstractMethodDeclaration;
import ca.concordia.java.ast.decomposition.AbstractStatement;
import ca.concordia.java.ast.decomposition.CompositeStatementObject;
import ca.concordia.java.ast.decomposition.MethodBodyObject;
import ca.concordia.java.ast.decomposition.StatementObject;
import ca.concordia.java.ast.decomposition.StatementType;
import ca.concordia.java.ast.decomposition.TryStatementObject;

public class CFG extends Graph implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int PUSH_NEW_LIST = 0;
	private static final int JOIN_TOP_LIST = 1;
	private static final int PLACE_NEW_LIST_SECOND_FROM_TOP = 2;
	private static final int JOIN_SECOND_FROM_TOP_LIST = 3;
	private AbstractMethodDeclaration method;
	private Stack<List<CFGBranchConditionalNode>> unjoinedConditionalNodes;
	private Map<CFGBranchSwitchNode, List<CFGNode>> switchBreakMap;
	private Map<CFGBlockNode, List<CFGNode>> directlyNestedNodesInBlocks;
	private BasicBlockCFG basicBlockCFG;

	public CFG(AbstractMethodDeclaration method) {
		this.method = method;
		this.unjoinedConditionalNodes = new Stack<List<CFGBranchConditionalNode>>();
		this.switchBreakMap = new LinkedHashMap<CFGBranchSwitchNode, List<CFGNode>>();
		this.directlyNestedNodesInBlocks = new LinkedHashMap<CFGBlockNode, List<CFGNode>>();
		MethodBodyObject methodBody = method.getMethodBody();
		if (methodBody != null) {
			CompositeStatementObject composite = methodBody.getCompositeStatement();
			process(new ArrayList<CFGNode>(), composite);
			GraphNode.resetNodeNum();
			this.basicBlockCFG = new BasicBlockCFG(this);
		}
	}

	public AbstractMethodDeclaration getMethod() {
		return method;
	}

	public BasicBlockCFG getBasicBlockCFG() {
		return basicBlockCFG;
	}

	public List<BasicBlock> getBasicBlocks() {
		return basicBlockCFG.getBasicBlocks();
	}

	public Map<CFGBlockNode, List<CFGNode>> getDirectlyNestedNodesInBlocks() {
		return directlyNestedNodesInBlocks;
	}

	private List<CFGNode> process(List<CFGNode> previousNodes, CompositeStatementObject composite) {
		if (composite.getType().equals(StatementType.TRY)) {
			CFGTryNode tryNode = new CFGTryNode(composite);
			directlyNestedNodeInBlock(tryNode);
			findBlockNodeControlParent(tryNode);
			directlyNestedNodesInBlocks.put(tryNode, new ArrayList<CFGNode>());
			AbstractStatement firstStatement = composite.getStatements().get(0);
			composite = (CompositeStatementObject) firstStatement;
		} else if (composite.getType().equals(StatementType.SYNCHRONIZED)) {
			CFGSynchronizedNode synchronizedNode = new CFGSynchronizedNode(composite);
			directlyNestedNodeInBlock(synchronizedNode);
			findBlockNodeControlParent(synchronizedNode);
			directlyNestedNodesInBlocks.put(synchronizedNode, new ArrayList<CFGNode>());
			AbstractStatement firstStatement = composite.getStatements().get(0);
			composite = (CompositeStatementObject) firstStatement;
		}
		int i = 0;
		for (AbstractStatement abstractStatement : composite.getStatements()) {
			if (abstractStatement instanceof StatementObject) {
				StatementObject statement = (StatementObject) abstractStatement;
				previousNodes = processNonCompositeStatement(previousNodes, statement, composite);
			} else if (abstractStatement instanceof CompositeStatementObject) {
				CompositeStatementObject compositeStatement = (CompositeStatementObject) abstractStatement;
				if (compositeStatement.getType().equals(StatementType.BLOCK)) {
					previousNodes = process(previousNodes, compositeStatement);
				} else if (compositeStatement.getType().equals(StatementType.LABELED)) {
					List<AbstractStatement> nestedStatements = compositeStatement.getStatements();
					if (!nestedStatements.isEmpty()) {
						AbstractStatement firstStatement = nestedStatements.get(0);
						if (firstStatement instanceof CompositeStatementObject) {
							CompositeStatementObject compositeStatement2 = (CompositeStatementObject) firstStatement;
							if (compositeStatement2.getType().equals(StatementType.BLOCK)) {
								previousNodes = process(previousNodes, compositeStatement2);
							} else if (compositeStatement2.getType().equals(StatementType.SYNCHRONIZED)) {
								previousNodes = processSynchronizedStatement(previousNodes, compositeStatement2);
							} else if (compositeStatement2.getType().equals(StatementType.TRY)) {
								previousNodes = processTryStatement(previousNodes, compositeStatement2);
							} else if (isLoop(compositeStatement2)) {
								previousNodes = processLoopStatement(previousNodes, compositeStatement2);
							} else if (compositeStatement2.getType().equals(StatementType.DO)) {
								previousNodes = processDoStatement(previousNodes, compositeStatement2);
							} else if (compositeStatement2.getType().equals(StatementType.SWITCH)) {
								int action = getAction(composite, i, compositeStatement2);
								previousNodes = processSwitchStatement(previousNodes, compositeStatement2, action);
							} else if (compositeStatement2.getType().equals(StatementType.IF)) {
								int action = getAction(composite, i, compositeStatement2);
								previousNodes = processIfStatement(previousNodes, compositeStatement2, action);
							}
						} else if (firstStatement instanceof StatementObject) {
							previousNodes = processNonCompositeStatement(previousNodes,
									(StatementObject) firstStatement, composite);
						}
					}
				} else if (compositeStatement.getType().equals(StatementType.SYNCHRONIZED)) {
					previousNodes = processSynchronizedStatement(previousNodes, compositeStatement);
				} else if (compositeStatement.getType().equals(StatementType.TRY)) {
					previousNodes = processTryStatement(previousNodes, compositeStatement);
				} else if (isLoop(compositeStatement)) {
					previousNodes = processLoopStatement(previousNodes, compositeStatement);
				} else if (compositeStatement.getType().equals(StatementType.DO)) {
					previousNodes = processDoStatement(previousNodes, compositeStatement);
				} else if (compositeStatement.getType().equals(StatementType.SWITCH)) {
					int action = getAction(composite, i, compositeStatement);
					previousNodes = processSwitchStatement(previousNodes, compositeStatement, action);
				} else if (compositeStatement.getType().equals(StatementType.IF)) {
					int action = getAction(composite, i, compositeStatement);
					previousNodes = processIfStatement(previousNodes, compositeStatement, action);
				}
			}
			i++;
		}
		return previousNodes;
	}

	private List<CFGNode> processSynchronizedStatement(List<CFGNode> previousNodes,
			CompositeStatementObject compositeStatement) {
		CFGSynchronizedNode synchronizedNode = new CFGSynchronizedNode(compositeStatement);
		directlyNestedNodeInBlock(synchronizedNode);
		findBlockNodeControlParent(synchronizedNode);
		nodes.add(synchronizedNode);
		directlyNestedNodesInBlocks.put(synchronizedNode, new ArrayList<CFGNode>());
		createTopDownFlow(previousNodes, synchronizedNode);
		ArrayList<CFGNode> currentNodes = new ArrayList<CFGNode>();
		currentNodes.add(synchronizedNode);
		previousNodes = currentNodes;
		AbstractStatement firstStatement = compositeStatement.getStatements().get(0);
		previousNodes = process(previousNodes, (CompositeStatementObject) firstStatement);
		return previousNodes;
	}

	private List<CFGNode> processTryStatement(List<CFGNode> previousNodes,
			CompositeStatementObject compositeStatement) {
		TryStatementObject tryStatement = (TryStatementObject) compositeStatement;
		if (!tryStatement.hasResources()) {
			// if a try node does not have resources, it is treated as a block
			// and is omitted
			CFGTryNode tryNode = new CFGTryNode(compositeStatement);
			// nodes.add(tryNode);
			directlyNestedNodeInBlock(tryNode);
			findBlockNodeControlParent(tryNode);
			directlyNestedNodesInBlocks.put(tryNode, new ArrayList<CFGNode>());
			AbstractStatement firstStatement = compositeStatement.getStatements().get(0);
			previousNodes = process(previousNodes, (CompositeStatementObject) firstStatement);
		} else {
			// if a try node has resources, it is treated as a non-composite
			// node
			CFGTryNode tryNode = new CFGTryNode(compositeStatement);
			directlyNestedNodeInBlock(tryNode);
			findBlockNodeControlParent(tryNode);
			nodes.add(tryNode);
			directlyNestedNodesInBlocks.put(tryNode, new ArrayList<CFGNode>());
			createTopDownFlow(previousNodes, tryNode);
			ArrayList<CFGNode> currentNodes = new ArrayList<CFGNode>();
			currentNodes.add(tryNode);
			previousNodes = currentNodes;
			AbstractStatement firstStatement = compositeStatement.getStatements().get(0);
			previousNodes = process(previousNodes, (CompositeStatementObject) firstStatement);
		}
		return previousNodes;
	}

	private void findBlockNodeControlParent(CFGBlockNode blockNode) {
		for (GraphNode node : nodes) {
			CFGNode cfgNode = (CFGNode) node;
			if (cfgNode.getStatement() instanceof CompositeStatementObject) {
				CompositeStatementObject composite = (CompositeStatementObject) cfgNode.getStatement();
				if (directlyNestedNode(blockNode, composite)) {
					blockNode.setControlParent(cfgNode);
					break;
				}
			}
		}
		for (CFGBlockNode blockNode2 : directlyNestedNodesInBlocks.keySet()) {
			List<CFGNode> nestedNodes = directlyNestedNodesInBlocks.get(blockNode2);
			if (nestedNodes.contains(blockNode)) {
				blockNode.setControlParent(blockNode2);
				break;
			}
		}
	}

	private List<CFGNode> processDoStatement(List<CFGNode> previousNodes, CompositeStatementObject compositeStatement) {
		List<CFGNode> tmpNodes = previousNodes;
		previousNodes = process(previousNodes, compositeStatement);
		CFGBranchNode currentNode = new CFGBranchDoLoopNode(compositeStatement);
		nodes.add(currentNode);
		directlyNestedNodeInBlock(currentNode);
		createTopDownFlow(previousNodes, currentNode);
		CFGNode topNode = getCommonNextNode(tmpNodes);
		if (topNode == null)
			topNode = (CFGNode) nodes.toArray()[0];
		Flow flow = new Flow(currentNode, topNode);
		flow.setTrueControlFlow(true);
		flow.setLoopbackFlow(true);
		edges.add(flow);
		ArrayList<CFGNode> currentNodes = new ArrayList<CFGNode>();
		currentNodes.add(currentNode);
		previousNodes = currentNodes;
		return previousNodes;
	}

	private List<CFGNode> processLoopStatement(List<CFGNode> previousNodes,
			CompositeStatementObject compositeStatement) {
		CFGBranchNode currentNode = new CFGBranchLoopNode(compositeStatement);
		nodes.add(currentNode);
		directlyNestedNodeInBlock(currentNode);
		createTopDownFlow(previousNodes, currentNode);
		previousNodes = new ArrayList<CFGNode>();
		ArrayList<CFGNode> currentNodes = new ArrayList<CFGNode>();
		currentNodes.add(currentNode);
		previousNodes.addAll(process(currentNodes, compositeStatement));
		for (CFGNode previousNode : previousNodes) {
			Flow flow = new Flow(previousNode, currentNode);
			if (previousNode instanceof CFGBranchNode) {
				if (previousNode.equals(currentNode))
					flow.setTrueControlFlow(true);
				else
					flow.setFalseControlFlow(true);
			}
			flow.setLoopbackFlow(true);
			edges.add(flow);
		}
		if (previousNodes.size() > 1) {
			List<CFGBranchConditionalNode> conditionalNodes = unjoinedConditionalNodes.pop();
			for (CFGBranchConditionalNode conditionalNode : conditionalNodes) {
				conditionalNode.setJoinNode(currentNode);
			}
		}
		previousNodes = currentNodes;
		return previousNodes;
	}

	private int getAction(CompositeStatementObject parentComposite, int i, CompositeStatementObject childComposite) {
		int action = PUSH_NEW_LIST;
		List<AbstractStatement> statements = new ArrayList<AbstractStatement>(parentComposite.getStatements());
		CompositeStatementObject parent = (CompositeStatementObject) statements.get(0).getParent();
		boolean isBlockWithoutCompositeParent = isBlockWithoutCompositeParent(parent);
		if (parent.getType().equals(StatementType.BLOCK)) 
			parent = (CompositeStatementObject) parent.getParent();
		int position = i;
		while (parent != null
				&& (parent.getType().equals(StatementType.TRY) || parent.getType().equals(StatementType.SYNCHRONIZED))) {
			CompositeStatementObject tryStatement = parent;
			CompositeStatementObject tryStatementParent = (CompositeStatementObject) tryStatement.getParent();
			List<AbstractStatement> tryParentStatements = new ArrayList<AbstractStatement>(
					tryStatementParent.getStatements());
			if (tryStatementParent.getType().equals(StatementType.BLOCK))
				tryStatementParent = (CompositeStatementObject) tryStatementParent.getParent();
			int positionOfTryStatementInParent = 0;
			int j = 0;
			for (AbstractStatement statement : tryParentStatements) {
				if (statement.equals(tryStatement)) {
					positionOfTryStatementInParent = j;
					break;
				}
				j++;
			}
			if (tryStatement.getType().equals(StatementType.TRY)) {
				TryStatementObject tempTry = (TryStatementObject) tryStatement;
				if (tempTry.hasResources()) {
					tryParentStatements.addAll(positionOfTryStatementInParent + 1, statements);
				} else {
					tryParentStatements.remove(tryStatement);
					tryParentStatements.addAll(positionOfTryStatementInParent, statements);
				}
			} else {
				tryParentStatements.addAll(positionOfTryStatementInParent + 1, statements);
			}
			statements = tryParentStatements;
			parent = tryStatementParent;
			if (tryStatement.getType().equals(StatementType.TRY)) {
				TryStatementObject tempTry = (TryStatementObject) tryStatement;
				if (tempTry.hasResources())
					position = positionOfTryStatementInParent + position + 1;
				else
					position = positionOfTryStatementInParent + position;
			} else {
				position = positionOfTryStatementInParent + position + 1;
			}
		}
		if (parent != null && parent.getType().equals(StatementType.SWITCH)
				&& parentComposite.getType().equals(StatementType.BLOCK)) {
			List<AbstractStatement> switchStatements = new ArrayList<AbstractStatement>(parent.getStatements());
			int positionOfBlockInParentSwitch = 0;
			int j = 0;
			for (AbstractStatement statement : switchStatements) {
				if (statement.equals(parentComposite)) {
					positionOfBlockInParentSwitch = j;
					break;
				}
				j++;
			}
			switchStatements.remove(parentComposite);
			switchStatements.addAll(positionOfBlockInParentSwitch, statements);
			statements = switchStatements;
			position = positionOfBlockInParentSwitch + position;
		}
		if (parent != null && isBlockWithoutCompositeParent) {
			List<AbstractStatement> blockStatements = new ArrayList<AbstractStatement>(parent.getStatements());
			int positionOfBlockInParent = 0;
			int j = 0;
			for (AbstractStatement statement : blockStatements) {
				if (statement.equals(parentComposite)) {
					positionOfBlockInParent = j;
					break;
				}
				j++;
			}
			blockStatements.remove(parentComposite);
			blockStatements.addAll(positionOfBlockInParent, statements);
			statements = blockStatements;
			position = positionOfBlockInParent + position;
		}
		if (statements.size() == 1) {
			action = JOIN_TOP_LIST;
			if (parent != null) {
				if (isLoop(parent) || parent.getType().equals(StatementType.DO))
					action = PUSH_NEW_LIST;
			}
		} else if (statements.size() > 1) {
			AbstractStatement previousStatement = null;
			if (position >= 1)
				previousStatement = statements.get(position - 1);
			int j = 0;
			while (previousStatement != null && previousStatement.getType().equals(StatementType.TRY)
					&& !((TryStatementObject) previousStatement).hasResources()) {
				CompositeStatementObject tryStatement = (CompositeStatementObject) previousStatement;
				AbstractStatement firstStatement = tryStatement.getStatements().get(0);
				if (firstStatement instanceof CompositeStatementObject) {
					CompositeStatementObject tryBlock = (CompositeStatementObject) firstStatement;
					List<AbstractStatement> tryBlockStatements = tryBlock.getStatements();
					if (tryBlockStatements.size() > 0) {
						// previous statement is the last statement of this try
						// block
						previousStatement = tryBlockStatements.get(tryBlockStatements.size() - 1);
					} else {
						// try block is empty and previous statement is the
						// statement before this try block
						if (position >= 2 + j)
							previousStatement = statements.get(position - 2 - j);
						else
							previousStatement = null;
					}
				}
				j++;
			}
			while (previousStatement != null && isBlockWithoutCompositeParent(previousStatement)) {
				CompositeStatementObject block = (CompositeStatementObject) previousStatement;
				List<AbstractStatement> blockStatements = block.getStatements();
				if (blockStatements.size() > 0) {
					// previous statement is the last statement of this block
					previousStatement = blockStatements.get(blockStatements.size() - 1);
				}
			}
			if (statements.get(statements.size() - 1).equals(childComposite)) {
				// current if statement is the last statement of the composite
				// statement
				if (previousStatement != null && (previousStatement.getType().equals(StatementType.IF)
						|| previousStatement.getType().equals(StatementType.SWITCH))) {
					action = JOIN_SECOND_FROM_TOP_LIST;
					if (parent != null && (isLoop(parent) || parent.getType().equals(StatementType.DO)))
						action = PLACE_NEW_LIST_SECOND_FROM_TOP;
				} else {
					action = JOIN_TOP_LIST;
					if (parent != null && (isLoop(parent) || parent.getType().equals(StatementType.DO)))
						action = PUSH_NEW_LIST;
				}
			} else {
				if (previousStatement != null && (previousStatement.getType().equals(StatementType.IF)
						|| previousStatement.getType().equals(StatementType.SWITCH)))
					action = PLACE_NEW_LIST_SECOND_FROM_TOP;
				else {
					action = PUSH_NEW_LIST;
				}
			}
		}
		return action;
	}

	private boolean isBlockWithoutCompositeParent(AbstractStatement statement) {
		boolean isBlock = false;
		if (statement.getType().equals(StatementType.BLOCK)) {
			isBlock = true;
		}
		boolean parentIsBlock = false;
		AbstractStatement parent = (AbstractStatement) statement.getParent();
		if (parent != null && parent.getType().equals(StatementType.BLOCK)) {
			parentIsBlock = true;
		}
		return isBlock && parentIsBlock;
	}

	private List<CFGNode> processNonCompositeStatement(List<CFGNode> previousNodes, StatementObject statement,
			CompositeStatementObject composite) {
		// special handling of break, continue, return
		CFGNode currentNode = createNonCompositeNode(statement);
		nodes.add(currentNode);
		if ((currentNode instanceof CFGBreakNode || currentNode instanceof CFGExitNode)
				&& composite.getType().equals(StatementType.SWITCH) && directlyNestedNode(currentNode, composite)) {
			CFGBranchSwitchNode switchNode = getMostRecentSwitchNode();
			if (switchBreakMap.containsKey(switchNode)) {
				List<CFGNode> breakList = switchBreakMap.get(switchNode);
				breakList.add(currentNode);
			} else {
				List<CFGNode> breakList = new ArrayList<CFGNode>();
				breakList.add(currentNode);
				switchBreakMap.put(switchNode, breakList);
			}
			createTopDownFlow(previousNodes, currentNode);
		} else if (currentNode instanceof CFGSwitchCaseNode) {
			CFGSwitchCaseNode switchCase = (CFGSwitchCaseNode) currentNode;
			if (previousNodesContainBreakOrReturn(previousNodes, composite)) {
				CFGBranchSwitchNode switchNode = getMostRecentSwitchNode();
				Flow flow = new Flow(switchNode, currentNode);
				if (switchCase.isDefault())
					flow.setFalseControlFlow(true);
				else
					flow.setTrueControlFlow(true);
				edges.add(flow);
			} else
				createTopDownFlow(previousNodes, currentNode);
		} else
			createTopDownFlow(previousNodes, currentNode);
		ArrayList<CFGNode> currentNodes = new ArrayList<CFGNode>();
		currentNodes.add(currentNode);
		previousNodes = currentNodes;
		return previousNodes;
	}

	private CFGNode createNonCompositeNode(StatementObject statement) {
		CFGNode currentNode;
		// Statement astStatement = statement.getStatement();
		if (statement.getType().equals(StatementType.RETURN))
			currentNode = new CFGExitNode(statement);
		else if (statement.getType().equals(StatementType.SWITCH_CASE))
			currentNode = new CFGSwitchCaseNode(statement);
		else if (statement.getType().equals(StatementType.BREAK))
			currentNode = new CFGBreakNode(statement);
		else if (statement.getType().equals(StatementType.CONTINUE))
			currentNode = new CFGContinueNode(statement);
		else if (statement.getType().equals(StatementType.THROW))
			currentNode = new CFGThrowNode(statement);
		else
			currentNode = new CFGNode(statement);
		directlyNestedNodeInBlock(currentNode);
		return currentNode;
	}

	private boolean previousNodesContainBreakOrReturn(List<CFGNode> previousNodes, CompositeStatementObject composite) {
		for (CFGNode previousNode : previousNodes) {
			AbstractStatement statement = previousNode.getStatement();
			if ((statement.getType().equals(StatementType.BREAK) || statement.getType().equals(StatementType.RETURN))
					&& directlyNestedNode(previousNode, composite))
				return true;
		}
		return false;
	}

	private boolean directlyNestedNode(CFGNode node, CompositeStatementObject composite) {
		for (AbstractStatement statement : composite.getStatements()) {
			if (statement.equals(node.getStatement()))
				return true;
			if (statement instanceof CompositeStatementObject) {
				CompositeStatementObject composite2 = (CompositeStatementObject) statement;
//				Statement astComposite2 = composite2.getStatement();
				if (composite2.getType().equals(StatementType.BLOCK)) {
					if (directlyNestedNode(node, composite2))
						return true;
				}
			}
		}
		return false;
	}

	private void directlyNestedNodeInBlock(CFGNode node) {
		for (CFGBlockNode blockNode : directlyNestedNodesInBlocks.keySet()) {
			if (directlyNestedNode(node, (CompositeStatementObject) blockNode.getStatement())) {
				List<CFGNode> directlyNestedNodes = directlyNestedNodesInBlocks.get(blockNode);
				directlyNestedNodes.add(node);
				break;
			}
		}
	}

	private List<CFGNode> processSwitchStatement(List<CFGNode> previousNodes,
			CompositeStatementObject compositeStatement, int action) {
		CFGBranchSwitchNode currentNode = new CFGBranchSwitchNode(compositeStatement);
		handleAction(currentNode, action);
		nodes.add(currentNode);
		directlyNestedNodeInBlock(currentNode);
		createTopDownFlow(previousNodes, currentNode);
		previousNodes = new ArrayList<CFGNode>();
		ArrayList<CFGNode> currentNodes = new ArrayList<CFGNode>();
		currentNodes.add(currentNode);
		previousNodes.addAll(process(currentNodes, compositeStatement));
		List<CFGNode> breakList = switchBreakMap.get(currentNode);
		if (breakList != null) {
			for (CFGNode node : breakList) {
				if (!previousNodes.contains(node))
					previousNodes.add(node);
			}
		}
		if (currentNode.getFalseControlFlow() == null)
			previousNodes.add(currentNode);
		return previousNodes;
	}

	private List<CFGNode> processIfStatement(List<CFGNode> previousNodes, CompositeStatementObject compositeStatement,
			int action) {
		CFGBranchIfNode currentNode = new CFGBranchIfNode(compositeStatement);
		handleAction(currentNode, action);

		nodes.add(currentNode);
		directlyNestedNodeInBlock(currentNode);
		createTopDownFlow(previousNodes, currentNode);
		previousNodes = new ArrayList<CFGNode>();
		List<AbstractStatement> ifStatementList = compositeStatement.getStatements();
		AbstractStatement thenClause = ifStatementList.get(0);
		if (thenClause instanceof StatementObject) {
			StatementObject thenClauseStatement = (StatementObject) thenClause;
			CFGNode thenClauseNode = createNonCompositeNode(thenClauseStatement);
			nodes.add(thenClauseNode);
			ArrayList<CFGNode> currentNodes = new ArrayList<CFGNode>();
			currentNodes.add(currentNode);
			createTopDownFlow(currentNodes, thenClauseNode);
			previousNodes.add(thenClauseNode);
		} else if (thenClause instanceof CompositeStatementObject) {
			CompositeStatementObject thenClauseCompositeStatement = (CompositeStatementObject) thenClause;
			ArrayList<CFGNode> currentNodes = new ArrayList<CFGNode>();
			currentNodes.add(currentNode);
			if (thenClauseCompositeStatement.getType().equals(StatementType.IF))
				previousNodes.addAll(processIfStatement(currentNodes, thenClauseCompositeStatement, JOIN_TOP_LIST));
			else if (thenClauseCompositeStatement.getType().equals(StatementType.SWITCH))
				previousNodes.addAll(processSwitchStatement(currentNodes, thenClauseCompositeStatement, JOIN_TOP_LIST));
			else if (isLoop(thenClauseCompositeStatement))
				previousNodes.addAll(processLoopStatement(currentNodes, thenClauseCompositeStatement));
			else if (thenClauseCompositeStatement.getType().equals(StatementType.DO))
				previousNodes.addAll(processDoStatement(currentNodes, thenClauseCompositeStatement));
			else
				previousNodes.addAll(process(currentNodes, thenClauseCompositeStatement));
		}
		if (ifStatementList.size() == 2) {
			AbstractStatement elseClause = ifStatementList.get(1);
			if (elseClause instanceof StatementObject) {
				StatementObject elseClauseStatement = (StatementObject) elseClause;
				CFGNode elseClauseNode = createNonCompositeNode(elseClauseStatement);
				nodes.add(elseClauseNode);
				ArrayList<CFGNode> currentNodes = new ArrayList<CFGNode>();
				currentNodes.add(currentNode);
				createTopDownFlow(currentNodes, elseClauseNode);
				previousNodes.add(elseClauseNode);
			} else if (elseClause instanceof CompositeStatementObject) {
				CompositeStatementObject elseClauseCompositeStatement = (CompositeStatementObject) elseClause;
				ArrayList<CFGNode> currentNodes = new ArrayList<CFGNode>();
				currentNodes.add(currentNode);
				if (elseClauseCompositeStatement.getType().equals(StatementType.IF))
					previousNodes.addAll(processIfStatement(currentNodes, elseClauseCompositeStatement, JOIN_TOP_LIST));
				else if (elseClauseCompositeStatement.getType().equals(StatementType.SWITCH))
					previousNodes
							.addAll(processSwitchStatement(currentNodes, elseClauseCompositeStatement, JOIN_TOP_LIST));
				else if (isLoop(elseClauseCompositeStatement))
					previousNodes.addAll(processLoopStatement(currentNodes, elseClauseCompositeStatement));
				else if (elseClauseCompositeStatement.getType().equals(StatementType.DO))
					previousNodes.addAll(processDoStatement(currentNodes, elseClauseCompositeStatement));
				else
					previousNodes.addAll(process(currentNodes, elseClauseCompositeStatement));
			}
		} else {
			previousNodes.add(currentNode);
		}
		return previousNodes;
	}

	private void handleAction(CFGBranchConditionalNode currentNode, int action) {
		if (action == JOIN_TOP_LIST && !unjoinedConditionalNodes.empty()) {
			List<CFGBranchConditionalNode> topList = unjoinedConditionalNodes.peek();
			topList.add(currentNode);
		} else if (action == JOIN_SECOND_FROM_TOP_LIST) {
			if (unjoinedConditionalNodes.size() > 1) {
				List<CFGBranchConditionalNode> list = unjoinedConditionalNodes
						.elementAt(unjoinedConditionalNodes.size() - 2);
				list.add(currentNode);
			} else {
				List<CFGBranchConditionalNode> topList = unjoinedConditionalNodes.pop();
				List<CFGBranchConditionalNode> list = new ArrayList<CFGBranchConditionalNode>();
				list.add(currentNode);
				unjoinedConditionalNodes.push(list);
				unjoinedConditionalNodes.push(topList);
			}
		} else if (action == PLACE_NEW_LIST_SECOND_FROM_TOP && !unjoinedConditionalNodes.empty()) {
			List<CFGBranchConditionalNode> topList = unjoinedConditionalNodes.pop();
			List<CFGBranchConditionalNode> list = new ArrayList<CFGBranchConditionalNode>();
			list.add(currentNode);
			unjoinedConditionalNodes.push(list);
			unjoinedConditionalNodes.push(topList);
		} else {
			List<CFGBranchConditionalNode> list = new ArrayList<CFGBranchConditionalNode>();
			list.add(currentNode);
			unjoinedConditionalNodes.push(list);
		}
	}

	private void createTopDownFlow(List<CFGNode> previousNodes, CFGNode currentNode) {
		for (CFGNode previousNode : previousNodes) {
			Flow flow = new Flow(previousNode, currentNode);
			int numberOfImmediateBlocks = getNumberOfImmediateBlocks(currentNode);
			if (previousNode instanceof CFGBranchNode) {
				if (currentNode.getId() == previousNode.getId() + 1 + numberOfImmediateBlocks
						&& !(previousNode instanceof CFGBranchDoLoopNode))
					flow.setTrueControlFlow(true);
				else
					flow.setFalseControlFlow(true);
			}
			edges.add(flow);
		}
		if (previousNodes.size() > 1) {
			List<CFGBranchConditionalNode> conditionalNodes = unjoinedConditionalNodes.pop();
			for (CFGBranchConditionalNode conditionalNode : conditionalNodes) {
				conditionalNode.setJoinNode(currentNode);
			}
		}
	}

	private int getNumberOfImmediateBlocks(CFGNode node) {
		for (CFGBlockNode tryNode : directlyNestedNodesInBlocks.keySet()) {
			List<CFGNode> directlyNestedNodes = directlyNestedNodesInBlocks.get(tryNode);
			if (directlyNestedNodes.contains(node))
				return 1 + getNumberOfImmediateBlocks(tryNode);
		}
		return 0;
	}

	private boolean isLoop(CompositeStatementObject compositeStatement) {
		if (compositeStatement.getType().equals(StatementType.WHILE) 
				|| compositeStatement.getType().equals(StatementType.FOR)
				|| compositeStatement.getType().equals(StatementType.ENHANCED_FOR))
			return true;
		return false;
	}

	private CFGNode getCommonNextNode(List<CFGNode> nodes) {
		HashMap<CFGNode, Integer> nextNodeCounterMap = new HashMap<CFGNode, Integer>();
		for (CFGNode node : nodes) {
			for (GraphEdge edge : node.outgoingEdges) {
				CFGNode nextNode = (CFGNode) edge.dst;
				if (nextNodeCounterMap.containsKey(nextNode))
					nextNodeCounterMap.put(nextNode, nextNodeCounterMap.get(nextNode) + 1);
				else
					nextNodeCounterMap.put(nextNode, 1);
			}
		}
		for (CFGNode key : nextNodeCounterMap.keySet()) {
			if (nextNodeCounterMap.get(key) == nodes.size())
				return key;
		}
		return null;
	}

	private CFGBranchSwitchNode getMostRecentSwitchNode() {
		for (int i = unjoinedConditionalNodes.size() - 1; i >= 0; i--) {
			List<CFGBranchConditionalNode> unjoinedConditionalNodeList = unjoinedConditionalNodes.get(i);
			for (int j = unjoinedConditionalNodeList.size() - 1; j >= 0; j--) {
				CFGBranchConditionalNode conditionalNode = unjoinedConditionalNodeList.get(j);
				if (conditionalNode instanceof CFGBranchSwitchNode) {
					return (CFGBranchSwitchNode) conditionalNode;
				}
			}
		}
		return null;
	}
}
