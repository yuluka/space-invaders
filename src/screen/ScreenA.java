package screen;

import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import model.Avatar;
import model.Bullet;
import model.Enemy;

public class ScreenA {
	
	private final int ENEMIES = 5;
	
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
		for (int i = 0; i < ENEMIES; i++) {
			int x = (int) (canvas.getWidth()/ENEMIES) + 20;
			
			enemies.add(new Enemy(canvas,x*i,100));
		}
		
		startAutomatas();
	}
	
	public void startAutomatas() {
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).start();
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
		for (int i = 0; i < enemies.size(); i++) {
			for (int j = 0; j < bullets.size(); j++) {
				//Comparar
				Enemy e = enemies.get(i);
				Bullet b = bullets.get(j);
				
				int eX = e.getX()+(int) (e.getWidth()/2);
				int bX = b.getX()+(int) (Bullet.getSize()/2); //Para que la distancia se calcule con base en la mitad del enemigo y no de su derecha.
				
				double distance = Math.sqrt(Math.pow(eX-bX, 2) + 
						Math.pow(e.getY()-b.getY(), 2));
				
				if(distance <= 15) {
					bullets.remove(j);
					enemies.remove(i);
					
					return;
				}
			}
		}
	}
	
	public void paintStars() {
		ArrayList<Double> x = new ArrayList<Double>();
		ArrayList<Double> y = new ArrayList<Double>();
		
		for(int i=0 ; i< 25 ; i++) {
			x.add(50.0+25*i);
			y.add(50*Math.random());
		}
		
		double[] resX = getMinMax(x);
		double minX = resX[0];
		double maxX = resX[1];
		
		double[] resY = getMinMax(y);
		double minY = resY[0];
		double maxY = resY[1];
		
		double deltaPX = canvas.getWidth();
		double deltaDias = maxX-minX;
		double pendienteX = deltaPX/deltaDias;
		double interceptoX = pendienteX*minX*(-1);
		
		double deltaPY = -canvas.getHeight();
		double deltaAccidente = maxY-minY;
		double pendienteY = deltaPY/deltaAccidente;
		double interceptoY = pendienteY*maxY*(-1);
		
		gc.setFill(Color.WHITE);
		for(int i=0 ; i < x.size() ; i++) {
			gc.fillOval( 
					conversion(pendienteX, interceptoX, x.get(i))-3,
					conversion(pendienteY, interceptoY, y.get(i))-3, 3,3);
		}
	}
	
	private double conversion(double m, double b, double input) {
		return m*input+b;
	}

	private double[] getMinMax(ArrayList<Double> arr) {
		ArrayList<Double> aux = new ArrayList<>();
		aux.addAll(arr);
		Collections.sort(aux);
		double min = aux.get(0);
		double max = aux.get(aux.size()-1);
		return new double[] {min, max};
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
