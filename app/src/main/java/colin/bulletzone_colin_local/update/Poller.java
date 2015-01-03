package colin.bulletzone_colin_local.update;

import android.os.SystemClock;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import colin.bulletzone_colin_local.rest.BulletZoneRestClient;
import colin.bulletzone_colin_local.wrapper.GridWrapper;

/**
 * Created by Daniel on 11/14/14.
 */
@EBean
public class Poller {
	private static final String TAG = "PollServer";
	private static boolean clientTankAlive = true;

	@RestService
	BulletZoneRestClient restClient; // used to interact with bulletzone server

	/**
	 * Used to poll the bulletzone server for the current state of the
	 * bulletzone game every 100 milliseconds. Run in a background thread to
	 * avoid slowing the UI thread and other bottlenecks.
	 */
	@Background(serial = "grid_poller_task")
	public void doPoll() {
		while (clientTankAlive) {
			onGridUpdate(restClient.grid());

			// poll server every 100ms
			SystemClock.sleep(100);
		}
	}

	/**
	 * Allows the poller to poll the bulletzone server.
	 */
	public void startPoller() {
		clientTankAlive = true;
	}

	/**
	 * Does not allow the poller to keep polling the bulletzone server.
	 */
	public void stopPoller() {
		clientTankAlive = false;
	}

	/**
	 * Uses the BusProvider class to post a message to any classes subscribed to
	 * the BusProvider. The message in this case has the contents of a
	 * GridWrapper class, a grid and a timestamp.
	 *
	 * @param gw
	 */
	@UiThread
	public void onGridUpdate(GridWrapper gw) {
		BusProvider.getInstance().post(new GridUpdateEvent(gw.getGrid(), gw.getTimeStamp()));
	}
}