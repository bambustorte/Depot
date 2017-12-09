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
    private int amount;
    private Date BestBefore, DateBought;
    private long id;

    Entry(){};

    Entry(long id, int amount, String name, String barcode, Date DateBought) {
        this.id = id;
        this.amount = amount;
        this.name = name;
        this.barcode = barcode;
        this.DateBought = DateBought;
    }

    @Override
    //TODO: make toString more beautiful
    public String toString(){
        return "name: " + this.name + "/id: " + id + "/amount: " + amount;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int type) {
        this.amount = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}