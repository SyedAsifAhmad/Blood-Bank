package com.asifahmad.donatelife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat extends AppCompatActivity {

    TextView usernameText;
    ImageView senbtn;
    FirebaseFirestore mFirestore;
    CollectionReference mChatsCollection;
    RecyclerView mChatsRecyclerView;
    EditText mMessageEditText;

    // MessageAdpater mChatsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        usernameText = findViewById(R.id.usernametext);
        senbtn = findViewById(R.id.sendbtn);


        // Initialize Firebase
        mFirestore = FirebaseFirestore.getInstance();
        mChatsCollection = mFirestore.collection("chats");

        // Initialize RecyclerView
        mChatsRecyclerView = findViewById(R.id.messagerecycler);
        mChatsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter
        // mMessages = new ArrayList<>();
        //  mChatsAdapter = new MessageAdpater(this, mMessages);
        //mChatsRecyclerView.setAdapter(mChatsAdapter);

        mMessageEditText = findViewById(R.id.messagetext);
        //String chatId = generateChatId(senderId, receiverId);

        Map<String, Object> message = new HashMap<>();
        message.put("senderId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        message.put("receiverId", "receiverUserID");
        message.put("timestamp", FieldValue.serverTimestamp());
        message.put("message", mMessageEditText);

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(message);

        senbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageText = mMessageEditText.getText().toString();

                // Clear the message input field
                mMessageEditText.setText("");

                // Add message details to the map
                message.put("senderId", FirebaseAuth.getInstance().getCurrentUser().getUid()); // Get sender ID from Firebase
                message.put("receiverId", "receiverUserID"); // Replace with actual receiver ID
                message.put("timestamp", FieldValue.serverTimestamp()); // Use server timestamp
                message.put("message", messageText);

                // Add message to Firestore
                FirebaseFirestore.getInstance().collection("chats")
                        .add(message)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Message added successfully
                                Log.d("TAG", "Message added successfully: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error adding message
                                Log.w("TAG", "Error adding message", e);
                            }
                        });
            }
        });

        Intent intent = getIntent();
        String receivedName = intent.getStringExtra("Name");
        usernameText.setText(receivedName);


    }

    private String generateChatId(String senderId, String receiverId) {
        // Implement your logic to generate a unique chat ID
        // This can be a combination of sender and receiver IDs, or a random string generator
        return "";
    }
}