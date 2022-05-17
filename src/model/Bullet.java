package model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bullet {
	
	@SuppressWarnings("unused")
	private Canvas canvas;
	private GraphicsContext gc;
	
	private final int SPEED = 10;
	private static final int SIZE = 15;
	
	private int x;
	private int y;
	private int size;
	private int speed;
	
	public Bullet(Canvas canvas, int x, int y) {
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		this.x = x;
		this.y = y;
		this.size = SIZE;
		this.speed = SPEED;
	}
	
	public void paint() {
		gc.setFill(Color.WHITE);
		gc.fillOval(x, y, size, size);
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
	
	public static int getSize() {
		return SIZE;
	}
}
