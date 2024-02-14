package com.asifahmad.donatelife;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asifahmad.donatelife.adapters.requestAdapter;
import com.asifahmad.donatelife.model.RequestListModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RequestList extends AppCompatActivity {

    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    com.asifahmad.donatelife.adapters.requestAdapter requestAdapter;
    ArrayList<RequestListModel> modelArrayList;
    ProgressBar progressBar;
    TextView dataText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        dataText = findViewById(R.id.noDataText);

        recyclerView = findViewById(R.id.Recycler_view);
        progressBar = findViewById(R.id.progressBar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Action Bar ko display karein
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Blood Requests");
        }

        firestore = FirebaseFirestore.getInstance();
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        modelArrayList = new ArrayList<>();
        requestAdapter = new requestAdapter(modelArrayList);

        recyclerView.setAdapter(requestAdapter);
        progressBar.setVisibility(View.VISIBLE);
        firestore.collection("requests").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                if (list.isEmpty()) {
                    dataText.setVisibility(View.VISIBLE);
                    dataText.setText("No Data");
                    recyclerView.setVisibility(View.GONE);
                } else {
                    for (DocumentSnapshot d : list) {
                        RequestListModel obj = d.toObject(RequestListModel.class);
                        modelArrayList.add(obj);
                    }
                    requestAdapter.notifyDataSetChanged();
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
            Intent intent = new Intent(this, BloodRequest.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}