package cs211.project.controllers;

import cs211.project.models.Staff;
import cs211.project.models.StaffList;
import cs211.project.services.Datasource;
import cs211.project.services.StaffListFileDatasource;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TeamStaffListController {
    @FXML
    private Button banButton;
    @FXML
    private TableView staffListTableView;
    private Staff leader;
    private Datasource<StaffList> staffListDatasource;
    private StaffList staffList;
    private Staff chosenStaff;

    public void init() {
        staffListDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        staffList = staffListDatasource.readData();

        showTeamTable(staffList.findStaffListByTeamId(leader.getTeamId()).findExistStaffList());
        staffListTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Staff>() {
            @Override
            public void changed(ObservableValue observable, Staff oldValue, Staff newValue) {
                if (newValue != null) {
                    chosenStaff = newValue;
                    banButton.setText(chosenStaff.isActive() ? "BAN" : "UNBAN");
                }
            }
        });

    }



    public void setLeader(Staff staff){
        this.leader = staff;
        init();
    }

    public void banStaff() {
        if (chosenStaff != null) {
            banButton.setText(banButton.getText().equals("BAN") ? "UNBAN" : "BAN");
            if (chosenStaff.isActive()) {
                leader.deActivate(chosenStaff);
            }else {
                leader.activate(chosenStaff);
            }
            staffListDatasource.writeData(staffList);
            showTeamTable(staffList.findStaffListByTeamId(leader.getTeamId()).findExistStaffList());
        }

    }

    private void showTeamTable(StaffList staffList) {
        Label customPlaceholderStaffs = new Label("This team have no staff yet.");
        customPlaceholderStaffs.setStyle("-fx-font-size: 18px;");
        staffListTableView.setPlaceholder(customPlaceholderStaffs);

        TableColumn<Staff, String> staffUsernameColumn = new TableColumn<>("USERNAME");
        staffUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        staffUsernameColumn.setPrefWidth(230);

        TableColumn<Staff, String> staffNameColumn = new TableColumn<>("STAFF NAME");
        staffNameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        staffNameColumn.setPrefWidth(230);

        TableColumn<Staff, String> leaderColumn = new TableColumn<>("ROLE");
        leaderColumn.setCellValueFactory(new PropertyValueFactory<>("stringLeader"));
        leaderColumn.setStyle("-fx-alignment: CENTER;");
        leaderColumn.setPrefWidth(69);


        TableColumn<Staff, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-alignment: CENTER;");
        statusColumn.setPrefWidth(69);

        staffListTableView.getColumns().clear();
        staffListTableView.getColumns().add(staffUsernameColumn);
        staffListTableView.getColumns().add(staffNameColumn);
        staffListTableView.getColumns().add(leaderColumn);
        staffListTableView.getColumns().add(statusColumn);


        staffListTableView.getItems().clear();
        if (staffList != null) {
            for (Staff staff : staffList.getStaffs()) {
                staffListTableView.getItems().add(staff);
            }
        }

    }
}
