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

import lk.oodp2.mediconnect01.dto.Doctors_DTO;
import lk.oodp2.mediconnect01.dto.Response_DTO;
import lk.oodp2.mediconnect01.dto.User_DTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoctersLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_docters_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textView17 = findViewById(R.id.textView17);
        textView17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctersLogin.this, DoctersRegistration.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        EditText editText1 = findViewById(R.id.edit_text1);
        EditText editText3 = findViewById(R.id.edit_text3);
        Button button6 = findViewById(R.id.button6);

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText1.getText().toString().isEmpty()){
                    Toast.makeText(DoctersLogin.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                }else if (editText3.getText().toString().isEmpty()) {
                    Toast.makeText(DoctersLogin.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }else{

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();

                            JsonObject user = new JsonObject();
                            user.addProperty("email", editText1.getText().toString());
                            user.addProperty("password", editText3.getText().toString());

                            OkHttpClient okHttpClient = new OkHttpClient();

                            RequestBody requestBody = RequestBody.create(gson.toJson(user), MediaType.get("application/json"));
                            Request request = new Request.Builder()
                                    .url(BuildConfig.URL+"/DoctorLogin")
                                    .post(requestBody)
                                    .build();

                            try {
                                Response response = okHttpClient.newCall(request).execute();
                                String responseText = response.body().string();
                                Log.i("MediConnectLogggggggggggggg", "Response: " + responseText);

                                Response_DTO<Doctors_DTO> response_dto = gson.fromJson(responseText, new TypeToken<Response_DTO<Doctors_DTO>>(){}.getType());

                                Log.i("MediConnectLogggggggggggggg", " "+response_dto);

                                Log.i("MediConnectLogggggggggggggg", " "+response_dto.getSuccess());
                                Log.i("MediConnectLogggggggggggggg", " "+response_dto.getMessage());
                                Log.i("MediConnectLogggggggggggggg", " "+response_dto.getContent());



                                if (response_dto.getSuccess()){

                                    Doctors_DTO doctor_dto = response_dto.getContent();

                                    JsonObject responseJson = gson.fromJson(responseText, JsonObject.class);

                                    if (responseJson.get("message").getAsString().equals("Waiting For Aprove Details")){

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(DoctersLogin.this, responseJson.get("message").getAsString(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }else{
                                        SharedPreferences sharedPreferences = getSharedPreferences("lk.oodp2.mediconnect01.doctor", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        String doctorJson = gson.toJson(doctor_dto);
                                        editor.putString("doctor", doctorJson);
                                        editor.apply();

                                        Intent intent = new Intent(DoctersLogin.this, DoctorHomeActivity.class);
                                        intent.putExtra("DoctorId", doctor_dto.getId());
                                        intent.putExtra("Doctorname", "Dr. "+doctor_dto.getFirst_name()+" "+doctor_dto.getLast_name());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                    }



                                }else{

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DoctersLogin.this, response_dto.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences2 = getSharedPreferences("lk.oodp2.mediconnect01.doctor", MODE_PRIVATE);
        if (sharedPreferences2.contains("doctor")) {
            Intent intent = new Intent(DoctersLogin.this, DoctorHomeActivity.class);
            startActivity(intent);
            finish(); // Close this activity to prevent going back
        }
    }
}