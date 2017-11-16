package cn.edu.nju.se.lcy.active.util;

import java.util.Comparator;

public class IndexComparator implements Comparator<MultiValueIndexItem> {

	private String[] allIndexNames;
	private String mostImportantIndex;
	public IndexComparator(String[] strings, String string) {
		// TODO Auto-generated constructor stub
		this.allIndexNames = strings;
		this.mostImportantIndex = string;
	}

	public int compare(MultiValueIndexItem o1, MultiValueIndexItem o2) {
		// TODO Auto-generated method stub
		int firstValue = 0;
		int secondValue = 0;
		
		for(String indexName : this.allIndexNames){
			firstValue += this.getByName(indexName, o1);
			secondValue += this.getByName(indexName, o2);
		}
		
		int firstMostImprotant = this.getByName(this.mostImportantIndex, o1);
		int secondMostImprotant = this.getByName(this.mostImportantIndex, o2);
		
		if(firstValue > secondValue){
			return 1;
		}else{
			if(firstValue < secondValue){
				return -1;
			}else{
				if(firstMostImprotant > secondMostImprotant){
					return 1;
				}else{
					if(firstMostImprotant < secondMostImprotant){
						return -1;
					}else{
						return 0;
					}
				}
			}
		}
	}
	
	private int getByName(String indexName, MultiValueIndexItem item){
		if(indexName.equals("gap")){
			return item.gapIndex;
		}else if(indexName.equals("max")){
			return item.maxIndex;
		}else if(indexName.equals("keyzero")){
			return item.keyzeroIndex;
		}else if(indexName.equals("keyvar")){
			return item.keyvarIndex;
		}else{
			return 0;
		}
	}

}
