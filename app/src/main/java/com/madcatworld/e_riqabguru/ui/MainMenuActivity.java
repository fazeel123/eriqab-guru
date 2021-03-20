package com.madcatworld.e_riqabguru.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.madcatworld.e_riqabguru.R;
import com.madcatworld.e_riqabguru.utils.SharedPrefsManager;

public class MainMenuActivity extends AppCompatActivity {

    private CardView cardView1;
    private CardView cardView2;
    String storeToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utama);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Utama");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        cardView1 = (CardView) findViewById(R.id.layout_cardView1);
        cardView2 = (CardView) findViewById(R.id.layout_cardView2);


        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity1();
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity2();
            }
        });

        // Get Token from LoginActivity
        SharedPrefsManager info = new SharedPrefsManager(this);
        //storeToken = getIntent().getStringExtra("TOKEN");
        //Toast.makeText(getApplicationContext(), "Token: " + info.getToken(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchActivity1() {
        Intent intent = new Intent(this, ClassListEdit.class);
        //intent.putExtra("TOKEN", storeToken);
        startActivity(intent);
    }

    private void launchActivity2() {
        Intent intent = new Intent(this, ClassDetails.class);
        //intent.putExtra("TOKEN", storeToken);
        startActivity(intent);
    }
}
