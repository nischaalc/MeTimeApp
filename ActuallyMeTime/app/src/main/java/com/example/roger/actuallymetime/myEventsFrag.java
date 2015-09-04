package com.example.roger.actuallymetime;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Roger on 8/26/2015.
 */
public class myEventsFrag extends Fragment {
    View rootview;

    String[] tempStrings = {"Event 1", "Event 2", "Event 3", "Event 4", "Event 5", "Event 6"};
    ArrayList<ArrayList<String>> eventArray = new ArrayList<ArrayList<String>>();
    ArrayList<String> eventNames = new ArrayList<String>();
    ArrayList<String> startTimes = new ArrayList<String>();
    ArrayList<String> endTimes = new ArrayList<String>();

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.fragment_my_events, container, false);

        Bundle bundle = this.getArguments();
        eventArray = (ArrayList<ArrayList<String>>) bundle.getSerializable("eventArray");

        for(Integer i = 0; i<eventArray.size();i++){
            String startReturn = eventArray.get(i).get(0);

            /*
            String startParts[] = startReturn.split("T");
            String startDate = startParts[0];
            String startTime = startParts[1];
            String startDateParts[] = startDate.split("-");
            Integer startDay = Integer.parseInt(startDateParts[2]);
            */

            String startParts[] = startReturn.split("T");
            String startDate = startParts[0];
            String startTime = startParts[1];

            String endReturn = eventArray.get(i).get(1);
            String endParts[] = endReturn.split("T");
            String endDate = endParts[0];
            String endTime = endParts[1];

            String[] timeParts = startTime.split(":");
            startTime = timeParts[0] + ":" + timeParts[1];
            timeParts = endTime.split(":");
            endTime = timeParts[0]+":"+timeParts[1];

            String startDateParts[] = startDate.split("-");
            Integer startDay = Integer.parseInt(startDateParts[2]);

            startTimes.add(startTime);
            endTimes.add(endTime);
            eventNames.add(eventArray.get(i).get(2));

        }

        if(eventNames.size() == 0){
            eventNames.add("Nothing Planned");
        }

        final ListView eventList = (ListView) rootview.findViewById(R.id.lstEventList);

        /*
        ArrayAdapter<String> aAdapter = new ArrayAdapter(rootview.getContext(), android.R.layout.simple_list_item_1, eventNames);
        eventList.setAdapter(aAdapter);
        */

        ClassListAdapter adapter = new ClassListAdapter(this.getActivity(),eventNames, startTimes, endTimes);
        eventList.setAdapter(adapter);


        /*
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)eventList.getItemAtPosition(position);
                Toast.makeText(rootview.getContext(), item, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(rootview.getContext(), EventEditActivity.class);
                //ADD BUNDLES HERE
                //intent.putStringArrayListExtra(blah, halb);
                startActivity(intent);
            }
        });
        */

        return rootview;
    }
}
