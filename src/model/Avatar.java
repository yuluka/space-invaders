package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Avatar {
	
	@SuppressWarnings("unused")
	private Canvas canvas;
	private GraphicsContext gc;
	
	private int x,y;
	
	private Image skin;
	private ArrayList<Image> explosionSprites;
	private int frame = 0;
	
	public Avatar(Canvas canvas) {
		this.canvas = canvas;
		gc = canvas.getGraphicsContext2D();
		
		this.explosionSprites = new ArrayList<Image>();
		
		setSkin();
		setSprites();
		
		x = (int) (canvas.getWidth()/2) - (int) (skin.getWidth()/2);
		y = (int) canvas.getHeight() - (int) skin.getHeight();
	}
	
	public void setSkin() {
		File file = new File("src/images/Ship.png");
		
		try {
			skin = new Image(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void setSprites() {
		try {
			for (int i = 1; i <= 5; i++) {
				File file = new File("src/images/Explosion " + i + ".png");
				explosionSprites.add(new Image(new FileInputStream(file)));
			}	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void paint() {
		gc.drawImage(skin, x, y);
	}
	
	public void destroyPlayer() {
		gc.drawImage(explosionSprites.get(frame%5), x, y);
		frame++;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void moveXBy(int pixels) {
		this.x += pixels;
	}
	
	public void moveYBy(int pixels) {
		this.y += pixels;
	}
	
	public void moveXYBy(int pixelsX, int pixelsY) {
		this.x += pixelsX;
		this.y += pixelsY;
	}
	
	public double getWidth() {
		return skin.getWidth();
	}
	
	public double getHeight() {
		return skin.getHeight();
	}
}
