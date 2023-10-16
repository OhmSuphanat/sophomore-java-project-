package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.awt.*;

public class WelcomeController {

    @FXML
    private TextField usernameTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Datasource<UserList> userListDatasource;
    private Datasource<AdminList> adminListDatasource;
    private UserList userList;
    private AdminList adminList;
    private AccountList accountList;

    @FXML
    public void initialize() {
        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        adminListDatasource = new AdminListFileDatasource("data", "admin-list.csv");
        accountList = new AccountList();
        readData();
        clearLabel();
        usernameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signIn();
            }
        });
        passwordPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signIn();
            }
        });
    }

    private void readData() {
        userList = userListDatasource.readData();
        adminList = adminListDatasource.readData();
        accountList.addNewAccounts(userList);
        accountList.addNewAccounts(adminList);
    }

    public void signIn() {
        clearLabel();
        String username = usernameTextField.getText().trim();
        String password = passwordPasswordField.getText().trim();
        Account exist = accountList.findAccountByUsername(username);
        if (exist != null && exist.getUsername().equals(username) && exist.getPassword().equals(password)) {
            if (exist instanceof Admin) {
                handleGoAdminAccountListController(exist);
            } else if (exist instanceof User) {
                User user = (User) exist;
                if (user.isActive()) {
                    user.setLoginDateTime(LocalDateTime.now());
                    userList.sortByLoginTime(new UserLoginTimeComparator());
                    userListDatasource.writeData(userList);
                    handleGoEventList(exist);

                }
                errorLabel.setText("This Account got Banned");
            }
        } else {
            errorLabel.setText("Username or Password is Invalid");
        }

    }


    private void handleGoEventList(Account account) {
        try {
            FXRouter.goTo("event-list", account.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleGoSignUp() {
        try {
            FXRouter.goTo("sign-up");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToCredit() {
        try {
            FXRouter.goTo("credit");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void handleGoAdminAccountListController(Account account) {
        try {
            FXRouter.goTo("admin-account-list", account.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openPDF(){
        try {
            File pdfFile = new File("handbook.pdf"); // Replace with the actual path to your PDF file
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                // Handle the case where Desktop is not supported
                System.err.println("Desktop not supported, unable to open PDF.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the PDF open error
            System.err.println("Error opening PDF: " + e.getMessage());
        }
    }
    private void clearLabel() {
        errorLabel.setText("");
    }


}
