/**
 * Dot.java
 */

package uk.ac.surrey.rb00166.dotz;

import java.io.Serializable;

/**
 * This class is a Dot object which is used to represent a physical Dot on screen.
 * 
 * @author Ryan Burke
 */
public class Dot implements Serializable {

  /** serialVersionUID */
  private static final long serialVersionUID = -4890995673397140599L;
  private int xValue; // The x value in pixels of the dot.
  private int yValue; // The y value in pixels of the dot.

  /**
   * The parameterised constructor for the creation of a Dot object.
   * 
   * @param xValue
   *          The X value at which the Dot will start at.
   * @param yValue
   *          The Y value at which the Dot will start at.
   */
  public Dot(int xValue, int yValue) {
    // Set the fields.
    this.xValue = xValue;
    this.yValue = yValue;
  }

  /**
   * Getter method for the X position of the Dot.
   * 
   * @return The X position of the Dot.
   */
  public int getXValue() {
    return this.xValue;
  }

  /**
   * Getter method for the Y position of the Dot.
   * 
   * @return The Y position of the Dot.
   */
  public int getYValue() {
    return this.yValue;
  }

}
