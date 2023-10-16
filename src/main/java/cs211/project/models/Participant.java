package cs211.project.models;

import java.time.LocalDateTime;

public class Participant extends User{
    private String eventId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public Participant(String id, String displayName, boolean active, String eventId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(id, displayName, active);
        this.eventId = eventId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public String getEventId() {
        return eventId;
    }

    public boolean isParticipant(String id, String eventId) {
        return this.id.equals(id) && this.eventId.equals(eventId);
    }

    public String getStringDateTime(LocalDateTime localDateTime) {
        String split[] = localDateTime.toString().split("T");
        return split[0] + "\t" + split[1];
    }

    public void linkUser(User user) {
        id = user.getUsername();
        displayName = user.getDisplayName();
    }


    public String getStatus() {
        return isActive() ? "common" : "banned";
    }

    public boolean isEventId(String eventId) {
        return this.eventId.equals(eventId);
    }
}


