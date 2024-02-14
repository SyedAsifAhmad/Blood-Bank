package com.asifahmad.donatelife;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.asifahmad.donatelife.adapters.userDetailsAdapter;
import com.asifahmad.donatelife.model.userDetailsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class UserDetails extends AppCompatActivity {


    RecyclerView recyclerView;
    ArrayList<userDetailsModel> dataList;
    FirebaseFirestore db;
    ProgressBar Barprogress;
    com.asifahmad.donatelife.adapters.userDetailsAdapter detailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);


        Barprogress = findViewById(R.id.loading);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();



        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Action Bar ko display karein
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Personal Details");
        }

        recyclerView = findViewById(R.id.userDetailsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        detailsAdapter = new userDetailsAdapter(dataList);
        recyclerView.setAdapter(detailsAdapter);

        if (currentUser != null) {
            String email = currentUser.getEmail();

            Barprogress.setVisibility(View.VISIBLE);
            db.collection("users").whereEqualTo("Email", email)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            Barprogress.setVisibility(View.GONE);
                            for (DocumentSnapshot d : list) {
                                userDetailsModel obj = d.toObject(userDetailsModel.class);
                                dataList.add(obj);
                                //userDetailsModel obj = d.toObject(userDetailsModel.class);
                            }

                            detailsAdapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Barprogress.setVisibility(View.GONE);
                            Toast.makeText(UserDetails.this,"Failed to load details!",Toast.LENGTH_SHORT).show();
                            Log.w("TAG", "Error retrieving user data", e);
                        }
                    });
        } else {
            // Handle the case where the user is not signed in
            Log.d("TAG", "User is not signed in");
            // You might want to redirect to a login screen here
        }
    }
}