package com.asifahmad.donatelife;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Acceptors extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DonationFetchAdapter adapter;
    private DonationFetchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptors);

        recyclerView = findViewById(R.id.recyclerView);

        // RecyclerView setup
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DonationFetchAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // ViewModel initialize
        viewModel = new ViewModelProvider(this)
                .get(DonationFetchViewModel.class);

        // Observe data
        viewModel.getDonations().observe(this, list -> {
            adapter.setData(list);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add) {

            Intent intent = new Intent(this, AddDonations.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}