package com.mck.groceries;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mck.groceries.model.Grocery;

/**
 * A placeholder fragment containing a simple view.
 */
public class GroceriesFragment extends Fragment implements GroceriesAdapter.OnViewHolderClickListener{

    public GroceriesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.groceries_fragment, container, false);

        // Get the RecyclerView from the inflated view.
        RecyclerView recyclerView =
                (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // Use a linear layout manager.
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // specify an adapter
        final RecyclerView.Adapter adapter = new GroceriesAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);

        // handle swiping
        new ItemTouchHelper(

                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeGrocery(((GroceriesAdapter.ViewHolder) viewHolder).grocery.id);
            }

        }).attachToRecyclerView(recyclerView);
        return view;
    }

    private void removeGrocery(Integer groceryId){
        GroceriesHelper.instance().remove(getActivity(), groceryId);
    }

    @Override
    public void onResume() {
        super.onResume();
        View rv = getView();
        assert rv != null;
        RecyclerView rView = (RecyclerView) rv.findViewById(R.id.my_recycler_view);
        if (((GroceriesAdapter) rView.getAdapter()).isEmpty()){
            showEditGroceries();
        }
    }

    private void showEditGroceries() {
       /* Toast.makeText(getActivity(),"There are no grocery in your list! Add some grocery to get started.",
                Toast.LENGTH_LONG).show();*/
    }


    @Override
    public void onViewClicked(Grocery grocery) {
        Bundle args = new Bundle();
        String groceryJson = (new Gson()).toJson(grocery);
        args.putString(GroceriesActivity.GROCERY_KEY, groceryJson);

        GroceryDialog groceryDialog = new GroceryDialog();
        groceryDialog.setArguments(args);
        groceryDialog.show(getActivity().getSupportFragmentManager(),null);
    }

    @Override
    public void onViewLongClicked(Grocery grocery) {
        String groceryJson = (new Gson()).toJson(grocery);
        Intent intent = new Intent(getActivity(), EditGroceryActivity.class);
        intent.putExtra(GroceriesActivity.GROCERY_KEY, groceryJson);
        getActivity().startActivityForResult(intent, GroceriesActivity.EDIT_GROCERY_RESULT_CODE);
    }

    @Override
    public void onCheckChanged(Grocery grocery, boolean isChecked) {
        grocery.purchased = isChecked;
        GroceriesHelper.instance().addGrocery(getActivity(),grocery);
    }
}
