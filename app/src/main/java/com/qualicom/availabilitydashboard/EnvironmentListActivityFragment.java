package com.qualicom.availabilitydashboard;

import android.view.View;
import android.widget.ListView;

/**
 * A list fragment representing a list of Nodes. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link DetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link ListActivityFragmentCallbacks}
 * interface.
 */
public class EnvironmentListActivityFragment extends ListActivityFragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EnvironmentListActivityFragment() {
        super();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onEnvironmentSelected(position);
    }

    @Override
    public boolean doDisplayDescriptions() {
        return false;
    }

    @Override
    protected String getTitle() {
        return this.getString(R.string.header_title_environments);
    }
}
