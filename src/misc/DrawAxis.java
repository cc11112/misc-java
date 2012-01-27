package misc;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class DrawAxis extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Line2D line1 = new Line2D.Double(0, 0, 200, 200);
	Line2D line2 = new Line2D.Double(70, 80, 100, 200);
	Line2D line3 = new Line2D.Double(100, 150, 150, 150);
	Stroke drawingStroke = new BasicStroke(2);

	public void paint(Graphics g) {
		Graphics2D graph = (Graphics2D) g;
		graph.setStroke(drawingStroke);
		graph.setPaint(Color.green);
		graph.draw(line1);
		graph.setPaint(Color.red);
		graph.draw(line2);
		graph.setPaint(Color.yellow);
		graph.draw(line3);

	}

	public static void main(String args[]) {
		Frame frame = new DrawAxis();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		frame.setSize(800, 500);
		frame.setVisible(true);
	}

}
