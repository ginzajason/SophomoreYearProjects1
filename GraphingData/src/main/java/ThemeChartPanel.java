import java.awt.BorderLayout;
import java.util.HashSet;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class ThemeChartPanel extends ChartPanel{
	public ThemeChartPanel(HashSet<DataObject> dataSet) {
		super(dataSet);
	}
	
	protected void graphData() {
		if(this.xPanel != null) {
			remove(xPanel);
		}
		
		HashSet<DataObject> dataSetx = new HashSet<DataObject>(dataSet);
		HashSet<DataObject> dataSety = new HashSet<DataObject>(dataSet);
		
		if(chooseObjectX.getSelectedIndex() != 1) {
			dataSetx.clear();
			dataSetx.add((DataObject) chooseObjectX.getSelectedItem());
		}
		if(chooseObjectY.getSelectedIndex() != 1) {
			dataSety.clear();
			dataSety.add((DataObject) chooseObjectY.getSelectedItem());
		}
		
		double[] xData = new double[dataSetx.size() + dataSety.size()];
		double[] yData = new double[dataSetx.size() + dataSety.size()];
		
		int count = 0;
		for(DataObject data : dataSetx) {
			xData[count] = Double.parseDouble(data.getDataList().get(chooseX.getSelectedIndex()-1));
			yData[count] = Double.parseDouble(data.getDataList().get(chooseY.getSelectedIndex()-1));
			count ++;
		}
		
		for(DataObject data : dataSety) {
			xData[count] = Double.parseDouble(data.getDataList().get(chooseX.getSelectedIndex()-1));
			yData[count] = Double.parseDouble(data.getDataList().get(chooseY.getSelectedIndex()-1));
			count ++;
		}
		
		String x_axis = DataObject.getDataContents().get(chooseX.getSelectedIndex()-1);
		String y_axis = DataObject.getDataContents().get(chooseY.getSelectedIndex()-1);
		String title = x_axis + " To " + y_axis;
		
		XYChart chart = new XYChartBuilder().build();
		chart.setTitle(title);
		chart.setXAxisTitle(x_axis);
		
	    chart.getStyler().setLegendVisible(true);
	    chart.getStyler().setLegendPosition(LegendPosition.OutsideS);
	    chart.getStyler().setHasAnnotations(true);
	    
	    XYSeries series = chart.addSeries("a", xData, yData);
	    series.setMarker(SeriesMarkers.NONE);
	    
		xPanel = new XChartPanel<XYChart>(chart);
		add(xPanel, BorderLayout.CENTER);
		this.updateUI();
	}
}
