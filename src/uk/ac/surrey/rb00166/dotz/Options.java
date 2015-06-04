/**
 * Settings.java
 */

package uk.ac.surrey.rb00166.dotz;

import java.util.ArrayList;

import uk.ac.surrey.rb00166.dotz.R;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author Ryan
 */
public class Options extends Rb00166_DotzActivity {

  /* UI Fields */
  private EditText          player1TextBox;        //The text box where the user enters player 1 name.
  private EditText          player2TextBox;        //The text box where the user enters player 2 name.
  private RadioGroup        rGroup;                //The radio group for size of the grid.
  private Spinner           player1Spinner;        //The spinner with the colours for player 1.
  private Spinner           player2Spinner;        //The spinner with the colours for player 2.
  private CompoundButton    botButton;             //Button to select if we are using the AIBot.

  /* Data fields */
  private ArrayList<String> colourList;            //The array list which holds the colours.
  private String            sizeString = "Small";  //Size of the grid.

  /**
   * When a button is clicked in the UI this method is called. We use this method to submit the input data to the GameActivity.
   * 
   * @param view The view pressed.
   */
  @Override
  public void onButtonClick(View view) {
    
    //Get values from the UI.
    String player1Name = this.player1TextBox.getText().toString();
    String player2Name = this.player2TextBox.getText().toString();
    String player1Selection = this.player1Spinner.getSelectedItem().toString();
    String player2Selection = this.player2Spinner.getSelectedItem().toString();
    boolean usingbot = this.botButton.isChecked();

    //If the colour for player 1 is equal to colour for player 2.
    if (player1Selection.equalsIgnoreCase(player2Selection)) {
      //Show an error message and don't do any more.
      new AlertDialog.Builder(this).setTitle(R.string.sameColourTitle).setMessage(R.string.sameColour)
          .setPositiveButton(R.string.okay, null).show();
    }
    else {
      //Some data validation on the name entered.
      if (player1Name.toString().length() == 0) player1Name = "Player 1";      
      if (player2Name.toString().length() == 0)  player2Name = "Player 2";      
      if (player1Name.length() > 15)  player1Name = player1Name.substring(0, 15);
      if (player2Name.length() > 15)  player2Name = player2Name.substring(0, 15);
      
      // Create the new intent to load the game interface.
      Intent loadGame = new Intent(this, GameActivity.class);
      // Put our values into the Intent
      loadGame.putExtra("playerOneName", player1Name);
      loadGame.putExtra("playerTwoName", player2Name);
      loadGame.putExtra("playerOneColour", Color.parseColor(player1Selection));
      loadGame.putExtra("playerTwoColour", Color.parseColor(player2Selection));
      loadGame.putExtra("size", this.sizeString);
      loadGame.putExtra("usingBot", usingbot);
      // Start the intent
      this.startActivity(loadGame);
    }
  }

  /** 
   * Called when the activity is first created.
   * 
   * @param savedInstanceState
   *
   * @see uk.ac.surrey.rb00166.dotz.Rb00166_DotzActivity#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //Set the view to use the options xml layout.
    this.setContentView(R.layout.options);
    //Set up the UI.
    this.rGroup = (RadioGroup) this.findViewById(R.id.radioGroup1);
    this.botButton = (CompoundButton) this.findViewById(R.id.usingBotCheckBox);
    //Set the listener for size of the grid radio buttons.
    this.rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(RadioGroup rGroup, int checkedId) {
        RadioButton checkedRadioButton = (RadioButton) rGroup.findViewById(checkedId);
        boolean isChecked = checkedRadioButton.isChecked();
        if (isChecked) {
          Options.this.sizeString = (String) checkedRadioButton.getText();
        }
      }
    });
    //Set up the spinners.
    this.spinnerSetup();
  }

  /**
   * Launched when the play against computer tick box is pressed.
   * 
   * @param view The view that was pressed.
   */
  public void playCPUClicked(View view) {
    //Get the UI elements.
    EditText player2TextField = (EditText) this.findViewById(R.id.playerTwoEdit);
    TextView computerText = (TextView) this.findViewById(R.id.computerText);
    
    //If the botButton is checked.
    if (this.botButton.isChecked()) {
      //Hide the player 2 text field.
      player2TextField.setVisibility(View.GONE);
      //Show the computer text.
      computerText.setVisibility(View.VISIBLE);
    }
    else {
      //Show the player 2 text field.
      player2TextField.setVisibility(View.VISIBLE);
      //Hide the computer text.
      computerText.setVisibility(View.GONE);
    }
  }

  /**
   * This method sets up the spinners and fills them with colours.
   */
  private void spinnerSetup() {
    // Colours we want to show in the spinners.
    String colours[] = { this.getString(R.string.red), this.getString(R.string.blue), this.getString(R.string.green),
        this.getString(R.string.cyan), this.getString(R.string.yellow) };
    // Link the fields with the XML files.
    this.player1TextBox = (EditText) this.findViewById(R.id.playerOneEdit);
    this.player2TextBox = (EditText) this.findViewById(R.id.playerTwoEdit);
    this.player1Spinner = (Spinner) this.findViewById(R.id.spinner1);
    this.player2Spinner = (Spinner) this.findViewById(R.id.spinner2);
    // Clear the colourList
    this.colourList = new ArrayList<String>();
    this.colourList.clear();
    // Fill the list with colours but make sure we dont get multiples.
    for (int i = 0; i < colours.length; i++) {
      if (!this.colourList.contains(colours[i])) {
        this.colourList.add(colours[i]);
      }
      // Create an adapter using the colourList.
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.colourList);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      // Set both spinners to use the colour array as its list.
      this.player1Spinner.setAdapter(adapter);
      this.player2Spinner.setAdapter(adapter);
      // Set the default selection for second spinner to blue.
      this.player2Spinner.setSelection(1);
    }
  }
}
