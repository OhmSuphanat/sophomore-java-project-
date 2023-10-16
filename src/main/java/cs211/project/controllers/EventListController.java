package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.util.ArrayList;

public class EventListController {
    public TableView eventsTableView;
    public TextField searchTextField;
    private Datasource<EventList> datasource;
    private Datasource<ParticipantList> participantListDatasource;
    private ParticipantList participantList;
    private EventList eventList;
    private EventList showList;
    private Datasource<UserList> userListDatasource;
    private UserList userList;
    private User user;
    private Event chosen;
    @FXML
    public void initialize() {
        // eventSys setup //
        datasource = new EventListDatasource("data", "event-list.csv");
        eventList = datasource.readData();
        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        participantList = participantListDatasource.readData();
        eventList.setStatus(participantList);
        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        userList = userListDatasource.readData();
        user = userList.findUserById((String) FXRouter.getData());
        ///////////////////////
        showList = eventList.findEventsNotDisable(); // specific case;
        showTable(showList);
        eventsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue observable, Event oldValue, Event newValue) {
                if (newValue != null) {
                    chosen = newValue;
                }
            }
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchTable(newValue);
        });

        eventsTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                // Perform your action here when double-click occurs.
                Object selectedItem = eventsTableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    handleGoEventDetail();
                    // Add your code to handle the double-click action.
                }
            }
        });

        eventsTableView.setOnKeyPressed(event ->{
            if (event.getCode() == KeyCode.TAB) {
                handleGoEventCreator();
            }
        });

    }


    private void showTable(EventList eventList) {
        Label customPlaceholder = new Label("Let's create the first Event!!!.");
        customPlaceholder.setStyle("-fx-font-size: 18px;");
        eventsTableView.setPlaceholder(customPlaceholder);

        TableColumn<Event, String> eventNameColumn = new TableColumn<>("EVENT NAME");
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventNameColumn.setPrefWidth(316);

        TableColumn<Event, String> startDateTimeColumn = new TableColumn<>("START DATE&TIME");
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("stringStartDateTime"));
        startDateTimeColumn.setStyle("-fx-alignment: CENTER;");
        startDateTimeColumn.setPrefWidth(180);

        TableColumn<Event, String> finishDateTimeColumn = new TableColumn<>("FINISH DATE&TIME");
        finishDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("stringFinishDateTime"));
        finishDateTimeColumn.setStyle("-fx-alignment: CENTER;");
        finishDateTimeColumn.setPrefWidth(180);

        TableColumn<Event, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-alignment: CENTER;");
        statusColumn.setPrefWidth(180);



        eventsTableView.getColumns().clear();
        eventsTableView.getColumns().add(eventNameColumn);
        eventsTableView.getColumns().add(startDateTimeColumn);
        eventsTableView.getColumns().add(finishDateTimeColumn);
        eventsTableView.getColumns().add(statusColumn);



        eventsTableView.getItems().clear();


        for (Event events: eventList.getEventList()) {
            eventsTableView.getItems().add(events);
        }
    }

    private void searchTable(String searchKeyword) {
        EventList filteredEventList = new EventList();
        for (Event event : showList.getEventList()) {
            if (event.getEventName().toLowerCase().contains(searchKeyword.toLowerCase())) {
                filteredEventList.addNewEvent(event);
            }
        }
        showTable(filteredEventList);
    }

    @FXML
    public void handleGoEventList(){
        try {
            datasource.writeData(eventList);
            FXRouter.goTo("event-list", user.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleGoEventCreator(){
        try {
            datasource.writeData(eventList);
            FXRouter.goTo("event-creator", user.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGoAccountHub(){
        try {
            datasource.writeData(eventList);
            FXRouter.goTo("account-hub", user.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleGoEventDetail(){
        try {
            datasource.writeData(eventList);
            ArrayList<String> component = new ArrayList<>();
            component.add(user.getId());
            component.add(chosen.getId());
            FXRouter.goTo("event-detail", component);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
