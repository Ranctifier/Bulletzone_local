package colin.bulletzone_colin_local;

import android.content.Context;

/**
 * Created by Colin on 12/6/14.
 */
public class AppContext {
	private static Context appContext;
	private static int gridRows = 16; // default number of rows and columns
	private static int gridColumns = 16;

	/**
	 * Constructor.
	 *
	 * @param context
	 */
	public AppContext(Context context) {
		if (null == appContext) {
			appContext = context; // only set this once
		}
	}

	/**
	 * Returns the context.
	 *
	 * @return
	 */
	public static Context getAppContext() {
		return appContext;
	}

	/**
	 * Setter for gridRows.
	 *
	 * @param rows
	 */
	public static void setGridRows(int rows) {
		gridRows = rows;
	}

	/**
	 * Setter for gridColumns.
	 *
	 * @param columns
	 */
	public static void setGridColumns(int columns) {
		gridColumns = columns;
	}

	/**
	 * Getter for gridRows.
	 *
	 * @return
	 */
	public static int getGridRows() {
		return gridRows;
	}

	/**
	 * Getter for gridColumns.
	 *
	 * @return
	 */
	public static int getGridColumns() {
		return gridColumns;
	}
}