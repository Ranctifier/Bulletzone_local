package colin.bulletzone_colin_local.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import colin.bulletzone_colin_local.cell.cell_graphic.AbstractCellGraphic;
import colin.bulletzone_local.R;

/**
 * Created by Colin on 11/15/2014.
 */
public class BulletZoneAdapter extends BaseAdapter {
	Context context;
	int layoutResourceId;
	private AbstractCellGraphic[] cells;
	private int rows, columns;
	private int rowPixels, colPixels;

	/**
	 * Constructor to put the adapter in the proper context and associated with
	 * the correct gridview.
	 *
	 * @param context
	 * @param layoutResourceId
	 */
	public BulletZoneAdapter(Context context, int layoutResourceId) {
		super();
		this.cells = new AbstractCellGraphic[256]; // default grid size 16x16
		this.layoutResourceId = layoutResourceId;
		this.context = context;
	}

	/**
	 * Returns the number of items in the adapter.
	 *
	 * @return
	 */
	@Override
	public int getCount() {
		return cells.length;
	}

	/**
	 * Returns an item using the specified index.
	 *
	 * @param i
	 * @return
	 */
	@Override
	public AbstractCellGraphic getItem(int i) {
		return cells[i];
	}

	/**
	 * Sets the dimensions for the array of this adapter, assumes the array must
	 * be re-instantiated and that the relevant layouts must be updated.
	 *
	 * @param rows
	 * @param columns
	 */
	public void setDimensions(int rows, int columns, boolean inGame) {
		this.rows = rows;
		this.columns = columns;
		this.cells = new AbstractCellGraphic[this.rows * this.columns];
		updateLayouts(inGame);
	}

	/**
	 * Updates the gameGrid UI element, the grid_cell UI element, and the
	 * cell_image UI element using the rows and columns instance variables.
	 * Assumes that the gridFrame UI element has a layout width of 400dp and a
	 * layout height of 400dp. First obtains the scale factor from the pixel
	 * density of the display. A conversion from dp to pixels is in the form of:
	 * int pixels = (int) (dp * scale + 0.5f); The conversion formula is from
	 * the android documentation.
	 */
	private void updateLayouts(boolean inGame) {
		float scale = context.getResources().getDisplayMetrics().density;
		int gridFrameSideLength = (int) (400 * scale + 0.5f);
		colPixels = gridFrameSideLength / columns;
		rowPixels = gridFrameSideLength / rows;

		LayoutInflater inflater = LayoutInflater.from(context);
		// update the gameGrid layout
		FrameLayout frame;
		GridView grid;
		if (inGame) { // called from Dispatcher via GameActivity
			frame = (FrameLayout) inflater.inflate(R.layout.game_activity, null);
		} else { // called from ReplayActivity
			frame = (FrameLayout) inflater.inflate(R.layout.replay_activity, null);
		}

		grid = (GridView) frame.findViewById(R.id.gameGrid);
		grid.setNumColumns(columns);
		grid.setColumnWidth(colPixels);
		// update the gridCell layout
		FrameLayout cellFrame;
		cellFrame = (FrameLayout) inflater.inflate(R.layout.grid_cell, null);
		cellFrame.setLayoutParams(new FrameLayout.LayoutParams(colPixels,
				rowPixels));
	}


	/**
	 * Puts an item into the adapter using an index.
	 *
	 * @param i
	 * @param graphic
	 */
	public void putItem(int i, AbstractCellGraphic graphic) {
		cells[i] = graphic;
	}

	/**
	 * Returns the id of an item, unused for now.
	 *
	 * @param i
	 * @return
	 */
	@Override
	public long getItemId(int i) {
		return 0;
	}

	/**
	 * This method is overridden to fill the viewGroup associated with this
	 * adapter with something other than TextViews, in this case, ImageViews.
	 * <p/>
	 * Obtains the AbstractCellGraphic using the specified index and removes the
	 * passed view is either replaced or reused. If replaced, the view has its
	 * contents set to the bitmap in the AbstractCellGraphic. The view is then
	 * re-added to the viewGroup at the view's original index.
	 *
	 * @param i
	 * @param view
	 * @param viewGroup
	 * @return
	 */
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		View grid;
		LayoutInflater inflater = LayoutInflater.from(context);

		if (null == view) {
			grid = new View(context);
			grid = inflater.inflate(R.layout.grid_cell, null);

			ImageView iv = (ImageView) grid.findViewById(R.id.cell_image);
			iv.setLayoutParams(new FrameLayout.LayoutParams(colPixels,
					rowPixels));
			AbstractCellGraphic curr_cell = cells[i];
			if (null == curr_cell) {
				iv.setImageResource(R.drawable.grass);
			} else {
				iv.setImageResource(curr_cell.getResId());
			}
		} else {
			grid = (View) view;
		}

		return grid;
	}
}
