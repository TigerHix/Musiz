package com.tigerhix.musiz.enums;

public enum NoteScale {
	
	SMALL(0.8), NORMAL(1), BIG(1.2);
	
	private double scale;
	
	private NoteScale(double scale) {
		this.scale = scale;
	}
	
	public double getScale() {
		return scale;
	}

}
