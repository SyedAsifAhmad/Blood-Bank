//package com.asifahmad.donatelife.adapters;
//
//import android.content.Context;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.asifahmad.donatelife.R;
//import com.google.firebase.auth.FirebaseAuth;
//
//import java.util.List;
//
//public class MessageAdpater extends RecyclerView.Adapter<MessageAdpater.ViewHolder>  {
//
//
//
//        private Context mContext;
//        private List<Message> mMessages;
//
//        public MessageAdpater(Context context, List<Message> messages) {
//            mContext = context;
//            mMessages = messages;
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            Message message = mMessages.get(position);
//
//            holder.mMessageTextView.setText(message.getMessage());
//
//            // Set the background color of the message view
//            if (message.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                holder.mMessageTextView.setBackgroundResource(R.color.colorPrimary);
//            } else {
//                holder.mMessageTextView.setBackgroundResource(R.color.colorAccent);
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return mMessages.size();
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//
//            private TextView mMessageTextView;
//
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//
//                mMessageTextView = itemView.findViewById(R.id.message_text_view);
//            }
//        }
//    }
//
