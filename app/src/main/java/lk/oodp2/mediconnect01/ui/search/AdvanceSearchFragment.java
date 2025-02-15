package lk.oodp2.mediconnect01.ui.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import lk.oodp2.mediconnect01.DocterDetailView;
import lk.oodp2.mediconnect01.R;
import lk.oodp2.mediconnect01.model.User;

public class AdvanceSearchFragment extends Fragment {

    public AdvanceSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advance_search, container, false);

        Spinner spinner = view.findViewById(R.id.spinner);
        Spinner spinner2 = view.findViewById(R.id.spinner2);

        String city[] = new String[]{"Select City", "Bandaragama", "Horana", "Colombo", "Gampaha", "Kaluthara"};
        String rate[] = new String[]{"Select Rate", "1 ⭐", "2 ⭐ ⭐", "3 ⭐ ⭐ ⭐", "4 ⭐ ⭐ ⭐ ⭐", "5 ⭐ ⭐ ⭐ ⭐ ⭐"};

        ArrayAdapter<String> arrayadapter = new ArrayAdapter<>(
            AdvanceSearchFragment.this.getActivity(),
            android.R.layout.simple_spinner_item,
            city
        );

        ArrayAdapter<String> arrayadapter2 = new ArrayAdapter<>(
                AdvanceSearchFragment.this.getActivity(),
                android.R.layout.simple_spinner_item,
                rate
        );

        spinner.setAdapter(arrayadapter);
        spinner2.setAdapter(arrayadapter2);

        ArrayList<User> userList2 = new ArrayList<>();
        userList2.add(new User("1", "Dr. Shehan Pereraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "Colombo", "Rs. 5000", "5", "About", "10", "Colombo", "0771234567"));
        userList2.add(new User("2", "Dr. Kasun Perera", "Kandy", "Rs. 4000", "5", "About", "10", "Kandy", "0771234567"));
        userList2.add(new User("3", "Dr. Nimesh Perera", "Gampaha", "Rs. 3000", "5", "About", "10", "Gampaha", "0771234567"));
        userList2.add(new User("4", "Dr. Nimal Perera", "Kurunagala", "Rs. 2000", "5", "About", "10", "Kurunagala", "0771234567"));
        userList2.add(new User("5", "Dr. Gayan Perera", "Colombo", "Rs. 1000", "5", "About", "10", "Colombo", "0771234567"));
        userList2.add(new User("6", "Dr. Shehan Perera", "Colombo", "Rs. 5000", "5", "About", "10", "Colombo", "0771234567"));

        RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView3);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView1.setLayoutManager(linearLayoutManager);

        lk.oodp2.mediconnect01.ui.search.Adapter2 userAdapter = new lk.oodp2.mediconnect01.ui.search.Adapter2(userList2);
        recyclerView1.setAdapter(userAdapter);

        return view;
    }
}


class Adapter2 extends RecyclerView.Adapter<lk.oodp2.mediconnect01.ui.search.Adapter2.ViewHolder> {

    static ArrayList<User> userList;

    public Adapter2(ArrayList<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public lk.oodp2.mediconnect01.ui.search.Adapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.docter_load, parent, false);
        lk.oodp2.mediconnect01.ui.search.Adapter2.ViewHolder viewHolder1 = new lk.oodp2.mediconnect01.ui.search.Adapter2.ViewHolder(view);

        return viewHolder1;

    }

    @Override
    public void onBindViewHolder(@NonNull lk.oodp2.mediconnect01.ui.search.Adapter2.ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewName.setText(user.getDocterName());
        holder.textViewCity.setText(user.getDocterCity());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        //        ImageView profileImage;
        TextView textViewName;
        TextView textViewCity;
        Button buttonDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textView18);
            textViewCity = itemView.findViewById(R.id.textView21);
            buttonDetails = itemView.findViewById(R.id.button8);
            buttonDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        User user = userList.get(position);

                        Intent intent = new Intent(itemView.getContext(), DocterDetailView.class);
                        intent.putExtra("docterName", user.getDocterName());
                        intent.putExtra("docterCity", user.getDocterCity());
                        intent.putExtra("Price", user.getPrice());

                        itemView.getContext().startActivity(intent);
                    }
                }
            });

        }
    }

}