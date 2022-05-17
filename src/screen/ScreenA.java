package screen;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import model.Avatar;
import model.Bullet;
import model.Enemy;

public class ScreenA {
	
	private Canvas canvas;
	private GraphicsContext gc;
	
	private Avatar avatar;
	private ArrayList<Bullet> bullets;
	private ArrayList<Enemy> enemies;
	
	private boolean keyW = false;
	private boolean keyA = false;
	private boolean keyS = false;
	private boolean keyD = false;
	private boolean keySpace = false;
	
	public ScreenA(Canvas canvas) {
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		
		avatar = new Avatar(canvas);
		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		
		createEnemies();
	}
	
	public void createEnemies() {
		for (int i = 0; i < 5; i++) {
			int x = (int) (canvas.getWidth()/5) + 20;
			
			enemies.add(new Enemy(canvas,x*i,100));
		}
	}
	
	public void paint() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
		
		avatar.paint();
		
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).paint();
			
			if(-bullets.get(i).getY() >= 0) {
				bullets.remove(i);
				--i;
			}
		}
		
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).paint();
		}
		
		//Calcular distancia
		try {
			for (int i = 0; i < enemies.size(); i++) {
				for (int j = 0; j < bullets.size(); j++) {
					//Comparar
					Enemy e = enemies.get(i);
					Bullet p = bullets.get(j);
					
					double distance = Math.sqrt(Math.pow(e.getX()-p.getX(), 2) + 
							Math.pow(e.getY()-p.getY(), 2));
					
					if(distance <= 30) {
						bullets.remove(j);
						enemies.remove(i);
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("El juego terminó");
		}
	}
	
	public void onKey() {
		if(keyW) {
			avatar.moveYBy(-5);
		} if(keyA) {
			avatar.moveXBy(-5);
		} if(keyS) {
			avatar.moveYBy(5);
		} if(keyD) {
			avatar.moveXBy(5);
		} if(keySpace) {
			shoot();
		}
	}
	
	public void shoot() {
		int bulletX = avatar.getX() + (int)(avatar.getWidth()/2)-(Bullet.getSize()/2);
		int bulletY = avatar.getY() - (Bullet.getSize());
		
		bullets.add(new Bullet(canvas,bulletX,bulletY));
	}
	
	public void onKeyPressed(KeyEvent e) {
		if(e.getCode().equals(KeyCode.A)) {
			keyA = true;
		} if(e.getCode().equals(KeyCode.W)) {
			keyW = true;
		} if(e.getCode().equals(KeyCode.S)) {
			keyS = true;	
		} if(e.getCode().equals(KeyCode.D)) {
			keyD = true;
		} if(e.getCode().equals(KeyCode.SPACE)) {
			keySpace = true;
		}
		
		onKey();
	}
	
	public void onKeyReleased(KeyEvent e) {
		if(e.getCode().equals(KeyCode.W)) {
			keyW = false;
		} else if(e.getCode().equals(KeyCode.A)) {
			keyA = false;			
		} else if(e.getCode().equals(KeyCode.S)) {
			keyS = false;
		} else if(e.getCode().equals(KeyCode.D)) {
			keyD = false;
		} else if(e.getCode().equals(KeyCode.SPACE)) {
			keySpace = false;
		}
		
		onKey();
	}
	
	public boolean isEndGame() {
		if(enemies.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
