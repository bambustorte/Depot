package se.ju.group8.depot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContextFragmentShoppingList extends Fragment {

    static ArrayAdapter<Entry> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.ShoppingList);

        adapter = new ArrayAdapter<>(rootView.getContext(),
                android.R.layout.simple_list_item_1,
                DataManager.getInstance().shoppingList.toArrayList());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO: show more information onClick, maybe delete option
//                Log.println(Log.DEBUG, "entry", DataManager.shoppingListEntries.get(position).toString());
            }
        });

        return rootView;
    }
    static void update(){
        adapter.clear();
        adapter.addAll(DataManager.getInstance().shoppingList.toArrayList());
        adapter.notifyDataSetChanged();
    }
}

