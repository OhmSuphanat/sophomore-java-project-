package cs211.project.application;

import javafx.application.Application;
import javafx.stage.Stage;
import cs211.project.services.FXRouter;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        configRoute();
        stage.setResizable(false);
        FXRouter.bind(this, stage, "CS211 661 Project", 1024, 768);
        FXRouter.goTo("welcome");
    }

    public static void configRoute() {
        String resourcesPath = "cs211/project/views/";
        FXRouter.when("sign-up", resourcesPath + "sign-up-view.fxml", 1024, 768);
        FXRouter.when("welcome", resourcesPath + "welcome-view.fxml", 1024, 768);
        FXRouter.when("credit", resourcesPath + "credit-view.fxml", 1024, 768);
        FXRouter.when("profile", resourcesPath + "profile-view.fxml", 1024, 768);
        FXRouter.when("change-password", resourcesPath + "change-password-view.fxml", 1024, 768);
        FXRouter.when("event-creator", resourcesPath + "event-creator-view.fxml", 1024, 768);
        FXRouter.when("admin-account-list",resourcesPath + "admin-account-list-view.fxml", 1024, 768);
        FXRouter.when("event-list",resourcesPath+"event-list-view.fxml", 1024, 768);
        FXRouter.when("account-hub",resourcesPath+"account-hub-view.fxml", 1024, 768);
        FXRouter.when("admin-setting",resourcesPath+"admin-setting-view.fxml", 1024, 768);
        FXRouter.when("admin-change-password",resourcesPath+"admin-change-password-view.fxml", 1024, 768);
        FXRouter.when("event-setting", resourcesPath + "event-setting-view.fxml", 1024, 768);
        FXRouter.when("your-event", resourcesPath + "your-event-view.fxml", 1024, 768);
        FXRouter.when("schedule-list", resourcesPath + "schedule-list-view.fxml", 1024, 768);
        FXRouter.when("staff-list", resourcesPath + "staff-list-view.fxml", 1024, 768);
        FXRouter.when("team-schedule-list", resourcesPath + "team-schedule-list-view.fxml", 1024, 768);
        FXRouter.when("owner-team-chat", resourcesPath + "owner-team-chat.fxml", 1024, 768);
        FXRouter.when("participant-list", resourcesPath + "participant-list-view.fxml", 1024, 768);
        FXRouter.when("event-detail",resourcesPath + "event-detail-view.fxml",1024,768);
        FXRouter.when("participant-schedule-list",resourcesPath + "participant-schedule-list-view.fxml",1024,768);
        FXRouter.when("staff",resourcesPath + "staff-view.fxml",1024,768);
        FXRouter.when("leader",resourcesPath + "leader-view.fxml",1024,768);
    }


    public static void main(String[] args) {
        launch();
    }
}