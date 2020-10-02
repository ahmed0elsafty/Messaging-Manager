package com.elsafty.messagingmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elsafty.messagingmanager.Pojos.MyGroup;
import com.elsafty.messagingmanager.R;
import com.elsafty.messagingmanager.SQLite.SqlCommunication;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private Context ctx;
    private List<MyGroup> items;
    private OnClickListener onClickListener = null;
    private SqlCommunication sqlCommunication;
    private  OnClickListener listener;
    public GroupAdapter(Context mContext, List<MyGroup> items,OnClickListener onClickListener) {
        this.ctx = mContext;
        this.items = items;
        listener = onClickListener;
        sqlCommunication = new SqlCommunication(mContext);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MyGroup group = items.get(position);

        // displaying text view data
        holder.name.setText(group.getName());
        holder.number.setText(ctx.getResources().getQuantityString(R.plurals.numberOfMembers,group.getMembers(),group.getMembers()));
        holder.image_letter.setText(group.getName().substring(0, 1));
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public MyGroup getItem(int position) {
        return items.get(position);
    }


    public interface OnClickListener {
        void onItemClick(View view, MyGroup obj, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name, number, image_letter;
        public CircularImageView image;
        public RelativeLayout lyt_image;
        public View lyt_parent;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            number = view.findViewById(R.id.number_of_members);
            image_letter = view.findViewById(R.id.image_letter);
            image = view.findViewById(R.id.image);
            lyt_image = view.findViewById(R.id.lyt_image);
            lyt_parent = view.findViewById(R.id.lyt_parent);
            lyt_parent.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            listener.onItemClick(view,items.get(getAdapterPosition()),getAdapterPosition());
        }
    }
}