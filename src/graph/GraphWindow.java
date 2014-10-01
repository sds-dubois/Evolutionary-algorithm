package graph;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;

import javax.swing.*;
import javax.swing.plaf.*;
import java.util.*;

public class GraphWindow {

	private final WindowPanel windowPanel;
	private final WindowData windowData;

	public GraphWindow(int l, int h,
			GraphInterface g,
			HashMap<Integer,Integer> colorVertices, 
			HashMap<IntegerPair, Integer> colorEdges,
			HashMap<IntegerPair,Float> widthEdges) {

		windowData = new WindowData(g,l,h,colorVertices,colorEdges,widthEdges);
		JFrame frame = new JFrame("Graph window");
		Insets insets = frame.getInsets();
		frame.setSize(l + insets.left + insets.right, h + insets.top + insets.bottom);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		windowPanel = new WindowPanel(windowData);
		frame.add(windowPanel);
		frame.add(BorderLayout.SOUTH, CommandPanel.createCommandPanel(windowData,windowPanel));
		frame.setVisible(true);

	}

	public GraphWindow(int l, int h, GraphInterface g) {
		this(l,h,g, new HashMap<Integer,Integer>(), new HashMap<IntegerPair, Integer>(), new HashMap<IntegerPair,Float>());
	}


}

class CommandPanel {

	static JComponent createCommandPanel(final WindowData drawing, final WindowPanel windowPanel) {

		JPanel panel = new JPanel();
		JButton bouton = new JButton("Redraw");
		bouton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawing.FR(1000);
				windowPanel.repaint();
			}
		});
		panel.add(bouton);

		return panel;

	}

}

@SuppressWarnings("serial")

class WindowPanel extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener {

	WindowData drawing;
	private double _xShift = 0;
	private double _yShift = 0;
	private double _initXS = 0;
	private double _initYS = 0;

	double _zoom = 1.0;

	private enum State
	{
		MOUSE_PRESSED,
		NONE
	};
	private State etat = State.NONE;
	private Point ref = new Point();

	WindowPanel(WindowData drawing) {
		setUI(DrawingUI.INSTANCE);
		this.drawing = drawing;
		addMouseListener(this);
		addMouseMotionListener(this);
		this.addMouseWheelListener(this);
	}

	int getFontSize() { return (int)(17*Math.sqrt(_zoom)); }

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mouseMoved(MouseEvent arg0) {}

	public void mousePressed(MouseEvent arg0) {
		etat = State.MOUSE_PRESSED;
		ref = arg0.getPoint();
		_initXS = _xShift;
		_initYS = _yShift;
	}
	public void mouseReleased(MouseEvent arg0) {
		etat = State.NONE;
	}
	public void mouseDragged(MouseEvent arg0) {
		if (etat == State.MOUSE_PRESSED)
		{
			Point p = arg0.getPoint();
			_xShift = _initXS+(p.x-ref.x)/_zoom;
			_yShift = _initYS+(p.y-ref.y)/_zoom;
			repaint();
		}
	}
	public Vector transformPoint(Vector p)
	{  
		int w = getWidth();
		int h = getHeight();
		return new Vector(w/2+(int)(_zoom*((_xShift+p.x)-w/2)),(int)(h/2+_zoom*(_yShift+p.y-h/2)));  }

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		_zoom = Math.min(Math.max(_zoom*Math.pow(1.1, -arg0.getWheelRotation()),0.01),100.0);
		repaint();
	}

}

class DrawingUI extends ComponentUI {

	private DrawingUI() {}

	static DrawingUI INSTANCE = new DrawingUI();

	@Override
	public void installUI(JComponent c) {
		LookAndFeel.installProperty(c, "opaque", true);
		c.setBackground(new ColorUIResource(Color.WHITE));
	}

	@Override
	public void paint(Graphics gg,JComponent component) {

		WindowPanel windowPanel = (WindowPanel)component;
		WindowData drawing = windowPanel.drawing;
		Graphics2D g = (Graphics2D)gg;

		Color oldColor = g.getColor();
		Stroke oldStroke = g.getStroke();
		Paint oldPaint = g.getPaint();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.white);
		g.fillRect(0, 0, windowPanel.getWidth(), windowPanel.getHeight());
		g.setColor(Color.black);
		g.setFont(new Font("Default", Font.BOLD, windowPanel.getFontSize()));

