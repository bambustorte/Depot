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

public class ContextFragmentInventoryList extends Fragment {

    static ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inventory, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.InventoryList);

        adapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_list_item_1,
                DataManager.inventoryEntries.entriesToStringList());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // TODO: show more information onClick, maybe delete option
                Log.println(Log.DEBUG, "entry", DataManager.inventoryEntries.get(position).toString());
            }
        });


//
//        final ArrayAdapter adapter = new ArrayAdapter<DataManager.Entry>(
//                this,
//                android.R.layout.simple_list_item_1,
//                DataManager.inventoryEntries
//        );
//
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                DataManager.Entry clickedEntry = DataManager.inventoryEntries.get(position);
////                if (getIntent().getIntExtra("action", 0) == 0) {
////                    Intent intent = new Intent(PickTodoActivity.this, ViewTodoActivity.class);
////                    intent.putExtra("todoIndex", position);
////                    startActivity(intent);
////                } else {
////                    new AlertDialog.Builder(PickTodoActivity.this).setTitle("Delete ToDo")
////                            .setMessage("really wanna delete it?")
////                            .setPositiveButton(
////                                    android.R.string.yes, new DialogInterface.OnClickListener() {
////                                        public void onClick(DialogInterface dialog, int wichButton) {
////                                            DataManager.inventoryEntries.remove(position);
//////                                            finish();
//////                                            listView.invalidate();
//////                                            View v = findViewById(R.id.linlay);
//////                                            v.invalidate();
////                                        }
////                                    }).setNegativeButton(
////                            android.R.string.no, new DialogInterface.OnClickListener() {
////                                public void onClick(DialogInterface dialog, int whichButton) {
////
////                                }
////                            }
////                    ).show();
////                }
////                ad.notifyDataSetChanged();
//
//            }
//        });

        return rootView;
    }

    static void update(){
        adapter.clear();
        adapter.addAll(DataManager.inventoryEntries.entriesToStringList());
        adapter.notifyDataSetChanged();
    }
}

