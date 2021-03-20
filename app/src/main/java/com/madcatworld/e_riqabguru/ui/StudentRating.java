package com.madcatworld.e_riqabguru.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.madcatworld.e_riqabguru.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.madcatworld.e_riqabguru.adapters.StudentRatingAdapter;
import com.madcatworld.e_riqabguru.model.ClassDetailsModel;
import com.madcatworld.e_riqabguru.model.StudentRatingModel;
import com.madcatworld.e_riqabguru.utils.RestAPI;
import com.madcatworld.e_riqabguru.utils.SharedPrefsManager;
import com.madcatworld.e_riqabguru.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StudentRating extends AppCompatActivity {

    private List<StudentRatingModel> ratingLists;
    private StudentRatingAdapter adapter;
    private Button selasaiBtn;
    private String storeToken;
    private RecyclerView recyclerView;
    private int classId;
    private TextView studentName;
    private RatingBar studentRating;
    public StudentRatingModel ratingList;
    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maklumat_pembelajaran_kehadiran);
        Log.d(TAG, "onCreate: called.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Maklumat Pembelajaran Kehadiran");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        classId = getIntent().getIntExtra("id", 0);
//        Toast.makeText(getApplicationContext(), String.valueOf(classId), Toast.LENGTH_LONG).show();

        studentRating = (RatingBar) findViewById(R.id.rateView);

        selasaiBtn = (Button) findViewById(R.id.selasai);
        selasaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRating();
            }
        });

        ratingLists = new ArrayList<>();
        setupMenuRecyclerView();
        fetchDetails();
    }

    private void setupMenuRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new StudentRatingAdapter(this, ratingLists);
        recyclerView.setAdapter(adapter);
    }

    private void fetchDetails() {

        String URL = RestAPI.ClassListId + classId;

        StringRequest request = new StringRequest(Request.Method.GET, URL, detailLoadListener, errorLoadListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + storeToken);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<String> detailLoadListener = response ->
    {
        Log.i("VOLLEY", "response" + response);



        try {
            JSONObject responseObj = new JSONObject(response);
            JSONArray array = responseObj.optJSONArray("data");
            int length = array.length();

            for(int i = 0; i < length; i++) {
                JSONObject ratingObj = array.optJSONObject(i);
                int id = ratingObj.optInt("id");
                String name = ratingObj.optString("name");
                int class_id = ratingObj.optInt("class_id");

                ratingList = new StudentRatingModel();
                ratingList.setId(id);
                ratingList.setName(name);
                ratingList.setClass_Id(class_id);
                ratingLists.add(ratingList);
                adapter.notifyItemInserted(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    };

    Response.ErrorListener errorLoadListener = error -> {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Already Rated");
        builder.setCancelable(true);
        builder.setNegativeButton(
                "Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(StudentRating.this, ClassDetails.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        Log.e("VOLLEY", "" + error.getMessage());
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
    };

    // Method for Rating
    private void sendRating()
    {
        String URL = RestAPI.Rate + classId;
        Log.i("VOLLEY", "StudentRating: URL: " + URL);

        JSONObject object = createParamsToJSON();
        JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, URL, object, addRatingsListener, errorRatingListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/form-data; charset=UTF-8");
                params.put("Authorization", "Bearer " + storeToken);
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(jsonrequest);
    }

    public Response.Listener<JSONObject> addRatingsListener = response ->
    {
        Log.i("VOLLEY", "response" + response.toString());

        Toast.makeText(getApplicationContext(), "Successfully Submitted ", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(StudentRating.this, ClassDetails.class);
        startActivity(intent);
    };

    Response.ErrorListener errorRatingListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
    };

    private JSONObject createParamsToJSON()
    {
        JSONArray rate = new JSONArray();
        JSONArray client = new JSONArray();
        for(int i=0; i<ratingLists.size(); i++)
        {
            StudentRatingModel ratingModel = ratingLists.get(i);
            rate.put(ratingModel.getRating());
            client.put(ratingModel.getId());
        }
        JSONObject object = new JSONObject();
        try
        {
            object.put("rate", rate);
            object.put("client_id", client);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        Log.i("VOLLEY", "StudentRating: object: " + object.toString());
        return object;
    }

    private void launchActivity1() {
        Intent intent = new Intent(this, ClassDetails.class);
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

    public void updateRating(int position, int rating)
    {
        ratingLists.get(position).setRating(rating);
        Log.i("RATE", "rate: " + ratingLists.get(position).getRating());
    }
}

