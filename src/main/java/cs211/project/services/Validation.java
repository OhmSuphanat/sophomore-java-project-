package cs211.project.services;

public final class Validation {
    //Login
    public static String isValidUsername(String username) {
        String regex = "^[A-z0-9_][A-z0-9_]{5,32}$";
        if (username.matches(regex)) {
            return "";
        }
        return "Username is Invalid.";
    }

    public static String isValidDisplayName(String displayName) {
        String regex = "^[A-z0-9_][A-z0-9_\\s]{4,31}[A-z0-9_]$";
        if (displayName.matches(regex)) {
            return "";
        }
        return "Display Name is Invalid.";
    }

    public static String isValidPassword(String password) {
        String regex = "^[A-z0-9_][A-z0-9_]{5,32}$";
        if (password.matches(regex)) {
            return "";
        }
        return "Password is Invalid.";
    }

    public static String isMatched(String ref, String comp) {
        return ref.equals(comp) ? "" : "Password doesn't match.";
    }

    //EventCreator
    public static String isAllowName(String eventName) {
        String regex = "^[A-Za-z0-9].{2,31}";
            if (eventName.matches(regex) && !eventName.contains("~")) {
                return "";
            }
            return "It must contain at least 3 letters and\nbe either alphabet or a number.";
    }

    public static String isTime(String time) {
        String regex = "^([01]\\d|2[0-3]):[0-5]\\d$";
        if (time.matches(regex)){
            return "";
        }
        return "Invalid time format [HH:MM].";
    }

    public static String isDetail(String detail){
        if (!detail.equals("") && !detail.contains("~")){
            return "";
        }
        return "Please enter a detail or correct detail.";
    }

    public static String isDate(String date){
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        if (date.matches(regex)){
            return "";
        }
        return "Please pick valid date.";
    }

    public static String isAllowAmount(String amount){
        if (amount.equals("")){
            amount = "-1";
        }
        if (amount.matches("[0-9]+")){
            return "";
        }
        return "Please enter valid number.";
    }


}
