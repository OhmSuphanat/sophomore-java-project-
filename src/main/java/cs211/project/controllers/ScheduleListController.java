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

public class ScheduleListController {

    @FXML private TableView schedulesTableView;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField startTimeTextField;
    @FXML private TextField endTimeTextField;
    @FXML private Label errorDateTimeLabel;
    @FXML private TextField titleTextField;
    @FXML private Label errorTitleLabel;
    @FXML private TextField detailTextField;
    @FXML private Label errorDetailLabel;

    private Datasource<OwnerList> ownerListDatasource;
    private OwnerList ownerList;
    private Owner owner;
    private Datasource<ScheduleList> scheduleListDatasource;
    private ScheduleList scheduleList;
    private ScheduleList eventSchedules;
    private Schedule chosen;
    private Datasource<EventList> eventListDatasource;
    private Event event;

    public void initialize() {
        ownerListDatasource = new OwnerListFileDatasource("data", "owner-list.csv");
        ownerList = ownerListDatasource.readData();
        scheduleListDatasource = new ScheduleListFileDatasource("data", "schedule-list.csv");
        scheduleList = scheduleListDatasource.readData();
        scheduleList.sortByStartDateTime(new ScheduleTimeComparator());
        owner = ownerList.findOwnerByEventId((String) FXRouter.getData());
        eventSchedules = scheduleList.findSchedulesByTeamId(owner.getEventId());
        eventListDatasource = new EventListDatasource("data", "event-list.csv");
        event = eventListDatasource.readData().findEventById(owner.getEventId());
        cleanUp();
        showTable(eventSchedules);

        schedulesTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Schedule>() {
            @Override
            public void changed(ObservableValue observable, Schedule oldValue, Schedule newValue) {
                if (newValue != null) {
                    cleanUp();
                    chosen = newValue;
                    titleTextField.setText(chosen.getTitle());
                    startDatePicker.setValue(LocalDate.of(chosen.getStartDateTime().getYear(), chosen.getStartDateTime().getMonthValue(), chosen.getStartDateTime().getDayOfMonth()));
                    endDatePicker.setValue(LocalDate.of(chosen.getFinishDateTime().getYear(), chosen.getFinishDateTime().getMonthValue(), chosen.getFinishDateTime().getDayOfMonth()));
                    startTimeTextField.setText(chosen.getStringStartTime());
                    endTimeTextField.setText(chosen.getStringFinishTime());
                    detailTextField.setText(chosen.getDetail());
                }
            }
        });
    }

    public void createSchedule() {
        cleanUp();
        if (startDatePicker.getValue() != null && endDatePicker.getValue() != null && !startTimeTextField.getText().equals("") && !endTimeTextField.getText().equals("")
        && Validation.isTime(startTimeTextField.getText()).equals("") && Validation.isTime(endTimeTextField.getText()).equals("")) {
            String title = titleTextField.getText();
            LocalDateTime startDateTime = LocalDateTime.parse(startDatePicker.getValue().toString() + "T" + startTimeTextField.getText());
            LocalDateTime endDateTime = LocalDateTime.parse(endDatePicker.getValue().toString() + "T" + endTimeTextField.getText());
            String detail = detailTextField.getText();
            if (checkText(title, startDateTime, endDateTime, detail)) {
                scheduleList.addNewSchedule(new Schedule(owner.getEventId(), title, startDateTime, endDateTime, detail, true));
                scheduleListDatasource.writeData(scheduleList);
                handleGoScheduleList();
            }
        }else {
            errorDateTimeLabel.setText("Please correctly determine The Date & Time");
        }

    }

    public void removeSchedule() {
        if (chosen != null) {
            scheduleList.remove(chosen);
            scheduleListDatasource.writeData(scheduleList);
            handleGoScheduleList();
        }
    }

    private boolean checkText(String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String detail) {
        errorTitleLabel.setText(Validation.isAllowName(title));
        errorDateTimeLabel.setText(!startDateTime.isBefore(event.getStartDateTime()) && !endDateTime.isAfter(event.getEndDateTime()) ? "" : "Schedule must be in event range" );
        errorDetailLabel.setText(Validation.isDetail(detail));
        if (errorTitleLabel.getText().equals("") && errorDateTimeLabel.getText().equals("") && errorDetailLabel.getText().equals("")) {
            return true;
        }
        return false;
    }

    private void cleanUp() {
        errorTitleLabel.setText("");
        errorDateTimeLabel.setText("");
        errorDetailLabel.setText("");
        startDatePicker.setEditable(false);
        endDatePicker.setEditable(false);
    }


    private void showTable(ScheduleList scheduleList) {
        Label customPlaceholder = new Label("This event have no schedule yet.");
        customPlaceholder.setStyle("-fx-font-size: 18px;");
        schedulesTableView.setPlaceholder(customPlaceholder);

        TableColumn<Schedule, String> startDateTimeColumn = new TableColumn<>("START DATE&TIME");
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("stringStartDateTime"));
        startDateTimeColumn.setStyle("-fx-alignment: CENTER;");
        startDateTimeColumn.setPrefWidth(120);

        TableColumn<Schedule, String> endDateTimeColumn = new TableColumn<>("END DATE&TIME");
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("stringFinishDateTime"));
        endDateTimeColumn.setStyle("-fx-alignment: CENTER;");
        endDateTimeColumn.setPrefWidth(115);

        TableColumn<Schedule, String> titleColumn = new TableColumn<>("TITLE");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));


        TableColumn<Schedule, String> detailColumn = new TableColumn<>("DETAIL");
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));


        schedulesTableView.getColumns().clear();
        schedulesTableView.getColumns().add(startDateTimeColumn);
        schedulesTableView.getColumns().add(endDateTimeColumn);
        schedulesTableView.getColumns().add(titleColumn);
        schedulesTableView.getColumns().add(detailColumn);



        schedulesTableView.getItems().clear();

        for (Schedule schedule: scheduleList.getSchedules()) {
            schedulesTableView.getItems().add(schedule);
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

    @FXML
    public void handleGoTeamChat(){
        try {
            FXRouter.goTo("owner-team-chat", owner.getEventId());
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
