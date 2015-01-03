package colin.bulletzone_colin_local.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.text.DateFormat;
import java.util.Date;

import colin.bulletzone_colin_local.AppContext;
import colin.bulletzone_colin_local.dispatcher.Dispatcher;
import colin.bulletzone_colin_local.update.BusProvider;
import colin.bulletzone_local.MainActivity_;
import colin.bulletzone_local.R;
import colin.bulletzone_colin_local.memento.SessionCaretaker;
import colin.bulletzone_colin_local.replay.ReplayDB;
import colin.bulletzone_colin_local.rest.BulletZoneRestClient;
import colin.bulletzone_colin_local.update.Poller;

/**
 * Created by Colin on 12/7/2014.
 */
@EActivity(R.layout.game_activity)
public class GameActivity extends Activity {
	public static long tankId = -1;
	public static byte tankDir = -1;
	public String sessionStartTime;
	public static Context gameContext;

	@ViewById
	TextView textViewTankId;

	@ViewById
	public static GridView gameGrid;

	@ViewById
	Button btnMain;

	@ViewById
	ImageView deathBG;

	@RestService
	BulletZoneRestClient restClient;

	@Bean
	public static Poller poller;

	@Bean
	Dispatcher dispatcher;

	@Bean
	public static SessionCaretaker sessionCaretaker;

	@Bean
	public static ReplayDB replayDB;

	/**
	 * Sets the text of the tank id textview, which should be the client's tank
	 * id. Used in the UI thread to update the UI directly.
	 */
	@UiThread
	void updateTankId() {
		textViewTankId.setText("TankId=" + tankId);
	}

	/**
	 * Attempts to join a bulletzone server, update the client's tank id, sets
	 * the dispatcher's context, initializes the session caretaker, starts the
	 * poller, and starts the dispatcher. Used in a background thread.
	 */
	@Background
	void join() {
		try {
			tankId = restClient.join().getResult();
			replayDB.deleteSession(); // remove session data from database
			sessionStartTime = DateFormat.getDateTimeInstance().format(new Date());
			updateTankId();
			dispatcher.setContext(this.getApplicationContext());
			showGrid();
			poller.doPoll();
			dispatcher.startDispatcher();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calls the join method. The annotation indicates that this should be
	 * called whenever the join button is pressed.
	 */
	@Click(R.id.btnJoin)
	public void joinClicked() {
		join();
	}

	/**
	 * Method that runs when Activity is created.
	 *
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sessionCaretaker = new SessionCaretaker();
		sessionCaretaker.startSession();
		gameContext = this.getApplicationContext();
	}

	/**
	 * Method that runs when Activity is resumed. Also registers the
	 * dispatcher's eventHandler to the BusProvider.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		replayDB = ReplayDB.getInstance(AppContext.getAppContext());
		BusProvider.getInstance().register(dispatcher.eventHandler);
	}

	/**
	 * Method that runs when Activity is paused. Also unregisters the
	 * dispatcher's eventHandler from the BusProvider.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		BusProvider.getInstance().unregister(dispatcher.eventHandler);
	}

	/**
	 * Sets the grid to visible, called when game begins or from a previous game
	 * over.
	 */
	@UiThread
	public void showGrid() {
		gameGrid.setVisibility(View.VISIBLE);
	}

	/**
	 * Method that sets the grid to invisible. Called when tank dies
	 */
	@UiThread
	public void hideGrid() {
		gameGrid.setVisibility(View.INVISIBLE);
	}

	/**
	 * Used to end the game and revert to the splash screen.
	 */
	@Click(R.id.btnMain)
	public void endGame() {
		Intent endIntent = new Intent(gameContext, MainActivity_.class);
		String msg = dispatcher.getEndMsg();
		endIntent.putExtra("source", "GameActivity");
		endIntent.putExtra("message", msg);
		btnMain.setVisibility(View.INVISIBLE);
		deathBG.setVisibility(View.INVISIBLE);
		startActivity(endIntent);
	}
}
