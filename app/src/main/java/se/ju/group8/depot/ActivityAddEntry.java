package se.ju.group8.depot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * @author max
 * @date 10/24/17.
 */

public class ActivityAddEntry extends AppCompatActivity {

    int openTab;

    public ActivityAddEntry(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        Intent intent = getIntent();
        openTab = intent.getIntExtra("openTab", 1);
    }

    public void onAddClick(View view){
        TextView name = (TextView) findViewById(R.id.addName);
        int category = 1;


        if(openTab == 2){

        }
        if(openTab == 3){
            category = 2;
        }
        if(openTab == 4){
            category = 3;
        }

        String nameString = name.getText().toString();
//        TextView date;

//        date = (TextView) findViewById(R.id.addDate);

        Log.d("added", DataManager.getInstance(view.getContext()).add(nameString).toString());
        finish();
    }
}
