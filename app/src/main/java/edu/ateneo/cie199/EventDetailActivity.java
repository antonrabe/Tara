package edu.ateneo.cie199.tara;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class EventDetailActivity extends AppCompatActivity {
    //private int eventIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Intent receivedIntent = getIntent();
        int eventIndex = receivedIntent.getIntExtra("EVENT_INDEX", 0);

        TaraApp app = (TaraApp) getApplication();
        ArrayList<Event> eventList = app.getEventList();

        Event selectedEvent = null;


        for(int i=0; i<eventList.size(); i++) {
            int tempEvent = eventList.get(i).getEventIndex();
            if( eventIndex == tempEvent ) {
                selectedEvent = eventList.get(i);
                break;
            }
        }

        if(selectedEvent != null)
            loadEvent(selectedEvent);

        final String eventHost = selectedEvent.getEventUser();
        Button btnTara = (Button) findViewById(R.id.btn_Tara);
        Log.d("eventhost: ", eventHost );
        Log.d("sharedpref: ", app.getAppUserUsername() );
        if (eventHost.equals(app.getAppUserUsername()))
        {
            btnTara.setEnabled(false);
        }
        btnTara.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TaraApp app = (TaraApp)getApplication();
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(EventDetailActivity.this)
                                        .setSmallIcon(R.drawable.logo)
                                        .setContentTitle("Tara")
                                        .setContentText("You have successfully joined " +  eventHost + "'s event.");

                        int mNotificationId = 001;
                        NotificationManager mNotifyMgr =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.notify(mNotificationId, mBuilder.build());
                    }
                }
        );

        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent launchCreateEventIntent= new Intent(EventDetailActivity.this, EventsActivity.class);
                        startActivity(launchCreateEventIntent);
                    }
                }
        );
}


    private void loadEvent(Event newEvent) {
        TaraApp app = (TaraApp) getApplication();
        TextView txvline1 = (TextView) findViewById(R.id.txv_line1);
        TextView txvline2 = (TextView) findViewById(R.id.txv_line2);
        TextView txvline3 = (TextView) findViewById(R.id.txv_line3);
        TextView txvUser = (TextView) findViewById(R.id.txv_User);

        txvline1.setText( "Let's " + newEvent.getEventCategory() +
                            " in " + newEvent.getEventVenue() +
                            " at " + newEvent.getEventTime() );

        txvline2.setText( "Contact me at: " + app.getAppUserContact());
        txvline3.setText( "I'm looking for " + newEvent.getNumPeople() + " companion/s");
        txvUser.setText("Posted by: " + newEvent.getEventUser());
    }
}
