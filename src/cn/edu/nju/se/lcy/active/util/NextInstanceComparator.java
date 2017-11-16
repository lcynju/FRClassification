package cn.edu.nju.se.lcy.active.util;

import java.util.Comparator;

public class NextInstanceComparator implements Comparator<MultiValueIndexItem> {

	private String type = "";
	
	public NextInstanceComparator(String type){
		this.type = type;
	}
	
	public int compare(MultiValueIndexItem item1, MultiValueIndexItem item2) {
		// TODO Auto-generated method stub
		double item1Value = 0;
		double item2Value = 0;
		if(this.type.equals("max")){
			item1Value = item1.getMax();
			item2Value = item2.getMax();
		}else if(this.type.equals("gap")){
			item1Value = item1.getGap();
			item2Value = item2.getGap();
		}else if(this.type.equals("keyzero")){
			item1Value = item1.getKeyzero();
			item2Value = item2.getKeyzero();
		}else if(this.type.equals("keyvar")){
			item1Value = item1.getKeyvar();
			item2Value = item2.getKeyvar();
		}
		
		if(item1Value > item2Value){
			return 1;
		}else{
			if(item1Value < item2Value){
				return -1;
			}else{
				return 0;
			}
		}
	}

}
