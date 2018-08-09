package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

public class PDGAntiDependence extends PDGAbstractDataDependence  implements Serializable{

	public PDGAntiDependence(PDGNode src, PDGNode dst,
			AbstractVariable data, CFGBranchNode loop) {
		super(src, dst, PDGDependenceType.ANTI, data, loop);
	}
	
}
