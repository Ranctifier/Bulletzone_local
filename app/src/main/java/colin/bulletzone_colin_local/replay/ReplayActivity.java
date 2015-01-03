package colin.bulletzone_colin_local.replay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
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

import java.util.Arrays;

import colin.bulletzone_colin_local.AppContext;
import colin.bulletzone_colin_local.cell.cell_graphic.AbstractCellGraphic;
import colin.bulletzone_colin_local.cell_factory.SimpleCellDataFactory;
import colin.bulletzone_colin_local.cell_factory.SimpleCellGraphicFactory;
import colin.bulletzone_local.MainActivity_;
import colin.bulletzone_local.R;
import colin.bulletzone_colin_local.adapter.BulletZoneAdapter;
import colin.bulletzone_colin_local.cell.cell_data.AbstractCellData;

/**
 * Created by Colin Smith on 12/5/2014.
 */

@EActivity(R.layout.replay_activity)
public class ReplayActivity extends Activity {
	public static boolean runReplay;
	int replaySpeed;
	int timeStep;
	public static Context replayContext;
	public static BulletZoneAdapter adapter;
	String endMsg;
	ReplayTriple[] replayArray;

	@ViewById
	public static GridView gameGrid;

	@ViewById
	public Button btnMain;

	@ViewById
	public ImageView splash_replay;

	@ViewById
	public Button btnSlow;

	@ViewById
	public Button btnNormal;

	@ViewById
	public Button btnFast;

	@ViewById
	public Button btnSuperFast;

	@ViewById
	public TextView textViewReplaySpeed;

	@Bean
	public static ReplayDB replayDB;

	/**
	 * Method that runs the replay and hides the speed buttons.
	 */
	public void startReplay() {
		hideButtons();
		hideMain();
		runReplay(replayArray);
	}

	/**
	 * Method activated by the slow button.
	 */
	@Click(R.id.btnSlow)
	public void slowSpeed() {
		slow();
	}

	/**
	 * Changes the replay speed and the textview for slow speed.
	 */
	@UiThread
	public void slow() {
		replaySpeed = 0;
		textViewReplaySpeed.setText("Replay Speed: Half Speed");
		startReplay();
	}

	/**
	 * Method activated by the normal button.
	 */
	@Click(R.id.btnNormal)
	public void normalSpeed() {
		normal();
	}

	/**
	 * Changes the replay speed and the textview for normal speed.
	 */
	@UiThread
	public void normal() {
		replaySpeed = 1;
		textViewReplaySpeed.setText("Replay Speed: Normal Speed");
		startReplay();
	}

	/**
	 * Method activated by the fast button.
	 */
	@Click(R.id.btnFast)
	public void fastSpeed() {
		fast();
	}

	/**
	 * Changes the replay speed and the textview for fast speed.
	 */
	@UiThread
	public void fast() {
		replaySpeed = 2;
		textViewReplaySpeed.setText("Replay Speed: Double Speed");
		startReplay();
	}

	/**
	 * Method activated by the super fast button.
	 */
	@Click(R.id.btnSuperFast)
	public void superSpeed() {
		superFast();
	}

	/**
	 * Changes the replay speed and the textview for super fast speed.
	 */
	@UiThread
	public void superFast() {
		replaySpeed = 3;
		textViewReplaySpeed.setText("Replay Speed: Quadruple Speed");
		startReplay();
	}

	/**
	 * Hides the speed buttons.
	 */
	@UiThread
	void hideButtons() {
		btnSlow.setVisibility(View.INVISIBLE);
		btnFast.setVisibility(View.INVISIBLE);
		btnNormal.setVisibility(View.INVISIBLE);
		btnSuperFast.setVisibility(View.INVISIBLE);
	}

	/**
	 * Shows the speed buttons.
	 */
	@UiThread
	void showButtons() {
		btnSlow.setVisibility(View.VISIBLE);
		btnFast.setVisibility(View.VISIBLE);
		btnNormal.setVisibility(View.VISIBLE);
		btnSuperFast.setVisibility(View.VISIBLE);
	}

	/**
	 * Method called when ReplayActivity is created.
	 *
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		replayContext = AppContext.getAppContext();
	}

	/**
	 * Method that runs after onCreate.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		replayDB = ReplayDB.getInstance(replayContext);
		splash_replay.setVisibility(View.VISIBLE);
		initReplay();
	}

	/**
	 * Performs initial tasks to show the replay.
	 */
	@Background
	public void initReplay() {
		hideButtons();
		Cursor data = loadReplay(); // attempts to load replay
		if (null != data) { // decode the replay when data exists
			decodeCursor(data);
			replayDB.close();
			showButtons(); // show the speed buttons
			showMain();
		} else { // show the back to main menu button and tell user data isn't there
			endMsg = "No replay stored.";
			showMain();
		}
	}

	/**
	 * Method that runs before Activity closes.
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * Queries the replayDB to populate sessionData with information. Returns
	 * true if the query was successful and data arrived, otherwise returns
	 * false.
	 *
	 * @return
	 */
	public Cursor loadReplay() {
		return replayDB.getSession();
	}

	/**
	 * Updates the timeStep variables based on the replaySpeed.
	 */
	@Background
	public void updateTimeStep() {
		if (replaySpeed == 0) { // slow
			timeStep = 200;
		} else if (replaySpeed == 1) { // normal
			timeStep = 100;
		} else if (replaySpeed == 2) { // fast
			timeStep = 50;
		} else if (replaySpeed == 3) { // super fast
			timeStep = 25;
		}
	}

