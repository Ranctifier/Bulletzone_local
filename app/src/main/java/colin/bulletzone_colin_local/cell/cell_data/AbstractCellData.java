package colin.bulletzone_colin_local.cell.cell_data;

/**
 * Created by Colin on 11/14/14.
 */
public abstract class AbstractCellData {
	int type;
	int row;
	int column;
	int direction;

	/**
	 * Constructor for abstract data.
	 */
	public AbstractCellData() {
		row = -1;
		column = -1;
	}

	/**
	 * Getter for row.
	 *
	 * @return
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Setter for row.
	 *
	 * @param row
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * Getter for column.
	 *
	 * @return
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Setter for column.
	 *
	 * @param column
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * Getter for type.
	 *
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * Setter for type.
	 *
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Getter for direction.
	 *
	 * @return
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * Setter for direction.
	 *
	 * @param direction
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}
}
