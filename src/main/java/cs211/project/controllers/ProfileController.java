package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class ProfileController {
    @FXML private Label totalJoinLabel;
    @FXML private Label totalDoneLabel;
    @FXML
    Label usernameLabel;
    @FXML
    Label displayNameLabel;

    @FXML private Label totalOwnerEventLabel;
    @FXML private Circle imageCircle;
    User user;
    UserList userList;

    private Datasource<UserList> userListDatasource;
    private Datasource<OwnerList> ownerListDatasource;
    private Datasource<ParticipantList> participantListDatasource;
    private Datasource<StaffList> staffListDatasource;
    private Datasource<EventList> eventListDatasource;
    private EventList eventList;
    private ParticipantList participantList;
    private StaffList staffList;
    private OwnerList ownerList;
    private String imagePath;


    @FXML
    public void initialize(){
        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        userList = userListDatasource.readData();
        user = userList.findUserById((String) FXRouter.getData());

        ownerListDatasource = new OwnerListFileDatasource("data", "owner-list.csv");
        ownerList = ownerListDatasource.readData();

        eventListDatasource = new EventListDatasource("data", "event-list.csv");
        eventList = eventListDatasource.readData();

        staffListDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        staffList = staffListDatasource.readData();

        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        participantList = participantListDatasource.readData();
        eventList.setStatus(participantList);
        clearLabel();
        usernameLabel.setText(user.getUsername());
        displayNameLabel.setText(user.getDisplayName());
        imageCircle.setFill(new ImagePattern(new Image("file:" + user.getImagePath())));
        showTotalLabel();
    }

    private void showTotalLabel() {
        OwnerList userOwnerList =  ownerList.findEventsByOwnerId(user.getId());
        totalOwnerEventLabel.setText(String.valueOf(userOwnerList.getOwners().size()));

        StaffList userStaffList = staffList.findStaffListByStaffId(user.getUsername());
        ParticipantList userParticipantList = participantList.findParticipantByUserId(user.getUsername());
        EventList joinerEventList = eventList.findEventListByJoiningUser(userParticipantList, userStaffList).findEventsNotDisable();
        totalJoinLabel.setText(String.valueOf(joinerEventList.getEventList().size()));

        EventList disableEventList = eventList.findEventListByJoiningUser(userParticipantList,userStaffList).findDisableEventList();
        totalDoneLabel.setText(String.valueOf(disableEventList.getEventList().size()));
    }


    private void clearLabel() {
        usernameLabel.setText("");
        displayNameLabel.setText("");
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
                imageCircle.setFill(new ImagePattern(new Image(target.toUri().toString())));
                imagePath = destDir + "/" + filename;
                imageCircle.setFill(new ImagePattern(new Image("file:"+imagePath)));
                user.setImagePath(imagePath);
                userListDatasource.writeData(userList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showScheduledEventListPopUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs211/project/views/scheduled-event-list.fxml"));
        Parent root = loader.load();

        ScheduledEventListController scheduledEventListController = loader.getController();
        scheduledEventListController.setUserId(user.getId());

        Stage popUpStage = new Stage();
        popUpStage.initStyle(StageStyle.UNDECORATED);
        popUpStage.setResizable(false);
        popUpStage.setScene(new Scene(root,630,465));
        popUpStage.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                // Pop-up lost focus, close it
                popUpStage.close();
            }
        });
        popUpStage.show();
    }

    public void showYourEventListPopUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs211/project/views/your-event-view.fxml"));
        Parent root = loader.load();

        YourEventListController yourEventListController = loader.getController();
        yourEventListController.setUserId(user.getId());

        Stage popUpStage = new Stage();
        popUpStage.initStyle(StageStyle.UNDECORATED);
        popUpStage.setResizable(false);
        popUpStage.setScene(new Scene(root,630,465));
        popUpStage.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                // Pop-up lost focus, close it
                popUpStage.close();
            }
        });
        popUpStage.show();
    }

    public void showDisableEventList() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs211/project/views/complete-event-list-view.fxml"));
        Parent root = loader.load();

        CompleteEventListController completeEventListController = loader.getController();
        completeEventListController.setUserId(user.getId());

        Stage popUpStage = new Stage();
        popUpStage.initStyle(StageStyle.UNDECORATED);
        popUpStage.setResizable(false);
        popUpStage.setScene(new Scene(root,630,465));
        popUpStage.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                // Pop-up lost focus, close it
                popUpStage.close();
            }
        });
        popUpStage.show();
    }
}
