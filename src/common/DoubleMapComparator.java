package common;

import java.util.Comparator;
import java.util.Map;

public class DoubleMapComparator implements Comparator<Integer> {

	private Map<Integer, Double> map;
	
	public DoubleMapComparator(Map<Integer, Double> map) {
		this.map = map;
	}

	@Override
	public int compare(Integer key1, Integer key2) {
        Double val1 = this.map.get(key1);
        Double val2 = this.map.get(key2);
        if (val1 != null && val2 != null) {
        	return (val1 >= val2 ? - 1 : 1);
        }
        return 0;
	}
}