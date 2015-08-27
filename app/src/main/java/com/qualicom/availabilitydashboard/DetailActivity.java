package com.qualicom.availabilitydashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An activity representing a single Node detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link DetailFragment}.
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Get the header in place
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setCustomView(R.layout.header);

        ImageView imageView = (ImageView)findViewById(R.id.header_logo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logoIntent = new Intent(Intent.ACTION_VIEW);
                logoIntent.setData(Uri.parse(getString(R.string.qualicom_url)));
                startActivity(logoIntent);
            }
        });
        ((TextView)findViewById(R.id.header_title)).setText(R.string.header_title_details);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(DetailFragment.ARG_NODE, getIntent().getSerializableExtra(DetailFragment.ARG_NODE));
            arguments.putSerializable(DetailFragment.ARG_SERVICE, getIntent().getSerializableExtra(DetailFragment.ARG_SERVICE));
            arguments.putSerializable(DetailFragment.ARG_ENVIRONMENT, getIntent().getSerializableExtra(DetailFragment.ARG_ENVIRONMENT));
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
