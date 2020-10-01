package com.elsafty.messagingmanager.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elsafty.messagingmanager.Pojos.MyMessage;
import com.elsafty.messagingmanager.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsVIewHolder> {
    ArrayList<MyMessage> messages;
    private Context mContext;

    public SmsAdapter(Context mContext, ArrayList<MyMessage> messages) {
        this.mContext = mContext;
        this.messages = messages;
    }

    @NonNull
    @Override
    public SmsVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SmsVIewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class SmsVIewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtMessage;
        private TextView txtTime;
        private ImageView image;
        public SmsVIewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_user_name);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtMessage = itemView.findViewById(R.id.txt_message);
        }
    }
}
