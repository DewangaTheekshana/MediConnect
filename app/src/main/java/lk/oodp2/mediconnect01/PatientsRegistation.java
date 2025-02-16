package lk.oodp2.mediconnect01;

import android.content.Intent;
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

import lk.oodp2.mediconnect01.dto.Response_DTO;
import lk.oodp2.mediconnect01.dto.User_DTO;
import okhttp3.MediaType;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PatientsRegistation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patients_registation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textView12 = findViewById(R.id.textView11);
        textView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientsRegistation.this, PatientsLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


        Button button3 = findViewById(R.id.button3);
        EditText FirstName = findViewById(R.id.edit_text1);
        EditText LastName = findViewById(R.id.edit_text2);
        EditText Email = findViewById(R.id.edit_text3);
        EditText Password = findViewById(R.id.edit_text4);
        EditText ConfirmPassword = findViewById(R.id.edit_text5);

        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (FirstName.getText().toString().isEmpty()) {
                    Toast.makeText(PatientsRegistation.this, "First Name is required", Toast.LENGTH_SHORT).show();
                } else if (LastName.getText().toString().isEmpty()) {
                    Toast.makeText(PatientsRegistation.this, "Last Name is required", Toast.LENGTH_SHORT).show();
                } else if (Email.getText().toString().isEmpty()) {
                    Toast.makeText(PatientsRegistation.this, "Email is required", Toast.LENGTH_SHORT).show();
                } else if (!Email.getText().toString().contains("@") || !Email.getText().toString().contains(".") || !Email.getText().toString().contains("gmail.com")) {
                    Toast.makeText(PatientsRegistation.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (Password.getText().toString().isEmpty()) {
                    Toast.makeText(PatientsRegistation.this, "Password is required", Toast.LENGTH_SHORT).show();
                } else if (ConfirmPassword.getText().toString().isEmpty()) {
                    Toast.makeText(PatientsRegistation.this, "Confirm Password is required", Toast.LENGTH_SHORT).show();
                } else if (!Password.getText().toString().equals(ConfirmPassword.getText().toString())) {
                    Toast.makeText(PatientsRegistation.this, "Password and Confirm Password should be same", Toast.LENGTH_SHORT).show();
                } else {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();

                            JsonObject user = new JsonObject();

                            user.addProperty("firstName", FirstName.getText().toString());
                            user.addProperty("lastName", LastName.getText().toString());
                            user.addProperty("email", Email.getText().toString());
                            user.addProperty("password", Password.getText().toString());

                            OkHttpClient okHttpClient = new OkHttpClient();

                            RequestBody requestBody = RequestBody.create(gson.toJson(user), MediaType.get("application/json"));
                            Request request = new Request.Builder()
                                    .url(BuildConfig.URL+"/PatientRegister")
                                    .post(requestBody)
                                    .build();

                            try {
                                Response response = okHttpClient.newCall(request).execute();
                                String responseText = response.body().string();


                                JsonObject responseJson = gson.fromJson(responseText, JsonObject.class);
                                boolean success = responseJson.get("success").getAsBoolean();
                                String message = responseJson.get("message").getAsString();


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (success) {
                                            Intent intent = new Intent(PatientsRegistation.this, PatientsLogin.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(PatientsRegistation.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } catch (IOException e) {
                                Log.e("MediConnectLogggggggggggggg", e.getMessage());
                            }
                        }
                    }).start();


                }

            }
        });


    }


}