package com.asifahmad.donatelife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView profileImage;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    RecyclerView recyclerView;
    List<String> dummyList;
    Intent intent;
    ExtendedFloatingActionButton donateFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        donateFAB = findViewById(R.id.fab);

        donateFAB.setOnClickListener(v ->{
            intent = new Intent(MainActivity.this, AddDonations.class);
            startActivity(intent);
        });

        // Setup views
        profileImage = findViewById(R.id.profile_Image);
        profileImage.setOnClickListener(v -> openUserDetails());

        // Load profile image if user is logged in
        if (currentUser != null) {
            loadProfileImage(currentUser.getUid());
        }

        // Setup cards
        setupCard(R.id.card1, R.drawable.ic_find_donor, "Find Donor");
        setupCard(R.id.card2, R.drawable.ic_blood_drop_, "Book Request");
        setupCard(R.id.card3, R.drawable.ic_warning, "My Requests");
        setupCard(R.id.card4, R.drawable.ic_location, "Centers");

        recyclerView = findViewById(R.id.recentRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Dummy data
        dummyList = new ArrayList<>();
        dummyList.add("John Doe - A+");
        dummyList.add("Jane Smith - B-");
        dummyList.add("Ali Khan - O+");
        dummyList.add("Sara Ahmed - AB+");

        // Inline adapter
        recyclerView.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.blood_request_card, parent, false);
                return new RecyclerView.ViewHolder(view) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                // Just show dummy name+blood type in card
                TextView name = holder.itemView.findViewById(R.id.requesterName);
                TextView bloodType = holder.itemView.findViewById(R.id.bloodType);
                name.setText(dummyList.get(position).split(" - ")[0]);
                bloodType.setText(dummyList.get(position).split(" - ")[1]);
            }

            @Override
            public int getItemCount() {
                return dummyList.size();
            }
        });


    }

    // Load user profile image from Firebase Storage
    private void loadProfileImage(String userId) {
        StorageReference profileRef = FirebaseStorage.getInstance()
                .getReference()
                .child("images/" + userId + "/" + userId);

        profileRef.getDownloadUrl()
                .addOnSuccessListener(uri -> profileImage.setImageURI(uri))
                .addOnFailureListener(e -> Log.e("ImageRetrieval", "Failed to retrieve image:", e));
    }

    // Open user details activity
    private void openUserDetails() {
        startActivity(new Intent(MainActivity.this, UserProfile.class));
    }

    // Setup individual cards with image and title
    private void setupCard(int cardId, int imageRes, String title) {
        View card = findViewById(cardId);
        ImageView cardImage = card.findViewById(R.id.cardImage);
        TextView cardTitle = card.findViewById(R.id.cardTitle);

        cardImage.setImageResource(imageRes);
        cardTitle.setText(title);

        card.setOnClickListener(view -> {
            if (cardId== R.id.card1){
                intent = new Intent(MainActivity.this, Acceptors.class);
                startActivity(intent);
            } else if (cardId == R.id.card2) {
                intent = new Intent(MainActivity.this, BloodRequest.class);
                startActivity(intent);
            }
        });
    }

    // Inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    // Handle menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            auth.signOut();
            startActivity(new Intent(this, SignUp.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}