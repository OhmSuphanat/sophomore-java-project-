package cs211.project.models;


import java.util.ArrayList;

public class AccountList {
    private ArrayList<Account> accounts;

    public AccountList() {
        accounts = new ArrayList<>();
    }

    public void addNewAccounts(UserList users) {
        accounts.addAll(users.getUsers());
    }
    public void addNewAccounts(AdminList admins){
        accounts.addAll(admins.getAdmins());
    }



    public Account findAccountByUsername(String username) {
        for (Account account : accounts) {
            if (account.isUsername(username)) {
                return account;
            }
        }
        return null;
    }




}