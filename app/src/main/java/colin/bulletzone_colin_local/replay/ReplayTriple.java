package colin.bulletzone_colin_local.replay;

import colin.bulletzone_colin_local.cell.cell_data.AbstractCellData;

/**
 * Created by Colin on 12/7/2014.
 */
public class ReplayTriple implements Comparable<ReplayTriple> {
	public Integer start;
	public Integer end;
	public AbstractCellData[][] grid;

	/**
	 * Constructor.
	 *
	 * @param start
	 * @param end
	 * @param grid
	 */
	public ReplayTriple(int start, int end, AbstractCellData[][] grid) {
		this.start = start;
		this.end = end;
		this.grid = grid;
	}

	/**
	 * Allows the replay triple to be sorted in some other structure. The
	 * sorting is done primarily with the start variable, though if the starts
	 * are equal then it is sorted by the end variable. Does not handle if rt's
	 * start > this start and rt's end < this end.
	 *
	 * @param rt
	 * @return
	 */
	@Override
	public int compareTo(ReplayTriple rt) {
		if (rt.start < this.start) {
			return 1;
		} else if (rt.start == this.start) {
			if (rt.end < this.end) {
				return 1;
			} else if (rt.end == this.end) {
				return 0;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}
}
