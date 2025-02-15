package lk.oodp2.mediconnect01;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DocterDetailView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docter_detail_view);

        // Retrieve intent data
        String docterName = getIntent().getStringExtra("docterName");
        String docterCity = getIntent().getStringExtra("docterCity");
        String Price = getIntent().getStringExtra("Price");
        String rate = getIntent().getStringExtra("rate");
        String about = getIntent().getStringExtra("about");
        String experiance = getIntent().getStringExtra("experiance");
        String location = getIntent().getStringExtra("location");
        String mobile = getIntent().getStringExtra("mobile");

        // Find TextViews
        TextView textViewName = findViewById(R.id.textView23);
        TextView textViewCity = findViewById(R.id.textView24);
        TextView textViewRate = findViewById(R.id.textView31);
        TextView textViewAbout = findViewById(R.id.textView37);
        TextView textViewExperiance = findViewById(R.id.textView29);
        TextView textViewLocation = findViewById(R.id.textView40);

        // Set data to TextViews
        if (docterName != null) {
            textViewName.setText(docterName);
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

        ImageView imageView11 = findViewById(R.id.imageView11);
        imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DocterDetailView.this, "Calling"+" "+mobile, Toast.LENGTH_SHORT).show();
            }
        });

        Button button15 = findViewById(R.id.button15);
        button15.setText("Book Appointment" + " : " + Price);

    }
}
