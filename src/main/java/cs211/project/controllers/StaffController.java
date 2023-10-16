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

public class StaffController {


    @FXML private TextField postTextField;
    @FXML private ListView threadListView;
    @FXML private Label teamNameLabel;
    @FXML private TableView scheduleListTableView;
    private Datasource<StaffList> staffListDatasource;
    private Datasource<ScheduleList> scheduleListDatasource;
    private Datasource<ChatList> chatListDatasource;
    private Datasource<UserList> userListDatasource;
    private UserList userList;
    private ChatList chatList;
    private ScheduleList scheduleList;
    private StaffList staffList;
    private Staff passedStaff;
    private Schedule selectedSchedule;
    private User thisUser;

    public void initialize() {
        staffListDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        staffList = staffListDatasource.readData();
        passedStaff = (Staff) FXRouter.getData();
        Staff thisStaff = staffList.findStaffListByEventId(passedStaff.getEventId()).findStaffById(passedStaff.getId());
        teamNameLabel.setText(thisStaff.getTeamName());

        scheduleListDatasource = new ScheduleListFileDatasource("data", "schedule-list.csv");
        scheduleList = scheduleListDatasource.readData();
        ScheduleList thisTeamScheduleList = scheduleList.findSchedulesByTeamId(thisStaff.getTeamId());

        chatListDatasource = new ChatListFileDatasource("data", "chat-list.csv");
        chatList = chatListDatasource.readData();

        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        userList = userListDatasource.readData();
        thisUser = userList.findUserByUsername(thisStaff.getId());

        showScheduleList(thisTeamScheduleList,thisStaff);

        scheduleListTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Schedule>() {
            @Override
            public void changed(ObservableValue observable, Schedule oldValue, Schedule newValue) {
                if (newValue != null) {
                    selectedSchedule = newValue;
                }
            }
        });

        showListView(chatList.findTeamChatByTeamId(thisStaff.getTeamId()));

        postTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onAddPostButtonClicked(thisStaff);
            }
        });
    }



    private void showScheduleList(ScheduleList scheduleList, Staff thisStaff) {
        Label customPlaceholderStaffs = new Label("This content isn't available for you.");
        customPlaceholderStaffs.setStyle("-fx-font-size: 18px;");
        scheduleListTableView.setPlaceholder(customPlaceholderStaffs);

        TableColumn<Schedule, String> titleColumn = new TableColumn<>("TITLE");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(190);

        TableColumn<Schedule, String> detailColumn = new TableColumn<>("DETAIL");
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));
        detailColumn.setPrefWidth(190);

        TableColumn<Schedule, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-alignment: CENTER;");
        statusColumn.setPrefWidth(68);


        scheduleListTableView.getColumns().clear();
        scheduleListTableView.getColumns().add(titleColumn);
        scheduleListTableView.getColumns().add(detailColumn);
        scheduleListTableView.getColumns().add(statusColumn);

        if (!thisStaff.isActive()) {
            return;
        }



        scheduleListTableView.getItems().clear();
        if (scheduleList != null) {
            for (Schedule schedule: scheduleList.getSchedules()) {
                scheduleListTableView.getItems().add(schedule);
            }
        }
    }

    private void showListView(ChatList observer) {
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

    public void onAddPostButtonClicked(Staff thisStaff) {
        String message = postTextField.getText().trim();
        if (!message.isEmpty()) {
            String userId = userList.findUserByUsername(thisStaff.getId()).getId();
            String teamId = thisStaff.getTeamId();
            LocalDateTime sentText = LocalDateTime.now();

            chatList.addNewChat(userId, teamId, message, sentText);
            chatListDatasource.writeData(chatList);
            postTextField.clear();

            showListView(chatList.findTeamChatByTeamId(thisStaff.getTeamId()));
        }
    }

    @FXML
    public void handleGoEventList(){
        try {
            FXRouter.goTo("event-list", thisUser.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleGoEventCreator(){
        try {
            FXRouter.goTo("event-creator", thisUser.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGoAccountHub(){
        try {
            FXRouter.goTo("account-hub", thisUser.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
