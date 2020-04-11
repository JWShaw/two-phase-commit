package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;

public class RMView
{
	private int xPos;
	private int yPos;
	private Color c;
	
	public RMView()
	{
		xPos = 0;
		yPos = 0;
	}
	
	public void setPosition(int x, int y)
	{
		xPos = x;
		yPos = y;
	}
	
	public void setColor(Color col)
	{
		c = col;
	}
	
	public void draw(Graphics2D g2)
	{
		Ellipse2D.Double fig = new Ellipse2D.Double(xPos, yPos, 32, 32);
		g2.setColor(c);
		g2.fill(fig);
		g2.draw(fig);
	}
}
