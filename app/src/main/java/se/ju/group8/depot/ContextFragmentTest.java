package se.ju.group8.depot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ContextFragmentTest extends Fragment {

    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.framgment_test, container, false);

        return rootView;
    }
    public void update(){
        TextView textView1 = rootView.findViewById(R.id.tv1);

        textView1.setText(FirebaseAuth.getInstance().getCurrentUser().toString());
    }
}

