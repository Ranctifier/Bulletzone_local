package colin.bulletzone_colin_local;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import colin.bulletzone_local.R;
import colin.bulletzone_local.game.GameActivity_;
import colin.bulletzone_local.replay.ReplayActivity_;

/**
 * Created by Colin and Daniel on 11/14/14.
 */

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {
	String topMessage;

	@ViewById
	TextView gameMessage;

	AppContext appContext;

	/**
	 * Starts the game activity when the new game button is clicked.
	 */
	@Click(R.id.btnNew)
	public void newClicked() {
		Intent gameIntent = new Intent(this, GameActivity_.class);
		gameIntent.putExtra("source", "MainActivity");
		startActivity(gameIntent);
	}

	/**
	 * Starts the replay activity when the replay button is clicked.
	 */
	@Click(R.id.btnReplay)
	public void replayClicked() {
		Intent replayIntent = new Intent(this, ReplayActivity_.class);
		replayIntent.putExtra("source", "MainActivity");
		startActivity(replayIntent);
	}

	/**
	 * Method that runs when MainActivity is created.
	 *
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String source = intent.getStringExtra("source");
		topMessage = null;
		if (null != source) { // intent extra exists
			String message = intent.getStringExtra("message");
			if (source.equals("GameActivity")) { // the source is GameActivity
				if (null != message) {
					if (!message.equals("done")) { // likely a http sent after client tank died
						topMessage = message;
					}
				}
			} else if (source.equals("ReplayActivity")) { // the source is ReplayActivity
				if (null != message) {
					if (!message.equals("done")) { // likely no replay was stored in DB
						topMessage = message;
					}
				}
			}
		}
	}

	/**
	 * Method that runs when MainActivity is resumed.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (appContext == null) {
			appContext = new AppContext(this.getApplicationContext());
		}

		if (null != topMessage) { // if message isn't null, use the textview
			gameMessage.setText(topMessage);
		} else { // otherwise, make the textview empty
			gameMessage.setText("");
		}
	}

	/**
	 * Method that runs when MainActivity is paused.
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}
}