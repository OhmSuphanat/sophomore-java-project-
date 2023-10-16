# Project A-Event
## cs211-661-project

## รายชื่อสมาชิก a-pang
* ศุภณัฐ สร้อยเพชร 6510450976
* ษริกา โอภาสพิชญดำรง 6510450984
* พจนินท์ วชิรวิทยากุล 6510450691
* ณัฐดนัย เอกสันตอ 6510450330

## วิธีการติดตั้งหรือรันโปรแกรม
สามารถ download โปรแกรม ได้ที่ Release 1.0.6 ชื่อไฟล์ A-Event.zip
* หลังจาก download ไฟล์ .zip แล้วให้คลิกขวา แล้ว Extract ไฟล์ออกมา 
* เมื่อกดเข้า folder ที่ Extract ออกมาแล้ว จะสังเกตเห็น 2 folders
    - macOS
    - Windows
* เลือก folder ระบบปฏิบัติการให้ตรงกับคอมพิวเตอร์ของท่าน
* เมื่อเข้ามาแล้ว ให้เปิด terminal แล้ว path terminal มาที่ตำแหน่ง folder ณ ปัจจุบัน
* พิมพ์คำสั่งใน terminal ตามนี้ ->            java -jar a-event.jar
* โปรแกรมจะรันขึ้นมาแล้วใช้งานได้ตามปกติ (ถ้าต้องการเปิดโปรแกรมใหม่อีกครั้งต้องพิมพ์คำสั่งข้างบนเท่านั้น)
* สามารถกดเพื่อดู คู่มือการใช้โปรแกรม ได้ใน folder ที่รันโปรแกรมเลย
## การวางโครงสร้างไฟล์
- data : file csv ต่างๆ
    - admin-list.csv
    - chat-list.csv      
    - event-list.csv     
    - owner-list.csv
    - participant-list.csv
    - schedule-list.csv
    - staff-list.csv
    - user-list.csv
- images : file image เริ่มต้น และ uploaded image
    - default-event-image.png
    - default-profile-image.jpg
- src
    - main
        - java
            - cs211.project
                - application
                    - MainApplication.java
                - controllers
                    - AccountHubController.java
                    - AdminAccountListController.java
                    - AdminChangePasswordController.java
                    - AdminSettingController.java
                    - ChangePasswordController.java
                    - CompleteEventListController.java
                    - CreditController.java
                    - EventCreatorController.java
                    - EventDetailController.java
                    - EventListController.java
                    - EventSettingController.java
                    - LeaderController.java
                    - OwnerTeamChatController.java
                    - ParticipantListController.java
                    - ParticipantScheduleListController.java
                    - ProfileController.java
                    - ScheduledEventListController.java
                    - ScheduleListController.java
                    - SignUpController.java
                    - StaffController.java
                    - TeamScheduleListController.java
                    - TeamStaffListController.java
                    - WelcomeController.java
                    - YourEventListController.java
                - models
                    - Account.java
                    - AccountList.java
                    - Admin.java
                    - AdminList.java
                    - Chat.java
                    - ChatList.java
                    - Event.java
                    - EventList.java
                    - Owner.java
                    - OwnerList.java
                    - Participant.java
                    - ParticipantList.java
                    - Schedule.java
                    - ScheduleList.java
                    - Staff.java
                    - StaffList.java
                    - User.java
                    - UserList.java
                - services
                    - AdminListFileDatasource.java
                    - ChatListFileDatasource.java
                    - Datasource.java
                    - EventListDatasource.java
                    - FXRouter.java
                    - OwnerListFileDatasource.java
                    - ParticipantListFileDatasource.java
                    - ScheduleListFileDatasource.java
                    - ScheduleTimeComparator.java
                    - StaffListFileDatasource.java
                    - UserListFileDatasource.java
                    - UserLoginTimeComparator.java
                    - Validation.java
                - Main.java
            - module-info.java
        - resources
            - cs221.project.views
                - account-hub-view.fxml
                - admin-account-list-view.fxml
                - admin-change-password-view.fxml
                - admin-setting-view.fxml
                - change-password-view.fxml
                - complete-event-list-view.fxml
                - credit-view.fxml
                - event-creator-view.fxml
                - event-detail-view.fxml
                - event-list-view.fxml
                - event-setting-view.fxml
                - leader-view.fxml
                - owner-team-chat.fxml
                - participant-list-view.fxml
                - participant-schedule-list-view.fxml
                - profile-view.fxml
                - schedule-list-view.fxml
                - scheduled-event-list.fxml
                - sign-up-view.fxml
                - staff-list-view.fxml
                - staff-view.fxml
                - team-schedule-list-view.fxml
                - team-staff-list-view.fxml
                - welcome-view.fxml
                - your-event-view.fxml
            - images
                - admin-logout.png
                - admin-profile.png
                - admin-setting.png
                - admit-password.png
                - aof.png
                - back.png
                - background.png
                - change-password.png
                - createEvent.png
                - event.png
                - eventsetting.png
                - handbook.png
                - kong.png
                - logging.png
                - logout.png
                - ohm.png
                - pang.png
                - profile.png
                - searchIcon.png
                - setting-account.png
                - ticket.png
