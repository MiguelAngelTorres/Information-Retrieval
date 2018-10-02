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

public class Plotter {

	public static void plot(List l, String filename) {
		int[][] datos = new int[l.size()][2];
		Iterator it = l.iterator();
		int i = 0;
		while (it.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry)it.next();
			datos[i][0]= i;
			datos[i][1]= - (Integer) pair.getValue();
			i++;
		}
		DataSetPlot dataset = new DataSetPlot(datos);
		dataset.setTitle("");

		JavaPlot p = new JavaPlot();
		Axis x = p.getAxis("X");
		x.setBoundaries(0,200);
		p.getAxis("X").setLabel("Id-Word");
		p.getAxis("Y").setLabel("Words frecuencies");
		p.setTitle("Word Frecuencies of " + filename, "Arial", 20);

		PlotStyle myPlotStyle = new PlotStyle();
		myPlotStyle.setStyle(Style.LINES);
		myPlotStyle.setLineWidth(3);
		myPlotStyle.setLineType(NamedPlotColor.BLUE);
		dataset.setPlotStyle(myPlotStyle);
	
		p.addPlot(dataset);		
		p.plot();
    }

}

