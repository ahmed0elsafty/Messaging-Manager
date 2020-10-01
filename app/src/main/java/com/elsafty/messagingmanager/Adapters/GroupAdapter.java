package com.elsafty.messagingmanager.Adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
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
    private SparseBooleanArray selected_items;
    private int current_selected_idx = -1;

    public GroupAdapter(Context mContext, List<MyGroup> items) {
        this.ctx = mContext;
        this.items = items;
        selected_items = new SparseBooleanArray();
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

        holder.lyt_parent.setActivated(selected_items.get(position, false));

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener == null) return;
                onClickListener.onItemClick(v, group, position);
            }
        });

        holder.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onClickListener == null) return false;
                onClickListener.onItemLongClick(v, group, position);
                return true;
            }
        });

        toggleCheckedIcon(holder, position);
        displayImage(holder, group);

    }

    private void displayImage(ViewHolder holder, MyGroup group) {
        if (group.getPhoto() != null) {
            holder.image.setImageResource(Integer.parseInt((group.getPhoto())));
            holder.image.setColorFilter(null);
            holder.image_letter.setVisibility(View.GONE);
        } else {
            holder.image.setImageResource(R.drawable.shape_circle);
            holder.image.setColorFilter(group.getColor());
            holder.image_letter.setVisibility(View.VISIBLE);
        }
    }

    private void toggleCheckedIcon(ViewHolder holder, int position) {
        if (selected_items.get(position, false)) {
            holder.lyt_image.setVisibility(View.GONE);
            holder.lyt_checked.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        } else {
            holder.lyt_checked.setVisibility(View.GONE);
            holder.lyt_image.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /*public MyGroup getItem(int position) {
        return items.get(position);
    }*/

    /*public void removeData(int position) {
        items.remove(position);
        resetCurrentIndex();
    }
*/
    private void resetCurrentIndex() {
        current_selected_idx = -1;
    }

   /* public void toggleSelection(int pos) {
        current_selected_idx = pos;
        if (selected_items.get(pos, false)) {
            selected_items.delete(pos);
        } else {
            selected_items.put(pos, true);
        }
        notifyItemChanged(pos);
    }
*/
    /*public void clearSelections() {
        selected_items.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selected_items.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selected_items.size());
        for (int i = 0; i < selected_items.size(); i++) {
            items.add(selected_items.keyAt(i));
        }
        return items;
    }*/

    public interface OnClickListener {
        void onItemClick(View view, MyGroup obj, int pos);
        void onItemLongClick(View view, MyGroup obj, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, number, image_letter;
        public CircularImageView image;
        public RelativeLayout lyt_checked, lyt_image;
        public View lyt_parent;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            number = view.findViewById(R.id.number_of_members);
            image_letter = view.findViewById(R.id.image_letter);
            image = view.findViewById(R.id.image);
            lyt_checked = view.findViewById(R.id.lyt_checked);
            lyt_image = view.findViewById(R.id.lyt_image);
            lyt_parent = view.findViewById(R.id.lyt_parent);
        }
    }
}