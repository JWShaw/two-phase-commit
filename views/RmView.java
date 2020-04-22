package views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 * A graphical representation of an RM (or TM) as a colored circle
 */
public class RmView {
  final int RADIUS = 32;
  private int xPos;
  private int yPos;
  private Color c;
  private String text;

  /**
   * Constructs a new RMView
   */
  public RmView() {
    xPos = 0;
    yPos = 0;
  }
  
  /**
   * Constructs a new RMView
   * @param s A 2-character string to display on the RM
   */
  public RmView(String s) {
    this();
    text = s;
  }

  /**
   * Sets the onscreen position of the RMView
   * 
   * @param x
   * @param y
   */
  public void setPosition(int x, int y) {
    xPos = x;
    yPos = y;
  }

  /**
   * @return The "x" position of the RMView on the panel
   */
  public int getXpos() {
    return xPos;
  }

  /**
   * @return The "y" position of the RMView on the panel
   */
  public int getYpos() {
    return yPos;
  }


  /**
   * Sets the color of the RMView
   * 
   * @param col The desired color
   */
  public void setColor(Color col) {
    c = col;
  }

  /**
   * Draws the RMView
   * 
   * @param g2 The graphics2D object with which the RMView is to be drawn
   */
  public void draw(Graphics2D g2) {
    Ellipse2D.Double fig = new Ellipse2D.Double(xPos, yPos, 32, 32);
    g2.setColor(c);
    g2.fill(fig);
    g2.draw(fig);
    
    if (text != null) {
      g2.setColor(Color.WHITE);
      g2.drawString(text, xPos + 7, yPos + 20);
    }
  }
}
