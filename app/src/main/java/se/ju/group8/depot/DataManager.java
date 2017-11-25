package se.ju.group8.depot;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * @author max
 * @date 10/23/17.
 */

class DataManager {

    int length1;
    int length2;
    int length3;

    private int id = 0;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private static DataManager instance = null;


    //make default constructor private to forbid creating objects
    DataManager(){database = FirebaseDatabase.getInstance();}

    static DataManager getInstance(){
        if(instance == null)
            instance = new DataManager();
        return instance;
    }

    //this code gets executed no matter what
    static {

        getInstance().add(1, "Spaghetti");
        getInstance().add(1, "Tomato Sauce");
        getInstance().add(1, "Tomato Sauce2");
        getInstance().add(1, "Onions");

        getInstance().add(2, "Spaghetti");
        getInstance().add(2, "Tomato Sauce");

        getInstance().add(3, "Potatoes");
    }

    Entry add(int list, String str){

        // Write a message to the database

        myRef = database.getReference("inventorys/lengths" + list);

        myRef.setValue(myRef.get);

        Log.d("fire", list + ": " + str);
//        Log.d("fireb", myRef.getKey());

//        myRef.child("alanisawesome").setValue(str);

        return new Entry(++id, str);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    //returns a string array instead of the objects in the given array


}