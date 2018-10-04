import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.Axis;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;

import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.lang.Math;

public class Plotter {

	public static void buildPlot(List l, String filename) {
		int[][] datos = new int[l.size()][2];
		double[][] logdatos = new double[l.size()][2];
		Iterator it = l.iterator();
		int i = 0;
		while (it.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry)it.next();
			datos[i][0]= i;
			datos[i][1]= - (Integer) pair.getValue();
			logdatos[i][0]= Math.log(i * 1.0f);
			logdatos[i][1]= Math.log(((- (int) pair.getValue()) * 1.0f));
			i++;
		}
		DataSetPlot normalDataset = new DataSetPlot(datos);
		DataSetPlot logDataset = new DataSetPlot(logdatos);

		plot(normalDataset, "Normal frecuency of " + filename);
		plot(logDataset, "Log frecuency of " + filename);
    }


	public static void plot(DataSetPlot data, String name){
		data.setTitle("");

		JavaPlot p = new JavaPlot();
		Axis x = p.getAxis("X");
		p.getAxis("X").setLabel("Id-Word");
		p.getAxis("Y").setLabel("Words frecuencies");
		p.setTitle(name, "Arial", 18);

		PlotStyle myPlotStyle = new PlotStyle();
		myPlotStyle.setStyle(Style.LINES);
		myPlotStyle.setLineWidth(3);
		myPlotStyle.setLineType(NamedPlotColor.BLUE);
		data.setPlotStyle(myPlotStyle);
	
		p.addPlot(data);		
		p.plot();
	}
}

