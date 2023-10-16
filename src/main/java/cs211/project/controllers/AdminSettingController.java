package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;

import java.io.IOException;

public class AdminSettingController {

    @FXML
    public void handleGoAdminAccountList(){
        try {
            FXRouter.goTo("admin-account-list",FXRouter.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleGoAdminSetting(){
        try {
            FXRouter.goTo("admin-setting", FXRouter.getData());
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

    public void handleGoAdminChangePassword(){
        try {
            FXRouter.goTo("admin-change-password", FXRouter.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
