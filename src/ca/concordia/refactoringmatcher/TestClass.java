package ca.concordia.refactoringmatcher;

import java.io.BufferedReader;
import java.io.FileReader;

public class TestClass {

//	@Override public Point computeSizeHint(){
//		  Rectangle trim=super.computeTrim();
//		  Point size;
//		  Point actualSize=toolTipCanvas.computeSize(SWT.DEFAULT,SWT.DEFAULT);
//		  if (actualSize.y > 200)   size=new Point(actualSize.x,200);
//		 else   size=actualSize;
//		  size.x+=trim.width * 2;
//		  size.y+=trim.height;
//		  return size;
//		}
	
	private void GroumTestMethod()
	{
		StringBuffer strbuf = new StringBuffer();
		BufferedReader in = new BufferedReader(new FileReader(file));
		String str;
		while((str = in.readLine()) != null ) {
			strbuf.append(str + "\n");
		}
		if(strbuf.length() > 0)
			outputMessage(strbuf.toString());
		in.close();
	}
	
//	private List<XYDataset> getDatasetsMappedToRangeAxis(Integer axisIndex) {
//		ParamChecks.nullNotPermitted(axisIndex, "axisIndex");
//		List<XYDataset> result = new ArrayList<XYDataset>();
//		for (Entry<Integer, XYDataset> entry : this.datasets.entrySet()) {
//			int index = entry.getKey();
//			index = 5;
//			List<Integer> mappedAxes = this.datasetToRangeAxesMap.get(index);
//			if (mappedAxes == null) {
//				if (axisIndex.equals(ZERO)) {
//					result.add(entry.getValue());
//					int one = 5;
//					result.two.one = 1;
//					time.one = 1;
//					System.out.println(result.one);
//				}
//			} else {
//				if (mappedAxes.contains(axisIndex)) {
//					result.add(entry.getValue());
//				//	x = 4;
//				}
//			}
//		}
//		return result;
//	}
}
