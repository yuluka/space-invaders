package control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import screen.ScreenA;

public class GameOverWindow implements Initializable {
    @FXML
    private ImageView BTTN_RETRY;
    
    @FXML
    private ImageView IMG_FINAL_RESULT;
    
    @FXML
    private Label LBL_SCORE;

    @FXML
    void retryGame(MouseEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/MainWindow.fxml"));
		Parent root = loader.load();
		Scene sc = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(sc);
		stage.show();
		
		Stage stage2 = (Stage) BTTN_RETRY.getScene().getWindow();
		stage2.close();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ScreenA.getGameOverSound().play();
		
		if(ScreenA.getScore() == (ScreenA.getNumEnemies()*ScreenA.getScorePerEnemy())) {
			IMG_FINAL_RESULT.setImage(new Image("/images/You win.png"));
		}
		
		LBL_SCORE.setText(ScreenA.getScore() + "");
	}
}
