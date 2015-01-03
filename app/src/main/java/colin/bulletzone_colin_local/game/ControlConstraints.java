package colin.bulletzone_colin_local.game;

/**
 * Created by Colin on 12/5/2014.
 */
public class ControlConstraints {
	private static int bulletMax;
	private static int bulletInterval;
	private static int moveInterval;
	private static int turnsPerMove;

	/**
	 * Constructor to initialize the variables used to implement constraints for
	 * the BulletZone game activity.
	 */
	public ControlConstraints() {
		bulletMax = 2;
		bulletInterval = 500; // in milliseconds
		moveInterval = 500; // in milliseconds
		turnsPerMove = 1;
	}

	/**
	 * Used in tandem with Dispatcher's fire method, this constrains whether or
	 * not the client's tank may fire a bullet.
	 *
	 * @param bullets
	 * @param sinceLastFire
	 * @return
	 */
	public static boolean canFire(int bullets, int sinceLastFire) {
		if (bullets < bulletMax && sinceLastFire > bulletInterval) {
			return true;
		}
		return false;
	}

	/**
	 * Used in tandem with Dispatcher's forward and reverse method, this
	 * constrains whether or not the client's tank may move in a forward or
	 * backward direction.
	 *
	 * @param sinceLastMove
	 * @return
	 */
	public static boolean canMove(int sinceLastMove) {
		if (sinceLastMove > moveInterval) {
			return true;
		}
		return false;
	}

	/**
	 * Used in tandem with Dispatcher's turnClockwise and turnCounterClockwise,
	 * this constrains whether or not the client's tank may turn 90 degrees left
	 * or right from its current facing.
	 *
	 * @param movesSinceTurn
	 * @return
	 */
	public static boolean canTurn(int movesSinceTurn) {
		if (movesSinceTurn >= turnsPerMove) {
			return true;
		}
		return false;
	}
}
