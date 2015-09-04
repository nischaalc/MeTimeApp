package com.example.roger.actuallymetime;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.Plus.PlusOptions;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GoogleLoginActivity extends AppCompatActivity implements
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{


    public JsonArray jarray;

    public String prevDayEvent = "";
    public String prevClassName = "";
    public String prevEventName = "";
    public String prevGoalName = "";

    public ArrayList<ArrayList<String>> dayArray = new ArrayList<ArrayList<String>>();
    public ArrayList<ArrayList<String>> classArray = new ArrayList<ArrayList<String>>();
    public ArrayList<ArrayList<String>> goalArray = new ArrayList<ArrayList<String>>();
    public ArrayList<ArrayList<String>> eventArray = new ArrayList<ArrayList<String>>();


    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;
    private GoogleApiClient mGoogleApiClient;

    public String token;

    private static final int RC_SIGN_IN = 9001;

    //Scope myScope = new Scope(Scopes.PROFILE);
    //String myScope = "https://www.googleapis.com/auth/calendar" + Scopes.PROFILE;
    Scope profileScope = new Scope(Scopes.PROFILE);
    Scope calendarScope = new Scope("https://www.googleapis.com/auth/calendar");

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Toast.makeText(this, "onConnectionFailed: " + connectionResult, Toast.LENGTH_SHORT).show();
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Toast.makeText(this, "Could not resolve Connection Result", Toast.LENGTH_SHORT).show();
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                //showErrorDialog(connectionResult);
                Toast.makeText(this, "This is an error dialog", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Show the signed-out UI
            //showSignedOutUI();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(profileScope)
                .addScope(calendarScope)
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.sign_in_button){
            onSignInClicked();
        }
    }

    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        Toast.makeText(this, "Signing In", Toast.LENGTH_SHORT).show();

        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a message to the user that we are signing in.
        //mStatusTextView.setText(R.string.signing_in);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "ActivityResult: " + requestCode + ":" + resultCode + ":" + data, Toast.LENGTH_SHORT).show();
        //Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        String usid = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient).toString();
        Toast.makeText(this, "OnConnected: " + usid, Toast.LENGTH_SHORT).show();;
        //Log.d(TAG, "onConnected:" + bundle);
        mShouldResolve = false;

        task.execute();

        // Show the signed-in UI
        //showSignedInUI();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost. The GoogleApiClient will automatically
        // attempt to re-connect. Any UI elements that depend on connection to Google APIs should
        // be hidden or disabled until onConnected is called again.
        Toast.makeText(this, "onConnectionSuspended: " + i, Toast.LENGTH_SHORT).show();
        //Log.w(TAG, "onConnectionSuspended:" + i);
    }

    AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>(){
        @Override
        protected String doInBackground(Void... params){
            token = null;
            try {
                token = GoogleAuthUtil.getToken(
                        GoogleLoginActivity.this,
                        Plus.AccountApi.getAccountName(mGoogleApiClient),
                        "oauth2:" + profileScope + " " + calendarScope);
            } catch (IOException transientEx) {
                // Network or server error, try later
                Log.e("***********", transientEx.toString());
            } catch (UserRecoverableAuthException e) {
                // Recover (with e.getIntent())
                Log.e("*********", e.toString());
                Intent recover = e.getIntent();
                //startActivityForResult(recover, REQUEST_CODE_TOKEN_AUTH);
            } catch (GoogleAuthException authEx) {
                // The call is not ever expected to succeed
                // assuming you have already verified that
                // Google Play services is installed.
                Log.e("********", authEx.toString());
            }
            return token;

        }

        @Override
        protected void onPostExecute(String token) {
            Log.i("DID THIS JUST WORK", "Access token retrieved:" + token);
            new get().execute();
        }
    };

    class get extends AsyncTask<ArrayList<String[]>,Void,ArrayList<String[]>>{
        private InputStreamReader isr;
        String locSite =    "https://metime.herokuapp.com/calEvents?token=" + token;

        @Override
        protected ArrayList<String[]> doInBackground(ArrayList<String[]>... params){
            try{
                URL url = new URL(locSite);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();
                request.connect();

                isr = new InputStreamReader((InputStream) request.getContent());
                JsonParser jp = new JsonParser();
                JsonObject jobj = jp.parse(isr).getAsJsonObject();
                jarray = jobj.get("items").getAsJsonArray();

                Log.i("YAYAYA", jobj.toString());


            }
            catch(Exception io){

            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String[]> al) {
            super.onPostExecute(al);

            for(int i = 0; i<jarray.size(); i++){
                String type = jarray.get(i).getAsJsonObject().get("description").getAsString();
                Log.i("TYPE: ", type);

                    ArrayList<String> dayEventInfo = new ArrayList<String>();

                    String dayStart = jarray.get(i).getAsJsonObject().get("start").getAsString();
                    String dayEnd = jarray.get(i).getAsJsonObject().get("end").getAsString();
                    String dayTitle = jarray.get(i).getAsJsonObject().get("title").getAsString();


                    if(!prevDayEvent.equals("")){
                        if(!prevDayEvent.equals(dayTitle)){
                            dayEventInfo.add(dayStart);
                            dayEventInfo.add(dayEnd);
                            dayEventInfo.add(dayTitle);

                            dayArray.add(dayEventInfo);

                            prevDayEvent = dayTitle;
                        }
                    }
                    else{
                        dayEventInfo.add(dayStart);
                        dayEventInfo.add(dayEnd);
                        dayEventInfo.add(dayTitle);

                        dayArray.add(dayEventInfo);

                        prevDayEvent = dayTitle;
                    }

                /*
                dayEventInfo.add(dayStart);
                dayEventInfo.add(dayEnd);
                dayEventInfo.add(dayTitle);

                dayArray.add(dayEventInfo);
                */

                    Log.i("IM ON A LOOP ", Integer.toString(i));

                if(type.equals("MeTime.Class")){
                    ArrayList<String> classInfo = new ArrayList<String>();
                    String start = jarray.get(i).getAsJsonObject().get("start").getAsString();
                    String end = jarray.get(i).getAsJsonObject().get("end").getAsString();
                    String title = jarray.get(i).getAsJsonObject().get("title").getAsString();

                    if(!prevClassName.equals("")){
                        if(!prevClassName.equals(title)){
                            classInfo.add(start);
                            classInfo.add(end);
                            classInfo.add(title);

                            classArray.add(classInfo);

                            prevClassName = title;
                        }
                    }
                    else{
                        classInfo.add(start);
                        classInfo.add(end);
                        classInfo.add(title);

                        classArray.add(classInfo);

                        prevClassName = title;
                    }


                    Log.i("CLASSARRAY", Integer.toString(classArray.size()));
                }
                else if(type.equals("MeTime.Goal")){
                    ArrayList<String> goalInfo = new ArrayList<String>();
                    String start = jarray.get(i).getAsJsonObject().get("start").getAsString();
                    String end = jarray.get(i).getAsJsonObject().get("end").getAsString();
                    String title = jarray.get(i).getAsJsonObject().get("title").getAsString();

                    if(!prevGoalName.equals("")){
                        if(!prevGoalName.equals(title)){
                            goalInfo.add(start);
                            goalInfo.add(end);
                            goalInfo.add(title);

                            goalArray.add(goalInfo);

                            prevGoalName = title;
                        }
                    }
                    else{
                        goalInfo.add(start);
                        goalInfo.add(end);
                        goalInfo.add(title);

                        goalArray.add(goalInfo);
                        prevGoalName = title;
                    }
                }
                else if(type.equals("MeTime.Event")){
                    ArrayList<String> eventInfo = new ArrayList<String>();
                    String start = jarray.get(i).getAsJsonObject().get("start").getAsString();
                    String end = jarray.get(i).getAsJsonObject().get("end").getAsString();
                    String title = jarray.get(i).getAsJsonObject().get("title").getAsString();

                    if(!prevEventName.equals("")){
                        if(!prevEventName.equals(title)){
                            eventInfo.add(start);
                            eventInfo.add(end);
                            eventInfo.add(title);

                            eventArray.add(eventInfo);

                            prevEventName = title;
                        }
                    }
                    else{
                        eventInfo.add(start);
                        eventInfo.add(end);
                        eventInfo.add(title);

                        eventArray.add(eventInfo);

                        prevEventName = title;
                    }


                }
            }
            ArrayList<String> tokenArray = new ArrayList<String>();
            tokenArray.add(token);

            Intent intent = new Intent(GoogleLoginActivity.this, MainActivity.class);
            Bundle mBundle = new Bundle();

            Log.i("WASSUP", token);

            mBundle.putSerializable("dayArray", dayArray);
            mBundle.putSerializable("classArray", classArray);
            mBundle.putSerializable("goalArray", goalArray);
            mBundle.putSerializable("eventArray", eventArray);
            mBundle.putSerializable("tokenArray", tokenArray);

            intent.putExtras(mBundle);
            startActivity(intent);

        }

    }


}