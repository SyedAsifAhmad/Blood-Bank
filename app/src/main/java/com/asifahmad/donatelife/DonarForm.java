package com.asifahmad.donatelife;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DonarForm extends AppCompatActivity {

    EditText FullName, phoneNo, email, bloodType, dob, gender, dissease;

    Spinner bloodSpinner, genderSpinner;

    Button saveBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donar_form);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Action Bar ko display karein
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Donate Blood");
        }

        progressBar = findViewById(R.id.BarProgress);
        progressBar.setVisibility(View.GONE);

        FirebaseApp.initializeApp(this);

        bloodType = findViewById(R.id.bloodType);
        bloodSpinner = findViewById(R.id.bloodSpinner);

        gender = findViewById(R.id.GenderText);
        genderSpinner = findViewById(R.id.GenderSpinner);


        List<String> genderList = new ArrayList<>();
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");

        ArrayAdapter<String> genAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderList);
        genderSpinner.setAdapter(genAdapter);

        genAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setInputType(InputType.TYPE_NULL);

        gender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    genderSpinner.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        gender.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                genderSpinner.setVisibility(View.GONE);
            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGen = genderList.get(position);
                gender.setText(selectedGen);

                genderSpinner.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> bloodTypes = new ArrayList<>();
        bloodTypes.add("Select Blood Type");
        bloodTypes.add("O+");
        bloodTypes.add("O-");
        bloodTypes.add("A+");
        bloodTypes.add("A-");
        bloodTypes.add("B+");
        bloodTypes.add("B-");
        bloodTypes.add("AB+");
        bloodTypes.add("AB-");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodTypes);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bloodSpinner.setAdapter(adapter);
        // bloodSpinner.setVisibility(View.GONE);

        bloodType.setInputType(InputType.TYPE_NULL);


        bloodType.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Show the spinner
                    bloodSpinner.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });

        bloodType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Hide the spinner when focus is lost
                    bloodSpinner.setVisibility(View.GONE);
                }
            }
        });


        bloodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedblood = bloodTypes.get(position);
                bloodType.setText(selectedblood);

                bloodSpinner.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FullName = findViewById(R.id.fullNameText);
        phoneNo = findViewById(R.id.PhoneNo);
        email = findViewById(R.id.EmailText);
        dob = findViewById(R.id.dobText);
        dissease = findViewById(R.id.disseaseText);


        Map<String, Object> user = new HashMap<>();

        saveBtn = findViewById(R.id.svaeBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                boolean allFieldsFilled = true;

                if (FullName.getText().toString().trim().isEmpty()) {
                    FullName.setError("Required");
                    allFieldsFilled = false;
                } else {
                    user.put("Name", FullName.getText().toString().trim());
                }

                if (phoneNo.getText().toString().trim().isEmpty()) {
                    phoneNo.setError("Required");
                    allFieldsFilled = false;
                } else {
                    user.put("PhoneNo", phoneNo.getText().toString().trim());
                }

                if (email.getText().toString().trim().isEmpty()) {
                    email.setError("Required");
                    allFieldsFilled = false;
                } else {
                    user.put("Email", email.getText().toString().trim());
                }

                if (bloodType.getText().toString().trim().isEmpty() || bloodType.getText().toString().trim().equals("Select Blood Type")) {
                    bloodType.setError("Required");
                    allFieldsFilled = false;

                } else {
                    user.put("BloodType", bloodType.getText().toString().trim());
                }

                if (dob.getText().toString().trim().isEmpty()) {
                    dob.setError("Required");
                    allFieldsFilled = false;
                } else {
                    user.put("DOB", dob.getText().toString().trim());
                }

                if (gender.getText().toString().trim().isEmpty() || gender.getText().toString().trim().equals("Select Gender")) {
                    gender.setError("Required");
                    allFieldsFilled = false;
                } else {
                    user.put("Gender", gender.getText().toString().trim());
                }

                user.put("Disease", dissease.getText().toString().trim());

                if (allFieldsFilled) {
                    // Assuming user.getEmail() returns the user's email
                    String userEmail = email.getText().toString();
                    String uniqueId = db.collection("users").document().getId();
                    user.put("uniqueId",uniqueId);

                    if (userEmail != null && !userEmail.isEmpty()) {
                        // Create a reference to the "users" collection with the user's email as the document ID
                        DocumentReference userRef = db.collection("Donar").document(userEmail);

                        // Set the user data to the specified document reference
                        userRef.set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(DonarForm.this, "Details Added Successfully", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(DonarForm.this, MainActivity.class);
                                        startActivity(i);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        // Handle the case where the user's email is null or empty
                        Toast.makeText(DonarForm.this, "User email is null or empty", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    // Display a general error message or handle the case as needed
                    Toast.makeText(DonarForm.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        
    }

}



