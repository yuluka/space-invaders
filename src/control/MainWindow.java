package control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import screen.ScreenA;

public class MainWindow implements Initializable{

    @FXML
    private Canvas CANVAS;
    @SuppressWarnings("unused")
	private GraphicsContext gc;
    
    private ScreenA screen;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		screen = new ScreenA(CANVAS);
		
		gc = CANVAS.getGraphicsContext2D();
		CANVAS.setFocusTraversable(true);
		
		new Thread(() -> {
			while (true) {
				isEndGame();
				
				Platform.runLater(() -> {
					paint();
					paintStars();
				});
				
				pause(50);
			}
		}).start();
		
		initEvents();
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

	private void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void finishGame() {
		System.out.println("Aiós");
		System.exit(0);
	}
}
