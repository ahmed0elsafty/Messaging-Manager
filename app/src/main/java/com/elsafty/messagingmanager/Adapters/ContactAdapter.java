package com.elsafty.messagingmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elsafty.messagingmanager.Pojos.Contact;
import com.elsafty.messagingmanager.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private Context mContext;
    private ArrayList<Contact> mContacts;
    private OnItemClickListener mOnItemClickListener;

    public ContactAdapter(Context mContext, ArrayList<Contact> mContacts, OnItemClickListener mOnItemClickListener) {
        this.mContext = mContext;
        this.mContacts = mContacts;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.contact_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = mContacts.get(position);
        holder.txtName.setText(contact.getName());
        holder.txtNumber.setText(contact.getNumber());
        Picasso.get().load(contact.getImagePath())
                .into(holder.imgContactPhoto);
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public interface OnItemClickListener {
        void onClick(String name, String phone);
    }

    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtName;
        private TextView txtNumber;
        private ImageView imgContactPhoto;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.contact_name);
            txtNumber = itemView.findViewById(R.id.contact_number);
            imgContactPhoto = itemView.findViewById(R.id.contact_photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Contact contact = mContacts.get(position);
            String name = contact.getName();
            String number = contact.getNumber();
            mOnItemClickListener.onClick(name, number);
        }
    }
}
