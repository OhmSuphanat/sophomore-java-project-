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

public class EventSettingController {


    @FXML private TextField startTimeTextField;
    @FXML private DatePicker startDatePicker;
    @FXML private TextField eventNameTextField;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField endTimeTextField;
    @FXML private TextField detailTextField;
    @FXML private Circle eventImageView;
    @FXML private Label errorEventName;
    @FXML private Label errorStartDate;
    @FXML private Label errorEndDate;
    @FXML private Label errorStartTime;
    @FXML private Label errorEndTime;
    @FXML private Label errorDetail;
    private Datasource<EventList> eventListDatasource;
    private Datasource<OwnerList> ownerListDatasource;
    private OwnerList ownerList;
    private Owner owner;
    private Event thisEvent;
    private EventList eventList;
    private String imagePath;
    public void initialize() {
        cleanUp();
        eventListDatasource = new EventListDatasource("data", "event-list.csv");
        eventList = eventListDatasource.readData();
        ownerListDatasource = new OwnerListFileDatasource("data", "owner-list.csv");
        ownerList = ownerListDatasource.readData();
        owner = ownerList.findOwnerByEventId((String) FXRouter.getData());
        thisEvent = eventList.findEventById((String) FXRouter.getData());
        imagePath = thisEvent.getImagePath();
        eventImageView.setFill(new ImagePattern(new Image("file:"+imagePath)));
        eventNameTextField.setText(thisEvent.getEventName());
        startDatePicker.setValue(LocalDate.parse(thisEvent.getStringStartDate()));
        endDatePicker.setValue(LocalDate.parse(thisEvent.getStringEndDate()));
        detailTextField.setText(thisEvent.getDetail());
        startTimeTextField.setText(thisEvent.getStringStartTime());
        endTimeTextField.setText(thisEvent.getStringFinishTime());

        startTimeTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                editEvent();
            }
        });
        eventNameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                editEvent();
            }
            if (event.getCode() == KeyCode.TAB){
                handleGoEventSetting();
            }
        });
        endTimeTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                editEvent();
            }
        });
        detailTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                editEvent();
            }
        });
        startDatePicker.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                editEvent();
            }
        });
        endDatePicker.setOnKeyPressed(event ->{
            if (event.getCode() == KeyCode.ENTER) {
                editEvent();
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
    }

    public boolean checkText(String eventName, String startTime, String endTime, String startDate, String endDate, String detail){
        errorEventName.setText(Validation.isAllowName(eventName));
        errorStartTime.setText(Validation.isTime(startTime));
        errorEndTime.setText(Validation.isTime(endTime));
        errorStartDate.setText(Validation.isDate(startDate));
        errorEndDate.setText(Validation.isDate(endDate));
        errorDetail.setText(Validation.isDetail(detail));

        checkDate();
        checkTime(startTime, endTime);
        if (errorEventName.getText().equals("") && errorStartTime.getText().equals("") &&
                errorEndTime.getText().equals("") && errorStartDate.getText().equals("") &&
                errorEndDate.getText().equals("") && errorDetail.getText().equals("") &&
                !startDatePicker.getValue().isAfter(endDatePicker.getValue()) ){
            return true;
        }
        return false;
    }
    public void editEvent(){
        String eventName = eventNameTextField.getText().trim();
        String startTime = startTimeTextField.getText();
        String endTime = endTimeTextField.getText();
        String startDate = "";
        String endDate = "";
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
        if (checkText(eventName, startTime, endTime, startDate, endDate, detail)) {
            String startDateTime = startDate + "T" + startTime;
            String endDateTime = endDate + "T" + endTime;
            thisEvent.setEventName(eventName);
            thisEvent.setStartDateTime(LocalDateTime.parse(startDateTime));
            thisEvent.setEndDateTime(LocalDateTime.parse(endDateTime));
            thisEvent.setDetail(detail);
            thisEvent.setImagePath(imagePath);
            eventListDatasource.writeData(eventList);
            handleGoEventList();
        }
    }

    public void checkDate(){
        if (startDatePicker.getValue() != null && endDatePicker.getValue() != null && startDatePicker.getValue().isAfter(endDatePicker.getValue())){
            errorStartDate.setText("The date must be chronological");
            errorEndDate.setText("The date must be chronological");
        }
    }
    public void checkTime(String startTime, String endTime){
        if (startDatePicker.getValue() != null && endDatePicker.getValue() != null && startDatePicker.getValue().isEqual(endDatePicker.getValue()) && !startTime.equals("") && !endTime.equals("")){
            if (LocalTime.parse(startTime).isAfter(LocalTime.parse(endTime))) {
                errorStartTime.setText("The time must be chronological");
                errorEndTime.setText("The time must be chronological");
            }
        }
    }



    @FXML
    public void handleGoEventList(){
        try {
            FXRouter.goTo("event-list", owner.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleGoEventCreator(){
        try {
            FXRouter.goTo("event-creator", owner.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGoScheduleList() {
        try {
            FXRouter.goTo("schedule-list", owner.getEventId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGoAccountHub(){
        try {
            FXRouter.goTo("account-hub", owner.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGoEventSetting(){
        try{
            FXRouter.goTo("event-setting", owner.getEventId());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void handleGoTeamChat(){
        try{
            FXRouter.goTo("owner-team-chat", owner.getEventId());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void handleGoStaffListController() {
        try {
            FXRouter.goTo("staff-list", owner.getEventId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGoTeamSchedule() {
        try {
            FXRouter.goTo("team-schedule-list", owner.getEventId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGoParticipantList(){
        try{
            FXRouter.goTo("participant-list", owner.getEventId());
        }catch (IOException e){
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
                System.out.println(imagePath);
                eventImageView.setFill(new ImagePattern(new Image("file:"+imagePath)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
