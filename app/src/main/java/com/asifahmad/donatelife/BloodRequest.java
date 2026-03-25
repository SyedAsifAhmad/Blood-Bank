package com.asifahmad.donatelife;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class BloodRequest extends AppCompatActivity {

    FirebaseFirestore database = FirebaseFirestore.getInstance();
    Button submitBtn;
    private TextInputEditText nameEt, unitsEt, hospitalEt, locationEt, phoneEt;
    private GridLayout gridLayout;
    private RadioGroup urgencyGroup;

    private String selectedBloodType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request);

        initViews();


        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View view = gridLayout.getChildAt(i);

            if (view instanceof TextView) {
                view.setOnClickListener(v -> {

                    // Reset all
                    for (int j = 0; j < gridLayout.getChildCount(); j++) {
                        gridLayout.getChildAt(j).setBackgroundResource(R.drawable.bg_blood_box);
                    }
                    // Select current
                    v.setBackgroundResource(R.drawable.bg_selected_blood);
                    selectedBloodType = v.getTag().toString();
                    Toast.makeText(BloodRequest.this, "Selected" + selectedBloodType, Toast.LENGTH_SHORT).show();
                });
            }
        }

        submitBtn.setOnClickListener(view -> {


        });

    }
    private void initViews() {
        gridLayout = findViewById(R.id.gridLayout);

        nameEt = findViewById(R.id.nameEt);
        unitsEt = findViewById(R.id.unitsEt);
        hospitalEt = findViewById(R.id.hospitalEt);
        locationEt = findViewById(R.id.locationEt);
        phoneEt = findViewById(R.id.phoneEt);

        urgencyGroup = findViewById(R.id.urgencyGroup);
        submitBtn = findViewById(R.id.submitBtn);
    }
}