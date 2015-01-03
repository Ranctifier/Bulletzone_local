package colin.bulletzone_colin_local.cell.cell_data;

/**
 * Created by Colin on 11/14/2014.
 */
public class TankCellData extends AbstractCellData {
	int tankId;
	int life;
	int direction;

	/**
	 * Constructor for tank data.
	 *
	 * @param tankId
	 * @param life
	 * @param direction
	 */
	public TankCellData(int tankId, int life, int direction) {
		super();
		this.tankId = tankId;
		this.life = life;
		this.direction = direction;
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
	 * Getter for life.
	 *
	 * @return
	 */
	public int getLife() {
		return life;
	}

	/**
	 * Setter for life.
	 *
	 * @param life
	 */
	public void setLife(int life) {
		this.life = life;
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
