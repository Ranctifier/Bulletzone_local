package colin.bulletzone_colin_local.cell_factory;

import colin.bulletzone_colin_local.cell.cell_graphic.AbstractCellGraphic;
import colin.bulletzone_colin_local.cell.cell_graphic.BulletCellGraphic;
import colin.bulletzone_colin_local.cell.cell_graphic.WallCellGraphic;
import colin.bulletzone_colin_local.game.GameActivity;
import colin.bulletzone_colin_local.cell.cell_data.AbstractCellData;
import colin.bulletzone_colin_local.cell.cell_graphic.GrassCellGraphic;
import colin.bulletzone_colin_local.cell.cell_graphic.TankCellGraphic;

/**
 * Created by Colin on 11/14/14.
 */
public class SimpleCellGraphicFactory {
	/**
	 * The rawData parameter determines how to instantiate the cell, then
	 * returns the cell. Useful for debugging to check if the other version of
	 * makeGraphic is working as expected.
	 *
	 * @param rawData
	 * @return
	 */
	public static AbstractCellGraphic makeGraphic(int rawData) {
		AbstractCellGraphic cell = null;
		/**
		 * The below if block generates cell graphical objects from rawData's
		 * value. If rawData's value does not fit in the specified ranges,
		 * then a null object is returned.
		 */
		if (rawData == 0) { // grass entity
			cell = new GrassCellGraphic();
		} else if (rawData == 1000) { // indestructible wall entity
			cell = new WallCellGraphic(false);
		} else if (rawData > 1000 && rawData <= 2000) { // destructible wall entity
			cell = new WallCellGraphic(true);
		} else if (rawData >= 2000000 && rawData <= 3000000) { // bullet entity
			cell = new BulletCellGraphic();
		} else if (rawData >= 10000000 && rawData <= 20000000) { // tank entity
			/**
			 * Removes the four least significant digits and the most
			 * significant digit, leaving three digits that indicate tank id.
			 */
			int tid = (rawData / 10000) % 1000;
			int life = (rawData / 10) % 1000;
			int dir = rawData % 10; // least significant digit

			if (tid == GameActivity.tankId) { // client's tank entity
				cell = new TankCellGraphic(true, life, dir);
			} else { // other tank entities
				cell = new TankCellGraphic(false, life, dir);
			}
		}
		return cell;
	}

	/**
	 * Creates and returns a AbstractCellGraphic when given a AbstractCellData.
	 * Only useful if the given data has all the required information.
	 *
	 * @param data
	 * @return
	 */
	public static AbstractCellGraphic makeGraphic(AbstractCellData data) {
		AbstractCellGraphic cell = null;

		int type = data.getType();
		int dir = data.getDirection();
		if (type == 0) { // grass entity
			cell = new GrassCellGraphic();
		} else if (type == 1) { // invincible wall entity
			cell = new WallCellGraphic(false);
		} else if (type == 2) { // destructible wall entity
			cell = new WallCellGraphic(true);
		} else if (type == 3) { // bullet entity
			cell = new BulletCellGraphic();
		} else if (type == 4) { // healthy client tank entity
			cell = new TankCellGraphic(true, 3, dir);
		} else if (type == 5) { // damaged client tank entity
			cell = new TankCellGraphic(true, 0, dir);
		} else if (type == 6) { // healthy other tank entity
			cell = new TankCellGraphic(false, 3, dir);
		} else if (type == 7) { // damaged other tank entity
			cell = new TankCellGraphic(false, 0, dir);
		}

		return cell;
	}
}
