package colin.bulletzone_colin_local.memento;

import android.util.Pair;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import colin.bulletzone_colin_local.cell.cell_data.AbstractCellData;

/**
 * Created by Colin on 12/3/14.
 */
@EBean
public class SessionCaretaker {
	private BlockingQueue<Pair<String, Integer>> queue;
	//    private static Hashtable<String, HashSet<Pair<Integer, Integer>>> sessionSlices;
	private static HashSet<MementoTriple> sessionSlices;
	private static boolean activeSession = false;

	/**
	 * Constructor to initialize updateCount and calls createStorage. In order
	 * to create a new storage object, there must be no currently active
	 * session. By default a session is inactive until startSession is called.
	 */
	public SessionCaretaker() {
		createStorage();
	}

	/**
	 * Creates a hash table used to store mementos, but only when there is no
	 * currently active session.
	 */
	private void createStorage() {
		if (!activeSession) {
			queue = new LinkedBlockingQueue<Pair<String, Integer>>();
//            sessionSlices = new Hashtable<String, HashSet<Pair<Integer, Integer>>>();
			sessionSlices = new HashSet<MementoTriple>();
		}
	}

	/**
	 * Indicates the current session has ended.
	 */
	public void endSession() {
		activeSession = false;
	}

	/**
	 * Indicates a new session has started.
	 */
	public void startSession() {
		createStorage();
		activeSession = true;
	}

	/**
	 * Getter for the sessionSlices class variable.
	 *
	 * @return
	 */
//    public Hashtable<String, HashSet<Pair<Integer, Integer>>> getSessionSlices() {
//        return sessionSlices;
//    }
	public HashSet<MementoTriple> getSessionSlices() {
		return sessionSlices;
	}

	/**
	 * If an active session exists, this method does the following: Places the
	 * passed 2D array of grid data into a memento. After the data is converted
	 * to a GridMemento, processMemento is called which stores the GridMemento
	 * for later use.
	 * <p/>
	 * Note: processMemento also tries to connect chronologically adjacent
	 * GridMementos that have the same internal state.
	 * <p/>
	 * If an active session does not exist, none of the above occurs.
	 *
	 * @param gridData
	 */
	@Background(serial = "mementoStorage")
	public void makeMemento(AbstractCellData[][] gridData, int start) {
		if (activeSession) {
			GridMemento gridMemento = new GridMemento();
			gridMemento.setGridState(gridData);
			sessionSlices.add(new MementoTriple(start, start + 1,
					gridMemento.getGridState()));

//            try {
//                queue.put(new Pair(gridMemento.getGridState(), start));
//            } catch (InterruptedException ie) {
//                Log.e("SessionCaretaker", "makeMemento", ie);
//            }
		}
	}

//    /**
//     * Method that consumes the contents of the queue.
//     * @throws InterruptedException
//     */
//    @Background
//    public void consumeData() {
//        while(activeSession)
//        {
//            try {
//                Pair<String, Integer> data = queue.poll(100,
//                        TimeUnit.MILLISECONDS);
//                if(null != data) {
//                    processMemento(data.first, data.second);
//                }
//            } catch (InterruptedException ie) {
//                Log.e("SessionCaretaker", "consumeData", ie);
//            }
//        }
//    }

	/**
	 * Returns whether or not the queue is empty.
	 *
	 * @return
	 */
	public boolean queueEmpty() {
		return queue.isEmpty();
	}
//
//    /**
//     * If sessionKey is not in the outer hash table, a new entry is added,
//     * timeKey becomes the first key of sessionKey's hash set, and timeValue
//     * becomes the value of timeKey's entry.
//     *
//     * If the passed string is in the outer hash table, there are two paths of
//     * execution:
//     * 1. If an entry in sessionKey's hash set has a value that
//     * matches timeKey, then that entry's value is replaced by timeValue.
//     * This should reduce the number of total entries per sessionKey and
//     * simplify code later on.
//     * 2. Otherwise, a new entry is made with timeKey and timeValue.
//     *
//     * Note: A sessionKey containing "deactivate" is used to shut down the
//     * consumeData method.
//     *
//     * @param sessionKey
//     * @param timeKey
//     */
//    @Background
//    public void processMemento(String sessionKey, int timeKey) {
//        if(sessionKey.equals("deactivate"))
//        { // session not active any longer
//            activeSession = false;
//            return; // leave method early
//        }
//
//        int timeValue = timeKey + 1;
//        if(null == sessionSlices)
//        {
//            Log.e("SessionCaretaker", "Storage not allocated");
//        }
//        if(null == sessionKey)
//        {
//            Log.e("SessionCaretaker", "Key is null");
//        }
//
//        if(!sessionSlices.containsKey(sessionKey))
//        { // sessionKey is not in sessionSlices, create new entry
//            HashSet<Pair<Integer, Integer>> times = new HashSet<Pair<Integer, Integer>>();
//            times.add(new Pair(timeKey, timeValue));
//            sessionSlices.put(sessionKey, times);
//        }
//        else
//        { // sessionKey in sessionSlices, the grid string already exists
//            HashSet<Pair<Integer, Integer>> times = sessionSlices.get(sessionKey);
//
//            // The entries in timeByStart have the same grid string key, so
//            // we need to know if a key in timeByStart has a value that
//            // matches the timeKey parameter, which means the times are
//            // adjacent.
//            // If we know the times are adjacent, we can change the value of
//            // the older start time key rather than make a redundant entry
//            boolean adjacentTimes = false;
//            Integer adjacentStart = null;
//
//            for(Pair<Integer, Integer> entry : times)
//            { // for each entry in the set of start and end times
//                if(timeKey == entry.second)
//                { // timeKey matches an existing end time
//                    adjacentTimes = true;
//                    adjacentStart = entry.first; // save old start
//                    times.remove(entry); // remove old
//                    times.add(new Pair(adjacentStart, timeValue)); // add new
//                    sessionSlices.put(sessionKey, times); // update slices
//                    break;
//                }
//            }
//            if(!adjacentTimes)
//            { // no time found adjacent to new one
//                times.add(new Pair(timeKey, timeValue)); // add new
//                sessionSlices.put(sessionKey, times); // update slices
//            }
//        }
//    }
}
