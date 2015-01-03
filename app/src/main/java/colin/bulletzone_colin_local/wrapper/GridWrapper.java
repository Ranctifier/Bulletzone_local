package colin.bulletzone_colin_local.wrapper;

/**
 * Created by Colin on 11/14/14.
 */
public class GridWrapper {
	private int[][] grid;
	private long timeStamp;

	/**
	 * Empty constructor.
	 */
	public GridWrapper() {
	}

	/**
	 * Constructor that sets the grid.
	 *
	 * @param grid
	 */
	public GridWrapper(int[][] grid) {
		this.grid = grid;
	}

	/**
	 * Getter for the grid.
	 *
	 * @return
	 */
	public int[][] getGrid() {
		return this.grid;
	}

	/**
	 * Setter for the grid.
	 *
	 * @param grid
	 */
	public void setGrid(int[][] grid) {
		this.grid = grid;
	}

	/**
	 * Getter for the timestamp.
	 *
	 * @return
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Setter for the timestamp.
	 *
	 * @param timeStamp
	 */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
}
