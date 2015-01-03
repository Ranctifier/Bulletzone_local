package colin.bulletzone_colin_local.dispatcher;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.common.eventbus.Subscribe;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpClientErrorException;

import colin.bulletzone_colin_local.AppContext;
import colin.bulletzone_colin_local.adapter.BulletZoneAdapter;
import colin.bulletzone_colin_local.cell.cell_data.AbstractCellData;
import colin.bulletzone_colin_local.cell.cell_graphic.AbstractCellGraphic;
import colin.bulletzone_colin_local.cell_factory.SimpleCellDataFactory;
import colin.bulletzone_colin_local.cell_factory.SimpleCellGraphicFactory;
import colin.bulletzone_colin_local.game.ControlConstraints;
import colin.bulletzone_colin_local.game.GameActivity;
import colin.bulletzone_colin_local.update.GridUpdateEvent;
import colin.bulletzone_colin_local.update.ShakerListener;
import colin.bulletzone_local.R;
import colin.bulletzone_colin_local.rest.BulletZoneRestClient;

/**
 * Created by Colin on 11/15/2014.
 */
@EBean
public class Dispatcher {
	boolean clientTankAlive;
	boolean firstUpdate;
	boolean firstInsert;
	int updateCount;
	BulletZoneAdapter adapter;
	ShakerListener shaker;

	String endMsg;

	@RootContext
	Context context;

	@ViewById
	GridView gameGrid;

	@ViewById
	Button btnMain;

	@ViewById
	ImageView deathBG;

	@ViewById
	Button btnJoin;

	@ViewById
	ImageButton btnFire;

	@ViewById
	ImageButton btnForward;

	@ViewById
	ImageButton btnReverse;

	@ViewById
	ImageButton btnTurnCW;

	@ViewById
	ImageButton btnTurnCCW;

	@RestService
	BulletZoneRestClient restClient;

	ControlConstraints constraints;
	int bulletsOnGrid = 0; // variables used to aid in constraining controls
	int lastFireFrame = 0;
	int lastMoveFrame = 0;
	int movesSinceTurn = 0;

	/**
	 * Starts the dispatcher.
	 */
	public void startDispatcher() {
		clientTankAlive = true; // assume client tank initially exists
		firstUpdate = true; // assume first poll for session hasn't happened
		firstInsert = false;
		constraints = new ControlConstraints();
		makeShaker();
		makeAdapter();
	}

	/**
	 * Makes the BulletZoneAdapter.
	 */
	public void makeAdapter() {
		adapter = new BulletZoneAdapter(context, GameActivity.gameGrid.getId());
	}

	/**
	 * Sets the context for Dispatcher and graphicFactory.
	 *
	 * @param context
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * Initializes the ShakerListener class
	 */
	@Background
	public void makeShaker() {
		shaker = new ShakerListener(context);
		if (null != shaker) { // avoids error on devices without an accelerometer
			shaker.startListener(this);
		}
	}

	/**
	 * Sends the rest command for fire to the server using the fire button.
	 */
	@Click(R.id.btnFire)
	public void fireClicked() {
		fire();
	}

	/**
	 * Sends the rest command for fire to the server. Fire is called by the
	 * ShakerListener when receiving a shake.
	 */
	@Background(serial = "firing")
	public void fire() {
		int sinceLastFire = (updateCount - lastFireFrame) * 100;
		if (clientTankAlive && ControlConstraints.canFire(bulletsOnGrid,
				sinceLastFire)) {
			try {
				restClient.fire(GameActivity.tankId);
				if (null != shaker) { // avoids error on devices without an accelerometer
					shaker.resetListener();
				}
				lastFireFrame = updateCount + 1;
			} catch (HttpClientErrorException rce) {
				if (rce.getStatusCode().value() != 404) { // error we don't know how to handle
					throw rce;
				} else { // likely sent an http while tank dead
					endError();
				}
			}
		}
	}

	@Click(R.id.btnTurnCW)
	public void turnCWClicked() {
		turnClockwise();
	}

