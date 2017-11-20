package se.ju.group8.depot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author max
 * @date 10/23/17.
 */

class DataManager {
//    //entries in the inventory
//    static EntryList inventoryEntries = new EntryList();
    static ArrayList<String> inventoryEntries;
//
//    //entries in the wanted list
//    static EntryList wantedEntries = new EntryList();
    static ArrayList<String> wantedEntries;
//
//    //entries in the shopping list
//    static EntryList shoppingListEntries = new EntryList();
    static ArrayList<String> shoppingListEntries;

    private int id = 0;

    private static DataManager instance = null;


    //make default constructor private to forbid creating objects
    DataManager(Context context){}

    static DataManager getInstance(Context context){
        if(instance == null)
            instance = new DataManager(context);
        return instance;
    }

    //this code gets executed no matter what
    static {
        inventoryEntries = new ArrayList<>();
        shoppingListEntries = new ArrayList<>();
        wantedEntries = new ArrayList<>();

        inventoryEntries.add("Spaghetti");
        inventoryEntries.add("Tomato Sauce");
        inventoryEntries.add("Tomato Sauce2");
        inventoryEntries.add("Onions");

        wantedEntries.add("Spaghetti");
        wantedEntries.add("Tomato Sauce");

        shoppingListEntries.add("Potatoes");

        //add some stuff to the lists because storage isn't implemented yet
//        inventoryEntries.add("Spaghetti","123456789");
//        inventoryEntries.add("Tomato Sauce","4238646842");
//        inventoryEntries.add("Tomato Sauce2","4238646842", new Date());
//        inventoryEntries.add("Onions");
//
//        wantedEntries.add("Spaghetti","123456789");
//        wantedEntries.add("Tomato Sauce","4238646842");
//
//        shoppingListEntries.add("Potatoes");
    }

    Entry add(String str){
        return new Entry(++id, str);
    }

    @Override
    public String toString() {
        return super.toString();

    }

    //returns a string array instead of the objects in the given array


}