package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ca.concordia.java.ast.decomposition.AbstractStatement;

public class CFGBranchDoLoopNode extends CFGBranchNode  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8144140382435399439L;

	public CFGBranchDoLoopNode(AbstractStatement statement) {
		super(statement);
	}

	public CFGNode getJoinNode() {
		Flow flow = getTrueControlFlow();
		return (CFGNode)flow.getDst();
	}

	public List<BasicBlock> getNestedBasicBlocks() {
		List<BasicBlock> blocksBetween = new ArrayList<BasicBlock>();
		BasicBlock srcBlock = getBasicBlock();
		BasicBlock joinBlock = getJoinNode().getBasicBlock();
		//join node is always before do-loop node
		blocksBetween.add(joinBlock);
		BasicBlock nextBlock = joinBlock;
		if(!joinBlock.equals(srcBlock)) {
			while(!nextBlock.getNextBasicBlock().equals(srcBlock)) {
				nextBlock = nextBlock.getNextBasicBlock();
				blocksBetween.add(nextBlock);
			}
		}
		return blocksBetween;
	}
}