		for (Integer s : drawing.g.allVertices()) {

			for (Integer t : drawing.g.successors(s)) {

				Vector o = windowPanel.transformPoint(drawing.points.get(s));
				Vector d = windowPanel.transformPoint(drawing.points.get(t));

				g.setColor(drawing.colorAlphaEdge(new IntegerPair(s,t)));
				g.setStroke(new BasicStroke(drawing.getEdgeWidth(new IntegerPair(s,t)),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));

				int xo=(int)o.x*(windowPanel.getWidth()-80)/drawing.width+40;
				int yo=(int)o.y*(windowPanel.getHeight()-80)/drawing.height+60;
				int xd=(int)d.x*(windowPanel.getWidth()-80)/drawing.width+40;
				int yd=(int)d.y*(windowPanel.getHeight()-80)/drawing.height+60;

				Vector v=new Vector(xd-xo,yd-yo);
				double nv=v.norm();
				v.x/=nv;
				v.y/=nv;
				Vector u=new Vector(-v.y,v.x);

				double arrowLength=17.0*windowPanel._zoom;
				double arrowWidth=8.0*windowPanel._zoom;

				int xa=(int)(0.5*(xo+xd)+0.15*arrowLength*v.x+arrowWidth*u.x);
				int ya=(int)(0.5*(yo+yd)+0.15*arrowLength*v.y+arrowWidth*u.y);
				int xb=(int)(0.5*(xo+xd)+0.15*arrowLength*v.x-arrowWidth*u.x);
				int yb=(int)(0.5*(yo+yd)+0.15*arrowLength*v.y-arrowWidth*u.y);

				g.drawLine(xo,yo,xd,yd);

				g.setColor(drawing.colorAlphaEdge(new IntegerPair(s,t)));
				GeneralPath arrowHead=new GeneralPath(GeneralPath.WIND_EVEN_ODD,3);
				arrowHead.moveTo((int)(0.5*(xo+xd)+arrowLength*v.x),(int)(0.5*(yo+yd)+arrowLength*v.y));
				arrowHead.lineTo(xa,ya);
				arrowHead.lineTo(xb,yb);
				arrowHead.closePath();
				g.fill(arrowHead);

			}

		}

		for (Integer s : drawing.g.allVertices()) {

			Vector p = windowPanel.transformPoint(drawing.points.get(s));

			g.setColor(drawing.colorAlphaVertex(s));
			int radius = (int)(drawing.getRadius(s)*Math.sqrt(windowPanel._zoom));

			g.fillOval((int)p.x * (windowPanel.getWidth() - 80) / drawing.width - radius + 40, (int)p.y
					* (windowPanel.getHeight() - 80) / drawing.height - radius + 60, radius*2, radius*2);

			g.setColor(WindowData.colorArray[0]);
			g.drawString(s.toString(), (int)p.x * (windowPanel.getWidth() - 80) / drawing.width
					- 5 + 40, (int)p.y * (windowPanel.getHeight() - 80) / drawing.height - 15 + 60);

		}

		g.setColor(oldColor);
		g.setStroke(oldStroke);
		g.setPaint(oldPaint);

	}

}

class WindowData {

	GraphInterface g;
	private HashMap<Integer,Integer> colorVertices;
	private HashMap<IntegerPair, Integer> colorEdges;
	private HashMap<IntegerPair,Float> widthEdges;
	private HashMap<Integer,Float> radiusVertex;

	HashMap<Integer, Vector> points;

	public WindowData(GraphInterface g, int l, int h,
			HashMap<Integer,Integer> colorVertices, 
			HashMap<IntegerPair, Integer> colorEdges,
			HashMap<IntegerPair,Float> widthEdges) {

		this.colorVertices = colorVertices;
		this.colorEdges = colorEdges;
		this.widthEdges = widthEdges;
		this.radiusVertex=new HashMap<Integer,Float>();

		this.g = g;
		points = new HashMap<Integer, Vector>();
		width=l;
		height=h;

		RND = new Random(6);

		for (Integer s : g.allVertices()) {

			int x = (int) (width * RND.nextDouble());
			int y = (int) (height * RND.nextDouble());
			points.put(s, new Vector(x, y));

		}

		FR(1000);
		
		colorArray=new Color[10];
		for (int i=1;i<10;i++) colorArray[i]=convertHSVtoRGB((int)(i/10.0*255),255,200);
		colorArray[0]=Color.black;
		
	}

	Color convertHSVtoRGB(int h, int s, int v) {

		int f;
		long p, q, t;
		int r,g,b;

		if (s ==0) {
			r = g = b = v;
			return new Color(r,g,b);
		}

		f = ((h%60)*255)/60;
		h /= 60;

		p = (v*(256 - s))/256;
		q = (v*(256 - (s*f)/256))/256;
		t = (v*(256 - (s*(256 - f))/256))/256;

		switch( h ) {
		case 0:
			r = v; g = (int)t; b = (int)p;
			break;
		case 1:
			r = (int)q; g = v; b = (int)p;
			break;
		case 2:
			r = (int)p; g = v; b = (int)t;
			break;
		case 3:
			r = (int)p; g = (int)q; b = v;
			break;
		case 4:
			r = (int)t; g = (int)p; b = v;
			break;
		default: 
			r = v; g = (int)p; b = (int)q;
			break;
		}
		
		return new Color(r,g,b);
		
	}

