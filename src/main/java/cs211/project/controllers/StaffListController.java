package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class StaffListController {

    @FXML private Button promoteButton;
    @FXML private Button banButton;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField startTimeTextField;
    @FXML private TextField endTimeTextField;
    @FXML private Label errorTeamNameLabel;
    @FXML private Label errorAmountLabel;
    @FXML private Label errorDateTimeLabel;
    @FXML private TableView teamsTableView;
    @FXML private TextField teamNameTextField;
    @FXML private TextField amountTextField;
    @FXML private TableView staffsTableView;

    private Datasource<OwnerList> ownerListDatasource;
    private OwnerList ownerList;
    private Owner owner;
    private Datasource<StaffList> staffListDatasource;
    private StaffList staffList;
    private StaffList eventStaffList;
    private StaffList teamList;
    private StaffList chosenTeamStaffList;
    private Staff chosenStaff;
    private Staff chosenTeam;

    private Datasource<EventList> eventListDatasource;
    private EventList eventList;
    private Event thisEvent;



    public void initialize() {
        ownerListDatasource = new OwnerListFileDatasource("data", "owner-list.csv");
        ownerList = ownerListDatasource.readData();
        owner = ownerList.findOwnerByEventId((String) FXRouter.getData());
        staffListDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        staffList = staffListDatasource.readData();
        eventStaffList = staffList.findStaffListByEventId(owner.getEventId());
        teamList = eventStaffList.findAllTeam();
        eventListDatasource = new EventListDatasource("data", "event-list.csv");
        eventList = eventListDatasource.readData();
        thisEvent = eventList.findEventById(owner.getEventId());
        cleanUp();
        showTeamTable(teamList);

        teamsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Staff>() {
            @Override
            public void changed(ObservableValue observable, Staff oldValue, Staff newValue) {
                if (newValue != null) {
                    cleanUp();
                    chosenTeam = newValue;
                    chosenTeamStaffList = eventStaffList.findStaffListByTeamId(chosenTeam.getTeamId()).findExistStaffList();
                    showStaffsTable(chosenTeamStaffList);
                    teamNameTextField.setText(chosenTeam.getTeamName());
                    amountTextField.setText(String.valueOf(eventStaffList.findStaffListByTeamId(chosenTeam.getTeamId()).getStaffs().size()));
                    startDatePicker.setValue(LocalDate.parse(chosenTeam.getStringStartDate()));
                    endDatePicker.setValue(LocalDate.parse(chosenTeam.getStringFinishDate()));
                    startTimeTextField.setText(chosenTeam.getStringStartTime());
                    endTimeTextField.setText(chosenTeam.getStringFinishTime());
                }
            }
        });

        showStaffsTable(chosenTeamStaffList);
        staffsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Staff>() {
            @Override
            public void changed(ObservableValue observable, Staff oldValue, Staff newValue) {
                if (newValue != null) {
                    chosenStaff = newValue;
                    banButton.setText(chosenStaff.isActive() ? "BAN": "UNBAN");
                    promoteButton.setText(chosenStaff.isLeader() ? "DEPROMOTE" : "PROMOTE");
                }
            }
        });

    }

    public void createTeam() {
        cleanUp();
        if (startDatePicker.getValue() != null && endDatePicker.getValue() != null && !startTimeTextField.getText().equals("") && !endTimeTextField.getText().equals("")
                && Validation.isTime(startTimeTextField.getText()).equals("") && Validation.isTime(endTimeTextField.getText()).equals("")) {
            String teamName = teamNameTextField.getText();
            LocalDateTime startDateTime = LocalDateTime.parse(startDatePicker.getValue().toString() + "T" + startTimeTextField.getText());
            LocalDateTime endDateTime = LocalDateTime.parse(endDatePicker.getValue().toString() + "T" + endTimeTextField.getText());
            String amount = amountTextField.getText();
            if (checkText(teamName, startDateTime, endDateTime, amount)) {
                if(teamList.bookNewStaffs(LocalDateTime.now().toString(), teamName, owner.getEventId(), Integer.valueOf(amount), startDateTime, endDateTime)){
                    staffList.bookInstantlyNewStaffs(LocalDateTime.now().toString(), teamName, owner.getEventId(), Integer.valueOf(amount), startDateTime, endDateTime);
                    staffListDatasource.writeData(staffList);
                    handleGoStaffListController();
                }else {
                    errorTeamNameLabel.setText("This team name was taken.");
                }

            }
        }else {
            errorDateTimeLabel.setText("Please correctly determine The Date & Time.");
        }
    }

    public void banStaff() {
        if (chosenStaff != null) {
            banButton.setText(banButton.getText().equals("BAN") ? "UNBAN" : "BAN");
            if (chosenStaff.isActive()) {
                owner.deactivate(chosenStaff);
            }else {
                owner.activate(chosenStaff);
            }
            staffListDatasource.writeData(staffList);
            showStaffsTable(chosenTeamStaffList);
        }

    }

    public void promoteStaff() {
        if (chosenStaff != null) {
            promoteButton.setText(promoteButton.getText().equals("PROMOTE") ? "DEPROMOTE" : "PROMOTE");
            if (chosenStaff.isLeader()) {
                owner.dePromote(chosenStaff);
            }else {
                owner.promote(chosenStaff);
            }
            staffListDatasource.writeData(staffList);
            showStaffsTable(chosenTeamStaffList);
        }

    }

    private boolean checkText(String teamName, LocalDateTime startDateTime, LocalDateTime endDateTime, String amount) {
        errorTeamNameLabel.setText(Validation.isAllowName(teamName));
        errorDateTimeLabel.setText(startDateTime.isBefore(thisEvent.getStartDateTime()) && endDateTime.isBefore(thisEvent.getStartDateTime())
        && startDateTime.isBefore(endDateTime) ? "" : "Please correctly determine this Date & Time.");
        errorAmountLabel.setText(Validation.isAllowAmount(amount));
        if (errorTeamNameLabel.getText().equals("") && errorDateTimeLabel.getText().equals("") && errorAmountLabel.getText().equals("")) {
            return true;
        }
        return false;

    }

    private void cleanUp() {
        errorTeamNameLabel.setText("");
        errorAmountLabel.setText("");
        errorDateTimeLabel.setText("");
        startDatePicker.setEditable(false);
        endDatePicker.setEditable(false);
    }

    private void showTeamTable(StaffList staffList) {
        Label customPlaceholderTeamTable = new Label("This event have no team yet.");
        customPlaceholderTeamTable.setStyle("-fx-font-size: 18px;");
        teamsTableView.setPlaceholder(customPlaceholderTeamTable);

        TableColumn<Staff, String> teamNameColumn = new TableColumn<>("TEAM");
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        teamNameColumn.setStyle("-fx-alignment: CENTER;");
        teamNameColumn.setPrefWidth(447);


        teamsTableView.getColumns().clear();
        teamsTableView.getColumns().add(teamNameColumn);



        teamsTableView.getItems().clear();

        for (Staff staff: staffList.getStaffs()) {
            teamsTableView.getItems().add(staff);
        }
    }

    @FXML
    public void handleGoTeamChat(){
        try {
            FXRouter.goTo("owner-team-chat", owner.getEventId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void showStaffsTable(StaffList staffList) {
        Label customPlaceholderStaffs = new Label("This team have no staff yet.");
        customPlaceholderStaffs.setStyle("-fx-font-size: 18px;");
        staffsTableView.setPlaceholder(customPlaceholderStaffs);

        TableColumn<Staff, String> staffUsernameColumn = new TableColumn<>("USERNAME");
        staffUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        staffUsernameColumn.setPrefWidth(150);

        TableColumn<Staff, String> staffNameColumn = new TableColumn<>("STAFF NAME");
        staffNameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        staffNameColumn.setPrefWidth(150);

        TableColumn<Staff, String> leaderColumn = new TableColumn<>("ROLE");
        leaderColumn.setCellValueFactory(new PropertyValueFactory<>("stringLeader"));
        leaderColumn.setStyle("-fx-alignment: CENTER;");
        leaderColumn.setPrefWidth(74);


        TableColumn<Staff, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-alignment: CENTER;");
        statusColumn.setPrefWidth(74);

        staffsTableView.getColumns().clear();
        staffsTableView.getColumns().add(staffUsernameColumn);
        staffsTableView.getColumns().add(staffNameColumn);
        staffsTableView.getColumns().add(leaderColumn);
        staffsTableView.getColumns().add(statusColumn);


        staffsTableView.getItems().clear();
        if (staffList != null) {
            for (Staff staff: staffList.getStaffs()) {
                staffsTableView.getItems().add(staff);
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
