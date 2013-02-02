package fr.affectation.service.statistics;

import org.jfree.chart.renderer.category.BarRenderer;
import java.awt.Color;
import java.awt.Paint;

class ColorsRenderer extends BarRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Paint[] colors;

	public ColorsRenderer() {
//		this.colors = new Paint[] { Color.red, Color.blue, Color.green, Color.yellow, Color.orange, Color.cyan, Color.magenta, Color.black, Color.gray,
//				Color.pink, Color.yellow, Color.darkGray, Color.lightGray};
		this.colors = new Paint[] { Color.blue};
	}

	public Paint getItemPaint(final int row, final int column) {
		// returns color for each column
		return (this.colors[column % this.colors.length]);
	}
}