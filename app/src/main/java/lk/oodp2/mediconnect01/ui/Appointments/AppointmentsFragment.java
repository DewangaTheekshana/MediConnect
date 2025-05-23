package lk.oodp2.mediconnect01.ui.Appointments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import lk.oodp2.mediconnect01.AppointmentUpdate;
import lk.oodp2.mediconnect01.BuildConfig;
import lk.oodp2.mediconnect01.R;
import lk.oodp2.mediconnect01.SqLite.DatabaseHelper;
import lk.oodp2.mediconnect01.dto.Appointments_DTO;
import lk.oodp2.mediconnect01.dto.ResponseList_DTO;
import lk.oodp2.mediconnect01.dto.User_DTO;
import lk.oodp2.mediconnect01.model.Appointments;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppointmentsFragment extends Fragment {



    private ArrayList<Appointments> appointmentList = new ArrayList<>();
    private ArrayList<Appointments> appointmentHistoryList = new ArrayList<>();

    private Adapter3 userAdapter;

    private Adapter4 userAdapter4;
    DatabaseHelper databaseHelper;

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private static final float SHAKE_THRESHOLD = 12.0f; // Adjust sensitivity
    private long lastShakeTime = 0;

    boolean isFlip;

    public AppointmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (sensor != null) {
            sensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    double acceleration = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;
                    long currentTime = System.currentTimeMillis();

                    if (acceleration > SHAKE_THRESHOLD && (currentTime - lastShakeTime) > 500) {
                        Log.i("AppShake", "Phone Shaken!");
                        lastShakeTime = currentTime;
                        Toast.makeText(getContext(), "Shake detected!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
        } else {
            Log.i("AppShake", "Accelerometer not available.");
        }


        return view;
    }




    private boolean isNetworkAvailable() {
        // Implement network check here
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void AppointmentLoad(){
        new Thread(() -> {
            Gson gson = new Gson();
            OkHttpClient okHttpClient = new OkHttpClient();

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("lk.oodp2.mediconnect01.user", Context.MODE_PRIVATE);
            String userJson = sharedPreferences.getString("user", null); // Retrieve JSON string

            if (userJson != null) {
                User_DTO user_dto = gson.fromJson(userJson, User_DTO.class); // Convert JSON to object

                // Log or use the data
                Log.i("MediConnectLoga", "User Name: " + user_dto.getFirst_name() + " " + user_dto.getLast_name());
                Log.i("MediConnectLoga", "User City: " + user_dto.getId());

                JsonObject appointment = new JsonObject();
                appointment.addProperty("userId", String.valueOf(user_dto.getId()));

                RequestBody requestBody = RequestBody.create(gson.toJson(appointment), MediaType.get("application/json"));
                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/AppointmnetLoad")
                        .post(requestBody)
                        .build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    Log.i("MediConnectLoga", responseText);
                    Type responseType = new TypeToken<ResponseList_DTO<Appointments_DTO>>() {}.getType();
                    ResponseList_DTO<Appointments_DTO> response_dto = gson.fromJson(responseText, responseType);
                    if (response_dto.getSuccess()) {
                        List<Appointments_DTO> doctors = response_dto.getContent();
                        databaseHelper.deleteAllOrders();
                        getActivity().runOnUiThread(() -> {
                            appointmentList.clear();
                            for (Appointments_DTO appointmentsDto : doctors) {
                                Appointments appointments2 = new Appointments(String.valueOf(appointmentsDto.getId()), appointmentsDto.getDocters(), appointmentsDto.getLocation(),String.valueOf(appointmentsDto.getAppointment_date()),String.valueOf(appointmentsDto.getAppointment_time()), appointmentsDto.getStatus());
                                databaseHelper.insertOrder(appointments2);
                                appointmentList.add(appointments2);
                            }
                            displayAppointment(getView(), appointmentList);
                            Log.i("MediConnectLogggggggggggggg", " "+appointmentList);
                        });
//
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Log.i("MediConnectLoga", "No user data found in SharedPreferences");
            }

        }).start();
    }

    private void appointmentHistory (){
        RecyclerView recyclerView2 = getActivity().findViewById(R.id.recyclerViewAppoinmentHistory);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter4 = new Adapter4(appointmentHistoryList);
        recyclerView2.setAdapter(userAdapter4);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Gson gson = new Gson();
                OkHttpClient okHttpClient = new OkHttpClient();

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("lk.oodp2.mediconnect01.user", Context.MODE_PRIVATE);
                String userJson = sharedPreferences.getString("user", null); // Retrieve JSON string

                if (userJson != null) {
                    User_DTO user_dto = gson.fromJson(userJson, User_DTO.class); // Convert JSON to object

                    // Log or use the data
                    Log.i("MediConnectLoga", "User Name: " + user_dto.getFirst_name() + " " + user_dto.getLast_name());
                    Log.i("MediConnectLoga", "User City: " + user_dto.getId());

                    JsonObject appointment = new JsonObject();
                    appointment.addProperty("userId", String.valueOf(user_dto.getId()));

                    RequestBody requestBody = RequestBody.create(gson.toJson(appointment), MediaType.get("application/json"));
                    Request request = new Request.Builder()
                            .url(BuildConfig.URL+"/AppointmentHistoryLoad")
                            .post(requestBody)
                            .build();
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        String responseText = response.body().string();
                        Log.i("MediConnectLoga", "History"+responseText);
                        Type responseType = new TypeToken<ResponseList_DTO<Appointments_DTO>>() {}.getType();
                        ResponseList_DTO<Appointments_DTO> response_dto = gson.fromJson(responseText, responseType);
                        if (response_dto.getSuccess()) {
                            List<Appointments_DTO> doctors = response_dto.getContent();
                            getActivity().runOnUiThread(() -> {
                                appointmentHistoryList.clear();
                                for (Appointments_DTO appointmentsDto : doctors) {
                                    appointmentHistoryList.add(new Appointments(String.valueOf(appointmentsDto.getId()), appointmentsDto.getDocters(), appointmentsDto.getLocation(),String.valueOf(appointmentsDto.getAppointment_date()),String.valueOf(appointmentsDto.getAppointment_time()),appointmentsDto.getStatus()));
                                }
                                userAdapter4.notifyDataSetChanged();
                                Log.i("MediConnectLogggggggggggggg", " "+appointmentHistoryList);
                            });
//
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

    @Override
    public void onResume() {
        super.onResume();



        Log.i("MediConnectLogggggggggggggg", "onResume");

        if (isNetworkAvailable()) {
            AppointmentLoad();
            appointmentHistory();
        } else {
            loadAppointmentFromSQLite(getView());
        }

//        AppointmentLoad();
//
//        appointmentHistory();

    }

    private void loadAppointmentFromSQLite(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Appointments> appointments = databaseHelper.getAllOrders();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        appointmentList.clear();
                        appointmentList.addAll(appointments);
                        displayAppointment(view, appointmentList);
                    }
                });
            }
        }).start();
    }
    private void displayAppointment(View view, ArrayList<Appointments> appointments) {

        RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView4);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new Adapter3(appointments);
        recyclerView1.setAdapter(userAdapter);
    }
}


