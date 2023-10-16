package cs211.project.models;

public class Admin extends Account {
    public Admin(String id,String username, String password) {
        super(id, username, password);
    }



    public void deactivate(User user) {
        user.deactivate();
    }

    public void activate(User user) {
        user.activate();
    }
}
