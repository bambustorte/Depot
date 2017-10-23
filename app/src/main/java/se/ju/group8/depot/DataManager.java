package se.ju.group8.depot;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author max
 * @date 10/23/17.
 */

public class DataManager {
    //entries in the inventory
    public static ArrayList<Entry> inventoryEntries = new ArrayList<Entry>();

    //entries in the wanted list
    public static ArrayList<Entry> wantedEntries = new ArrayList<Entry>();

    //entries in the shopping list
    public static ArrayList<Entry> shoppingListEntries = new ArrayList<Entry>();


    //default constructor
    public DataManager(){}

    //this code gets executed no matter what
    static{
        //add some stuff to the lists because storage isn't implemented yet
        inventoryEntries.add(new Entry("Spaghetti","123456789"));
        inventoryEntries.add(new Entry("Tomato Sauce","4238646842"));
        inventoryEntries.add(new Entry("Tomato Sauce","4238646842", new Date()));
        inventoryEntries.add(new Entry("Onions"));

        wantedEntries.add(new Entry("Spaghetti","123456789"));
        wantedEntries.add(new Entry("Tomato Sauce","4238646842"));

        shoppingListEntries.add(new Entry("Potatoes"));
    }

    //returns a string array instead of the objects in the given array
    static String[] entriesToStringArray(ArrayList<Entry> arrayToProcess){
        String [] StringArray = new String[arrayToProcess.size()];

        for(int i = 0; i < arrayToProcess.size(); i++){
            StringArray[i] = arrayToProcess.get(i).toString();
        }

        return StringArray;
    }

    private static class Entry {

        //properties of the entry
        //TODO: think of more properties and their data types
        String name, barcode;
        Date BestBefore, DateBought;

        Entry(String name){
            //just the name is required, if no barcode is given, leave it blank
            this(name, "");
        }

        Entry(String name, String barcode) {
            //if no date is given, use the current date
            this(name, barcode, new Date());
        }
        Entry(String name, String barcode, Date DateBought) {
            this.name = name;
            this.barcode = barcode;
            this.DateBought = DateBought;
        }

        @Override
        //TODO: make toString more beautiful
        public String toString(){
            return "name: " + this.name;
        }
    }

}