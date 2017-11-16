package cn.edu.nju.se.lcy.active.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MultiValueIndexItem i1 = new MultiValueIndexItem(1);
		MultiValueIndexItem i2 = new MultiValueIndexItem(2);
		MultiValueIndexItem i3 = new MultiValueIndexItem(3);
		MultiValueIndexItem i4 = new MultiValueIndexItem(4);
		MultiValueIndexItem i5 = new MultiValueIndexItem(5);
		MultiValueIndexItem i6 = new MultiValueIndexItem(6);
		
		i1.setGap(0.4);
		i2.setGap(0.2);
		i3.setGap(0.3);
		i4.setGap(0.5);
		i5.setGap(0.1);
		i6.setGap(0.8);
		
		List<MultiValueIndexItem> is = new ArrayList<MultiValueIndexItem>();
		is.add(i1);
		is.add(i2);
		is.add(i3);
		is.add(i4);
		is.add(i5);
		is.add(i6);
		
		NextInstanceComparator nic = new NextInstanceComparator("gap");
		Collections.sort(is, nic);
		for(MultiValueIndexItem mvi : is){
			System.out.println(mvi.getGap());
		}
	}

}
