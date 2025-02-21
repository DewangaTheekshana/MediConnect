package lk.oodp2.mediconnect01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;

public class DoctersRegistration extends AppCompatActivity {

    private TextInputEditText editText1, editText2, editText3, editText4, editText5, editText6;
    private TextInputLayout layout1, layout2, layout3, layout4, layout5, layout6;
    private Button registerButton;
    private Animation shakeAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docters_registration);

        // Initialize fields layout1 = findViewById(R.id.filledTextField1);
        layout2 = findViewById(R.id.filledTextField2);
        layout3 = findViewById(R.id.filledTextField3);
        layout4 = findViewById(R.id.filledTextField4);
        layout5 = findViewById(R.id.filledTextField5);
        layout6 = findViewById(R.id.filledTextField6);

        editText1 = findViewById(R.id.edit_text1);
        editText2 = findViewById(R.id.edit_text2);
        editText3 = findViewById(R.id.edit_text3);
        editText4 = findViewById(R.id.edit_text4);
        editText5 = findViewById(R.id.edit_text5);
        editText6 = findViewById(R.id.edit_text6);

        registerButton = findViewById(R.id.button);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);

        registerButton.setOnClickListener(v -> validateFields());

        TextView textView12 = findViewById(R.id.textView12);
        textView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctersRegistration.this, DoctersLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

    private void validateFields() {
        if (isFieldEmpty(editText1, layout1, "Name is required")) {
            return;
        } else if (isFieldEmpty(editText2, layout2, "Username is required")) {
            return;
        } else if (isFieldEmpty(editText3, layout3, "Email is required")) {
            return;
        } else if (isFieldEmpty(editText4, layout4, "Password is required")) {
            return;
        } else if (isFieldEmpty(editText5, layout5, "Confirm password is required")) {
            return;
        } else if (isFieldEmpty(editText6, layout6, "Phone number is required")) {
            return;
        }

        // If all fields are filled, proceed with registration
        // TODO: Add registration logic here (e.g., API call, database insert)
    }

    private boolean isFieldEmpty(TextInputEditText editText, TextInputLayout layout, String errorMessage) {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            layout.setError(errorMessage);
            layout.startAnimation(shakeAnimation);
            return true;
        } else {
            layout.setError(null);  // Remove error if valid
            return false;
        }
    }
}
