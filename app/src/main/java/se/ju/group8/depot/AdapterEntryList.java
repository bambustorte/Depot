package se.ju.group8.depot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hama17zp on 2017-12-11.
 */

public class AdapterEntryList extends BaseAdapter{

    private Context context;
    private ArrayList<Entry> entries;
    private int listNumber;

    AdapterEntryList(Context context, ArrayList<Entry> entries, int listNumber) {
        this.context = context;
        this.entries = entries;
        this.listNumber = listNumber;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(this.context.getApplicationContext());
        View customView = convertView;

        customView = (customView == null) ? inflater.inflate(R.layout.list_view_entry, null) : customView;


        Entry entry = (Entry) getItem(position);

        final TextView textView1 = (TextView) customView.findViewById(R.id.list_view_entry_text1);
        TextView textView2 = (TextView) customView.findViewById(R.id.list_view_entry_text2);
        Button buttonRemove = (Button) customView.findViewById(R.id.list_view_entry_remove);
        final TextView textId = (TextView) customView.findViewById(R.id.list_view_entry_id);

        textView1.setText(entry.getName());
        textView2.setText(String.valueOf(entry.getAmount()));
        textId.setText(String.valueOf(entry.getId()));


        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstance().removeEntry(listNumber, textView1.getText().toString());
            }
        });

        return customView;
    }

    void update(ArrayList<Entry> entries){
        //new list
        this.entries = entries;
        //update
        notifyDataSetChanged();
    }
}
