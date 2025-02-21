package lk.oodp2.mediconnect01.model;

public class AppointmentDocter {

    private String id;
    private String UserName;
    private String date;
    private String time;
    private String doctor_Availability_id;
    private String status;

    public AppointmentDocter(String id, String userName, String date, String time, String doctor_Availability_id, String status) {
        this.id = id;
        UserName = userName;
        this.date = date;
        this.time = time;
        this.doctor_Availability_id = doctor_Availability_id;
        this.status = status;
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

    public String getDoctor_Availability_id() {
        return doctor_Availability_id;
    }

    public void setDoctor_Availability_id(String doctor_Availability_id) {
        this.doctor_Availability_id = doctor_Availability_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
