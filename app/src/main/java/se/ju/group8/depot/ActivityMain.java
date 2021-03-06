package se.ju.group8.depot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    static {
        //enable firebase offline capabilities
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    //this is the data manager
    DataManager dataManager;

    FirebaseUser user;

    //fragment variables for later use (switching through "screens")
    Fragment fragmentMain;
    Fragment fragmentInventoryList;
    Fragment fragmentShoppingList;
    Fragment fragmentWantedItemsList;
    Fragment fragmentTest;

    //fragment manager and transaction for dynamically changing fragments
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    //store which tab is currently open
    int openTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Intent login = new Intent(this.getApplicationContext(), ActivityLogin.class);
            startActivity(login);
            finish();
        } else {

            //start data manager first
            dataManager = DataManager.getInstance();
        }
        //set view to the fragment_main.xml layout
        setContentView(R.layout.fragment_main);

        //"toolbar"... it's actually the bar on top of the main screen, including
        //the floating menu, the main content of the application and the name
        //displayed in the top of the app
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //the little round add button in the bottom left corner of the main screen(s)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ActivityMain.this).setTitle(R.string.dialog_select_add_method)
                        .setMessage(R.string.dialog_how_to_add)
                        .setNegativeButton(
                                R.string.dialog_manually, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Intent intent = new Intent(ActivityMain.this.getApplicationContext(), ActivityAddEntry.class);
                                        intent.putExtra("openTab", openTab);
                                        startActivity(intent);
                                    }
                                }
                        ).setPositiveButton(
                        R.string.dialog_camera, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int witchButton) {
//                                DataManager.inventoryEntries.remove(position);

                                Intent myIntent = new Intent();
                                myIntent.setClassName("info.androidhive.barcodereader", "info.androidhive.barcodereader.MainActivity");
                                startActivityForResult(myIntent, 123);
                                //TODOne: add items to inventory, maybe make it dependent on which fragment is displayed
                                //TODOne: e.g. add an item to the wanted list when this fragment is displayed


                                Context context = getApplicationContext();
                                CharSequence text = ("Searching for Barcode...");
                                int duration = Toast.LENGTH_LONG;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        }
                ).show();

            }
        });

        //drawer menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //add the actual drawer inventoryEntries to the drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //fragment setup (they are created here)
        fragmentInventoryList = new ContextFragmentInventoryList();
        fragmentShoppingList = new ContextFragmentShoppingList();
        fragmentMain = new ContextFragmentInventoryList();
        fragmentWantedItemsList = new ContextFragmentWantedItemsList();
        fragmentTest = new ContextFragmentTest();

        //setup for the dynamic fragment change stuff
        fragmentManager = ActivityMain.this.getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        //Don'tForget: replace test fragment with inventory list fragment
        //set the inventory list as the first displayed fragment after the app gets started
//        fragmentTransaction.replace(R.id.context_container, fragmentTest, "test");
        fragmentTransaction.replace(R.id.context_container, fragmentInventoryList, "inventoryList");
        fragmentTransaction.commitNow(); //commitNow to access the views instantly

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ContextFragmentInventoryList.adapter != null) {
            ContextFragmentInventoryList.update(DataManager.getInstance().inventoryList.Entries);
        }
        if(ContextFragmentWantedItemsList.adapter != null) {
            ContextFragmentWantedItemsList.update(DataManager.getInstance().wantedList.Entries);
        }
        if(ContextFragmentShoppingList.adapter != null) {
            ContextFragmentShoppingList.update(DataManager.getInstance().shoppingList.Entries);
        }

        Log.d("files", getFilesDir().toString());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //if the drawer is open, close it
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //otherwise normal back button behavior
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //sign out option
        if (id == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            //TODOne: finish()?
            DataManager.dropInstance();
            finish();
            startActivity(new Intent(ActivityMain.this.getApplicationContext(), ActivityMain.class));
//            ActivityMain.this.recreate();
            return true;
        }

        //display current user
        if (id == R.id.menu_show_user) {
            Toast.makeText(ActivityMain.this.getApplicationContext(),
                    user.getUid(), Toast.LENGTH_LONG).show();
            return true;
        }

        //open test fragment
        if (id == R.id.menu_test_fragment) {
            fragmentManager = ActivityMain.this.getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.context_container, fragmentTest, "test");
            fragmentTransaction.commitNow();
          return true;
        }

        //populate list
        if (id == R.id.menu_populate) {
//            DataManager.getInstance().add(1, "Spaghetti", 500L, "", -1, -1, -1, 0);
//            DataManager.getInstance().add(1, "Tomato Sauce", 500L, "124323324", -1, -1, -1, 0);
//            DataManager.getInstance().add(1, "Tomato Sauce2", 200L, "", 2, 15, 2016, Entry.UNIT_LITER);
//            DataManager.getInstance().add(1, "Onions", 3L, "", -1, -1, -1,0);
//
//            DataManager.getInstance().add(2, "Spaghetti", 100l, "", -1, -1, -1);
//            DataManager.getInstance().add(2, "Tomato Sauce", 1000l, "", -1, -1, -1);
//
//            DataManager.getInstance().add(3, "Tomato Sauce", 500l, "dd", -1, -1, -1);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        //get the id of the item that was clicked
        int id = item.getItemId();

        //in this if else construct the fragments get changed dynamically
        if (id == R.id.nav_open_inventory) {
            fragmentManager = ActivityMain.this.getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.context_container, fragmentInventoryList, "inventoryList");
            fragmentTransaction.commitNow();
            openTab = 1;

        } else if (id == R.id.nav_wanted_items_list) {
            fragmentManager = ActivityMain.this.getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.context_container, fragmentWantedItemsList, "wantedItemsList");
            fragmentTransaction.commitNow();
            openTab = 3;

        } else if (id == R.id.nav_shopping_list) {
            fragmentManager = ActivityMain.this.getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.context_container, fragmentShoppingList, "shoppingList");
            fragmentTransaction.commitNow();
            openTab = 4;
        }

        //finally close the drawer after tap on an item
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 123) {
            String BAR = data.getStringExtra("barcode");
            Intent intent = new Intent(getApplicationContext(), ActivityAddEntry.class);
            intent.putExtra("openTab", getOpenTab());
            intent.putExtra("Barcode", BAR);

            startActivity(intent);
        }
    }

    public int getOpenTab() {
        return openTab;
    }
}
