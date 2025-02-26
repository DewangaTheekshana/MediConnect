package lk.oodp2.mediconnect01.dto;

import java.io.Serializable;

public class Appointments_DTO implements Serializable{

    private int id;

    private String user_id;

    private String docters;

    private String docters_id;

    private String location;

    private String appointment_date;

    private String appointment_time;

    private String status;

    private String doctor_Availability_id;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDocters() {
        return docters;
    }

    public void setDocters(String docters) {
        this.docters = docters;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctor_Availability_id() {
        return doctor_Availability_id;
    }

    public void setDoctor_Availability_id(String doctor_Availability_id) {
        this.doctor_Availability_id = doctor_Availability_id;
    }

    public String getDocters_id() {
        return docters_id;
    }

    public void setDocters_id(String docters_id) {
        this.docters_id = docters_id;
    }
}
