package edu.ateneo.cie199.tara;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateEventActivity extends AppCompatActivity {
    private UploadEventTask mUploadEventTask  = null;
    private static final int MENU_OPTION_SET_SERVER = 0;
    private String idx = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Intent receiveIntent = getIntent();
        idx = receiveIntent.getStringExtra("INDEX");

        final EditText edtTime = (EditText) findViewById(R.id.edt_EventTime);
        final EditText edtVenue = (EditText) findViewById(R.id.edt_EventVenue);
        final Spinner edtCategory = (Spinner) findViewById(R.id.edt_Category);
        final EditText edtNumPeople = (EditText) findViewById(R.id.edt_NumPeople);

        Button btnPost = (Button) findViewById(R.id.btn_post);
        btnPost.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mUploadEventTask != null) {
                            Toast.makeText(CreateEventActivity.this, "Already posting event.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        TaraApp app = (TaraApp) getApplication();
                        String user = app.getAppUserUsername();

                        String ven = edtVenue.getText().toString();
                        String tme = edtTime.getText().toString();
                        String ctg = edtCategory.getSelectedItem().toString();
                        String ppl = edtNumPeople.getText().toString();

                        mUploadEventTask = new UploadEventTask();
                        mUploadEventTask.execute(idx, ven, tme, ctg, ppl, user);

                        Intent launchEventsIntent = new Intent(CreateEventActivity.this, EventsActivity.class);
                        startActivity(launchEventsIntent);
                    }
                }
        );

        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent launchEventsIntent = new Intent(CreateEventActivity.this, EventsActivity.class);
                        startActivity(launchEventsIntent);
                    }
                }
        );
    }

    private class UploadEventTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            TaraApp app = (TaraApp) getApplication();
            return app.postNewEvent(Integer.parseInt(params[0]), params[1], params[2], params[3], Integer.parseInt(params[4]), params[5]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mUploadEventTask = null;

            if (result == true) {
                if (mUploadEventTask != null) {
                    Toast.makeText(CreateEventActivity.this,
                            "Already uploading",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                /* Display a Toast if sending was unsuccessful */
                Toast.makeText(CreateEventActivity.this,
                        "Sending failed",
                        Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(result);
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        menu.add(0, MENU_OPTION_SET_SERVER, 0, "Set Chat Server");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_OPTION_SET_SERVER:
                displaySetServerDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displaySetServerDialog() {
        final TaraApp app = (TaraApp) getApplication();
        final EditText edtServer = new EditText(this);
        edtServer.setText(app.getServerUrl());

        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);

        dlgBuilder.setTitle("Set Server")
                .setMessage("Enter your chat server URL:")
                .setView(edtServer)
                .setPositiveButton("OK",

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String server = edtServer.getText().toString();
                                if (server.equals("") == false) {

                                    app.setServer(server);
                                }

                                dialog.cancel();
                                return;
                            }
                        })
                .setNegativeButton("Cancel",

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                return;
                            }
                        });

        dlgBuilder.show();
        return;
    }

}
