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

import java.io.IOException;

public class YourEventListController {
    @FXML private TextField searchTextField;
    @FXML private TableView eventListTableView;
    private String userId;
    private Datasource<OwnerList> ownerListDatasource ;
    private Datasource<EventList> eventListDatasource;
    private Owner owner;
    private Event chosen;
    private OwnerList ownerList;
    private EventList eventList;
    private EventList ownerEventList;

    public void init() {
        ownerListDatasource= new OwnerListFileDatasource("data", "owner-list.csv");
        eventListDatasource = new EventListDatasource("data" , "event-list.csv");
        ownerList = ownerListDatasource.readData();
        eventList = eventListDatasource.readData();
        ownerList = ownerList.findEventsByOwnerId(userId);
        ownerEventList = eventList.findEventsByOwnerList(ownerList);
        ownerEventList.sortByEndDateTime();
        showTable(ownerEventList);

        eventListTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue observable, Event oldValue, Event newValue) {
                if (newValue != null) {
                    chosen = newValue;
                    owner = ownerList.findOwnerByEventId(chosen.getId());
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
                    handleGoEventSetting();
                }
            }
        });
    }

    public void setUserId(String userId) {
        this.userId = userId;
        init();
    }
    private void showTable(EventList ownerEventList) {
        Label customPlaceholder = new Label("You have no own event yet.");
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

        for (Event events: ownerEventList.getEventList()) {
            eventListTableView.getItems().add(events);
        }
    }

    private void searchTable(String searchKeyword) {
        EventList filteredEventList = new EventList();
        for (Event event : ownerEventList.getEventList()) {
            if (event.getEventName().toLowerCase().contains(searchKeyword.toLowerCase())) {
                filteredEventList.addNewEvent(event);
            }
        }
        showTable(filteredEventList);
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
    public void handleGoEventSetting(){
        try {
            FXRouter.goTo("event-setting", owner.getEventId());
        }catch (IOException e){
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
}
