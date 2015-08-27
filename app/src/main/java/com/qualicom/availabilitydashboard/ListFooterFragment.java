package com.qualicom.availabilitydashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kangelov on 2015-08-27.
 */
public class ListFooterFragment extends Fragment {

    public static final String ARG_FOOTER_LAYOUT = "footerLayout";

    private Integer layout;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args != null && args.containsKey(ARG_FOOTER_LAYOUT))
            this.layout = args.getInt(ARG_FOOTER_LAYOUT);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_FOOTER_LAYOUT))
            this.layout = savedInstanceState.getInt(ARG_FOOTER_LAYOUT);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_FOOTER_LAYOUT, this.layout);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_FOOTER_LAYOUT))
            this.layout = savedInstanceState.getInt(ARG_FOOTER_LAYOUT);

        if (layout != null)
            view = inflater.inflate(layout, container, false);
        else
            view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }
}
