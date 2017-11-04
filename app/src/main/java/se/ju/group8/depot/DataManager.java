package se.ju.group8.depot;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author max
 * @date 10/23/17.
 */

class DataManager {
    SQLiteDatabase database = DatabaseHelper.getInstance(this);

    //entries in the inventory
    static EntryList inventoryEntries = new EntryList();

    //entries in the wanted list
    static EntryList wantedEntries = new EntryList();

    //entries in the shopping list
    static EntryList shoppingListEntries = new EntryList();


    //make default constructor private to forbid creating objects
    private DataManager(){}

    //this code gets executed no matter what
    static{
        //add some stuff to the lists because storage isn't implemented yet
        inventoryEntries.add("Spaghetti","123456789");
        inventoryEntries.add("Tomato Sauce","4238646842");
        inventoryEntries.add("Tomato Sauce2","4238646842", new Date());
        inventoryEntries.add("Onions");

        wantedEntries.add("Spaghetti","123456789");
        wantedEntries.add("Tomato Sauce","4238646842");

        shoppingListEntries.add("Potatoes");
    }

    //returns a string array instead of the objects in the given array


}