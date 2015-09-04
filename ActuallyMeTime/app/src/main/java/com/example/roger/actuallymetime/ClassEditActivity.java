package com.example.roger.actuallymetime;

import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class ClassEditActivity extends ActionBarActivity {

    //New

    ArrayList<String> tokenArray = new ArrayList<String>();

    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;
    private GoogleApiClient mGoogleApiClient;

    public String token;

    private static final int RC_SIGN_IN = 9001;

    //Scope myScope = new Scope(Scopes.PROFILE);
    //String myScope = "https://www.googleapis.com/auth/calendar" + Scopes.PROFILE;
    Scope profileScope = new Scope(Scopes.PROFILE);
    Scope calendarScope = new Scope("https://www.googleapis.com/auth/calendar");


    //end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_edit);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        tokenArray = (ArrayList<String>) bundle.getSerializable("tokenArray");
        //Log.i("Final", tokenArray.get(0));

        //Toast.makeText(this, tokenArray.get(0), Toast.LENGTH_SHORT).show();

        //*********************
        //NewCode
        final TextView className = (TextView) findViewById(R.id.txtNewClassName);
        final TextView startTime = (TextView) findViewById(R.id.txtNewStartClass);
        final TextView endTime = (TextView) findViewById(R.id.txtNewEndClass);
        final RatingBar rating = (RatingBar) findViewById(R.id.newClassRatingBar);
        final CheckBox monday = (CheckBox) findViewById(R.id.cbNewMondayClass);
        final CheckBox tuesday = (CheckBox) findViewById(R.id.cbNewTuesdayClass);
        final CheckBox wednesday = (CheckBox) findViewById(R.id.cbNewWednesdayClass);
        final CheckBox thursday = (CheckBox) findViewById(R.id.cbNewThursdayClass);
        final CheckBox friday = (CheckBox) findViewById(R.id.cbNewFridayClass);

        Button addClass = (Button) findViewById(R.id.btnNewAddClass);
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = className.getText().toString();
                final String stime = startTime.getText().toString();
                final String etime = endTime.getText().toString();
                final Float pri = rating.getRating();

                new Thread( new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ArrayList<String> allChecked = new ArrayList<>();

                            if(monday.isChecked()){
                                allChecked.add("Monday");
                            }
                            if(tuesday.isChecked()){
                                allChecked.add("Tuesday");
                            }
                            if(wednesday.isChecked()){
                                allChecked.add("Wednesday");
                            }
                            if(thursday.isChecked()){
                                allChecked.add("Thursday");
                            }
                            if(friday.isChecked()){
                                allChecked.add("Friday");
                            }
                            String dtime = "";
                            if(allChecked.size() > 1) {
                                for (String i : allChecked) {
                                    dtime += (i + ",");
                                }
                                dtime = dtime.substring(0, dtime.length()-1);
                            }
                            else if(allChecked.size() == 1) {
                                dtime = allChecked.get(0);
                            }

                            JSONObject json = new JSONObject();
                            json.put("dtime", dtime);
                            json.put("etime", etime);
                            json.put("stime", stime);
                            json.put("title",title);
                            json.put("pri",Float.toString(pri));
                            json.put("token",tokenArray.get(0));
                            json.put("itype","Class");



                            Log.i("*******", json.toString());

                            String params = "dtime=" + dtime + "&etime=" + etime +
                                    "&stime=" + stime + "&title=" + title + "&pri=" + pri +
                                    "&token=" + tokenArray.get(0) + "&itype=Class";

                            /*
                            String params = "dtime=" + dtime + "&etime=" + etime + "&stime=" +
                                    stime + "&title=" + title + "&pri=" + pri +
                                    "&token=ya29.4wHjpKEeESj0XdIET0dDmr104YRJiSej8EESR-OeBps2aiNSuTomjcoXMRvHVnm2qQBy-Q" +
                                    "&itype=Class";
                                    */

                            URL url = new URL("https://metime.herokuapp.com/addItem");
                            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                            Log.i("Made", "it this far");
                            //connection.setRequestProperty("Cookie", cookie);
                            //Set to POST
                            connection.setDoOutput(true);
                            connection.setRequestMethod("POST");
                            connection.setReadTimeout(10000);
                            Writer writer = new OutputStreamWriter(connection.getOutputStream());
                            writer.write(json.toString());
                            //writer.flush();
                            writer.close();

                            String line = "";
                            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                            BufferedReader reader = new BufferedReader(isr);
                            StringBuilder sb = new StringBuilder();

                            while ((line = reader.readLine()) != null)
                            {
                                sb.append(line + "\n");
                            }
                            String response = null;
                            // Response from server after login process will be stored in response variable.
                            response = sb.toString();
                            // You can perform UI operations here
                            Log.i("Response:", response);
                            isr.close();
                            reader.close();
                            Log.i("---------------", "DONE THE POST");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Log.i("**************", e.toString());
                        }
                    }
                }).start();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_class_edit, menu);
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
