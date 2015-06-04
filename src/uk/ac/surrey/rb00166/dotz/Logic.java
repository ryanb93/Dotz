/**
 * Logic.java
 */

package uk.ac.surrey.rb00166.dotz;

import java.io.Serializable;
import java.util.Stack;

/**
 * @author Ryan
 */
public class Logic implements Serializable {

  private static final long serialVersionUID = 1783675515988705549L;
  
  /* Objects fields. */
  private Dot[][]           grid;                 //The grid made of Dot objects.
  private Line[][]          horizontalLines;      //The horizontal lines.
  private Line[][]          verticalLines;        //The vertical lines.
  private Box[][]           boxes;                //The boxes.
  private Stack<Line>       pressedLines;         //Lines that have been pressed.
  /* Display fields. */
  private int               displayWidth;         //The width of the display.
  private int               displayHeight;        //The height of the display.
  /* Player 1 fields. */ 
  private int               score1           = 0; //Player 1 score.
  private int               player1Colour;        //The colour of player 1.
  /* Player 2 fields. */
  private int               score2           = 0; //Player 2 score.
  private int               player2Colour;        //The colour of player 2.
  /* Game info fields. */
  private int               currentPlayer    = 1; //The current player.
  private boolean           aBoxWasSelected;       //If a box was created.

  
  /**
   * The logic constructor which creates a logic object.
   * 
   * @param gridSize The size of the grid.
   * @param width    The width of the view.
   * @param height   The height of the view.
   * @param player1  The name of player 1.
   * @param player2  The name of player 2.
   * @param colour1  The colour of player 1.
   * @param colour2  The colour of player 2.
   */
  public Logic(String gridSize, int width, int height, int colour1, int colour2) {
    //Set the display diameters.
    this.displayWidth = width;
    this.displayHeight = height;
    // Set up our stack.
    this.pressedLines = new Stack<Line>();
    // Set the player info.
    this.player1Colour = colour1;
    this.player2Colour = colour2;

    // Create dots for the grid.
    if (gridSize.equalsIgnoreCase("Small")) {
      this.createDots(5);
    }
    if (gridSize.equalsIgnoreCase("Medium")) {
      this.createDots(6);
    }
    if (gridSize.equalsIgnoreCase("Large")) {
      this.createDots(7);
    }
    //Create lines based on dots.
    this.createLines();
    //Create boxes based on lines.
    this.createBoxes();
  }

  /**
   * Method which checks if a box had been selected.
   * 
   * @return the aBoxWasSelected
   */
  public boolean boxWasSelected() {
    return this.aBoxWasSelected;
  }

  /**
   * The method which creates Box objects.
   */
  private void createBoxes() {
    // Initialise the grid of dots.
    this.boxes = new Box[this.grid.length - 1][this.grid[0].length - 1];
    // Fill the grid with dots.
    for (int i = 0; i < this.boxes.length; i++) {
      for (int j = 0; j < this.boxes[0].length; j++) {
        this.boxes[i][j] = new Box(this.grid[i][j].getXValue(), this.grid[i][j].getYValue(),
        // Top Line.
            this.horizontalLines[i][j],
            // Bottom Line.
            this.horizontalLines[i][j + 1],
            // Left Line.
            this.verticalLines[i][j],
            // Right Line.
            this.verticalLines[i + 1][j]);
      }
    }
  }

