package lk.oodp2.mediconnect01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import lk.oodp2.mediconnect01.dto.Doctors_DTO;
import lk.oodp2.mediconnect01.dto.Response_DTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

        TextView textView70 = findViewById(R.id.textView70);
        textView70.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("lk.oodp2.mediconnect01.doctor", Context.MODE_PRIVATE).edit();
                editor.clear(); // Clear all stored preferences
                editor.apply();

                // Redirect to login activity
                Intent intent = new Intent(DoctorProfile.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        EditText FirstName = findViewById(R.id.edit_text1);
        EditText LastName = findViewById(R.id.edit_text2);
        EditText Password = findViewById(R.id.edit_text3);
        EditText Mobile = findViewById(R.id.edit_text6);
        EditText AppointmentPrice = findViewById(R.id.edit_text7);
        EditText About = findViewById(R.id.edit_text8);
        EditText Experience = findViewById(R.id.edit_text9);
        EditText ClinicName = findViewById(R.id.edit_text10);
        EditText ClinicAddress = findViewById(R.id.edit_text11);
        EditText City = findViewById(R.id.edit_text12);

        Button button18 = findViewById(R.id.button18);
        button18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();

                        JsonObject docterProfile = new JsonObject();

                        SharedPreferences sharedPreferences = getSharedPreferences("lk.oodp2.mediconnect01.doctor", MODE_PRIVATE);
                        String userJson = sharedPreferences.getString("doctor", null); // Retrieve JSON string

                        Doctors_DTO doctors_dto2 = gson.fromJson(userJson, Doctors_DTO.class); // Convert JSON to object

                        Log.i("MediConnectLogggggggggggggg", "ID chack: "+doctors_dto2.getId());

                        docterProfile.addProperty("Id", doctors_dto2.getId());
                        docterProfile.addProperty("FirstName", FirstName.getText().toString());
                        docterProfile.addProperty("LastName", LastName.getText().toString());
                        docterProfile.addProperty("Password", Password.getText().toString());
                        docterProfile.addProperty("Mobile", Mobile.getText().toString());
                        docterProfile.addProperty("AppointmentPrice", AppointmentPrice.getText().toString());
                        docterProfile.addProperty("About", About.getText().toString());
                        docterProfile.addProperty("Experience", Experience.getText().toString());
                        docterProfile.addProperty("ClinicName", ClinicName.getText().toString());
                        docterProfile.addProperty("ClinicAddress", ClinicAddress.getText().toString());
                        docterProfile.addProperty("City", City.getText().toString());

                        OkHttpClient okHttpClient = new OkHttpClient();

                        RequestBody requestBody = RequestBody.create(gson.toJson(docterProfile), MediaType.get("application/json"));
                        Request request = new Request.Builder()
                                .url(BuildConfig.URL+"/DocterProfileUpdate")
                                .post(requestBody)
                                .build();

                        try {
                            Response response = okHttpClient.newCall(request).execute();
                            String responseText = response.body().string();
                            Log.i("MediConnectLogggggggggggggg", "docterProfileUpdate "+" "+responseText);

                            Response_DTO<Doctors_DTO> response_dto = gson.fromJson(responseText, new TypeToken<Response_DTO<Doctors_DTO>>(){}.getType());

                            if (response_dto.getSuccess()){
                                Doctors_DTO doctor_dto = response_dto.getContent();

                                SharedPreferences sharedPreferences1 = getSharedPreferences("lk.oodp2.mediconnect01.doctor", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences1.edit();
                                String doctorJson = gson.toJson(doctor_dto);
                                editor.putString("doctor", doctorJson);
                                editor.apply();

                                Intent intent = new Intent(DoctorProfile.this, DoctorProfile.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);

                            }


//                            JsonObject responseJson = gson.fromJson(responseText, JsonObject.class);
//                            boolean success = responseJson.get("success").getAsBoolean();
//                            String message = responseJson.get("message").getAsString();
//
//
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    if (success) {
//                                        Intent intent = new Intent(PatientsRegistation.this, PatientsLogin.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                        startActivity(intent);
//                                    } else {
//                                        Toast.makeText(PatientsRegistation.this, message, Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });

                        } catch (IOException e) {
                            Log.e("MediConnectLogggggggggggggg", e.getMessage());
                        }

                    }
                }).start();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                JsonObject user = new JsonObject();
                user.addProperty("cityId", doctors_dto2.getDocter_city_id().getId());

                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody requestBody = RequestBody.create(gson.toJson(user), MediaType.get("application/json"));
                Request request = new Request.Builder()
                        .url(BuildConfig.URL + "/loadCity")
                        .post(requestBody)
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    Log.i("MediConnectLogggggggggggggg", "Response: " + responseText);

                    JsonObject responseJson = gson.fromJson(responseText, JsonObject.class);
                    String conternt = responseJson.get("message").getAsString();

                    Log.i("MediConnectLogggggggggggggg", "Fuck"+conternt);

                    edit_text12.setText(conternt);

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();



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



    }
}