package com.example.roger.actuallymetime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Roger on 8/26/2015.
 */
public class myWeekFrag extends Fragment {
    View rootview;

    Button buttonPrev, buttonNext;
    ViewFlipper viewFlipper, viewFlipperOther;

    Animation slide_in_left, slide_out_right, slide_out_left, slide_in_right;

    Calendar calendar = Calendar.getInstance();
    int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
    Integer dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

    String dayOfWeek = "";
    ArrayList<Integer> daysInOrderList = new ArrayList<>();
    ArrayList<String> daysOfWeek = new ArrayList<>(Arrays.asList("Sunday", "Monday", "Tuesday",
            "Wednesday", "Thursday", "Friday", "Saturday"));
    String toast = "Showing Calander For ";

    ArrayList<ArrayList<String>> dayArray = new ArrayList<ArrayList<String>>();
    ArrayList<String> dayEventNames = new ArrayList<String>();
    ArrayList<String> dayEventStart = new ArrayList<String>();
    ArrayList<String> dayEventEnd = new ArrayList<String>();

    Integer stabilizer = 1;




    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        new GetWeather((MainActivity) getActivity()).execute();

        rootview = inflater.inflate(R.layout.fragment_my_week, container, false);


        getDaysInOrder(currentDay);
        setDayNames();

        //setString();

        buttonPrev = (Button) rootview.findViewById(R.id.prev);
        buttonNext = (Button) rootview.findViewById(R.id.next);
        viewFlipper = (ViewFlipper) rootview.findViewById(R.id.viewflipper);
        viewFlipperOther = (ViewFlipper) rootview.findViewById(R.id.viewflipper);

