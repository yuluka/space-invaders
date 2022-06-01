package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import screen.ScreenA;

public class MainWindow implements Initializable{

    @FXML
    private Canvas CANVAS;
    
    @FXML
    private Label LBL_SCORE;
    
    @SuppressWarnings("unused")
	private GraphicsContext gc;
    
    private ScreenA screen;
    private boolean isAlive = true;
    private double crono;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		screen = new ScreenA(CANVAS);
		
		gc = CANVAS.getGraphicsContext2D();
		CANVAS.setFocusTraversable(true);
		
		new Thread(() -> {
			while (isAlive) {
				isEndGame();
				
				Platform.runLater(() -> {
					paint();
					paintStars();
					LBL_SCORE.setText(ScreenA.getScore() + "");
				});
				
				pause(50);
			}
		}).start();
		
		initEvents();
		enemyShoot();
	}
	
	private void paint() {
		screen.paint();
	}
	
	private void paintStars() {
		screen.paintStars();
	}
	
	private void isEndGame() {
		if(screen.isEndGame()) {
			finishGame();
		}
	}

	public void initEvents() {	
		CANVAS.setOnKeyPressed(e -> {
			screen.onKeyPressed(e);
		});
		
		CANVAS.setOnKeyReleased(e -> {
			screen.onKeyReleased(e);
		});
	}
	
	public void enemyShoot() {
		new Thread(() -> {
			while(isAlive) {
				screen.invaderShoot();
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void finishGame() {
		crono = 0;
		
		new Thread(() -> {			
			while(crono < 0.2) {
				crono += 0.1;
				
				if(crono == 0.2) {
					System.out.println("Aiós");
					isAlive = false;
					
					Platform.runLater(() -> {
						goToGameOver();
					});
				}
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();		
	}
	
	private void goToGameOver() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/GameOverWindow.fxml"));
			Parent root = loader.load();
			
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
			
			Stage stage2 = (Stage) CANVAS.getScene().getWindow();
			stage2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
