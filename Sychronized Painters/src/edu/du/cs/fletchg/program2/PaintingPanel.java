package edu.du.cs.fletchg.program2;

import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PaintingPanel extends JPanel {

	private ArrayList<PaintingPrimitive> prims;

	public PaintingPanel() {
		prims = new ArrayList<>();
	}

	public void addPrimitive(PaintingPrimitive obj) {
		this.prims.add(obj);
		repaint();
	}
	
	public void removeLast() {
		prims.remove(prims.size()-1);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (PaintingPrimitive obj : prims) {
			obj.drawGeometry(g);
		}
	}
}
