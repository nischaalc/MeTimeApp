package com.example.roger.actuallymetime;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class DayViewActivity extends ActionBarActivity {
    String dayOfWeek = "TempDay";
    String[] tempStrings = {"Math", "Science", "Chores", "Homework", "Gym", "Party"};

    //ArrayList<ArrayList<String>> dayArray = new ArrayList<ArrayList<String>>();

    ArrayList<String> dayEvents = new ArrayList<String>();
    ArrayList<String> eventStarts = new ArrayList<String>();
    ArrayList<String> eventEnds = new ArrayList<String>();
    ArrayList<String> startTimes = new ArrayList<String>();
    ArrayList<String> endTimes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);

        dayEvents = this.getIntent().getExtras().getStringArrayList("Event Names");
        eventStarts = this.getIntent().getExtras().getStringArrayList("Event Start Time");
        eventEnds = this.getIntent().getExtras().getStringArrayList("Event End Time");

        ArrayList<String> value = new ArrayList<String>();
        for(int i=0; i< dayEvents.size(); i++){
            //value.add(dayEvents.get(i) + "  " + eventStarts.get(i).substring(0,5) + "  " + eventEnds.get(i).substring(0,5));
            startTimes.add("");
            endTimes.add("");
        }

        /*
        Bundle bundle = getIntent().getExtras();
        dayOfWeek = bundle.getString("Day");
        dayArray = (ArrayList<ArrayList<String>>) bundle.getSerializable("dayArray");
        */


        setTitle(dayOfWeek);


        ListView dayCalander = (ListView) findViewById(R.id.lstDayCalander);

        /*
        ArrayAdapter<String> aAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, value);
        dayCalander.setAdapter(aAdapter);
        */


        ClassListAdapter adapter = new ClassListAdapter(this,dayEvents, startTimes, endTimes);
        dayCalander.setAdapter(adapter);

        //Toast.makeText(this, Integer.toString(dayArray.size()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_day_view, menu);
        return true;
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
}
