package edu.ateneo.cie199.tara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Category extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent receiveIntent = getIntent();
        String username = receiveIntent.getStringExtra("USERNAME");

        ImageView eat = (ImageView)findViewById(R.id.eat);
        ImageView watch = (ImageView)findViewById(R.id.watch);
        ImageView play = (ImageView)findViewById(R.id.play);

        final TextView profile = (TextView) findViewById(R.id.profile);
        profile.setText("Welcome back, " + username + "!");


        profile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent launchIntent = new Intent(Category.this, ProfileActivity.class);
                        launchIntent.putExtra("USERNAME", profile.getText().toString());
                        startActivity (launchIntent);
                    }
                }
        );

        eat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent launchIntent = new Intent(Category.this, EventsActivity.class);
                        startActivity (launchIntent);
                    }
                }
        );

        watch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent launchIntent = new Intent(Category.this, EventsActivity.class);
                        startActivity (launchIntent);
                    }
                }
        );
        play.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent launchIntent = new Intent(Category.this, EventsActivity.class);
                        startActivity (launchIntent);
                    }
                }
        );

    }
}

