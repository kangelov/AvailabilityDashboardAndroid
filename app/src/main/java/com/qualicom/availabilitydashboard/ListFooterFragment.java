package com.qualicom.availabilitydashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qualicom.availabilitydashboard.db.PersistenceManager;
import com.qualicom.availabilitydashboard.vo.Settings;

/**
 * Created by kangelov on 2015-08-27.
 */
public class ListFooterFragment extends Fragment {

    private static final String ARG_FOOTER_SETTINGS = "footerSettings";

    private static final int LAYOUT_FOOTER_NEW = R.layout.list_footer_new;
    private static final int LAYOUT_FOOTER_UPDATED = R.layout.list_footer_updated;

    private Settings settings;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_FOOTER_SETTINGS, settings);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_FOOTER_SETTINGS)) {
            settings = (Settings) savedInstanceState.getSerializable(ARG_FOOTER_SETTINGS);
        } else {
            PersistenceManager pm = new PersistenceManager(getActivity());
            settings = pm.getSettings();
        }

        if (settings == null || TextUtils.isEmpty(settings.getLastRefreshDate()) || TextUtils.isEmpty(settings.getLastUpdateDate()))
            view = inflater.inflate(LAYOUT_FOOTER_NEW, container, false);
        else {
            view = inflater.inflate(LAYOUT_FOOTER_UPDATED, container, false);
            TextView lastUpdateDateView = (TextView) view.findViewById(R.id.footer_last_updated_date);
            TextView lastRefreshDateView = (TextView) view.findViewById(R.id.footer_last_fetched_date);
            lastUpdateDateView.setText(settings.getLastUpdateDate());
            lastRefreshDateView.setText(settings.getLastRefreshDate());
        }
        return view;
    }
}
