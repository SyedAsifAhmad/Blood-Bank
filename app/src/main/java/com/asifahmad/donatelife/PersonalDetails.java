package com.asifahmad.donatelife;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class PersonalDetails extends AppCompatActivity {

    private EditText NameText, cityText, DobText, BloodDisease, Phone;
    private AutoCompleteTextView bloodSpinner, GenderSpinner;
    private ImageView profileImage, buttonAdd;
    private Button saveBtn;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;

    private Uri image;
    private Calendar calendar;

    // Image picker
    private final ActivityResultLauncher<Intent> resultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    image = result.getData().getData();
                    Glide.with(this).load(image).into(profileImage);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        FirebaseApp.initializeApp(this);

        initFirebase();
        initViews();
        setupSpinners();
        setupDatePicker();
        setupListeners();
    }

    //  Init Firebase
    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        calendar = Calendar.getInstance();
    }

    //  Init Views
    private void initViews() {
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

    //  Spinners
    private void setupSpinners() {
        String[] bloodTypes = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        bloodSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, bloodTypes));

        String[] genders = {"Male", "Female", "Prefer not to say"};
        GenderSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genders));
    }

    //  Date Picker
    private void setupDatePicker() {
        DobText.setOnClickListener(v -> new DatePickerDialog(this, (view, year, month, day) -> {
            calendar.set(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            DobText.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    //  Listeners
    private void setupListeners() {
        buttonAdd.setOnClickListener(v -> pickImage());
        saveBtn.setOnClickListener(v -> saveData());
    }

    //  Image Picker
    private void pickImage() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        resultLauncher.launch(i);
    }

    // Save Data
    private void saveData() {

        Map<String, Object> userDetails = new HashMap<>();

        // Validation
        if (NameText.getText().toString().trim().isEmpty()) {
            NameText.setError("Required");
            return;
        }

        // Data put
        userDetails.put("Name", NameText.getText().toString().trim());
        userDetails.put("City", cityText.getText().toString().trim());
        userDetails.put("Phone", Phone.getText().toString().trim());
        userDetails.put("BloodType", bloodSpinner.getText().toString());
        userDetails.put("Gender", GenderSpinner.getText().toString());
        userDetails.put("DOB", DobText.getText().toString());
        userDetails.put("Disease", BloodDisease.getText().toString().trim());

        // Get user email safely
        if (firebaseAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userEmail = firebaseAuth.getCurrentUser().getEmail();

        if (userEmail == null) {
            Toast.makeText(this, "Email not found", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference docRef = db.collection("users").document(userEmail);

        //  If image selected → upload
        if (image != null) {

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID());

            ref.putFile(image).addOnSuccessListener(task -> {
                task.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {

                    userDetails.put("imageUrl", uri.toString());
                    saveToFirestore(docRef, userDetails);

                });
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show();
            });

        } else {
            //  No image → direct save
            saveToFirestore(docRef, userDetails);
        }
    }

    //  Save to Firestore
    private void saveToFirestore(DocumentReference docRef, Map<String, Object> data) {

        docRef.set(data).addOnSuccessListener(unused -> {

            Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));

        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error saving data", e);
        });
    }
}