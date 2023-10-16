package cs211.project.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class StaffList {
    private ArrayList<Staff> staffs;
    public StaffList() {
        staffs = new ArrayList<>();
    }


    public void addNewStaff(String id, String displayName,boolean active, String teamId, String teamName, String eventId, boolean leader, LocalDateTime startDateTime, LocalDateTime finishDateTime) {
        teamName = teamName.trim();
        staffs.add(new Staff(id, displayName, active, teamId, teamName, eventId, leader, startDateTime, finishDateTime));
    }

    public void addNewStaff(Staff staff) {
        staffs.add(staff);
    }

    public boolean bookNewStaffs(String teamId, String teamName, String eventId, int amount, LocalDateTime startDateTime, LocalDateTime finishDateTime) {
        teamName = teamName.trim();
        Staff exist = findStaffByTeamName(teamName);
        if (exist == null) {
            for (int i=0;i<amount;i++) {
                staffs.add(new Staff("","", true,teamId,teamName,eventId,false, startDateTime, finishDateTime));
            }
            return true;
        }
        return false;
    }

    public void bookInstantlyNewStaffs(String teamId, String teamName, String eventId, int amount, LocalDateTime startDateTime, LocalDateTime finishDateTime) {
            for (int i=0;i<amount;i++) {
                staffs.add(new Staff("","", true,teamId,teamName,eventId,false, startDateTime, finishDateTime));
            }
    }

    public StaffList findStaffListByStaffId(String id) {
        StaffList staffList = new StaffList();
        for (Staff staff : staffs) {
            if (staff.isId(id)) {
                staffList.addNewStaff(staff);
            }
        }
        return staffList;
    }

    public Staff findEmptyStaffByTeamId(String teamId) {
        for (Staff staff : staffs) {
            if (staff.isTeamId(teamId) && staff.getId().equals("")) {
                return staff;
            }
        }
        return null;
    }

    public Staff findStaffByTeamName(String teamName) {
        for (Staff staff : staffs) {
            if (staff.isTeamName(teamName)) {
                return staff;
            }
        }
        return null;
    }

    public StaffList findStaffListByEventId(String eventId) {
        StaffList staffList = new StaffList();
        for (Staff staff : staffs) {
            if (staff.isEventId(eventId)) {
                staffList.addNewStaff(staff);
            }
        }
        return staffList;
    }

    public StaffList findExistStaffList() {
        StaffList staffList = new StaffList();
        for (Staff staff : staffs) {
            if (!staff.getId().equals("")){
                staffList.addNewStaff(staff);
            }
        }
        return staffList;
    }

    public StaffList findStaffListByTeamId(String teamId) {
        StaffList staffList = new StaffList();
        for (Staff staff : staffs) {
            if (staff.isTeamId(teamId)) {
                staffList.addNewStaff(staff);
            }
        }
        return staffList;
    }

    public Staff findStaffById(String id) {
        for (Staff staff : staffs) {
            if (staff.isId(id)) {
                return staff;
            }
        }
        return null;
    }

    public Staff findExistTeamId(String teamId){
        for (Staff staff : staffs) {
            if (staff.isTeamId(teamId)){
                return staff;
            }
        }
        return null;
    }

    public Staff findStaffByEventId(String eventId) {
        for (Staff staff : staffs) {
            if (staff.isEventId(eventId)){
                return staff;
            }
        }
        return null;
    }
    public StaffList findAllTeam() {
        StaffList staffList = new StaffList();
        for (Staff staff : staffs) {
            Staff exist = staffList.findExistTeamId(staff.getTeamId());
            if (exist == null) {
                staffList.addNewStaff(staff);
            }
        }
        return staffList;
    }

    public ArrayList<Staff> getStaffs() {
        return staffs;
    }
}
