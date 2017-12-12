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

    //TODO: think about more units
    static final int
            UNIT_NONE = 0,
            UNIT_GRAM = 1,
            UNIT_KILOGRAM = 2,
            UNIT_LITER = 3;

    //TODO: extract string
    static final String[] UNITS = {"", "gram", "kilo", "liter"};


    //properties of the entry
    //TODO: think of more properties and their data types
    private String name, barcode;
    private Long amount;
    private MyDate DateBought;
    private Date BestBefore;
    private long id;
    private int type;

    Entry(){}

    Entry(long id, long amount, String name, String barcode, MyDate myDate, int type) {
        this.id = id;
        this.amount = amount;
        this.name = name;
        this.barcode = barcode;
        this.DateBought = myDate;
        this.type = type;
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

    public MyDate getDateBought() {
        return DateBought;
    }

    public void setDateBought(MyDate dateBought) {
        DateBought = dateBought;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}