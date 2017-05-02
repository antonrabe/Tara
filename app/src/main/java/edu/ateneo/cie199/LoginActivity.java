package edu.ateneo.cie199.tara;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private static final int MENU_OPTION_SET_SERVER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TaraApp app = (TaraApp) getApplication();
                        String validUsername = app.getAppUserUsername();
                        String validPassword = app.getAppUserPassword();

                        EditText edtUsername = (EditText) findViewById(R.id.edt_username);
                        EditText edtPassword = (EditText) findViewById(R.id.edt_password);

                        String enteredUsername = edtUsername.getText().toString();
                        String enteredPassword = edtPassword.getText().toString();

                        if (enteredUsername.equals("")) {
                            Toast.makeText(getApplicationContext(), "Name is empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (enteredUsername.equals(validUsername) &&
                                enteredPassword.equals(validPassword)) {

                            Intent launchIntent = new Intent(LoginActivity.this,
                                    Category.class);

                            launchIntent.putExtra("USERNAME", edtUsername.getText().toString());
                            launchIntent.putExtra("PASSWORD", edtPassword.getText().toString());

                            startActivity(launchIntent);
                        } else if (enteredUsername == validUsername &&
                                enteredPassword != validPassword) {
                            Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid username", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        return;
                    }
                }
        );

        Button btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        EditText edtUsername = (EditText) findViewById(R.id.edt_username);
                        EditText edtPassword = (EditText) findViewById(R.id.edt_password);

                        Intent launchIntent = new Intent(LoginActivity.this, RegisterActivity.class);

                        launchIntent.putExtra("USERNAME", edtUsername.getText().toString());
                        launchIntent.putExtra("PASSWORD", edtPassword.getText().toString());

                        startActivity(launchIntent);

                        return;
                    }
                }
        );
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