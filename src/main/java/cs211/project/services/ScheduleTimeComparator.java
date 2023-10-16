package cs211.project.services;

import cs211.project.models.Schedule;

import java.util.Comparator;

public class ScheduleTimeComparator implements Comparator<Schedule> {
    @Override
    public int compare(Schedule o1, Schedule o2) {
        return o1.getStartDateTime().toString().compareTo(o2.getStartDateTime().toString());
    }
}