  /**
   * The method which creates Dot objects.
   * 
   * @param The number of dots to create each way.
   */
  private void createDots(int x) {

    // Set each dot to take up about a 6th of the screen.
    int xOffset = this.displayWidth / (x + 1);
    int yOffset = this.displayHeight / (x + 5);

    // Initialise the grid of dots.
    this.grid = new Dot[x][x];
    // Fill the grid with dots.
    for (int i = 0; i < this.grid.length; i++) {
      for (int j = 0; j < this.grid[0].length; j++) {
        // Top left dot
        if (i == 0 && j == 0) {
          this.grid[i][j] = new Dot((xOffset), (yOffset * 2));
        }
        else {
          // First column
          if (i == 0) {
            this.grid[i][j] = new Dot(this.grid[i][j - 1].getXValue(), this.grid[i][j - 1].getYValue() + yOffset);
          }
          // First row
          else if (j == 0) {
            this.grid[i][j] = new Dot(this.grid[i - 1][j].getXValue() + xOffset, this.grid[i - 1][j].getYValue());
          }
          // Everything else
          else if (i != 0 && j != 0) {
            this.grid[i][j] = new Dot(this.grid[i - 1][j - 1].getXValue() + xOffset, this.grid[i - 1][j - 1].getYValue() + yOffset);
          }

        }
      }
    }
  }

  /**
   * The method which creates line objects.
   */
  private void createLines() {

    //Set up fields.
    this.horizontalLines = new Line[this.grid.length - 1][this.grid[0].length];
    this.verticalLines = new Line[this.grid.length][this.grid[0].length - 1];

    // We want a line between every dot horizontally and vertically.
    // Going through a column.
    for (int i = 0; i < this.horizontalLines.length; i++) {
      // Going through each row.
      for (int j = 0; j < this.horizontalLines[i].length; j++) {
        // Create horizontal line across.
        this.horizontalLines[i][j] = (new Line(this.grid[i][j], this.grid[i + 1][j], "horizontal", i, j));
      }
    }
    for (int i = 0; i < this.verticalLines.length; i++) {
      // Going through each row.
      for (int j = 0; j < this.verticalLines[i].length; j++) {
        // Create vertical line down.
        this.verticalLines[i][j] = (new Line(this.grid[i][j], this.grid[i][j + 1], "vertical", i, j));
      }
    }
  }

  /**
   * The method which checks to see if the game has ended.
   * 
   * @return If the game has ended or not.
   */
  public boolean gameEnded() {
    //Go through each box.
    for (int i = 0; i < this.boxes.length; i++) {
      for (int j = 0; j < this.boxes[0].length; j++) {
        //If the is a box which is not selected then the game hasnt ended.
        if (!this.boxes[i][j].isSelected()) {
          return false;
        }
      }
    }
      //If it has got through to here it means all boxes were selected.
      return true;
  }

  /**
   * The method which returns the Box objects.
   * 
   * @return The boxes contained in logic.
   */
  public Box[][] getBoxes() {
    return this.boxes;
  }

  /**
   * The method which returns the Dot objects.
   * 
   * @return The Dot objects contained in logic.
   */
  public Dot[][] getGrid() {
    return this.grid;
  }

  /**
   * The method which returns the Line objects in horizontalLines.
   * 
   * @return The horizontal Lines.
   */
  public Line[][] getHorizontalLines() {
    return this.horizontalLines;
  }

  /**
   * The method which gets the current player.
   * 
   * @return The current player number.
   */
  public int getPlayer() {
    return this.currentPlayer;
  }

  /**
   * The method which gets the player 1 score.
   * 
   * @return Player 1 score.
   */
  public int getPlayer1Score() {
    return this.score1;
  }

  /**
   * The method which gets the player 2 score.
   * 
   * @return Player 2 score.
   */
  public int getPlayer2Score() {
    return this.score2;
  }

  /**
   * The method which returns the Line objects in verticalLines.
   * 
   * @return The vertical Lines.
   */
  public Line[][] getVerticalLines() {
    return this.verticalLines;
  }

  /**
   * Called from GameView when a horizontal line has been pressed. It tells linePressed() that a line has been pressed.
   * It also acts as an indicator to whoever called it if this line has already been pressed or not.
   * 
   * @param i The i value of the line pressed.
   * @param j The j value of the line pressed.
   * @return If the line has already been selected or not.
   */
  public boolean horizontalPressed(int i, int j) {
    // Check to see if the line has already been pressed.
    if (this.horizontalLines[i][j].isSelected()) {
      return false;
    }
    else {
      this.linePressed(this.horizontalLines[i][j]);
      return true;
    }
  }

