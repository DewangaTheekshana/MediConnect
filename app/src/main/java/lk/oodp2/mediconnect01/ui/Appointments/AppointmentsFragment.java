package lk.oodp2.mediconnect01.ui.Appointments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.List;

import lk.oodp2.mediconnect01.BuildConfig;
import lk.oodp2.mediconnect01.DocterDetailView;
import lk.oodp2.mediconnect01.R;
import lk.oodp2.mediconnect01.dto.Clinics_DTO;
import lk.oodp2.mediconnect01.dto.ResponseList_DTO;
import lk.oodp2.mediconnect01.dto.User_DTO;
import lk.oodp2.mediconnect01.model.Appointments;
import lk.oodp2.mediconnect01.model.User;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AppointmentsFragment extends Fragment {

    private ArrayList<Appointments> appointmentList = new ArrayList<>();

    private Adapter3 userAdapter;

    public AppointmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);

        ArrayList<Appointments> appointmentList = new ArrayList<>();
        appointmentList.add(new Appointments("1", "Dr. Shehan Pereraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "66/3 Yatiyana Road, Wawita, Bandaragama", "2024/03/03", "10.30PM"));
        appointmentList.add(new Appointments("2", "Dr. Kaveesha Danujaya", "Colomo 1", "2024/03/03", "10.30PM"));
        appointmentList.add(new Appointments("3", "Dr. Kavindu Kodikara", "Colomo 2", "2024/03/04", "8.30PM"));
        appointmentList.add(new Appointments("4", "Dr. Sumya sewmini", "Colomo 3", "2024/03/05", "10.30AM"));
        appointmentList.add(new Appointments("5", "Dr. Kasun Perera", "Colomo 4", "2024/03/06", "10.30PM"));
        appointmentList.add(new Appointments("6", "Dr. Pasan Perera", "Colomo 5", "2024/03/07", "10.30PM"));
        appointmentList.add(new Appointments("7", "Dr. Sadun Perera", "Colomo 6", "2024/03/08", "11.30PM"));
        appointmentList.add(new Appointments("8", "Dr. Warun Perera", "Colomo 7", "2024/03/09", "10.30PM"));


        RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView4);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView1.setLayoutManager(linearLayoutManager);

        Adapter3 appointmentsAdapter = new Adapter3(appointmentList);
        recyclerView1.setAdapter(appointmentsAdapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("MediConnectLogggggggggggggg", "onResume");

        RecyclerView recyclerView1 = getActivity().findViewById(R.id.recyclerView4);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new Adapter3(appointmentList);
        recyclerView1.setAdapter(userAdapter);

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
                    Log.i("MediConnectLogggggggggggggg", responseText);
//                Type responseType = new TypeToken<ResponseList_DTO<Clinics_DTO>>() {}.getType();
//                ResponseList_DTO<Clinics_DTO> response_dto = gson.fromJson(responseText, responseType);
//                if (response_dto.getSuccess()) {
//                    List<Clinics_DTO> doctors = response_dto.getContent();
//                    getActivity().runOnUiThread(() -> {
//                        appointmentList.clear();
//                        for (Clinics_DTO doctor : doctors) {
//                            appointmentList.add(new User(String.valueOf(doctor.getDocters()), doctor.getFirst_name() + " " + doctor.getLast_name(), doctor.getClinic_city(), doctor.getAppointment_price(), doctor.getRate(), doctor.getAbout(), doctor.getExperience(), doctor.getClinic_address(), doctor.getMobile(), String.valueOf(doctor.getDoctor_Availability_id())));
//                        }
//                        userAdapter.notifyDataSetChanged();
//                        Log.i("MediConnectLogggggggggggggg", " "+appointmentList);
//                    });
//
//                }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Log.i("MediConnectLoga", "No user data found in SharedPreferences");
            }

        }).start();

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
        holder.buttonFindLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), appointments.getLocation(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

}