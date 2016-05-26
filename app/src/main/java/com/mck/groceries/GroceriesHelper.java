package com.mck.groceries;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.mck.groceries.model.Groceries;
import com.mck.groceries.model.GroceriesProvider;
import com.mck.groceries.model.Grocery;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;

/**
 * A Singleton that helps the app connect data to the adapter.
 *
 * Created by Michael on 5/23/2016.
 */
public class GroceriesHelper {
    public static GroceriesHelper instance;
    private GroceriesUpdateListener listener;

    Groceries groceries;
    Integer maxId;

    public interface GroceriesUpdateListener {
        void onGroceriesUpdate(Set<Integer> keys);
        void onAddGrocery(Integer id);
        void onRemovedGrocery(Integer id);
    }
    private static final String LOCK = "g_instance_lock";
    public static GroceriesHelper instance(){
        if (instance == null){
            synchronized (LOCK){
                if (instance == null){
                    instance = new GroceriesHelper();
                }
            }
        }
        return instance;
    }

    //public static GroceriesHelper instance(){return instance;}
    public void setUpdateListener(Activity activity, GroceriesUpdateListener listener){
        this.listener = listener;
        if (groceries == null){
            groceries = GroceriesProvider.getInstance().restoreGroceryList(activity);
        }
        listener.onGroceriesUpdate(Collections
                .unmodifiableSet(groceries.groceries.keySet()));
    }

    public void remove(final Activity activity, final int groceryId) {
        new AsyncTask<Integer, Integer, Integer>(){
            @Override
            protected Integer doInBackground(Integer... params) {
                if (groceries == null){
                    groceries = GroceriesProvider.getInstance().restoreGroceryList(activity);
                }
                Grocery grocery = groceries.groceries.remove(groceryId);
                GroceriesProvider.getInstance().storeGroceryList(activity, groceries);
                if (grocery == null) return null;
                return grocery.id;
            }

            @Override
            protected void onPostExecute(Integer id) {
                super.onPostExecute(id);
                if (listener != null && id != null) {
                    listener.onRemovedGrocery(id);
                }
            }
        }.execute(groceryId);
    }

    public void addGrocery(final Activity activity, String groceryData){
        new AsyncTask<String, Integer, Integer>(){
            @Override
            protected Integer doInBackground(String... params) {
                Grocery grocery = (new Gson()).fromJson(params[0], Grocery.class);
                if (groceries == null){
                    groceries = GroceriesProvider.getInstance().restoreGroceryList(activity);
                }
                if (grocery.id == null){
                    getId(grocery);
                }
                groceries.groceries.put(grocery.id, grocery);
                GroceriesProvider.getInstance().storeGroceryList(activity, groceries);
                return grocery.id;
            }

            @Override
            protected void onPostExecute(Integer id) {
                super.onPostExecute(id);
                if (listener != null) {
                    Log.v("GroceriesHelper", "grocery quantity is " + groceries.groceries.get(id).quantity);
                    listener.onAddGrocery(id);
                }
            }
        }.execute(groceryData);
    }


    public void addGrocery(final Activity activity, Grocery grocery){
        new AsyncTask<Grocery, Integer, Integer>(){
            @Override
            protected Integer doInBackground(Grocery... params) {
                Grocery grocery = params[0];
                if (groceries == null){
                    groceries = GroceriesProvider.getInstance().restoreGroceryList(activity);
                }
                if (grocery.id == null){
                    getId(grocery);
                }
                groceries.groceries.put(grocery.id, grocery);
                GroceriesProvider.getInstance().storeGroceryList(activity, groceries);
                return grocery.id;
            }

            @Override
            protected void onPostExecute(Integer id) {
                super.onPostExecute(id);
                if (listener != null) {
                    listener.onAddGrocery(id);
                }
            }
        }.execute(grocery);
    }

    private void getId(Grocery grocery) {
        if (maxId == null){
            maxId = 0;
            for (Integer key : groceries.groceries.keySet()) {
                if (key > maxId) maxId = key;
            }
        }
        grocery.id = maxId += 1;
    }

    public Grocery getGrocery(Integer key) {
        return groceries.groceries.get(key);
    }

    public void removePurchased(final Activity activity) {
        new AsyncTask<Integer, Integer, Integer>(){
            @Override
            protected Integer doInBackground(Integer... params) {
                if (groceries == null){
                    groceries = GroceriesProvider.getInstance().restoreGroceryList(activity);
                }
                Hashtable<Integer, Grocery> toRemain = new Hashtable<>();
                for(Integer key: groceries.groceries.keySet()){
                    if (!groceries.groceries.get(key).purchased){
                        toRemain.put(key, groceries.groceries.get(key));
                    }
                }

                groceries.groceries = toRemain;
                GroceriesProvider.getInstance().storeGroceryList(activity, groceries);
                return null;
            }

            @Override
            protected void onPostExecute(Integer id) {
                super.onPostExecute(id);
                if (listener != null) {
                    listener.onGroceriesUpdate(Collections
                            .unmodifiableSet(groceries.groceries.keySet()));                }
            }
        }.execute();
    }
}
