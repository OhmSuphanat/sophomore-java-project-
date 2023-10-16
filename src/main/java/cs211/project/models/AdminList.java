package cs211.project.models;

import java.util.ArrayList;

public class AdminList {
    private ArrayList<Admin> admins;

    public AdminList() {
        admins = new ArrayList<>();
    }

    public void addNewAdmin(String id,String username, String password) {
        id = id.trim();
        username = username.trim();
        password = password.trim();
        Admin exist = findAdminById(id);
        if (exist == null) {
            admins.add(new Admin(id, username, password));
        }
    }

    public Admin findAdminById(String id) {
        for (Admin admin : admins) {
            if (admin.isId(id)){
                return admin;
            }
        }
        return null;
    }

    public ArrayList<Admin> getAdmins() {
        return admins;
    }
}