package se.ju.group8.depot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import se.ju.group8.depot.R;

public class ContextFragmentShoppingList extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        System.out.println("test");

        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.ShoppingList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_list_item_1,
                DataManager.entriesToStringArray(DataManager.shoppingListEntries));

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO: show more information onClick, maybe delete option
                System.out.println("test: " + DataManager.shoppingListEntries.get(position));
            }
        });

        return rootView;
    }
}

