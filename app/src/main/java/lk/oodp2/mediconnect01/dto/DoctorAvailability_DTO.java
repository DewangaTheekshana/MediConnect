package lk.oodp2.mediconnect01.dto;

import java.io.Serializable;

public class DoctorAvailability_DTO implements Serializable {

    private int id;

    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
