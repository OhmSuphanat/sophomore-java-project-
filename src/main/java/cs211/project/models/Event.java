package cs211.project.models;

import java.time.LocalDateTime;

public class Event {
    private String id;
    private String eventName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String owner;
    private String status;
    private String imagePath;
    private String detail;

    public Event(String id, String eventName, LocalDateTime startDateTime, LocalDateTime endDateTime, String owner, String status, String imagePath, String detail) {
        this.id = id;
        this.eventName = eventName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.owner = owner;
        this.status = status;
        this.imagePath = imagePath;
        this.detail = detail;
    }

    public void setStatus(LocalDateTime startDateTime ,LocalDateTime endDateTime) {
        if (LocalDateTime.now().isAfter(endDateTime) && LocalDateTime.now().isBefore(this.startDateTime)) {
            setPreparing();
        }else if (LocalDateTime.now().isAfter(this.startDateTime) && LocalDateTime.now().isBefore(this.endDateTime)) {
            setOnGoingStatus();
        }else if (LocalDateTime.now().isAfter(this.endDateTime)) {
            setDisable();
        } else if (!LocalDateTime.now().isBefore(startDateTime) && !LocalDateTime.now().isAfter(endDateTime)) {
            setJoinAble();
        } else if (LocalDateTime.now().isBefore(startDateTime)){
            setPreparing();
        }

    }

    public String getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getEventName() {
        return eventName;
    }

    public String getStringStartDateTime() {
        String startDate[] = startDateTime.toString().split("T");
        return startDate[0] + "\t" +startDate[1];
    }

    public String getStringFinishDateTime() {
        String endDate[] = endDateTime.toString().split("T");
        return endDate[0] + "\t" + endDate[1];
    }

    public String getStringStartDate() {
        String startDate[] = startDateTime.toString().split("T");
        return startDate[0];
    }

    public String getStringEndDate() {
        String endDate[] = endDateTime.toString().split("T");
        return endDate[0];
    }
    public String getStringStartTime(){
        String startTime[] = startDateTime.toString().split("T");
        return startTime[1].split("\\.")[0];
    }

    public String getStringFinishTime(){
        String endTime[] = endDateTime.toString().split("T");
        return endTime[1].split("\\.")[0];
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getOwner() {
        return owner;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public boolean isId(String id) {
        return id.equals(this.id);
    }

    public void setOnGoingStatus(){
        status = "on-going";
    }

    public void setDisable(){
        status = "disable";
    }

    public void setPreparing() {
        status = "preparing";
    }

    public void setJoinAble() { status = "join-able";}

    public boolean isStatus(String status) {
        return this.status.equals(status);
    }
}