package com.tigerhix.musiz.enums;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tigerhix.musiz.TextureDict;

public enum NoteRank {
	
	MISS(-1, 0, 0, TextureDict.MISS), // Special: if you touch a note, whatever the offset, it won't be miss
	BAD(Integer.MAX_VALUE, 0.25, 0, TextureDict.BAD), // So once you touched a note, the worst rank is BAD not MISS
	GOOD(0.3, 0.5, 0.3, TextureDict.GOOD), 
	PERFECT(0.1, 1, 0.7, TextureDict.PERFECT),
	AMAZING(0.05, 1, 1, TextureDict.AMAZING); 
	
	private double maxOffsetSecond;
	private double scoreScale;
	private double tpScale;
	
	private Texture texture;
	
	private NoteRank(double maxOffsetSecond, double scoreScale, double tpScale, Texture texture) {
		this.maxOffsetSecond = maxOffsetSecond;
		this.scoreScale = scoreScale;
		this.tpScale = tpScale;
		this.texture = texture;
	}
	
	public double getMaxOffsetSecond() {
		return maxOffsetSecond;
	}
	
	public double getScoreScale() {
		return scoreScale;
	}
	
	public double getTpScale() {
		return tpScale;
	}
	
	public Texture getTexture() {
		return texture;
	}

}
