package cn.edu.nju.se.lcy.active.util;

public class MultiValueIndexItem{

	public int index;
	private double gap = 0.0;
	private double max = 0.0;
	private double keyzero = 0.0;
	private double keyvar = 0.0;

	public int gapIndex;
	public int maxIndex;
	public int keyzeroIndex;
	public int keyvarIndex;
	
	public int indexForSort;

	public MultiValueIndexItem(int index) {
		this.index = index;

		this.gapIndex = -1;
		this.maxIndex = -1;
		this.keyzeroIndex = -1;
		this.keyvarIndex = -1;
	}
	
	public double getGap() {
		return gap;
	}

	public void setGap(double gap) {
		this.gap = gap;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getKeyzero() {
		return keyzero;
	}

	public void setKeyzero(double keycounts) {
		this.keyzero = keycounts;
	}

	public double getKeyvar() {
		return keyvar;
	}

	public void setKeyvar(double keyvar) {
		this.keyvar = keyvar;
	}

}
