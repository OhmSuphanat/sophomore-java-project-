package cs211.project.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ParticipantList {
    private ArrayList<Participant> participants;

    public ParticipantList() {
        participants = new ArrayList<>();
    }

    public void addNewParticipant(String userId, String displayName ,boolean active,String eventId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        userId = userId.trim();
        eventId = eventId.trim();
        participants.add(new Participant(userId, displayName,active, eventId, startDateTime, endDateTime));
    }

    public void addNewParticipant(Participant participant) {
        participants.add(participant);
    }

    public void bookNewParticipants(String eventId, int amount, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        eventId = eventId.trim();
        for (int i=0;i<amount;i++) {
            participants.add(new Participant("","",true, eventId, startDateTime, endDateTime));
        }
    }

    public Participant findParticipant(String id, String eventId) {
        for (Participant participant : participants) {
            if (participant.isParticipant(id,eventId)){
                return participant;
            }
        }
        return null;
    }

    public ParticipantList findParticipantByUserId(String userId) {
        ParticipantList participantList = new ParticipantList();
        for (Participant participant : participants) {
            if (participant.getId().equals(userId))
                participantList.addNewParticipant(participant);
        }
        return participantList;
    }

    public ParticipantList findParticipantsByEventId(String eventId) {
        ParticipantList participantList = new ParticipantList();
        for (Participant participant : participants) {
            if (participant.getEventId().equals(eventId))
                participantList.addNewParticipant(participant);
        }
        return participantList;
    }

    public ParticipantList findExistParticipantList() {
        ParticipantList participantList = new ParticipantList();
        for (Participant participant : participants) {
            if (!participant.getId().equals("")){
                participantList.addNewParticipant(participant);
            }
        }
        return participantList;
    }

    public Participant findEmptyParticipant(String eventId) {
        for (Participant participant : participants) {
            if (participant.isParticipant("",eventId)) {
                return participant;
            }
        }
        return null;
    }

    public Participant findParticipantByEventId(String eventId) {
        for (Participant participant : participants) {
            if (participant.isEventId(eventId)) {
                return participant;
            }
        }
        return null;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }
}
