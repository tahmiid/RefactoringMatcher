package ca.concordia.refactoringmatcher;

public class TestClass {

	private List<XYDataset> getDatasetsMappedToRangeAxis(Integer axisIndex) {
		ParamChecks.nullNotPermitted(axisIndex, "axisIndex");
		List<XYDataset> result = new ArrayList<XYDataset>();
		for (Entry<Integer, XYDataset> entry : this.datasets.entrySet()) {
			int index = entry.getKey();
			this.index = 5;
			index = 5;
			List<Integer> mappedAxes = this.datasetToRangeAxesMap.get(index);
			if (mappedAxes == null) {
				if (axisIndex.equals(ZERO)) {
					result.add(entry.getValue());
				}
			} else {
				if (mappedAxes.contains(axisIndex)) {
					result.add(entry.getValue());
				}
			}
		}
		return result;
	}
}
