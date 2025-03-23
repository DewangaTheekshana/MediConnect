package lk.oodp2.mediconnect01;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
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

public class AppointmentBuyPage extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final int SHAKE_THRESHOLD = 400;
    private long lastUpdate;
    private float lastX, lastY, lastZ;
    Random random = new Random();
    int randomNum = 100000 + random.nextInt(900000); // Generates a 6-digit random number
    String AppointmentId = "ID"+randomNum;

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
                            firebaseInsert();
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

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

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

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastUpdate) > 100) {
                long diffTime = currentTime - lastUpdate;
                lastUpdate = currentTime;

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    // Shake detected, go back to the previous screen
                    finish();
                }

                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
                        String formattedTime = String.format("%02d:%02d %s",
                                (selectedHour == 0) ? 12 : (selectedHour > 12 ? selectedHour - 12 : selectedHour),
                                selectedMinute,
                                (selectedHour >= 12) ? "PM" : "AM"
                        );
                        txtSelectedTime.setText(formattedTime);
                    }
                },
                hour, minute, false // true for 24-hour format, false for AM/PM format
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

    public void  firebaseInsert(){

        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("lk.oodp2.mediconnect01.user", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", null); // Retrieve JSON string

        User_DTO user_dto1 = gson.fromJson(userJson, User_DTO.class); // Convert JSON to object


        HashMap<String,Object> document = new HashMap<>();
        document.put("user",String.valueOf(user_dto1.getId()));
        document.put("appointment_id", AppointmentId);
        document.put("price",Price);
        document.put("user_name", docterName);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("appoimentadd").add(document)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("AppointmentNotification", "onSuccess");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("AppointmentNotification", "error");
                    }
                });

        firestore.collection("appoimentadd")
                .whereEqualTo("user",String.valueOf(user_dto1.getId()))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("AppointmentNotification", "Listen failed.", error);
                            return;
                        }

                        if (snapshots != null) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {

                                if (dc.getType().equals(DocumentChange.Type.ADDED)){
                                    Log.d("AppointmentNotification", "notifed ");
                                    notification();
                                }

                            }
                        }
                    }
                });

    }

    public void notification() {
        // Use getActivity() to access the context of the fragment's parent activity
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "C1",
                    "Channel 1",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(notificationChannel);
        }


        Notification notification = new NotificationCompat.Builder(AppointmentBuyPage.this, "C1")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Appointment Booking!")  // Title of the notification
                .setContentText("Your Appointment #" + AppointmentId + " has been placed successfully. Total: RS." + Price)  // Content with Order ID and Total Amount
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // Priority
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 1000, 500})
                .build();

        notificationManager.notify(1, notification);
    }

}