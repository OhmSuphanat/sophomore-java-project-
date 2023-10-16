package cs211.project.models;

import java.time.LocalDateTime;

public class User extends Account{
    protected String displayName;
    private LocalDateTime loginDateTime;
    private String imagePath;
    protected boolean active;

    public User(String id, String username, String password, String displayName, LocalDateTime loginDateTime, String imagePath, boolean active){
        super(id, username, password);
        this.displayName = displayName;
        this.loginDateTime = loginDateTime;
        this.imagePath = imagePath;
        this.active = active;
    }


    public User(String id){
        super(id);
    }

    public User(String id, String displayName,boolean active) {
        super(id);
        this.displayName = displayName;
        this.active = active;
    }


    public void deactivate() {
        active = false;
    }

    public void activate() {
        active = true;
    }


    public LocalDateTime getLoginDateTime() {
        return loginDateTime;
    }

    public String getLoginDateTimeString() { return getLoginTime() + "\t" + getLoginDate();}

    public String getLoginTime() {
        return loginDateTime.toString().split("T")[1].split("\\.")[0];
    }

    public String getLoginDate() {
        return loginDateTime.toString().split("T")[0];
    }


    public String getImagePath() {
        return imagePath;
    }

    public String getDisplayName() {
        return displayName;
    }
    public void setUsername(String username){
        this.username = username;
    }


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setLoginDateTime(LocalDateTime loginDateTime) {
        this.loginDateTime = loginDateTime;
    }

    public boolean isActive() {
        return active;
    }

    public String getActive(){
        return isActive()?"true":"false";
    }





}

