package com.mck.groceries;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mck.groceries.model.Grocery;

public class EditGroceryActivity extends AppCompatActivity {
    private Grocery mGrocery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grocery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setImageResource(R.drawable.ic_done_black_36dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!wasEdited()) {
                    EditGroceryActivity.this.finish();
                    return;
                }
                Intent data = new Intent();
                data.putExtra(GroceriesActivity.GROCERY_KEY, getGroceryAsJson());
                if (getParent() == null) {
                    setResult(Activity.RESULT_OK, data);
                } else {
                    getParent().setResult(Activity.RESULT_OK, data);
                }
                EditGroceryActivity.this.finish();
            }
        });
        Intent intent = getIntent();
        String sGrocery = intent.getStringExtra(GroceriesActivity.GROCERY_KEY);
        if (sGrocery != null){
            mGrocery = (new Gson()).fromJson(sGrocery, Grocery.class);
            
            TextView tvName = (TextView) findViewById(R.id.etName);
            assert tvName != null;
            tvName.setText(mGrocery.name);

            TextView tvDesc = (TextView) findViewById(R.id.etDescription);
            assert tvDesc != null;
            tvDesc.setText(mGrocery.description);

            TextView tvQuantity = (TextView) findViewById(R.id.etQuantity);
            assert tvQuantity != null;
            String quantity = mGrocery.quantity.toString();
            tvQuantity.setText(quantity);
        }
    }

    private boolean wasEdited() {
        EditText etName = (EditText) findViewById(R.id.etName);
        assert etName != null;
        String name = etName.getText().toString();
        String defaultName = getResources().getString(R.string.no_text);
        return !name.equals(defaultName);
    }

    public  String getGroceryAsJson() {
        EditText etName = (EditText) findViewById(R.id.etName);
        EditText etQuantity = (EditText) findViewById(R.id.etQuantity);
        EditText etDescription = (EditText) findViewById(R.id.etDescription);
        assert etName != null;
        assert etQuantity != null;
        assert etDescription != null;
        String name = etName.getText().toString();
        String quantity = etQuantity.getText().toString();
        String desc = etDescription.getText().toString();

        Grocery grocery = new Grocery();
        grocery.name = name;
        if (quantity.isEmpty())
            quantity = "1";
        grocery.quantity = Integer.valueOf(quantity);
        grocery.description = desc;
        if (this.mGrocery != null) {
            grocery.id = this.mGrocery.id;
            grocery.purchased = this.mGrocery.purchased;
        }
        return (new Gson()).toJson(grocery);
    }
}
