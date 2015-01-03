package colin.bulletzone_colin_local.cell.cell_data;

/**
 * Created by Colin on 11/14/2014.
 */
public class WallCellData extends AbstractCellData {
	boolean isDestructible;
	int life;

	/**
	 * Constructor for indestructible wall data.
	 */
	public WallCellData() {
		super();
		isDestructible = false;
		life = -1;
	}

	/**
	 * Constructor for destructible wall data.
	 *
	 * @param life
	 */
	public WallCellData(int life) {
		super();
		isDestructible = true;
		this.life = life;
	}

	/**
	 * Getter for isDestructible.
	 *
	 * @return
	 */
	public boolean isDestructible() {
		return isDestructible;
	}

	/**
	 * Setter for isDestructible.
	 *
	 * @param isDestructible
	 */
	public void setDestructible(boolean isDestructible) {
		this.isDestructible = isDestructible;
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
}
