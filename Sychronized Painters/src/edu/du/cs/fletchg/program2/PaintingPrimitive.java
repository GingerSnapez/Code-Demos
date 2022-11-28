package edu.du.cs.fletchg.program2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class PaintingPrimitive implements Serializable {

	protected Color color;

	public PaintingPrimitive(Color c) {

		this.color = c;
	}

	// This is an example of the Template Design Pattern
	public final void draw(Graphics g) {
		g.setColor(this.color);
		drawGeometry(g);
	}
	
	protected abstract void setStart(Point p);
	
	protected abstract void setEnd(Point p);

	protected abstract void drawGeometry(Graphics g);

	public String toString() {
		return this.color.toString();
	}
}
