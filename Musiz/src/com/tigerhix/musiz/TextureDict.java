package com.tigerhix.musiz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class TextureDict {
	
	public static Texture AMAZING;
	public static Texture PERFECT;
	public static Texture GOOD;
	public static Texture BAD;
	public static Texture MISS;
	
	public TextureDict() {
		
		AMAZING = new Texture(Gdx.files.internal("data/texture/amazing.png"));
		PERFECT = new Texture(Gdx.files.internal("data/texture/perfect.png"));
		GOOD = new Texture(Gdx.files.internal("data/texture/good.png"));
		BAD = new Texture(Gdx.files.internal("data/texture/bad.png"));
		MISS = new Texture(Gdx.files.internal("data/texture/miss.png"));
		
		AMAZING.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		PERFECT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		GOOD.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		BAD.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		MISS.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
	}

}
