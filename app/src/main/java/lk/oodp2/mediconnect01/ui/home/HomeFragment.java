package lk.oodp2.mediconnect01.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lk.oodp2.mediconnect01.BuildConfig;
import lk.oodp2.mediconnect01.DocterDetailView;
import lk.oodp2.mediconnect01.R;
import lk.oodp2.mediconnect01.databinding.FragmentHomeBinding;
import lk.oodp2.mediconnect01.dto.Clinics_DTO;
import lk.oodp2.mediconnect01.dto.ResponseList_DTO;
import lk.oodp2.mediconnect01.model.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import java.lang.reflect.Type;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ArrayList<User> docterList = new ArrayList<>();
    private Adapter1 userAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageView profileimage = root.findViewById(R.id.profileimage);
        profileimage.setOnClickListener(v -> {
            Intent intent = new Intent(root.getContext(), lk.oodp2.mediconnect01.PatientsProfile.class);
            startActivity(intent);
        });

        Button button11 = root.findViewById(R.id.button11);
        button11.setOnClickListener(v -> {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_search); // ID of the tab
        });

        ImageView imageView12 = root.findViewById(R.id.imageView12);
        imageView12.setOnClickListener(v -> {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_search); // ID of the tab
        });

        Button button12 = root.findViewById(R.id.button12);
        button12.setOnClickListener(v -> {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_appointments); // ID of the tab
        });

        EditText editTextSearch1 = root.findViewById(R.id.editTextSearch1);
        editTextSearch1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchDoctors(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



        return root;
    }

    private boolean isNetworkAvailable() {
        // Implement network check here
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("MediConnectLogggggggggggggg", "onResume");

        if (isNetworkAvailable()) {
            loadHomeDocters();
        } else {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadHomeDocters(){
        RecyclerView recyclerView1 = getActivity().findViewById(R.id.recyclerView1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new Adapter1(docterList);
        recyclerView1.setAdapter(userAdapter);

        new Thread(() -> {
            Gson gson = new Gson();
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(BuildConfig.URL+"/PatientHomeLoadDocters")
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                String responseText = response.body().string();
                Log.i("MediConnectLogggggggggggggg", responseText);
                Type responseType = new TypeToken<ResponseList_DTO<Clinics_DTO>>() {}.getType();
                ResponseList_DTO<Clinics_DTO> response_dto = gson.fromJson(responseText, responseType);
                if (response_dto.getSuccess()) {
                    List<Clinics_DTO> doctors = response_dto.getContent();
                    Log.i("MediConnectLoggggggggo", "fuck"+response_dto.getContent());
                    getActivity().runOnUiThread(() -> {
                        docterList.clear();
                        for (Clinics_DTO doctor : doctors) {
                            docterList.add(new User(String.valueOf(doctor.getId()), doctor.getFirst_name() + " " + doctor.getLast_name(), doctor.getClinic_city(), doctor.getAppointment_price(), doctor.getRate(), doctor.getAbout(), doctor.getExperience(), doctor.getClinic_address(), doctor.getMobile(), String.valueOf(doctor.getDoctor_Availability_id()),doctor.getAvailibility_time_to(),doctor.getAvailibility_time_from()));
                            Log.i("MediConnectLoggggggggo", String.valueOf(doctor.getId()));
                        }
                        userAdapter.notifyDataSetChanged();

                        Log.i("MediConnectLogggggggggggggg", " "+docterList);
                    });

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void SearchDoctors(String query) {
        ArrayList<User> searchList = new ArrayList<>();
        for (User doctor : docterList) {
            if (doctor.getDocterCity().toLowerCase().contains(query.toLowerCase())) {
                searchList.add(doctor);
            }
        }
        userAdapter.updateList(searchList);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

class Adapter1 extends RecyclerView.Adapter<Adapter1.ViewHolder> {
    static ArrayList<User> docterList;

    public Adapter1(ArrayList<User> docterList) {
        this.docterList = docterList;
    }

    public void updateList(ArrayList<User> newList) {
        this.docterList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewCity, textViewRate;
        CardView cardColor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textView18);
            textViewCity = itemView.findViewById(R.id.textView21);
            textViewRate = itemView.findViewById(R.id.textView22);
            cardColor = itemView.findViewById(R.id.statuscard1);
            itemView.findViewById(R.id.button8).setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User user = docterList.get(position);
                    Intent intent = new Intent(itemView.getContext(), DocterDetailView.class);
                    intent.putExtra("doctor_id", user.getDocterId());
                    intent.putExtra("docterName", user.getDocterName());
                    intent.putExtra("docterCity", user.getDocterCity());
                    intent.putExtra("Price", user.getPrice());
                    intent.putExtra("rate", user.getRate());
                    intent.putExtra("about", user.getAbout());
                    intent.putExtra("experiance", user.getExperiance());
                    intent.putExtra("location", user.getLocation());
                    intent.putExtra("mobile", user.getMobile());
                    intent.putExtra("status", user.getStatus());
                    intent.putExtra("lng", user.getStatus());
                    intent.putExtra("availibility_time_to", user.getAvailibility_time_to());
                    intent.putExtra("availibility_time_from", user.getAvailibility_time_from());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.docter_load, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User docter = docterList.get(position);
        holder.textViewName.setText("Dr." + docter.getDocterName());
        holder.textViewCity.setText(docter.getDocterCity());
        holder.textViewRate.setText(docter.getRate());
        if (docter.getStatus().equals("3.0")){
            holder.cardColor.setCardBackgroundColor(Color.parseColor("#79BF2B"));
        }else {
            holder.cardColor.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return docterList.size();
    }
}
