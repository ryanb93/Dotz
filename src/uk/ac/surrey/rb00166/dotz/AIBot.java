/**
 * AIBot.java
 */

package uk.ac.surrey.rb00166.dotz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The class which implements a basic artificial intelligence and acts as the player 2 when user plays against the CPU.
 * 
 * @author Ryan
 */
public class AIBot {

  private Logic      logic;       // A local version of the logic controller.
  private List<Line> normalLines; // Lines which are normal priority.
  private List<Line> goodLines;   // Lines which are top priority.
  private List<Line> badLines;    // Lines which are bottom priority.
  private int        botLevel;    // The level of the bot. Between 0 and 2, with 2 being hardest.

  /**
   * Constructor of the AIBot. It takes a logic object and sets up its Lists
   * .
   * @param logic The logic from the game to be processed.
   */
  public AIBot(Logic logic) {
    // The Bot knows the same as the user in terms of the game logic.
    this.logic = logic;
    // Set up array lists and the bots level.
    this.normalLines = new ArrayList<Line>();
    this.goodLines = new ArrayList<Line>();
    this.badLines = new ArrayList<Line>();
    this.botLevel = Rb00166_DotzActivity.botLevel;
  }

  /**
   * This method will find the lines available from the game logic and work out the priority in which the bot should press them. 
   * It then puts these lines in the correct Lists. It does this by getting a box, seeing how many of the lines inside it are selected
   * and then based on the fact that if 3 lines are selected already, this box will only need one more press to score a point.
   */
  private void findLinesToPress() {
    // Loop through each box.
    for (Box[] boxRow : this.logic.getBoxes()) {
      for (Box box : boxRow) {
        // If the box is not full.
        if (!box.isSelected()) {
          List<Line> tempLines = new ArrayList<Line>();
          // Get the non-selected lines in the box.
          for (Line tempLine : box.getLines()) {
            if (!tempLine.isSelected()) {
              tempLines.add(tempLine);
            }
          }
          //Now go through each line found.
          for (Line freeLine : tempLines) {
            // Bot is easy
            if (this.botLevel == 0) {
              //Just add all the lines to be normal.
              this.normalLines.add(freeLine);
            }
            // Bot is medium
            if (this.botLevel == 1) {
              switch (tempLines.size()) {
                case 1:
                  this.goodLines.add(freeLine);
                  break;
                case 2: // Drop through
                case 3: // Drop through
                case 4:
                  this.normalLines.add(freeLine);
                  break;
              }
            }
            // Bot is hard
            if (this.botLevel == 2) {
              // How many lines in this box have already been pressed?
              switch (tempLines.size()) {
                case 1:
                  this.goodLines.add(freeLine);
                  break;
                case 2:
                  this.badLines.add(freeLine);
                  break;
                case 3:
                  this.normalLines.add(freeLine);
                  break;
                case 4:
                  this.normalLines.add(freeLine);
                  break;
              }
            }
          }
        }
      }
    }
  }

  /**
   * The main logic within the AIBot class. This is passed a logic object and will call the findLinesToPress() method to create a the lists of priority.
   * This method will then chose random numbers from those lists starting with good lines first, then normal lines, and lastly bad lines. This creates
   * the perception of intelligence when playing against the computer.
   * 
   * @param logic The logic from the game.
   * @return The new logic after it has been modified by the bot.
   */
  public Logic think(Logic logic) {
    // See if the botLevel has changed.
    this.botLevel = Rb00166_DotzActivity.botLevel;
    // Replace logic with most current version.
    this.logic = logic;
    // Reset lines to press.
    this.normalLines = new ArrayList<Line>();
    this.goodLines = new ArrayList<Line>();
    this.badLines = new ArrayList<Line>();
    // Create the list of lines we can press.
    this.findLinesToPress();
    // Pick a random number in the range of length of the linesToPress.
    Random rand = new Random();
    // If there are good lines to press.
    if (this.goodLines.size() != 0) {
      // Chose a random line from the list.
      Line lineToPress = this.goodLines.get(rand.nextInt(this.goodLines.size()));
      // If the line is a horizontal line.
      if (lineToPress.getOrientation() == "horizontal") {
        // Press the line in our local version of logic.
        this.logic.horizontalPressed(lineToPress.getI(), lineToPress.getJ());
      }
      // Else if the line is a vertical line.
      else if (lineToPress.getOrientation() == "vertical") {
        // Press the line in our local version of logic.
        this.logic.verticalPressed(lineToPress.getI(), lineToPress.getJ());
      }
      //If a box was created then go again.
      if (this.logic.boxWasSelected()) {
        this.think(this.logic);
      }
    }
    // If there are good lines to press.
    else if (this.normalLines.size() != 0) {
      // Chose a random line from the list.
      Line lineToPress = this.normalLines.get(rand.nextInt(this.normalLines.size()));
      // If the line is a horizontal line.
      if (lineToPress.getOrientation() == "horizontal") {
        // Press the line in our local version of logic.
        this.logic.horizontalPressed(lineToPress.getI(), lineToPress.getJ());
      }
      // Else if the line is a vertical line.
      else if (lineToPress.getOrientation() == "vertical") {
        // Press the line in our local version of logic.
        this.logic.verticalPressed(lineToPress.getI(), lineToPress.getJ());
      }
      //If a box was created then go again.
      if (this.logic.boxWasSelected()) {
        this.think(this.logic);
      }
    }
    // Finally if there are bad lines to press.
    else if (this.badLines.size() != 0) {
      // Chose a random line from the list.
      Line lineToPress = this.badLines.get(rand.nextInt(this.badLines.size()));
      // If the line is a horizontal line.
      if (lineToPress.getOrientation() == "horizontal") {
        // Press the line in our local version of logic.
        this.logic.horizontalPressed(lineToPress.getI(), lineToPress.getJ());
      }
      // Else if the line is a vertical line.
      else if (lineToPress.getOrientation() == "vertical") {
        // Press the line in our local version of logic.
        this.logic.verticalPressed(lineToPress.getI(), lineToPress.getJ());
      }
      //If a box was created then go again.
      if (this.logic.boxWasSelected()) {
        this.think(this.logic);
      }
    }
    return this.logic;
  }
}