	static Color[] colorArray;

	public int getColorVertex(Integer s) {
		Integer color = colorVertices.get(s);
		return (color != null ? color : 0);
	}

	public int getColorEdge(IntegerPair a) {
		Integer color = colorEdges.get(a);
		return (color != null ? color : 0);
	}

	public float getEdgeWidth(IntegerPair a) {
		Float width = widthEdges.get(a);
		return (width != null ? width.floatValue() : 1.0f);
	}

	public float getRadius(Integer a) {
		Float radius = radiusVertex.get(a);
		return (radius != null ? radius.floatValue() : 1.0f);
	}

	int height, width;

	private Random RND;

	Color color(int c) {
		Color d = colorArray[c % colorArray.length];
		return d;
	}

	Color colorAlpha(int c) {
		Color d = colorArray[c % colorArray.length];
		return new Color(d.getRed(),d.getGreen(),d.getBlue(),128);
	}

	Color colorAlphaVertex(Integer s) {
		return colorAlpha(getColorVertex(s));
	}

	Color colorAlphaEdge(IntegerPair s) {
		return colorAlpha(getColorEdge(s));
	}

	Color colorVertex(Integer s) {
		return color(getColorVertex(s));
	}

	Color colorEdge(IntegerPair s) {
		return color(getColorEdge(s));
	}

	static double fa(double z, double k) {
		return z * z / k;
	}

	static double fr(double z, double k) {
		if (2 * k > z)
			return k * k / z;
		else
			return 0;
	}

	void FR(int nbI) {

		for (Integer s : g.allVertices()) {

			Vector p = points.get(s);
			p.x = (int) (width * (0.5 + RND.nextDouble()) / 2);
			p.y = (int) (height * (0.5 + RND.nextDouble()) / 2);

		}

		double aire = width * height;

		int nbS=0;
		for (Integer i : g.allVertices()) nbS++;

		double k = Math.sqrt(aire / nbS);

		for (int i = 0; i < nbI; i++) {

			HashMap<Integer, Vector> dep = new HashMap<Integer, Vector>();
			for (Integer v : g.allVertices()) dep.put(v, new Vector(0.0,0.0));

			for (Integer v : g.allVertices()) {

				for (Integer u : g.allVertices()) {

					if (!u.equals(v)) {

						Vector up = points.get(u);
						Vector vp = points.get(v);
						Vector D = new Vector(vp.x - up.x, vp.y - up.y);
						double norm = D.norm();
						double f = fr(norm, k) / norm;
						dep.get(v).add(new Vector(D.x*f,D.y*f));
						dep.get(u).add(new Vector(-D.x*f,-D.y*f));

					}

				}

				for (Integer a : g.successors(v)) {

					Vector vp = points.get(v);
					Vector up = points.get(a);
					Vector D = new Vector(vp.x - up.x, vp.y - up.y);
					double norm = D.norm();
					double f = fa(norm, k) / norm;
					dep.get(v).add(new Vector(-D.x*f,-D.y*f));
					dep.get(a).add(new Vector(D.x*f,D.y*f));

				}

			}

			double t = ((double) width) / (2 * (1 + i));

			for (Integer v : g.allVertices()) {

				Vector pv = points.get(v);
				Vector dv = dep.get(v);
				double maxt = Math.min(1, t / dv.norm());
				pv.x = (int) (pv.x + dv.x * maxt);
				pv.y = (int) (pv.y + dv.y * maxt);
				points.put(v, pv);

			}

		}
		
		// compute the bounding box

		double minx=width;
		double maxx=0;
		double miny=height;
		double maxy=0;
		
		for (Integer v : g.allVertices()) {

			Vector pv = points.get(v);
			minx = Math.min(minx, pv.x);
			maxx = Math.max(maxx, pv.x);
			miny = Math.min(miny, pv.y);
			maxy = Math.max(maxy, pv.y);

		}

		// rescale
		
		for (Integer v : g.allVertices()) {

			Vector pv = points.get(v);
			pv.x=(pv.x-minx)/(maxx-minx)*width;
			pv.y=(pv.y-miny)/(maxy-miny)*width;
			
		}
		
	}

	int currentColor=3;

}

class Vector {

	double x, y;

	Vector(double x, double y) {

		this.x = x;
		this.y = y;

	}

	double norm() {

		return Math.sqrt(x * x + y * y);

	}

	void add(Vector v) {

		x+=v.x;
		y+=v.y;

	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}


