package com.mck.groceries;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mck.groceries.model.Grocery;

/**
 * Displays the details of a grocery item.
 * Created by Michael on 5/24/2016.
 */
public class GroceryDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get the grocery item and its data
        Bundle args = getArguments();
        String groceryJson = args.getString(GroceriesActivity.GROCERY_KEY);
        final Grocery grocery = (new Gson()).fromJson(groceryJson, Grocery.class);
        // set up the dialog's view.
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getContext()).inflate(R.layout.grocery_detail, null);
        TextView tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
        TextView tvDesc = (TextView) view.findViewById(R.id.tvDescription);
        CheckBox cbPurchased = (CheckBox) view.findViewById(R.id.cbPurchased);
        String quantity = grocery.quantity.toString();
        tvQuantity.setText(quantity);
        if (grocery.description != null && !grocery.description.isEmpty()){
            tvDesc.setText(grocery.description);
        } else {
            tvDesc.setVisibility(View.GONE);
        }
        cbPurchased.setChecked(grocery.purchased);
        // create the dialog using a builder.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GroceriesHelper.instance().remove(getActivity(), grocery.id);
            }
        });
        builder.setNegativeButton(R.string.edit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String groceryJson = (new Gson()).toJson(grocery);
                Intent intent = new Intent(getActivity(), EditGroceryActivity.class);
                intent.putExtra(GroceriesActivity.GROCERY_KEY, groceryJson);
                getActivity().startActivityForResult(intent, GroceriesActivity.EDIT_GROCERY_RESULT_CODE);
            }
        });
        builder.setTitle(grocery.name);
        return builder.create();
    }

}
