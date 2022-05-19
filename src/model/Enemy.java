package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Enemy extends Thread {

	private final int ENEMY_MOVEMENT = 10;
	
	@SuppressWarnings("unused")
	private Canvas canvas;
	private GraphicsContext gc;
	
	private int x,y;
	
	private Image skin;
	
	private boolean isAlive = true;
	
	@Override
	public void run() {
		int counter = 1;
		
		while(isAlive) {
			int xMove;
			
			if(counter %2 == 0) {
				xMove = -ENEMY_MOVEMENT;
			} else {
				xMove = ENEMY_MOVEMENT;
			}
			
			x += xMove;
			
			counter++;
			
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
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
	
	public double getWidth() {
		return skin.getWidth();
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
}
