package se.ju.group8.depot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class ActivityEntryInfo extends AppCompatActivity {

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

        final TextView textViewAmount = (TextView) findViewById(R.id.entry_info_amount);
        final TextView textViewNewAmount = (TextView) findViewById(R.id.entry_info_new_amount);
        SeekBar seekBar = (SeekBar) findViewById(R.id.entry_info_seekbar);

        final EditText editTextBarcode = (EditText) findViewById(R.id.entry_info_barcode);

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
                                -1
                        );
                //TODO: allow user to verify
                Log.d("test", textViewNewAmount.getText().toString());
                Log.d("test", textViewAmount.getText().toString());
                Log.d("test", String.valueOf(Long.parseLong(textViewNewAmount.getText().toString())
                        - Long.parseLong(textViewAmount.getText().toString())));
                finish();
            }
        });
    }
}
