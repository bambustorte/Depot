package se.ju.group8.depot;

import java.util.Date;

/**
 * @author max
 * @date 10/24/17.
 */

class Entry {
    //properties of the entry
    //TODO: think of more properties and their data types
    String name, barcode;
    Date BestBefore, DateBought;
    int id;

    Entry(int id, String name){
        //just the name is required, if no barcode is given, leave it blank
        this(id, name, "");
    }

    Entry(int id, String name, String barcode) {
        //if no date is given, use the current date
        this(id, name, barcode, new Date());
    }
    Entry(int id, String name, String barcode, Date DateBought) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.DateBought = DateBought;
    }

    @Override
    //TODO: make toString more beautiful
    public String toString(){
        return "name: " + this.name + ", id: " + id;
    }
}