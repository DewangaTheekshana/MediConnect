package lk.oodp2.mediconnect01.model;

public class City {

    private int id;
    private String city;

    public City(int id, String city) {
        this.id = id;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
