/**
 * Credits.java
 */

package uk.ac.surrey.rb00166.dotz;

import uk.ac.surrey.rb00166.dotz.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * The Credits activity launches the Credit layout showing extra information about the development of the game.
 * 
 * @author Ryan Burke
 */
public class Credits extends Activity {

  /**
   * Set the content of R.layout.credit as the view.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.credit);
  }
}
