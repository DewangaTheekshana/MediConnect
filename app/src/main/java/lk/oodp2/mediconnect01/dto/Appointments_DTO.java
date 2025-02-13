package lk.oodp2.mediconnect01.dto;

import java.io.Serializable;

public class Appointments_DTO implements Serializable{

    private int id;

    private User_DTO user;

    private Doctors_DTO docters;

    private String appointment_date;

    private String appointment_time;

    private String status;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User_DTO getUser() {
        return user;
    }

    public void setUser(User_DTO user_dto) {
        this.user = user_dto;
    }

    public Doctors_DTO getDocters() {
        return docters;
    }

    public void setDocters(Doctors_DTO docters_dto) {
        this.docters = docters_dto;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
