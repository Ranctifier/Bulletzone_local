package colin.bulletzone_colin_local.update;

import com.google.common.eventbus.EventBus;

/**
 * Created by Colin on 11/14/14.
 */

public class BusProvider {
	final static EventBus bus = new EventBus();

	/**
	 * Method used by some external class that returns an instance of the class.
	 * Used as part of a singleton pattern.
	 *
	 * @return
	 */
	public static EventBus getInstance() {
		return bus;
	}

	/**
	 * Empty private constructor for singleton pattern.
	 */
	private BusProvider() {
	}
}