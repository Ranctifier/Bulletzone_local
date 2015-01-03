package colin.bulletzone_colin_local.wrapper;

/**
 * Created by Colin on 11/14/14.
 */
public class BooleanWrapper {
	private boolean result;

	/**
	 * Empty constructor.
	 */
	public BooleanWrapper() {

	}

	/**
	 * Constructor that sets the wrapper's instance variable.
	 *
	 * @param result
	 */
	public BooleanWrapper(boolean result) {
		this.result = result;
	}

	/**
	 * Getter for the wrapper's instance variable.
	 *
	 * @return
	 */
	public boolean getResult() {
		return this.result;
	}

	/**
	 * Setter for the wrapper's instance variable.
	 *
	 * @param result
	 */
	public void setReturn(boolean result) {
		this.result = result;
	}
}
