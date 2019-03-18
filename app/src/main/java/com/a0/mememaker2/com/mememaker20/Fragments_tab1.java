package com.a0.mememaker2.com.mememaker20;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a0.mememaker2.com.mememaker20.R;

public class Fragments_tab1 extends Fragment {

    private static String TAG = "Fragment_TAB 1";
    TextView txtview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fargment_tabs1,container,false);
        txtview= view.findViewById(R.id.tab1);
        return  view;

    }
}
