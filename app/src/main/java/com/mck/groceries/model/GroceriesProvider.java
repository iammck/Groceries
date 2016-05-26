package com.mck.groceries.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Singleton object to get and set the list of grocery from file.
 * If there is no file, will return a new Groceries.
 * If there is a file, will overwrite it with incoming list.
 * Created by Michael on 5/21/2016.
 */
public class GroceriesProvider {

    private static GroceriesProvider instance;
    private static final String LOCK = "instance_lock";

    public static GroceriesProvider getInstance(){
        if (instance == null){
            synchronized (LOCK){
                if (instance == null){
                    instance = new GroceriesProvider();
                }
            }
        }
        return instance;
    }

    // the name of the file to open.
    public static final String FILE_NAME = "Grocery_List";

    public Groceries restoreGroceryList(Context context){
        synchronized (this) {
            if (context == null) return null;
            try {
                FileInputStream fileInputStream = context.openFileInput(FILE_NAME);
                BufferedInputStream bufferedInputStream =
                        new BufferedInputStream(fileInputStream);
                int next;
                StringBuilder result = new StringBuilder();
                while ((next = bufferedInputStream.read()) != -1) {
                    result.append((char) next);
                }
                bufferedInputStream.close();
                Gson gson = new Gson();
                return gson.fromJson(result.toString(), Groceries.class);
            } catch (Exception e){
                return new Groceries();
            }
        }
    }

    public void storeGroceryList(Context context, Groceries groceries) {
        synchronized (this) {
            if (context == null) return;
            try {
                Gson gson = new Gson();
                String result = gson.toJson(groceries);
                FileOutputStream fileOutputStream =
                        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                fileOutputStream.write(result.getBytes());
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
