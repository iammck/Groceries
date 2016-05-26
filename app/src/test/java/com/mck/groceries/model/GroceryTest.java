package com.mck.groceries.model;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests to be assured a Grocery Item behaves correctly.
 * Should be able to go to/from json using GSon. all fields
 * are optional, but Id
 * Created by Michael on 5/21/2016.
 */
public class GroceryTest {
    String expectedName = "Ajax Soap";
    Integer expectedCount = 2;
    Integer expectedId = 42;
    String expectedDescription = "Deep cleaning soap.";

    String emptyGroceryJson = "{\"purchased\":false}";
    String justNameJson = String.format(Locale.getDefault(),"{\"name\":\"%s\",\"purchased\":false}", expectedName);
    String fullJson = String.format(Locale.getDefault(),
            "{\"name\":\"%s\",\"id\":%d,\"quantity\":%d," +
                    "\"description\":\"%s\",\"purchased\":false}",
            expectedName, expectedId, expectedCount, expectedDescription);

    @Test
    public void canInstantiateFromGSonString(){
        // test strings to and from json should start/end looking like these.
        Gson gson = new Gson();

        Grocery result = gson.fromJson(emptyGroceryJson, Grocery.class);
        assertNull("Expecting null grocery value.", result.quantity);
        assertNull("Expecting null grocery value.", result.id);
        assertNull("Expecting null grocery value.", result.name);
        assertNull("Expecting null grocery value.", result.description);

        result = gson.fromJson(justNameJson, Grocery.class);
        assertNull("Expecting null grocery value.", result.quantity);
        assertNull("Expecting null grocery value.", result.id);
        assertNotNull("Expecting valid name grocery value.", result.name);
        assertNull("Expecting null grocery value.", result.description);

        result = gson.fromJson(fullJson, Grocery.class);
        assertNotNull("Expecting valid grocery value.", result.quantity);
        assertNotNull("Expecting valid grocery value.", result.id);
        assertNotNull("Expecting valid name grocery value.", result.name);
        assertNotNull("Expecting valid grocery value.", result.description);
    }

    @Test
    public void canGetJsonFromItem(){
        Grocery empty = new Grocery();
        Gson gson = new Gson();

        String result = gson.toJson(empty);
        assertEquals("Expected Json does not equal result." , emptyGroceryJson, result);

        Grocery justName = new Grocery();
        justName.name = expectedName;
        result = gson.toJson(justName);
        assertEquals("Expected Json does not equal result.",justNameJson, result);

        Grocery full = new Grocery();
        full.name = expectedName;
        full.quantity = expectedCount;
        full.id = expectedId;
        full.description = expectedDescription;
        result = gson.toJson(full);
        assertEquals("Expected Json does not equal result.",fullJson, result);


    }

}