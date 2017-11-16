package cn.edu.nju.se.lcy.active.util;

import java.util.Collections;
import java.util.List;

public class NextInstanceListManager {

	public static void sortByTypeValue(List<MultiValueIndexItem> list, String typeName) {
		NextInstanceComparator mc = new NextInstanceComparator(typeName);

		Collections.sort(list, mc);

		for (MultiValueIndexItem item : list) {
			if(typeName.equals("gap")){
				item.gapIndex = list.indexOf(item);
			}else if(typeName.equals("max")){
				item.maxIndex = list.indexOf(item);
			}else if(typeName.equals("keyzero")){
				item.keyzeroIndex = list.indexOf(item);
			}else if(typeName.equals("keyvar")){
				item.keyvarIndex = list.indexOf(item);
			}else{
				
			}
		}
	}

	public static int sortByIndex(List<MultiValueIndexItem> list, String[] allName, String mostImportantName) {
		IndexComparator mc = new IndexComparator(allName, mostImportantName);

		Collections.sort(list, mc);
		
		return list.get(0).index;
	}

}
