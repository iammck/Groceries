package com.mck.groceries;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

/**
 * GroceriesActivity should start up the GroceryListDataProvider.
 * Created by Michael on 5/22/2016.
 */
@RunWith(AndroidJUnit4.class)
public class GroceriesActivityTest {
    @Rule
    public ActivityTestRule<GroceriesActivity> mActivityRule = new ActivityTestRule<>(
            GroceriesActivity.class);

    @Test
    public void hasSetupGroceryListDataHandler(){
        assertNotNull("There should be A grocery handler instance.",
                GroceriesHelper.instance());
    }
}