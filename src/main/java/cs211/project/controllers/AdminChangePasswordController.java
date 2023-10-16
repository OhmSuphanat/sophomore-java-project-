package cs211.project.controllers;

import cs211.project.models.Admin;
import cs211.project.models.AdminList;
import cs211.project.services.AdminListFileDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.Validation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;

import java.io.IOException;

public class AdminChangePasswordController {

    @FXML private PasswordField newPasswordPasswordField;
    @FXML private PasswordField comfirmNewPasswordPasswordField;
    @FXML private PasswordField oldPasswordPasswordField;
    @FXML private Label oldPasswordErrorLabel;
    @FXML private Label newPasswordErrorLabel;
    @FXML private Label confirmNewPasswordErrorLabel;

    private AdminList adminList;
    private Datasource<AdminList> datasource;
    private Admin admin;
    public void initialize() {
        clearLabel();
        datasource = new AdminListFileDatasource("data", "admin-list.csv");
        adminList = datasource.readData();
        admin = adminList.findAdminById((String) FXRouter.getData());
        newPasswordPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                changePassword();
            }
        });
        oldPasswordPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                changePassword();
            }
        });
        comfirmNewPasswordPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                changePassword();
            }
        });
    }

    public void changePassword() {
        String oldPassword = oldPasswordPasswordField.getText();
        String newPassword = newPasswordPasswordField.getText();
        String validatedNewPassword = comfirmNewPasswordPasswordField.getText();
        if (checkText(oldPassword,newPassword,validatedNewPassword)) {
            admin.setPassword(newPassword);
            datasource.writeData(adminList);
            handleGoAdminSetting();
        }


    }

    private boolean checkText(String oldPassword, String newPassword, String validatedNewPassword) {
        clearLabel();
        oldPasswordErrorLabel.setText(Validation.isMatched(oldPassword,admin.getPassword()));
        newPasswordErrorLabel.setText(Validation.isValidPassword(newPassword));
        confirmNewPasswordErrorLabel.setText(Validation.isMatched(newPassword,validatedNewPassword));
        if (oldPasswordErrorLabel.getText().equals("") && newPasswordErrorLabel.getText().equals("") && confirmNewPasswordErrorLabel.getText().equals("")) {
            return true;
        }
        return false;
    }

    private void clearLabel() {
        oldPasswordErrorLabel.setText("");
        newPasswordErrorLabel.setText("");
        confirmNewPasswordErrorLabel.setText("");
    }
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
            FXRouter.goTo("admin-setting",FXRouter.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
