package lk.oodp2.mediconnect01.ui.Appointments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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

    public AppointmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);

        databaseHelper = new DatabaseHelper(getContext());


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
        }else {
            holder.buttonFindLocation.setBackgroundColor(Color.parseColor("#3CA0DF"));
            holder.buttonFindLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), appointments.getLocation(), Toast.LENGTH_SHORT).show();
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