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

    //properties of the entry
    //TODO: think of more properties and their data types
    private String name, barcode;
    private long amount;
    private MyDate DateBought;
    private Date BestBefore;
    private long id;

    Entry(){}

    Entry(long id, long amount, String name, String barcode, MyDate myDate) {
        this.id = id;
        this.amount = amount;
        this.name = name;
        this.barcode = barcode;
        this.DateBought = myDate;
    }

    @Override
    //TODO: make toString more beautiful
    public String toString(){
        return id + ": " + this.name + "x" + this.amount;
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

    public MyDate getDateBought() {
        return DateBought;
    }

    public void setDateBought(MyDate dateBought) {
        DateBought = dateBought;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}