	/**
	 * Obtains and decodes the data in the sessionData cursor. Data parsed into
	 * a start time, end time, and a string for gridState. gridState is parsed
	 * to create a 2D array of AbstractGridData. That 2D array and the times are
	 * used to add to an array of replayTriples.
	 * <p/>
	 * This means each cursor row is converted into a replayTriple. After the
	 * entire cursor is converted into replayTriples, the cursor is closed and
	 * the array of replayTriples is sorted by start time.
	 *
	 * @param sessionData
	 */
	public void decodeCursor(Cursor sessionData) {
		sessionData.moveToFirst();
		int start, end;
		int rows = AppContext.getGridRows();
		int cols = AppContext.getGridColumns();
		replayArray = new ReplayTriple[sessionData.getCount()];
		String gridState = null;
		String segment = null;
		AbstractCellData[][] grid = null;
		AbstractCellData cell = null;
		for (int i = 0; i < sessionData.getCount(); i++) {
			// iterate the tuples of the sessionData
			start = sessionData.getInt(0);
			end = sessionData.getInt(1);
			gridState = sessionData.getString(2);
			grid = new AbstractCellData[rows][cols];
			int offset = 0; // used to access segments of gridState
			for (int j = 0; j < rows; j++) { // iterate grid
				int type;
				int dir;
				int r, c;
				for (int k = 0; k < cols; k++) {
					// obtain segment for each cell in gridState
					segment = gridState.substring(offset, offset + 4);
					// parse integers from segment
					type = Character.digit(segment.charAt(0), 16);
					dir = Character.digit(segment.charAt(1), 16);
					r = Character.digit(segment.charAt(2), 16);
					c = Character.digit(segment.charAt(3), 16);
					// create the cell graphic
					cell = SimpleCellDataFactory.makeData(type, r, c, dir);
					grid[r][c] = cell;
					offset += 4; // increment offset
					type = dir = r = c = -1; // reset variables
					cell = null;
					segment = null;
				}
			}
			replayArray[i] = new ReplayTriple(start, end, grid); // add triple
			sessionData.moveToNext(); // move to next row of cursor
			start = end = -1; // reset variables
			gridState = null;
			grid = null;
		}
		sessionData.close(); // shouldn't need cursor any more
		Arrays.sort(replayArray); // sort the replay array by start time
	}

	/**
	 * First, a BulletZoneAdapter is created and initialized. Then the timestep
	 * for the replay is determined. The array of replay triples is iterated to
	 * populate the adapter with cell graphics. On the first iteration after the
	 * adapter is populated, the grid ui is shown. Each iteration updates the
	 * grid ui and then the next iteration is delayed by a call to
	 * SystemClock.sleep using the timeStep value.
	 * <p/>
	 * After the replay array has been fully iterated, the replay is finished,
	 * the grid is hidden, and the back to main menu button appears.
	 *
	 * @param replayArray
	 */
	@Background
	public void runReplay(ReplayTriple[] replayArray) {
		int rows = AppContext.getGridRows();
		int cols = AppContext.getGridColumns();
		// setup adapter and the timestep
		adapter = new BulletZoneAdapter(replayContext, gameGrid.getId());
		adapter.setDimensions(rows, cols, false);
		updateTimeStep();
		runReplay = true;
		AbstractCellData[][] data;
		AbstractCellGraphic cell;

		for (int i = 0; i < replayArray.length; i++) { // iterate replay steps
			data = replayArray[i].grid;
			for (int j = 0; j < rows; j++) { // populate adapter with the triple's grid
				for (int k = 0; k < cols; k++) {
					cell = SimpleCellGraphicFactory.makeGraphic(data[j][k]);
					adapter.putItem(j * rows + k, cell);
				}
			}

			if (i == 0) { // show the grid and then start updating it
				showGrid();
			}
			updateGrid();
			SystemClock.sleep(timeStep);
		}
		endMsg = "done";
		hideGrid();
		showButtons();
		showMain();
	}

	/**
	 * Set's the adapter for the grid and tells the adapter to notify the grid
	 * that the contents of the adapter have changed.
	 */
	@UiThread
	public void updateGrid() {
		gameGrid.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	/**
	 * Shows the grid.
	 */
	@UiThread
	public void showGrid() {
		gameGrid.setVisibility(View.VISIBLE);
	}

	/**
	 * Hides the grid.
	 */
	@UiThread
	public void hideGrid() {
		gameGrid.setVisibility(View.INVISIBLE);
	}

	/**
	 * Shows the back to main menu button and the splash screen.
	 */
	@UiThread
	public void showMain() {
		btnMain.setVisibility(View.VISIBLE);
		btnMain.setClickable(true);
		splash_replay.setVisibility(View.VISIBLE);
	}

	/**
	 * Hides the back to main menu button and the splash screen.
	 */
	@UiThread
	public void hideMain() {
		btnMain.setVisibility(View.INVISIBLE);
		splash_replay.setVisibility(View.INVISIBLE);
	}

	/**
	 * Used to end the replay and revert back to the splash screen.
	 */
	@Click(R.id.btnMain)
	public void endReplay() {
		Intent intent = new Intent(this, MainActivity_.class);
		intent.putExtra("source", "ReplayActivity");
		intent.putExtra("message", endMsg);
		btnMain.setVisibility(View.INVISIBLE);
		startActivity(intent);
	}
}