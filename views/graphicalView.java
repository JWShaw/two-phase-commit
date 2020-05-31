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
		
		// Paint ALL RMViews in the list (draw them in a circle)
		tm_view.setPosition((getWidth() - tm_view.RADIUS) / 2,
				(getHeight() - tm_view.RADIUS) / 2);
		tm_view.draw(g2);
		
		// Paint ALL RMViews in the list (draw them in a circle)
//		RMView vew = new RMView();
//		vew.setPosition(100, 100);
//		vew.setColor(Color.green);
//		vew.draw(g2);
	}
	
	public void addRMView(RMView v)
	{
		rm_views.add(v);
		repaint();
	}
}