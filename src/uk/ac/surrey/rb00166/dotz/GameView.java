/**
 * GameInterface.java
 */

package uk.ac.surrey.rb00166.dotz;

import java.io.Serializable;

import uk.ac.surrey.rb00166.dotz.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

/**
 * The game interface controller.
 * 
 * @author Ryan Burke
 */
public class GameView extends View {

  private Paint                       dot;        // The paint for a dot.
  private Paint                       box;        // The paint for a box.
  private Paint                       scoreText;  // The paint for player info text.
  private Paint                       pressedLine; // The paint for a pressed line.
  private Context                     context;    // Context of the view.
  private final float                 scale;      // Scale for when we create our views. (DP)
  private Logic                       logic;      // Logic of the game.
  private MediaPlayer                 mediaPlayer; // Music Player makes the pop sound.
  private AIBot                       bot;        // Create the AIBot.
  private boolean                     usingBot;   // If we are playing against the CPU or a player.
  private Animation                   shake;      // Shake animation.
  private AsyncTask<Void, Void, Void> task;       // The Asynchronous task used to launch the AIBot thinking.
  private int                         width;      // The width of the view.
  private int                         height;     // The height of the view.
  private String                      player1;    // The name of player 1.
  private String                      player2;    // The name of player 2.

  /**
   * The GameView object which is an extension of View. Creates what the player will see when playing the game. Controls all user
   * interaction with the game and passes information through to the game Logic.
   * 
   * @param context
   *          The current state of the application.
   * @param player1
   *          The name of the player 1.
   * @param player2
   *          The name of the player 2.
   * @param colour1
   *          The colour chosen by player 1.
   * @param colour2
   *          The colour chosen by player 2.
   * @param gridSize
   *          The size of the grid used for the game.
   * @param usingBot
   *          If we are playing against the computer or not.
   */
  public GameView(Context context, String player1, String player2, int colour1, int colour2, String gridSize, boolean usingBot) {
    super(context);

    // Create logic controller using width and height of the view.
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    this.width = display.getWidth();
    this.height = display.getHeight();
    this.logic = new Logic(gridSize, this.width, this.height, colour1, colour2);
    // Initialise fields.
    this.player1  = player1;
    this.player2 = player2;
    this.context = context;
    this.shake = AnimationUtils.loadAnimation(this.context, R.anim.shake);
    this.scale = this.getResources().getDisplayMetrics().density;
    this.mediaPlayer = MediaPlayer.create(context, R.raw.pop);
    this.usingBot = usingBot;
    if (this.usingBot == true) {
      this.bot = new AIBot(this.logic);
    }
    //Set up the task running the AIBot. 
    this.setupTask();
    // Initialise paint objects.
    this.pressedLine = new Paint();
    this.pressedLine.setStrokeWidth(5 * this.scale);
    this.box = new Paint();
    this.scoreText = new Paint();
    this.scoreText.setColor(Color.parseColor("#FFFFFF"));
    this.scoreText.setTextSize(20 * this.scale);
    this.scoreText.setAntiAlias(true);
    this.dot = new Paint();
    this.dot.setColor(Color.WHITE);
    this.dot.setAntiAlias(true);

  }

  /**
   * A method that is called when a user selects a line that has already been selected. They will be presented with a toast
   * notification and the screen will shake.
   */
  private void alreadySelected() {
    Toast.makeText(this.context, R.string.lineSelected, Toast.LENGTH_SHORT).show();
    this.startAnimation(this.shake);
  }

