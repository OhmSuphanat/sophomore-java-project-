package cs211.project.models;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScheduleList {
    private ArrayList<Schedule> schedules;

    public ScheduleList() {
        schedules = new ArrayList<>();
    }
    public void addNewSchedule(String teamId,String title,LocalDateTime startDateTime,LocalDateTime finishDateTime,String detail,boolean active) {
        title = title.trim();
        if (!title.equals("")) {
            schedules.add(new Schedule(teamId.trim(),title.trim(),startDateTime,finishDateTime,detail.trim(),active));
        }
    }

    public void addNewSchedule(Schedule schedule) {
        schedules.add(schedule);
    }

    public ScheduleList findSchedulesByTeamId(String teamId) {
        ScheduleList scheduleList = new ScheduleList();
        for (Schedule schedule : schedules) {
            if (schedule.isTeamId(teamId)) {
                scheduleList.getSchedules().add(schedule);
            }
        }
        return scheduleList;
    }



    public void remove(Schedule schedule) {
        schedules.remove(schedule);
    }

    public void sortByStartDateTime(Comparator <Schedule> cmp) {
        Collections.sort(schedules, cmp);
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }
}
