package com.asifahmad.donatelife.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asifahmad.donatelife.R;
import com.asifahmad.donatelife.model.userDetailsModel;

import java.util.ArrayList;

public class userDetailsAdapter extends RecyclerView.Adapter<userDetailsAdapter.myviewholder> {

  ArrayList<userDetailsModel> model;

    public userDetailsAdapter(ArrayList<userDetailsModel> userDetailsModels) {
        this.model = userDetailsModels;
    }

    @NonNull
    @Override
    public userDetailsAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_user,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userDetailsAdapter.myviewholder holder, int position) {

      userDetailsModel user = this.model.get(position);

      holder.name.setText(model.get(position).getName());
      holder.phone.setText(model.get(position).getPhoneNumber());
      holder.email.setText(model.get(position).getEmail());
      holder.bloodtype.setText(model.get(position).getBloodType());
      holder.gender.setText(model.get(position).getGender());
      holder.dob.setText(model.get(position).getDOB());
      holder.disease.setText(model.get(position).getDisease());
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    static class myviewholder extends RecyclerView.ViewHolder{

    EditText name,phone,email,bloodtype,gender,dob,disease;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.NameText);
            phone = itemView.findViewById(R.id.TextPhone);
            email = itemView.findViewById(R.id.EmailText);
            bloodtype = itemView.findViewById(R.id.bloodType);
            gender = itemView.findViewById(R.id.GenderText);
            dob = itemView.findViewById(R.id.dobText);
            disease = itemView.findViewById(R.id.disseaseText);
        }
    }
}
