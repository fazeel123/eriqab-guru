package com.madcatworld.e_riqabguru.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.madcatworld.e_riqabguru.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.madcatworld.e_riqabguru.adapters.ClassRatingEditAdapter;
import com.madcatworld.e_riqabguru.model.ClassRatingEditModel;
import com.madcatworld.e_riqabguru.utils.RestAPI;
import com.madcatworld.e_riqabguru.utils.SharedPrefsManager;
import com.madcatworld.e_riqabguru.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClassRatingEdit extends AppCompatActivity {

    private Button tidak_lulus;
    private Button sahkan;
    private String storeToken;
    private RecyclerView recyclerView;
    private ClassRatingEditAdapter adapter;
    private List<ClassRatingEditModel> ratingLists;
    private ClassRatingEditModel ratingList;
    private int classId;
    private String time;
    private TextView rateVal;

    private int c_id;
    private int c_rate;

    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengesahan_rekod);
        Log.d(TAG, "onCreate: called.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Pengesahan Rekod Baru - Pemantau");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        tidak_lulus = (Button) findViewById(R.id.tidak);
        sahkan = (Button) findViewById(R.id.sahkan);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        classId = getIntent().getIntExtra("id", 0);

        ratingLists = new ArrayList<>();

        Date takeTime = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:00", Locale.ENGLISH);
        time = dateFormat.format(takeTime);

        ratingList = new ClassRatingEditModel();
        ratingList.setTime(time);

        setupMenuRecyclerView();
        fetchDetails();

        tidak_lulus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity();
            }
        });

        sahkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApproveFormData();
//                launchActivity2();
            }
        });
    }

    private void setupMenuRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ClassRatingEditAdapter(this, ratingLists);
        recyclerView.setAdapter(adapter);
    }

    private void fetchDetails() {

        String URL = RestAPI.ViewRate + classId;

        StringRequest request = new StringRequest(Request.Method.GET, URL, detailListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + storeToken);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<String> detailListener = response ->
    {
        Log.i("VOLLEY", "response" + response);
        try{
            JSONObject responseObj = new JSONObject(response);
            JSONArray array = responseObj.optJSONArray("data");
            int length = array.length();

            for(int i = 0; i < length; i++) {
                JSONObject ratingObj = array.optJSONObject(i);
                int id = ratingObj.optInt("id");
                int rate = ratingObj.optInt("rate");

                JSONObject client = ratingObj.getJSONObject("client");
                int client_id = client.optInt("id");
                String client_name = client.optString("name");

                int class_id = ratingObj.optInt("class_id");

                ratingList = new ClassRatingEditModel();
                ratingList.setId(id);
                ratingList.setClientRating(rate);
                ratingList.setClientId(client_id);
                ratingList.setClientName(client_name);
                ratingList.setClassId(class_id);
                ratingLists.add(ratingList);
                adapter.notifyItemInserted(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    Response.ErrorListener errorListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
    };

    // Method for Approve PUT Data on Form Fields
    private void ApproveFormData() {

        String URL = RestAPI.ApproveClass + classId;

        Log.i("PARAM", "timeTaken: " + time);

        StringRequest request = new StringRequest(Request.Method.POST, URL, detailApproveListener, errorApproveListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + storeToken);
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("timeTaken", time);
                return params;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<String> detailApproveListener = response ->
    {
        Log.i("VOLLEY", "response" + response);
        Toast.makeText(getApplicationContext(), "Successfully Submitted ", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ClassRatingEdit.this, ClassListEdit.class);
        startActivity(intent);
    };

    public Response.ErrorListener errorApproveListener = error -> {
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
        Log.e("VOLLEY", "" + error.getMessage());
    };
    // End Method for Approve PUT Data on Form Fields

    private void launchActivity() {
        Intent intent = new Intent(this, ClassEdit.class);
        intent.putExtra("id", classId);
        startActivity(intent);
    }

    private void launchActivity2() {
        Intent intent = new Intent(this, ClassListEdit.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}