package cs211.project.models;

import java.time.LocalDateTime;

public class Chat {
    private String userId;
    private String teamId;
    private String message;
    private LocalDateTime sentDateTimeText;


    public Chat(String userId, String teamId, String message, LocalDateTime sentText) {
        this.userId = userId;
        this.teamId = teamId;
        this.message = message;
        this.sentDateTimeText = sentText;
    }

    public String getUserId() {
        return userId;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getSentText(){
        return sentDateTimeText;
    }

    public Boolean isTeamId(String teamId){
        return this.teamId.equals(teamId);
    }
}
