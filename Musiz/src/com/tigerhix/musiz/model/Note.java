package com.tigerhix.musiz.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.tigerhix.musiz.Musiz;
import com.tigerhix.musiz.enums.NoteRank;
import com.tigerhix.musiz.enums.NoteScale;

public class Note {
	
	private Stage stage;
	private Image image;
	
	private int id;
	private double second;
	private double x;
	private NoteScale scale;
	
	private boolean showed;
	private boolean touched;
	
	public Note(Stage stage, int id, double second, double x, NoteScale scale, boolean down) {
		this.stage = stage;
		this.scale = scale;
		this.setId(id);
		this.setSecond(second);
		this.setX(x);
		Texture texture = new Texture(Gdx.files.internal("data/note" + (down ? "Down" : "Up") + ".png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		image = new Image(texture);
		image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
		image.setSize((float) (stage.getWidth() / 6.5 * scale.getScale()), (float) (stage.getWidth() / 6.5 * scale.getScale()));
		image.setBounds(0, 0, image.getWidth(), image.getHeight());
		image.setVisible(false);
		image.setTouchable(Touchable.enabled);
		image.addListener(new InputListener() {
			
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (showed && !touched) {
                	//if (Math.abs(image.getWidth() - x) < image.getWidth() * 0.75 && Math.abs(image.getWidth() - y) < image.getWidth() * 0.75) {
                		touched = true;
                		image.setTouchable(Touchable.disabled);
                		Musiz.touchNote(Note.this);
                	//}
                }
                return true;
			}
 
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
        	}
        
		});
		stage.addActor(image);
	}
	
	public void move(float x, float y) {
		image.setX(x - image.getWidth() / 2);
		image.setY(y - image.getHeight() / 2);
	}
	
	public void showBlur() {
		showed = true;
		if (!image.isVisible()) image.setVisible(true); 
		Color c = image.getColor();
		image.setColor(c.r, c.g, c.b, 0.3f);
	}
	
	public void showClear() {
		showed = true;
		if (!image.isVisible()) image.setVisible(true); 
		Color c = image.getColor();
		image.setColor(c.r, c.g, c.b, 1f);
	}
	
	public void dispose(NoteRank rank) {
		float x = image.getX();
		float y = image.getY();
		image.remove();
		image = new Image(new Sprite(rank.getTexture()));
		image.setPosition(x, y);
		image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
		image.setSize((float) (stage.getWidth() / 6.5 * scale.getScale()), (float) (stage.getWidth() / 6.5 * scale.getScale()));
		image.setTouchable(Touchable.disabled);
		stage.addActor(image);
		Timer.schedule(new Task(){
		    @Override
		    public void run() {
		        image.remove();
		    }
		}, 0.5f);
	}
	
	public Image getImage() {
		return image;
	}
	
	public boolean isTouched() {
		return touched;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getSecond() {
		return second;
	}

	public void setSecond(double second) {
		this.second = second;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

}
