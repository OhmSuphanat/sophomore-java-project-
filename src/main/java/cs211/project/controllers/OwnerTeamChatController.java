package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OwnerTeamChatController {
    @FXML
    private ListView<String> threadListView;

    @FXML
    private TextArea postTextArea;
    @FXML
    private TableView teamTableView;

    private ChatList chatlist;
    private Datasource<ChatList> chatListDatasource;
    private UserList userList;
    private User user;
    private Datasource<UserList> userListDatasource;
    private OwnerList ownerList;
    private Owner owner;
    private Datasource<OwnerList> ownerListDatasource;
    private StaffList staffList;
    private Staff thisTeam;
    private StaffList thisEventStaffList;
    private Datasource<StaffList> staffListDatasource;

    @FXML
    public void initialize() {
        ownerListDatasource = new OwnerListFileDatasource("data", "owner-list.csv");
        chatListDatasource = new ChatListFileDatasource("data", "chat-list.csv");
        staffListDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        ownerList = ownerListDatasource.readData();
        chatlist = chatListDatasource.readData();
        staffList = staffListDatasource.readData();
        userList = userListDatasource.readData();
        owner = ownerList.findOwnerByEventId((String) FXRouter.getData());
        thisEventStaffList = staffList.findStaffListByEventId(owner.getEventId());
        user = userList.findUserById(owner.getId());
        showTeamTableView(thisEventStaffList.findAllTeam());
        postTextArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onAddPostButtonClicked();
            }
        });

        teamTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Staff>() {
            @Override
            public void changed(ObservableValue observable, Staff oldValue, Staff newValue) {
                if (newValue != null) {
                    thisTeam = newValue;
                    showListView(chatlist.findTeamChatByTeamId(thisTeam.getTeamId()));

                }
            }
        });
    }

    public void onAddPostButtonClicked() {
        if (thisTeam != null) {
            String message = postTextArea.getText().trim();
            if (!message.isEmpty()) {
                String userId = user.getId();
                String teamId = thisTeam.getTeamId();
                LocalDateTime sentText = LocalDateTime.now();

                chatlist.addNewChat(userId, teamId, message, sentText);
                chatListDatasource.writeData(chatlist);
                postTextArea.clear();

                showListView(chatlist.findTeamChatByTeamId(thisTeam.getTeamId()));
            }
        }
    }
    public void showTeamTableView(StaffList staffList){
        Label customPlaceholderTeam = new Label("This event have no team yet.");
        customPlaceholderTeam.setStyle("-fx-font-size: 18px;");
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
    public void showListView(ChatList observer) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        ArrayList<String> chatThreads = new ArrayList<>();
        for (Chat chat : observer.getChats()) {
            String displayName = userList.findUserById(chat.getUserId()).getDisplayName();
            String chatThread = displayName + "\t"+ chat.getSentText().toString().split("T")[0]+ "\t" + "[" + chat.getSentText().format(formatter) + "]" + " :\n" + chat.getMessage();
            chatThreads.add(chatThread);
        }

        // Create an observable list and set it to the ListView
        ObservableList<String> observableChatThreads = FXCollections.observableArrayList(chatThreads);
        threadListView.setItems(observableChatThreads);
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
