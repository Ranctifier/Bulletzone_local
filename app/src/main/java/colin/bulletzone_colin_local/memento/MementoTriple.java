package colin.bulletzone_colin_local.memento;

/**
 * Created by Colin on 12/7/2014.
 */
public class MementoTriple implements Comparable<MementoTriple> {
	public Integer start;
	public Integer end;
	public String grid;

	/**
	 * Constructor.
	 *
	 * @param start
	 * @param end
	 * @param grid
	 */
	public MementoTriple(Integer start, Integer end, String grid) {
		this.start = start;
		this.end = end;
		this.grid = grid;
	}

	/**
	 * Allows MementoTriple to be stored in a sortable structure.
	 *
	 * @param mt
	 * @return
	 */
	@Override
	public int compareTo(MementoTriple mt) {
		if (mt.start < this.start) {
			return 1;
		} else if (mt.start == this.start) {
			if (mt.end < this.end) {
				return 1;
			} else if (mt.end == this.end) {
				return 0;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}
}
