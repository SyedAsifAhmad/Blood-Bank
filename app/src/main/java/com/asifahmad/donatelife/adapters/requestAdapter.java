package com.asifahmad.donatelife.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asifahmad.donatelife.Chat;
import com.asifahmad.donatelife.R;
import com.asifahmad.donatelife.model.RequestListModel;

import java.util.ArrayList;

public class requestAdapter extends RecyclerView.Adapter<requestAdapter.myviewholder> {

    ArrayList<RequestListModel> datalist;
    Context context;

    public requestAdapter (ArrayList<RequestListModel> datalist){
        this.datalist  = datalist;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_layout,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

        RequestListModel user = this.datalist.get(position);

        holder.nameText.setText(datalist.get(position).getName());
        holder.emailText.setText("Email: "+datalist.get(position).getEmail());
        holder.phoneText.setText("Phone Number: "+datalist.get(position).getPhoneNo());
        holder.bloodText.setText("Blood Type: "+datalist.get(position).getBloodType());
        holder.requestForText.setText("Request For: "+datalist.get(position).getRequestFor());

        holder.phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = user.getPhoneNo();

                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + phone));
                holder.itemView.getContext().startActivity(i);
            }
        });

        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedName = datalist.get(position).getName();  // Get the name of the selected user

                Intent i = new Intent(v.getContext(), Chat.class);
                i.putExtra("Name", selectedName);  // Pass the name to the Chat activity
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    static class myviewholder extends RecyclerView.ViewHolder {

        TextView nameText,emailText,phoneText,requestForText,bloodText;
        ImageButton phoneBtn,chatBtn;
        public myviewholder(@NonNull View itemView) {

            super(itemView);

            nameText= itemView.findViewById(R.id.nameText);
            emailText= itemView.findViewById(R.id.emailText);
            phoneText= itemView.findViewById(R.id.phoneText);
            requestForText= itemView.findViewById(R.id.requestForText);
            bloodText= itemView.findViewById(R.id.bloodText);

            phoneBtn = itemView.findViewById(R.id.phoneBtn);
            chatBtn = itemView.findViewById(R.id.chatBtn);




        }
    }
}
