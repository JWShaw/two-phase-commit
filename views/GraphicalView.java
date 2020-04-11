package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GraphicalView extends JPanel
{
	private ArrayList<RMView> RMViews;
	//private TMView TMview;
	
	public GraphicalView()
	{
		this.setBackground(Color.WHITE);
		RMViews = new ArrayList<>();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		
		// Paint ALL RMViews in the list
		RMView vew = new RMView();
		vew.setPosition(100, 100);
		vew.setColor(Color.green);
		vew.draw(g2);
	}
	
	public void addRMView(RMView v)
	{
		RMViews.add(v);
	}
}