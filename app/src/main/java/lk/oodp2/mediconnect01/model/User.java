package lk.oodp2.mediconnect01.model;

public class User {

    private String docterId;
    private String docterName;
    private String docterCity;
    private String Price;
    private String rate;
    private String about;
    private String experiance;
    private String location;
    private String mobile;
    private String status;
    private String availibility_time_to;
    private String availibility_time_from;

    public User(String docterId, String docterName, String docterCity, String price, String rate, String about, String experiance, String location, String mobile, String status, String availibility_time_to, String availibility_time_from) {
        this.docterId = docterId;
        this.docterName = docterName;
        this.docterCity = docterCity;
        Price = price;
        this.rate = rate;
        this.about = about;
        this.experiance = experiance;
        this.location = location;
        this.mobile = mobile;
        this.status = status;
        this.availibility_time_to = availibility_time_to;
        this.availibility_time_from = availibility_time_from;
    }

    public String getDocterId() {
        return docterId;
    }

    public void setDocterId(String docterId) {
        this.docterId = docterId;
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

    public String getDocterName() {
        return docterName;
    }

    public void setDocterName(String docterName) {
        this.docterName = docterName;
    }

    public String getDocterCity() {
        return docterCity;
    }

    public void setDocterCity(String docterCity) {
        this.docterCity = docterCity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getExperiance() {
        return experiance;
    }

    public void setExperiance(String experiance) {
        this.experiance = experiance;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
