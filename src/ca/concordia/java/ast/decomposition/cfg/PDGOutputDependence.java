package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

public class PDGOutputDependence extends PDGAbstractDataDependence  implements Serializable{

	public PDGOutputDependence(PDGNode src, PDGNode dst,
			AbstractVariable data, CFGBranchNode loop) {
		super(src, dst, PDGDependenceType.OUTPUT, data, loop);
	}
	
}
