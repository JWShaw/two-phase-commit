package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * One of the two views for the simulation: the graphical display at the right
 * of the window
 */
public class GraphicalView extends JPanel {
  private ArrayList<RMView> rm_views;
  private RMView tm_view;

  /**
   * Constructor
   */
  public GraphicalView() {
    this.setBackground(Color.WHITE);
    rm_views = new ArrayList<>();
    tm_view = new RMView();
  }

  /**
   * Redraws the view (use repaint() to call)
   */
  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    super.paintComponent(g2);

    // Draws the TM in the middle of its panel
    tm_view.setPosition((getWidth() - tm_view.RADIUS) / 2, (getHeight() - tm_view.RADIUS) / 2);
    tm_view.draw(g2);

    double theta = 2 * Math.PI / rm_views.size();

    // Draws the RMs in a circle around the TMView
    for (int i = 0; i < rm_views.size(); i++) {
      rm_views.get(i).setPosition((int) ((tm_view.getXpos()) + getWidth() / 4 * Math.sin(i * theta)),
          (int) ((tm_view.getYpos()) - getHeight() / 4 * Math.cos(i * theta)));
      rm_views.get(i).draw(g2);
    }
  }

  /**
   * Adds a new depiction of an RM to the simulation
   */
  public void addRMView() {
    rm_views.add(new RMView());
    repaint();
  }

  /**
   * @return The number of RMs depicted in the simulation
   */
  public int numRMViews() {
    return rm_views.size();
  }

  /**
   * Paints a specific RM depiction with a chosen color
   * 
   * @param id The unique ID of the depiction to be recolored
   * @param c  The desired color
   */
  public void colorRMView(int id, Color c) {
    rm_views.get(id).setColor(c);
    repaint();
  }

  /**
   * Paints the TMView a specific color
   * 
   * @param c The desired color
   */
  public void colorTMView(Color c) {
    tm_view.setColor(c);
    repaint();
  }

  /**
   * Resets the view to its default appearance so that a new simulation can occur
   */
  public void reset() {
    rm_views = new ArrayList<>();
    tm_view.setColor(Color.BLUE);
    repaint();
  }
}