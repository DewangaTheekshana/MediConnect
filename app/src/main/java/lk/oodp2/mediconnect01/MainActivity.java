package lk.oodp2.mediconnect01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("lk.oodp2.mediconnect01.user", MODE_PRIVATE);
        if (sharedPreferences.contains("user")) {
            Intent intent = new Intent(MainActivity.this, PatientsHome.class);
            startActivity(intent);
            finish(); // Close this activity to prevent going back
        }

        SharedPreferences sharedPreferences2 = getSharedPreferences("lk.oodp2.mediconnect01.doctor", MODE_PRIVATE);
        if (sharedPreferences2.contains("doctor")) {
            Intent intent = new Intent(MainActivity.this, DoctorHomeActivity.class);
            startActivity(intent);
            finish(); // Close this activity to prevent going back
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open PatientsRegistation activity

                Intent intent = new Intent(MainActivity.this, PatientsRegistation.class);
                startActivity(intent);

            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DoctersRegistration.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        TextView textView4 = findViewById(R.id.textView4);
        TextView textView = findViewById(R.id.textView);
        TextView textView2 = findViewById(R.id.textView2);

        Animation animation1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.animation1);
        textView4.startAnimation(animation1);
        textView.startAnimation(animation1);
        textView2.startAnimation(animation1);
    }
}