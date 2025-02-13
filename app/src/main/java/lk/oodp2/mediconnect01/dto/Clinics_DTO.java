package lk.oodp2.mediconnect01.dto;

import java.io.Serializable;

public class Clinics_DTO implements Serializable {

    private int id;

    private Doctors_DTO docters;

    private String clinic_name;

    private String clinic_address;

    private String clinic_city;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Doctors_DTO getDocters() {
        return docters;
    }

    public void setDocters(Doctors_DTO docters_dto) {
        this.docters = docters_dto;
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


}



