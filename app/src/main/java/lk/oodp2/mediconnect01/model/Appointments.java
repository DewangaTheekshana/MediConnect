package lk.oodp2.mediconnect01.model;

public class Appointments {
    private String id;
    private String doctorName;
    private String location;
    private String date;
    private String time;

    public Appointments(String id, String doctorName, String location, String date, String time) {
        this.id = id;
        this.doctorName = doctorName;
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
