package se.ju.group8.depot;

import android.content.Intent;
import android.support.annotation.UiThread;
import android.util.Log;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author max
 * @date 10/23/17.
 */

class DataManager {

    EntryList inventoryList;
    EntryList wantedList;
    EntryList shoppingList;

    private FirebaseUser user;

    private FirebaseDatabase database;
    DatabaseReference barcodes;
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

        //root of all the barcodes
        barcodes = database.getReference("barcodes/");

        //initiate all lists
        inventoryList = new EntryList(EntryList.INVENTORY_LIST);
        wantedList = new EntryList(EntryList.WANTED_LIST);
        shoppingList = new EntryList(EntryList.SHOPPING_LIST);

        refToList1 = userData.child("1");
        refToList1.keepSynced(true);
        refToList1.addChildEventListener(new ChildEventListenerBuilder(inventoryList).getListener());

        refToList2 = userData.child("2");
        refToList2.keepSynced(true);
        refToList2.addChildEventListener(new ChildEventListenerBuilder(wantedList).getListener());

        refToList3 = userData.child("3");
        refToList3.keepSynced(true);
        refToList3.addChildEventListener(new ChildEventListenerBuilder(shoppingList).getListener());


    }

    class ChildEventListenerBuilder{
        ChildEventListener childEventListener;

        ChildEventListenerBuilder(final EntryList list) {
            this.childEventListener = new ChildEventListener() {

                //gets executed in the beginning
                void update(DataSnapshot dataSnapshot, String s) {

                    boolean updated = false;

                    if (list.id <= (long) dataSnapshot.child("id").getValue()) {
                        list.id = (long) dataSnapshot.child("id").getValue() + 1;
                        Log.d("log", "id updated to: " + list.id);
                    }

                    Entry internalEntry;

//                    Log.d("log", "update internal list: " + list.Entries.toString());
//                    Log.d("log", "update external list: " + dataSnapshot.toString());

                    for (int i = 0; i < list.Entries.size(); i++) {
                        internalEntry = list.Entries.get(i);

                        if (internalEntry.getName().equals(dataSnapshot.getKey())) {

                            internalEntry.setAmount(
                                    (Long) dataSnapshot.child("amount").getValue()
                            );

                            internalEntry.setBarcode(
                                    (String) dataSnapshot.child("barcode").getValue()
                            );

                            internalEntry.setDateBought(
                                    dataSnapshot.child("dateBought").getValue(MyDate.class)
                            );

                            internalEntry.setType(
                                    (int) (long) dataSnapshot.child("type").getValue()
                            );

                            updated = true;
                        }
                    }
                    if (!updated) {
                        Entry toAdd = dataSnapshot.getValue(Entry.class);
                        list.Entries.add(toAdd);
                    }
                }

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    update(dataSnapshot, s);
                    updateAdapters();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    update(dataSnapshot, s);
                    updateAdapters();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    for (Entry e :
                            list.Entries) {
                        if (dataSnapshot.getKey().equals(e.getName())) {
                            list.Entries.remove(e);
                            updateAdapters();
                            return;
                        }
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

                private void updateAdapters() {
                    if(list.type == EntryList.INVENTORY_LIST) {
                        if (ContextFragmentInventoryList.adapter != null) {
                            ContextFragmentInventoryList.update(list.Entries);
                        }
                    }
                    if(list.type == EntryList.WANTED_LIST) {
                        if (ContextFragmentWantedItemsList.adapter != null) {
                            ContextFragmentWantedItemsList.update(list.Entries);
                        }
                    }
                    if(list.type == EntryList.SHOPPING_LIST) {
                        if (ContextFragmentShoppingList.adapter != null) {
                            ContextFragmentShoppingList.update(list.Entries);
                        }
                    }

                }

            };
        }

        ChildEventListener getListener(){return this.childEventListener;}
    }

    static DataManager getInstance(){
        if(instance == null)
            instance = new DataManager();
        return instance;
    }

    //this code gets executed no matter what
//    static {}

    static void dropInstance(){
        instance = null;
    }

    EntryList numberToList(int list){
        EntryList entryList = inventoryList;

        if (list == EntryList.WANTED_LIST)
            entryList = wantedList;

        if (list == EntryList.SHOPPING_LIST)
            entryList = shoppingList;

        return entryList;
    }

    void add(int list, String name, Long amount, String barcode, int MM, int DD, int YYYY) {
        add(list, name, amount, barcode, MM, DD, YYYY, 0);
    }

    void add(int list, String name, Long amount, String barcode, int MM, int DD, int YYYY, int type) {
        EntryList listToAdd = inventoryList;

        if(list == EntryList.WANTED_LIST)
            listToAdd = wantedList;
        if(list == EntryList.SHOPPING_LIST)
            listToAdd = shoppingList;


        final Entry entryToAdd = new Entry(listToAdd.id, amount, name, barcode, new MyDate(MM, DD, YYYY), type);

        add(list, entryToAdd);
    }
    void add(int list, final Entry entryToAdd){

        // Write a message to the database
        myRef = userData.child(Integer.toString(list)).child(entryToAdd.getName());//FIXME: name gets changed internally first

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if ( dataSnapshot.exists() ) {

                    myRef.child("amount").setValue(
                            entryToAdd.getAmount() + (Long) dataSnapshot.child("amount").getValue()
                    );

                    myRef.child("barcode").setValue(entryToAdd.getBarcode());
//                    myRef.child("name").setValue(entryToAdd.getName());
                    myRef.child("type").setValue(entryToAdd.getType());
                    myRef.child("dateBought").setValue(entryToAdd.getDateBought());

                } else {
                    myRef.setValue(entryToAdd);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("log", "datamanager add concancelled");
            }
        });

        //TODOne: change amount instead of overriding
    }

    void updateEntry(int list, Entry entry, Long amount, MyDate myDate, String barcode, String name, int type){

        Entry entry1 = new Entry();

        entry1.setName(entry.getName());
        entry1.setDateBought(entry.getDateBought());
        entry1.setId(0);

        if(amount != null) entry1.setAmount(-amount);
        if(myDate != null) entry1.setDateBought(myDate);
        if(barcode != null) entry1.setBarcode(barcode);
//        if(name != null) entry1.setName(name);
        if(type != -1) entry1.setType(type);

        add(list, entry1);
    }

    void removeEntry(int list, String nameOfEntry){
        // remove entry from the database
        myRef = userData.child(Integer.toString(list)).child(nameOfEntry);
        myRef.setValue(null);

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

        ArrayList<Entry> toArrayList(){
            return Entries;
        }

        Entry getEntryById(long id){
            for (Entry e :
                    Entries) {
                if (e.getId() == id)
                    return e;
            }
            return null;
        }

        Entry getEntryByName(String name){
            for (Entry e :
                    Entries) {
                if (e.getName().equals(name))
                    return e;
            }
            return null;
        }

    }

    String addBarcode(String barcodeToAdd, String name){
        barcodes.child(barcodeToAdd).setValue(name);
        return barcodeToAdd;
    }

}