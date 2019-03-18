package com.a0.mememaker2.com.mememaker20;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragments_tab3 extends Fragment {

    private static String TAG = "Fragment_TAB 3";
    TextView txtview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fargment_tabs3,container,false);
        txtview= view.findViewById(R.id.tab3);
        return  view;

    }
}
