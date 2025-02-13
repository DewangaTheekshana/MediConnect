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

import lk.oodp2.mediconnect01.dto.Response_DTO;
import lk.oodp2.mediconnect01.dto.User_DTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PatientsLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patients_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textView = findViewById(R.id.textView14);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientsLogin.this , PatientsRegistation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        Button button4 = findViewById(R.id.button4);
        EditText Email = findViewById(R.id.edit_text3);
        EditText Password = findViewById(R.id.edit_text5);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Email.getText().toString().isEmpty()){
                    Toast.makeText(PatientsLogin.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                }else if (Password.getText().toString().isEmpty()) {
                    Toast.makeText(PatientsLogin.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }else{

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();

                            JsonObject user = new JsonObject();
                            user.addProperty("email", Email.getText().toString());
                            user.addProperty("password", Password.getText().toString());

                            OkHttpClient okHttpClient = new OkHttpClient();

                            RequestBody requestBody = RequestBody.create(gson.toJson(user), MediaType.get("application/json"));
                            Request request = new Request.Builder()
                                    .url("http://192.168.37.146:8080/MediConnect/PatientLogin")
                                    .post(requestBody)
                                    .build();

                            try {
                                Response response = okHttpClient.newCall(request).execute();
                                String responseText = response.body().string();
                                Log.i("MediConnectLogggggggggggggg", "Response: " + responseText);

                                Response_DTO<User_DTO> response_dto = gson.fromJson(responseText, new TypeToken<Response_DTO<User_DTO>>(){}.getType());

                                Log.i("MediConnectLogggggggggggggg", " "+response_dto);

                                Log.i("MediConnectLogggggggggggggg", " "+response_dto.getSuccess());
                                Log.i("MediConnectLogggggggggggggg", " "+response_dto.getMessage());
                                Log.i("MediConnectLogggggggggggggg", " "+response_dto.getContent());



                                if (response_dto.getSuccess()){

                                    User_DTO user_dto = response_dto.getContent();

                                    SharedPreferences sharedPreferences = getSharedPreferences("lk.oodp2.mediconnect01.user", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    String userJson = gson.toJson(user_dto);
                                    editor.putString("user", userJson);
                                    editor.apply();

                                    Intent intent = new Intent(PatientsLogin.this, PatientsHome.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);

                                }else{

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(PatientsLogin.this, response_dto.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                            }catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();

                }

            }
        });

    }
}