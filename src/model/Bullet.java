package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Bullet {
	
	@SuppressWarnings("unused")
	private Canvas canvas;
	private GraphicsContext gc;
	
	private final int SPEED = 10;
	
	private int x;
	private int y;
	private int speed;
	private static Image skin;
	
	public Bullet(Canvas canvas, int x, int y) {
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		this.x = x;
		this.y = y;
		this.speed = SPEED;
		
		setSkin();
	}
	
	public static void setSkin() {
		File file = new File("src/images/Bullet.png");
		try {
			skin = new Image(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void paint() {
		gc.drawImage(skin, x, y);
		y -= speed;
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
	
	public static double getWidth() {
		setSkin();
		
		return skin.getWidth();
	}
	
	public static double getHeight() {
		setSkin();
		
		return skin.getHeight();
	}
}
