package colin.bulletzone_colin_local.memento;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import colin.bulletzone_colin_local.cell.cell_data.AbstractCellData;

/**
 * Created by Colin on 12/3/14.
 */
@EBean
public class GridMemento {
	public String gridState;

	/**
	 * Sets the memento's state from a 2D array of cell data. Since a replay
	 * would only need the type, direction, and position of a cell, we decided
	 * the best way to represent this is an encoded string. This method takes
	 * the cell data array, converts its contents into a string, and then sets
	 * the gridState to that string.
	 * <p/>
	 * The order of elements in the encoded string for a cell are: type,
	 * direction, row, column
	 * <p/>
	 * The type and direction elements are plain integers and unchanged from the
	 * specification. The row and column elements are plain integers converted
	 * into their hexadecimal equivalent.
	 * <p/>
	 * Since there are 16 rows and elements in a default grid, an encoded string
	 * for a cell is 4 characters long. An encoded string for an entire default
	 * grid is 1024 characters long.
	 *
	 * @param gridData
	 */
	@Background(serial = "mementoStorage")
	public void setGridState(AbstractCellData[][] gridData) {
		// 16 rows * 16 columns * 4 chars per cell = 1024 total chars (default)
		StringBuilder sb = new StringBuilder(1024);
		int type, direction;
		for (int i = 0; i < gridData.length; i++) { // create stringbuilder using cell data
			for (int j = 0; j < gridData[0].length; j++) {
				// get minimal data needed for replay
				type = gridData[i][j].getType();
				direction = gridData[i][j].getDirection();
				// row and column are implied from i and j

				sb.append(Integer.toHexString(type)); // append cell data to stringbuilder
				sb.append(Integer.toHexString(direction));
				sb.append(Integer.toHexString(i));
				sb.append(Integer.toHexString(j));
			}
		}
		gridState = sb.toString(); // get string from stringbuilder
	}

	/**
	 * Getter for gridState.
	 *
	 * @return
	 */
	public String getGridState() {
		return gridState;
	}
}