class Adapter3 extends RecyclerView.Adapter<Adapter3.appointmentViewHolder> {

    static class appointmentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewTime;
        TextView textViewDate;
        Button buttonFindLocation;

        public appointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textView41);
            textViewTime = itemView.findViewById(R.id.textView42);
            textViewDate = itemView.findViewById(R.id.textView43);
            buttonFindLocation = itemView.findViewById(R.id.button13);

        }
    }
    static ArrayList<Appointments> appointmentList;

    public Adapter3(ArrayList<Appointments> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public appointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.appointments_load, parent, false);
        appointmentViewHolder viewHolder2 = new appointmentViewHolder(view);

        return viewHolder2;

    }

    @Override
    public void onBindViewHolder(@NonNull appointmentViewHolder holder, int position) {
        Appointments appointments = appointmentList.get(position);
        holder.textViewName.setText(appointments.getDoctorName());
        holder.textViewDate.setText(appointments.getDate());
        holder.textViewTime.setText(appointments.getTime());
        if (appointments.getStatus().equals("0")){
            holder.buttonFindLocation.setBackgroundColor(Color.parseColor("#f0950c"));
            holder.buttonFindLocation.setText("Pending");
            holder.buttonFindLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), AppointmentUpdate.class);
                    intent.putExtra("id", appointments.getId());
                    intent.putExtra("DoctorName", appointments.getDoctorName());
                    intent.putExtra("location", appointments.getLocation());
                    intent.putExtra("date", appointments.getDate());
                    intent.putExtra("time", appointments.getTime());
                    intent.putExtra("status", appointments.getStatus());
                    v.getContext().startActivity(intent);
                }
            });
        }else {
            holder.buttonFindLocation.setBackgroundColor(Color.parseColor("#3CA0DF"));
            holder.buttonFindLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();
                            OkHttpClient okHttpClient = new OkHttpClient();

                            JsonObject appointment = new JsonObject();
                            appointment.addProperty("AppoitmentId", String.valueOf(appointments.getId()));

                            RequestBody requestBody = RequestBody.create(gson.toJson(appointment), MediaType.get("application/json"));
                            Request request = new Request.Builder()
                                    .url(BuildConfig.URL + "/DocterIdGetAppointment")
                                    .post(requestBody)
                                    .build();
                            try {
                                Response response = okHttpClient.newCall(request).execute();
                                String responseText = response.body().string();
                                Log.i("MediConnectLoga", "History" + responseText);

                                JsonObject responseJson = gson.fromJson(responseText, JsonObject.class);

                                String message = responseJson.get("message").getAsString();

                                Intent intent = new Intent(v.getContext(), lk.oodp2.mediconnect01.DoctorMap.class);
                                intent.putExtra("doctor_id", message);
                                v.getContext().startActivity(intent);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }).start();


//                    Intent intent = new Intent(v.getContext(), lk.oodp2.mediconnect01.DoctorMap.class);
//                    intent.putExtra("doctor_id", appointments.getId());
//                    v.getContext().startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

}

class Adapter4 extends RecyclerView.Adapter<Adapter4.appointmentViewHolder> {

    static class appointmentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewTime;
        TextView textViewDate;
        Button buttonFindLocation;

        public appointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textView41);
            textViewTime = itemView.findViewById(R.id.textView42);
            textViewDate = itemView.findViewById(R.id.textView43);
            buttonFindLocation = itemView.findViewById(R.id.button13);

        }
    }
    static ArrayList<Appointments> appointmentHistoryList;

    public Adapter4(ArrayList<Appointments> appointmentList) {
        this.appointmentHistoryList = appointmentList;
    }

    @NonNull
    @Override
    public appointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.appointments_load, parent, false);
        appointmentViewHolder viewHolder2 = new appointmentViewHolder(view);

        return viewHolder2;

    }

    @Override
    public void onBindViewHolder(@NonNull appointmentViewHolder holder, int position) {
        Appointments appointments = appointmentHistoryList.get(position);
        holder.textViewName.setText(appointments.getDoctorName());
        holder.textViewDate.setText(appointments.getDate());
        holder.textViewTime.setText(appointments.getTime());
        holder.buttonFindLocation.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return appointmentHistoryList.size();
    }

}