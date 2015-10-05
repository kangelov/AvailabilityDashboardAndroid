package com.qualicom.availabilitydashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qualicom.availabilitydashboard.db.PersistenceManager;
import com.qualicom.availabilitydashboard.net.CommunicationCallbacks;
import com.qualicom.availabilitydashboard.net.CommunicationManager;
import com.qualicom.availabilitydashboard.vo.Environment;
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
        implements ListActivityFragmentCallbacks, CommunicationCallbacks, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

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
            this.resetSelections(getDisplayList());

            Settings settings = getSettings();
            if (settings != null) {
                swipeLayout.setRefreshing(true);
                this.onRefresh();
            }
        }

        // The detail container view will be present only in the
        // large-screen layouts (res/values-large and
        // res/values-sw600dp). If this view is present, then the
        // activity should be in two-pane mode.
        //First, do we have Settings? If not no point to continue.
    }

    private List<Environment> getDisplayList() {
        PersistenceManager pm = new PersistenceManager(this);
        return pm.getAllEnvironments();
    }

    private Settings getSettings() {
        PersistenceManager pm = new PersistenceManager(this);
        return pm.getSettings();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Settings settings = getSettings();

        if (settings == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent settingsIntent = new Intent(ListActivity.this, LoginActivity.class);
                    ListActivity.this.startActivityForResult(settingsIntent, 0, null);
                }
            }, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.onRefresh();
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
        CommunicationManager manager = new CommunicationManager(this, new PersistenceManager(this));
        manager.refreshAvailability();
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
                startActivityForResult(menuIntent, 0, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.list_container);
        if (fragment instanceof EnvironmentListActivityFragment) {
            Log.i("BACK", "Back to environment fragment");
            selectedEnvironment = null;
            selectedService = null;
            selectedNode = null;
            ((EnvironmentListActivityFragment) fragment).setDisplayList(displayList);
        } else if (fragment instanceof ServiceListActivityFragment) {
            Log.i("BACK", "Back to service fragment");
            selectedService = null;
            selectedNode = null;
            ((ServiceListActivityFragment) fragment).setDisplayList(selectedEnvironment.getServices());
        } else if (mTwoPane && fragment instanceof NodeListActivityFragment) {
            /*
            In a two-panel arrangement (e.g. tablet) there is no transition between List activity and Detail
            activity, it's all done within the list activity and the detail is just a fragment. Going back
            *should* trigger the onBackPressed handler. This needs to be tested.
            */
            Log.i("BACK", "Back to node fragment");
            selectedNode = null;
            ((NodeListActivityFragment) fragment).setDisplayList(selectedService.getNodes());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.list_container);
        if (!mTwoPane && fragment != null && fragment instanceof NodeListActivityFragment) {
            /*
            In a single-panel arrangement (e.g. phone) there is a transition to a new activity. The Detail
            fragment is in a separate activity, so going back changes the entire activity. onBackPressed
            isn't getting called, but onResume is as the entire activity is resumed.
             */
            Log.i("BACK", "Back to node fragment");
            selectedNode = null;
        }
    }

    /**
     * This is called when we cannot preserve the user selections as things have changed on the
     * backend and one or more of the selected components are now unavailable. We need to clear
     * everything and go back to the environment panel.
     */
    private void resetSelections(List<Environment> environmentList) {

        this.displayList = environmentList;

        selectedEnvironment = null;
        selectedNode = null;
        selectedService = null;

        //Refresh screen
        Bundle arguments = new Bundle();
        arguments.putSerializable(ListActivityFragment.ARG_DISPLAY_LIST, (Serializable) displayList);
        arguments.putBoolean(ListActivityFragment.ARG_ONE_CLICK_ACTIVATION, mTwoPane);
        ListActivityFragment environmentFragment = new EnvironmentListActivityFragment();
        environmentFragment.setArguments(arguments);

        FragmentTransaction txn = getSupportFragmentManager().beginTransaction();
        txn.replace(R.id.list_container, environmentFragment);
        txn.replace(R.id.footer_container, new ListFooterFragment());
        txn.commit();
    }

    @Override
    public void handleRefresh() {
        Toast toast = Toast.makeText(this, R.string.communication_successful, Toast.LENGTH_SHORT);
        toast.show();

        //clear the back stack. We are starting over.
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++)
            getSupportFragmentManager().popBackStack();

        restoreEnvironmentSelection(getDisplayList());

        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setRefreshing(false);
    }

    private void restoreEnvironmentSelection(List<Environment> environmentList) {
        if (selectedEnvironment != null) {
            Environment oldEnvironment = selectedEnvironment;
            selectedEnvironment = null;
            for (int i = 0; i < environmentList.size(); i++) {
                Environment newEnvironment = environmentList.get(i);
                if (oldEnvironment.equals(newEnvironment)) {
                    this.displayList = environmentList;
                    selectedEnvironment = newEnvironment;
                    this.onEnvironmentSelected(i);
                    this.restoreServiceSelection(newEnvironment.getServices());
                }
            }
            if (selectedEnvironment == null) {

                //if the environment selection cannot be repeated, kick back to the environment fragment
                //since there is nothing else to do this for us here. This is the reset scenario.
                resetSelections(environmentList);
                Toast toast = Toast.makeText(this, R.string.unavailable_environment_selection, Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            //if no selections exist, we just set the new list and we are good to go.
            resetSelections(environmentList);
        }
    }

    private void restoreServiceSelection(List<Service> serviceList) {
        if (selectedService != null) {
            Service oldService = selectedService;
            selectedService = null;
            for (int i = 0; i < serviceList.size(); i++) {
                Service newService = serviceList.get(i);
                if (oldService.equals(newService)) {
                    selectedService = newService;
                    this.onServiceSelected(i);
                    this.restoreNodeSelection(newService.getNodes());
                }
            }
            if (selectedService == null) {
                selectedNode = null;
                Toast toast = Toast.makeText(this, R.string.unavailable_service_selection, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void restoreNodeSelection(List<Node> nodeList) {
        if (selectedNode != null) {
            Node oldNode = selectedNode;
            selectedNode = null;
            for (int i = 0; i < nodeList.size(); i++) {
                Node newNode = nodeList.get(i);
                if (oldNode.equals(newNode)) {
                    selectedNode = newNode;
                    this.onNodeSelected(i);
                }
            }
            if (selectedNode == null) {
                Toast toast = Toast.makeText(this, R.string.unavailable_node_selection, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void handleError(String message) {
        if (TextUtils.isEmpty(message))
            message = getString(R.string.communication_failed);
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();

        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setRefreshing(false);
    }
}
