package lk.oodp2.mediconnect01;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DocterDetailView extends AppCompatActivity {

    private static final int CALL_PERMISSION_REQUEST_CODE = 1;  // Define a request code for permission

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docter_detail_view);

        // Retrieve intent data
        String doctor_id = getIntent().getStringExtra("doctor_id");
        String docterName = getIntent().getStringExtra("docterName");
        String docterCity = getIntent().getStringExtra("docterCity");
        String Price = getIntent().getStringExtra("Price");
        String rate = getIntent().getStringExtra("rate");
        String about = getIntent().getStringExtra("about");
        String experiance = getIntent().getStringExtra("experiance");
        String location = getIntent().getStringExtra("location");
        String mobile = getIntent().getStringExtra("mobile");
        String status = getIntent().getStringExtra("status");
        String availibility_time_to = getIntent().getStringExtra("availibility_time_to");
        String availibility_time_from = getIntent().getStringExtra("availibility_time_from");

        Log.i("MediConnectLogggggggggggggg", "status"+status);
//        Log.i("MediConnectLoggggggggo", doctor_id);

        // Find TextViews
        TextView textViewName = findViewById(R.id.textView23);
        TextView textViewCity = findViewById(R.id.textView24);
        TextView textViewRate = findViewById(R.id.textView31);
        TextView textViewAbout = findViewById(R.id.textView37);
        TextView textViewExperiance = findViewById(R.id.textView29);
        TextView textViewLocation = findViewById(R.id.textView40);
        CardView cardView = findViewById(R.id.statuscard1);
        // Set data to TextViews
        if (docterName != null) {
            textViewName.setText("Dr." + docterName);
        }
        if (docterCity != null) {
            textViewCity.setText(docterCity);
        }
        if (rate != null) {
            textViewRate.setText(rate);
        }
        if (about != null) {
            textViewAbout.setText(about);
        }
        if (experiance != null) {
            textViewExperiance.setText(experiance + " " + "Years");
        }
        if (location != null) {
            textViewLocation.setText(location);
        }

        if (status.equals("3.0")){
            cardView.setCardBackgroundColor(Color.parseColor("#79BF2B"));
        }else{
            cardView.setVisibility(View.GONE);
        }



        ImageView imageView11 = findViewById(R.id.imageView11);
        imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the CALL_PHONE permission is granted
                if (ContextCompat.checkSelfPermission(DocterDetailView.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission if not granted
                    ActivityCompat.requestPermissions(DocterDetailView.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
                } else {
                    // Permission is granted, initiate the call
                    makePhoneCall(mobile);
                }
            }
        });

        Button button15 = findViewById(R.id.button15);
        button15.setText("Book Appointment" + " : " + Price);
        button15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DocterDetailView.this, AppointmentBuyPage.class);
                intent.putExtra("doctor_id", doctor_id);
                intent.putExtra("docterName", docterName);
                intent.putExtra("location", location);
                intent.putExtra("Price", Price);
                intent.putExtra("availibilityTime", availibility_time_from + " -- " + availibility_time_to);
                startActivity(intent);
            }
        });

        Button button16 = findViewById(R.id.button16);
        button16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DocterDetailView.this, DoctorMap.class);
                intent.putExtra("doctor_id", doctor_id);
                startActivity(intent);
            }
        });

    }

    // Method to make a phone call
    private void makePhoneCall(String mobile) {
        if (mobile != null && !mobile.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(android.net.Uri.parse("tel:" + mobile));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid mobile number", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initiate the call
                String mobile = getIntent().getStringExtra("mobile");
                makePhoneCall(mobile);
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied to make the call", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
