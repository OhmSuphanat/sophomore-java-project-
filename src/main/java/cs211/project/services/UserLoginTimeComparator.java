package cs211.project.services;

import cs211.project.models.User;

import java.util.Comparator;

public class UserLoginTimeComparator implements Comparator<User>{
        @Override
        public int compare(User o1, User o2) {
            return -o1.getLoginDateTime().toString().compareTo(o2.getLoginDateTime().toString());
    }
}
