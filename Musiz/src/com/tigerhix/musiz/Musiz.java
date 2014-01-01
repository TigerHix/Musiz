package com.tigerhix.musiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.tigerhix.musiz.enums.NoteRank;
import com.tigerhix.musiz.enums.NoteScale;
import com.tigerhix.musiz.model.Note;

public class Musiz implements ApplicationListener {
	
	private static Stage stage;
	
	private static Image scanner;
	private static Image background;
	
	private static ArrayList<Note> notes = new ArrayList<Note>();
	
	private static long startMillis;
	private static double totalSecond;
	private static int totalRow;
	
	private static String level;
	private static String name;
	private static double scannerSecond; // Known as PAGE_SIZE
	private static double scannerOffset; // Known as PAGE_SHIFT
	
	private static int score;
	private static double tp = 100;
	
    @Override
    public void create() {
    	
        stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        
        // FPS counter
        
        LabelStyle labelStyle = new LabelStyle(new BitmapFont(), Color.GRAY); 
        Label fpsLabel = new Label("Initializing", labelStyle);
        fpsLabel.setName("fpsLabel");
        fpsLabel.setY(0); 
        fpsLabel.setX(stage.getWidth() - fpsLabel.getTextBounds().width); 
        stage.addActor(fpsLabel);
        
        // Score counter
         
        Label scoreLabel = new Label("Initializing", labelStyle);
        scoreLabel.setName("scoreLabel");
        scoreLabel.setY(stage.getHeight() - scoreLabel.getTextBounds().height - 50); 
        scoreLabel.setX(stage.getWidth() - scoreLabel.getTextBounds().width); 
        stage.addActor(scoreLabel);
        
        // System settings
        
        Gdx.input.setInputProcessor(stage);
        Texture.setEnforcePotImages(false);
        
        // Initialize scanner, background, ui... 
        
        new TextureDict();
        
        Texture texture = new Texture(Gdx.files.internal("data/scanner.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        scanner = new Image(texture);
        scanner.setOrigin(scanner.getWidth() / 2, scanner.getHeight() / 2);
        scanner.setSize(stage.getWidth(), stage.getHeight() * 0.05f);
        scanner.setTouchable(Touchable.disabled);
        
        stage.addActor(scanner);
        
        loadBeatmap("l2b");
        
        startGame();
        
    }
 
    @Override
    public void dispose() {
    	
        stage.dispose();
        
    }
    
    public void loadBeatmap(String path) {
    	
    	// beatmap.txt
    	
    	List<String> lines = Arrays.asList(Gdx.files.internal("data/songs/" + path + "/beatmap.txt").readString().split("\n"));
    	
    	for (String line : lines) {
    		String[] data = line.split("\t");
    		String type = data[0];
    		if (type.equals("LEVEL")) {
    			level = data[1];
    		} else if (type.equals("NAME")) {
    			name = data[1];
    		} else if (type.equals("SPEED")) {
    			scannerSecond = Double.valueOf(data[1]);
    		} else if (type.equals("OFFSET")) {
    			scannerOffset = Double.valueOf(data[1]) + scannerSecond;
    		} else if (type.equals("NOTE")) {
    			int noteRow = (int) ((Double.valueOf(data[2]) + scannerOffset) / scannerSecond);
    			notes.add(new Note(stage, Integer.valueOf(data[1]), Double.valueOf(data[2]), Double.valueOf(data[3]), NoteScale.NORMAL, noteRow % 2 == 0));
    		}
    	}
    	
    	// background.png
    	
    	Texture texture = new Texture(Gdx.files.internal("data/songs/" + path + "/background.png"));
    	texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    	
        background = new Image(texture);
        background.setOrigin(background.getWidth() / 2, background.getHeight() / 2);
        background.setSize(stage.getWidth(), stage.getHeight());
        background.setPosition(0, 0);
        background.setTouchable(Touchable.disabled);
        
        Color c = background.getColor();
		background.setColor(c.r, c.g, c.b, 0.1f);
        
        stage.addActor(scanner);
        stage.addActor(background);
    	
    }
    
    public void startGame() {
    	
    	startMillis = System.currentTimeMillis();
    	
    	// Generate notes
    	
    	Collections.reverse(notes);
    	
    	for (Note note : notes) {

    		note.move((float) note.getX() * stage.getWidth(), calculateY(stage.getHeight(), note.getSecond(), scannerOffset, scannerSecond));
    		
    	}
    	
    }
 
    @Override
    public void render() {
    	
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        
        // Update FPS counter

        Label fpsLabel = (Label) stage.getRoot().findActor("fpsLabel");
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + " LEVEL: " + level + " NAME: " + name + " SPEED: " + scannerSecond + " OFFSET: " + scannerOffset);
        fpsLabel.setX(stage.getWidth() - fpsLabel.getTextBounds().width);
        
        // Update score counter
        
        Label scoreLabel = (Label) stage.getRoot().findActor("scoreLabel");
        scoreLabel.setText("Score: " + score + " TP: " + ((int)(tp*100))/100);
        scoreLabel.setX(stage.getWidth() - scoreLabel.getTextBounds().width);
        
        // Get & set some information
        
        totalSecond = (double) (System.currentTimeMillis() - startMillis) / 1000;
        totalRow = (int) ((totalSecond + scannerOffset) / scannerSecond);
        
        // Update scanner
        
        if (totalRow % 2 == 0) { // Up to down
        	scanner.setY((float) (stage.getHeight() - stage.getHeight() * ((totalSecond + scannerOffset) % scannerSecond / scannerSecond)));
        } else { // Down to up
        	scanner.setY((float) (stage.getHeight() * ((totalSecond + scannerOffset) % scannerSecond / scannerSecond)));
        }
        
        scanner.setZIndex(Integer.MAX_VALUE); // Make sure the scanner overlays notes
        
        // Update notes
        
        for (Note note : notes) {
    		int noteRow = (int) ((note.getSecond() + scannerOffset) / scannerSecond);
    		int rowDifference = noteRow - totalRow;
    		if (rowDifference == 1) {
    			if (!note.isTouched()) {
    				// The next row is the row where the note should appear, so let's show it with blur to let user get ready for this note
        			if (totalRow % 2 == 0 && scanner.getY() < stage.getHeight() * 0.35) { // Up to down
        				note.showBlur();
        			} else if (totalRow % 2 == 1 && scanner.getY() > stage.getHeight() * 0.65) { // Down to up
        	        	note.showBlur();
        	        }
    			}
    		} else if (rowDifference == 0) {
    			// This is the row! Show it clearly! (unless it has been touched wrongly)
    			if (!note.isTouched()) note.showClear();
    		} else if (rowDifference == -1) {
    			if (!note.isTouched()) {
    				missNote(note);
    			}
    		}
    	}
        
    }

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public static void touchNote(Note note) {
		NoteRank rank = calculateNoteRank(note, totalSecond);
		score += 10000 * rank.getScoreScale();
		tp = (tp + 100 * rank.getTpScale()) / 2; 
		note.dispose(rank);
		/*
		final Image image = rank.getImage();
		stage.addActor(image);
		image.setSize(note.getImage().getWidth(), note.getImage().getHeight());
		image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
		image.setPosition(note.getImage().getX(), note.getImage().getY());
		image.setZIndex(0);
		float delay = 0.5f;
		Timer.schedule(new Task(){
		    @Override
		    public void run() {
		        image.remove();
		    }
		}, delay);
		*/
	}
	
	public static void missNote(Note note) {
		NoteRank rank = NoteRank.MISS;
		tp = (tp + 100 * rank.getTpScale()) / 2; 
		note.dispose(rank);
		/*
		final Image image = rank.getImage();
		stage.addActor(image);
		image.setSize(note.getImage().getWidth(), note.getImage().getHeight());
		image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
		image.setPosition(note.getImage().getX(), note.getImage().getY());
		image.setZIndex(0);
		float delay = 0.5f;
		Timer.schedule(new Task(){
		    @Override
		    public void run() {
		        image.remove();
		    }
		}, delay);
		*/
	}
	
	public static float calculateY(float areaHeight, double appearSecond, double offsetSecond, double speedSecond) {
		if ((int) (((appearSecond + offsetSecond) / speedSecond) % 2) == 0) { // Up to down
        	return (float) (areaHeight - areaHeight * ((appearSecond + offsetSecond) % speedSecond / speedSecond));
        } else { // Down to up
        	return (float) (areaHeight * ((appearSecond + offsetSecond) % speedSecond / speedSecond));
        }
	}
	
	public static NoteRank calculateNoteRank(Note note, double touchSecond) {
		NoteRank returnRank = NoteRank.BAD;
		double difference = Math.abs(note.getSecond() - touchSecond);
		for (NoteRank rank : NoteRank.values()) {
			if (difference < rank.getMaxOffsetSecond()) returnRank = rank; 
		}
		return returnRank;
	}
	
}
