package se.ju.group8.depot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.ju.group8.depot.R;

public class ContextFragmentInventoryList extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        System.out.println("test");
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }
}

