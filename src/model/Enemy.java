package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Enemy extends Thread {

	private final int ENEMY_MOVEMENT = 10;
	
	@SuppressWarnings("unused")
	private Canvas canvas;
	private GraphicsContext gc;
	
	private int x,y;
	
	private static Image skin;
	private ArrayList<Image> explosionSprites;
	
	private boolean isAlive = true;
	private int frame = 0;
	
	public Enemy(Canvas canvas, int x, int y) {
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		this.x = x;
		this.y = y;
		this.explosionSprites = new ArrayList<Image>();
		
		setSkin();
		setSprites();
	}
	
	public static void setSkin() {
		File file = new File("src/images/Invader.png");		
		
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
	
	public void destroyInvader() {
		gc.drawImage(explosionSprites.get(frame%5), x, y);
		frame++;
	}
	
	@Override
	public void run() {
		int xMove = ENEMY_MOVEMENT;
		
		while(isAlive) {
			int position = x + (int)getWidth();
			
			if(position >= canvas.getWidth()) {
				y += getHeight();
				xMove *= -1;
				
			} else if(x <= 0) {
				y += getHeight();
				xMove *= -1;
			}
			
			x += xMove;
			
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
}