- uml
    - controllers-UML.png
    - models-UML.png
    - services-UML.png
- handbook.pdf

## ตัวอย่างข้อมูลผู้ใช้ระบบ

1. Admin (ผู้ใช้ไม่สามารถสมัครบัญชีที่เป็น admin แต่จะมีบัญชี admin ให้สำหรับผู้ใช้งาน role นี้)
    - Username: admin, Password: 123456

2. User ปกติ
    - Participant
        - Participant ปกติ
            - Username: Lalisa, Password: blackpink
        - Participant ที่โดนแบน
            -Username: Pinpawat, Password: title10
    - Staff
        - Staff ปกติ
            - Username: Wonyoung, Password: iz_one
        - Staff ที่โดนแบน
            - Username: steven, Password: Football
        - Staff ที่เป็น Leader
            - Username: anonymous, Password: guessX

3. User ที่โดนแบน 
    - Username: Roseanne, Password: 11021997

4.  User ที่มี Event เป็นของตัวเอง
    - Username: TaylorSwift, Password: 12131989

* ส่วนข้อมูลเริ่มต้นในการทดสอบโปรแกรม มีอยู่ในโปรแกรมเรียบร้อยแล้ว

## สรุปสิ่งที่พัฒนาแต่ละครั้งที่นำเสนอความก้าวหน้าของระบบ รวมครั้งส่งโครงงานที่สมบูรณ์
* ครั้งที่ 1
    * ศุภณัฐ สร้อยเพชร (Admin page + หน้า EventList)
    * ษริกา โอภาสพิชญดำรง (Welcome page + SignIn page)
    * พจนินท์ วชิรวิทยากุล (Form page + สร้าง Event page)
    * ณัฐดนัย เอกสันตอ (Home page + ChangePassword page + Profile page)

* ครั้งที่ 2 
    * ศุภณัฐ สร้อยเพชร (ระบบ Login + ระบบ สร้าง Account)
    * ษริกา โอภาสพิชญดำรง (ระบบ Admin)
    * พจนินท์ วชิรวิทยากุล (ระบบ Event)
    * ณัฐดนัย เอกสันตอ (ระบบ Schedules Event)

* ครั้งที่ 3
    * ศุภณัฐ สร้อยเพชร (เปลี่ยนระบบ Login เป็นแบบ inherit แยก Admin กับ User + ระบบ จัดการผู้เข้าร่วม event + ระบบ team)
    * ษริกา โอภาสพิชญดำรง (ระบบ Admin แบน user + Admin ChangePassword + Credit)
    * พจนินท์ วชิรวิทยากุล (ระบบ Chat +  ระบบสร้างและแก้ไข Event + เปิดรับผู้เข้าร่วม)
    * ณัฐดนัย เอกสันตอ (เปลี่ยน Account เป็นรูปแบบ inherit + ระบบ Profile + ระบบ Join Event)

* ครั้งส่งโครงงานที่สมบูรณ์
    * ศุภณัฐ สร้อยเพชร (Clear No Usage code + แสดง Schedule ของ ผู้เข้าร่วม + Staff's page + Leader's Page)
    * ษริกา โอภาสพิชญดำรง (คู่มือของโปรแกรม + ข้อมูลทดสอบโปรแกรม)
    * พจนินท์ วชิรวิทยากุล (แก้การ polymorphism Interface)
    * ณัฐดนัย เอกสันตอ (ระบบ สามารถเข้าร่วม Event + แสดง Event ต่างๆที่เข้าร่วม ทั้งที่เป็น เจ้าของ, ผู้เข้าร่วมทั่วไป, staff, เจ้าของEvent)