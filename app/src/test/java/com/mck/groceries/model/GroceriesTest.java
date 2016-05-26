package com.mck.groceries.model;

import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * A grocery list can be instantiated from json and deserialize
 * to a Groceries.
 *
 * Created by Michael on 5/21/2016.
 */
public class GroceriesTest {
    @Test
    public void canGetJsonFromInstance(){
        Groceries instance = new Groceries();
        Gson gson = new Gson();
        String resultJson = gson.toJson(instance);
        String expectingJson = "{\"groceries\":{}}";
        assertTrue("Expected "+ expectingJson +" json value, but was " + resultJson + ".",
                expectingJson.equals(resultJson));
    }

    @Test
    public void canGetFromJson(){
        Gson gson = new Gson();
        String json = "{\"grocery\":{}}";
        Groceries result = gson.fromJson(json, Groceries.class);
        assertNotNull("Should return Groceries instance.", result);
        assertNotNull("Groceries should have grocery.", result.groceries);
    }
}