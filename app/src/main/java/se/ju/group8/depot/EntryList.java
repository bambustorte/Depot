package se.ju.group8.depot;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author max
 * @date 10/24/17.
 */

class EntryList {
    private ArrayList<Entry> entries;
    int id;


    EntryList(){
        entries = new ArrayList<Entry>();
        id = 0;
    }

    @Override
    public String toString() {
        String result = "";

        for (Entry entry : entries) {
            result += entry.toString();
        }

        return result;
    }

    ArrayList<String> entriesToStringList(){
        ArrayList<String> StringArray = new ArrayList<String>();

        for(Entry entry: entries){
            StringArray.add(entry.toString());
        }

        return StringArray;
    }

    private void add(Entry entry){
        this.entries.add(entry);
    }

    Entry add(String name){
        return add(name, "");
    }

    Entry add(String name, String barcode){
        return add(name, barcode, new Date());
    }

    Entry add(String name, String barcode, Date date){
        Entry entry = new Entry(id++, name, barcode, date);
        this.entries.add(entry);
        return entry;
    }

    Entry get(int position){
        return entries.get(position);
    }
}
