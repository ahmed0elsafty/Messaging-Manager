package com.elsafty.messagingmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elsafty.messagingmanager.Activities.MainActivity;
import com.elsafty.messagingmanager.Pojos.MyGroup;
import com.elsafty.messagingmanager.R;
import com.elsafty.messagingmanager.SQLite.SqlCommunication;
import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private Context mContext;
    private List<MyGroup> items;
    private OnClickListener listener;
    private MyGroup mRecentlyDeletedGroup;
    private int mRecentlyDeletedGroupPosition;
    private MainActivity mActivity;
    private SqlCommunication sqlCommunication;

    public GroupAdapter(Context mContext, List<MyGroup> items, OnClickListener onClickListener) {
        this.mContext = mContext;
        this.items = items;
        listener = onClickListener;
        mActivity = (MainActivity) mContext;
        sqlCommunication = new SqlCommunication(mContext);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        MyGroup group = items.get(position);
        holder.name.setText(group.getName());
        holder.number.setText(mContext.getResources().getQuantityString(R.plurals.numberOfMembers, group.getMembers(), group.getMembers()));
        holder.image_letter.setText(group.getName().substring(0, 1));
        holder.image.setImageResource(R.drawable.shape_circle);
        holder.image.setColorFilter(group.getColor());
        holder.image_letter.setVisibility(View.VISIBLE);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public MyGroup getItem(int position) {
        return items.get(position);
    }

    public Context getContext() {
        return mContext;
    }

    public void deleteItem(int position) {
        mRecentlyDeletedGroup = items.get(position);
        mRecentlyDeletedGroupPosition = position;
        items.remove(position);
        sqlCommunication.deleteGroup(sqlCommunication.getGrouptId(mRecentlyDeletedGroup));
        notifyItemRemoved(position);
        showUndoSnackbar(position);
    }
    private void showUndoSnackbar(int position) {
        View view = mActivity.findViewById(R.id.content);
        Snackbar snackbar = Snackbar.make(view, "DELETE GROUP",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO DELETE", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoDelete();
            }
        });
        snackbar.show();
    }

    private void undoDelete() {

        items.add(mRecentlyDeletedGroupPosition,
                mRecentlyDeletedGroup);
        sqlCommunication.insertGroup(mRecentlyDeletedGroup);
        notifyItemInserted(mRecentlyDeletedGroupPosition);
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
            number = view.findViewById(R.id.number);
            image_letter = view.findViewById(R.id.image_letter);
            image = view.findViewById(R.id.image);
            lyt_image = view.findViewById(R.id.lyt_image);
            lyt_parent = view.findViewById(R.id.lyt_parent);
            lyt_parent.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, items.get(getAdapterPosition()), getAdapterPosition());
        }
    }
}