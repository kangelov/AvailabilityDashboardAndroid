package com.qualicom.availabilitydashboard;

/**
 * A callback interface that all activities containing this fragment must
 * implement. This mechanism allows activities to be notified of item
 * selections.
 */
public interface ListActivityFragmentCallbacks {

    public void onEnvironmentSelected(int id);

    public void onServiceSelected(int id);

    public void onNodeSelected(int id);

    public void setListTitle(String title);

}
