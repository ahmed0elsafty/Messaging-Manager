package com.elsafty.messagingmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elsafty.messagingmanager.Pojos.MyContact;
import com.elsafty.messagingmanager.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {

    private Context mContext;
    private List<MyContact> items;
    private MyOnClickListener myOnClickListener;

    public MembersAdapter(Context mContext, List<MyContact> items,MyOnClickListener myOnClickListener) {
        this.mContext = mContext;
        this.items = items;
        this.myOnClickListener = myOnClickListener;
    }
    public interface MyOnClickListener{
        void onClick(String name,String number,int id);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MyContact contact = items.get(position);

        // displaying text view data
        holder.name.setText(contact.getName());
        holder.number.setText(contact.getNumber());
        holder.image_letter.setText(contact.getName().substring(0, 1));

        displayImage(holder, contact);

    }

    private void displayImage(ViewHolder holder, MyContact contact) {
        if (contact.getImage() != null) {
            holder.image.setImageResource(contact.getImage());
            holder.image.setColorFilter(null);
            holder.image_letter.setVisibility(View.GONE);
        } else {
            holder.image.setImageResource(R.drawable.shape_circle);
            holder.image.setColorFilter(contact.getColor());
            holder.image_letter.setVisibility(View.VISIBLE);
        }
    }

    public MyContact getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name, number, image_letter;
        public CircularImageView image;
        public RelativeLayout lyt_checked, lyt_image;
        public View lyt_parent;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            number = view.findViewById(R.id.number);
            image_letter = view.findViewById(R.id.image_letter);
            image = view.findViewById(R.id.image);
            lyt_image = view.findViewById(R.id.lyt_image);
            lyt_parent = view.findViewById(R.id.lyt_parent);
            lyt_parent.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            myOnClickListener.onClick(getItem(getAdapterPosition()).getName(),getItem(getAdapterPosition()).getNumber(),getAdapterPosition()+1);
        }
    }
}