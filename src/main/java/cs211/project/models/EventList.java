package cs211.project.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventList {

    private ArrayList<Event> events;

    public EventList() {
        events = new ArrayList<>();
    }

    public void addNewEvent(String id, String eventName, LocalDateTime startDateTime, LocalDateTime endDateTime, String owner, String status, String imagePath, String detail) {
        Event exist = findEventById(id);
        if (exist == null) {
            events.add(new Event(id, eventName, startDateTime,  endDateTime, owner,status,imagePath,detail));
        }

    }

    public void addNewEvent(Event event){
        events.add(event);
    }


    public Event findEventById(String id){
        for (Event event: events){
            if (event.isId(id)){
                return event;
            }
        }
        return null;
    }
    public EventList findEventsByOwnerList(OwnerList ownerList){
        EventList eventList = new EventList();
        for(Owner owner : ownerList.getOwners()){
            eventList.addNewEvent(this.findEventById(owner.getEventId()));
        }
        return  eventList;
    }

    public EventList findEventListByJoiningUser(ParticipantList participantList, StaffList staffList) {
        EventList eventList = new EventList();
        for (Participant participant : participantList.getParticipants()) {
            eventList.addNewEvent(findEventById(participant.getEventId()));
        }

        for (Staff staff : staffList.getStaffs()) {
            eventList.addNewEvent(findEventById(staff.getEventId()));
        }
        return  eventList;
    }

    public void setStatus(ParticipantList participantList) {
        for (Event event : events) {
            event.setStatus(participantList.findParticipantsByEventId(event.getId()).getParticipants().get(0).getStartDateTime(),participantList.findParticipantsByEventId(event.getId()).getParticipants().get(0).getEndDateTime());
        }
    }
    public EventList findEventsNotDisable() {
        EventList eventList = new EventList();
        for (Event event : events) {
            if (!event.isStatus("disable")) {
                eventList.addNewEvent(event);
            }
        }
        return eventList;
    }

    public EventList findDisableEventList() {
        EventList eventList = new EventList();
        for (Event event : events) {
            if (event.isStatus("disable")) {
                eventList.addNewEvent(event);
            }
        }
        return eventList;
    }

    public void sortByEndDateTime() {
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return -o1.getEndDateTime().toString().compareTo(o2.getEndDateTime().toString());
            }
        });
    }

    public ArrayList<Event> getEventList() {
        return events;
    }
}
