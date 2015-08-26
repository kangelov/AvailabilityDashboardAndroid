package com.qualicom.availabilitydashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qualicom.availabilitydashboard.vo.ListEntry;
import com.qualicom.availabilitydashboard.vo.Status;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kangelov on 2015-08-24.
 */
public class ListActivityFragmentArrayAdapter extends ArrayAdapter<ListEntry> {


    private final List<ListEntry> entries;

    private final Context context;

    private final boolean doShowDescription;

    private final static Map<Status, Integer> STATUS_TO_ICON_MAP = new HashMap<Status,Integer>();
    private final static Status DEFAULT_STATUS=Status.UNKNOWN;
    static {
        STATUS_TO_ICON_MAP.put(Status.OK, R.drawable.ok);
        STATUS_TO_ICON_MAP.put(Status.WRONG_VERSION, R.drawable.wrong_version);
        STATUS_TO_ICON_MAP.put(Status.FAILED, R.drawable.failed);
        STATUS_TO_ICON_MAP.put(Status.UNKNOWN, R.drawable.unknown);
    }


    public ListActivityFragmentArrayAdapter(Context context, List<ListEntry> entries, boolean doShowDescription) {
        super(context, -1, entries);
        this.entries = entries;
        this.context = context;
        this.doShowDescription = doShowDescription;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_entry, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.nameTextView);
        TextView descriptionView = (TextView) rowView.findViewById(R.id.descriptionTextView);
        ImageView statusView = (ImageView) rowView.findViewById(R.id.statusImageView);

        nameView.setText(entries.get(position).getName());
        descriptionView.setText(doShowDescription && entries.get(position).getStatus() != null ? entries.get(position).getStatus().name() : null);
        if (entries.get(position).getStatus() != null && STATUS_TO_ICON_MAP.containsKey(entries.get(position).getStatus())) {
            statusView.setImageResource(STATUS_TO_ICON_MAP.get(entries.get(position).getStatus()));
        } else {
            statusView.setImageResource(STATUS_TO_ICON_MAP.get(DEFAULT_STATUS));
        }

        return rowView;
    }
}
