package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.IOException;

public class AccountHubController {
    @FXML
    public void handleGoChangePassword(){
        try {
            FXRouter.goTo("change-password", FXRouter.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleGoWelcome(){
        try {
            FXRouter.goTo("welcome");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void handleGoEventList(){
        try {
            FXRouter.goTo("event-list", FXRouter.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleGoEventCreator(){
        try {
            FXRouter.goTo("event-creator", FXRouter.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleGoAccountHub(){
        try {
            FXRouter.goTo("account-hub", FXRouter.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleGoProfile(){
        try {
            FXRouter.goTo("profile", FXRouter.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
