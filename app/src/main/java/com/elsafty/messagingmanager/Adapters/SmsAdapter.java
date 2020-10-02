package com.elsafty.messagingmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elsafty.messagingmanager.Pojos.RealMessage;
import com.elsafty.messagingmanager.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsVIewHolder> {
    private ArrayList<RealMessage> messages;
    private Context mContext;
    public SmsAdapter(Context mContext, ArrayList<RealMessage> messages) {
        this.mContext = mContext;
        this.messages = messages;
    }

    @NonNull
    @Override
    public SmsVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SmsVIewHolder(LayoutInflater
                .from(mContext).inflate(R.layout.sms_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SmsVIewHolder holder, int position) {
        RealMessage message = messages.get(position);
        holder.txtName.setText(message.getName());
        holder.txtMessage.setText(message.getTxtMessage());
        holder.txtDate.setText(message.getDate());
        holder.txtTime.setText(message.getTime());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class SmsVIewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtMessage;
        private TextView txtTime;
        private TextView txtDate;
        public SmsVIewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_user_name);
            txtTime = itemView.findViewById(R.id.txt_time_message);
            txtMessage = itemView.findViewById(R.id.txt_message);
            txtDate = itemView.findViewById(R.id.txt_sms_date);
        }
    }
}
