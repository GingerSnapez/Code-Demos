package edu.du.cs.fletchg.program2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

@SuppressWarnings("serial")
public class Circle extends PaintingPrimitive {
	
	private Point center;
	private Point radiusPoint;

	public Circle(Color col, Point cent, Point rad) {
		super(col);
		this.center = cent;
		this.radiusPoint = rad;
	}
	
	protected void setStart(Point p) {
		this.center = p;
	}
	
	protected void setEnd(Point p) {
		this.radiusPoint = p;
	}

	public void drawGeometry(Graphics g) {
		int radius = (int) Math.abs(center.distance(radiusPoint));
		g.setColor(super.color);
		g.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
		//System.out.println("Circle drawn");
	}
}
