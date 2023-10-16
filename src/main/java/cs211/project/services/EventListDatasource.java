package cs211.project.services;

import cs211.project.models.Event;
import cs211.project.models.EventList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class EventListDatasource implements Datasource<EventList>{
    private String directoryName;
    private String fileName;

    public EventListDatasource(String directoryName, String fileName) {
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
    public EventList readData() {
        EventList eventList = new EventList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // เตรียม object ที่ใช้ในการอ่านไฟล์
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
            // ใช้ while loop เพื่ออ่านข้อมูลรอบละบรรทัด
            while ( (line = buffer.readLine()) != null ){
                // ถ้าเป็นบรรทัดว่าง ให้ข้าม
                if (line.equals("")) continue;

                // แยกสตริงด้วย ,
                String[] data = line.split("~");

                // อ่านข้อมูลตาม index แล้วจัดการประเภทของข้อมูลให้เหมาะสม
                String id = data[0].trim();
                String eventName = data[1].trim();
                LocalDateTime startDateTime = LocalDateTime.parse(data[2].trim());
                LocalDateTime endDateTime = LocalDateTime.parse(data[3].trim());
                String owner = data[4].trim();
                String status = data[5].trim();
                String imagePath = data[6].trim();
                String detail = data[7].trim();


                // เพิ่มข้อมูลลงใน list
                eventList.addNewEvent(id, eventName, startDateTime, endDateTime, owner, status, imagePath, detail);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return eventList;
    }

    @Override
    public void writeData(EventList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // prepare file for reading
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
            for (Event event : data.getEventList()) {
                String line = event.getId() + "~" + event.getEventName() + "~" + event.getStartDateTime().toString() + "~" + event.getEndDateTime().toString() + "~" +
                              event.getOwner() + "~" + event.getStatus() + "~" + event.getImagePath() + "~" + event.getDetail();
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
