package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.services.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LeaderController {

    @FXML private ListView threadListView;
    @FXML private TextField postTextField;
    @FXML private TableView scheduleListTableView;
    @FXML private TextField titleTextField;
    @FXML private Label errorTitleLabel;
    @FXML private TextField detailTextField;
    @FXML private  Label errorDetailLabel;
    @FXML private  Label teamNameLabel;

    private Datasource<StaffList> staffListDatasource;
    private StaffList staffList;
    private Staff leader;

    private Datasource<UserList> userListDatasource;
    private UserList userList;
    private Datasource<ScheduleList> scheduleListDatasource;
    private ScheduleList scheduleList;
    private Schedule selectedSchedule;
    private Datasource<ChatList> chatListDatasource;
    private  ChatList chatList;

    public void initialize() {
        staffListDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        staffList = staffListDatasource.readData();
        Staff passedStaff = (Staff) FXRouter.getData();
        leader = staffList.findStaffListByEventId(passedStaff.getEventId()).findStaffById(passedStaff.getId());
        teamNameLabel.setText(leader.getTeamName());

        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        userList = userListDatasource.readData();

        scheduleListDatasource = new ScheduleListFileDatasource("data", "schedule-list.csv");
        scheduleList = scheduleListDatasource.readData();

        chatListDatasource = new ChatListFileDatasource("data", "chat-list.csv");
        chatList = chatListDatasource.readData();

        cleanUp();
        showTeamScheduleList(scheduleList.findSchedulesByTeamId(leader.getTeamId()));
        showListView(chatList.findTeamChatByTeamId(leader.getTeamId()));

        postTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onAddPostButtonClicked(leader);
            }
        });

        scheduleListTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Schedule>() {
            @Override
            public void changed(ObservableValue observable, Schedule oldValue, Schedule newValue) {
                if (newValue != null) {
                    cleanUp();
                    selectedSchedule = newValue;
                    titleTextField.setText(selectedSchedule.getTitle());
                    detailTextField.setText(selectedSchedule.getDetail());
                }
            }
        });
    }

    public void createSchedule() {
        String title = titleTextField.getText();
        String detail = detailTextField.getText();
        if (checkText(title,detail)) {
            scheduleList.addNewSchedule(leader.getTeamId(), title, LocalDateTime.now(), LocalDateTime.now(), detail, true);
            scheduleListDatasource.writeData(scheduleList);
            showTeamScheduleList(scheduleList.findSchedulesByTeamId(leader.getTeamId()));
        }
    }

    public void removeSchedule() {
        if (selectedSchedule != null) {
            scheduleList.remove(selectedSchedule);
            scheduleListDatasource.writeData(scheduleList);
            showTeamScheduleList(scheduleList.findSchedulesByTeamId(leader.getTeamId()));
        }

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

    private void showTeamScheduleList(ScheduleList scheduleList) {
        Label customPlaceholderStaffs = new Label("This content isn't available for you.");
        customPlaceholderStaffs.setStyle("-fx-font-size: 18px;");
        scheduleListTableView.setPlaceholder(customPlaceholderStaffs);

        TableColumn<Schedule, String> titleColumn = new TableColumn<>("TITLE");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(200);

        TableColumn<Schedule, String> detailColumn = new TableColumn<>("DETAIL");
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));
        detailColumn.setPrefWidth(300);

        TableColumn<Schedule, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-alignment: CENTER;");
        statusColumn.setPrefWidth(73);

        if (!leader.isActive()){
            return;
        }

        scheduleListTableView.getColumns().clear();
        scheduleListTableView.getColumns().add(titleColumn);
        scheduleListTableView.getColumns().add(detailColumn);
        scheduleListTableView.getColumns().add(statusColumn);



        scheduleListTableView.getItems().clear();
        if (scheduleList != null) {
            for (Schedule schedule: scheduleList.getSchedules()) {
                scheduleListTableView.getItems().add(schedule);
            }
        }
    }

    private void cleanUp() {
        errorTitleLabel.setText("");
        errorDetailLabel.setText("");
    }

    private boolean checkText(String title, String detail) {
        errorTitleLabel.setText(Validation.isAllowName(title));
        errorDetailLabel.setText(Validation.isDetail(detail));
        if (errorTitleLabel.getText().equals("") && errorDetailLabel.getText().equals("")) {
            return true;
        }
        return false;
    }

    public void showStaffListPopUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs211/project/views/team-staff-list-view.fxml"));
        Parent root = loader.load();

        TeamStaffListController teamStaffListController = loader.getController();
        teamStaffListController.setLeader(leader);

        Stage popUpStage = new Stage();
        popUpStage.initStyle(StageStyle.UNDECORATED);
        popUpStage.setResizable(false);
        popUpStage.setScene(new Scene(root,600,600));
        popUpStage.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                // Pop-up lost focus, close it
                popUpStage.close();
            }
        });
        popUpStage.show();
    }

    @FXML
    public void handleGoEventList(){
        try {
            FXRouter.goTo("event-list", userList.findUserByUsername(leader.getId()).getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleGoEventCreator(){
        try {
            FXRouter.goTo("event-creator", userList.findUserByUsername(leader.getId()).getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGoAccountHub(){
        try {
            FXRouter.goTo("account-hub", userList.findUserByUsername(leader.getId()).getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
