package edu.ateneo.cie199.tara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TaraApp app = (TaraApp) getApplication();

        TextView txvName = (TextView) findViewById(R.id.txv_name);
        TextView txvUsername = (TextView) findViewById(R.id.txv_username);
        TextView txvCompany = (TextView) findViewById(R.id.txv_company);
        TextView txvContact = (TextView) findViewById(R.id.txv_contact);

        txvUsername.setText ("Name: " + app.getAppUserUsername());
        txvName.setText ("Username: " + app.getAppUserName());
        txvCompany.setText ("Company: " + app.getAppUserCompany());
        txvContact.setText ("Contact: " + app.getAppUserContact());
    }
}
