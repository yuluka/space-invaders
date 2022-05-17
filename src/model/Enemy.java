package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Enemy {

	@SuppressWarnings("unused")
	private Canvas canvas;
	private GraphicsContext gc;
	
	private int x,y;
	
	private Image skin;

	public Enemy(Canvas canvas, int x, int y) {
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		this.x = x;
		this.y = y;
		
		setSkin();
	}
	
	public void setSkin() {
		File file = new File("src/images/Invader.png");
		
		try {
			skin = new Image(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void paint() {
		gc.drawImage(skin, x, y);
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
}