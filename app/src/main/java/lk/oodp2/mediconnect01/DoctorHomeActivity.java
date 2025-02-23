package lk.oodp2.mediconnect01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lk.oodp2.mediconnect01.dto.Appointments_DTO;
import lk.oodp2.mediconnect01.dto.Doctors_DTO;
import lk.oodp2.mediconnect01.dto.ResponseList_DTO;
import lk.oodp2.mediconnect01.dto.User_DTO;
import lk.oodp2.mediconnect01.model.AppointmentDocter;
import lk.oodp2.mediconnect01.model.Appointments;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoctorHomeActivity extends AppCompatActivity {

    private ArrayList<AppointmentDocter> appointmentDocterLoadList = new ArrayList<>();

    private Adapter4 userAdapter;

    private PieChart pieChart;


    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        profileImageView = findViewById(R.id.profileimage);

        // Doctor email (change this dynamically based on logged-in user)
        String doctorEmail = "susantha10@gmail.com";

        // Fetch and display the profile image
        fetchDoctorImage(doctorEmail);

        CardView cardView1 = findViewById(R.id.cardView1);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DoctorHomeActivity.this, DoctorProfile.class);
                startActivity(intent);
            }
        });

        Button button17 = findViewById(R.id.button17);
        button17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();

                SharedPreferences sharedPreferences = getSharedPreferences("lk.oodp2.mediconnect01.doctor", MODE_PRIVATE);
                String userJson = sharedPreferences.getString("doctor", null); // Retrieve JSON string

                Doctors_DTO doctors_dto2 = gson.fromJson(userJson, Doctors_DTO.class); // Convert JSON to object

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();
                        OkHttpClient okHttpClient = new OkHttpClient();

                        if (userJson != null) {

                            // Log or use the data
                            Log.i("MediConnectLoga", "User Name: " + doctors_dto2.getFirst_name() + " " + doctors_dto2.getLast_name());
                            Log.i("MediConnectLoga", "User City: " + doctors_dto2.getId());

                            JsonObject appointment = new JsonObject();
                            appointment.addProperty("DoctorId", String.valueOf(doctors_dto2.getId()));

                            RequestBody requestBody = RequestBody.create(gson.toJson(appointment), MediaType.get("application/json"));
                            Request request = new Request.Builder()
                                    .url(BuildConfig.URL+"/DocterAvialibilitychange")
                                    .post(requestBody)
                                    .build();
                            try {
                                Response response = okHttpClient.newCall(request).execute();
                                String responseText = response.body().string();
                                Log.i("MediConnectLoga", "Doctor Appointment"+responseText);

                                JsonObject responseJson = gson.fromJson(responseText, JsonObject.class);

                                if (responseJson.get("message").getAsString().equals("online")) {

                                    button17.setBackgroundColor(Color.parseColor("#3ea100"));
                                    button17.setText("Now Online");
//
                                }else {

                                    button17.setBackgroundColor(Color.parseColor("#3CA0DF"));
                                    button17.setText("Now Offline");

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.i("MediConnectLoga", "No user data found in SharedPreferences");
                        }
                    }
                }).start();
            }
        });

    }

    private void fetchDoctorImage(String email) {
        OkHttpClient client = new OkHttpClient();

        // Build the request
        Request request = new Request.Builder()
                .url(BuildConfig.URL + "/getDoctorImage"+"?email=" + email)  // Send doctor's email as a parameter
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("DoctorProfile", "Image fetch failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    // Update UI on the main thread
                    runOnUiThread(() -> profileImageView.setImageBitmap(bitmap));
                } else {
                    Log.e("DoctorProfile", "Server error: " + response.code());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

//        ArrayList<PieEntry> pieEntries = new ArrayList<>();
//        pieEntries.add(new PieEntry(2, "Pending"));
//        pieEntries.add(new PieEntry(3, "Conform"));
//
//        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Appointments Status");
//
//        ArrayList<Integer> colorsArrayList = new ArrayList<>();
//        colorsArrayList.add(getColor(R.color.bar1));
//        colorsArrayList.add(getColor(R.color.bar2));
//        pieDataSet.setColors(colorsArrayList);
//
//        PieData pieData = new PieData(pieDataSet);
//        pieChart.setData(pieData);
//        pieChart.setEntryLabelTextSize(12);
//        pieChart.setEntryLabelColor(Color.BLACK);
//
//        pieChart.animateY(2000, Easing.EaseInOutCubic);
//
//
//        pieChart.invalidate();



        Gson gson = new Gson();

        SharedPreferences sharedPreferences = getSharedPreferences("lk.oodp2.mediconnect01.doctor", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("doctor", null); // Retrieve JSON string

        Doctors_DTO doctors_dto = gson.fromJson(userJson, Doctors_DTO.class); // Convert JSON to object

        TextView textView45 = findViewById(R.id.textView45);
        textView45.setText("Dr. "+doctors_dto.getFirst_name()+" "+doctors_dto.getLast_name());

        RecyclerView recyclerView5 = findViewById(R.id.recyclerView5);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView5.setLayoutManager(linearLayoutManager);
        userAdapter = new Adapter4(appointmentDocterLoadList);
        recyclerView5.setAdapter(userAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Gson gson = new Gson();
                OkHttpClient okHttpClient = new OkHttpClient();

                if (userJson != null) {

                    // Log or use the data
                    Log.i("MediConnectLoga", "User Name: " + doctors_dto.getFirst_name() + " " + doctors_dto.getLast_name());
                    Log.i("MediConnectLoga", "User City: " + doctors_dto.getId());

                    JsonObject appointment = new JsonObject();
                    appointment.addProperty("DoctorId", String.valueOf(doctors_dto.getId()));

                    RequestBody requestBody = RequestBody.create(gson.toJson(appointment), MediaType.get("application/json"));
                    Request request = new Request.Builder()
                            .url(BuildConfig.URL+"/AppointmentLoadDocter")
                            .post(requestBody)
                            .build();
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        String responseText = response.body().string();
                        Log.i("MediConnectLoga", "Doctor Appointment"+responseText);
                        Type responseType = new TypeToken<ResponseList_DTO<Appointments_DTO>>() {}.getType();
                        ResponseList_DTO<Appointments_DTO> response_dto = gson.fromJson(responseText, responseType);
                        if (response_dto.getSuccess()) {
                            List<Appointments_DTO> doctors = response_dto.getContent();
                            runOnUiThread(() -> {
                                appointmentDocterLoadList.clear();
                                for (Appointments_DTO appointmentsDto : doctors) {
                                    appointmentDocterLoadList.add(new AppointmentDocter(String.valueOf(appointmentsDto.getId()), appointmentsDto.getUser_id(),String.valueOf(appointmentsDto.getAppointment_date()),String.valueOf(appointmentsDto.getAppointment_time()), appointmentsDto.getDoctor_Availability_id(), appointmentsDto.getStatus()));
                                }
                                userAdapter.notifyDataSetChanged();
                                Log.i("MediConnectLogggggggggggggg", " "+appointmentDocterLoadList);
                            });

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.i("MediConnectLoga", "No user data found in SharedPreferences");
                }

                pieChart = findViewById(R.id.PieChart);
                fetchAppointmentStatus();
            }
        }).start();
    }

    private void fetchAppointmentStatus() {

        Gson gson = new Gson();

        SharedPreferences sharedPreferences = getSharedPreferences("lk.oodp2.mediconnect01.doctor", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("doctor", null); // Retrieve JSON string

        Doctors_DTO doctors_dto2 = gson.fromJson(userJson, Doctors_DTO.class); // Convert JSON to object

        Log.i("fetchAppointmentStatus", "fetchAppointmentStatus: "+doctors_dto2.getId());

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BuildConfig.URL + "/DoctorAppointmentsStatusServlet?doctor_id="+doctors_dto2.getId())
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API_ERROR", "Request Failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    Log.i("fetchAppointmentStatus", "onResponse: "+jsonData);
                    runOnUiThread(() -> updatePieChart(jsonData));
                } else {
                    Log.e("API_ERROR", "Response Failed: " + response.code());
                }
            }
        });
    }

    private void updatePieChart(String jsonData) {

        int pendingCount = 0;
        int confirmedCount = 0;

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray appointmentsArray = jsonObject.getJSONArray("appointments");

            for (int i = 0; i < appointmentsArray.length(); i++) {
                JSONObject appointment = appointmentsArray.getJSONObject(i);

                // Ensure both "status" and "count" exist
                if (appointment.has("status") && appointment.has("count")) {
                    String status = appointment.getString("status").trim();
                    int count = appointment.getInt("count");

                    // Adjust the counts based on the status
                    if (status.equals("0")) {
                        pendingCount += count;  // Add count to pendingCount
                    } else if (status.equals("1")) {
                        confirmedCount += count;  // Add count to confirmedCount
                    }
                }
            }

            Log.i("fetchAppointmentStatus", "updatePieChart: "+ pendingCount);
            Log.i("fetchAppointmentStatus", "updatePieChart2: "+ confirmedCount);

            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            pieEntries.add(new PieEntry(pendingCount, "Pending"));
            pieEntries.add(new PieEntry(confirmedCount, "Confirmed"));

            PieDataSet pieDataSet = new PieDataSet(pieEntries, "Appointments Status");

            ArrayList<Integer> colorsArrayList = new ArrayList<>();
            colorsArrayList.add(getColor(R.color.bar1));
            colorsArrayList.add(getColor(R.color.bar2));
            pieDataSet.setColors(colorsArrayList);

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.setEntryLabelTextSize(12);
            pieChart.setEntryLabelColor(Color.BLACK);

            pieChart.animateY(2000, Easing.EaseInOutCubic);
            pieChart.invalidate();

        } catch (Exception e) {
            Log.e("JSON_ERROR", "Error parsing JSON: " + e.getMessage());
        }
    }
    class Adapter4 extends RecyclerView.Adapter<Adapter4.ViewHolder> {
        private final ArrayList<AppointmentDocter> appointmentList;

        public Adapter4(ArrayList<AppointmentDocter> appointmentList) {
            this.appointmentList = appointmentList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_request_load, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            AppointmentDocter appointment = appointmentList.get(position);
            holder.UserName.setText(appointment.getUserName());
            holder.UserAppointmentDate.setText(appointment.getDate());
            holder.UserAppointmentTime.setText(appointment.getTime());
            if (appointment.getStatus().equals("0")){
                holder.statusAppointment.setBackgroundColor(Color.parseColor("#f0950c"));
                holder.statusAppointment.setText("Pending");

                holder.statusAppointment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                Gson gson = new Gson();
                                OkHttpClient okHttpClient = new OkHttpClient();

                                JsonObject appointment2 = new JsonObject();
                                appointment2.addProperty("AppointmentId", appointment.getId());
                                appointment2.addProperty("AppointmentStatus", appointment.getStatus());


                                RequestBody requestBody = RequestBody.create(gson.toJson(appointment2), MediaType.get("application/json"));
                                Request request = new Request.Builder()
                                        .url(BuildConfig.URL+"/AppointmentStatusUpdate")
                                        .post(requestBody)
                                        .build();
                                try {
                                    Response response = okHttpClient.newCall(request).execute();
                                    String responseText = response.body().string();
                                    Log.i("MediConnectLoga", "Doctor Appointment"+responseText);

                                    JsonObject responseJson = gson.fromJson(responseText, JsonObject.class);

                                    if (responseJson.get("success").getAsBoolean()) {

                                        Log.i("MediConnectLoga", "working");
//
                                        Intent intent = new Intent(DoctorHomeActivity.this, DoctorHomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
//
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                });

            }else{
                holder.statusAppointment.setBackgroundColor(Color.parseColor("#3CA0DF"));
                holder.statusAppointment.setText("Conform");
            }
        }

        @Override
        public int getItemCount() {
            return appointmentList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView UserName;
            private final TextView UserAppointmentDate;
            private final TextView UserAppointmentTime;
            Button statusAppointment;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                UserName = itemView.findViewById(R.id.textView47);
                UserAppointmentDate = itemView.findViewById(R.id.textView48);
                UserAppointmentTime = itemView.findViewById(R.id.textView46);
                statusAppointment = itemView.findViewById(R.id.button14);
            }
        }
    }
}