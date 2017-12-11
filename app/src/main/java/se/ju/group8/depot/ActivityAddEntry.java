package se.ju.group8.depot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;

/**
 * @author max
 * @date 10/24/17.
 */

public class ActivityAddEntry extends AppCompatActivity {

    int openTab;

    String BarcodeResult;


    public ActivityAddEntry(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        Intent intent = getIntent();
        openTab = intent.getIntExtra("openTab", 1);
        BarcodeResult = intent.getStringExtra("Barcode");

        if (BarcodeResult != null) {
            TextView textView = (TextView) findViewById(R.id.addBarcode);
            textView.setText(BarcodeResult);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy");
            String strDate = sdf.format(c.getTime());
            TextView textView1 = (TextView) findViewById(R.id.addDate);
            textView1.setText(strDate);
        }



    }



    public void onAddClick(View view){
        TextView name = (TextView) findViewById(R.id.addName);
        TextView amount = (TextView) findViewById(R.id.addAmount);
        TextView barcode = (TextView) findViewById(R.id.addBarcode);
        TextView date = (TextView) findViewById(R.id.addDate);

        int category = 1;

//        if(openTab == 2){
//
//        }
        if(openTab == 3){
            category = 2;
        }
        if(openTab == 4){
            category = 3;
        }

        String nameString = name.getText().toString();
        String amountString = amount.getText().toString();
        String barcodeString = barcode.getText().toString();
        String dateString = date.getText().toString();


        if (amountString.isEmpty()){
            DataManager.getInstance().add(category, nameString);
            finish();
            Log.d("log", "addentry: added");
            return;
        }

        if(barcodeString.isEmpty()){
            DataManager.getInstance().add(category, nameString, Integer.valueOf(amountString));
            finish();
            Log.d("log", "addentry: added");
            return;
        }

        DataManager.getInstance().add(category, nameString, Integer.valueOf(amountString), barcodeString);

//        DataManager.getInstance().add(category, nameString, Integer.valueOf(amountString), barcodeString, new Date());

        Log.d("log", "addentry: added");
        finish();
    }
}
