/**
 * Box.java
 */

package uk.ac.surrey.rb00166.dotz;

import java.io.Serializable;

import android.graphics.Color;

/**
 * The parameterised constructor for the creation of a Box object.
 * 
 * @author Ryan
 */
public class Box implements Serializable {

  /** serialVersionUID */
  private static final long serialVersionUID = 9048332802578376849L;
  // Fields
  private int               xValue;
  private int               yValue;
  private int               colour           = Color.BLUE;
  private Line[]            lines;

  /**
   * The Box object is created here.
   * 
   * @param xValue
   *          The X value at which the Box will start at.
   * @param yValue
   *          The Y value at which the Box will start at.
   * @param topLine
   *          The Line object which makes up the top of the Box.
   * @param bottomLine
   *          The Line object which makes up the bottom of the Box.
   * @param leftLine
   *          The Line object which makes up the left side of the Box.
   * @param rightLine
   *          The Line object which makes up the right side of the Box.
   */
  public Box(int xValue, int yValue, Line topLine, Line bottomLine, Line leftLine, Line rightLine) {
    // Set up the Line array.
    this.lines = new Line[4];
    // Set the fields.
    this.xValue = xValue;
    this.yValue = yValue;
    // Fill the Line array with the objects.
    this.lines[0] = topLine;
    this.lines[1] = bottomLine;
    this.lines[2] = leftLine;
    this.lines[3] = rightLine;
  }

  /**
   * A boolean result which checks to see if a Box contains a line.
   * 
   * @param line
   *          The Line to check.
   * @return A boolean value if the Box contains the Line.
   */
  public boolean containsLine(Line line) {
    // Loop through the Line array and check for the line.
    for (Line tempLine : this.lines) {
      if (tempLine.equals(line)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Getter method for the colour of the Box.
   * 
   * @return The colour of the Box.
   */
  public int getColour() {
    return this.colour;
  }

  public Line[] getLines() {
    return this.lines;
  }

  /**
   * Getter method for the X position of the Box.
   * 
   * @return The X position of the Box.
   */
  public int getXValue() {
    return this.xValue;
  }

  /**
   * Getter method for the Y position of the Box.
   * 
   * @return The Y position of the Box.
   */
  public int getYValue() {
    return this.yValue;
  }

  /**
   * A boolean method to see if the Box is 'full'. ie. All the Line objects in the Box are selected.
   * 
   * @return If this Box is full.
   */
  public boolean isSelected() {
    // If all the lines are selected.
    if (this.lines[0].isSelected() && this.lines[1].isSelected() && this.lines[2].isSelected() && this.lines[3].isSelected()) {
      return true;
    }
    return false;
  }

  /**
   * A method which is called when one of the lines in the Box is pressed.
   * 
   * @param colour
   *          The colour of the line pressed.
   */
  public void wasClicked(int colour) {
    this.colour = colour;
  }
}
