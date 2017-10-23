package se.ju.group8.depot;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author max
 * @date 10/23/17.
 */

public class Data {
    public static ArrayList<Entry> entries = new ArrayList<Entry>();

    public Data(){

    }

    public static String[] entriesToStringArray(){
        String [] StringArray = new String[entries.size()];

        for(int i = 0; i < entries.size(); i++){
            StringArray[i] = entries.get(i).toString();
        }

        return StringArray;
    }

    static{
        entries.add(new Entry("Spaghetti","123456789"));
        entries.add(new Entry("Tomato Sauce","4238646842"));
        entries.add(new Entry("Tomato Sauce","4238646842", new Date()));
        entries.add(new Entry("Onions"));
    }


    static class Entry {

        String name, barcode;
        Date BestBefore, DateBought;

        Entry(String name){
            this(name, "");
        }

        Entry(String name, String barcode) {
            this(name, barcode, new Date());
        }
        Entry(String name, String barcode, Date DateBought) {
            this.name = name;
            this.barcode = barcode;
            this.DateBought = DateBought;
        }

        @Override
        public String toString(){
            return "name: " + this.name;
        }
    }

}