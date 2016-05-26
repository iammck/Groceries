package com.mck.groceries.model;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * should be able to get the list of grocery. Either new, or from file.
 * should be able to set the list of grocery, overwriting if file exists.
 *
 * Since this is testing for file, need access to the android apis.
 * Created by Michael on 5/21/2016.
 */
public class GroceriesProviderTest extends ApplicationTestCase<Application> {
    public GroceriesProviderTest() {
        super(Application.class);
    }


    public void testCanSetGroceryList(){
        Groceries groceries = new Groceries();
        Grocery expectedGrocery = new Grocery();
        String expectedName = "item";
        Integer expectedId = 42;
        expectedGrocery.name = expectedName;
        expectedGrocery.id = expectedId;
        groceries.groceries.put(expectedId, expectedGrocery);
        GroceriesProvider.getInstance().storeGroceryList(getContext(), groceries);

        Groceries result = GroceriesProvider.getInstance().restoreGroceryList(getContext());
        assertNotNull("Result should not be null.", result);
        assertTrue("Result should have size of 1.", result.groceries.size() == 1);
        assertTrue("Result item should have name equal to expected value of " +
                expectedName +
                ", but was " + result.groceries.get(expectedId).name,
                result.groceries.get(expectedId).name.equals(expectedName));
    }

    public void testCanGetGroceryList(){
        // if there is no file then should return a new grocery list
        Groceries result = GroceriesProvider.getInstance().restoreGroceryList(getContext());
        assertNotNull("Should be able to return a new grocery list.", result);
    }


    public void testRestoreCorruptFileMeansNewGroceryList(){
        try {
            String badFile = "{";
            FileOutputStream fileOutputStream =
                    getContext().openFileOutput(GroceriesProvider.FILE_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(badFile.getBytes());
            fileOutputStream.close();
            Groceries result = GroceriesProvider.getInstance().restoreGroceryList(getContext());
            // this should have size of none.
            assertNotNull("should have a Groceries instance", result);
            assertTrue("should have an empty groceryList", result.groceries.size() == 0);
        } catch (IOException e) {
            e.printStackTrace();
            fail("unable to retrieve a grocery list");
        }

    }
}