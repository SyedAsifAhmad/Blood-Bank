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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloodRequest extends AppCompatActivity {

    EditText Text_Name, Text_email, Text_bloodType, Text_Phone, requestforText;
    Button saveBtn;

    Spinner spinner_blood, requestForSpinner;

    ProgressBar progressBar;
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Action Bar ko display karein
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Request blood");
        }

        progressBar = findViewById(R.id.BarProgress);
        progressBar.setVisibility(View.GONE);

        requestForSpinner = findViewById(R.id.Spinner_requestFor);
        List<String> request = new ArrayList<>();
        request.add("Request for");
        request.add("Father");
        request.add("Mother");
        request.add("Son");
        request.add("Daughter");
        request.add("Grand Father");
        request.add("Grand Mother");
        request.add("Friend");
        request.add("Boy");
        request.add("Girl");
        request.add("colleague");

        ArrayAdapter<String> requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, request);
        requestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        requestForSpinner.setAdapter(requestAdapter);

        requestforText = findViewById(R.id.requets_forText);
        requestforText.setInputType(InputType.TYPE_NULL);

        requestforText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Show the spinner
                    requestForSpinner.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });

        requestForSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Hide the spinner when focus is lost
                    requestForSpinner.setVisibility(View.GONE);
                }
            }
        });

        requestForSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String requestItems = request.get(position);
                requestforText.setText(requestItems);

                requestForSpinner.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_blood = findViewById(R.id.Spinner_blood);
        Text_bloodType = findViewById(R.id.Text_bloodType);
        Text_email = findViewById(R.id.Text_Email);
        Text_Name = findViewById(R.id.TextFull_Name);
        Text_Phone = findViewById(R.id.Text_Phone);

        saveBtn = findViewById(R.id.button_save);

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

        spinner_blood.setAdapter(adapter);
        // bloodSpinner.setVisibility(View.GONE);

        Text_bloodType.setInputType(InputType.TYPE_NULL);

        Text_bloodType.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Show the spinner
                    spinner_blood.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });

        Text_bloodType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Hide the spinner when focus is lost
                    spinner_blood.setVisibility(View.GONE);
                }
            }
        });


        spinner_blood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedblood = bloodTypes.get(position);
                Text_bloodType.setText(selectedblood);

                spinner_blood.setVisibility(View.GONE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        FirebaseApp.initializeApp(this);

        Map<String, Object> requests = new HashMap<>();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                boolean allFields = true;

                if (requestforText.getText().toString().trim().isEmpty() || requestforText.getText().toString().trim().equals("Request for")){
                    requestforText.setError("Required");
                    allFields= false;
                }else {
                    requests.put("RequestFor",requestforText.getText().toString().trim());
                }

                if (Text_Name.getText().toString().trim().isEmpty()) {
                    Text_Name.setError("Required");
                    allFields = false;
                } else {
                    requests.put("Name", Text_Name.getText().toString().trim());
                }

                if (Text_Phone.getText().toString().trim().isEmpty()) {
                    Text_Phone.setError("Required");
                    allFields = false;
                } else {
                    requests.put("PhoneNo", Text_Phone.getText().toString().trim());
                }

                if (Text_email.getText().toString().trim().isEmpty()) {
                    Text_email.setError("Required");
                    allFields = false;
                } else {
                    requests.put("Email", Text_email.getText().toString().trim());
                }

                if (Text_bloodType.getText().toString().trim().isEmpty() || Text_bloodType.getText().toString().trim().equals("Select Blood Type")) {
                    Text_bloodType.setError("Required");
                    allFields = false;
                } else {
                    requests.put("BloodType", Text_bloodType.getText().toString().trim());
                }
                if (allFields) {

                    String userEmail = Text_email.getText().toString();
                    String uniqueId = database.collection("requests").document().getId();
                    requests.put("uniqueId",uniqueId);

                    if (userEmail != null && !userEmail.isEmpty()){
                        DocumentReference userRef = database.collection("requests").document(userEmail);
                        userRef.set(requests)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(BloodRequest.this, "Request Added Successfully", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(BloodRequest.this, MainActivity.class);
                                        startActivity(i);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding requests", e);
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    }else {
                        // Handle the case where the user's email is null or empty
                        Toast.makeText(BloodRequest.this, "User email is null or empty", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    // Display a general error message or handle the case as needed
                    Toast.makeText(BloodRequest.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }
}