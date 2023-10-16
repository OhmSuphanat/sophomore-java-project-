package cs211.project.models;

public class Owner extends User{
    private String eventId;
    public Owner(String id, String eventId) {
        super(id);
        this.eventId = eventId;
    }

    public void deactivate(Participant participant) {
        participant.deactivate();
    }

    public void activate(Participant participant) {
        participant.activate();
    }

    public void deactivate(Staff staff) {
        staff.deactivate();
    }

    public void activate(Staff staff) {
        staff.activate();
    }

    public void promote(Staff staff) {
        staff.promote();
    }

    public void dePromote(Staff staff) {
        staff.dePromote();
    }

    public String getEventId() {
        return eventId;
    }

    public boolean isOwner(String ownerId, String eventId) {
        return this.id.equals(ownerId) && this.eventId.equals(eventId);
    }

    public boolean isEventId(String eventId) {
        return this.eventId.equals(eventId);
    }
}
