package com.madcatworld.e_riqabguru.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.madcatworld.e_riqabguru.R;
import com.madcatworld.e_riqabguru.model.CreateClassModel;
import com.madcatworld.e_riqabguru.model.SubjectModel;
import com.madcatworld.e_riqabguru.utils.RestAPI;
import com.madcatworld.e_riqabguru.utils.SharedPrefsManager;
import com.madcatworld.e_riqabguru.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ClassCreate extends AppCompatActivity {

    private EditText place;
    private Spinner subject;
    private Spinner location;
    private String[] locationList ={"Luar Pusat", "Dalam Pusat"};
    private String storeLocation;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private int currentHour;
    private Calendar calendar;
    private int currentMinute;
    private String amPm;
    private EditText choosedate;
    private EditText chooseTime;
    private Button hantr;
    private Button exit;
    private EditText title;
    private EditText item;
    private EditText outcome;
    private String storeToken;
    private ArrayList<SubjectModel> subjects;
    private CreateClassModel classModel;
    private SubjectModel subjectModel;

    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maklumat_pembelajaran_punch_card);
        Log.d(TAG, "onCreate: called.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Maklumat P & P");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        location = (Spinner)findViewById(R.id.location);
        place = (EditText)findViewById(R.id.place);
        choosedate = (EditText) findViewById(R.id.datepicker);
        chooseTime = (EditText) findViewById(R.id.timepicker);
        subject = (Spinner)findViewById(R.id.subject);
        title = (EditText) findViewById(R.id.title);
        item = (EditText) findViewById(R.id.item);
        outcome = (EditText) findViewById(R.id.outcome);
        hantr = (Button) findViewById(R.id.hantar);

        classModel = new CreateClassModel();

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        hantr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mplace = place.getText().toString().trim();
                String mtitle = title.getText().toString().trim();
                String mitem = item.getText().toString().trim();
                String moutcome = outcome.getText().toString().trim();

                if(!mplace.isEmpty() || !mtitle.isEmpty() || !mitem.isEmpty() || !moutcome.isEmpty()) {

                    classModel.setPlace(place.getText().toString());
                    classModel.setDate(choosedate.getText().toString());
                    classModel.setTime(chooseTime.getText().toString());
                    classModel.setTitle(title.getText().toString());
                    classModel.setItem(item.getText().toString());
                    classModel.setOutcome(outcome.getText().toString());

                    SubmitData();
                    launchActivity1();

                } else {

                    place.setError("Field cannot be empty");
                    title.setError("Field cannot be empty");
                    item.setError("Field cannot be empty");
                    outcome.setError("Field cannot be empty");
                }
            }
        });

        // Date Dialog for Tarikh Field
        choosedate.setInputType(InputType.TYPE_NULL);
        choosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendr = Calendar.getInstance();
                int day = calendr.get(Calendar.DAY_OF_MONTH);
                int month = calendr.get(Calendar.MONTH);
                int year = calendr.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(ClassCreate.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                choosedate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Time Dialog for Masa Field
        chooseTime = findViewById(R.id.timepicker);
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(ClassCreate.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        chooseTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        // Spinner Dropdown for Location
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this,R.layout.support_simple_spinner_dropdown_item, locationList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        location.setAdapter(adapter);

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String locate = ClassCreate.this.location.getItemAtPosition(ClassCreate.this.location.getSelectedItemPosition()).toString();
                classModel.setLocation(locate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        // End Spinner for Location

        // Spinner Dropdown for Subject
        LoadSubjectData();
        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subject = ClassCreate.this.subject.getItemAtPosition(ClassCreate.this.subject.getSelectedItemPosition()).toString();
                subjectModel = subjects.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        // End Spinner for Subject
    }
    // Method for Spinner Subject
    private void LoadSubjectData() {

        StringRequest request = new StringRequest(Request.Method.GET, RestAPI.SubjectURL, detailListener, errorListener) {
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
            subjects = new ArrayList<>();
            JSONObject responseObj = new JSONObject(response);
            JSONArray array = responseObj.optJSONArray("data");
            int length = array.length();

            for(int i = 0; i < length; i++) {
                JSONObject subjectObj = array.optJSONObject(i);
                int id_ = subjectObj.optInt("id");
                String name = subjectObj.optString("name");
                subjectModel = new SubjectModel();
                subjectModel.setId(id_);
                subjectModel.setSubject(name);
                subjects.add(subjectModel);
            }
            ArrayAdapter<SubjectModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subject.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    public Response.ErrorListener errorListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
    };
    // End Method for Spinner Subject

    // Method for POST data fields
    private void SubmitData() {

        StringRequest request = new StringRequest(Request.Method.POST, RestAPI.CreateClass, detailDataListener, errorDataListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + storeToken);
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Log.i("PARAM", "location: " + classModel.getLocation());
                Log.i("PARAM", "place: " + classModel.getPlace());
                Log.i("PARAM", "date: " + classModel.getDate());
                Log.i("PARAM", "time: " + classModel.getTime());
                Log.i("PARAM", "subject_id: " + subjectModel.getId());
                Log.i("PARAM", "title: " + classModel.getTitle());
                Log.i("PARAM", "item: " + classModel.getItem());
                Log.i("PARAM", "outcome: " + classModel.getOutcome());

                Map<String, String> params = new HashMap<>();
                params.put("location", classModel.getLocation());
                params.put("place", classModel.getPlace());
                params.put("date", classModel.getDate());
                params.put("time", classModel.getTime());
                params.put("subject_id", String.valueOf(subjectModel.getId()));
                params.put("title", classModel.getTitle());
                params.put("item", classModel.getItem());
                params.put("outcome", classModel.getOutcome());
                return params;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<String> detailDataListener = response ->
    {
        Log.i("VOLLEY", "response" + response);
    };

    public Response.ErrorListener errorDataListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
    };

    private void launchActivity1() {
        Intent intent = new Intent(this, ClassDetails.class);
        intent.putExtra("id", classModel.getId());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
