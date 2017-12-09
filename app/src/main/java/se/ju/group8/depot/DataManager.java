package se.ju.group8.depot;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * @author max
 * @date 10/23/17.
 */

class DataManager {

    //referred to as [list]1
//    static ArrayList<String> inventoryEntries = new ArrayList<>();
    EntryList inventoryList;
    //referred to as [list]2
//    static ArrayList<String> wantedEntries = new ArrayList<>();
    EntryList wantedList;
    //referred to as [list]3
//    static ArrayList<String> shoppingListEntries = new ArrayList<>();
    EntryList shoppingList;

    private FirebaseUser user;

    private FirebaseDatabase database;
    private DatabaseReference userData;
    private DatabaseReference refToList1;
    private DatabaseReference refToList2;
    private DatabaseReference refToList3;
    private DatabaseReference myRef;

    private static DataManager instance = null;


    //make default constructor private to forbid creating objects
    private DataManager(){
        //get database
        database = FirebaseDatabase.getInstance();
        //and user
        user = FirebaseAuth.getInstance().getCurrentUser();

        //user root of database
        userData = database.getReference("user/" + user.getUid());

        //initiate all lists
        inventoryList = new EntryList(EntryList.INVENTORY_LIST);
        wantedList = new EntryList(EntryList.WANTED_LIST);
        shoppingList = new EntryList(EntryList.SHOPPING_LIST);

        refToList1 = userData.child("1");
        Log.d("log", refToList1.toString());
        refToList1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("call", "ondatachange call");
                ArrayList<Entry> entrs = new ArrayList<>();

                for (DataSnapshot o : dataSnapshot.getChildren()) {
                    Entry newEntry = o.getValue(Entry.class);
                    Log.d("debug", newEntry.toString());
                    entrs.add(newEntry);
                }
//                inventoryList.update(entrs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    static DataManager getInstance(){
        if(instance == null)
            instance = new DataManager();
        return instance;
    }

    //this code gets executed no matter what
    static {
        Log.d("call", "datamanager static");

        getInstance().add(1, "null", 0);
        getInstance().add(1, "Spaghetti", 500);
        getInstance().add(1, "Tomato Sauce", 500, "124323324");
        getInstance().add(1, "Tomato Sauce2", 200);
        getInstance().add(1, "Onions", 3);

        getInstance().add(2, "Spaghetti", 100);
        getInstance().add(2, "Tomato Sauce", 1000);

        getInstance().add(3, "Tomato Sauce", 500);
    }

    static void dropInstance(){
        instance = null;
    }

    static void print(){
//        Log.d("test", getInstance().testEntry.toString());
    }

    void add(int list, String name, int value){
        add(list, name, value, "");
    }

    void add(int list, String name, int value, String barcode){
        add(list, name, value, barcode, new Date());
    }

    void add(int list, String name, int value, String barcode, Date DateBought){
        //assume the list is 1
        EntryList listToAdd = inventoryList;

        if(list == EntryList.WANTED_LIST)
            listToAdd = wantedList;
        if(list == EntryList.SHOPPING_LIST)
            listToAdd = shoppingList;

        Entry entryToAdd = listToAdd.addEntry(name, value, barcode, DateBought);
        // Write a message to the database

        myRef = userData.child(Integer.toString(list)).child(name);
        myRef.setValue(entryToAdd);
        //TODO: manage child nodes
    }

    @Override
    public String toString() {
        return super.toString();
    }

    //returns a string array instead of the objects in the given array



    class EntryList{
        static final int
                INVENTORY_LIST = 1,
                WANTED_LIST = 2,
                SHOPPING_LIST = 3;

        int type;
        long id = 1;
        ArrayList<Entry> Entries;

        EntryList(int type){
            Entries = new ArrayList<>();
            this.type = type;
        }

        Entry addEntry(String name, int value, String barcode, Date dateBought){
            Entry entry = new Entry(id++, value, name, barcode, dateBought);
            Entries.add(entry);
            return entry;
        }

        ArrayList<Entry> toArrayList(){
            return Entries;
        }

        void update(ArrayList<Entry> externalEntries){
            for (Entry externalEntry : externalEntries) {
                //update id of the list to ensure that it is the highest
                if(this.id <= externalEntry.getId()) {
                    this.id = externalEntry.getId()+1;
                }

                Log.d("test", DataManager.getInstance().wantedList.toArrayList().toString());

                //check for all internal entries, if external entry exists yet
                for (Entry internalEntry : Entries) {
                    //if entry with "name" already exists, increase its amount
                    if(internalEntry.getName().equals(externalEntry.getName())) {
                        Log.d("entry", internalEntry.toString());
                        internalEntry.setAmount(
                                internalEntry.getAmount() + externalEntry.getAmount()
                        );
                        Log.d("entry", internalEntry.toString());
                    } else {
                        Entries.add(externalEntry);
                        Log.d("entry", externalEntry.toString());
                    }
                }
            }
//            if(ContextFragmentInventoryList.adapter != null) {
//                ContextFragmentInventoryList.update();
//            }
        }
    }
}