package se.ju.group8.depot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityEntryInfo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_info);

        Intent intent = getIntent();

        String entryName = intent.getStringExtra("nameOfEntry");
        final int listNumber = intent.getIntExtra("numberOfList", 1);

        DataManager.EntryList entryList = DataManager.getInstance().numberToList(listNumber);

        final Entry entry = entryList.getEntryByName(entryName);

        String name = entry.getName();
        final Long amount = entry.getAmount();

        final TextView textViewName = (TextView) findViewById(R.id.entry_info_name);

        final EditText textViewAmount = (EditText) findViewById(R.id.entry_info_amount);
        final EditText textViewNewAmount = (EditText) findViewById(R.id.entry_info_new_amount);
        SeekBar seekBar = (SeekBar) findViewById(R.id.entry_info_seekbar);

        final EditText editTextBarcode = (EditText) findViewById(R.id.entry_info_barcode);

        type = 0;

        Spinner unitSpinner = (Spinner) findViewById(R.id.entry_info_unitSpinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this.getApplicationContext(),
                R.array.units_array,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(spinnerAdapter);
        unitSpinner.setOnItemSelectedListener(this);
        unitSpinner.setSelection(entry.getType());


        final EditText editTextDateMM = (EditText) findViewById(R.id.entry_info_date_mm);
        final EditText editTextDateDD = (EditText) findViewById(R.id.entry_info_date_dd);
        final EditText editTextDateYY = (EditText) findViewById(R.id.entry_info_date_yy);

        Button button = (Button) findViewById(R.id.entry_info_update);


        textViewName.setText(name);

        textViewAmount.setText(amount.toString());
        textViewNewAmount.setText(textViewAmount.getText());

        seekBar.setMax(amount.intValue());
        seekBar.setProgress(seekBar.getMax());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewNewAmount.setText(
                        String.valueOf(
                                (long) (amount * ( (float) progress / seekBar.getMax() ))
                        )
                );
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        editTextBarcode.setText(entry.getBarcode());

        editTextDateMM.setText(String.valueOf(entry.getDateBought().getMM()));
        editTextDateDD.setText(String.valueOf(entry.getDateBought().getDD()));
        editTextDateYY.setText(String.valueOf(entry.getDateBought().getYY()));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstance()
                        .updateEntry(
                                listNumber,
                                entry,
                                amount - Long.valueOf(textViewNewAmount.getText().toString()),
                                new MyDate(
                                        Integer.parseInt(editTextDateMM.getText().toString()),
                                        Integer.parseInt(editTextDateDD.getText().toString()),
                                        Integer.parseInt(editTextDateYY.getText().toString())
                                        ),
                                editTextBarcode.getText().toString(),
                                null,
                                type
                        );
                //TODO: allow user to verify
                finish();

//                new android.support.v7.app.AlertDialog.Builder(ActivityEntryInfo.this.getApplicationContext())
//                        .setTitle(R.string.entry_info_confirm)
//                        .setMessage(R.string.entry_info_sure)
//                        .setNegativeButton(
//                            R.string.Yes, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    finish();
//                                }
//                            }
//                        ).setPositiveButton(
//                            R.string.No, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int witchButton) {
//                                    //nothing
//                                }
//                            }
//                    ).show();

//                Log.d("test", textViewNewAmount.getText().toString());
//                Log.d("test", textViewAmount.getText().toString());
//                Log.d("test", String.valueOf(Long.parseLong(textViewNewAmount.getText().toString())
//                        - Long.parseLong(textViewAmount.getText().toString())));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        type = 0;
    }
}
