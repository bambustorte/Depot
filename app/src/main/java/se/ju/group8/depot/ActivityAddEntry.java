package se.ju.group8.depot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.Locale;

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
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        TextView textView1 = (TextView) findViewById(R.id.addDateMonth);
        TextView textView2 = (TextView) findViewById(R.id.addDateDay);
        TextView textView3 = (TextView) findViewById(R.id.addDateYear);


        textView1.setText(String.valueOf(month));
        textView2.setText(String.valueOf(day));
        textView3.setText(String.valueOf(year));

    }



    public void onAddClick(View view){
        TextView name = (TextView) findViewById(R.id.addName);
        TextView amount = (TextView) findViewById(R.id.addAmount);
        TextView barcode = (TextView) findViewById(R.id.addBarcode);
        TextView date = (TextView) findViewById(R.id.addDate);

        EditText dateMM = (EditText) findViewById(R.id.addDateMonth);
        EditText dateDD = (EditText) findViewById(R.id.addDateDay);
        EditText dateYY = (EditText) findViewById(R.id.addDateYear);
        //TODO: finish data structure for date

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
            Log.d("log", "addentry: added");
            finish();
            return;
        }

        if(barcodeString.isEmpty()){
            DataManager.getInstance().add(category, nameString, Integer.valueOf(amountString));
            Log.d("log", "addentry: added");
            finish();
            return;
        }

        if (dateString.isEmpty()) {
            DataManager.getInstance().add(category, nameString, Integer.valueOf(amountString), barcodeString);
            Log.d("log", "addentry: added");
            finish();
            return;
        }


        DateFormat format = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.ENGLISH);
        Date date1 = new Date();
        try {
            date1 = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        DataManager.getInstance().add(category, nameString, Integer.valueOf(amountString), barcodeString, date1);
        Log.d("log", "addentry: added");
        finish();
    }
}
