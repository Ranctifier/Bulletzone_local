package colin.bulletzone_colin_local.update;

/**
 * Created by Colin on 11/14/14.
 */
public class GridUpdateEvent {
	int[][] grid;
	long timestamp;

	/**
	 * Constructor for a grid update event. This class has the same instance
	 * variables as a GridWrapper object, but this class is used specifically in
	 * conjunction with a BusProvider to facilitate a polling mechanism.
	 *
	 * @param grid
	 * @param timestamp
	 */
	public GridUpdateEvent(int[][] grid, long timestamp) {
		this.grid = grid;
		this.timestamp = timestamp;
	}

	/**
	 * Getter for the grid.
	 *
	 * @return
	 */
	public int[][] getGrid() {
		return grid;
	}

	/**
	 * Getter for the timestamp.
	 *
	 * @return
	 */
	public long getTimestamp() {
		return timestamp;
	}
}