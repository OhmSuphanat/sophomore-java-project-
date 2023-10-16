package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SignUpController {
    @FXML private PasswordField passwordPasswordField;
    @FXML private PasswordField confirmPasswordPasswordField;
    @FXML private Circle profileImageCircle;
    @FXML private Label usernameErrorLabel;
    @FXML private Label displayNameErrorLabel;
    @FXML private Label passwordErrorLabel;
    @FXML private Label validatedErrorLabel;
    @FXML private TextField usernameTextField;
    @FXML private TextField displayNameTextField;

    private Datasource<UserList> userListDatasource;
    private Datasource<AdminList> adminListDatasource;
    private UserList userList;
    private AdminList adminList;
    private AccountList accountList;
    private String imagePath;

    @FXML
    public void initialize() {
        userListDatasource = new UserListFileDatasource("data","user-list.csv");
        adminListDatasource = new AdminListFileDatasource("data", "admin-list.csv");
        accountList = new AccountList();
        readData();
        clearLabel();
        imagePath = "images/default-profile-image.jpg";
        profileImageCircle.setFill(new ImagePattern(new Image("file:"+imagePath)));
        usernameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createAccount();
            }
        });
        displayNameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createAccount();
            }
        });
        passwordPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createAccount();
            }
        });
        confirmPasswordPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createAccount();
            }
        });
    }

    private void readData(){
        userList = userListDatasource.readData();
        adminList = adminListDatasource.readData();
        accountList.addNewAccounts(userList);
        accountList.addNewAccounts(adminList);
    }

    public void createAccount(){
        String username = usernameTextField.getText().trim();
        String displayName = displayNameTextField.getText().trim();
        String password = passwordPasswordField.getText().trim();
        String validatedPassword = confirmPasswordPasswordField.getText().trim();
        if (checkText(username, displayName, password, validatedPassword)) {
            userList.addNewUser(LocalDateTime.now().toString(),username,password,displayName, LocalDateTime.now(),imagePath,true);
            userListDatasource.writeData(userList);
            handleGoWelcome();
        }

    }

    private void clearLabel() {
        usernameErrorLabel.setText("");
        displayNameErrorLabel.setText("");
        passwordErrorLabel.setText("");
        validatedErrorLabel.setText("");
    }
    private boolean checkText(String username, String displayName, String password, String validatedPassword) {
        clearLabel();
        Account exist = accountList.findAccountByUsername(username);
        usernameErrorLabel.setText(Validation.isValidUsername(username));
        if (exist != null) {
            usernameErrorLabel.setText("Username's already exist");
        }
        displayNameErrorLabel.setText(Validation.isValidDisplayName(displayName));
        passwordErrorLabel.setText(Validation.isValidPassword(password));
        validatedErrorLabel.setText(Validation.isMatched(password,validatedPassword));
        if (exist == null && Validation.isValidUsername(username).equals("") && Validation.isValidDisplayName(displayName).equals("") && Validation.isValidPassword(password).equals("") && Validation.isMatched(password,validatedPassword).equals("")) {
            return true;
        }
        return false;
    }

    @FXML public void handleGoWelcome() {
        try {
            FXRouter.goTo("welcome");
        } catch (IOException e) {
            System.out.println("can't go welcome");
            throw new RuntimeException(e);
        }
    }

    @FXML public void handleUploadButton(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        // SET FILECHOOSER INITIAL DIRECTORY
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        // DEFINE ACCEPTABLE FILE EXTENSION
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        // GET FILE FROM FILECHOOSER WITH JAVAFX COMPONENT WINDOW
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                // CREATE FOLDER IF NOT EXIST
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                // RENAME FILE
                String[] fileSplit = file.getName().split("\\.");
                String filename = LocalDate.now() + "_"+System.currentTimeMillis() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                // COPY WITH FLAG REPLACE FILE IF FILE IS EXIST
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                // SET NEW FILE PATH TO IMAGE
                profileImageCircle.setFill(new ImagePattern(new Image(target.toUri().toString())));
                imagePath = destDir + "/" + filename;
                profileImageCircle.setFill(new ImagePattern(new Image("file:"+imagePath)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}
