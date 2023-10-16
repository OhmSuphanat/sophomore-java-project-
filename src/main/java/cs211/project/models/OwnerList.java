package cs211.project.models;

import java.util.ArrayList;

public class OwnerList {
    private ArrayList<Owner> owners;

    public OwnerList() {
       owners = new ArrayList<>();
    }

    public void addNewOwnerEvent(String ownerId,String eventId) {
        ownerId = ownerId.trim();
        eventId = eventId.trim();
        Owner exist = findOwnerById(ownerId,eventId);
        if (exist == null) {
            owners.add(new Owner(ownerId, eventId));
        }


    }

    public void addNewOwnerEvent(Owner owner) {
        addNewOwnerEvent(owner.getId(), owner.getEventId());
    }

    public Owner findOwnerById(String ownerId, String eventId) {
        for (Owner owner : owners) {
            if (owner.isOwner(ownerId,eventId)) {
                return owner;
            }
        }
        return null;
    }

    public Owner findOwnerByEventId(String eventId) {
        for (Owner owner : owners) {
            if (owner.isEventId(eventId)) {
                return owner;
            }
        }
        return null;
    }

    public OwnerList findEventsByOwnerId(String ownerId) {
        OwnerList ownerList = new OwnerList();
        for (Owner owner : owners) {
            if (owner.getId().equals(ownerId)) {
                ownerList.addNewOwnerEvent(owner);
            }
        }
        return  ownerList;
    }

    public ArrayList<Owner> getOwners() {
        return owners;
    }
}
