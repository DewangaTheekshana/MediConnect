package lk.oodp2.mediconnect01;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.io.FileSystem;

public class DoctersRegistration extends AppCompatActivity {

    private TextInputEditText editText1, editText2, editText3, editText4, editText5, editText6, editText8, editText9, editText10, editText11, editText12, editText13, editText14, editText15, editText16;
    private TextInputLayout layout1, layout2, layout3, layout4, layout5, layout6, layout8, layout9, layout10, layout11, layout12, layout13, layout14, layout15, layout16;
    private Button registerButton;
    private Animation shakeAnimation;

    private List<Uri> imageUris = new ArrayList<>();
    private ImageView imageView1, imageView2, imageView3;

    private ActivityResultLauncher<String> productImagePickerLauncher1;
    private ActivityResultLauncher<String> productImagePickerLauncher2;
    private ActivityResultLauncher<String> productImagePickerLauncher3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docters_registration);

        layout1 = findViewById(R.id.filledTextField1);
        layout2 = findViewById(R.id.filledTextField2);
        layout3 = findViewById(R.id.filledTextField3);
        layout4 = findViewById(R.id.filledTextField4);
        layout5 = findViewById(R.id.filledTextField5);
        layout6 = findViewById(R.id.filledTextField7);
        layout8 = findViewById(R.id.filledTextField8);
        layout9 = findViewById(R.id.filledTextField9);
        layout10 = findViewById(R.id.filledTextField10);
        layout11 = findViewById(R.id.filledTextField11);
        layout12 = findViewById(R.id.filledTextField12);
        layout13 = findViewById(R.id.filledTextField13);
        layout14 = findViewById(R.id.filledTextField14);
        layout14 = findViewById(R.id.filledTextField15);
        layout14 = findViewById(R.id.filledTextField16);

        editText1 = findViewById(R.id.edit_text1);
        editText2 = findViewById(R.id.edit_text2);
        editText3 = findViewById(R.id.edit_text3);
        editText4 = findViewById(R.id.edit_text4);
        editText5 = findViewById(R.id.edit_text5);
        editText6 = findViewById(R.id.edit_text6);
        editText8 = findViewById(R.id.edit_text8);
        editText9 = findViewById(R.id.edit_text9);
        editText10 = findViewById(R.id.edit_text10);
        editText11 = findViewById(R.id.edit_text11);
        editText12 = findViewById(R.id.edit_text12);
        editText13 = findViewById(R.id.edit_text13);
        editText14 = findViewById(R.id.edit_text14);
        editText15 = findViewById(R.id.edit_text15);
        editText16 = findViewById(R.id.edit_text16);

        TextView textView12 = findViewById(R.id.textView12);
        textView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctersRegistration.this, DoctersLogin.class);
                startActivity(intent);
            }
        });

        registerButton = findViewById(R.id.buttonregisterDoc);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);

        imageView1 = findViewById(R.id.imageView13);
        imageView2 = findViewById(R.id.imageView22);
        imageView3 = findViewById(R.id.imageView17);


        productImagePickerLauncher3 = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageView3.setImageURI(uri);
                        if (imageUris.size() > 0) {
                            imageUris.set(0, uri);
                        } else {
                            imageUris.add(uri);
                        }
                    }
                }
        );

        productImagePickerLauncher1 = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageView1.setImageURI(uri);
                        if (imageUris.size() > 1) {
                            imageUris.set(1, uri);
                        } else {
                            imageUris.add(uri);
                        }
                    }
                }
        );

        productImagePickerLauncher2 = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageView2.setImageURI(uri);
                        if (imageUris.size() > 2) {
                            Log.i("MediConnectggggggggggggg", "onCreate:");
                            imageUris.set(2, uri);
                        } else {
                            imageUris.add(uri);
                        }
                    }
                }
        );

        // Set click listeners for image selection
        imageView1.setOnClickListener(view -> productImagePickerLauncher1.launch("image/*"));
        imageView2.setOnClickListener(view -> productImagePickerLauncher2.launch("image/*"));
        imageView3.setOnClickListener(view -> productImagePickerLauncher3.launch("image/*"));

        registerButton.setOnClickListener(v -> validateFields());
    }

    private File getFileFromUri(Uri uri) {
        File file = null;
        try {
            // Get file name
            String fileName = "temp_image_" + System.currentTimeMillis() + ".jpg";
            File cacheDir = getCacheDir(); // Use cache directory
            file = new File(cacheDir, fileName);

            // Copy file content
            try (InputStream inputStream = getContentResolver().openInputStream(uri);
                 OutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }
        } catch (IOException e) {
            Log.e("MediConnectggggggggggggg", "Failed to convert URI to file", e);
        }
        return file;
    }

    private void validateFields() {

        Log.i("MediConnectggggggggggggg", "validateFields: " + imageUris);

        if (editText1.getText().toString().isEmpty()) {
            Toast.makeText(this, "First Name is required", Toast.LENGTH_SHORT).show();
        } else if (editText2.getText().toString().isEmpty()) {
            Toast.makeText(this, "Last Name is required", Toast.LENGTH_SHORT).show();
        } else if (editText3.getText().toString().isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
        } else if (editText4.getText().toString().isEmpty()) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
        } else if (editText5.getText().toString().isEmpty()) {
            Toast.makeText(this, "Confirm Password is required", Toast.LENGTH_SHORT).show();
        } else if (editText6.getText().toString().isEmpty()) {
            Toast.makeText(this, "Phone is required", Toast.LENGTH_SHORT).show();
        } else if (editText8.getText().toString().isEmpty()) {
            Toast.makeText(this, "Appointment Price is required", Toast.LENGTH_SHORT).show();
        } else if (editText9.getText().toString().isEmpty()) {
            Toast.makeText(this, "About is required", Toast.LENGTH_SHORT).show();
        } else if (editText10.getText().toString().isEmpty()) {
            Toast.makeText(this, "Experience is required", Toast.LENGTH_SHORT).show();
        } else if (editText11.getText().toString().isEmpty()) {
            Toast.makeText(this, "Clinic Name is required", Toast.LENGTH_SHORT).show();
        } else if (editText12.getText().toString().isEmpty()) {
            Toast.makeText(this, "Clinic Address is required", Toast.LENGTH_SHORT).show();
        } else if (editText13.getText().toString().isEmpty()) {
            Toast.makeText(this, "City is required", Toast.LENGTH_SHORT).show();
        } else if (editText14.getText().toString().isEmpty()) {
            Toast.makeText(this, "Clinic Location is required", Toast.LENGTH_SHORT).show();
        } else if (editText15.getText().toString().isEmpty()) {
            Toast.makeText(this, "Availibility Time From is required", Toast.LENGTH_SHORT).show();
        } else if (editText16.getText().toString().isEmpty()) {
            Toast.makeText(this, "Availibility Time To is required", Toast.LENGTH_SHORT).show();
        }
        if (imageUris.size() < 3) {
            Toast.makeText(this, "Upload Profile Image, Doctor Identity Image, and Clinic Image", Toast.LENGTH_SHORT).show();
        } else {

            new Thread(() -> {
                OkHttpClient okHttpClient = new OkHttpClient();
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

                Gson gson = new Gson();
                JsonObject docter = new JsonObject();
                docter.addProperty("firstName", editText1.getText().toString());
                docter.addProperty("lastName", editText2.getText().toString());
                docter.addProperty("email", editText3.getText().toString());
                docter.addProperty("password", editText4.getText().toString());
                docter.addProperty("phone", editText6.getText().toString());
                docter.addProperty("appointmentPrice", editText8.getText().toString());
                docter.addProperty("about", editText9.getText().toString());
                docter.addProperty("experience", editText10.getText().toString());
                docter.addProperty("clinicName", editText11.getText().toString());
                docter.addProperty("clinicAddress", editText12.getText().toString());
                docter.addProperty("city", editText13.getText().toString());
                docter.addProperty("clinicLocation", editText14.getText().toString());
                docter.addProperty("availibility_time_from", editText15.getText().toString());
                docter.addProperty("availibility_time_to", editText16.getText().toString());

                builder.addFormDataPart("data", gson.toJson(docter));

                for (int i = 0; i < imageUris.size(); i++) {
                    File file = getFileFromUri(imageUris.get(i));
                    if (file != null) {
                        builder.addFormDataPart("image" + (i + 1), file.getName(),
                                RequestBody.create(file, MediaType.parse("image/*")));
                    }
                }


                RequestBody requestBody = builder.build();
                Request request = new Request.Builder()
                        .url(BuildConfig.URL + "/DoctorsRegistration")
                        .post(requestBody)
                        .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String responseText = response.body().string();
                    Log.e("MediConnectggggggggggggg", responseText);

                    JsonObject responseJson = gson.fromJson(responseText, JsonObject.class);

                    String message = responseJson.get("message").getAsString();

                    if (message.equals("ok")) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, DoctersLogin.class);
                            startActivity(intent);
                        });
                    }

                } catch (IOException e) {
                    Log.e("MediConnectggggggggggggg", e.getMessage());
                }
            }).start();
        }
    }
}