	/**
	 * Sends the turn clockwise command to the server. turnClockwise is called
	 * by the activity_main.xml when the turnClockwise button is pressed.
	 */
	@Background(serial = "turning")
	public void turnClockwise() {
		if (clientTankAlive && ControlConstraints.canTurn(movesSinceTurn)) {
			byte dir = GameActivity.tankDir;
			if (dir < 6) {
				dir += 2;
			} else {
				dir = 0;
			}
			try {
				restClient.turn(GameActivity.tankId, dir);
				movesSinceTurn = 0;
			} catch (HttpClientErrorException rce) {
				if (rce.getStatusCode().value() != 404) { // error we don't know how to handle
					throw rce;
				} else { // likely sent an http while tank dead
					endError();
				}
			}
		}
	}

	@Click(R.id.btnTurnCCW)
	public void turnCCWClicked() {
		turnCounterClockwise();
	}

	/**
	 * Sends the turnCounterClockwise command to the server. Called when the
	 * turnCounterClockwise button is pressed by the user.
	 */
	@Background(serial = "turning")
	public void turnCounterClockwise() {
		if (clientTankAlive && ControlConstraints.canTurn(movesSinceTurn)) {
			byte dir = GameActivity.tankDir;
			if (dir > 0) {
				dir -= 2;
			} else {
				dir = 6;
			}
			try {
				restClient.turn(GameActivity.tankId, dir);
				movesSinceTurn = 0;
			} catch (HttpClientErrorException rce) {
				if (rce.getStatusCode().value() != 404) { // error we don't know how to handle
					throw rce;
				} else { // likely sent an http while tank dead
					endError();
				}
			}
		}
	}

	@Click(R.id.btnForward)
	public void forwardClicked() {
		forward();
	}

	/**
	 * Sends the forward command to the server. Called when the forward button
	 * is pressed by the user.
	 */
	@Background(serial = "movement")
	public void forward() {
		int sinceLastMove = (updateCount - lastMoveFrame) * 100;
		if (clientTankAlive && ControlConstraints.canMove(sinceLastMove)) {
			try {
				lastMoveFrame = updateCount + 1;
				restClient.move(GameActivity.tankId, GameActivity.tankDir);
				movesSinceTurn++;
			} catch (HttpClientErrorException rce) {
				if (rce.getStatusCode().value() != 404) { // error we don't know how to handle
					throw rce;
				} else { // likely sent an http while tank dead
					endError();
				}
			}
		}
	}

	@Click(R.id.btnReverse)
	public void reverseClicked() {
		reverse();
	}

	/**
	 * Sends the reverse command to the server. Called when reverse button is
	 * pressed by the user.
	 */
	@Background(serial = "movement")
	public void reverse() {
		int sinceLastMove = (updateCount - lastMoveFrame) * 100;
		if (clientTankAlive && ControlConstraints.canMove(sinceLastMove)) {
			byte dir = GameActivity.tankDir;
			if (dir == 0 || dir == 2) {
				dir += 4;
			} else if (dir == 4 || dir == 6) {
				dir -= 4;
			}
			try {
				lastMoveFrame = updateCount + 1;
				restClient.move(GameActivity.tankId, dir);
				movesSinceTurn++;
			} catch (HttpClientErrorException rce) {
				if (rce.getStatusCode().value() != 404) { // error we don't know how to handle
					throw rce;
				} else { // likely sent an http while tank dead
					endError();
				}
			}
		}
	}

	/**
	 * Inner object whose annotation subscribes the class to the BusProvider.
	 * Whenever this class receives the appropriate event from the BusProvider,
	 * updateGrid is called with that event.
	 */
	public Object eventHandler = new Object() {
		@Subscribe
		public void onUpdateGrid(GridUpdateEvent event) {
			generateCells(event);
		}
	};

