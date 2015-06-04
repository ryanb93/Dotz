/**
 * Game.java
 */

package uk.ac.surrey.rb00166.dotz;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * The activity which launches the game.
 * 
 * @author Ryan Burke
 */
public class GameActivity extends Rb00166_DotzActivity {

  private String          playerOneName;  // The name of player one.
  private String          playerTwoName;  // The name of player two.
  private static GameView gameView;       // The instance of the gameView view.
  float                   scale;          // The density ratio of the screen.

  /**
   * Method to create the interface to be seen by the user.
   */
  private void createInterface() {

    // Create a main linear layout view
    LinearLayout mainView = new LinearLayout(this);
    mainView.setOrientation(LinearLayout.VERTICAL);
    mainView.setBackgroundColor(Color.parseColor("#282828"));

    // Create layout parameters for the main view.
    LinearLayout.LayoutParams mainParam = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

    // Create layout parameters for the gameView.
    LinearLayout.LayoutParams gameParam = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    gameParam.gravity = Gravity.BOTTOM;
    gameParam.weight = 0.5f;

    // Create reset button.
    Button resetButton = new Button(this);
    resetButton.setText("Reset");
    resetButton.setOnClickListener(new Button.OnClickListener() {

      @Override
      public void onClick(View arg0) {
        gameView.resetClicked();
      }
    });

    // Create undo Button.
    Button undoButton = new Button(this);
    undoButton.setText("Undo");
    undoButton.setOnClickListener(new Button.OnClickListener() {

      @Override
      public void onClick(View arg0) {
        gameView.undoPressed();
      }
    });

    // Create an inner view containing the buttons.
    LinearLayout buttonView = new LinearLayout(this);
    buttonView.setOrientation(LinearLayout.HORIZONTAL);

    // Create layout parameters for the buttons.
    LinearLayout.LayoutParams buttonsParam = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    buttonsParam.weight = 0.5f;
    buttonView.setPadding(0, 0, 0, (int) (10 * this.scale));

    // Add the buttons to the inner view.
    buttonView.addView(resetButton, buttonsParam);
    buttonView.addView(undoButton, buttonsParam);

    // Create layout parameters for the buttonView.
    LinearLayout.LayoutParams buttonParam = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    buttonParam.gravity = Gravity.BOTTOM;

    // Add the views to our main view.
    mainView.addView(gameView, gameParam);
    mainView.addView(buttonView, buttonParam);

    // Show the view to the user.
    this.setContentView(mainView, mainParam);
  }

  /**
   * When the activity is launched.
   * 
   * @param savedInstanceState
   *          Saved instances being loaded.
   * 
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Get values from the settings class.
    this.playerOneName = (this.getIntent().getStringExtra("playerOneName"));
    this.playerTwoName = (this.getIntent().getStringExtra("playerTwoName"));
    int playerOneColour = (this.getIntent().getIntExtra("playerOneColour", Color.BLACK));
    int playerTwoColour = (this.getIntent().getIntExtra("playerTwoColour", Color.BLACK));
    boolean usingBot = (this.getIntent().getBooleanExtra("usingBot", false));
    if (usingBot) {
      this.playerTwoName = "Computer";
    }
    String gridSize = (this.getIntent().getStringExtra("size"));

    // Display setup for later use.
    this.scale = this.getResources().getDisplayMetrics().density;

    // Create the view which contains the game.
    gameView = new GameView(this, this.playerOneName, this.playerTwoName, playerOneColour, playerTwoColour, gridSize, usingBot);
    // Create the layout for the main view.
    this.createInterface();

    // Get the AudioManager
    AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    // Set the volume of played media to maximum.
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

  }

  /**
   * When the application restores its state we want it to call this method. This method restores the game logic so it is not reset.
   * 
   * @param savedInstanceState
   *          The Bundle containing the instance state.
   * 
   * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
   */
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    // Set the gameView instance logic instance.
    GameActivity.gameView.setLogic(savedInstanceState.getSerializable("logic"));
    super.onRestoreInstanceState(savedInstanceState);
  }

  /**
   * When the application saves its state we want it to call this method. This method backs up the game logic so it is not reset.
   * 
   * @param outState
   *          The Bundle containing the instance state.
   * 
   * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    // Get the Logic instance from the gameView.
    outState.putSerializable("logic", GameActivity.gameView.getLogic());
    super.onSaveInstanceState(outState);
  }

}
