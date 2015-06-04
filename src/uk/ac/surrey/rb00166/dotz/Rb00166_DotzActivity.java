package uk.ac.surrey.rb00166.dotz;

import uk.ac.surrey.rb00166.dotz.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * The main activity of the application.
 * It launches other activities such as the Game and credits activities.
 * 
 * @author Ryan
 */
public class Rb00166_DotzActivity extends Activity {

  public static boolean soundOn = true; //If the sound is activated.
  public static int     botLevel = 0;   //The level of the bot, easy by default.

  /**
   * When a button is clicked in the UI this method is called.
   * 
   * @param view The view pressed.
   */
  public void onButtonClick(View view) {
    //Get the id of the button pressed.
    switch (view.getId()) {
      //New Game button was pressed.
      case R.id.newGameButton:
        //Launch the Options activity.
        Intent gameOptions = new Intent(this, Options.class);
        this.startActivity(gameOptions);
        break;
      //Instructions button was pressed.
      case R.id.instructionsButton:
        //Create an alertDialog and present it to the user.
        AlertDialog instructionsDialog = new AlertDialog.Builder(this).setIcon(R.drawable.icon)
            .setMessage(R.string.instructionsText).setTitle(R.string.instructions).create();
        instructionsDialog.show();
        TextView instructionsText = (TextView) instructionsDialog.findViewById(android.R.id.message);
        instructionsText.setGravity(Gravity.CENTER);
        break;  
      //Credits button was pressed.
      case R.id.creditsButton:
        //Launch the Credits activity.
        Intent credits = new Intent(this, Credits.class);
        this.startActivity(credits);
        break;

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
    this.setContentView(R.layout.main);
    // Restore preferences
    SharedPreferences settings = this.getSharedPreferences("DotGame", 0);
    boolean soundEffects = settings.getBoolean("soundEffects", false);
    int savedBotLevel = settings.getInt("botLevel", 0);
    Log.d("savedBotLevel", "" + savedBotLevel);
    soundOn = soundEffects;
    botLevel = savedBotLevel;
  }

  /**
   * When the menu is created fill it with this menu layout.
   * 
   * @param menu The menu created.
   * @return true
   *
   * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = this.getMenuInflater();
    inflater.inflate(R.layout.menu, menu);
    return true;
  }

  /**
   * When a menuItem is selected this method is called.
   * 
   * @param item The item selected.
   * @return
   *
   * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    //Get the id of the item pressed.
    switch (item.getItemId()) {
      //About button was pressed.
      case R.id.aboutMenu:
        //Launch a dialog.
        AlertDialog aboutDialog = new AlertDialog.Builder(this).setIcon(R.drawable.icon).setMessage(R.string.aboutBoxText)
            .setTitle(R.string.aboutButtonText).create();
        aboutDialog.show();
        TextView aboutText = (TextView) aboutDialog.findViewById(android.R.id.message);
        aboutText.setGravity(Gravity.CENTER);
        break;
      //Settings button was pressed.
      case R.id.settingsMenu: 
        //Launch an alertDialog using the settingsmenu.xml layout.
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.settingsmenu, (ViewGroup) this.findViewById(R.id.settingsAlert));

        //Set up the checkBox.
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        CheckBox checkBox = (CheckBox) layout.findViewById(R.id.soundCheckbox);
        checkBox.setChecked(soundOn);
        checkBox.setOnCheckedChangeListener((new CheckBox.OnCheckedChangeListener() {

          @Override
          public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            System.out.println(arg1);
            if (arg1 == true) {
              soundOn = true;
            }
            else {
              soundOn = false;
            }
          }
        }));

        //Setup the bot level radio group.
        RadioGroup botLevelGroup = (RadioGroup) layout.findViewById(R.id.cpuRadioGroup);
        RadioButton easy = (RadioButton) layout.findViewById(R.id.easyRadio);
        RadioButton medium = (RadioButton) layout.findViewById(R.id.mediumRadio);
        RadioButton hard = (RadioButton) layout.findViewById(R.id.hardRadio);
        //Set checked depending on the saved value.
        switch (botLevel) {
          case 0:
            easy.setChecked(true);
            break;
          case 1:
            medium.setChecked(true);
            break;
          case 2:
            hard.setChecked(true);
            break;
          default:
            easy.setChecked(true);
            break;
        }
        //Set the listener.
        botLevelGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup group, int checkedId) {
            //Set the bot level depending on the selected button.
            switch (checkedId) {
              case (R.id.easyRadio):
                botLevel = 0;
                break;
              case (R.id.mediumRadio):
                botLevel = 1;
                break;
              case (R.id.hardRadio):
                botLevel = 2;
                break;

            }

            Log.d("botLevel Changed to", "" + botLevel);

          }
        });

        //Set up the alertDialog and show it.
        alertDialog.setView(layout);
        alertDialog.setTitle(R.string.gameSettings);
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
        break;

    }
    return true;
  }

  /**
   * When the activity is stopped this method is called.
   * 
   * @see android.app.Activity#onStop()
   */
  @Override
  protected void onStop() {
    super.onStop();
    // We need an Editor object to make preference changes.
    SharedPreferences settings = this.getSharedPreferences("DotGame", 0);
    SharedPreferences.Editor editor = settings.edit();
    //Put the sound effects and botlevel into the editor.
    editor.putBoolean("soundEffects", soundOn);
    editor.putInt("botLevel", botLevel);
    // Commit the edits
    editor.commit();
  }
}