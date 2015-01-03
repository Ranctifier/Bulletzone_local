package colin.bulletzone_colin_local.wrapper;

/**
 * Created by Colin on 11/14/14.
 */
public class LongWrapper {
	private long result;

	/**
	 * Empty constructor.
	 */
	public LongWrapper() {

	}

	/**
	 * Constructor that sets the wrapper's instance variable.
	 *
	 * @param result
	 */
	public LongWrapper(long result) {
		this.result = result;
	}

	/**
	 * Getter for the wrapper's instance variable.
	 *
	 * @return
	 */
	public long getResult() {
		return this.result;
	}

	/**
	 * Setter for the wrapper's instance variable.
	 *
	 * @param result
	 */
	public void setResult(long result) {
		this.result = result;
	}
}
