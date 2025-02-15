package lk.oodp2.mediconnect01;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import lk.oodp2.mediconnect01.ui.Appointments.AppointmentsFragment;
import lk.oodp2.mediconnect01.ui.home.HomeFragment;
import lk.oodp2.mediconnect01.ui.notifications.NotificationsFragment;
import lk.oodp2.mediconnect01.ui.search.AdvanceSearchFragment;

public class PatientsHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patients_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load the default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        // Handle bottom navigation item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.navigation_search) {
                selectedFragment = new AdvanceSearchFragment();
            } else if (item.getItemId() == R.id.navigation_appointments) {
                selectedFragment = new AppointmentsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }

            return true;
        });

    }
}