  /**
   * Check the touch event which happened, determine which line was pressed.
   * 
   * @param x
   *          The X coordinate pressed.
   * @param y
   *          The Y coordinate pressed.
   * @return
   */
  private void calculateLine(int x, int y) {

    //Logic variables.
    boolean verticalPressed = false;
    boolean horizontalPressed = false;
    Line pressedLine = null;

    // If we are playing against the bot and it is the bots turn.
    if (this.usingBot == true && this.logic.getPlayer() == 2) {
      this.notYourTurn();
    }
    else {
      // Go through each vertical line.
      for (Line[] verticalLines : this.logic.getVerticalLines()) {
        for (Line verticalLine : verticalLines) {
          // If the y value touched was between this lines Y values.
          if (y > Math.abs(verticalLine.getStartDot().getYValue()) && y < Math.abs(verticalLine.getEndDot().getYValue())) {
            // If the touch was between 10px left or right of the line.
            if (Math.abs(verticalLine.getStartDot().getXValue() - x) < 10 * this.scale) {
              verticalPressed = true;
              pressedLine = verticalLine;
            }
          }
        }
      }
      // Some optimisation, if we have already found a vertical line no point searching for a horizontal one.
      if (verticalPressed != true) {
        // Go through each horizontal line.
        for (Line[] horizontalLines : this.logic.getHorizontalLines()) {
          for (Line horizontalLine : horizontalLines) {
            // If the x value touched was between this lines X values.
            if (x > Math.abs(horizontalLine.getStartDot().getXValue()) && x < Math.abs(horizontalLine.getEndDot().getXValue())) {
              // If the touch was between 10px below or above of the line.
              if (Math.abs(horizontalLine.getStartDot().getYValue() - y) < 10 * this.scale) {
                horizontalPressed = true;
                pressedLine = horizontalLine;
              }
            }
          }
        }
      }

      //If a vertical Line was pressed then do the veritcalPressed method.
      if (verticalPressed == true) {
        this.verticalPressed(pressedLine.getI(), pressedLine.getJ());
      }
      //If a horizontal Line was pressed then do the horizontalPressed method.
      else if (horizontalPressed == true) {
        this.horizontalPressed(pressedLine.getI(), pressedLine.getJ());
      }
      //If either was pressed then make a sound.
      if (verticalPressed == true || horizontalPressed == true) {
        // If sound is on then make a sound.
        if (Rb00166_DotzActivity.soundOn == true) {
          this.mediaPlayer.start();
        }

      }
    }

  }

  /**
   * This method is called from the onDraw method and manages the drawing of the Boxes.
   * 
   * @param canvas The canvas to draw on.
   */
  public void drawBoxes(Canvas canvas) {
    // Go through each box.
    for (Box[] boxRow : this.logic.getBoxes()) {
      for (Box tempBox : boxRow) {
        //If the box is selected.
        if (tempBox.isSelected()) {
          //Set the colour of the box.
          this.box.setColor(tempBox.getColour());
          this.box.setAlpha(70);
          //Get the width and height of the box.
          int boxWidth = this.logic.getGrid()[1][0].getXValue() - this.logic.getGrid()[0][0].getXValue();
          int boxHeight = this.logic.getGrid()[0][1].getYValue() - this.logic.getGrid()[0][0].getYValue();
          //Draw the box onto the canvas.
          canvas.drawRect(tempBox.getXValue(), tempBox.getYValue(), tempBox.getXValue() + boxWidth,
              tempBox.getYValue() + boxHeight, this.box);
        }
      }
    }
  }

  /**
   * This method is called from the onDraw method and manages the drawing of the dots which make up the grid.
   * 
   * @param canvas The canvas to draw on.
   */
  public void drawDots(Canvas canvas) {
    // Go through each dot.
    for (int i = 0; i < this.logic.getGrid().length; i++) {
      for (int j = 0; j < this.logic.getGrid()[0].length; j++) {
        //Get its x and y location and draw it onto the canvas.
        canvas.drawCircle(this.logic.getGrid()[i][j].getXValue(), this.logic.getGrid()[i][j].getYValue(), 7 * this.scale, this.dot);
      }
    }
  }

  /**
   * This method is called from the onDraw method and manages the drawing of the horizontalLines between the dots on the grid.
   * 
   * @param canvas The canvas to draw on.
   */
  public void drawHorizontalLines(Canvas canvas) {
    // Go through each horizontal line.
    for (int i = 0; i < this.logic.getHorizontalLines().length; i++) {
      for (int j = 0; j < this.logic.getHorizontalLines()[0].length; j++) {
        //If the line is selected.
        if (this.logic.getHorizontalLines()[i][j].isSelected()) {
          //Set the colour of the line.
          this.pressedLine.setColor(this.logic.getHorizontalLines()[i][j].getColour());
          //Draw the line onto the canvas using the values of its end dots.
          canvas.drawLine(this.logic.getHorizontalLines()[i][j].getStartDot().getXValue(), this.logic.getHorizontalLines()[i][j]
              .getStartDot().getYValue(), this.logic.getHorizontalLines()[i][j].getEndDot().getXValue(), this.logic
              .getHorizontalLines()[i][j].getEndDot().getYValue(), this.pressedLine);
        }
      }
    }
  }

