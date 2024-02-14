package com.asifahmad.donatelife;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PersonalDetails extends AppCompatActivity {

    ActionBar actionBar;
    EditText NameText, EmailText, BloodType, GenderText, DobText, BloodDisease, Phone;
    Spinner bloodSpinner, GenderSpinner;
    ProgressBar progressBar;
    ImageView profileImage, buttonAdd;
    Button saveBtn;
    FirebaseFirestore db;
    StorageReference storageReference;
    Uri image;
    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == RESULT_OK) {
                if (o.getData() != null) {
                    image = o.getData().getData();
                    buttonAdd.setEnabled(true);
                    Glide.with(getApplicationContext()).load(image).into(profileImage);
                }
            } else {
                Toast.makeText(PersonalDetails.this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Personal Details");
        }

        db = FirebaseFirestore.getInstance();

        NameText = findViewById(R.id.NameText);
        EmailText = findViewById(R.id.EmailText);
        BloodType = findViewById(R.id.bloodType);
        DobText = findViewById(R.id.dobText);
        GenderText = findViewById(R.id.GenderText);
        BloodDisease = findViewById(R.id.disseaseText);
        Phone = findViewById(R.id.TextPhone);

        bloodSpinner = findViewById(R.id.bloodSpinner);
        GenderSpinner = findViewById(R.id.GenderSpinner);

        progressBar = findViewById(R.id.BarProgress);
        saveBtn = findViewById(R.id.saveBtn);

        profileImage = findViewById(R.id.profileImg);
        buttonAdd = findViewById(R.id.add_button);
//        uploadBtn = findViewById(R.id.upload_btn);

        FirebaseApp.initializeApp(this);

        storageReference = FirebaseStorage.getInstance().getReference();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                resultLauncher.launch(i);
            }
        });

        List<String> genderList = new ArrayList<>();
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Prefer not to say");

        ArrayAdapter<String> genderadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderList);
        GenderSpinner.setAdapter(genderadapter);

        genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GenderText.setInputType(InputType.TYPE_NULL);

        GenderText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    GenderSpinner.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });

        GenderText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    GenderSpinner.setVisibility(View.GONE);
                }
            }
        });
        GenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = genderList.get(position);
                GenderText.setText(selectedGender);

                GenderSpinner.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> bloodTypes = new ArrayList<>();
        bloodTypes.add("Select Blood Type");
        bloodTypes.add("A+");
        bloodTypes.add("A-");
        bloodTypes.add("B+");
        bloodTypes.add("B-");
        bloodTypes.add("O+");
        bloodTypes.add("O-");
        bloodTypes.add("AB+");
        bloodTypes.add("AB-");

        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodTypes);

        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodSpinner.setAdapter(bloodAdapter);
        BloodType.setInputType(InputType.TYPE_NULL);

        BloodType.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    bloodSpinner.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });

        BloodType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    bloodSpinner.setVisibility(View.GONE);
                }
            }
        });
        bloodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedBlood = bloodTypes.get(position);
                BloodType.setText(selectedBlood);

                bloodSpinner.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Map<String, Object> userDetails = new HashMap<>();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                boolean allFieldsFilled = true;

                if (NameText.getText().toString().trim().isEmpty()) {
                    NameText.setError("Required");
                    allFieldsFilled = false;
                } else {
                    userDetails.put("Name", NameText.getText().toString().trim());
                }
                if (EmailText.getText().toString().trim().isEmpty()) {
                    EmailText.setError("Required");
                    allFieldsFilled = false;
                } else {
                    userDetails.put("Email", EmailText.getText().toString().trim());
                }
                if (Phone.getText().toString().trim().isEmpty()) {
                    Phone.setError("Required");
                    allFieldsFilled = false;
                } else {
                    userDetails.put("PhoneNumber", Phone.getText().toString().trim());
                }
                if (BloodType.getText().toString().trim().isEmpty() || BloodType.getText().toString().trim().equals("Select Blood Type")) {
                    BloodType.setError("Required");
                    allFieldsFilled = false;
                } else {
                    userDetails.put("BloodType", BloodType.getText().toString().trim());
                }
                if (DobText.getText().toString().trim().isEmpty()) {
                    DobText.setError("Required");
                    allFieldsFilled = false;
                } else {
                    userDetails.put("DOB", DobText.getText().toString().trim());
                }
                if (GenderText.getText().toString().trim().isEmpty() || GenderText.getText().toString().trim().equals("Select Gender")) {
                    GenderText.setError("Required");
                    allFieldsFilled = false;
                } else {
                    userDetails.put("Gender", GenderText.getText().toString().trim());
                }
                userDetails.put("Disease", BloodDisease.getText().toString().trim());


                if (allFieldsFilled) {
                    if (image == null) {
                        Toast.makeText(PersonalDetails.this, "Please select an image", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    progressBar.setVisibility(View.VISIBLE);

                    String userEmail = EmailText.getText().toString();
                    String id = db.collection("users").document().getId();
                    userDetails.put("Id", id);


                    if (userEmail != null && !userEmail.isEmpty()) {
                        DocumentReference documentReference = db.collection("users").document(userEmail);

                        StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());
                        reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get the image download URL
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful()) ;
                                Uri downloadUrl = urlTask.getResult();

                                // Add image URL to user details
                                userDetails.put("ImageUrl", downloadUrl.toString());

                                // Save user details to Firestore
                                documentReference.set(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        //Toast.makeText(PersonalDetails.this, "Image upload Success", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PersonalDetails.this, "Image upload Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        documentReference.set(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PersonalDetails.this, "Details added Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(PersonalDetails.this, MainActivity.class);
                                startActivity(i);

                                progressBar.setVisibility(View.GONE);
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
                    } else {
                        Toast.makeText(PersonalDetails.this, "User email is null or empty", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(PersonalDetails.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

//    private void uploadBtn(Uri image) {
//
//        StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());
//        reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                Toast.makeText(PersonalDetails.this, "Image upload successfully", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(PersonalDetails.this, "Image upload Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}