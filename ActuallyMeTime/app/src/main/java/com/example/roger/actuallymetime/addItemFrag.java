package com.example.roger.actuallymetime;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Roger on 8/26/2015.
 */
public class addItemFrag extends Fragment{

    View rootview;

    Button buttonClass, buttonEvent, buttonGoal, buttonAddClassConfirm, buttonAddEventConfirm, buttonAddGoalConfirm;
    ViewFlipper viewFlipper;
    Animation slide_in_left, slide_out_right;

    Integer cView = 1;


    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.fragment_add_item, container, false);

        viewFlipper = (ViewFlipper) rootview.findViewById(R.id.addItemViewflipper);

        buttonClass = (Button) rootview.findViewById(R.id.btnAddClassSwipe);
        buttonEvent = (Button) rootview.findViewById(R.id.btnAddEventSwipe);
        buttonGoal = (Button) rootview.findViewById(R.id.btnAddGoalSwipe);

        buttonAddClassConfirm = (Button) rootview.findViewById(R.id.btnAddClass);
        buttonAddEventConfirm = (Button) rootview.findViewById(R.id.btnAddEvent);
        buttonAddGoalConfirm = (Button) rootview.findViewById(R.id.btnAddGoal);

        buttonClass.setEnabled(false);

        slide_in_left = AnimationUtils.loadAnimation(rootview.getContext(), android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(rootview.getContext(), android.R.anim.slide_out_right);

        viewFlipper.setInAnimation(slide_in_left);
        viewFlipper.setOutAnimation(slide_out_right);


        buttonClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cView == 2) {
                    buttonEvent.setEnabled(true);
                    viewFlipper.showPrevious();
                }
                if (cView == 3) {
                    buttonGoal.setEnabled(true);
                    viewFlipper.showNext();
                }
                cView = 1;
                buttonClass.setEnabled(false);
            }
        });

        buttonEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cView == 1) {
                    buttonClass.setEnabled(true);
                    viewFlipper.showNext();
                }
                if (cView == 3) {
                    buttonGoal.setEnabled(true);
                    viewFlipper.showPrevious();
                }
                cView = 2;
                buttonEvent.setEnabled(false);
            }
        });

        buttonGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cView == 1) {
                    buttonClass.setEnabled(true);
                    viewFlipper.showPrevious();
                }
                if (cView == 2) {
                    buttonEvent.setEnabled(true);
                    viewFlipper.showNext();
                }
                cView = 3;
                buttonGoal.setEnabled(false);
            }
        });

        buttonAddClassConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //Declare Views
                TextView txtName, txtStart, txtEnd;
                String name, start, end;
                CheckBox cbMo, cbTu, cbWe, cbTh, cbFr;
                Boolean monday, tuesday, wednesday, thursday, friday;
                Double rating;
                RatingBar ratingBar;

                monday = false;
                tuesday = false;
                wednesday = false;
                thursday = false;
                friday = false;

                rating = 0.0;

                //Set Views
                txtName = (TextView) rootview.findViewById(R.id.txtClassName);
                txtStart = (TextView) rootview.findViewById(R.id.txtStartClass);
                txtEnd = (TextView) rootview.findViewById(R.id.txtEndClass);

                cbMo = (CheckBox) rootview.findViewById(R.id.cbMondayClass);
                cbTu = (CheckBox) rootview.findViewById(R.id.cbTuesdayClass);
                cbWe = (CheckBox) rootview.findViewById(R.id.cbWednesdayClass);
                cbTh = (CheckBox) rootview.findViewById(R.id.cbThursdayClass);
                cbFr = (CheckBox) rootview.findViewById(R.id.cbFridayClass);

                ratingBar = (RatingBar) rootview.findViewById(R.id.classRatingBar);

                //Value of Text Items
                name = txtName.getText().toString();
                start = txtName.getText().toString();
                end = txtName.getText().toString();

                //Value of Check Boxes
                //Note, because multiple can be checked at once
                //  they should be in seperate if statements
                if(cbMo.isChecked() == true){
                    monday = true;
                }
                if(cbTu.isChecked() == true){
                    tuesday = true;
                }
                if(cbWe.isChecked() == true){
                    wednesday = true;
                }
                if(cbTh.isChecked() == true){
                    thursday = true;
                }
                if(cbFr.isChecked() == true){
                    friday = true;
                }

                //Convert rating(Float) to a string, for easy parse into
                //  Double withoug loss of accuracy
                rating = Double.parseDouble(Float.toString(ratingBar.getRating()));

                Toast.makeText(rootview.getContext(), "Add Class Toast with rating: " + rating, Toast.LENGTH_SHORT).show();
            }
        });

        buttonAddEventConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Declare Views
                TextView txtName, txtStart, txtEnd;
                String name, start, end;
                RadioButton rbMo, rbTu, rbWe, rbTh, rbFr, rbSa, rbSu;
                CheckBox cbOutside;
                Boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday, isOutside;
                Double rating;
                RatingBar ratingBar;

                monday = false;
                tuesday = false;
                wednesday = false;
                thursday = false;
                friday = false;
                saturday = false;
                sunday = false;
                isOutside = false;

                rating = 0.0;

                //Set Views
                txtName = (TextView) rootview.findViewById(R.id.txtEventName);
                txtStart = (TextView) rootview.findViewById(R.id.txtStartEvent);
                txtEnd = (TextView) rootview.findViewById(R.id.txtEndEvent);

                cbOutside = (CheckBox) rootview.findViewById(R.id.cbEventOutside);

                /*
                rbMo = (RadioButton) rootview.findViewById(R.id.rbMondayEvent);
                rbTu = (RadioButton) rootview.findViewById(R.id.rbTuesdayEvent);
                rbWe = (RadioButton) rootview.findViewById(R.id.rbWednesdayEvent);
                rbTh = (RadioButton) rootview.findViewById(R.id.rbThursdayEVent);
                rbFr = (RadioButton) rootview.findViewById(R.id.rbFridayEvent);
                rbSa = (RadioButton) rootview.findViewById(R.id.rbSaturdayEvent);
                rbSu = (RadioButton) rootview.findViewById(R.id.rbSundayEvent);
                */

                ratingBar = (RatingBar) rootview.findViewById(R.id.eventRatingBar);

                //Set Text Values
                name = txtName.getText().toString();
                start = txtStart.getText().toString();
                end = txtEnd.getText().toString();

                //Set value of boolean isOutside
                if(cbOutside.isChecked()==true){
                    isOutside = true;
                }
                else{
                    isOutside = false;
                }

                //Value of radio buttons
                //Note that only one radio button in the radio group
                //  can be selected at a time, so it is acceptable to use
                //  one series of if else

                /*
                if(rbMo.isChecked()==true){
                    monday = true;
                }
                else if (rbTu.isChecked() == true) {
                    tuesday = true;
                }
                else if(rbWe.isChecked() == true){
                    wednesday = true;
                }
                else if(rbTh.isChecked()==true){
                    thursday = true;
                }
                else if(rbFr.isChecked()==true){
                    friday = true;
                }
                else if(rbSa.isChecked()==true){
                    saturday = true;
                }
                else if(rbSu.isChecked()==true){
                    sunday = true;
                }
                */

                //Convert rating(Float) to a string, for easy parse into
                //  Double withoug loss of accuracy
                rating = Double.parseDouble(Float.toString(ratingBar.getRating()));


                Toast.makeText(rootview.getContext(), "Add Event Toast with a rating: " + rating, Toast.LENGTH_SHORT).show();
            }
        });

        buttonAddGoalConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                TextView txtName;
                RatingBar ratingBar;
                String name;
                Double rating;

                txtName = (TextView) rootview.findViewById(R.id.txtGoalName);
                ratingBar = (RatingBar) rootview.findViewById(R.id.goalRatingBar);
                name = txtName.getText().toString();
                rating = Double.parseDouble(Float.toString(ratingBar.getRating()));


                Toast.makeText(rootview.getContext(), "Add Goal Toast with a rating: " + rating, Toast.LENGTH_SHORT).show();
            }
        });


        return rootview;
    }


}


