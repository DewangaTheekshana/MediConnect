package lk.oodp2.mediconnect01;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoctorMap extends AppCompatActivity {

    private double latitude;
    private double longitude;
    private boolean isLocationAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String doctor_id = getIntent().getStringExtra("doctor_id");
        Log.i("MapNotify", "onCreate: doctor_id: " + doctor_id);

        // Fetch location data in a separate thread
        new Thread(() -> {
            Gson gson = new Gson();
            OkHttpClient okHttpClient = new OkHttpClient();

            JsonObject search = new JsonObject();
            search.addProperty("docterId", doctor_id);

            RequestBody requestBody = RequestBody.create(gson.toJson(search), MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(BuildConfig.URL + "/MapLoad")
                    .post(requestBody)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String responseText = response.body().string();
                    Log.i("MapNotify", responseText);

                    JsonObject responseJson = gson.fromJson(responseText, JsonObject.class);
                    String locationString = responseJson.get("message").getAsString(); // Example: "6.9271,79.8612"

                    // Split latitude and longitude from the string
                    String[] latLngArray = locationString.split(",");
                    if (latLngArray.length == 2) {
                        latitude = Double.parseDouble(latLngArray[0]);
                        longitude = Double.parseDouble(latLngArray[1]);
                        isLocationAvailable = true;
                    }
                }
            } catch (IOException | NumberFormatException e) {
                Log.e("MapNotify", "Error fetching map location: " + e.getMessage());
            }
        }).start();

        // Load Google Map
        SupportMapFragment supportMapFragment = new SupportMapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fregmentMap, supportMapFragment);
        fragmentTransaction.commit();

        // Set up the map when ready
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            public void onMapReady(@NonNull GoogleMap googleMap) {
                Log.i("MapNotify", "Map is ready");

                if (isLocationAvailable) {
                    LatLng orderLocation = new LatLng(latitude, longitude);
                    googleMap.addMarker(new MarkerOptions().position(orderLocation).title("Doctor's Location"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(orderLocation, 15));
                } else {
                    Log.e("MapNotify", "Location data is not available");
                }
            }
        });
    }
}
