package screen;

import java.applet.AudioClip;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import model.Avatar;
import model.Bullet;
import model.Enemy;

public class ScreenA {
	
	private final static int ENEMIES = 10;
	private final static int SCORE_PER_ENEMY = 10;
	private final int LINES = 1;
	
	private Canvas canvas;
	private GraphicsContext gc;
	
	private Avatar avatar;
	
	private static AudioClip gameOverSound;
	private AudioClip shootSound;
	private AudioClip explosionSound;
	
	private static int score = 0;
	
	private ArrayList<Bullet> bullets;
	private static ArrayList<Enemy> enemies;
	
	private boolean keyW = false;
	private boolean keyA = false;
	private boolean keyS = false;
	private boolean keyD = false;
	private boolean keySpace = false;
	
	public ScreenA(Canvas canvas) {
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		score = 0;
		
		avatar = new Avatar(canvas);
		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		
		createEnemies();
		
		shootSound = java.applet.Applet.newAudioClip(getClass().getResource("/sounds/Shoot.wav"));
		explosionSound = java.applet.Applet.newAudioClip(getClass().getResource("/sounds/Killed enemy.wav"));
		gameOverSound = java.applet.Applet.newAudioClip(getClass().getResource("/sounds/Game over.wav"));
	}
	
	public void createEnemies() {
		int lineEnemies = ENEMIES/LINES;
		
		int y = 25;
		double separation = Enemy.getWidth();
		int x = 25;
		
		for (int i = 0; i < ENEMIES; i++) {

			if(enemies.size() != 0 && (enemies.size()) %lineEnemies == 0) {
				y += Enemy.getHeight();
				x = 25;
			}
			
			enemies.add(new Enemy(canvas,x,y));
			
			x += separation+Enemy.getWidth()/2;
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
		
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).paint();
			
			if(-bullets.get(i).getY() >= 0) {
				bullets.remove(i);
				--i;
			}
		}
		
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).paint();
			
			for (int j = 0; j < enemies.get(i).getBullets().size(); j++) {
				enemies.get(i).getBullets().get(j).paintForEnemy();
			}
		}
		
		if(avatar != null) {
			avatar.paint();	
			enemyPlayerDistance();
			playerBulletDistance();
		}
		
		enemyBulletDistance();
	}
	
	public void enemyBulletDistance() {
		//Calcular distancia
		for (int i = 0; i < enemies.size(); i++) {
			for (int j = 0; j < bullets.size(); j++) {
				//Comparar
				Enemy e = enemies.get(i);
				Bullet b = bullets.get(j);
				
				int eX = e.getX()+(int) (Enemy.getWidth()/2);
				int bX = b.getX()+(int) (Bullet.getWidth()/2); //Para que la distancia se calcule con base en la mitad del enemigo y no de su derecha.
				
				double distance = Math.sqrt(Math.pow(eX-bX, 2) + 
						Math.pow(e.getY()-b.getY(), 2));
				
				if(distance <= 15) {					
					bullets.remove(j);
					
					destroyInvader(enemies.remove(i));
					
					return;
				}
			}
		}
	}
	
	public void playerBulletDistance() {
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			
			for (int j = 0; j < enemy.getBullets().size(); j++) {
				if(avatar != null) {
					Bullet bullet = enemy.getBullets().get(j);
					
					int bX = bullet.getX() + (int)(Bullet.getWidth()/2);
					int pX = avatar.getX() + (int)(avatar.getWidth()/2);
					
					double distance = Math.sqrt(Math.pow(bX-pX, 2) +
							Math.pow(avatar.getY()-bullet.getY(), 2));
					
					if(distance <= 30) {
						enemy.getBullets().remove(j);
						
						destroyAvatar();
						
						return;
					}
				} else {
					j = enemy.getBullets().size();
					i = enemies.size();
				}	
			}
		}
	}
	
	public void enemyPlayerDistance() {
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			
			int eX = e.getX()+(int) (Enemy.getWidth()/2);
			int pX = avatar.getX()+(int) (avatar.getWidth()/2);
			
			double distance = Math.sqrt(Math.pow(eX-pX, 2) + 
					Math.pow(e.getY()-avatar.getY(), 2));
			
			if(distance <= 25) {
				destroyInvader(enemies.remove(i));
				destroyAvatar();
				
				return;
			}
		}
	}
	
	public void destroyInvader(Enemy enemy) {
		new Thread(() -> {
			int frame = 0;
			
			while(frame < 5) {
				Platform.runLater(() -> {
					enemy.destroyInvader();
				});
				
				frame++;
				
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		score += SCORE_PER_ENEMY;
		
		explosionSound.play();
	}
	
	public void destroyAvatar() {
		Avatar avatarAux = avatar;
		
		new Thread(() -> {
			int frame = 0;
			
			while(frame < 5) {
				Platform.runLater(() -> {
					avatarAux.destroyPlayer();
				});
				
				frame++;
				
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		avatar = null;
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
		if(avatar != null) {
			if(keyW) {
				if(avatar.getY() >= 0) {
					avatar.moveYBy(-5);	
				}

			} if(keyA) {
				if(avatar.getX() >= 0) {
					avatar.moveXBy(-5);
				}		

			} if(keyS) {
				if(avatar.getY()+avatar.getHeight() <= canvas.getHeight()) {
					avatar.moveYBy(5);
				}

			} if(keyD) {
				if(avatar.getX()+avatar.getWidth() <= canvas.getWidth()) {
					avatar.moveXBy(5);
				}
				
			} if(keySpace) {
				shoot();
				shootSound.play();
			}
		}
	}
	
	public void shoot() {
		int bulletX = avatar.getX() + (int)(avatar.getWidth()/2)-(int)(Bullet.getWidth()/2);
		int bulletY = avatar.getY() - (int)(Bullet.getHeight());
		
		bullets.add(new Bullet(canvas,bulletX,bulletY));
	}
	
	public void invaderShoot() {
		Random random = new Random();
		
		int index = random.nextInt(enemies.size());
		
		enemies.get(index).shoot();
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
		} else if(avatar == null) {
			return true;
		} else if(enemies.get(0).getY() > canvas.getHeight()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static ArrayList<Enemy> getEnemies() {
		return enemies;
	}
	
	public static AudioClip getGameOverSound() {
		return gameOverSound;
	}
	
	public static int getNumEnemies() {
		return ENEMIES;
	}
	
	public static int getScorePerEnemy() {
		return SCORE_PER_ENEMY;
	}
	
	public static int getScore() {
		return score;
	}
	
	public static void stopAutomatas() {
		Enemy.setAlive(false);
	}
}
