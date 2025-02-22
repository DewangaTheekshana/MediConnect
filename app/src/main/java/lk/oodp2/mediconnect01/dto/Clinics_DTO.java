package lk.oodp2.mediconnect01.dto;

import java.io.Serializable;

public class Clinics_DTO implements Serializable {

    private int id;

    private Doctors_DTO docters;

    private String clinic_name;

    private String clinic_address;

    private String clinic_city;

    private Object Doctor_Availability_id;
    private Object Doctor_Availability_status;

    private String first_name;

    private String last_name;

    private String email;

    private String password;

    private String mobile;

//    private String doctor_identity_front;
//
//    private String doctor_identity_back;

    private String appointment_price;

    private String about;

//    private String profile_pic;

    private String experience;

    private String rate;

    private String availibility_time_to;

    private String availibility_time_from;


    public Clinics_DTO(String experience, int id, Doctors_DTO docters, String clinic_name, String clinic_address, String clinic_city, Object doctor_Availability_id, Object doctor_Availability_status, String first_name, String last_name, String email, String password, String mobile, String appointment_price, String about, String rate, String availibility_time_to, String availibility_time_from) {
        this.experience = experience;
        this.id = id;
        this.docters = docters;
        this.clinic_name = clinic_name;
        this.clinic_address = clinic_address;
        this.clinic_city = clinic_city;
        Doctor_Availability_id = doctor_Availability_id;
        Doctor_Availability_status = doctor_Availability_status;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.appointment_price = appointment_price;
        this.about = about;
        this.rate = rate;
        this.availibility_time_to = availibility_time_to;
        this.availibility_time_from = availibility_time_from;
    }

    public String getAvailibility_time_to() {
        return availibility_time_to;
    }

    public void setAvailibility_time_to(String availibility_time_to) {
        this.availibility_time_to = availibility_time_to;
    }

    public String getAvailibility_time_from() {
        return availibility_time_from;
    }

    public void setAvailibility_time_from(String availibility_time_from) {
        this.availibility_time_from = availibility_time_from;
    }

    public String getAppointment_price() {
        return appointment_price;
    }

    public void setAppointment_price(String appointment_price) {
        this.appointment_price = appointment_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Doctors_DTO getDocters() {
        return docters;
    }

    public void setDocters(Doctors_DTO docters) {
        this.docters = docters;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public String getClinic_address() {
        return clinic_address;
    }

    public void setClinic_address(String clinic_address) {
        this.clinic_address = clinic_address;
    }

    public String getClinic_city() {
        return clinic_city;
    }

    public void setClinic_city(String clinic_city) {
        this.clinic_city = clinic_city;
    }

    public Object getDoctor_Availability_id() {
        return Doctor_Availability_id;
    }

    public void setDoctor_Availability_id(Object doctor_Availability_id) {
        Doctor_Availability_id = doctor_Availability_id;
    }

    public Object getDoctor_Availability_status() {
        return Doctor_Availability_status;
    }

    public void setDoctor_Availability_status(Object doctor_Availability_status) {
        Doctor_Availability_status = doctor_Availability_status;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}