        slide_in_left = AnimationUtils.loadAnimation(rootview.getContext(), android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(rootview.getContext(), android.R.anim.slide_out_right);

        slide_in_right = AnimationUtils.loadAnimation(rootview.getContext(), R.anim.slide_in_right);
        slide_out_left = AnimationUtils.loadAnimation(rootview.getContext(), R.anim.slide_out_left);

        RelativeLayout day1Layout = (RelativeLayout) rootview.findViewById(R.id.day1Layout);
        day1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayOfWeek = daysOfWeek.get(daysInOrderList.get(0)-1);
                Toast.makeText(rootview.getContext(), toast + dayOfWeek, Toast.LENGTH_SHORT).show();
                startDayView();
            }
        });

        RelativeLayout day2Layout = (RelativeLayout) rootview.findViewById(R.id.day2Layout);
        day2Layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dayOfWeek = daysOfWeek.get(daysInOrderList.get(1)-1);
                Toast.makeText(rootview.getContext(), toast + dayOfWeek, Toast.LENGTH_SHORT).show();
                startDayView();
            }
        });

        RelativeLayout day3Layout = (RelativeLayout) rootview.findViewById(R.id.day3Layout);
        day3Layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dayOfWeek = daysOfWeek.get(daysInOrderList.get(2)-1);
                Toast.makeText(rootview.getContext(), toast + dayOfWeek, Toast.LENGTH_SHORT).show();
                startDayView();
            }
        });

        RelativeLayout day4Layout = (RelativeLayout) rootview.findViewById(R.id.day4Layout);
        day4Layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dayOfWeek = daysOfWeek.get(daysInOrderList.get(3)-1);
                Toast.makeText(rootview.getContext(), toast + dayOfWeek, Toast.LENGTH_SHORT).show();
                startDayView();
            }
        });

        RelativeLayout day5Layout = (RelativeLayout) rootview.findViewById(R.id.day5Layout);
        day5Layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dayOfWeek = daysOfWeek.get(daysInOrderList.get(4)-1);
                Toast.makeText(rootview.getContext(), toast + dayOfWeek, Toast.LENGTH_SHORT).show();
                startDayView();
            }
        });

        RelativeLayout day6Layout = (RelativeLayout) rootview.findViewById(R.id.day6Layout);
        day6Layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dayOfWeek = daysOfWeek.get(daysInOrderList.get(5)-1);
                Toast.makeText(rootview.getContext(), toast + dayOfWeek, Toast.LENGTH_SHORT).show();
                startDayView();
            }
        });

        RelativeLayout day7Layout = (RelativeLayout) rootview.findViewById(R.id.day7Layout);
        day7Layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dayOfWeek = daysOfWeek.get(daysInOrderList.get(6)-1);
                Toast.makeText(rootview.getContext(), toast + dayOfWeek, Toast.LENGTH_SHORT).show();
                startDayView();
            }
        });

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1);
                stabilizer = stabilizer -1;
                if(stabilizer == 0){
                    calendar.add(Calendar.DATE, 7);
                    stabilizer = 7;
                }
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                Toast.makeText(rootview.getContext(), Integer.toString(dayOfMonth) + "----" + Integer.toString(stabilizer), Toast.LENGTH_SHORT).show();
                viewFlipperOther.setInAnimation(slide_in_left);
                viewFlipperOther.setOutAnimation(slide_out_right);
                viewFlipperOther.showPrevious();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1);
                stabilizer = stabilizer +1;
                if(stabilizer == 8){
                    calendar.add(Calendar.DATE, -7);
                    stabilizer = 1;
                }
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                Toast.makeText(rootview.getContext(), Integer.toString(dayOfMonth) + "----" + Integer.toString(stabilizer), Toast.LENGTH_SHORT).show();
                viewFlipper.setInAnimation(slide_in_right);
                viewFlipper.setOutAnimation(slide_out_left);
                viewFlipper.showNext();
            }
        });


        return rootview;
    }

    private void getDaysInOrder(int currentDay) {
        for (int i = currentDay; i<=Calendar.SATURDAY; i++) {
            daysInOrderList.add(i);
        }
        for (int i=Calendar.SUNDAY; i<currentDay; i++) {
            daysInOrderList.add(i);
        }
    }

    public void setDayNames(){
        TextView tv = (TextView) rootview.findViewById(R.id.day1Title);
        tv.setText(daysOfWeek.get(daysInOrderList.get(0)-1));
        //tv = (TextView) rootview.findViewById(R.id.day1NextClass);
        //tv.setText("Next: CALCULUS");

        tv = (TextView) rootview.findViewById(R.id.day2Title);
        tv.setText(daysOfWeek.get(daysInOrderList.get(1)-1));

        tv = (TextView) rootview.findViewById(R.id.day3Title);
        tv.setText(daysOfWeek.get(daysInOrderList.get(2)-1));

        tv = (TextView) rootview.findViewById(R.id.day4Title);
        tv.setText(daysOfWeek.get(daysInOrderList.get(3)-1));

        tv = (TextView) rootview.findViewById(R.id.day5Title);
        tv.setText(daysOfWeek.get(daysInOrderList.get(4)-1));

        tv = (TextView) rootview.findViewById(R.id.day6Title);
        tv.setText(daysOfWeek.get(daysInOrderList.get(5)-1));

        tv = (TextView) rootview.findViewById(R.id.day7Title);
        tv.setText(daysOfWeek.get(daysInOrderList.get(6)-1));

    }



    /*
    public void setString(){
        TextView tv = (TextView) rootview.findViewById(R.id.day1Title);
        tv.setText("MONDAY");
        tv = (TextView) rootview.findViewById(R.id.day1NextClass);
        tv.setText("Next: CALCULUS");

        tv = (TextView) rootview.findViewById(R.id.day2Title);
        tv.setText("TUESDAY");
        tv = (TextView) rootview.findViewById(R.id.day2NextClass);
        tv.setText("Next: DISCRETE SHHH");

        tv = (TextView) rootview.findViewById(R.id.day3Title);
        tv.setText("WEDNESDAY");
        tv = (TextView) rootview.findViewById(R.id.day3NextClass);
        tv.setText("Next: SPANISH");

        tv = (TextView) rootview.findViewById(R.id.day4Title);
        tv.setText("THURSDAY");
        tv = (TextView) rootview.findViewById(R.id.day4NextClass);
        tv.setText("Next: CS 275");

        tv = (TextView) rootview.findViewById(R.id.day5Title);
        tv.setText("FRIDAY");
        tv = (TextView) rootview.findViewById(R.id.day5NextClass);
        tv.setText("Next: RECESS");

        tv = (TextView) rootview.findViewById(R.id.day6Title);
        tv.setText("SATURDAY");
        tv = (TextView) rootview.findViewById(R.id.day6NextClass);
        tv.setText("Next: DATA STRUCTURES");

        tv = (TextView) rootview.findViewById(R.id.day7Title);
        tv.setText("SUNDAY");
        tv = (TextView) rootview.findViewById(R.id.day7NextClass);
        tv.setText("Next: PUBLC SPEAKING");

    }
    */

    public void startDayView(){

        Bundle prevBundle = this.getArguments();
        dayArray = (ArrayList<ArrayList<String>>) prevBundle.getSerializable("dayArray");
        Log.i("RIGHT AWAY: ", Integer.toString(dayArray.size()));



        Log.i("RETURNING", dayArray.get(0).get(0) + ":" + dayArray.get(0).get(1));

        for(Integer i = 0; i<dayArray.size();i++){
            String startReturn = dayArray.get(i).get(0);
            String endReturn = dayArray.get(i).get(1);
            String startParts[] = startReturn.split("T");
            String endParts[] = endReturn.split("T");
            String startDate = startParts[0];
            String startTime = startParts[1];
            String endTime = endParts[1];
            String startDateParts[] = startDate.split("-");
            Integer startDay = Integer.parseInt(startDateParts[2]);

            if(startDay == dayOfMonth){
                dayEventNames.add(dayArray.get(i).get(2));
                dayEventStart.add(startTime);
                dayEventEnd.add(endTime);
            }
        }

        if(dayEventNames.size() == 0){
            dayEventNames.add("Nothing Planned");
            dayEventStart.add("");
            dayEventEnd.add("");
        }

        Intent intent = new Intent(rootview.getContext(), DayViewActivity.class);
        intent.putStringArrayListExtra("Event Names", dayEventNames);
        intent.putStringArrayListExtra("Event Start Time", dayEventStart);
        intent.putStringArrayListExtra("Event End Time", dayEventEnd);
        startActivity(intent);

        dayEventNames.clear();

        //2015-09-01T01:00:00-04:00:

        //Intent intent = new Intent(rootview.getContext(), DayViewActivity.class);

        /*
        Bundle bundle = new Bundle();
        bundle.putString("Day", dayOfWeek);
        bundle.putSerializable("dayArray", dayArray);
        Log.i("IN_FRAGMENT: " , Integer.toString(dayArray.size()));
        intent.putExtras(bundle);
        */

        //startActivity(intent);
    }


    /*
    Bundle mBundle = new Bundle();

            Log.i("WASSUP", Integer.toString(dayArray.size()));

            mBundle.putSerializable("dayArray", dayArray);

            intent.putExtras(mBundle);
     */

}
