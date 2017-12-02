package se.ju.group8.depot;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.Date;

/**
 * @author max
 * @date 10/24/17.
 */

class Entry {
    public static final int
            INVENTORY_LIST = 1,
            WANTED_LIST = 2,
            SHOPPING_LIST = 3;

    //properties of the entry
    //TODO: think of more properties and their data types
    private String name, barcode;
    private int type;
    private Date BestBefore, DateBought;
    private long id;

    Entry(){};

//    Entry(int id, int type, String name, String barcode, Date DateBought) {
//        this.id = id;
//        this.type = type;
//        this.name = name;
//        this.barcode = barcode;
//        this.DateBought = DateBought;
//    }
//
//    Entry(int id, int type, String name, String barcode, Date DateBought) {
//        this.id = id;
//        this.type = type;
//        this.name = name;
//        this.barcode = barcode;
//        this.DateBought = DateBought;
//    }
//
//    Entry(int id, int type, String name, String barcode, Date DateBought) {
//        this.id = id;
//        this.type = type;
//        this.name = name;
//        this.barcode = barcode;
//        this.DateBought = DateBought;
//    }

    Entry(int id, int type, String name, String barcode, Date DateBought) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.barcode = barcode;
        this.DateBought = DateBought;
    }

    @Override
    //TODO: make toString more beautiful
    public String toString(){
        return "name: " + this.name + ", id: " + id;
    }



    //getter&setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Date getBestBefore() {
        return BestBefore;
    }

    public void setBestBefore(Date bestBefore) {
        BestBefore = bestBefore;
    }

    public Date getDateBought() {
        return DateBought;
    }

    public void setDateBought(Date dateBought) {
        DateBought = dateBought;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}