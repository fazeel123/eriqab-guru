package com.madcatworld.e_riqabguru.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.madcatworld.e_riqabguru.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.madcatworld.e_riqabguru.adapters.ClassListEditAdapter;
import com.madcatworld.e_riqabguru.model.ClassDetailsModel;
import com.madcatworld.e_riqabguru.model.ClassListEditModel;
import com.madcatworld.e_riqabguru.utils.RestAPI;
import com.madcatworld.e_riqabguru.utils.SharedPrefsManager;
import com.madcatworld.e_riqabguru.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClassListEdit extends AppCompatActivity {

    private List<ClassListEditModel> classLists;
    private ClassListEditAdapter adapter;
    private String storeToken;
    private RecyclerView recyclerView;

    private static final String TAG = "";
    public ClassListEditModel classList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekod_pemantau);
        Log.d(TAG, "onCreate: called.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Pengesahan Rekod Baru - Pemantau");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        classLists = new ArrayList<>();
        setupMenuRecyclerView();
        fetchDetails();
    }

    private void setupMenuRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ClassListEditAdapter(this, classLists);
        recyclerView.setAdapter(adapter);
        //adapter.setOnItemClickListener(ClassListEdit.this);
    }

    private void fetchDetails() {

        StringRequest request = new StringRequest(Request.Method.GET, RestAPI.CheckedClass, detailListener, errorListener) {
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
                JSONObject kelasObj = array.optJSONObject(i);
                int id = kelasObj.optInt("id");
                String location = kelasObj.optString("location");
                String place = kelasObj.optString("place");
                String date = kelasObj.optString("date");
                String time = kelasObj.optString("time");
                int subject_id = kelasObj.optInt("id");
                String subject_name = kelasObj.optString("name");
                String title = kelasObj.optString("title");
                String item = kelasObj.optString("item");
                String outcome = kelasObj.optString("outcome");
                String status = kelasObj.optString("status");

                classList = new ClassListEditModel();
                classList.setId(id);
                classList.setLocation(location);
                classList.setPlace(place);
                classList.setDate(date);
                classList.setTime(time);
                classList.setSubject_id(subject_id);
                classList.setSubject_name(subject_name);
                classList.setTitle(title);
                classList.setItem(item);
                classList.setOutcome(outcome);
                classList.setStatus(status);
                classLists.add(classList);
                adapter.notifyItemInserted(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    Response.ErrorListener errorListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
    };

    // Method for SearchView
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem  = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.search));
        searchView.setIconifiedByDefault(true);
        searchView.setIconified(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                newText.toLowerCase();
                List<ClassListEditModel> modelList = new ArrayList<>();
                for(ClassListEditModel newModel : classLists) {
                    String search_data = newModel.getTitle().toLowerCase();
                    String search_month = newModel.getMonth().toLowerCase();
                    if(search_data.contains(newText) || search_month.contains(newText)) {
                        modelList.add(newModel);
                    }
                }
                adapter.setSearchOperation(modelList);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    // End Method for SearchView

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
