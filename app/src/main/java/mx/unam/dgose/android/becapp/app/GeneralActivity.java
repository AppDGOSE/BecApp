package mx.unam.dgose.android.becapp.app;

import java.util.Locale;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;


import android.content.Intent;

import mx.unam.dgose.android.becapp.R;

public class GeneralActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    Session session;

    Payments payments;
    Profile profile;
    Events events;

    private static final int INFO = 0;
    private static final int PAYMENTS = 1;
    private static final int EVENTS = 2;

    private static final int NUMBER_OF_TABS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);
                }
            });

        mViewPager.setOffscreenPageLimit(NUMBER_OF_TABS - 1);
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                             actionBar.newTab()
                             .setText(mSectionsPagerAdapter.getPageTitle(i))
                             .setTabListener(this));
        }

        Intent intent = getIntent();
        session = (Session) intent.getSerializableExtra("sessionObject");

        if (session.getNewEvents())
            showUnSeenEventsDialog();


        payments = new Payments(session);
        profile = new Profile(session);
        events = new Events(session);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            session.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            return true;
        }

        if (id == R.id.action_contact) {
            showContactDialog();
            return true;
        }

        if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showContactDialog() {
        FragmentManager manager = getSupportFragmentManager();
        ContactDialogFragment contactDialogFragment = new ContactDialogFragment();

        contactDialogFragment.show(manager, "ContactDialog");
    }

    public void showAboutDialog() {
        FragmentManager manager = getSupportFragmentManager();
        AboutDialogFragment aboutDialogFragment = new AboutDialogFragment();

        aboutDialogFragment.show(manager, "AboutDialog");
    }

    public void showUnSeenEventsDialog() {
        FragmentManager manager = getSupportFragmentManager();
        UnSeenEventsDialogFragment unSeenEventsDialogFragment = new UnSeenEventsDialogFragment();
        unSeenEventsDialogFragment.show(manager, "unseenEventsDialog");
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        if (tab.getPosition() == EVENTS) {
            AsyncSeenTask task = new AsyncSeenTask();
            String[] params = {};
            task.execute(params);
        }

        mViewPager.setCurrentItem(tab.getPosition());
    }

    private class AsyncSeenTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String[] params) {
            events.seen();
            return "";
        }

        @Override
        protected void onPostExecute(final String status) {
        }

        @Override
        protected void onCancelled() {
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.

            switch (position) {
            case INFO:
                return new InfoFragment(profile);
            case PAYMENTS:
                return new PaymentsFragment(payments);
            case EVENTS:
                return new EventsFragment(events);
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show total tabs.
            return NUMBER_OF_TABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
            case INFO:
                return getString(R.string.title_info).toUpperCase(l);
            case PAYMENTS:
                return getString(R.string.title_payments).toUpperCase(l);
            case EVENTS:
                return getString(R.string.title_events).toUpperCase(l);
            }
            return null;
        }
    }
}
