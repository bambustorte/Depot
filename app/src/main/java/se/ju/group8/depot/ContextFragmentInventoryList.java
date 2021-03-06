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

public class ContextFragmentInventoryList extends Fragment {

    static AdapterEntryList adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inventory, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.InventoryList);

        adapter = new AdapterEntryList(rootView.getContext(),
                DataManager.getInstance().inventoryList.toArrayList(),
                1
                );

        listView.setAdapter(adapter);

        return rootView;
    }

    static void update(ArrayList<Entry> entries) {
        if (adapter != null) {
            adapter.update(entries);
        }
    }
}

