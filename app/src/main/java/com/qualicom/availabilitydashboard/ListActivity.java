package com.qualicom.availabilitydashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qualicom.availabilitydashboard.db.PersistenceManager;
import com.qualicom.availabilitydashboard.vo.Environment;
import com.qualicom.availabilitydashboard.vo.ListEntry;
import com.qualicom.availabilitydashboard.vo.Node;
import com.qualicom.availabilitydashboard.vo.Service;
import com.qualicom.availabilitydashboard.vo.Settings;

import java.io.Serializable;
import java.util.List;


/**
 * An activity representing a list of Nodes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link NodeListActivityFragment} and the item details
 * (if present) is a {@link DetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link ListActivityFragmentCallbacks} interface
 * to listen for item selections.
 */
public class ListActivity extends AppCompatActivity
        implements ListActivityFragmentCallbacks, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String ARG_DISPAYLIST = "displayList";
    public static final String ARG_SELECTEDENVIRONMENT = "selectedEnvironment";
    public static final String ARG_SELECTEDSERVICE = "selectedService";
    public static final String ARG_SELECTEDNODE = "selectedNode";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;


    private List<Environment> displayList;
    private Environment selectedEnvironment;
    private Service selectedService;
    private Node selectedNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.header);

        ImageView imageView = (ImageView)findViewById(R.id.header_logo);
        imageView.setOnClickListener(this);

        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);

        this.mTwoPane = (findViewById(R.id.detail_container) != null);

        if (savedInstanceState != null) {
            this.displayList = (List<Environment>) savedInstanceState.getSerializable(ARG_DISPAYLIST);
            this.selectedEnvironment = (Environment) savedInstanceState.getSerializable(ARG_SELECTEDENVIRONMENT);
            this.selectedService = (Service) savedInstanceState.getSerializable(ARG_SELECTEDSERVICE);
            this.selectedNode = (Node) savedInstanceState.getSerializable(ARG_SELECTEDNODE);
        } else {
            displayList = getDisplayList();

            Bundle arguments = new Bundle();
            arguments.putSerializable(ListActivityFragment.ARG_DISPLAY_LIST, (Serializable) displayList);
            arguments.putBoolean(ListActivityFragment.ARG_ONE_CLICK_ACTIVATION, mTwoPane);
            ListActivityFragment environmentFragment = new EnvironmentListActivityFragment();
            environmentFragment.setArguments(arguments);

            ListFooterFragment footer = new ListFooterFragment();
            Bundle footerArgs = new Bundle();
            footerArgs.putInt(ListFooterFragment.ARG_FOOTER_LAYOUT, R.layout.list_footer_new);
            footer.setArguments(footerArgs);

            FragmentTransaction txn = getSupportFragmentManager().beginTransaction();
            txn.replace(R.id.list_container, environmentFragment);
            txn.replace(R.id.footer_container, footer);
            txn.commit();
        }

        // The detail container view will be present only in the
        // large-screen layouts (res/values-large and
        // res/values-sw600dp). If this view is present, then the
        // activity should be in two-pane mode.
        //First, do we have Settings? If not no point to continue.
    }

    private List<Environment> getDisplayList() {
        return ListEntry.dummyEntries;
    }

    @Override
    protected void onStart() {
        super.onStart();
        PersistenceManager pm = new PersistenceManager(this);
        Settings settings = pm.getSettings();

        if (settings == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent settingsIntent = new Intent(ListActivity.this, LoginActivity.class);
                    ListActivity.this.startActivity(settingsIntent);
                }
            }, 0);
        }
        //else refreshData();
    }

    @Override
    public void onEnvironmentSelected(int id) {
        this.selectedEnvironment = displayList.get(id);
        Bundle arguments = new Bundle();
        arguments.putSerializable(ListActivityFragment.ARG_DISPLAY_LIST, (Serializable) selectedEnvironment.getServices());
        arguments.putBoolean(ListActivityFragment.ARG_ONE_CLICK_ACTIVATION, mTwoPane);
        ListActivityFragment serviceFragment = new ServiceListActivityFragment();
        serviceFragment.setArguments(arguments);
        FragmentTransaction txn = getSupportFragmentManager().beginTransaction();
        txn.addToBackStack(null);
        txn.replace(R.id.list_container, serviceFragment);
        txn.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        txn.commit();
    }

    @Override
    public void onServiceSelected(int id) {
        this.selectedService = selectedEnvironment.getServices().get(id);
        Bundle arguments = new Bundle();
        arguments.putSerializable(ListActivityFragment.ARG_DISPLAY_LIST, (Serializable) selectedService.getNodes());
        arguments.putBoolean(ListActivityFragment.ARG_ONE_CLICK_ACTIVATION, mTwoPane);
        ListActivityFragment nodeFragment = new NodeListActivityFragment();
        nodeFragment.setArguments(arguments);
        FragmentTransaction txn = getSupportFragmentManager().beginTransaction();
        txn.addToBackStack(null);
        txn.replace(R.id.list_container, nodeFragment);
        txn.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        txn.commit();
    }

    @Override
    public void onNodeSelected(int id) {
        this.selectedNode = selectedService.getNodes().get(id);
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(DetailFragment.ARG_NODE, selectedNode);
            arguments.putSerializable(DetailFragment.ARG_SERVICE, selectedService);
            arguments.putSerializable(DetailFragment.ARG_ENVIRONMENT, selectedEnvironment);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            FragmentTransaction txn = getSupportFragmentManager().beginTransaction();
            txn.addToBackStack(null);
            txn.replace(R.id.detail_container, fragment);
            txn.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            txn.commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra(DetailFragment.ARG_NODE, selectedNode);
            detailIntent.putExtra(DetailFragment.ARG_SERVICE, selectedService);
            detailIntent.putExtra(DetailFragment.ARG_ENVIRONMENT, selectedEnvironment);
            startActivity(detailIntent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_DISPAYLIST, (Serializable) displayList);
        outState.putSerializable(ARG_SELECTEDENVIRONMENT, selectedEnvironment);
        outState.putSerializable(ARG_SELECTEDSERVICE, selectedService);
        outState.putSerializable(ARG_SELECTEDNODE, selectedNode);
    }

    @Override
    public void setListTitle(String title) {
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.header_title)).setText(title);
    }

    //On clicking the Qualicom logo
    @Override
    public void onClick(View v) {
        Intent logoIntent = new Intent(Intent.ACTION_VIEW);
        logoIntent.setData(Uri.parse(getString(R.string.qualicom_url)));
        startActivity(logoIntent);
    }

    //On pulling list to refresh
    @Override
    public void onRefresh() {

        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setRefreshing(false);

        ListFooterFragment footer = new ListFooterFragment();
        Bundle footerArgs = new Bundle();
        footerArgs.putInt(ListFooterFragment.ARG_FOOTER_LAYOUT, R.layout.list_footer_updated);
        footer.setArguments(footerArgs);

        FragmentTransaction txn = getSupportFragmentManager().beginTransaction();
        txn.replace(R.id.footer_container, footer);
        txn.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_settings:
                Intent menuIntent = new Intent(this, LoginActivity.class);
                startActivity(menuIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