  /**
   * This method is called from the onDraw method and manages the drawing of the players name and current score onto the screen.
   * 
   * @param canvas The canvas to draw on.
   */
  public void drawNames(Canvas canvas) {

    // Get strings and width/heights for formatting.
    String player1Text = (this.player1 + "   Score: " + this.logic.getPlayer1Score());
    String player2Text = (this.player2 + "   Score: " + this.logic.getPlayer2Score());
    Rect rect = new Rect();
    this.scoreText.setTextAlign(Align.CENTER);
    this.scoreText.getTextBounds(player1Text, 0, player1Text.length(), rect);
    this.height = Math.abs(rect.bottom - rect.top);
    Paint boldText = new Paint();
    boldText.set(this.scoreText);
    boldText.setTypeface(Typeface.DEFAULT_BOLD);

    // Depending on whose turn it is bold their text.
    switch (this.logic.getPlayer()) {
      case 1:
        canvas.drawText(player1Text, this.width / 2, this.height + (10 * this.scale), boldText);
        canvas.drawText(player2Text, this.width / 2, this.height * 2 + (15 * this.scale), this.scoreText);
        break;
      case 2:
        canvas.drawText(player1Text, this.width / 2, this.height + (10 * this.scale), this.scoreText);
        canvas.drawText(player2Text, this.width / 2, this.height * 2 + (15 * this.scale), boldText);
        break;
    }
  }

  /**
   * This method is called from the onDraw method and manages the drawing of the verticalLines between the dots on the grid.
   * 
   * @param canvas The canvas to draw on.
   */
  public void drawVerticalLines(Canvas canvas) {
    // Go through each vertical line.
    for (int i = 0; i < this.logic.getVerticalLines().length; i++) {
      for (int j = 0; j < this.logic.getVerticalLines()[0].length; j++) {
        //If the line is selected.
        if (this.logic.getVerticalLines()[i][j].isSelected()) {
          //Set the colour of the line.
          this.pressedLine.setColor(this.logic.getVerticalLines()[i][j].getColour());
          //Draw the line onto the canvas using the values of its end dots.
          canvas.drawLine(this.logic.getVerticalLines()[i][j].getStartDot().getXValue(), this.logic.getVerticalLines()[i][j]
              .getStartDot().getYValue(), this.logic.getVerticalLines()[i][j].getEndDot().getXValue(), this.logic
              .getVerticalLines()[i][j].getEndDot().getYValue(), this.pressedLine);
        }
      }
    }
  }

  /**
   * Gets the current logic of the game so it can be saved by the activity.
   * 
   * @return The logic.
   */
  public Serializable getLogic() {
    return this.logic;
  }

  /**
   * The method which is called when a horizontal line has been pressed.
   * 
   * @param i The i value of the line across the grid.
   * @param j The j value of the line down the grid.
   */
  private void horizontalPressed(int i, int j) {
    // Send the press to game logic.
    if (!this.logic.horizontalPressed(i, j)) {
      // If it returns false then it was already selected.
      this.alreadySelected();
    }
    // Else, player 1 has been pressed, and if we are using the bot.
    else if (this.usingBot == true) {
      // Set up our task again.
      this.setupTask();
      // Execute the task.
      this.task.execute((Void[]) null);
    }
    // If the game has ended show the alert box.
    if (this.logic.gameEnded()) {
      this.showWinner();
    }
    // Redraw the canvas.
    this.invalidate();
  }

  private void notYourTurn() {
    // Show a toast message.
    Toast.makeText(this.context, R.string.notyourturn, Toast.LENGTH_SHORT).show();
    // Shake the screen to get user attention.
    this.startAnimation(this.shake);
  }

  /**
   * The onDraw method which is called whenever the screen is invalidated or loaded.
   * 
   * @param canvas The canvas on which to draw on.
   *
   * @see android.view.View#onDraw(android.graphics.Canvas)
   */
  @Override
  protected void onDraw(Canvas canvas) {
    //Draw the names of the players.
    this.drawNames(canvas);
    //First draw the boxes so everything else appears on top of it.
    this.drawBoxes(canvas);
    //Draw both sets of lines.
    this.drawHorizontalLines(canvas);
    this.drawVerticalLines(canvas);
    //Finally draw the dots (grid) on top of the lines and boxes.
    this.drawDots(canvas);
  }

