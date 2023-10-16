package cs211.project.controllers;

import cs211.project.models.Admin;
import cs211.project.models.AdminList;
import cs211.project.models.User;
import cs211.project.models.UserList;
import cs211.project.services.AdminListFileDatasource;
import cs211.project.services.Datasource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDatasource;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class AdminAccountListController {

    @FXML private Button banButton;
    @FXML private Label hintLabel;
    @FXML private Label loginDateTimeLabel;
    @FXML private TableView usersTableView;
    @FXML private Circle profileImageCircle;

    @FXML private Label usernameLabel;
    @FXML private Label displayNameLabel;
    private Datasource<AdminList> adminListDatasource;
    private AdminList adminList;

    private UserList userList;
    private User chosenOne;
    private Datasource<UserList> userListDatasource;

    @FXML
    public void initialize() {
        adminListDatasource = new AdminListFileDatasource("data","admin-list.csv");
        userListDatasource = new UserListFileDatasource("data","user-list.csv");
        readData();
        cleanUp();
        showTable(userList);
    }

    public void handleBan(){
        Admin admin = adminList.findAdminById((String)FXRouter.getData());
        if (chosenOne.isActive()){
            admin.deactivate(chosenOne);
            banButton.setText("UNBAN");
        }else{
            admin.activate(chosenOne);
            banButton.setText("BAN");
        }
        banButton.setDisable(true);
        userListDatasource.writeData(userList);
        readData();
        showTable(userList);
    }

    private void readData() {
        adminList = adminListDatasource.readData();
        userList = userListDatasource.readData();
    }

    private void cleanUp(){
        usernameLabel.setText("");
        displayNameLabel.setText("");
        loginDateTimeLabel.setText("");
        hintLabel.setText("PLEASE SELECT AN USER");
        banButton.setDisable(true);
        profileImageCircle.setVisible(false);
    }
    private void showTable(UserList userList) {

        TableColumn<User, String> latestLoginColumn = new TableColumn<>("Latest Login");
        latestLoginColumn.setCellValueFactory(new PropertyValueFactory<>("loginDateTimeString"));
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User, String> activeColumn = new TableColumn<>("Status");
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
        TableColumn<User, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        usersTableView.getColumns().clear();
        usersTableView.getColumns().add(idColumn);
        usersTableView.getColumns().add(latestLoginColumn);
        usersTableView.getColumns().add(usernameColumn);
        usersTableView.getColumns().add(activeColumn);

        usersTableView.getItems().clear();

        for (User user: userList.getUsers()) {
            usersTableView.getItems().add(user);
        }

        usersTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue observable, User oldValue, User newValue) {
                if (newValue != null) {
                    hintLabel.setText("");
                    banButton.setDisable(false);
                    banButton.setText(newValue.isActive() ? "BAN" : "UNBAN");
                    chosenOne = newValue;
                    usernameLabel.setText(newValue.getUsername());
                    displayNameLabel.setText(newValue.getDisplayName());
                    loginDateTimeLabel.setText(newValue.getLoginDateTimeString());
                    profileImageCircle.setVisible(true);
                    profileImageCircle.setFill(new ImagePattern(new Image("file:"+newValue.getImagePath())));

                }
            }
        });
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
