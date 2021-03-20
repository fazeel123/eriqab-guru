package com.madcatworld.e_riqabguru.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.madcatworld.e_riqabguru.R;

public class PembetulanActivity extends AppCompatActivity {

    private Button hantr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembetulan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Proses Pembetulan");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        hantr = (Button) findViewById(R.id.hantar);
        final EditText text = (EditText) findViewById(R.id.editText1);
        final ScrollView scroll = (ScrollView) findViewById(R.id.scroll);

        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    scroll.smoothScrollTo(0,text.getBottom()+5);
                }
            }
        });

        hantr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity1();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchActivity1() {
        Intent intent = new Intent(this, MainMenuActivity.class);

        startActivity(intent);
    }
}
