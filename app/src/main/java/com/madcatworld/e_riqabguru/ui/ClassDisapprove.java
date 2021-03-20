package com.madcatworld.e_riqabguru.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.madcatworld.e_riqabguru.R;
import com.madcatworld.e_riqabguru.utils.RestAPI;
import com.madcatworld.e_riqabguru.utils.SharedPrefsManager;
import com.madcatworld.e_riqabguru.utils.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class ClassDisapprove extends AppCompatActivity {

    private EditText reason;
    private Button submit;
    private String mReason;
    private int classId;
    private String storeToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disapprove_class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Pengesahan Rekod Baru - Pemantau");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        reason = (EditText) findViewById(R.id.details);
        submit = (Button) findViewById(R.id.submit);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        classId = getIntent().getIntExtra("id", 0);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReason = reason.getText().toString().trim();
                DisapproveFormData();
            }
        });
    }

    // Method for Dispprove PUT Data on Form Fields
    private void DisapproveFormData() {

        String URL = RestAPI.DisapproveClass + classId;

        StringRequest request = new StringRequest(Request.Method.POST, URL, detailDispproveListener, errorDispproveListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + storeToken);
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("comment", mReason);
                return params;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<String> detailDispproveListener = response ->
    {
        Log.i("VOLLEY", "response" + response);
        Intent intent = new Intent(ClassDisapprove.this, ClassListEditUpdated.class);
        startActivity(intent);
    };

    public Response.ErrorListener errorDispproveListener = error -> {
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
        Log.e("VOLLEY", "" + error.getMessage());
        Toast.makeText(this, String.valueOf(error.getMessage()), Toast.LENGTH_LONG).show();
    };
    // End Method for Dispprove PUT Data on Form Fields

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(ClassDisapprove.this, ClassListEdit.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
