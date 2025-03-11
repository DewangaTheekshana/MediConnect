package lk.oodp2.mediconnect01;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import lk.oodp2.mediconnect01.dto.Appointments_DTO;
import lk.oodp2.mediconnect01.dto.Response_DTO;
import lk.oodp2.mediconnect01.dto.User_DTO;
import lk.oodp2.mediconnect01.ui.Appointments.AppointmentsFragment;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppointmentUpdate extends AppCompatActivity {

    private TextView txtSelectedDate;
    private TextView btnSelectDate;

    private TextView txtSelectedTime;
    private TextView btnSelectTime;
    String id;
    String doctorName;
    String location;
    String date;
    String time;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        id = getIntent().getStringExtra("id");
        doctorName = getIntent().getStringExtra("DoctorName");
        location = getIntent().getStringExtra("location");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        status = getIntent().getStringExtra("status");

        Log.i("updateappointment", "onCreate: id: " + id);
        Log.i("updateappointment", "onCreate: doctorName: " + doctorName);
        Log.i("updateappointment", "onCreate: location: " + location);
        Log.i("updateappointment", "onCreate: date: " + date);
        Log.i("updateappointment", "onCreate: time: " + time);
        Log.i("updateappointment", "onCreate: status: " + status);

        txtSelectedDate = findViewById(R.id.textView82);
        btnSelectDate = findViewById(R.id.btnSelectDate2);
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        txtSelectedTime = findViewById(R.id.textView85);
        btnSelectTime = findViewById(R.id.btnSelectTime2);

        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        TextView docterName1 = findViewById(R.id.textView76);
        TextView location1 = findViewById(R.id.textView78);

        docterName1.setText(doctorName);
        location1.setText(location);
        txtSelectedDate.setText(date);
        txtSelectedTime.setText(time);

        Button payButton = findViewById(R.id.button41);
        payButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter nowDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedDate = myDateObj.format(nowDate);

                System.out.println("After formatting Date: " + formattedDate);

                // Convert formattedDate (String) to Date with correct format
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                Date formattedDateObject = null;

                try {
                    formattedDateObject = sdf.parse(formattedDate); // Convert to Date
                } catch (ParseException ex) {
                    Log.e("updateappointment", "Error parsing current date: " + ex.getMessage());
                }

                // Check if the date and time fields are filled
                if (txtSelectedDate.getText().toString().equals("None")) {
                    Toast.makeText(AppointmentUpdate.this, "Date is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (txtSelectedTime.getText().toString().equals("None")) {
                    Toast.makeText(AppointmentUpdate.this, "Time is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convert txtSelectedDate (String) to Date
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date selectedDateObject = null;

                try {
                    selectedDateObject = simpleDateFormat.parse(txtSelectedDate.getText().toString());
                } catch (ParseException ex) {
                    Log.e("updateappointment", "Date Parsing Error: " + ex.getMessage());
                }

                // Check if either date object is null before comparison
                if (selectedDateObject == null || formattedDateObject == null) {
                    Toast.makeText(AppointmentUpdate.this, "Error processing dates", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Compare selected date with current date
                if (selectedDateObject.before(formattedDateObject)) {
                    Toast.makeText(AppointmentUpdate.this, "Invalid Date", Toast.LENGTH_SHORT).show();
                } else {
                    InsertAppointment();
                }
            }
        });


    }

    private void InsertAppointment(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

                JsonObject appointment = new JsonObject();

                appointment.addProperty("appointmentId", id.toString());
                appointment.addProperty("docterName", doctorName.toString());
                appointment.addProperty("location", location.toString());
                appointment.addProperty("availibilityDate", txtSelectedDate.getText().toString());
                appointment.addProperty("availibilityTime", txtSelectedTime.getText().toString());
                appointment.addProperty("status", status.toString());

                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody requestBody = RequestBody.create(gson.toJson(appointment), MediaType.get("application/json"));
                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/UpdateAppointment")
                        .post(requestBody)
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    Log.i("updateappointment", "appointmentResponse"+responseText);

                    JsonObject responseJson = gson.fromJson(responseText, JsonObject.class);
                    String conternt = responseJson.get("message").getAsString();

                    Log.i("updateappointment", " "+conternt);
//                    Intent intent = new Intent(AppointmentUpdate.this, AppointmentsFragment.class);
//                    startActivity(intent);


                    


                } catch (IOException e) {
                    Log.e("updateappointment", e.getMessage());
                }
            }
        }).start();

    }

    private void showTimePicker() {
        // Get the current time
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog with 24-hour format enabled
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                AppointmentUpdate.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        // Format time as HH:MM (24-hour format)
                        String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);

                        // Set formatted time to the TextView
                        txtSelectedTime.setText(formattedTime);
                    }
                },
                hour, minute, true // Set to 'true' for 24-hour format
        );

        timePickerDialog.show();
    }


    private void showDatePicker() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AppointmentUpdate.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        txtSelectedDate.setText(selectedDate);
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}