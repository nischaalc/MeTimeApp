package com.example.roger.actuallymetime;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    ArrayList<ArrayList<String>> dayArray = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> classArray = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> goalArray = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> eventArray = new ArrayList<ArrayList<String>>();
    ArrayList<String> tokenArray = new ArrayList<String>();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        dayArray = (ArrayList<ArrayList<String>>) bundle.getSerializable("dayArray");
        classArray = (ArrayList<ArrayList<String>>) bundle.getSerializable("classArray");
        goalArray = (ArrayList<ArrayList<String>>) bundle.getSerializable("goalArray");
        eventArray = (ArrayList<ArrayList<String>>) bundle.getSerializable("eventArray");
        tokenArray = (ArrayList<String>) bundle.getSerializable("tokenArray");
        Log.i("LOST?", tokenArray.get(0));

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        Fragment fragment;
        fragment = new myWeekFrag();
        Bundle bdl = new Bundle();
        bundle.putSerializable("dayArray", dayArray);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment fragment = null;
        Bundle bundle;
        String title = "";

        switch(position){
            case 0:
                fragment = new myWeekFrag();
                title = "My Week";
                bundle = new Bundle();
                bundle.putSerializable("dayArray", dayArray);
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment = new myClassesFrag();
                title = "My Classes";
                bundle = new Bundle();
                bundle.putSerializable("classArray", classArray);
                bundle.putSerializable("tokenArray", tokenArray);
                fragment.setArguments(bundle);
                break;
            case 2:
                fragment = new myGoalsFrag();
                title = "My Goals";
                bundle = new Bundle();
                bundle.putSerializable("goalArray", goalArray);
                fragment.setArguments(bundle);
                break;
            case 3:
                fragment = new myEventsFrag();
                title = "My Events";
                bundle = new Bundle();
                bundle.putSerializable("eventArray", eventArray);
                fragment.setArguments(bundle);
                break;
            /*
            case 4:
                fragment = new addItemFrag();
                title = "Add Item";
                break;
                */
        }

        // update the main content by replacing fragments

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        setTitle(title);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "My Week";
                break;
            case 2:
                mTitle = "My Classes";
                break;
            case 3:
                mTitle = "My Goals";
                break;
            case 4:
                mTitle = "My Events";
                break;
            /*
            case 5:
                mTitle = "Add Items";
                */
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
