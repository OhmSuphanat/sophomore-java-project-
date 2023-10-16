package cs211.project.services;

import cs211.project.models.Staff;
import cs211.project.models.StaffList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class StaffListFileDatasource implements Datasource<StaffList>{
    private String directoryName;
    private String fileName;

    public StaffListFileDatasource(String directoryName, String fileName) {
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
    public StaffList readData() {
        StaffList staffList = new StaffList();
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
                String displayName = data[1].trim();
                boolean active = data[2].trim().equals("true");
                String teamId = data[3].trim();
                String teamName = data[4].trim();
                String eventId = data[5].trim();
                boolean leader = data[6].trim().equals("true");
                LocalDateTime startDateTime = LocalDateTime.parse(data[7].trim());
                LocalDateTime finishDateTime = LocalDateTime.parse(data[8].trim());


                // เพิ่มข้อมูลลงใน list
                staffList.addNewStaff(id, displayName, active, teamId, teamName, eventId, leader, startDateTime, finishDateTime);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return staffList;
    }

    @Override
    public void writeData(StaffList data) {
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
            for (Staff staff : data.getStaffs()) {
                String line = staff.getId() + "~" + staff.getDisplayName() + "~" + staff.getActive() + "~" + staff.getTeamId() + "~" + staff.getTeamName() + "~" + staff.getEventId() + "~" + staff.getLeader() + "~" + staff.getStartDateTime() + "~" + staff.getFinishDateTime();
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
