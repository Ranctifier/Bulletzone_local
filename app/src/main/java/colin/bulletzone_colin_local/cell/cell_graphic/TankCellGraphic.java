package colin.bulletzone_colin_local.cell.cell_graphic;

import colin.bulletzone_local.R;

/**
 * Created by Colin on 11/15/2014.
 */
public class TankCellGraphic extends AbstractCellGraphic {
	/**
	 * Constructor, determines the resource id depending on the three given
	 * parameters.
	 *
	 * @param isUser
	 * @param life
	 * @param direction
	 */
	public TankCellGraphic(boolean isUser, int life, int direction) {
		if (isUser) {
			if (life == 3) {
				type = 4;
				switch (direction) {
					case 0:
						resId = R.drawable.user_tank0;
						break;
					case 2:
						resId = R.drawable.user_tank1;
						break;
					case 4:
						resId = R.drawable.user_tank2;
						break;
					case 6:
						resId = R.drawable.user_tank3;
				}
			} else {
				type = 5;
				switch (direction) {
					case 0:
						resId = R.drawable.user_tank_damaged0;
						break;
					case 2:
						resId = R.drawable.user_tank_damaged1;
						break;
					case 4:
						resId = R.drawable.user_tank_damaged2;
						break;
					case 6:
						resId = R.drawable.user_tank_damaged3;
						break;
				}
			}
		} else {
			if (life == 3) {
				type = 6;
				switch (direction) {
					case 0:
						resId = R.drawable.other_tank0;
						break;
					case 2:
						resId = R.drawable.other_tank1;
						break;
					case 4:
						resId = R.drawable.other_tank2;
						break;
					case 6:
						resId = R.drawable.other_tank3;
						break;
				}
			} else {
				type = 7;
				switch (direction) {
					case 0:
						resId = R.drawable.other_tank_damaged0;
						break;
					case 2:
						resId = R.drawable.other_tank_damaged1;
						break;
					case 4:
						resId = R.drawable.other_tank_damaged2;
						break;
					case 6:
						resId = R.drawable.other_tank_damaged3;
				}
			}
		}
		this.direction = direction;
	}
}
