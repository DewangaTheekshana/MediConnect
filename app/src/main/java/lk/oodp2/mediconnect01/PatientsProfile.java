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
import lk.oodp2.mediconnect01.dto.User_DTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PatientsProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patients_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textView69 = findViewById(R.id.textView69);
        textView69.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("lk.oodp2.mediconnect01.user", MODE_PRIVATE).edit();
                editor.clear(); // Clear all stored preferences
                editor.apply();

                // Redirect to login activity
                Intent intent = new Intent(PatientsProfile.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        TextView email = findViewById(R.id.textView71);
        EditText FirstName = findViewById(R.id.edit_text2);
        EditText LastName = findViewById(R.id.edit_text3);
        EditText Password = findViewById(R.id.edit_text4);
        EditText Mobile = findViewById(R.id.edit_text5);

        Button button9 = findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();

                        JsonObject docterProfile = new JsonObject();

                        SharedPreferences sharedPreferences = getSharedPreferences("lk.oodp2.mediconnect01.user", MODE_PRIVATE);
                        String userJson = sharedPreferences.getString("user", null); // Retrieve JSON string

                        Doctors_DTO doctors_dto2 = gson.fromJson(userJson, Doctors_DTO.class); // Convert JSON to object

                        Log.i("MediConnectLogggggggggggggg", "ID chack: "+doctors_dto2.getId());

                        docterProfile.addProperty("Id", doctors_dto2.getId());
                        docterProfile.addProperty("FirstName", FirstName.getText().toString());
                        docterProfile.addProperty("LastName", LastName.getText().toString());
                        docterProfile.addProperty("Password", Password.getText().toString());
                        docterProfile.addProperty("Mobile", Mobile.getText().toString());

                        OkHttpClient okHttpClient = new OkHttpClient();

                        RequestBody requestBody = RequestBody.create(gson.toJson(docterProfile), MediaType.get("application/json"));
                        Request request = new Request.Builder()
                                .url(BuildConfig.URL+"/PatientsProfileUpdate")
                                .post(requestBody)
                                .build();

                        try {
                            Response response = okHttpClient.newCall(request).execute();
                            String responseText = response.body().string();
                            Log.i("MediConnectLogggggggggggggg", "docterProfileUpdate "+" "+responseText);

                            Response_DTO<User_DTO> response_dto = gson.fromJson(responseText, new TypeToken<Response_DTO<User_DTO>>(){}.getType());

                            if (response_dto.getSuccess()){
                                User_DTO doctor_dto = response_dto.getContent();

                                SharedPreferences sharedPreferences1 = getSharedPreferences("lk.oodp2.mediconnect01.user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences1.edit();
                                String doctorJson = gson.toJson(doctor_dto);
                                editor.putString("user", doctorJson);
                                editor.apply();

                                Intent intent = new Intent(PatientsProfile.this, PatientsProfile.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);

                            }


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

        TextView email = findViewById(R.id.textView71);
        EditText FirstName = findViewById(R.id.edit_text2);
        EditText LastName = findViewById(R.id.edit_text3);
        EditText Password = findViewById(R.id.edit_text4);
        EditText Mobile = findViewById(R.id.edit_text5);

        Gson gson = new Gson();

        SharedPreferences sharedPreferences = getSharedPreferences("lk.oodp2.mediconnect01.user", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", null); // Retrieve JSON string

        User_DTO user_dto2 = gson.fromJson(userJson, User_DTO.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                JsonObject user = new JsonObject();
                user.addProperty("user_id", user_dto2.getId());

                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody requestBody = RequestBody.create(gson.toJson(user), MediaType.get("application/json"));
                Request request = new Request.Builder()
                        .url(BuildConfig.URL + "/loadPatientMobileInProfile")
                        .post(requestBody)
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    Log.i("MediConnectLogggggggggggggg", "Response: " + responseText);

                    JsonObject responseJson = gson.fromJson(responseText, JsonObject.class);
                    String conternt = responseJson.get("message").getAsString();

                    Log.i("MediConnectLogggggggggggggg", " "+conternt);

                    Mobile.setText(conternt);

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        email.setText(user_dto2.getEmail());
        FirstName.setText(user_dto2.getFirst_name());
        LastName.setText(user_dto2.getLast_name());
        Password.setText(user_dto2.getPassword());



    }
}