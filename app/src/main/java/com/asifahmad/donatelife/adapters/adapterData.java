package com.asifahmad.donatelife.adapters;

import static android.content.Intent.getIntent;

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
import com.asifahmad.donatelife.MainActivity;
import com.asifahmad.donatelife.R;
import com.asifahmad.donatelife.androidUtils.AndroidUtils;
import com.asifahmad.donatelife.model.model;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class adapterData extends RecyclerView.Adapter<adapterData.myviewholder>{

    ArrayList<model> datalist;
    Context context;

    public adapterData(ArrayList<model> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position ) {

        model user = this.datalist.get(position);

        holder.t1.setText(datalist.get(position).getName());
        holder.t2.setText("Email: "+datalist.get(position).getEmail());
        holder.t7.setText("Phone Number: "+datalist.get(position).getPhoneNo());
        holder.t3.setText("Blood Type: "+datalist.get(position).getBloodType());
        holder.t4.setText("Gender: "+datalist.get(position).getGender());
        holder.t5.setText("Date of Birth: "+datalist.get(position).getDOB());
        holder.t6.setText("Disease: "+datalist.get(position).getDisease());



        holder.phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = user.getPhoneNo();

                // Open phone dialer
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                holder.itemView.getContext().startActivity(intent);
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

    static class myviewholder extends RecyclerView.ViewHolder{

        TextView t1,t2,t3,t4,t5,t6,t7;


        ImageButton chatBtn, phoneBtn;


        public myviewholder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.t1);
            t2 = itemView.findViewById(R.id.t2);
            t3 = itemView.findViewById(R.id.t3);
            t4 = itemView.findViewById(R.id.t4);
            t5 = itemView.findViewById(R.id.t5);
            t6 = itemView.findViewById(R.id.t6);
            t7 = itemView.findViewById(R.id.t7);
            chatBtn=itemView.findViewById(R.id.chatBtn);
            phoneBtn=itemView.findViewById(R.id.phoneBtn);


        }
    }
}
