package lk.oodp2.mediconnect01;

import android.os.Bundle;
import android.view.View;
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

        // Find TextViews
        TextView textViewName = findViewById(R.id.textView23);
        TextView textViewCity = findViewById(R.id.textView24);

        // Set data to TextViews
        if (docterName != null) {
            textViewName.setText(docterName);
        }
        if (docterCity != null) {
            textViewCity.setText(docterCity);
        }

        ImageView imageView11 = findViewById(R.id.imageView11);
        imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DocterDetailView.this, "Calling", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
