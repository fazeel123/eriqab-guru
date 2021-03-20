package com.madcatworld.e_riqabguru.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.madcatworld.e_riqabguru.R;
import com.madcatworld.e_riqabguru.model.ClassEditModel;
import com.madcatworld.e_riqabguru.model.SubjectModelEdit;
import com.madcatworld.e_riqabguru.utils.RestAPI;
import com.madcatworld.e_riqabguru.utils.SharedPrefsManager;
import com.madcatworld.e_riqabguru.utils.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ClassEdit extends AppCompatActivity {

    private Spinner subject;
    private Spinner location;
    private String[] locationList ={"Luar Pusat", "Dalam Pusat"};
    private EditText place;
    private EditText title;
    private EditText item;
    private EditText outcome;
    private String storeToken;
    private int classId;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private int currentHour;
    private Calendar calendar;
    private int currentMinute;
    private String amPm;
    private EditText choosedate;
    private EditText chooseStartTime, chooseEndTime;
    private Button tidak_lulus;
    private Button sahkan;
    private ImageView preview1, preview2;

    private ClassEditModel classEdit;
    private SubjectModelEdit subjectModel;
    private ArrayList<SubjectModelEdit> subjects;
    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengesahan_pemantau);
        Log.d(TAG, "onCreate: called.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Pengesahan Rekod Baru - Pemantau");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        classId = getIntent().getIntExtra("id", 0);
        Toast.makeText(getApplicationContext(), String.valueOf(classId), Toast.LENGTH_LONG).show();

        location = (Spinner)findViewById(R.id.location);
        place = (EditText)findViewById(R.id.place);
        choosedate = (EditText) findViewById(R.id.datepicker);
        chooseStartTime = (EditText) findViewById(R.id.timepickerstart);
        chooseEndTime = (EditText) findViewById(R.id.timepickerend);
        subject = (Spinner)findViewById(R.id.subject);
        title = (EditText) findViewById(R.id.title);
        item = (EditText) findViewById(R.id.item);
        outcome = (EditText) findViewById(R.id.outcome);
        tidak_lulus = (Button) findViewById(R.id.tidak);
        sahkan = (Button) findViewById(R.id.sahkan);
        preview1 = (ImageView) findViewById(R.id.preview1);
        preview2 = (ImageView) findViewById(R.id.preview2);

        // Date Dialog for Tarikh Field
        choosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendr = Calendar.getInstance();
                int day = calendr.get(Calendar.DAY_OF_MONTH);
                int month = calendr.get(Calendar.MONTH);
                int year = calendr.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(ClassEdit.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                choosedate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Time Dialog for Masa Mula Field
        chooseStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(ClassEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        chooseStartTime.setText(String.format("%02d:%02d:00", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        // Time Dialog for Masa Tamat Field
        chooseEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(ClassEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        chooseEndTime.setText(String.format("%02d:%02d:00", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        // Spinner Dropdown for Place
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, locationList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        location.setAdapter(adapter);

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String locate = ClassEdit.this.location.getItemAtPosition(ClassEdit.this.location.getSelectedItemPosition()).toString();
                classEdit.setLocation(locate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        // Spinner Dropdown for Subject Field
        LoadSubjectData();
        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subject = ClassEdit.this.subject.getItemAtPosition(ClassEdit.this.subject.getSelectedItemPosition()).toString();
                subjectModel = subjects.get(position);
                classEdit.setSubject_id(subjectModel.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        LoadFormData();

        tidak_lulus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity();
            }
        });

        sahkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity2();
            }
        });
    }

    // Method for GET Data on Form Fields
    private void LoadFormData() {

        String URL = RestAPI.ViewClass + classId;

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
        try {

            JSONObject responseObj = new JSONObject(response);
            JSONObject jsonResponse = responseObj.optJSONObject("data");
            int gid = jsonResponse.optInt("id");
            String glocation = jsonResponse.optString("location");
            String gplace = jsonResponse.optString("place");
            String gdate = jsonResponse.optString("date");
            String gstarttime = jsonResponse.optString("start_time");
            String gendtime = jsonResponse.optString("end_time");
            int gsubjectId = jsonResponse.optInt("id");
            String gsubjectName = jsonResponse.optString("name");
            String gtitle = jsonResponse.optString("title");
            String gitem = jsonResponse.optString("item");
            String goutcome = jsonResponse.optString("outcome");
            String gstatus = jsonResponse.optString("status");
            String gcomment = jsonResponse.optString("comment");
            String gpicture = jsonResponse.optString("punchCard");

            classEdit = new ClassEditModel();
            classEdit.setId(gid);
            classEdit.setLocation(glocation);
            classEdit.setPlace(gplace);
            classEdit.setDate(gdate);
            classEdit.setStartTime(gstarttime);
            classEdit.setEndTime(gendtime);
            classEdit.setSubject_id(gsubjectId);
            classEdit.setSubject_name(gsubjectName);
            classEdit.setTitle(gtitle);
            classEdit.setItem(gitem);
            classEdit.setOutcome(goutcome);
            classEdit.setStatus(gstatus);
            classEdit.setComment(gcomment);
            classEdit.setPicture(gpicture);

//            Toast.makeText(getApplicationContext(), gpicture.toString(), Toast.LENGTH_LONG).show();

            place.setText(classEdit.getPlace());
            choosedate.setText(classEdit.getDate());
            chooseStartTime.setText(classEdit.getStartTime());
            chooseEndTime.setText(classEdit.getEndTime());

            String tempTime = chooseStartTime.getText().toString();
            chooseStartTime.setText(tempTime.substring(0, tempTime.length() - 3));

            title.setText(classEdit.getTitle());
            item.setText(classEdit.getItem());
            outcome.setText(classEdit.getOutcome());

//            for(int i = 0; i < gpicture.length; i++) {
//                Picasso.with(getApplicationContext()).load(gpicture[i]).into(preview1);
//            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    };

    public Response.ErrorListener errorListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
    };
    // End Method for GET Data on Form Fields

    // Method for Spinner Subject
    private void LoadSubjectData() {

        StringRequest request = new StringRequest(Request.Method.GET, RestAPI.SubjectURL, detailLoadListener, errorLoadListener) {
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
            subjects = new ArrayList<>();
            JSONObject responseObj = new JSONObject(response);
            JSONArray array = responseObj.optJSONArray("data");
            int length = array.length();

            for(int i = 0; i < length; i++) {
                JSONObject subjectObj = array.optJSONObject(i);
                int id_ = subjectObj.optInt("id");
                String name = subjectObj.optString("name");
                subjectModel = new SubjectModelEdit();
                subjectModel.setId(id_);
                subjectModel.setSubject(name);
                subjects.add(subjectModel);
            }
            ArrayAdapter<SubjectModelEdit> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subject.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    public Response.ErrorListener errorLoadListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
    };
    // End Method for Spinner Subject

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(ClassEdit.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchActivity() {
        Intent intent = new Intent(this, ClassDisapprove.class);
        intent.putExtra("id", classId);
        startActivity(intent);
    }

    private void launchActivity2() {
        Intent intent = new Intent(this, ClassRatingEdit.class);
        intent.putExtra("id", classId);
        startActivity(intent);
    }
}
