package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class CreditController {
    @FXML private Label kongLabel;
    @FXML private Label aofLabel;
    @FXML private Label ohmLabel;
    @FXML private Label pangLabel;
    @FXML private Rectangle kongRectangle;
    @FXML private Rectangle aofRectangle;
    @FXML private Rectangle ohmRectangle;
    @FXML private Rectangle pangRectangle;

    @FXML public void initialize(){
        kongRectangle.setFill(new ImagePattern(new Image(getClass().getResource("/images/kong.png").toString())));
        kongLabel.setText("Natdanai Eaksunti (Kong)\n          6510450330");
        aofRectangle.setFill(new ImagePattern(new Image(getClass().getResource("/images/aof.png").toString())));
        aofLabel.setText("Podjanin Wachirawitttayakul (Aof)\n                   6510450691");
        ohmRectangle.setFill(new ImagePattern(new Image(getClass().getResource("/images/ohm.png").toString())));
        ohmLabel.setText("Suphanat Sroyphet (Ohm)\n          6510450976");
        pangLabel.setText("Sarika Opastpitchayadamrong (Pang)\n                       6510450984");
        pangRectangle.setFill(new ImagePattern(new Image(getClass().getResource("/images/pang.png").toString())));
    }

    @FXML public void backToWelcome() {
        try {
            FXRouter.goTo("welcome");
        } catch (IOException e) {
            System.out.println("can't go welcome");
            throw new RuntimeException(e);
        }
    }

}
