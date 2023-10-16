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

import java.io.IOException;

public class ParticipantListController {


    @FXML private Label nowLabel;
    @FXML private Label totalLabel;
    @FXML private  TableView participantsTableView;
    @FXML private Button banButton;
    private Datasource<OwnerList> ownerListDatasource;
    private OwnerList ownerList;
    private Owner owner;
    private Datasource<ParticipantList> participantListDatasource;
    private ParticipantList participantList;
    private ParticipantList thisEventParticipantList;
    private Participant chosen;

    public void initialize() {
        ownerListDatasource = new OwnerListFileDatasource("data", "owner-list.csv");
        ownerList = ownerListDatasource.readData();
        owner = ownerList.findOwnerByEventId((String) FXRouter.getData());
        participantListDatasource = new ParticipantListFileDatasource("data", "participant-list.csv");
        participantList = participantListDatasource.readData();
        thisEventParticipantList = participantList.findParticipantsByEventId(owner.getEventId());
        setText();
        showTable(thisEventParticipantList.findExistParticipantList());

        participantsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Participant>() {
            @Override
            public void changed(ObservableValue observable, Participant oldValue, Participant newValue) {
                if (newValue != null) {
                    chosen = newValue;
                    banButton.setText(chosen.isActive() ? "BAN" : "UNBAN");
                }
            }
        });
    }

    public void banParticipant() {
        if (chosen != null) {
            if (chosen.isActive()) {
                owner.deactivate(chosen);
            }else {
                owner.activate(chosen);
            }
            banButton.setText(chosen.isActive() ? "BAN" : "UNBAN");
            participantListDatasource.writeData(participantList);
            showTable(thisEventParticipantList.findExistParticipantList());
        }
    }

    private void setText() {
        totalLabel.setText(String.valueOf(thisEventParticipantList.getParticipants().size()));
        totalLabel.setAlignment(Pos.CENTER);
        nowLabel.setText(String.valueOf(thisEventParticipantList.findExistParticipantList().getParticipants().size()));
        nowLabel.setAlignment(Pos.CENTER);
    }

    private void showTable(ParticipantList participantList) {
        Label customPlaceholderStaffs = new Label("This event have no participant yet.");
        customPlaceholderStaffs.setStyle("-fx-font-size: 18px;");
        participantsTableView.setPlaceholder(customPlaceholderStaffs);

        TableColumn<Participant, String> usernameColumn = new TableColumn<>("USERNAME");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setPrefWidth(300);

        TableColumn<Participant, String> displayNameColumn = new TableColumn<>("NAME");
        displayNameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        displayNameColumn.setPrefWidth(300);

        TableColumn<Participant, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-alignment: CENTER;");
        statusColumn.setPrefWidth(97);





        participantsTableView.getColumns().clear();
        participantsTableView.getColumns().add(usernameColumn);
        participantsTableView.getColumns().add(displayNameColumn);
        participantsTableView.getColumns().add(statusColumn);



        participantsTableView.getItems().clear();

        for (Participant participant: participantList.getParticipants()) {
            participantsTableView.getItems().add(participant);
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
