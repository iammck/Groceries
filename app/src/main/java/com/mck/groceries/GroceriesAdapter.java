package com.mck.groceries;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mck.groceries.model.Groceries;
import com.mck.groceries.model.Grocery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * RecyclerView subclass for displaying grocery items as a
 * vertical list.
 *
 * Created by Michael on 5/22/2016.
 */
public class GroceriesAdapter extends RecyclerView.Adapter implements GroceriesHelper.GroceriesUpdateListener {

    private OnViewHolderClickListener onViewHolderClickListener;
    private ArrayList<Integer> keys;

    public boolean isEmpty() {
        return keys.isEmpty();
    }

    // Provide a ref to the views for each data item
    // Complex data items may need more than one view per item,
    // and on provides access to all the views for a data item
    // in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
        private OnViewHolderClickListener onViewHolderClickListener;
        public Grocery grocery;

        public ViewHolder(View v){
            super(v);
            CheckBox cbPurchased = (CheckBox) v.findViewById(R.id.cbPurchased);
            cbPurchased.setOnCheckedChangeListener(this);
            v.setOnClickListener(this);
            v.setLongClickable(true);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onViewHolderClickListener.onViewClicked(grocery);
        }

        @Override
        public boolean onLongClick(View v) {
            onViewHolderClickListener.onViewLongClicked(grocery);
            return true;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            onViewHolderClickListener.onCheckChanged(grocery, isChecked);
        }
    }

    public interface OnViewHolderClickListener{
        void onViewClicked(Grocery grocery);
        void onViewLongClicked(Grocery grocery);
        void onCheckChanged(Grocery grocery, boolean isChecked);
    }

    // provide a suitable constructor
    public GroceriesAdapter(Activity activity, OnViewHolderClickListener clickListener){
        keys = new ArrayList<>();
        onViewHolderClickListener = clickListener;
        GroceriesHelper.instance().setUpdateListener(activity, this);
    }

    // Create new views. (invoked by the layout manager).
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grocery_item, parent, false);
        // set up view here
        ViewHolder holder = new ViewHolder(view);
        holder.onViewHolderClickListener = onViewHolderClickListener;
        return holder;
    }

    // replace the contents of a view
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // get element from data set at this position
        Grocery grocery = GroceriesHelper.instance().getGrocery(keys.get(position));
        // add in the view holders grocery item.
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.grocery = grocery;
        View view = viewHolder.itemView;
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        TextView tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
        CheckBox cbPurchased = (CheckBox) view.findViewById(R.id.cbPurchased);

        // replace the contents of the view with that element
        tvName.setText(grocery.name);
        String quantity = grocery.quantity.toString();
        if (!quantity.equals("1")) {
            Log.v("GroceriesAdapter", "quantity is being set to " + quantity);
            tvQuantity.setText(quantity);
        } else {
            tvQuantity.setText("");
        }
        if (grocery.description != null && !grocery.description.isEmpty()){
            tvDescription.setText(grocery.description);
            tvDescription.setVisibility(View.VISIBLE);
        } else {
            tvDescription.setText("");
            tvDescription.setVisibility(View.GONE);
        }
        cbPurchased.setChecked(grocery.purchased);
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    @Override
    public void onGroceriesUpdate(Set<Integer> keys) {
        this.keys = new ArrayList<>(keys);
        // sort ascending by id todo
        Collections.sort(this.keys);
        notifyDataSetChanged();
    }

    @Override
    public void onAddGrocery(Integer groceryId) {
        if (!keys.contains(groceryId))
            keys.add(groceryId);
        notifyItemChanged(keys.indexOf(groceryId));
    }

    @Override
    public void onRemovedGrocery(Integer groceryId) {
        int index = keys.indexOf(groceryId);
        keys.remove(groceryId);
        notifyItemRemoved(index);

    }

}
