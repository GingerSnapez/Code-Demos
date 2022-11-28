package edu.du.cs.fletchg.program2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

@SuppressWarnings("serial")
public class Line extends PaintingPrimitive{
	
	private Point start;
	private Point end;

	public Line(Color c, Point s, Point e) {
		super(c);
		this.start = s;
		this.end = e;
	}
	
	protected void setStart(Point p) {
		this.start = p;
	}
	
	protected void setEnd(Point p) {
		this.end = p;
	}

	@Override
	protected void drawGeometry(Graphics g) {
		g.setColor(super.color);
		g.drawLine(start.x, start.y, end.x, end.y);
		//System.out.println("Line drawn");
	}
}
