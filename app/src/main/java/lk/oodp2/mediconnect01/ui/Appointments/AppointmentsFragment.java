package lk.oodp2.mediconnect01.ui.Appointments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lk.oodp2.mediconnect01.DocterDetailView;
import lk.oodp2.mediconnect01.R;
import lk.oodp2.mediconnect01.model.Appointments;
import lk.oodp2.mediconnect01.model.User;


public class AppointmentsFragment extends Fragment {

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