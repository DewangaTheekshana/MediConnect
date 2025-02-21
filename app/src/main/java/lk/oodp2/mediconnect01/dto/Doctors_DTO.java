package lk.oodp2.mediconnect01.dto;

public class Doctors_DTO {

    private int id;

    private DoctorAvailability_DTO Doctor_Availability;

    private DoctorCity_DTO docter_city_id;

    private String first_name;

    private String last_name;

    private String email;

    private String password;

    private String mobile;

    private String doctor_identity_front;

    private String doctor_identity_back;

    private String appointment_price;

    private String about;

    private String profile_pic;

    private String experience;

    private String clinic_name;

    private String clinic_address;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DoctorAvailability_DTO getDoctor_Availability() {
        return Doctor_Availability;
    }

    public void setDoctor_Availability(DoctorAvailability_DTO Doctor_Availability_dto) {
        this.Doctor_Availability = Doctor_Availability_dto;
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

    public String getDoctor_identity_front() {
        return doctor_identity_front;
    }

    public void setDoctor_identity_front(String doctor_identity_front) {
        this.doctor_identity_front = doctor_identity_front;
    }

    public String getDoctor_identity_back() {
        return doctor_identity_back;
    }

    public void setDoctor_identity_back(String doctor_identity_back) {
        this.doctor_identity_back = doctor_identity_back;
    }

    public String getAppointment_price() {
        return appointment_price;
    }

    public void setAppointment_price(String appointment_price) {
        this.appointment_price = appointment_price;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
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

    public DoctorCity_DTO getDocter_city_id() {
        return docter_city_id;
    }

    public void setDocter_city_id(DoctorCity_DTO docter_city_id) {
        this.docter_city_id = docter_city_id;
    }
}
