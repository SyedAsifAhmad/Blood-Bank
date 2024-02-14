package com.asifahmad.donatelife;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static kotlin.jvm.internal.Reflection.function;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asifahmad.donatelife.adapters.adapterData;
import com.asifahmad.donatelife.model.model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Acceptors extends AppCompatActivity {


    RecyclerView recyclerView;
    ArrayList<model> datalist;

    FirebaseFirestore db;
    TextView text;
    com.asifahmad.donatelife.adapters.adapterData adapterData;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptors);

        ActionBar actionBar = getSupportActionBar();
        progressBar = findViewById(R.id.BarProgress);
        text = findViewById(R.id.noDataText);

        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Donar's List");
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        datalist = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        adapterData = new adapterData(datalist);

        recyclerView.setAdapter(adapterData);
        progressBar.setVisibility(View.VISIBLE);
        db.collection("Donar").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                if (list.isEmpty()) {
                    text.setVisibility(View.VISIBLE);
                    text.setText("No Data");
                    recyclerView.setVisibility(View.GONE);
                } else {
                    for (DocumentSnapshot d : list) {
                        model obj = d.toObject(model.class);
                        datalist.add(obj);
                    }
                    adapterData.notifyDataSetChanged();

                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            // Plus button clicked, start the new activity
            Intent intent = new Intent(this, DonarForm.class); // Replace SecondActivity with the name of your second activity
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

