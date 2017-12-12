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

                    Log.d("log", "update internal list: " + list.Entries.toString());
                    Log.d("log", "update external list: " + dataSnapshot.toString());

                    for (int i = 0; i < list.Entries.size(); i++) {
                        internalEntry = list.Entries.get(i);

                        if (internalEntry.getName().equals(dataSnapshot.getKey())) {
                            internalEntry.setAmount(
                                    internalEntry.getAmount() + (long) dataSnapshot.child("amount").getValue()
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
    static {
//        Log.d("call", "datamanager static");
//
//        getInstance().add(1, "Spaghetti", 500);
//        getInstance().add(1, "Tomato Sauce", 500, "124323324");
//        getInstance().add(1, "Tomato Sauce2", 200);
//        getInstance().add(1, "Onions", 3);
//
//        getInstance().add(2, "Spaghetti", 100);
//        getInstance().add(2, "Tomato Sauce", 1000);
//
//        getInstance().add(3, "Tomato Sauce", 500);
    }

    static void dropInstance(){
        instance = null;
    }

    void add(int list, String name, int amount, String barcode, int MM, int DD, int YYYY){
        //assume the list is 1
        EntryList listToAdd = inventoryList;

        if(list == EntryList.WANTED_LIST)
            listToAdd = wantedList;
        if(list == EntryList.SHOPPING_LIST)
            listToAdd = shoppingList;

        Entry entryToAdd = new Entry(listToAdd.id, amount, name, barcode, new MyDate(MM, DD, YYYY));

        // Write a message to the database
        myRef = userData.child(Integer.toString(list)).child(name);
        myRef.setValue(entryToAdd);

        //TODO: change amount instead of overriding
    }


//    void add(int list, String name, int amount, String barcode, Date DateBought){
//        //assume the list is 1
//        EntryList listToAdd = inventoryList;
//
//        if(list == EntryList.WANTED_LIST)
//            listToAdd = wantedList;
//        if(list == EntryList.SHOPPING_LIST)
//            listToAdd = shoppingList;
//
//        Entry entryToAdd = new Entry(listToAdd.id, amount, name, barcode, DateBought);
//
//        // Write a message to the database
//        myRef = userData.child(Integer.toString(list)).child(name);
//        myRef.setValue(entryToAdd);
//
//    }

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

    }
}