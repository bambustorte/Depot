package se.ju.group8.depot;

import android.content.Intent;
import android.os.Bundle;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

        //enable firebase offline capabilities
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

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
                                        Intent intent = new Intent(ActivityMain.this, ActivityAddEntry.class);
                                        intent.putExtra("openTab", openTab);
                                        startActivity(intent);
                                    }
                                }
                        ).setPositiveButton(
                        R.string.dialog_camera, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int witchButton) {
//                                DataManager.inventoryEntries.remove(position);
                                //TODO: add items to inventory, maybe make it dependent on which fragment is displayed
                                //TODO: e.g. add an item to the wanted list when this fragment is displayed
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
        fragmentTransaction.replace(R.id.context_container, fragmentTest, "test");
//        fragmentTransaction.replace(R.id.context_container, fragmentInventoryList, "inventoryList");
        fragmentTransaction.commitNow(); //commitNow to access the views instantly

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ContextFragmentInventoryList.adapter != null) {
            ContextFragmentInventoryList.update();
        }
        if(ContextFragmentWantedItemsList.adapter != null) {
            ContextFragmentWantedItemsList.update();
        }
        if(ContextFragmentShoppingList.adapter != null) {
            ContextFragmentShoppingList.update();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            //TODO: make and implement settings screen
            Snackbar.make(findViewById(R.id.context_container), "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return true;
        }

        //sign out option
        if (id == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            //TODO: finish()?
            DataManager.dropInstance();
            finish();
            startActivity(new Intent(ActivityMain.this.getApplicationContext(), ActivityMain.class));
//            ActivityMain.this.recreate();
            return true;
        }

        //display current user
        if (id == R.id.menu_show_user) {
//            ContextFragmentTest cft = (ContextFragmentTest) fragmentTest;
//            cft.update();
            Toast.makeText(ActivityMain.this.getApplicationContext(),
                    user.getUid(), Toast.LENGTH_LONG)
                    .show();
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

        return super.onOptionsItemSelected(item);
    }

    //    @SuppressWarnings("StatementWithEmptyBody")
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

        } else if (id == R.id.nav_scan_item) {
            //TODO: ask for permission and use camera or open camera app
            Toast.makeText(this, "camera", Toast.LENGTH_LONG).show();
            openTab = 2;

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

    public int getOpenTab() {
        return openTab;
    }
}
