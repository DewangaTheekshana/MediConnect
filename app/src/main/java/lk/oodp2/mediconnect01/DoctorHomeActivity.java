package lk.oodp2.mediconnect01;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

import lk.oodp2.mediconnect01.model.AppointmentDocter;

public class DoctorHomeActivity extends AppCompatActivity {

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

        RecyclerView recyclerView5 = findViewById(R.id.recyclerView5);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView5.setLayoutManager(linearLayoutManager);

        ArrayList<AppointmentDocter> appointmentList = new ArrayList<>();
        appointmentList.add(new AppointmentDocter("1", "John Doe1", "2021-10-10", "10:00"));
        appointmentList.add(new AppointmentDocter("2", "Jane Doe2", "2021-10-11", "11:00"));
        appointmentList.add(new AppointmentDocter("3", "John Doe3", "2021-10-12", "12:00"));
        appointmentList.add(new AppointmentDocter("4", "Jane Doe4", "2021-10-13", "13:00"));

        Adapter4 appointmentsAdapter = new Adapter4(appointmentList);
        recyclerView5.setAdapter(appointmentsAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            private final Paint paint = new Paint();
            private final Paint textPaint = new Paint();

            {
                textPaint.setColor(Color.WHITE);
                textPaint.setTextSize(50);
                textPaint.setTextAlign(Paint.Align.CENTER);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    Toast.makeText(DoctorHomeActivity.this, "Appointment Rejected", Toast.LENGTH_SHORT).show();
                } else if (direction == ItemTouchHelper.RIGHT) {
                    Toast.makeText(DoctorHomeActivity.this, "Appointment Accepted", Toast.LENGTH_SHORT).show();
                }
                appointmentList.remove(position);
                appointmentsAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float width = (float) itemView.getWidth();
                    float threshold = width / 2;

                    if (Math.abs(dX) < threshold) {
                        paint.setColor(Color.TRANSPARENT);
                    } else if (dX > 0) {
                        paint.setColor(Color.GREEN);
                        c.drawText("ACCEPT", itemView.getLeft() + width / 4, itemView.getTop() + itemView.getHeight() / 2, textPaint);
                    } else {
                        paint.setColor(Color.RED);
                        c.drawText("REJECT", itemView.getRight() - width / 4, itemView.getTop() + itemView.getHeight() / 2, textPaint);
                    }

                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), paint);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView5);
    }

    static class Adapter4 extends RecyclerView.Adapter<Adapter4.ViewHolder> {
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
            holder.bind(appointment);
        }

        @Override
        public int getItemCount() {
            return appointmentList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView UserName;
            private final TextView UserAppointmentDate;
            private final TextView UserAppointmentTime;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                UserName = itemView.findViewById(R.id.textView47);
                UserAppointmentDate = itemView.findViewById(R.id.textView48);
                UserAppointmentTime = itemView.findViewById(R.id.textView46);
            }

            public void bind(AppointmentDocter appointment) {
                UserName.setText(appointment.getUserName());
                UserAppointmentDate.setText(appointment.getDate());
                UserAppointmentTime.setText(appointment.getTime());
            }
        }
    }
}
