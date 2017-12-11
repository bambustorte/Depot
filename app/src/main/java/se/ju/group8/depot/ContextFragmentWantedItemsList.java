package se.ju.group8.depot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ContextFragmentWantedItemsList extends Fragment {

    static AdapterEntryList adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wanted_items_list, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.WantedList);

        adapter = new AdapterEntryList(rootView.getContext(),
                DataManager.getInstance().wantedList.toArrayList(),
                2
        );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO: show more information onClick, maybe delete option
                Log.println(Log.DEBUG, "entry", adapter.getItem(position).toString());
            }
        });

        return rootView;
    }

    static void update(ArrayList<Entry> entries) {
        if (adapter != null) {
            adapter.update(entries);
        }
    }
}

