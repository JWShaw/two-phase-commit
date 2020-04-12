package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GraphicalView extends JPanel
{
	private ArrayList<RMView> rm_views;
	private RMView tm_view;
	
	public GraphicalView()
	{
		this.setBackground(Color.WHITE);
		rm_views = new ArrayList<>();
		tm_view = new RMView();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		
		// Draws the TM in the middle of its panel
		tm_view.setPosition((getWidth() - tm_view.RADIUS) / 2,
				(getHeight() - tm_view.RADIUS) / 2);
		tm_view.draw(g2);
		
		double theta = 2 * Math.PI / rm_views.size();
		
		// Draws the RMs in a circle around the TMView
		for (int i = 0; i < rm_views.size(); i++)
		{
			rm_views.get(i).setPosition(
					(int)((tm_view.getXpos()) + getWidth()/4 * Math.sin(i * theta)),
					(int)((tm_view.getYpos()) - getHeight()/4 * Math.cos(i * theta)));
			rm_views.get(i).draw(g2);
		}
	}
	
	public void addRMView(RMView v)
	{
		rm_views.add(v);
		repaint();
	}
}