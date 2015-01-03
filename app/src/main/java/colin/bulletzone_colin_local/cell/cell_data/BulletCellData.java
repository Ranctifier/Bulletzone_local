package colin.bulletzone_colin_local.cell.cell_data;

/**
 * Created by Colin on 11/14/2014.
 */
public class BulletCellData extends AbstractCellData {
	int tankId;
	int damage;

	/**
	 * Constructor for bullet data.
	 *
	 * @param tankId
	 * @param damage
	 */
	public BulletCellData(int tankId, int damage) {
		super();
		this.tankId = tankId;
		this.damage = damage;
	}

	/**
	 * Getter for tank id.
	 *
	 * @return
	 */
	public int getTankId() {
		return tankId;
	}

	/**
	 * Setter for tank id.
	 *
	 * @param tankId
	 */
	public void setTankId(int tankId) {
		this.tankId = tankId;
	}

	/**
	 * Getter for damage.
	 *
	 * @return
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Setter for damage.
	 *
	 * @param damage
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}
}
