package com.example.roger.actuallymetime;

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
public class myGoalsFrag extends Fragment {
    View rootview;

    String[] tempStrings = {"Goal 1", "Goal 2", "Goal 3", "Goal 4", "Goal 5"};
    ArrayList<ArrayList<String>> goalArray = new ArrayList<ArrayList<String>>();
    ArrayList<String> startTimes = new ArrayList<String>();
    ArrayList<String> endTimes = new ArrayList<String>();
    ArrayList<String> goalNames = new ArrayList<String>();

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.fragment_my_goals, container, false);

        Bundle bundle = this.getArguments();
        goalArray = (ArrayList<ArrayList<String>>) bundle.getSerializable("goalArray");

        for(Integer i = 0; i<goalArray.size();i++) {
            String startReturn = goalArray.get(i).get(0);
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

            String endReturn = goalArray.get(i).get(1);
            String endParts[] = endReturn.split("T");
            String endDate = endParts[0];
            String endTime = endParts[1];

            String[] timeParts = startTime.split(":");
            startTime = timeParts[0] + ":" + timeParts[1];
            timeParts = endTime.split(":");
            endTime = timeParts[0]+":"+timeParts[1];

            String startDateParts[] = startDate.split("-");
            Integer startDay = Integer.parseInt(startDateParts[2]);



            startTimes.add("");
            endTimes.add("");


            goalNames.add(goalArray.get(i).get(2));
        }

        if(goalNames.size()==0){
            goalNames.add("Nothing Planned");
        }

        final ListView goalList = (ListView) rootview.findViewById(R.id.lstGoalsList);

        /*
        ArrayAdapter<String> aAdapter = new ArrayAdapter(rootview.getContext(), android.R.layout.simple_list_item_1, goalNames);
        goalList.setAdapter(aAdapter);
        */

        ClassListAdapter adapter = new ClassListAdapter(this.getActivity(),goalNames, startTimes, endTimes);
        goalList.setAdapter(adapter);

        /*
        goalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)goalList.getItemAtPosition(position);
                Toast.makeText(rootview.getContext(), item, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(rootview.getContext(), GoalEditActivity.class);
                startActivity(intent);
            }
        });
        */

        return rootview;
    }
}
