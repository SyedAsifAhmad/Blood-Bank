package com.asifahmad.donatelife;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class PersonalDetails extends AppCompatActivity {

    ActionBar actionBar;

    EditText NameText, cityText, DobText, BloodDisease, Phone;
    AutoCompleteTextView bloodSpinner, GenderSpinner;

    ProgressBar progressBar;
    ImageView profileImage, buttonAdd;
    Button saveBtn;
    FirebaseFirestore db;
    StorageReference storageReference;
    Uri image;
    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            image = result.getData().getData();
            Glide.with(getApplicationContext()).load(image).into(profileImage);
        } else {
            Toast.makeText(PersonalDetails.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    });
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        initializeViews();

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Personal Details");
        }

        FirebaseApp.initializeApp(this);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        calendar = Calendar.getInstance();

        DobText.setOnClickListener(v -> new DatePickerDialog(PersonalDetails.this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String format = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            DobText.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());


        String[] bloodTypes = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};

        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, bloodTypes);

        bloodSpinner.setAdapter(bloodAdapter);

        String[] genders = {"Male", "Female", "Prefer not to say"};

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genders);

        GenderSpinner.setAdapter(genderAdapter);

        buttonAdd.setOnClickListener(v -> {

            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            resultLauncher.launch(i);

        });

        saveBtn.setOnClickListener(v -> {

            Map<String, Object> userDetails = new HashMap<>();

            boolean allFieldsFilled = true;

            if (NameText.getText().toString().trim().isEmpty()) {
                NameText.setError("Required");
                allFieldsFilled = false;
            } else {
                userDetails.put("Name", NameText.getText().toString().trim());
            }

            if (cityText.getText().toString().trim().isEmpty()) {
                cityText.setError("Required");
                allFieldsFilled = false;
            } else {
                userDetails.put("Email", cityText.getText().toString().trim());
            }

            if (Phone.getText().toString().trim().isEmpty()) {
                Phone.setError("Required");
                allFieldsFilled = false;
            } else {
                userDetails.put("PhoneNumber", Phone.getText().toString().trim());
            }

            if (bloodSpinner.getText().toString().trim().isEmpty()) {
                bloodSpinner.setError("Required");
                allFieldsFilled = false;
            } else {
                userDetails.put("BloodType", bloodSpinner.getText().toString());
            }

            if (GenderSpinner.getText().toString().trim().isEmpty()) {
                GenderSpinner.setError("Required");
                allFieldsFilled = false;
            } else {
                userDetails.put("Gender", GenderSpinner.getText().toString());
            }

            if (DobText.getText().toString().trim().isEmpty()) {
                DobText.setError("Required");
                allFieldsFilled = false;
            } else {
                userDetails.put("DOB", DobText.getText().toString());
            }

            userDetails.put("Disease", BloodDisease.getText().toString().trim());


            if (!allFieldsFilled) {

                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (image == null) {

                Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
                return;
            }

            String userEmail = cityText.getText().toString();

            DocumentReference documentReference = db.collection("users").document(userEmail);

            StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());

            reference.putFile(image).addOnSuccessListener(taskSnapshot -> {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    userDetails.put("ImageUrl", uri.toString());
                    documentReference.set(userDetails).addOnSuccessListener(unused -> {

                        Toast.makeText(PersonalDetails.this, "Details added Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PersonalDetails.this, MainActivity.class));

                    }).addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding document", e);
                    });

                });

            }).addOnFailureListener(e -> {

                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show();

            });

        });

    }

    void initializeViews() {
        NameText = findViewById(R.id.NameText);
        cityText = findViewById(R.id.cityText);
        DobText = findViewById(R.id.dobText);
        BloodDisease = findViewById(R.id.disseaseText);
        Phone = findViewById(R.id.TextPhone);

        bloodSpinner = findViewById(R.id.bloodSpinner);
        GenderSpinner = findViewById(R.id.GenderSpinner);

        profileImage = findViewById(R.id.profileImg);

        buttonAdd = findViewById(R.id.add_button);
        saveBtn = findViewById(R.id.saveBtn);

    }
}