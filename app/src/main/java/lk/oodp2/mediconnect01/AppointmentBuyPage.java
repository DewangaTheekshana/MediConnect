package lk.oodp2.mediconnect01;

import android.app.Activity;
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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import lk.oodp2.mediconnect01.dto.Doctors_DTO;
import lk.oodp2.mediconnect01.dto.User_DTO;
import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.StatusResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppointmentBuyPage extends AppCompatActivity {

    private TextView txtSelectedDate;
    private TextView btnSelectDate;

    private TextView txtSelectedTime;
    private TextView btnSelectTime;
    private String doctor_id;
    private String docterName;
    private String location;
    private String Price;
    private String availibilityTime;

    private static final String TAG = "AppointmentBuyPage";

    private final ActivityResultLauncher<Intent> payHereLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                    Intent data = result.getData();
                    if (data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)){
                        Serializable serializable = data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
                        if (serializable instanceof PHResponse){
                            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) serializable;
                            String msg = response.isSuccess() ? "Payment completed: " + response.getData() : "Payment failed: "+response;
                            Log.d("MediConnectLogggggggggggggg", msg);
                            InsertAppointment();
                        }
                    }
                }else if (result.getResultCode() == Activity.RESULT_CANCELED){
                    Log.d("MediConnectLogggggggggggggg", "Payment cancelled");
                }

            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment_buy_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        doctor_id = getIntent().getStringExtra("doctor_id");
        docterName = getIntent().getStringExtra("docterName");
        location = getIntent().getStringExtra("location");
        Price = getIntent().getStringExtra("Price");
        availibilityTime = getIntent().getStringExtra("availibilityTime");

        Log.i("MediConnectLoggggggggo", doctor_id+" "+docterName);

        TextView docterName1 = findViewById(R.id.textView53);
        TextView location1 = findViewById(R.id.textView54);
        TextView Price1 = findViewById(R.id.textView57);
        TextView availibilityTime1 = findViewById(R.id.textView59);

        docterName1.setText(docterName);
        location1.setText(location);
        Price1.setText(Price);
        availibilityTime1.setText(availibilityTime);


        txtSelectedDate = findViewById(R.id.textView62);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        txtSelectedTime = findViewById(R.id.textView65);
        btnSelectTime = findViewById(R.id.btnSelectTime);

        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });


        Button payButton = findViewById(R.id.button19);
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
                    Log.e("MediConnectLog", "Error parsing current date: " + ex.getMessage());
                }

                // Check if the date and time fields are filled
                if (txtSelectedDate.getText().toString().equals("None")) {
                    Toast.makeText(AppointmentBuyPage.this, "Date is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (txtSelectedTime.getText().toString().equals("None")) {
                    Toast.makeText(AppointmentBuyPage.this, "Time is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convert txtSelectedDate (String) to Date
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date selectedDateObject = null;

                try {
                    selectedDateObject = simpleDateFormat.parse(txtSelectedDate.getText().toString());
                } catch (ParseException ex) {
                    Log.e("MediConnect", "Date Parsing Error: " + ex.getMessage());
                }

                // Check if either date object is null before comparison
                if (selectedDateObject == null || formattedDateObject == null) {
                    Toast.makeText(AppointmentBuyPage.this, "Error processing dates", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Compare selected date with current date
                if (selectedDateObject.before(formattedDateObject)) {
                    Toast.makeText(AppointmentBuyPage.this, "Invalid Date", Toast.LENGTH_SHORT).show();
                } else {
                    InitatPayement();
                }
            }
        });

    }

    private void InsertAppointment(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

                SharedPreferences sharedPreferences = getSharedPreferences("lk.oodp2.mediconnect01.user", MODE_PRIVATE);
                String userJson = sharedPreferences.getString("user", null); // Retrieve JSON string

                User_DTO user_dto2 = gson.fromJson(userJson, User_DTO.class);


                JsonObject appointment = new JsonObject();

                appointment.addProperty("doctor_id", doctor_id.toString());
                appointment.addProperty("User_id", user_dto2.getId());
                appointment.addProperty("docterName", docterName.toString());
                appointment.addProperty("location", location.toString());
                appointment.addProperty("Price", Price.toString());
                appointment.addProperty("availibilityDate", txtSelectedDate.getText().toString());
                appointment.addProperty("availibilityTime", txtSelectedTime.getText().toString());

                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody requestBody = RequestBody.create(gson.toJson(appointment), MediaType.get("application/json"));
                Request request = new Request.Builder()
                        .url(BuildConfig.URL+"/BookAppointment")
                        .post(requestBody)
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    Log.i("MediConnectLoggggggggo", "appointmentResponse"+responseText);

                    Intent intent = new Intent(AppointmentBuyPage.this, PatientsHome.class);
                    startActivity(intent);


                } catch (IOException e) {
                    Log.e("MediConnectLogggggggggggggg", e.getMessage());
                }
            }
        }).start();

    }

    private void InitatPayement(){

        InitRequest req = new InitRequest();
        req.setMerchantId("1223738");       // Merchant ID
        req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
        req.setAmount(Integer.valueOf(Price));             // Final Amount to be charged
        req.setOrderId("230000123");        // Unique Reference ID
        req.setItemsDescription("Dr: "+docterName);  // Item description title
        req.setCustom1("This is the custom message 1");
        req.setCustom2("This is the custom message 2");
        req.getCustomer().setFirstName("Saman");
        req.getCustomer().setLastName("Perera");
        req.getCustomer().setEmail("samanp@gmail.com");
        req.getCustomer().setPhone("+94771234567");
        req.getCustomer().getAddress().setAddress("No.1, Galle Road");
        req.getCustomer().getAddress().setCity("Colombo");
        req.getCustomer().getAddress().setCountry("Sri Lanka");

        //Optional Params
//        req.setNotifyUrl(“xxxx”);           // Notifiy Url
//        req.getCustomer().getDeliveryAddress().setAddress("No.2, Kandy Road");
//        req.getCustomer().getDeliveryAddress().setCity("Kadawatha");
//        req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");
        req.getItems().add(new Item(null, "Dr: "+docterName, 1, Integer.valueOf(Price)));

        Intent intent = new Intent(this, PHMainActivity.class);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
        PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);

        payHereLauncher.launch(intent);

    }

    private void showTimePicker() {
        // Get the current time
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                AppointmentBuyPage.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        // Format time as HH:MM
                        String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        txtSelectedTime.setText(formattedTime);
                    }
                },
                hour, minute, true // true for 24-hour format, false for AM/PM format
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
                AppointmentBuyPage.this,
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