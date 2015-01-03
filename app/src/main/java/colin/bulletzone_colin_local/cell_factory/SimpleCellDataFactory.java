package colin.bulletzone_colin_local.cell_factory;

import colin.bulletzone_colin_local.cell.cell_data.GrassCellData;
import colin.bulletzone_colin_local.cell.cell_data.TankCellData;
import colin.bulletzone_colin_local.game.GameActivity;
import colin.bulletzone_colin_local.cell.cell_data.AbstractCellData;
import colin.bulletzone_colin_local.cell.cell_data.BulletCellData;
import colin.bulletzone_colin_local.cell.cell_data.WallCellData;

/**
 * Created by Colin on 11/14/14.
 */
public class SimpleCellDataFactory {

	/**
	 * Creates and returns an AbstractCellData object given some rawData in the
	 * form of an integer. It is assumed that the class using this factory knows
	 * the row and column the AbstractCellData is for.
	 *
	 * @param rawData
	 * @return
	 */
	public static AbstractCellData makeData(int rawData) {
		AbstractCellData cell = null;

		/**
		 * The below if block generates cell data objects based on rawData's
		 * value. If rawData's value does not fit in the specified ranges,
		 * then a null object is returned.
		 */
		if (rawData == 0) { // grass entity
			cell = new GrassCellData();
			cell.setType(0);
			cell.setDirection(0);
		} else if (rawData == 1000) { // indestructible wall entity
			cell = new WallCellData();
			cell.setType(1);
			cell.setDirection(0);
		} else if (rawData > 1000 && rawData <= 2000) { // destructible wall entity
			int life = rawData - 1000; // life of destructible wall
			cell = new WallCellData(life);
			cell.setType(2);
			cell.setDirection(0);
		} else if (rawData >= 2000000 && rawData <= 3000000) { // bullet entity
			/**
			 * Removes the three least significant digits and the most
			 * significant digit, leaving three digits that indicate tank id.
			 */
			int tid = (rawData / 1000) % 1000;
			int dmg = rawData % 1000; // three least significant digits
			cell = new BulletCellData(tid, dmg);
			cell.setType(3);
			cell.setDirection(0);
		} else if (rawData >= 10000000 && rawData <= 20000000) { // tank entity
			/**
			 * Removes the four least significant digits and the most
			 * significant digit, leaving three digits that indicate tank id.
			 */
			int tid = (rawData / 10000) % 1000;
			/**
			 * Removes the least significant digit and four most significant
			 * digits, leaving three digits that indicate life.
			 */
			int life = (rawData / 10) % 1000;
			int dir = rawData % 10; // least significant digit
			cell = new TankCellData(tid, life, dir);

			if (tid == GameActivity.tankId) { // client's tank
				GameActivity.tankDir = (byte) dir;
				if (life == 3) {
					cell.setType(4);
				} else {
					cell.setType(5);
				}
			} else {
				if (life == 3) {
					cell.setType(6);
				} else {
					cell.setType(7);
				}
			}

			cell.setDirection(dir);
		}
		return cell;
	}

	/**
	 * Creates and returns an AbstractCellData object when its instance
	 * variables are given. Assumes that no special data, such as life and
	 * tankId are needed, so the cell is instantiated as GrassCellData. Useful
	 * for storing data that needs to be used later, like for a replay.
	 *
	 * @param type
	 * @param row
	 * @param column
	 * @param direction
	 * @return
	 */
	public static AbstractCellData makeData(int type, int row, int column,
	                                        int direction) {
		AbstractCellData cell = new GrassCellData();
		cell.setType(type);
		cell.setRow(row);
		cell.setColumn(column);
		cell.setDirection(direction);
		return cell;
	}
}
