package cs211.project.models;

import java.time.LocalDateTime;

public class Staff extends User{
    private String teamId;
    private String teamName;
    private String eventId;
    private boolean leader;
    private LocalDateTime startDateTime;
    private LocalDateTime finishDateTime;

    public Staff(String id, String displayName, boolean active, String teamId, String teamName, String eventId, boolean leader, LocalDateTime startDateTime, LocalDateTime finishDateTime) {
        super(id, displayName, active);
        this.teamId = teamId;
        this.teamName = teamName;
        this.eventId = eventId;
        this.leader = leader;
        this.startDateTime = startDateTime;
        this.finishDateTime = finishDateTime;
    }

    public boolean activate(Staff staff) {
        if (isLeader()) {
            staff.activate();
            return true;
        }else {
            return false;
        }
    }

    public boolean deActivate(Staff staff) {
        if (isLeader()) {
            staff.deactivate();
            return true;
        }else {
            return false;
        }
    }

    public void promote() {
        leader = true;
    }
    public void dePromote() {
        leader = false;
    }

    public boolean isStaff(String userId, String teamId, String eventId) {
        return this.id.equals(userId) && this.teamId.equals(teamId) && this.eventId.equals(eventId);
    }

    public boolean isTeamName(String teamName) {
        return this.teamName.equals(teamName);
    }

    public boolean isTeamId(String teamId) {
        return  this.teamId.equals(teamId);
    }

    public boolean isEventId(String eventId) {
        return this.eventId.equals(eventId);
    }

    public String getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getEventId() {
        return eventId;
    }

    public String getLeader() { return leader?"true":"false";}

    public String getStringStartDate() {
        String startDate[] = startDateTime.toString().split("T");
        return startDate[0];
    }
    public String getStringFinishDate(){
        String endDate[] = finishDateTime.toString().split("T");
        return endDate[0];
    }

    public String getStringStartTime(){
        String startTime[] = startDateTime.toString().split("T");
        return startTime[1].split("\\.")[0];
    }
    public String getStringFinishTime(){
        String endTime[] = finishDateTime.toString().split("T");
        return endTime[1].split("\\.")[0];
    }

    public String getStringStartDateTime() {
        String startDate[] = startDateTime.toString().split("T");
        return startDate[0] + "\t" +startDate[1];
    }

    public String getStringFinishDateTime() {
        String endDate[] = finishDateTime.toString().split("T");
        return endDate[0] + "\t" + endDate[1];
    }


    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getFinishDateTime() {
        return finishDateTime;
    }

    public String getStatus() {
        return isActive() ? "common" : "banned";
    }

    public String getStringLeader() {
        return isLeader() ? "leader" : "common";
    }

    public boolean isLeader() {
        return leader;
    }

    public void setId(String userId) {
        id = userId;
    }

    public void linkUser(User user) {
        id = user.getUsername();
        displayName = user.getDisplayName();
    }

}
