package cs211.project.models;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserList {
    private ArrayList<User> users;

    public UserList() {
        users = new ArrayList<>();
    }

    public void addNewUser(String id, String username, String password, String displayName, LocalDateTime loginDateTime, String imagePath, boolean active) {
        id = id.trim();
        username = username.trim();
        password = password.trim();
        displayName = displayName.trim();
        User exist = findUserById(id);
        if (exist == null) {
            users.add(new User(id, username, password, displayName, loginDateTime, imagePath, active));
        }
    }

    public User findUserById(String id) {
        for (User user : users) {
            if (user.isId(id)){
                return user;
            }
        }
        return null;
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.isUsername(username)) {
                return user;
            }
        }
        return null;
    }

    public void sortByLoginTime(Comparator<User> cmp) {
        Collections.sort(users, cmp);
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
