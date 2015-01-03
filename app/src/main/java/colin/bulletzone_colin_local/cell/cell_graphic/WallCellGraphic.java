package colin.bulletzone_colin_local.cell.cell_graphic;

import colin.bulletzone_local.R;

/**
 * Created by Colin on 11/15/2014.
 */
public class WallCellGraphic extends AbstractCellGraphic {
	public WallCellGraphic(boolean isDestructible) {
		if (isDestructible) {
			type = 2;
			resId = R.drawable.des_wall;
		} else {
			type = 1;
			resId = R.drawable.invin_wall;
		}
		direction = 0;
	}
}
