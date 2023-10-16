package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
import java.time.LocalTime;

public class EventCreatorController {


    @FXML private TextField startTimeTextField;
    @FXML private DatePicker startDatePicker;
    @FXML private TextField eventNameTextField;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField endTimeTextField;
    @FXML private DatePicker joinableStartDatePicker;
    @FXML private DatePicker joinableEndDatePicker;
    @FXML private TextField joinableStartTimeTextField;
    @FXML private TextField joinableEndTimeTextField;
    @FXML private TextField detailTextField;
    @FXML private TextField maxParticipantsTextField;
    @FXML private Circle eventImageView;
    @FXML private Label errorEventName;
    @FXML private Label errorStartDate;
    @FXML private Label errorEndDate;
    @FXML private Label errorStartTime;
    @FXML private Label errorEndTime;
    @FXML private Label errorJoinAbleStartDate;
    @FXML private Label errorJoinAbleEndDate;
    @FXML private Label errorJoinAbleStartTime;

    @FXML private Label errorJoinAbleEndTime;
    @FXML private Label errorParticipants;
    @FXML private Label errorDetail;



    private User user;
    private UserList userList;
    private ParticipantList participantList;
    private Datasource<ParticipantList> participantListDatasource;
    private Datasource<UserList> userListDatasource;
    private Datasource<EventList> eventListDatasource;
    private Datasource<OwnerList> ownerListDatasource;
    private OwnerList ownerList;
    private EventList eventList;
    private String imagePath;
    public void initialize() {
        cleanUp();
        imagePath = "images/default-event-image.png";
        eventImageView.setFill(new ImagePattern(new Image("file:"+imagePath)));
        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        userList = userListDatasource.readData();
        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        participantList = participantListDatasource.readData();
        user = userList.findUserById((String) FXRouter.getData());
        eventListDatasource = new EventListDatasource("data", "event-list.csv");
        eventList = eventListDatasource.readData();
        ownerListDatasource = new OwnerListFileDatasource("data", "owner-list.csv");
        ownerList = ownerListDatasource.readData();
        startTimeTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createEvent();
            }
        });
        eventNameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createEvent();
            }
        });
        endTimeTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createEvent();
            }
        });
        detailTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createEvent();
            }
        });
        startDatePicker.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createEvent();
            }
        });
        endDatePicker.setOnKeyPressed(event ->{
            if (event.getCode() == KeyCode.ENTER) {
                createEvent();
            }
        });
    }



    public void cleanUp(){
        startDatePicker.setEditable(false);
        endDatePicker.setEditable(false);
        errorEventName.setText("");
        errorStartDate.setText("");
        errorEndDate.setText("");
        errorStartTime.setText("");
        errorEndTime.setText("");
        errorDetail.setText("");
        joinableStartDatePicker.setEditable(false);
        joinableEndDatePicker.setEditable(false);
        errorJoinAbleStartDate.setText("");
        errorJoinAbleStartTime.setText("");
        errorJoinAbleEndDate.setText("");
        errorJoinAbleEndTime.setText("");
        errorParticipants.setText("");
    }

    public boolean checkText(String eventName, String startTime, String endTime, String startDate, String endDate, String joinAbleStartTime, String joinAbleEndTime,String joinAbleStartDate, String joinAbleEndDate, String detail, String maxParticipants){
        errorEventName.setText(Validation.isAllowName(eventName));
        errorStartTime.setText(Validation.isTime(startTime));
        errorEndTime.setText(Validation.isTime(endTime));
        errorStartDate.setText(Validation.isDate(startDate));
        errorEndDate.setText(Validation.isDate(endDate));
        errorJoinAbleStartTime.setText(Validation.isTime(joinAbleStartTime));
        errorJoinAbleEndTime.setText(Validation.isTime(joinAbleEndTime));
        errorJoinAbleStartDate.setText(Validation.isDate(joinAbleStartDate));
        errorJoinAbleEndDate.setText(Validation.isDate(joinAbleEndDate));
        errorParticipants.setText(Validation.isAllowAmount(maxParticipants));
        errorDetail.setText(Validation.isDetail(detail));

        joinAble(startTime,joinAbleEndTime);
        checkDate();
        checkTime(startTime, endTime, joinAbleStartTime, joinAbleEndTime);
        if (errorEventName.getText().equals("") && errorStartTime.getText().equals("") &&
                errorEndTime.getText().equals("") && errorStartDate.getText().equals("") &&
                errorEndDate.getText().equals("") && errorDetail.getText().equals("") &&
                !startDatePicker.getValue().isAfter(endDatePicker.getValue()) &&
                errorJoinAbleStartTime.getText().equals("") && errorJoinAbleEndTime.getText().equals("") &&
                !joinableStartDatePicker.getValue().isAfter(joinableEndDatePicker.getValue()) &&
                errorJoinAbleStartDate.getText().equals("") && errorJoinAbleEndDate.getText().equals("") &&
                !joinAbleStartTime.equals("") && !joinAbleEndTime.equals("") &&
                !startTime.equals("") && !endTime.equals("") && errorParticipants.getText().equals("")){
            return true;
        }
        return false;
    }
    public void createEvent(){
        String eventName = eventNameTextField.getText().trim();
        String startTime = startTimeTextField.getText();
        String endTime = endTimeTextField.getText();
        String joinableStartTime = joinableStartTimeTextField.getText();
        String joinableEndTime = joinableEndTimeTextField.getText();
        String startDate = "";
        String endDate = "";
        String joinableStartDate = "";
        String joinableEndDate = "";
        String maxParticipants = maxParticipantsTextField.getText();
        //Joinable
        if (joinableStartDatePicker.getValue() == null) {
            errorJoinAbleStartDate.setText("Invalid Date");
        }
        else{
            joinableStartDate = joinableStartDatePicker.getValue().toString();
        }
        if (joinableEndDatePicker.getValue() == null){
            errorJoinAbleEndDate.setText("Invalid Date");
        }
        else{
            joinableEndDate = joinableEndDatePicker.getValue().toString();
        }
        //Event-Duration
        if (startDatePicker.getValue() == null) {
            errorStartDate.setText("Invalid Date");
        }
        else{
            startDate = startDatePicker.getValue().toString();
        }
        if (endDatePicker.getValue() == null){
            errorStartDate.setText("Invalid Date");
        }
        else{
            endDate = endDatePicker.getValue().toString();
        }
        String detail = detailTextField.getText().trim();
        if (checkText(eventName, startTime, endTime, startDate, endDate, joinableStartTime, joinableEndTime, joinableStartDate, joinableEndDate, detail, maxParticipants)) {
            String startDateTime = startDate + "T" + startTime;
            String endDateTime = endDate + "T" + endTime;
            String joinAbleStartDateTime = joinableStartDate + "T" + joinableStartTime;
            String joinAbleEndDateTime = joinableEndDate + "T" + joinableEndTime;
            String eventId = LocalDateTime.now().toString();
            eventList.addNewEvent(eventId, eventName, LocalDateTime.parse(startDateTime), LocalDateTime.parse(endDateTime), user.getUsername(), "preparing", imagePath, detail);
            ownerList.addNewOwnerEvent(user.getId(), eventId);
            participantList.bookNewParticipants(eventId, Integer.parseInt(maxParticipants), LocalDateTime.parse(joinAbleStartDateTime), LocalDateTime.parse(joinAbleEndDateTime));
            participantListDatasource.writeData(participantList);
            eventListDatasource.writeData(eventList);
            ownerListDatasource.writeData(ownerList);
            handleGoEventList();
        }
    }

    public void checkDate(){
        if (startDatePicker.getValue() != null && endDatePicker.getValue() != null && startDatePicker.getValue().isAfter(endDatePicker.getValue())){
            errorStartDate.setText("The date must be chronological");
            errorEndDate.setText("The date must be chronological");
        }
        if (joinableStartDatePicker.getValue() != null && joinableEndDatePicker.getValue() != null && joinableStartDatePicker.getValue().isAfter(joinableEndDatePicker.getValue())){
            errorJoinAbleStartDate.setText("The date must be chronological");
            errorJoinAbleEndDate.setText("The date must be chronological");
        }
    }
    public void checkTime(String startTime, String endTime, String joinableStartTime, String joinableEndTime){
        if (startDatePicker.getValue() != null && endDatePicker.getValue() != null && startDatePicker.getValue().isEqual(endDatePicker.getValue()) && !startTime.equals("") && !endTime.equals("")){
            if (LocalTime.parse(startTime).isAfter(LocalTime.parse(endTime))) {
                errorStartTime.setText("The time must be chronological");
                errorEndTime.setText("The time must be chronological");
            }
        }
        if (joinableStartDatePicker.getValue() != null && joinableEndDatePicker.getValue() != null && joinableStartDatePicker.getValue().isEqual(joinableEndDatePicker.getValue()) && !joinableStartTime.equals("") && !joinableEndTime.equals("")){
            if (LocalTime.parse(joinableStartTime).isAfter(LocalTime.parse(joinableEndTime))) {
                errorJoinAbleStartTime.setText("The time must be chronological");
                errorJoinAbleEndTime.setText("The time must be chronological");
            }
        }
    }

    public void joinAble(String startTime, String joinAbleEndTime){
        if (!(joinableEndDatePicker.getValue() != null && joinableStartDatePicker.getValue() != null &&
            startDatePicker.getValue() != null && endDatePicker.getValue() != null && !joinableStartDatePicker.getValue().isBefore(LocalDate.now()) && !joinableEndDatePicker.getValue().isBefore(LocalDate.now()) &&
                (joinableEndDatePicker.getValue().isBefore(startDatePicker.getValue()) || joinableEndDatePicker.getValue().isEqual(startDatePicker.getValue())))){
            errorJoinAbleStartDate.setText("The date must align chronologically with the event.");
            errorJoinAbleEndDate.setText("The date must align chronologically with the event.");
        }
        if ((joinableStartDatePicker.getValue() != null && joinableEndDatePicker.getValue() != null &&
                startDatePicker.getValue() != null && endDatePicker.getValue() != null &&
                joinableEndDatePicker.getValue().isEqual(startDatePicker.getValue()))) {
            if (!startTime.equals("") && !joinAbleEndTime.equals("") && LocalTime.parse(startTime).isBefore(LocalTime.parse(joinAbleEndTime))){
                errorJoinAbleStartTime.setText("Time should not overlap once the event has already started.");
                errorJoinAbleEndTime.setText("Time should not overlap once the event has already started.");
            }
        }
    }
    @FXML
    public void handleGoEventList(){
        try {
            FXRouter.goTo("event-list", user.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleGoEventCreator(){
        try {
            FXRouter.goTo("event-creator", user.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGoAccountHub(){
        try {
            FXRouter.goTo("account-hub", user.getId());
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
        if (file != null) {
            try {
                // CREATE FOLDER IF NOT EXIST
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                // RENAME FILE
                String[] fileSplit = file.getName().split("\\.");
                String filename = LocalDate.now() + "_" + System.currentTimeMillis() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath() + System.getProperty("file.separator") + filename
                );
                // COPY WITH FLAG REPLACE FILE IF FILE IS EXIST
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                // SET NEW FILE PATH TO IMAGE
                eventImageView.setFill(new ImagePattern(new Image(target.toUri().toString())));
                imagePath = destDir + "/" + filename;
                eventImageView.setFill(new ImagePattern(new Image("file:" + imagePath)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
