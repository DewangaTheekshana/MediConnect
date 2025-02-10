package lk.oodp2.mediconnect01.model;

public class AppointmentDocter {

    private String id;
    private String UserName;
    private String date;
    private String time;

    public AppointmentDocter(String id, String userName, String date, String time) {
        this.id = id;
        UserName = userName;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
