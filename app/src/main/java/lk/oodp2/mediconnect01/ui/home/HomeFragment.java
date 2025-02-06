package lk.oodp2.mediconnect01.ui.home;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.oodp2.mediconnect01.DocterDetailView;
import lk.oodp2.mediconnect01.R;
import lk.oodp2.mediconnect01.databinding.FragmentHomeBinding;
import lk.oodp2.mediconnect01.model.User;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User("1", "Dr. Shehan Pereraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "Colombo"));
        userList.add(new User("2", "Dr. Kasun Perera", "Kandy"));
        userList.add(new User("3", "Dr. Nimesh Perera", "Gampaha"));
        userList.add(new User("4", "Dr. Nimal Perera", "Kurunagala"));
        userList.add(new User("5", "Dr. Gayan Perera", "Colombo"));

        RecyclerView recyclerView1 = root.findViewById(R.id.recyclerView1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView1.setLayoutManager(linearLayoutManager);

        Adapter1 userAdapter = new Adapter1(userList);
        recyclerView1.setAdapter(userAdapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


class Adapter1 extends RecyclerView.Adapter<Adapter1.ViewHolder>{

    static ArrayList<User> userList;

    public Adapter1(ArrayList<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view =  layoutInflater.inflate(R.layout.docter_load,parent,false);
        ViewHolder viewHolder1 = new ViewHolder(view);

        return viewHolder1;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewName.setText(user.getDocterName());
        holder.textViewCity.setText(user.getDocterCity());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

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

                        itemView.getContext().startActivity(intent);
                    }
                }
            });

        }
    }

}