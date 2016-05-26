package com.mck.groceries.model;

import java.util.Hashtable;

/**
 * The Groceries is the container for grocery. It is capable of being
 * read into and from a json string.
 * Created by Michael on 5/21/2016.
 */
public class Groceries {
    public Hashtable<Integer, Grocery> groceries;
    public Groceries(){
        groceries = new Hashtable<>();
    }

}
