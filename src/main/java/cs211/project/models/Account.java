package cs211.project.models;

public class Account{
    protected String id;
    protected String username;
    protected String password;

    public Account(String id,String username,String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }
    public Account(String id){
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUsername(String username){
        return this.username.equals(username);
    }
    public boolean isId(String id){
        return this.id.equals(id);
    }
}