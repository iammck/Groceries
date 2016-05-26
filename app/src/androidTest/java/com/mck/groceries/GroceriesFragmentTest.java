package com.mck.groceries;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.mck.groceries.model.Groceries;
import com.mck.groceries.model.GroceriesProvider;
import com.mck.groceries.model.Grocery;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileOutputStream;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Michael on 5/23/2016.
 */
@RunWith(AndroidJUnit4.class)
public class GroceriesFragmentTest {
    @Rule
    public ActivityTestRule<GroceriesActivity> mActivityRule = new ActivityTestRule<>(
            GroceriesActivity.class);


    @Before
    public void beforeHand(){
        mActivityRule.getActivity();
    }

    @Test
    public void showsGroceriesWithNoGroceryData(){
        mActivityRule.getActivity();
        // save an empty set of grocery
        saveToFile(new Groceries());
        // does the screen behave correctly?
        onView(withId(R.id.my_recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void showsGroceriesWithGroceryData(){
        // save an expected set of grocery
        saveToFile(getExpectedGroceriesData());
        // does the screen behave correctly?
    }


    private void saveToFile(Groceries expectedGroceriesData) {
        Gson gson = new Gson();
        String output = gson.toJson(expectedGroceriesData);
        FileOutputStream outputStream;

        try {
            outputStream = mActivityRule.getActivity()
                    .getApplicationContext()
                    .openFileOutput(GroceriesProvider.FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(output.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Groceries getExpectedGroceriesData() {
        Groceries result = new Groceries();
        insertGrocery(result, 2, "Ajax", 2, "desc1");// result, id, name, quantity, desc.
        insertGrocery(result, 2, "Olive Oil", 2, "desc1");
        insertGrocery(result, 2, "Lettuce", 1, "desc1");
        insertGrocery(result, 2, "Orange", 5, "desc1");
        insertGrocery(result, 2, "Banana", 0, "desc1");



        return result;
    }

    private void insertGrocery(Groceries result, int id, String name, int quantity, String desc) {
        Grocery grocery = new Grocery();
        grocery.id = id;
        grocery.name = name;
        grocery.quantity = quantity;
        grocery.description = desc;
        result.groceries.put(id, grocery);
    }
}