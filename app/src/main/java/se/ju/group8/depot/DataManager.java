package se.ju.group8.depot;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

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
//        Log.d("log", refToList1.toString());
//
//        refToList1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("call", "ondatachange call");
//                ArrayList<Entry> entrs = new ArrayList<>();
//
//                for (DataSnapshot o : dataSnapshot.getChildren()) {
//                    Entry newEntry = o.getValue(Entry.class);
//                    Log.d("log", newEntry.toString());
//                    entrs.add(newEntry);
//                }
//                inventoryList.update(entrs);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        refToList1.keepSynced(false);

        refToList1.addChildEventListener(new ChildEventListener() {
//            for (Entry internalEntry : Entries) {
//                if(internalEntry.getName().equals(externalEntry.getName())) {
//                    Log.d("entry", internalEntry.toString());
//                    internalEntry.setAmount(
//                            internalEntry.getAmount() + externalEntry.getAmount()
//                    );
//                    Log.d("entry", internalEntry.toString());
//                } else {
//                    Entries.add(externalEntry);
//                    Log.d("entry", externalEntry.toString());
//                }
//            }
//
//            if(ContextFragmentInventoryList.adapter != null) {
//                ContextFragmentInventoryList.update();
//            }

            //gets executed in the beginning
            void update(DataSnapshot dataSnapshot, String s){
                EntryList list = inventoryList;
                boolean updated = false;

                if(list.id <= (long)dataSnapshot.child("id").getValue()){
                    list.id = (long)dataSnapshot.child("id").getValue() + 1;
                    Log.d("log", "id updated to: " + list.id);
                }

                Entry internalEntry;

                Log.d("log", "update: " + list.Entries.toString());

                for ( int i = 0; i < list.Entries.size(); i++ ) {
                    internalEntry = list.Entries.get(i);

                    if(internalEntry.getName().equals(dataSnapshot.getKey())) {
                        internalEntry.setAmount(
                                internalEntry.getAmount() + (long)dataSnapshot.child("amount").getValue()
                        );
                        updated = true;
                    }
                }
                if(!updated){
                    Entry toAdd = dataSnapshot.getValue(Entry.class);
                    list.Entries.add(toAdd);
                }
            }

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                update(dataSnapshot, s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                update(dataSnapshot, s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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
//    static {
//        Log.d("call", "datamanager static");
//
//        getInstance().add(1, "null", 0);
//        getInstance().add(1, "Spaghetti", 500);
//        getInstance().add(1, "Tomato Sauce", 500, "124323324");
//        getInstance().add(1, "Tomato Sauce2", 200);
//        getInstance().add(1, "Onions", 3);
//
//        getInstance().add(2, "Spaghetti", 100);
//        getInstance().add(2, "Tomato Sauce", 1000);
//
//        getInstance().add(3, "Tomato Sauce", 500);
//    }

    static void dropInstance(){
        instance = null;
    }

    void add(int list, String name){
        add(list, name, 1);
    }

    void add(int list, String name, int value){
        add(list, name, value, "");
    }

    void add(int list, String name, int value, String barcode){
        add(list, name, value, barcode, new Date());
    }

    void add(int list, String name, int amount, String barcode, Date DateBought){
        //assume the list is 1
        EntryList listToAdd = inventoryList;

        if(list == EntryList.WANTED_LIST)
            listToAdd = wantedList;
        if(list == EntryList.SHOPPING_LIST)
            listToAdd = shoppingList;

        Entry entryToAdd = new Entry(listToAdd.id, amount, name, barcode, DateBought);

        // Write a message to the database
        myRef = userData.child(Integer.toString(list)).child(name);
        myRef.setValue(entryToAdd);

        //TODO: manage child nodes
    }

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

        Entry addEntry(Entry entryToAdd){
            Entries.add(entryToAdd);
            return entryToAdd;
        }

        ArrayList<Entry> toArrayList(){
            return Entries;
        }

//        void update(ArrayList<Entry> externalEntries){
//            Entry externalEntry;
//            for (int i = 0; i < externalEntries.size(); i++) {
//                Log.d("log", "" + i);
//                externalEntry = externalEntries.get(i);
//
//                //update id of the list to ensure that it is the highest
//                if(this.id <= externalEntry.getId()) {
//                    this.id = externalEntry.getId()+1;
//                }
////                Entries.add(new Entry(id++, 34, "test", "", new Date()));
//                Log.d("test", Entries.toString());
////                Log.d("test", "ich bin liste " + this.type);
//
//
//
//
//
//
//
//
//
//                //check for all internal entries, if external entry exists yet
//                Entry internalEntry;
//                for (int j = 0; j < Entries.size(); j++) {
////                    Log.d("log", "" + Entries.size());
//                    internalEntry = Entries.get(j);
//
//                    //if entry with "name" already exists, increase its amount
//                    if(internalEntry.getName().equals(externalEntry.getName())) {
//                        Log.d("entry", internalEntry.toString());
//                        internalEntry.setAmount(
//                                internalEntry.getAmount() + externalEntry.getAmount()
//                        );
//                        Log.d("entry", internalEntry.toString());
//                    } else {
//                        Entries.add(externalEntry);
//                        Log.d("entry", externalEntry.toString());
//                    }
//                }
//            }
//
//            if(ContextFragmentInventoryList.adapter != null) {
//                ContextFragmentInventoryList.update();
//            }
//        }
    }
}