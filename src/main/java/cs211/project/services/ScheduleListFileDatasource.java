package cs211.project.services;

import cs211.project.models.Schedule;
import cs211.project.models.ScheduleList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class ScheduleListFileDatasource implements Datasource<ScheduleList>{
    private String directoryName;
    private String fileName;
    public ScheduleListFileDatasource(String directoryName,String fileName){
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }
    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ScheduleList readData() {
        ScheduleList scheduleList = new ScheduleList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {
            while ( (line = buffer.readLine()) != null ){
                if (line.equals("")) continue;

                String[] data = line.split("~");
                String teamID = data[0].trim();
                String title = data[1].trim();
                LocalDateTime startDateTime = LocalDateTime.parse(data[2]);
                LocalDateTime finishDateTime = LocalDateTime.parse(data[3]);
                String detail = data[4].trim();
                boolean active = data[5].equals("true");


                scheduleList.addNewSchedule(teamID,title,startDateTime,finishDateTime,detail,active);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return scheduleList;
    }

    @Override
    public void writeData(ScheduleList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                fileOutputStream,
                StandardCharsets.UTF_8
        );
        BufferedWriter buffer = new BufferedWriter(outputStreamWriter);

        try {
            // create and write CSV
            for (Schedule schedule : data.getSchedules()) {
                String line = schedule.getTeamId() + "~" + schedule.getTitle() + "~" + schedule.getStartDateTime().toString() + "~" + schedule.getFinishDateTime().toString() + "~" + schedule.getDetail() + "~"+schedule.getActive();
                buffer.append(line);
                buffer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.flush();
                buffer.close();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }

    }
}