  /**
   * This method deals with pressing the line and making it selected.
   * It also deals with the selection of a box.
   * 
   * @param line The line to be pressed.
   */
  public void linePressed(Line line) {
    //Empty colour.
    int colour = 0;
    //Push the line to the stack so we can undo later.
    this.pressedLines.push(line);
    //Get the current player.
    switch (this.currentPlayer) {
      //If its player one.
      case 1:
        colour = this.player1Colour;
        line.wasClicked(this.player1Colour);
        break;
      //If its player two.
      case 2:
        colour = this.player2Colour;
        line.wasClicked(this.player2Colour);
        break;
    }
    //Switch the player.
    this.switchPlayer();
    //Have we made a box.
    boolean boxMade = false;
    //Go through each box.
    for (Box[] boxRow : this.boxes) {
      for (Box tempBox : boxRow) {
        //If the box contains the line we just pressed.
        if (tempBox.containsLine(line)) {
          //Click the box with the colour of the player who pressed the line.
          tempBox.wasClicked(colour);
          //If the box is selected.
          if (tempBox.isSelected()) {
            //We made a box!
            boxMade = true;
            //Get the colour of the box.
            if (tempBox.getColour() == this.player1Colour) {
              //Increment the score of the player 1.
              this.score1++;
              //Give player 1 another go.
              this.currentPlayer = 1;
            }
            else {
              //Increment the score of the player 2.
              this.score2++;
              //Give player 2 another go.
              this.currentPlayer = 2;
            }
          }
        }
      }
    }
    //If a box was made set aBoxWasSelected to true.
    if (boxMade == true) this.aBoxWasSelected = true;
    else this.aBoxWasSelected = false;
  }

  /**
   * This method resets the game.
   */
  public void resetGame() {
    // Reset score and turn.
    this.score1 = 0;
    this.score2 = 0;
    this.currentPlayer = 1;
    this.aBoxWasSelected = false;
    this.pressedLines.clear();
    // Clear selections.
    for (int i = 0; i < this.horizontalLines.length; i++) {
      for (int j = 0; j < this.horizontalLines[0].length; j++) {
        this.horizontalLines[i][j].deselect();
      }
    }
    for (int i = 0; i < this.verticalLines.length; i++) {
      for (int j = 0; j < this.verticalLines[0].length; j++) {
        this.verticalLines[i][j].deselect();
      }
    }
  }

  /**
   * This method switches the player.
   */
  public void switchPlayer() {
    if (this.currentPlayer == 1) {
      this.currentPlayer = 2;
    }
    else {
      this.currentPlayer = 1;
    }
  }

  /** This method undoes the last line pressed.
   */
  public void undo() {
    //If the stack is not empty.
    if (!this.pressedLines.empty()) {
      // Get the last pressed Line from the stack
      Line line = this.pressedLines.pop();

      //Go through each box.
      for (Box[] boxRow : this.boxes) {
        for (Box tempBox : boxRow) {
          //If the box contains the line to undo.
          if (tempBox.containsLine(line)) {
            //If the box was selected.
            if (tempBox.isSelected()) {
              //Remove the score.
              if (tempBox.getColour() == this.player1Colour) {
                this.score1--;
                this.currentPlayer = 2;
              }
              else {
                this.score2--;
                this.currentPlayer = 1;
              }
            }
          }
        }
      }
      //Deselect the line.
      line.deselect();
      //Switch players.
      this.switchPlayer();
    }
  }

  /**
   * Called from GameView when a vertical line has been pressed. It tells linePressed() that a line has been pressed.
   * It also acts as an indicator to whoever called it if this line has already been pressed or not.
   * 
   * @param i The i value of the line pressed.
   * @param j The j value of the line pressed.
   * @return If the line has already been selected or not.
   */
  public boolean verticalPressed(int i, int j) {
    // Check to see if the line has already been pressed.
    if (this.verticalLines[i][j].isSelected()) {
      return false;
    }
    else {
      this.linePressed(this.verticalLines[i][j]);
      return true;
    }
  }
}
