package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;


import javafx.scene.input.KeyCode;

import java.io.IOException;

public class ChangePasswordController {
    @FXML private PasswordField newPasswordField;
    @FXML PasswordField oldPasswordField;
    @FXML private PasswordField validatedNewPasswordField;
    @FXML private Label oldPasswordErrorLabel;
    @FXML private Label newPasswordErrorLabel;
    @FXML private Label confirmNewPasswordErrorLabel;
    private UserList userList;
    private Datasource<UserList> datasource;
    private User user;
    public void initialize(){
        clearLabel();
        datasource = new UserListFileDatasource("data", "user-list.csv");
        userList = datasource.readData();
        user = userList.findUserById((String) FXRouter.getData());
        newPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleGoAccountHub();
            }
        });
        oldPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleGoAccountHub();
            }
        });
        validatedNewPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleGoAccountHub();
            }
        });

    }

    private void clearLabel() {
        oldPasswordErrorLabel.setText("");
        newPasswordErrorLabel.setText("");
        confirmNewPasswordErrorLabel.setText("");
    }

    public void changePassword(){
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String validatedNewPassword = validatedNewPasswordField.getText();
        if (checkText(oldPassword,newPassword,validatedNewPassword)) {
            user.setPassword(newPassword);
            datasource.writeData(userList);
            handleGoAccountHub();
        }
    }

    private boolean checkText(String oldPassword, String newPassword, String validatedNewPassword) {
        clearLabel();
        oldPasswordErrorLabel.setText(Validation.isMatched(oldPassword,user.getPassword()));
        newPasswordErrorLabel.setText(Validation.isValidPassword(newPassword));
        confirmNewPasswordErrorLabel.setText(Validation.isMatched(newPassword,validatedNewPassword));
        if (oldPasswordErrorLabel.getText().equals("") && newPasswordErrorLabel.getText().equals("") && confirmNewPasswordErrorLabel.getText().equals("")) {
            return true;
        }
        return false;
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

    public void handleGoAccountHub(){
        try {
            FXRouter.goTo("account-hub", FXRouter.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
