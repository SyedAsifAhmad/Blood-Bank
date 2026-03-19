package com.asifahmad.donatelife;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MainActivity extends AppCompatActivity {

    CardView acceptCard, RequestListCard, bloodInfo;
    FirebaseFirestore db;
    ImageView profile_image;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile_image = findViewById(R.id.profile_Image);


        // StorageReference profilePictureRef = FirebaseStorage.getInstance().getReference().child("images/").child(userId);


        if (currentUser != null) {
            StorageReference profilePictureRef = FirebaseStorage.getInstance().getReference().child("images/" + userId).child(userId);

            profilePictureRef.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profile_image.setImageURI(uri);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle errors appropriately; consider logging, displaying messages, etc.
                            Log.e("ImageRetrieval", "Failed to retrieve image:", e);
                        }
                    });
        } else {
            // Handle the case where no user is authenticated (e.g., show a sign-in prompt)
        }

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserDetails.class);
                startActivity(i);
            }
        });

        db = FirebaseFirestore.getInstance();
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Action Bar ko display karein
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("DonateLife");
        }

        acceptCard = findViewById(R.id.acceptCard);

        RequestListCard = findViewById(R.id.requestList_card);
        // requestCard = findViewById(R.id.requestCard);

        RequestListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RequestList.class);
                startActivity(i);
            }
        });

        acceptCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Acceptors.class);
                startActivity(intent);
            }
        });

        bloodInfo = findViewById(R.id.bloodInfo_card);
        bloodInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Blood_Types_info.class);
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {

            auth.signOut();

            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}