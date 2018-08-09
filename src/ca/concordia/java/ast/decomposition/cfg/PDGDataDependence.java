package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

public class PDGDataDependence extends PDGAbstractDataDependence  implements Serializable{

	public PDGDataDependence(PDGNode src, PDGNode dst,
			AbstractVariable data, CFGBranchNode loop) {
		super(src, dst, PDGDependenceType.DATA, data, loop);
	}
	
}
