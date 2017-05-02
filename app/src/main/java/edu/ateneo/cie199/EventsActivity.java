package edu.ateneo.cie199.tara;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;

public class EventsActivity extends AppCompatActivity {
    private ArrayList<Event> mEventList = new ArrayList<>();
    private ArrayAdapter<Event> mAdapter = null;
    private SyncEventsTask mSyncEventsTask = null;
    private String countEvents = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        mSyncEventsTask = new SyncEventsTask();
        mSyncEventsTask.execute();

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ListView lstEvents = (ListView) findViewById(R.id.lst_events);

        lstEvents.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        Log.d("Count Events: ", countEvents);

        lstEvents.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent launchIntent = new Intent(EventsActivity.this, EventDetailActivity.class);


                        int targetEventIndex = position;

                        launchIntent.putExtra("EVENT_INDEX", targetEventIndex);
                        startActivity(launchIntent);
                    }
                }
        );


        Button btnCreateEvent = (Button) findViewById(R.id.btn_CreateEvent);
        btnCreateEvent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent launchCreateEventIntent= new Intent(EventsActivity.this, CreateEventActivity.class);
                        launchCreateEventIntent.putExtra("INDEX", countEvents);
                        startActivity(launchCreateEventIntent);
                    }
                }
        );

        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent launchCreateEventIntent= new Intent(EventsActivity.this, Category.class);
                        startActivity(launchCreateEventIntent);
                    }
                }
        );

        Button btnRefresh = (Button) findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent launchCreateEventIntent= new Intent(EventsActivity.this, EventsActivity.class);
                        startActivity(launchCreateEventIntent);
                    }
                }
        );
    }


    private class SyncEventsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            TaraApp app = (TaraApp) getApplication();
            ArrayList<Event> syncedEvents = app.syncEvent();


            if (syncedEvents.size() > 0) {
                countEvents = Integer.toString(syncedEvents.size());

                mEventList.clear();
                mEventList.addAll(syncedEvents);
            }
            else {
                countEvents = "0";
            }

            for(int i=0; i<mEventList.size(); i++) {
                Log.i("Array string: ", mEventList.get(i).toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            mAdapter.notifyDataSetChanged();
            mAdapter.clear();
            mAdapter.addAll(mEventList);
            mSyncEventsTask = null;

            super.onPostExecute(aVoid);
            return;
        }
    }
}
