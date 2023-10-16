package cs211.project.controllers;

import cs211.project.models.Schedule;
import cs211.project.models.ScheduleList;
import cs211.project.services.Datasource;
import cs211.project.services.ScheduleListFileDatasource;
import cs211.project.services.ScheduleTimeComparator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class ParticipantScheduleListController {
    @FXML private TableView scheduleListTableView;
    private String eventId;
    private Datasource<ScheduleList> scheduleListDatasource;
    private ScheduleList scheduleList;
    private ScheduleList thisEventSchedule;


    public void init() {
        scheduleListDatasource = new ScheduleListFileDatasource("data", "schedule-list.csv");
        scheduleList = scheduleListDatasource.readData();
        scheduleList.sortByStartDateTime(new ScheduleTimeComparator());
        thisEventSchedule = scheduleList.findSchedulesByTeamId(eventId);
        showTable(thisEventSchedule);

        scheduleListTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Schedule>() {
            @Override
            public void changed(ObservableValue observable, Schedule oldValue, Schedule newValue) {
                if (newValue != null) {
                }
            }
        });

    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
        init();
    }

    private void showTable(ScheduleList scheduleList) {
        Label customPlaceholder = new Label("This event have no schedule yet.");
        customPlaceholder.setStyle("-fx-font-size: 18px;");
        scheduleListTableView.setPlaceholder(customPlaceholder);

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


        scheduleListTableView.getColumns().clear();
        scheduleListTableView.getColumns().add(startDateTimeColumn);
        scheduleListTableView.getColumns().add(endDateTimeColumn);
        scheduleListTableView.getColumns().add(titleColumn);
        scheduleListTableView.getColumns().add(detailColumn);



        scheduleListTableView.getItems().clear();

        for (Schedule schedule: scheduleList.getSchedules()) {
            scheduleListTableView.getItems().add(schedule);
        }
    }



}
