package cs211.project.models;

import java.time.LocalDateTime;

public class Schedule {
    private String teamId;
    private String title;
    private LocalDateTime startDateTime;
    private LocalDateTime finishDateTime;
    private String detail;
    private boolean active;

    public Schedule(String teamID,String title,LocalDateTime startDateTime,LocalDateTime finishDateTime,String detail,boolean active){
        this.teamId = teamID;
        this.title = title;
        this.startDateTime = startDateTime;
        this.finishDateTime = finishDateTime;
        this.detail = detail;
        this.active = active;
    }
    public void deactivate() {
        active = false;
    }

    public void activate() {
        active = true;
    }
    public String getTeamId(){
        return teamId;
    }
    public String getTitle(){
        return title;
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

    public String getDetail(){
        return detail;
    }
    public String getActive(){
        return isActive()?"true":"false";
    }

    public String getStatus() {
        return isActive() ? "on-going" : "complete" ;
    }

    public void setActive() {
        if (isActive()) {
            deactivate();
        }else {
            activate();
        }
    }

    public boolean isActive(){
        return active;
    }

    public boolean isTeamId(String teamId) {
        return this.teamId.equals(teamId);
    }


}
