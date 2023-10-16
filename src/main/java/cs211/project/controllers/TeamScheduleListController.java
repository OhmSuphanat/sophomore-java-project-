package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDateTime;

public class TeamScheduleListController {
    @FXML private Button markDoneButton;
    @FXML private TableView teamTableView;
    @FXML private TableView schedulesTableView;
    @FXML private TextField titleTextField;
    @FXML private Label errorTitleLabel;
    @FXML private TextField detailTextField;
    @FXML private Label errorDetailLabel;


    private Datasource<OwnerList> ownerListDatasource;
    private OwnerList ownerList;
    private Owner owner;
    private Datasource<StaffList> staffListDatasource;
    private StaffList staffList;
    private StaffList eventStaffList;
    private Staff thisTeam;
    private Datasource<ScheduleList> scheduleListDatasource;
    private ScheduleList scheduleList;
    private ScheduleList thisTeamScheduleList;
    private Schedule thisSchedule;

    public void initialize() {
        ownerListDatasource = new OwnerListFileDatasource("data", "owner-list.csv");
        ownerList = ownerListDatasource.readData();
        owner = ownerList.findOwnerByEventId((String) FXRouter.getData());

        staffListDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        staffList = staffListDatasource.readData();
        eventStaffList = staffList.findStaffListByEventId(owner.getEventId());

        scheduleListDatasource = new ScheduleListFileDatasource("data", "schedule-list.csv");
        scheduleList = scheduleListDatasource.readData();

        thisTeamScheduleList = new ScheduleList(); // avoid Null

        cleanUp();

        showTeam(eventStaffList.findAllTeam());

        teamTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Staff>() {
            @Override
            public void changed(ObservableValue observable, Staff oldValue, Staff newValue) {
                if (newValue != null) {
                    cleanUp();
                    thisTeam = newValue;
                    thisTeamScheduleList = scheduleList.findSchedulesByTeamId(thisTeam.getTeamId());
                    titleTextField.setEditable(true);
                    detailTextField.setEditable(true);
                    showTeamScheduleList(thisTeamScheduleList);
                }
            }
        });

        showTeamScheduleList(thisTeamScheduleList);

        schedulesTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Schedule>() {
            @Override
            public void changed(ObservableValue observable, Schedule oldValue, Schedule newValue) {
                if (newValue != null) {
                    cleanUp();
                    titleTextField.setEditable(true);
                    detailTextField.setEditable(true);
                    thisSchedule = newValue;
                    titleTextField.setText(thisSchedule.getTitle());
                    detailTextField.setText(thisSchedule.getDetail());
                    markDoneButton.setText(thisSchedule.getStatus().equals("on-going") ? "MARK DONE" : "UNMARK");

                }
            }
        });
    }

    public void createSchedule() {
        String title = titleTextField.getText();
        String detail = detailTextField.getText();
        if (checkText(title,detail)) {
            scheduleList.addNewSchedule(thisTeam.getTeamId(), title, LocalDateTime.now(), LocalDateTime.now(), detail, true);
            scheduleListDatasource.writeData(scheduleList);
            showTeamScheduleList(thisTeamScheduleList);
            handleGoTeamSchedule();
        }
    }

    public void markDone() {
        if (thisSchedule != null) {
            markDoneButton.setText(markDoneButton.getText().equals("MARK DONE") ? "UNMARK" : "MARK DONE");
            thisSchedule.setActive();
            scheduleListDatasource.writeData(scheduleList);
            showTeamScheduleList(thisTeamScheduleList);
        }
    }

    public void removeSchedule() {
        if (thisSchedule != null) {
            scheduleList.remove(thisSchedule);
            scheduleListDatasource.writeData(scheduleList);
            handleGoTeamSchedule();
        }

    }

    private boolean checkText(String title, String detail) {
        errorTitleLabel.setText(Validation.isAllowName(title));
        errorDetailLabel.setText(Validation.isDetail(detail));
        if (errorTitleLabel.getText().equals("") && errorDetailLabel.getText().equals("")) {
            return true;
        }
        return false;
    }

    private void showTeam(StaffList staffList) {
        Label customPlaceholderTeam = new Label("This event have no team yet.");
        customPlaceholderTeam.setStyle("-fx-font-size: 12px;");
        teamTableView.setPlaceholder(customPlaceholderTeam);

        TableColumn<Staff, String> teamNameColumn = new TableColumn<>("TEAM");
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        teamNameColumn.setPrefWidth(197);

        teamTableView.getColumns().clear();
        teamTableView.getColumns().add(teamNameColumn);



        teamTableView.getItems().clear();
        if (staffList != null) {
            for (Staff staff: staffList.getStaffs()) {
                teamTableView.getItems().add(staff);
            }
        }
    }

    private void showTeamScheduleList(ScheduleList scheduleList) {
        Label customPlaceholderStaffs = new Label("This team have no schedule yet.");
        customPlaceholderStaffs.setStyle("-fx-font-size: 18px;");
        schedulesTableView.setPlaceholder(customPlaceholderStaffs);

        TableColumn<Schedule, String> titleColumn = new TableColumn<>("TITLE");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(200);

        TableColumn<Schedule, String> detailColumn = new TableColumn<>("DETAIL");
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));
        detailColumn.setPrefWidth(300);

        TableColumn<Schedule, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-alignment: CENTER;");
        statusColumn.setPrefWidth(73);


        schedulesTableView.getColumns().clear();
        schedulesTableView.getColumns().add(titleColumn);
        schedulesTableView.getColumns().add(detailColumn);
        schedulesTableView.getColumns().add(statusColumn);



        schedulesTableView.getItems().clear();
        if (scheduleList != null) {
            for (Schedule schedule: scheduleList.getSchedules()) {
                schedulesTableView.getItems().add(schedule);
            }
        }
    }

    private void cleanUp() {
        errorTitleLabel.setText("");
        errorDetailLabel.setText("");
        titleTextField.setEditable(false);
        detailTextField.setEditable(false);
    }

    @FXML
    public void handleGoTeamChat(){
        try {
            FXRouter.goTo("owner-team-chat", owner.getEventId());
        } catch (IOException e) {
            throw new RuntimeException(e);
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
}
