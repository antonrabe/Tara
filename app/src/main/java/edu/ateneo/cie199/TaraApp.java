package edu.ateneo.cie199.tara;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Angelo on 4/28/2017.
 */

public class TaraApp extends Application {
    private ArrayList<Event> mEventList = new ArrayList<>();
    private ArrayList<User> mUserList = new ArrayList<>();

    private String mServer = "http://192.168.1.143:8000/";

    public String getServerUrl() {
        return mServer;
    }

    public boolean setServer(String serverUrl) {
        mServer = serverUrl;
        return true;
    }

    private HttpClient mHttpClient = new DefaultHttpClient();

    public boolean postNewEvent(int index, String venue, String time, String category, int numPeople, String user) {
        ArrayList<NameValuePair> postParams = new ArrayList<>();

        postParams.add(new BasicNameValuePair("index", Integer.toString(index)));
        postParams.add(new BasicNameValuePair("venue", venue));
        postParams.add(new BasicNameValuePair("time", time));
        postParams.add(new BasicNameValuePair("category", category));
        postParams.add(new BasicNameValuePair("numPeople", Integer.toString(numPeople)));
        postParams.add(new BasicNameValuePair("user", user));

        HttpPost httpost = new HttpPost(mServer + "upload_events");
        try {
            httpost.setEntity( new UrlEncodedFormEntity(postParams) );
            HttpResponse response = mHttpClient.execute(httpost);
        } catch(Exception e) {
            Log.e("TaraApp", "Exception occurred: " +e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<Event> syncEvent() {
        String contents = "";
        HttpGet request = new HttpGet(mServer + "get_events");

        try {
            HttpResponse response = mHttpClient.execute(request);

            contents = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            Log.e("TaraApp", "Exception occurred: " + e.getMessage());
            contents = "";
        }

        return parseEvents(contents);
    }

    private ArrayList<Event> parseEvents(String respContents) {
        Log.i("TaraApp", respContents);
        mEventList.clear();
        String events[] = respContents.split(";");

        for (int i = 0; i < events.length; i++) {
            String eventPart[] = events[i].split("\\|");

            if (eventPart.length != 6) {
                continue;
            }

            int index = Integer.parseInt(eventPart[0]);
            String venue = eventPart[1];
            String time = eventPart[2];
            String category = eventPart[3];
            int numPeople = Integer.parseInt(eventPart[4]);
            String user = eventPart[5];

            mEventList.add(new Event(index, venue, time, category, numPeople, user));
        }

        for (Event event : mEventList) {
            Log.d("TaraApp", "Events --> " + event.toString());
        }
        return mEventList;
    }

    public ArrayList<Event> getEventList() {return mEventList;}

    public boolean registerNewUser(String username, String fullname, String company, String contact, String password) {
        ArrayList<NameValuePair> postParamsRegister = new ArrayList<>();

        postParamsRegister.add(new BasicNameValuePair("username", username));
        postParamsRegister.add(new BasicNameValuePair("fullname", fullname));
        postParamsRegister.add(new BasicNameValuePair("company", company));
        postParamsRegister.add(new BasicNameValuePair("contact", contact));
        postParamsRegister.add(new BasicNameValuePair("password", password));

        HttpPost httpost = new HttpPost(mServer + "upload_users");
        try {
            httpost.setEntity( new UrlEncodedFormEntity(postParamsRegister) );
            HttpResponse response = mHttpClient.execute(httpost);
        } catch(Exception e) {
            Log.e("TaraApp", "Exception occurred: " +e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<User> syncUser() {
        String contents = "";

        HttpGet request = new HttpGet(mServer + "get_users");

        try {
            HttpResponse response2 = mHttpClient.execute(request);
            contents = EntityUtils.toString(response2.getEntity());
        } catch (Exception e) {
            Log.e("TaraApp", "Exception occurred: " + e.getMessage());
            contents = "";
        }

        return parseUsers(contents);
    }

    private ArrayList<User> parseUsers(String respContents) {
        Log.i("TaraApp", respContents);
        mUserList.clear();
        String users[] = respContents.split(";");

        for (int i = 0; i < users.length; i++) {
            String userPart[] = users[i].split("\\|");

            if (userPart.length != 5) {
                continue;
            }

            String username = userPart[0];
            String name = userPart[1];
            String company = userPart[2];
            String contact = userPart[3];
            String password = userPart[4];

            mUserList.add(new User(username, name, company, contact, password));
        }

        for (User user: mUserList) {
            Log.d("TaraApp", "Users --> " + user.toString());
        }

        return mUserList;
    }

    public ArrayList<User> getUserList() {return mUserList;}

    public void saveUserData(String name, String username, String password, String company, String contact) {

        SharedPreferences prefs = getSharedPreferences("edu.ateneo.cie199.tara", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = prefs.edit();

        edt.putString("NAME", name);
        edt.putString("USERNAME", username);
        edt.putString("PASSWORD", password);
        edt.putString("COMPANY", company);
        edt.putString("CONTACT", contact);

        edt.commit();
        return;
    }

    public String getAppUserName() {
        SharedPreferences prefs = getSharedPreferences("edu.ateneo.cie199.tara", Context.MODE_PRIVATE);
        return prefs.getString("NAME", "");
    }

    public String getAppUserUsername() {
        SharedPreferences prefs = getSharedPreferences("edu.ateneo.cie199.tara", Context.MODE_PRIVATE);
        return prefs.getString("USERNAME", "");
    }

    public String getAppUserPassword() {
        SharedPreferences prefs = getSharedPreferences("edu.ateneo.cie199.tara", Context.MODE_PRIVATE);
        return prefs.getString("PASSWORD", "");
    }

    public String getAppUserCompany() {
        SharedPreferences prefs = getSharedPreferences("edu.ateneo.cie199.tara", Context.MODE_PRIVATE);
        return prefs.getString("COMPANY", "");
    }

    public String getAppUserContact() {
        SharedPreferences prefs = getSharedPreferences("edu.ateneo.cie199.tara", Context.MODE_PRIVATE);
        return prefs.getString("CONTACT", "");
    }
}
