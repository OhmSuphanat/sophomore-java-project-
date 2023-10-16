package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ScheduledEventListController {
    @FXML private TableView eventListTableView;
    @FXML private TextField searchTextField;
    private String userId;
    private Datasource<EventList> eventListDatasource;
    private Datasource<StaffList> staffListDatasource;
    private Datasource<ParticipantList> participantListDatasource;
    private Datasource<UserList> userListDatasource;
    private EventList eventList;
    private StaffList staffList;
    private ParticipantList participantList;
    private UserList userList;
    private User user;
    private Event chosenEvent;
    private EventList joinerEventList;
    private StaffList userStaffList;
    ParticipantList userParticipantList;

    public void init() {
        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        userList = userListDatasource.readData();
        user = userList.findUserById((String) FXRouter.getData());

        eventListDatasource = new EventListDatasource("data", "event-list.csv");
        eventList = eventListDatasource.readData();

        staffListDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        staffList = staffListDatasource.readData();

        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        participantList = participantListDatasource.readData();
        eventList.setStatus(participantList);

        userStaffList = staffList.findStaffListByStaffId(user.getUsername());
        userParticipantList = participantList.findParticipantByUserId(user.getUsername());
        joinerEventList = eventList.findEventListByJoiningUser(userParticipantList, userStaffList).findEventsNotDisable();
        showTable(joinerEventList);

        eventListTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue observable, Event oldValue, Event newValue) {
                if (newValue != null) {
                    chosenEvent = newValue;
                }
            }
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchTable(newValue);
        });

        eventListTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                // Perform your action here when double-click occurs.
                Object selectedItem = eventListTableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    try {
                        showScheduleList();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });


    }

    public void setUserId(String userId) {
        this.userId = userId;
        init();
    }

    private void showTable(EventList eventList) {
        Label customPlaceholder = new Label("You have no scheduled event yet.");
        customPlaceholder.setStyle("-fx-font-size: 18px;");
        eventListTableView.setPlaceholder(customPlaceholder);

        TableColumn<Event, String> eventNameColumn = new TableColumn<>("EVENT NAME");
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventNameColumn.setPrefWidth(175);

        TableColumn<Event, String> startDateTimeColumn = new TableColumn<>("START DATE&TIME");
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("stringStartDateTime"));
        startDateTimeColumn.setStyle("-fx-alignment: CENTER;");
        startDateTimeColumn.setPrefWidth(175);

        TableColumn<Event, String> finishDateTimeColumn = new TableColumn<>("FINISH DATE&TIME");
        finishDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("stringFinishDateTime"));
        finishDateTimeColumn.setStyle("-fx-alignment: CENTER;");
        finishDateTimeColumn.setPrefWidth(175);

        TableColumn<Event, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-alignment: CENTER;");
        statusColumn.setPrefWidth(73);


        eventListTableView.getColumns().clear();
        eventListTableView.getColumns().add(eventNameColumn);
        eventListTableView.getColumns().add(startDateTimeColumn);
        eventListTableView.getColumns().add(finishDateTimeColumn);
        eventListTableView.getColumns().add(statusColumn);



        eventListTableView.getItems().clear();


        for (Event events: eventList.getEventList()) {
            eventListTableView.getItems().add(events);
        }
    }

    private void searchTable(String searchKeyword) {
        EventList filteredEventList = new EventList();
        for (Event event : joinerEventList.getEventList()) {
            if (event.getEventName().toLowerCase().contains(searchKeyword.toLowerCase())) {
                filteredEventList.addNewEvent(event);
            }
        }
        showTable(filteredEventList);
    }

    private void showScheduleList() throws IOException {
        Participant thisParticipant = userParticipantList.findParticipantByEventId(chosenEvent.getId());
        if (thisParticipant != null && thisParticipant.isActive()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs211/project/views/participant-schedule-list-view.fxml"));
            Parent root = loader.load();

            ParticipantScheduleListController participantScheduleListController = loader.getController();
            participantScheduleListController.setEventId(chosenEvent.getId());

            Stage popUpStage = new Stage();
            popUpStage.initStyle(StageStyle.UNDECORATED);
            popUpStage.setResizable(false);
            popUpStage.setScene(new Scene(root,600,800));
            popUpStage.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal) {
                    // Pop-up lost focus, close it
                    popUpStage.close();
                }
            });
            popUpStage.show();
        }else {
            Staff thisStaff = userStaffList.findStaffByEventId(chosenEvent.getId());
            if (thisStaff != null && !thisStaff.isLeader()) {
                FXRouter.goTo("staff" ,thisStaff);
            }else if (thisStaff != null && thisStaff.isLeader()) {
                FXRouter.goTo("leader", thisStaff);
            }
        }

    }
}
