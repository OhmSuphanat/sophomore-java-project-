package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EventDetailController {
    @FXML private Button joinStaffButton;
    @FXML private Button joinParticipantButton;
    @FXML private Label nowSLabel;
    @FXML private Label totalSLabel;
    @FXML private TableView teamTableView;
    @FXML private Label eventNameLabel;
    @FXML private Label startJoinParticipantTimeLabel;
    @FXML private Label eventOwnerNameLabel;
    @FXML private Label eventStartTimeLabel;
    @FXML private Label eventEndTimeLabel;
    @FXML private Label startJoinStaffTimeLabel;
    @FXML private Label endJoinParticipantTimeLabel;
    @FXML private Label endJoinStaffTimeLabel;
    @FXML private Label eventDetailLabel;
    @FXML private Label nowPLabel;
    @FXML private Label totalPLabel;
    @FXML private Circle imageCircle;
    private Datasource <UserList> userListDatasource;
    private UserList userList;
    private User user;
    private Datasource<EventList> eventListDatasource;
    private EventList eventList;
    private Event thisEvent;
    private Datasource<ParticipantList> participantListDatasource;
    private ParticipantList participantList;
    private ParticipantList thisEventParticipantList;
    private Participant participant;
    private Datasource<StaffList> staffListDatasource;
    private StaffList staffList;
    private StaffList thisEventStaffList;
    private Staff chosenStaff;
    private Datasource<OwnerList> ownerListDatasource;
    private OwnerList ownerList;
    private Owner owner;

    public void initialize(){
        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        userList = userListDatasource.readData();
        ArrayList<String> fromFXRouter = (ArrayList<String>)FXRouter.getData();
        user = userList.findUserById(fromFXRouter.get(0));
        eventListDatasource = new EventListDatasource("data", "event-list.csv");
        eventList = eventListDatasource.readData();
        thisEvent = eventList.findEventById(fromFXRouter.get(1));
        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        participantList = participantListDatasource.readData();
        thisEventParticipantList = participantList.findParticipantsByEventId(thisEvent.getId());
        staffListDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        staffList = staffListDatasource.readData();
        thisEventStaffList = staffList.findStaffListByEventId(thisEvent.getId());
        ownerListDatasource = new OwnerListFileDatasource("data", "owner-list.csv");
        ownerList = ownerListDatasource.readData();
        owner = ownerList.findOwnerByEventId(thisEvent.getId());
        imageCircle.setFill(new ImagePattern(new Image("file:"+thisEvent.getImagePath())));
        cleanUp();
        showTable(thisEventStaffList.findAllTeam());

        teamTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Staff>() {
            @Override
            public void changed(ObservableValue observable, Staff oldValue, Staff newValue) {
                if (newValue != null) {
                    chosenStaff = newValue;
                    joinStaffButton.setVisible(true);
                    cleanUp();
                    setStaffTextButton();

                }
            }
        });

    }

    public void joinStaff() {
        if (chosenStaff != null) {
            thisEventStaffList.findEmptyStaffByTeamId(chosenStaff.getTeamId()).linkUser(user);
            staffListDatasource.writeData(staffList);
            handleGoEventDetail();
        }

    }

    public void joinParticipant() {
        thisEventParticipantList.findEmptyParticipant(thisEvent.getId()).linkUser(user);
        participantListDatasource.writeData(participantList);
        handleGoEventDetail();
    }
    private void setStaffTextButton() {
        startJoinStaffTimeLabel.setText(chosenStaff.getStringStartDateTime());
        endJoinStaffTimeLabel.setText(chosenStaff.getStringFinishDateTime());
        nowSLabel.setText(String.valueOf(thisEventStaffList.findStaffListByTeamId(chosenStaff.getTeamId()).findExistStaffList().getStaffs().size()));
        totalSLabel.setText(String.valueOf(thisEventStaffList.findStaffListByTeamId(chosenStaff.getTeamId()).getStaffs().size()));
        if (thisEventStaffList.findStaffListByTeamId(chosenStaff.getTeamId()).findExistStaffList().getStaffs().size() ==
                thisEventStaffList.findStaffListByTeamId(chosenStaff.getTeamId()).getStaffs().size() || LocalDateTime.now().isBefore(thisEventStaffList.getStaffs().get(0).getStartDateTime())) {
            joinStaffButton.setVisible(false);
        }
    }

    private void showTable(StaffList staffList) {
        Label customPlaceholder = new Label("This event has no team yet.");
        customPlaceholder.setStyle("-fx-font-size: 12px;");
        teamTableView.setPlaceholder(customPlaceholder);

        TableColumn<Staff, String> teamNameColumn = new TableColumn<>("TEAM");
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        teamNameColumn.setPrefWidth(187);


        teamTableView.getColumns().clear();
        teamTableView.getColumns().add(teamNameColumn);


        teamTableView.getItems().clear();
        if (staffList != null) {
            for (Staff staff: staffList.getStaffs()) {
                teamTableView.getItems().add(staff);
            }
        }
    }

    private void cleanUp() {
        eventNameLabel.setText(thisEvent.getEventName());
        eventOwnerNameLabel.setText(thisEvent.getOwner());
        eventDetailLabel.setText(thisEvent.getDetail());
        eventStartTimeLabel.setText(thisEvent.getStringStartDateTime());
        eventEndTimeLabel.setText(thisEvent.getStringFinishDateTime());
        participant = thisEventParticipantList.getParticipants().get(0);
        startJoinParticipantTimeLabel.setText(participant.getStringDateTime(participant.getStartDateTime()));
        endJoinParticipantTimeLabel.setText(participant.getStringDateTime(participant.getEndDateTime()));
        nowPLabel.setText(String.valueOf(thisEventParticipantList.findExistParticipantList().getParticipants().size()));
        nowPLabel.setAlignment(Pos.CENTER_RIGHT);
        totalPLabel.setText(String.valueOf(thisEventParticipantList.getParticipants().size()));
        startJoinStaffTimeLabel.setText("Choose A team");
        endJoinStaffTimeLabel.setText("Choose A team");
        nowSLabel.setText("");
        totalSLabel.setText("");
        if (thisEventParticipantList.findParticipant(user.getUsername(), thisEvent.getId()) != null || thisEventStaffList.findStaffById(user.getUsername()) != null || owner.getId().equals(user.getId())) {
            joinParticipantButton.setVisible(false);
            joinStaffButton.setVisible(false);
        }
        if (thisEventParticipantList.findExistParticipantList().getParticipants().size() == thisEventParticipantList.getParticipants().size()) {
            joinStaffButton.setVisible(false);
        }
        Participant forCheck = thisEventParticipantList.getParticipants().get(0);
        if ((LocalDateTime.now().isBefore(forCheck.getStartDateTime())) || thisEventParticipantList.findEmptyParticipant(thisEvent.getId()) == null) {
            joinParticipantButton.setVisible(false);
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

    @FXML
    public void handleGoEventDetail(){
        try {
            ArrayList<String> component = new ArrayList<>();
            component.add(user.getId());
            component.add(thisEvent.getId());
            FXRouter.goTo("event-detail", component);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
