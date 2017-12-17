package se.ju.group8.depot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import java.util.Date;

/**
 * @author max
 * @date 10/24/17.
 */

public class ActivityAddEntry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int openTab;

    String BarcodeResult;
    int type;

    TextView textViewMM;
    TextView textViewDD;
    TextView textViewYY;
    TextView textViewName;
    TextView textViewBarcode;
    TextView textViewAmount;
    Spinner spinner;

    public ActivityAddEntry(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        Intent intent = getIntent();
        openTab = intent.getIntExtra("openTab", 1);
        BarcodeResult = intent.getStringExtra("Barcode");

        type = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        textViewMM = (TextView) findViewById(R.id.addDateMonth);
        textViewDD = (TextView) findViewById(R.id.addDateDay);
        textViewYY = (TextView) findViewById(R.id.addDateYear);
        textViewName = (TextView) findViewById(R.id.addName);
        textViewAmount = (TextView) findViewById(R.id.addAmount);
        textViewBarcode = (TextView) findViewById(R.id.addBarcode);
        spinner = (Spinner) findViewById(R.id.addUnit);


        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this.getApplicationContext(),
                R.array.units_array,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        Spinner spinner = (Spinner) findViewById(R.id.addUnit);
        spinner.setOnItemSelectedListener(this);


        textViewMM.setText(String.valueOf(month));
        textViewDD.setText(String.valueOf(day));
        textViewYY.setText(String.valueOf(year));

        if (BarcodeResult != null) {
            textViewBarcode.setText(BarcodeResult);

            DataManager.getInstance().barcodes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable iterable = dataSnapshot.getChildren();
                    DataSnapshot child;
                    for (Object o : iterable) {
                        child = (DataSnapshot) o;
//                        Log.d("test", "lala " + child.getValue().toString());
                        if(child.getKey().equals(BarcodeResult)){
//                            Log.d("test", "found it");
                            textViewName.setText(child.getValue().toString());
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

//        Log.d("test", "bar = " + BarcodeResult);

    }


    public void onAddClick(View view){

        EditText dateMM = (EditText) findViewById(R.id.addDateMonth);
        EditText dateDD = (EditText) findViewById(R.id.addDateDay);
        EditText dateYY = (EditText) findViewById(R.id.addDateYear);
        //TODOne: finish data structure for date

        int category = 1;

        if(openTab == 3){
            category = 2;
        }
        if(openTab == 4){
            category = 3;
        }

        String nameString = textViewName.getText().toString();
        if (nameString.isEmpty()) return;
        String amountString = (textViewAmount.getText().toString().isEmpty()) ? "1" : textViewAmount.getText().toString();
        String barcodeString = (textViewBarcode.getText().toString().isEmpty()) ? "" : DataManager.getInstance().addBarcode(textViewBarcode.getText().toString(), nameString);

        Log.d("test", "barcode " + barcodeString);

        int dateMMInt = (dateMM.getText().toString().isEmpty()) ? -1 : Integer.parseInt(dateMM.getText().toString());
        int dateDDInt = (dateDD.getText().toString().isEmpty()) ? -1 : Integer.parseInt(dateDD.getText().toString());
        int dateYYInt = (dateYY.getText().toString().isEmpty()) ? -1 : Integer.parseInt(dateYY.getText().toString());



        DataManager.getInstance().add(category, nameString, Long.valueOf(amountString), barcodeString, dateMMInt, dateDDInt, dateYYInt, type);
        Log.d("log", "addentry: added");

        //TODO: the following function call doesn't work at all,
        // the function itself should work fine, but it somehow breaks firebase
        // and our other data structures. So we kept this code commented out, since
        // we could not figure out what the problem is.
//        DataManager.getInstance().updateShoppingList();

        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
