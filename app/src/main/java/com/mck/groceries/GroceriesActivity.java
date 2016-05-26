package com.mck.groceries;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class GroceriesActivity extends AppCompatActivity {

    public static final int EDIT_GROCERY_RESULT_CODE = 37;
    public static final String GROCERY_KEY = "GROCERY_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setImageResource(R.drawable.ic_note_add_white_36dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroceriesActivity.this, EditGroceryActivity.class);
                startActivityForResult(intent, EDIT_GROCERY_RESULT_CODE);
            }
        });

        if (savedInstanceState == null){
            GroceriesFragment groceriesFragment = new GroceriesFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.grocery_list_fragment_container, groceriesFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_groceries, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rm_purchased) {
            GroceriesHelper.instance().removePurchased(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * There may be results from an edit, start an asyncTask and update the
     * dataHandler
     * @param requestCode looking for edit grocery result code
     * @param resultCode and an ok result.
     * @param data the resulting grocery is a json string extra.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EDIT_GROCERY_RESULT_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user entered a new grocery.
                String groceryData = data.getStringExtra(GROCERY_KEY);
                if (groceryData != null) {
                    Log.v("GroceriesActivity", "groceryData is " + groceryData);
                    GroceriesHelper.instance().addGrocery(GroceriesActivity.this, groceryData);
                }
            }
        }


    }
}
