package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * One of the two views for the simulation: the graphical display at the right
 * of the window
 */
public class GraphicalView extends JPanel {
  private ArrayList<RmView> rmViewsList;
  private RmView tmView;

  /**
   * Constructor
   */
  public GraphicalView() {
    this.setBackground(Color.WHITE);
    rmViewsList = new ArrayList<>();
    tmView = new RmView("TM");
  }

  /**
   * Adds a new depiction of an RM to the simulation
   */
  public void addRmView() {
    String label = String.format("%02d", rmViewsList.size());
    rmViewsList.add(new RmView(label));
    repaint();
  }

  /**
   * @return The number of RMs depicted in the simulation
   */
  public int numRmViews() {
    return rmViewsList.size();
  }

  /**
   * Paints a specific RM depiction with a chosen color
   * 
   * @param id The unique ID of the depiction to be recolored
   * @param c  The desired color
   */
  public void colorRmView(int id, Color c) {
    rmViewsList.get(id).setColor(c);
    repaint();
  }

  /**
   * Paints the TMView a specific color
   * 
   * @param c The desired color
   */
  public void colorTmView(Color c) {
    tmView.setColor(c);
    repaint();
  }

  /**
   * Resets the view to its default appearance so that a new simulation can occur
   */
  public void reset() {
    rmViewsList = new ArrayList<>();
    tmView.setColor(Color.BLUE);
    repaint();
  }

  /**
   * Redraws the view (use repaint() to call)
   */
  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    super.paintComponent(g2);

    // Sets TM position to middle of the panel
    tmView.setPosition((getWidth() - tmView.RADIUS) / 2, (getHeight() - tmView.RADIUS) / 2);

    // Defines an angle in radians for which to separate the RM views
    double theta = 2 * Math.PI / rmViewsList.size();

    // Draws the RMs in a circle around the TMView
    for (int i = 0; i < rmViewsList.size(); i++) {
      
      // Defines the position of the RM on the frame
      Position rmPos = new Position((int) ((tmView.getXpos()) + getWidth() / 4 * Math.sin(i * theta)),
          (int) ((tmView.getYpos()) - getHeight() / 4 * Math.cos(i * theta)));
      
      rmViewsList.get(i).setPosition(rmPos.x, rmPos.y);
      
      // Draws a line from TM to RM for visual clarity in their relationship
      Line2D.Double connector = new Line2D.Double(rmPos.x + tmView.RADIUS/2, rmPos.y + tmView.RADIUS/2, 
          getWidth()/2, getHeight()/2);
      g2.setColor(Color.BLACK);
      g2.draw(connector);
      
      // Draws the RM
      rmViewsList.get(i).draw(g2);
    }
    
    // Draws the TM last
    tmView.draw(g2);
  }

  /**
   * A tiny tuple class (x,y)-coordinate tuple class used locally to keep things clean.
   */
  class Position
  {
    int x;
    int y;
    Position(int x, int y)
    {
      this.x = x;
      this.y = y;
    }
  }
  
}