  /**
   * When a touch is detected, work out which MotionEvent it was and send the co-ordinates pressed to the check_touch() method for
   * processing.
   * 
   * @param event
   * @return Telling super there was a touchEvent.
   * 
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    //If the event was a touch down event then calculate the line with those coordinates pressed.
    if(event.getAction() == MotionEvent.ACTION_DOWN) this.calculateLine((int) event.getX(), (int) event.getY());
    return super.onTouchEvent(event);
  }

  /**
   * When the reset Button is clicked we want to present an alert box to the user for final confirmation before we reset the logic.
   * 
   */
  protected void resetClicked() {
    //Present the user with an alert asking if they are sure they want to reset the game.
    new AlertDialog.Builder(this.context).setIcon(R.drawable.icon).setTitle(R.string.resetTitle).setMessage(R.string.resetMessage)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int whichButton) {
            //Call the reset method.
            GameView.this.resetGrid();
          }
        }).setNegativeButton(R.string.no, null).create().show();
  }

  /**
   * Resets the game.
   */
  public void resetGrid() {
    //Reset the game logic.
    this.logic.resetGame();
    //Show these changes.
    this.invalidate();
  }

  /**
   * Sets the logic of the game so we can restore its state.
   * 
   * @param serializable The logic we want to set.
   * @return
   */
  public void setLogic(Serializable serializable) {
    //Set the logic.
    this.logic = (Logic) serializable;
  }

  /**
   * Set up the task which takes the turns of the AIBot. Needs to be set up every time it is called because an AsyncTask can only be run once.
   * Lets us process in the background without lagging the UI thread and it allows us to give the impression of the computer thinking and
   * give a small delay in the press being made rather than it happening instantly. This was mainly due to the user not being able to see where the AIBot 
   * had gone after they made their move.
   * 
   */
  private void setupTask() {
    //Set up the task.
    this.task = new AsyncTask<Void, Void, Void>() {
      //In the background we want to do this.
      @Override
      protected Void doInBackground(Void... params) {
        try {
          //Sleep for half a second to give impression of thinking time.
          Thread.sleep(500);
          //If a box was not created.
          if (!GameView.this.logic.boxWasSelected()) {
            //Set the logic of the GameView to the result of the AIBot thinking. 
            GameView.this.setLogic(GameView.this.bot.think(GameView.this.logic));
          }
        }
        catch (InterruptedException e) {
        }
        return null;
      }

      /**
       * After it has been executed.
       * 
       * @param result The result of doInBackground
       *
       * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
       */
      @Override
      protected void onPostExecute(Void result) {
        //Invalidate the view.
        GameView.this.invalidate();
        //If the game has ended after the AIBot's turn then show the winner.
        if (GameView.this.logic.gameEnded() && GameView.this.logic.getPlayer() == 2) {
          GameView.this.showWinner();
        }
      }
    };
  }

  /**
   * A method which shows the winner of the game through an alertbox and is called at the end of the game.
   */
  private void showWinner() {

    // Set up the winner alertbox.
    AlertDialog winner = new AlertDialog.Builder(this.context).create();
    winner.setTitle(R.string.gameComplete);
    winner.setIcon(R.drawable.icon);
    winner.setButton(this.context.getString(R.string.thanks), (new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int whichButton) {
        //Call the game reset.
        GameView.this.resetGrid();
      }
    }));
    // If there is a draw.
    if (this.logic.getPlayer1Score() == this.logic.getPlayer2Score()) {
      winner.setMessage(this.context.getString(R.string.drawText));
    }
    // Else someone must have won.
    else {
      // Create an empty string.
      String winnerString = null;
      // Set string to whoever won.
      if (this.logic.getPlayer1Score() > this.logic.getPlayer2Score()) {
        winnerString = this.player1;
      }
      else {
        winnerString = this.player2;
      }
      winner.setMessage(this.context.getString(R.string.congratulations) + " " + winnerString
          + this.context.getString(R.string.winner));
      if (this.usingBot == true && (this.logic.getPlayer1Score() < this.logic.getPlayer2Score())) {
        winner.setMessage(this.context.getString(R.string.beaten));
      }
    }
    winner.show();
  }

  /**
   * When the undo button has been pressed. This is called and acts as a way of the activity talking to the logic.
   */
  public void undoPressed() {
    //Call the undo method in logic.
    this.logic.undo();
    // Invalidate the canvas.
    this.invalidate();
  }

  /**
   * The method which is called when a vertical line has been pressed.
   * 
   * @param i The i value of the line across the grid.
   * @param j The j value of the line down the grid.
   */
  private void verticalPressed(int i, int j) {

    // Send the press to game logic.
    if (!this.logic.verticalPressed(i, j)) {
      // If it returns false then it was already selected.
      this.alreadySelected();
    }
    // Else, player 1 has been pressed, and if we are using the bot.
    else if (this.usingBot == true) {
      // Set up our task again.
      this.setupTask();
      // Execute the task.
      this.task.execute((Void[]) null);
    }
    // If the game has ended show the alert box.
    if (this.logic.gameEnded()) {
      this.showWinner();
    }
    // Redraw the canvas.
    this.invalidate();

  }

}
