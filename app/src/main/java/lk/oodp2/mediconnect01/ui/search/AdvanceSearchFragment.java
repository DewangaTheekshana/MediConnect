package lk.oodp2.mediconnect01.ui.search;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.AdapterView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import lk.oodp2.mediconnect01.dto.DoctorCity_DTO;
import lk.oodp2.mediconnect01.dto.ResponseList_DTO;
import lk.oodp2.mediconnect01.model.City;
import lk.oodp2.mediconnect01.model.User;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdvanceSearchFragment extends Fragment {

    private ArrayList<User> docterList = new ArrayList<>();

    TextView textview;
    TextView textview2;

    EditText editTextSearch;
    private ArrayList<City> arrayList = new ArrayList<>();
    Dialog dialog;
    static String NameSelect = "";
    static String CitySelect = "";

    private Adapter2 userAdapter;

    public AdvanceSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advance_search, container, false);





        new Thread(new Runnable() {
            @Override
            public void run() {

                Gson gson = new Gson();
                OkHttpClient okHttpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/CityLoad")
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    Log.i("MediConnectLogggggggggggggg", responseText);
                    Type responseType2 = new TypeToken<ResponseList_DTO<DoctorCity_DTO>>() {}.getType();
                    ResponseList_DTO<DoctorCity_DTO> response_dto = gson.fromJson(responseText, responseType2);

                    if (response_dto.getSuccess()) {
                        List<DoctorCity_DTO> city = response_dto.getContent();
                        getActivity().runOnUiThread(() -> {
                            arrayList.clear();
                            arrayList.add(new City(0,"All City"));
                            for (DoctorCity_DTO citys : city) {
                                arrayList.add(new City(citys.getId(), citys.getCity()));
                                Log.i("MediConnectLogggggggggggggg", " "+citys.getCity());
                                Log.i("MediConnectLogggggggggggggg", " "+citys.getId());
                            }
                            Log.i("MediConnectLogggggggggggggg", " "+arrayList);
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        editTextSearch= view.findViewById(R.id.editTextText2);
        textview=view.findViewById(R.id.testView);
        textview2=view.findViewById(R.id.textView49);

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(view.getContext());

                dialog.setContentView(R.layout.dialog_searchable_spinner);

                dialog.getWindow().setLayout(650,800);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                ArrayAdapter<City> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1,arrayList);

                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }
                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        City city = arrayList.get(position);
                        textview.setText(city.toString());
                        textview2.setText(String.valueOf(city.getId()));
                        if (editTextSearch.toString().trim().isEmpty() && textview.getText().equals("All City")) {
                            loadAllDoctors();
                        } else {
                            searchDoctors();
                        }
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });


        Spinner spinner2 = view.findViewById(R.id.spinner2);;
        String rate[] = new String[]{"Select Rate", "1 ⭐", "2 ⭐ ⭐", "3 ⭐ ⭐ ⭐", "4 ⭐ ⭐ ⭐ ⭐", "5 ⭐ ⭐ ⭐ ⭐ ⭐"};


        ArrayAdapter<String> arrayadapter2 = new ArrayAdapter<>(
                AdvanceSearchFragment.this.getActivity(),
                android.R.layout.simple_spinner_item,
                rate
        );
        spinner2.setAdapter(arrayadapter2);

        ArrayList<User> userList2 = new ArrayList<>();

        //get ResponseList_DTO content list
        Type listType = new TypeToken<ResponseList_DTO<Clinics_DTO>>() {
        }.getType();
        ResponseList_DTO<Clinics_DTO> responseList = new ResponseList_DTO<>();
        List<Clinics_DTO> clinics = responseList.getContent();

        RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView3);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView1.setLayoutManager(linearLayoutManager);

        userAdapter = new Adapter2(docterList);
        recyclerView1.setAdapter(userAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Gson gson = new Gson();
                OkHttpClient okHttpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/PatientHomeLoadDocters")
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    Log.i("MediConnectLog", responseText);
                    Type responseType = new TypeToken<ResponseList_DTO<Clinics_DTO>>() {}.getType();
                    ResponseList_DTO<Clinics_DTO> response_dto = gson.fromJson(responseText, responseType);

                    if (response_dto.getSuccess()) {
                        List<Clinics_DTO> doctors = response_dto.getContent();
                        getActivity().runOnUiThread(() -> {
                            docterList.clear();
                            for (Clinics_DTO doctor : doctors) {
                                docterList.add(new User(String.valueOf(doctor.getDocters()), doctor.getFirst_name() + " " + doctor.getLast_name(), doctor.getClinic_city(), doctor.getAppointment_price(), doctor.getRate(), doctor.getAbout(), doctor.getExperience(), doctor.getClinic_address(), doctor.getMobile()));
                            }
                            userAdapter.notifyDataSetChanged();
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();


        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty() && textview.getText().equals("All City")) {
                    loadAllDoctors();
                } else {
                    searchDoctors();
                }
            }


            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void loadAllDoctors() {
        Log.i("MediConnectLogggggggggggggg", "loadAllDoctors: ");
        new Thread(() -> {
            Gson gson = new Gson();
            OkHttpClient okHttpClient = new OkHttpClient();

            JsonObject search = new JsonObject();
            search.addProperty("city", textview.getText().toString());
            search.addProperty("name", editTextSearch.getText().toString());
            search.addProperty("cityId",textview2.getText().toString());

            RequestBody requestBody = RequestBody.create(gson.toJson(search), MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(BuildConfig.URL+"/PatientAdvanceSearch")
                    .post(requestBody)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                String responseText = response.body().string();
                Log.i("MediConnectLogggggggggggggg", responseText);

                Type responseType = new TypeToken<ResponseList_DTO<Clinics_DTO>>() {}.getType();
                ResponseList_DTO<Clinics_DTO> response_dto = gson.fromJson(responseText, responseType);

                if (response_dto.getSuccess()) {
                    List<Clinics_DTO> doctors = response_dto.getContent();
                    getActivity().runOnUiThread(() -> {
                        docterList.clear();
                        for (Clinics_DTO doctor : doctors) {
                            docterList.add(new User(
                                    String.valueOf(doctor.getDocters()),
                                    doctor.getFirst_name() + " " + doctor.getLast_name(),
                                    doctor.getClinic_city(),
                                    doctor.getAppointment_price(),
                                    doctor.getRate(),
                                    doctor.getAbout(),
                                    doctor.getExperience(),
                                    doctor.getClinic_address(),
                                    doctor.getMobile()
                            ));
                        }
                        userAdapter.notifyDataSetChanged();
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void searchDoctors (){

        new Thread(() -> {
            Gson gson = new Gson();
            OkHttpClient okHttpClient = new OkHttpClient();

            JsonObject search = new JsonObject();
            search.addProperty("city", textview.getText().toString());
            search.addProperty("name", editTextSearch.getText().toString());
            search.addProperty("cityId",textview2.getText().toString());

            RequestBody requestBody = RequestBody.create(gson.toJson(search), MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(BuildConfig.URL+"/PatientAdvanceSearch")
                    .post(requestBody)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                String responseText = response.body().string();
                Log.i("MediConnectLogggggggggggggg", responseText);

                Type responseType = new TypeToken<ResponseList_DTO<Clinics_DTO>>() {}.getType();
                ResponseList_DTO<Clinics_DTO> response_dto = gson.fromJson(responseText, responseType);
                getActivity().runOnUiThread(() -> {
                    docterList.clear();
                    if (response_dto.getSuccess()) {
                        List<Clinics_DTO> doctors = response_dto.getContent();
                        for (Clinics_DTO doctor : doctors) {
                            docterList.add(new User(
                                    String.valueOf(doctor.getDocters()),
                                    doctor.getFirst_name() + " " + doctor.getLast_name(),
                                    doctor.getClinic_city(),
                                    doctor.getAppointment_price(),
                                    doctor.getRate(),
                                    doctor.getAbout(),
                                    doctor.getExperience(),
                                    doctor.getClinic_address(),
                                    doctor.getMobile()
                            ));
                        }
                        Log.i("MediConnectLogggggggggggggg", " " + docterList);
                    } else {
                        // Clear the list if no data is found and notify adapter
                        docterList.clear();
                        userAdapter.notifyDataSetChanged();
                        Log.i("MediConnectLogggggggggggggg", "No doctors found, list cleared.");
                    }
                    userAdapter.notifyDataSetChanged();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

}


class Adapter2 extends RecyclerView.Adapter<lk.oodp2.mediconnect01.ui.search.Adapter2.ViewHolder> {

    static ArrayList<User> userList;

    public Adapter2(ArrayList<User> userList) {
        this.userList = userList;
    }

    public void updateList(ArrayList<User> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        //        ImageView profileImage;
        TextView textViewName, textViewCity, textViewRate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textView18);
            textViewCity = itemView.findViewById(R.id.textView21);
            textViewRate = itemView.findViewById(R.id.textView22);
            itemView.findViewById(R.id.button8).setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User user = userList.get(position);
                    Intent intent = new Intent(itemView.getContext(), DocterDetailView.class);
                    intent.putExtra("docterName", user.getDocterName());
                    intent.putExtra("docterCity", user.getDocterCity());
                    intent.putExtra("Price", user.getPrice());
                    intent.putExtra("rate", user.getRate());
                    intent.putExtra("about", user.getAbout());
                    intent.putExtra("experiance", user.getExperiance());
                    intent.putExtra("location", user.getLocation());
                    intent.putExtra("mobile", user.getMobile());
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.docter_load, parent, false);
        return new Adapter2.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull lk.oodp2.mediconnect01.ui.search.Adapter2.ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewName.setText(user.getDocterName());
        holder.textViewCity.setText(user.getDocterCity());
        holder.textViewRate.setText(user.getRate());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}