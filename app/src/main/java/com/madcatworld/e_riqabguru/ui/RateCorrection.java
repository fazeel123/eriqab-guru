package com.madcatworld.e_riqabguru.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.madcatworld.e_riqabguru.R;
import com.madcatworld.e_riqabguru.adapters.RateCorrectionAdapter;
import com.madcatworld.e_riqabguru.model.RateCorrectionModel;
import com.madcatworld.e_riqabguru.utils.RestAPI;
import com.madcatworld.e_riqabguru.utils.SharedPrefsManager;
import com.madcatworld.e_riqabguru.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RateCorrection extends AppCompatActivity {

    private static final String TAG = "";
    private String storeToken;
    private int classId;
    private String reason;
    private RecyclerView recyclerView;
    private RateCorrectionAdapter adapter;
    private Button submit;
    private TextView reasonView;

    private List<RateCorrectionModel> lists;
    private RateCorrectionModel rateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_correction);
        Log.d(TAG, "onCreate: called.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Maklumat P & P");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        classId = getIntent().getIntExtra("id", 0);
        reason = getIntent().getStringExtra("correction");

        reasonView = (TextView) findViewById(R.id.reason);
        submit = (Button) findViewById(R.id.selasai);

        reasonView.setText(reason);

        lists = new ArrayList<>();
        setupMenuRecyclerView();
        fetchDetails();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateRating();
            }
        });
    }

    private void setupMenuRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RateCorrectionAdapter(this, lists);
        recyclerView.setAdapter(adapter);
    }

    private void fetchDetails() {

        String URL = RestAPI.EditRateList + classId;

        StringRequest request = new StringRequest(Request.Method.GET, URL, detailListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("Accept", "application/vnd.MAIS_ERIQAB.v1+json");
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

                rateList = new RateCorrectionModel();
                rateList.setId(id);
                rateList.setClientRating(rate);
                rateList.setClientId(client_id);
                rateList.setClientName(client_name);
                rateList.setClassId(class_id);
                lists.add(rateList);
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

    private void UpdateRating() {

        String URL_UP = RestAPI.UpdateRate + classId;

        JSONObject object = createParamsToJSON();
        JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, URL_UP, object, detailUpdateListener, errorUpdateListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + storeToken);
                return params;
            }

//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                c_id = getIntent().getIntExtra("clientId", 0);
//                c_rate = getIntent().getIntExtra("clientRating,", 0);
//                params.put("id[]", String.valueOf(c_id));
//                params.put("rate[]", String.valueOf(c_rate));
//                return params;
//            }

        };

        VolleySingleton.getInstance(this).addToRequestQueue(jsonrequest);
    }

    public Response.Listener<JSONObject> detailUpdateListener = response ->
    {
        Log.i("VOLLEY", "response" + response);

        Toast.makeText(getApplicationContext(), "Successfully Submitted ", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(RateCorrection.this, ClassDetails.class);
        startActivity(intent);
    };

    public Response.ErrorListener errorUpdateListener = error -> {
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
        Log.e("VOLLEY", "" + error.getMessage());
    };

    private JSONObject createParamsToJSON() {

        JSONArray rate = new JSONArray();
        JSONArray client = new JSONArray();
        for(int i=0; i < lists.size(); i++)
        {
            RateCorrectionModel ratingModel = lists.get(i);
            rate.put(ratingModel.getClientRating());
            client.put(ratingModel.getId());
        }
        JSONObject object = new JSONObject();
        try
        {
            object.put("id", client);
            object.put("rate", rate);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        Log.i("VOLLEY", "StudentRating: object: " + object.toString());
        return object;
    }


    public void updateRating(int position, int rating)
    {
        lists.get(position).setClientRating(rating);
        Log.i("RATE", "rate: " + lists.get(position).getClientRating());
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
