package lk.oodp2.mediconnect01.model;

public class User {

    private String docterId;
    private String docterName;
    private String docterCity;
    private String Price;

    public User(String docterId, String docterName, String docterCity, String price) {
        this.docterId = docterId;
        this.docterName = docterName;
        this.docterCity = docterCity;
        Price = price;
    }

    public String getDocterId() {
        return docterId;
    }

    public void setDocterId(String docterId) {
        this.docterId = docterId;
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
}
