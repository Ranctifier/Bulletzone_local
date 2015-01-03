package colin.bulletzone_colin_local.replay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.util.Arrays;
import java.util.HashSet;

import colin.bulletzone_colin_local.memento.MementoTriple;

/**
 * Created by Colin on 12/3/14.
 */
@EBean
public class ReplayDB extends SQLiteOpenHelper {
	private static volatile ReplayDB dbInstance = null; // object for singleton
	private static final String dbName = "BulletZoneReplayStorage.db";
	private static final int dbVersion = 1;
	private static SQLiteDatabase replayDB = null;
	private static boolean hasData;
	private static Context replayContext;

	/**
	 * Constructor that must exist to extend SQLiteOpenHelper.
	 *
	 * @param context
	 */
	public ReplayDB(Context context) {
		super(context, dbName, null, dbVersion);
		replayDB = super.getWritableDatabase();

		if (null == replayContext) {
			replayContext = context; // set this only once
		}
	}

	/**
	 * Used as part of singleton pattern, the purpose of which is to avoid
	 * queries to the database before the database is created.
	 *
	 * @param context
	 * @return
	 */
	public static ReplayDB getInstance(Context context) {
		if (null == dbInstance) {
			synchronized (ReplayDB.class) {
				if (null == dbInstance) {
					dbInstance = new ReplayDB(context);
				}
			}
		}
		return dbInstance;
	}

	/**
	 * This method runs when ReplayDB is created.
	 *
	 * @param db
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(getDBSchema());
	}

	/**
	 * This method runs when the schema for ReplayDB is changed.
	 *
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * Returns SQL strings that will make ReplayDB's schema.
	 *
	 * @return
	 */
	public String getDBSchema() {
		String sessionDataSchema =
				"CREATE TABLE IF NOT EXISTS SESSION_DATA(" +
						"StartPoll INTEGER," +
						" EndPoll INTEGER," +
						" GridString TEXT," +
						" PRIMARY KEY (StartPoll, EndPoll, GridString));";
		return sessionDataSchema;
	}

	/**
	 * First, sessionData is iterated, and in each iteration an SQL string is
	 * generated that will insert strings that are equivalent to the data from
	 * sessionData into the ReplayDB. After the current iteration's string is
	 * generated, it is used in a raw query to place it into ReplayDB's
	 * SESSION_DATA table.
	 *
	 * @param sessionData
	 */
	@Background
	public void batchInsert(HashSet<MementoTriple> sessionData) {
		// formatted string that can be repeatedly used in rawQuery for inserts
		String insertRaw = "INSERT INTO SESSION_DATA(StartPoll, EndPoll, " +
				"GridString) VALUES(";
		String[] valueParams; // used to replace the ? in insertRaw for a rawQuery
		ContentValues cv = new ContentValues();
		MementoTriple[] data = sessionData.toArray(new MementoTriple[sessionData.size()]);
		Arrays.sort(data);

		for (int i = 0; i < data.length; i++) {
			cv.put("StartPoll", data[i].start);
			cv.put("EndPoll", data[i].end);
			cv.put("GridString", data[i].grid);
			replayDB.insert("SESSION_DATA", null, cv);
			cv.clear();
		}
	}

	/**
	 * Generates and runs a SQL query that deletes the rows in the SESSIONS_DATA
	 * table.
	 */
	@Background
	public void deleteSession() {
		String deleteRaw = "DELETE FROM SESSION_DATA";
//        replayDB.rawQuery(deleteRaw, null);
		replayDB.delete("SESSION_DATA", null, null);
	}

	/**
	 * Returns a cursor containing the data from the SESSION_DATA table. Sets
	 * the hasData variable as a result;
	 *
	 * @return
	 */
	public Cursor getSession() {
		String getSessionRaw = "SELECT * FROM SESSION_DATA ORDER BY StartPoll ASC";
		Cursor data = replayDB.query(
				false, // distinct
				"SESSION_DATA", // table
				null, // select *
				null, // where
				null, // selectargs
				null, // groupby
				null, // having
				"StartPoll ASC", // order by
				null); // limit
		if (null == data) { // no cursor, no data
			hasData = false;
		} else { // have cursor
			if (data.getCount() == 0) { // no data
				hasData = false;
			} else { // have data
				hasData = true;
			}
		}

		return data;
	}

	/**
	 * Getter for hasData.
	 *
	 * @return
	 */
	public boolean hasData() {
		return hasData;
	}

	/**
	 * Getter for replayContext.
	 *
	 * @return
	 */
	public static Context getContext() {
		return replayContext;
	}
}