package com.qualicom.availabilitydashboard;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.qualicom.availabilitydashboard.vo.Environment;
import com.qualicom.availabilitydashboard.vo.ListEntry;
import com.qualicom.availabilitydashboard.vo.Node;
import com.qualicom.availabilitydashboard.vo.Service;
import com.qualicom.availabilitydashboard.vo.Status;

import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a single Node detail screen.
 * This fragment is either contained in a {@link ListActivity}
 * in two-pane mode (on tablets) or a {@link DetailActivity}
 * on handsets.
 */
public class DetailFragment extends Fragment {


    public static final Map<Status, Integer> STATUS_TO_BACKGROUND_MAP = new HashMap<Status,Integer>();
    static {
        STATUS_TO_BACKGROUND_MAP.put(Status.OK, R.drawable.ok_background);
        STATUS_TO_BACKGROUND_MAP.put(Status.FAILED, R.drawable.failed_background);
        STATUS_TO_BACKGROUND_MAP.put(Status.WRONG_VERSION, R.drawable.wrong_version_background);
        STATUS_TO_BACKGROUND_MAP.put(Status.UNKNOWN, R.drawable.unknown_background);
    }

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_NODE = "node";
    public static final String ARG_SERVICE = "service";
    public static final String ARG_ENVIRONMENT = "environment";

    /**
     * The dummy content this fragment is presenting.
     */
    private Node node;
    private Service service;
    private Environment environment;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_NODE) &&
                getArguments().containsKey(ARG_SERVICE) &&
                getArguments().containsKey(ARG_ENVIRONMENT)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            this.node = (Node)getArguments().getSerializable(ARG_NODE);
            this.service = (Service)getArguments().getSerializable(ARG_SERVICE);
            this.environment = (Environment)getArguments().getSerializable(ARG_ENVIRONMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (node != null) {
            ((TextView) rootView.findViewById(R.id.detailNameView)).setText(node.getName());
            ((TextView) rootView.findViewById(R.id.detailStatusView)).setText(node.getStatus().toString());
            ((TextView) rootView.findViewById(R.id.detailVersionView)).setText(node.getVersion());
            ((TextView) rootView.findViewById(R.id.detailPingView)).setText(node.getResponse());
            ((GridLayout) rootView.findViewById(R.id.detail_layout)).setBackgroundResource(STATUS_TO_BACKGROUND_MAP.get(node.getStatus()));
        }

        return rootView;
    }
}
