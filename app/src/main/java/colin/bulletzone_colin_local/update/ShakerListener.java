package colin.bulletzone_colin_local.update;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import colin.bulletzone_colin_local.dispatcher.Dispatcher;

/**
 * Created by Daniel Morin on 11/15/2014.
 * <p/>
 * Creates a Listener that checks for a shake and sends a message to dispatcher
 * when appropriate.
 */
@EBean
public class ShakerListener {

	@RootContext
	Context context;

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private TriggerEventListener mTriggerEventListener;

	/**
	 * Constructor to give the shaker listener the context of the activity.
	 *
	 * @param context
	 */
	public ShakerListener(Context context) {
		this.context = context;
	}

	/**
	 * Initializes mSensor as an acceleration sensor for significant motion and
	 * starts the relevant trigger event listener.
	 *
	 * @param dispatch
	 */
	public void startListener(Dispatcher dispatch) {
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);

		if (null != mSensor) { // avoids error on devices without accelerometer
			mTriggerEventListener = new BulletZoneTriggerEventListener(dispatch);
			mSensorManager.requestTriggerSensor(mTriggerEventListener, mSensor);
		}

	}

	/**
	 * Requests a trigger sensor using the trigger event listener and sensor.
	 */
	public void resetListener() {
		if (null != mSensor) { // avoids error on devices without accelerometer
			mSensorManager.requestTriggerSensor(mTriggerEventListener, mSensor);
		}
	}

	/**
	 * Private inner class extending TriggerEventListener that waits for trigger
	 * events and handles them.
	 */
	private class BulletZoneTriggerEventListener extends TriggerEventListener {
		private final Dispatcher dispatch;

		/**
		 * Initializes the trigger event listener.
		 *
		 * @param dispatcher
		 */
		public BulletZoneTriggerEventListener(Dispatcher dispatcher) {
			this.dispatch = dispatcher;
		}

		/**
		 * Method that runs and handles an event of significant motion.
		 *
		 * @param event
		 */
		@Override
		public void onTrigger(TriggerEvent event) {
			this.dispatch.fire();
		}
	}
}