	/**
	 * Sets the grid's adapter and notifies that the adapter's data changed.
	 */
	@UiThread
	public void updateGrid() {
		gameGrid.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	/**
	 * Creates the data and graphical cells for the current grid on a background
	 * thread. After the cells are created, updateGrid is called, which updates
	 * the UI to reflect the new set of cells.
	 *
	 * @param event
	 */
	@Background
	public void generateCells(GridUpdateEvent event) {
		int[][] grid = event.getGrid();
		boolean tmp = false;
		int bulletsNext = 0;
		int thisCount = updateCount();

		if (firstUpdate) { // first poll encountered
			firstUpdate = false;
			adapter.setDimensions(grid.length, grid[0].length, true);
			AppContext.setGridRows(grid.length);
			AppContext.setGridColumns(grid[0].length);
		}


		int cellType = -1;
		AbstractCellData[][] data =
				new AbstractCellData[grid.length][grid[0].length];
		AbstractCellGraphic[][] graphics =
				new AbstractCellGraphic[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				// create cell data
				data[i][j] = SimpleCellDataFactory.makeData(grid[i][j]);
				data[i][j].setRow(i);
				data[i][j].setColumn(j);

				cellType = data[i][j].getType();

				if (cellType == 3) { // a bullet exists on grid
					bulletsNext++; // increment amount of bullets on grid
				}
				if (cellType == 4 || cellType == 5) { // client tank exists at this cell
					tmp = true;
				}

				// create cell graphics
				graphics[i][j] = SimpleCellGraphicFactory.makeGraphic(data[i][j]);
				// add graphics to adapter
				adapter.putItem(i * grid.length + j, graphics[i][j]);
				cellType = -1; // reset variable
			}
		}
		clientTankAlive = tmp;
		updateGrid();
		updateCaretaker(data, thisCount);
		bulletsOnGrid = bulletsNext;

		if (!clientTankAlive) { // client tank died
			endNormal();
		}
	}

	public int updateCount() {
		int tmp = updateCount++;
		return tmp;
	}

	@Background(serial = "caretaker")
	public void updateCaretaker(AbstractCellData[][] data, int startPoll) {
		GameActivity.sessionCaretaker.makeMemento(data, startPoll);
	}

	/**
	 * Hides the grid.
	 */
	@UiThread
	public void hideGrid() {
		gameGrid.setVisibility(View.INVISIBLE);
	}

	/**
	 * Hides the join, fire, movement, and turning buttons.
	 */
	@UiThread
	public void hideMostButtons() {
		btnJoin.setVisibility(View.INVISIBLE);
		btnFire.setVisibility(View.INVISIBLE);
		btnForward.setVisibility(View.INVISIBLE);
		btnReverse.setVisibility(View.INVISIBLE);
		btnTurnCW.setVisibility(View.INVISIBLE);
		btnTurnCCW.setVisibility(View.INVISIBLE);
	}

	/**
	 * Used to stop the sessionCaretaker from collecting data and to add that
	 * data to the replay database.
	 */
	@Background(delay = 1000, serial = "end_session")
	public void endSession() {
		GameActivity.poller.stopPoller();
		hideGrid();
		hideMostButtons();
		tryInsert();
		showMain(); // show the main menu button
	}

	@Background(serial = "end_session")
	public void tryInsert() {
		if (!firstInsert) {
			firstInsert = true;
			GameActivity.replayDB.batchInsert(
					GameActivity.sessionCaretaker.getSessionSlices());
		}
	}

	@Background(serial = "waiter")
	public void waiter() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Hides the back to main menu button and the death background.
	 */
	@UiThread
	public void hideMain() {
		btnMain.setVisibility(View.INVISIBLE);
		deathBG.setVisibility(View.INVISIBLE);
	}

	/**
	 * Shows the back to main menu button and the death background.
	 */
	@UiThread(delay = 1000)
	public void showMain() {
		btnMain.setVisibility(View.VISIBLE);
		deathBG.setVisibility(View.VISIBLE);
	}

	/**
	 * Used to end the game when an http 404 error occurs during one of the rest
	 * commands. Note: Errors from join and leave not handled yet.
	 */
	public void endError() {
		endSession();
		endMsg = "Error: Tank not found, replay still saved";
	}

	/**
	 * Used to end the game from normal execution.
	 */
	public void endNormal() {
		endSession();
		endMsg = "done";
	}

	/**
	 * Allows GameActivity to obtain the end message.
	 *
	 * @return
	 */
	public String getEndMsg() {
		return endMsg;
	}
}