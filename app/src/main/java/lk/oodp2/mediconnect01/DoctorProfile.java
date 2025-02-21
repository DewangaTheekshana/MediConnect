package lk.oodp2.mediconnect01;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import lk.oodp2.mediconnect01.dto.Doctors_DTO;

public class DoctorProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textView35 = findViewById(R.id.textView35);
        EditText edit_text1 = findViewById(R.id.edit_text1);
        EditText edit_text2 = findViewById(R.id.edit_text2);
        EditText edit_text3 = findViewById(R.id.edit_text3);
        EditText edit_text6 = findViewById(R.id.edit_text6);
        EditText edit_text7 = findViewById(R.id.edit_text7);
        EditText edit_text8 = findViewById(R.id.edit_text8);
        EditText edit_text9 = findViewById(R.id.edit_text9);
        EditText edit_text10 = findViewById(R.id.edit_text10);
        EditText edit_text11 = findViewById(R.id.edit_text11);
        EditText edit_text12 = findViewById(R.id.edit_text12);

        Gson gson = new Gson();

        SharedPreferences sharedPreferences = getSharedPreferences("lk.oodp2.mediconnect01.doctor", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("doctor", null); // Retrieve JSON string

        Doctors_DTO doctors_dto2 = gson.fromJson(userJson, Doctors_DTO.class); // Convert JSON to object

        textView35.setText(doctors_dto2.getEmail());
        edit_text1.setText(doctors_dto2.getFirst_name());
        edit_text2.setText(doctors_dto2.getLast_name());
        edit_text3.setText(doctors_dto2.getPassword());
        edit_text6.setText(doctors_dto2.getMobile());
        edit_text7.setText(doctors_dto2.getAppointment_price());
        edit_text8.setText(doctors_dto2.getAbout());
        edit_text9.setText(doctors_dto2.getExperience());
        edit_text10.setText(doctors_dto2.getClinic_name());
        edit_text11.setText(doctors_dto2.getClinic_address());
        edit_text12.setText(doctors_dto2.getDocter_city_id().getCity());


    }
}