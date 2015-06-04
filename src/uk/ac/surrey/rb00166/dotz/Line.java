/**
 * Line.java
 */

package uk.ac.surrey.rb00166.dotz;

import java.io.Serializable;

/**
 * This class is a Line object which is used to represent a physical line on screen.
 * 
 * @author Ryan Burke
 */
public class Line implements Serializable {

  /** serialVersionUID */
  private static final long serialVersionUID = 5987933259549382139L;
  private Dot     startDot;     //The dot at the start of the line.
  private Dot     endDot;       //The dot at the end of the line.
  private boolean selected;     //If the line is selected or not.
  private int     colour;       //The colour of the line.
  private String  orientation;  //If the line is horizontal or vertical.
  private int     i;            //The number of the line across.
  private int     j;            //The number of the line down.

  /**
   * The parameterised constructor for the creation of a Line object.
   * 
   * @param startDot
   *          The start of the Line will be a Dot object.
   * @param endDot
   *          The end of the Line will be a Dot object.
   */
  public Line(Dot startDot, Dot endDot, String orientation, int i, int j) {
    // Set the fields.
    this.startDot = startDot;
    this.endDot = endDot;
    this.selected = false;
    this.orientation = orientation;
    this.i = i;
    this.j = j;
  }

  /**
   * A method which de-selects a Line object.
   * 
   */
  public void deselect() {
    this.selected = false;
  }

  /**
   * Getter method for the colour of the Dot.
   * 
   * @return The colour of the Dot.
   */
  public int getColour() {
    return this.colour;
  }

  /**
   * Getter method for the ending Dot.
   * 
   * @return The end Dot object.
   */
  public Dot getEndDot() {
    return this.endDot;
  }

  /**
   * Getter method for the i value of the dot.
   * 
   * @return i
   */
  public int getI() {
    return this.i;
  }

  /**
   * Getter method for the j value of the dot.
   * 
   * @return k
   */
  public int getJ() {
    return this.j;
  }

  /**
   * Getter method for the orientation of the Dot.
   * 
   * @return The orientation of the Dot.
   */
  public String getOrientation() {
    return this.orientation;
  }

  /**
   * Getter method for the starting Dot.
   * 
   * @return The start Dot object.
   */
  public Dot getStartDot() {
    return this.startDot;
  }

  /**
   * A boolean method to check if the Line is selected.
   * 
   * @return If the Line is selected or not.
   */
  public boolean isSelected() {
    return this.selected;
  }

  /**
   * A method which is called when the Line has been clicked. It sets the colour of the Line and sets it to be selected.
   * 
   * @param colour
   *          The colour to set the Line to.
   */
  public void wasClicked(int colour) {
    this.colour = colour;
    this.selected = true;
  }

}