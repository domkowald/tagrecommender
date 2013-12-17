package common;

import java.util.Comparator;
import java.util.Map;

public class IntMapComparator implements Comparator<Integer> {

	private Map<Integer, Integer> map;
	
	public IntMapComparator(Map<Integer, Integer> map) {
		this.map = map;
	}

	@Override
	public int compare(Integer key1, Integer key2) {
        Integer val1 = this.map.get(key1);
        Integer val2 = this.map.get(key2);
        if (val1 != null && val2 != null) {
        	return (val1 >= val2 ? - 1 : 1);
        }
        return 0;
	}
}