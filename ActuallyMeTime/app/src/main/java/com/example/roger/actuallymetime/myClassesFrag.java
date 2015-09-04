package com.example.roger.actuallymetime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Roger on 8/26/2015.
 */
public class myClassesFrag extends Fragment {
    View rootview;
    String[] tempStrings = {"Class 1", "Class 2", "Class 3", "Class 4", "Class 5", "Class 6", "Class 7"};
    ArrayList<ArrayList<String>> classArray = new ArrayList<ArrayList<String>>();

    ArrayList<String> classNames = new ArrayList<String>();
    ArrayList<String> startTimes = new ArrayList<String>();
    ArrayList<String> endTimes = new ArrayList<String>();
    ArrayList<String> tokenArray = new ArrayList<String>();

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.fragment_my_classes, container, false);

        Bundle bundle = this.getArguments();
        classArray = (ArrayList<ArrayList<String>>) bundle.getSerializable("classArray");
        tokenArray = (ArrayList<String>) bundle.getSerializable("tokenArray");
        Log.i("Still Here", tokenArray.get(0));


        /*
        classNames.add("Add New Class");
        startTimes.add("");
        endTimes.add("");
        */

        for(Integer i = 0; i<classArray.size();i++){
            String startReturn = classArray.get(i).get(0);
            String startParts[] = startReturn.split("T");
            String startDate = startParts[0];
            String startTime = startParts[1];

            String endReturn = classArray.get(i).get(1);
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

            classNames.add(classArray.get(i).get(2));

        }

        if(classNames.size() == 0){
            classNames.add("Nothing Planned");
            startTimes.add("");
            endTimes.add("");
        }

        final ListView classList = (ListView)rootview.findViewById(R.id.lstClassList);

        /*
        ArrayAdapter<String> aAdapter = new ArrayAdapter<>(rootview.getContext(), android.R.layout.simple_list_item_1, classNames);
        classList.setAdapter(aAdapter);
        */

        ClassListAdapter adapter = new ClassListAdapter(this.getActivity(),classNames, startTimes, endTimes);
        classList.setAdapter(adapter);


        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                Intent intent = new Intent(rootview.getContext(), ClassEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("tokenArray", tokenArray);
                intent.putExtras(bundle);
                //ADD BUNDLES HERE
                //intent.putStringArrayListExtra(blah, halb);
                startActivity(intent);
            }
            }
        });


        return rootview;
    }
}
