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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.madcatworld.e_riqabguru.R;
import com.madcatworld.e_riqabguru.model.CreateClassModel;
import com.madcatworld.e_riqabguru.model.ImageModel;
import com.madcatworld.e_riqabguru.model.SubjectModel;
import com.madcatworld.e_riqabguru.utils.RestAPI;
import com.madcatworld.e_riqabguru.utils.SharedPrefsManager;
import com.madcatworld.e_riqabguru.utils.VolleyMultipartRequest;
import com.madcatworld.e_riqabguru.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MaklumatPembelajaranPunchCardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
    private EditText chooseTimestart;
    private EditText chooseTimeend;
    private ConstraintLayout ctLayout5;
    private Button hantr;
    private ImageView prview;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public int PCODE = 0;
    private Button exit;
    private EditText title;
    private EditText item;
    private EditText outcome;
    private String storeToken;
    private ArrayList<SubjectModel> subjects;
    private CreateClassModel classModel;
    private SubjectModel subjectModel;
    private TextView imageName1, imageName2, imageName3;
    private String date;
    private ImageModel image;

    private static final String TAG = "";
    public int PIC_CODE = 0;
    ArrayList<Bitmap> uploadImage = new ArrayList<>();

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
        chooseTimestart = (EditText) findViewById(R.id.timepickerstart);
        chooseTimeend = (EditText) findViewById(R.id.timepickerend);
        subject = (Spinner)findViewById(R.id.subject);
        title = (EditText) findViewById(R.id.title);
        item = (EditText) findViewById(R.id.item);
        outcome = (EditText) findViewById(R.id.outcome);
        hantr = (Button) findViewById(R.id.hantar);
        imageName1 = (TextView) findViewById(R.id.camera_text1);
        imageName2 = (TextView) findViewById(R.id.camera_text2);
        imageName3 = (TextView) findViewById(R.id.camera_text3);

        classModel = new CreateClassModel();
        image = new ImageModel();

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

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
                    classModel.setStartTime(chooseTimestart.getText().toString());
                    classModel.setEndTime(chooseTimeend.getText().toString());
                    classModel.setTitle(title.getText().toString());
                    classModel.setItem(item.getText().toString());
                    classModel.setOutcome(outcome.getText().toString());

                    SubmitData();
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

                datePickerDialog = new DatePickerDialog(MaklumatPembelajaranPunchCardActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                choosedate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Time Dialog for Masa Mula Field
        chooseTimestart = findViewById(R.id.timepickerstart);
        chooseTimestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(MaklumatPembelajaranPunchCardActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        chooseTimestart.setText(String.format("%02d:%02d:00", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        // Time Dialog for Masa Tamat Field
        chooseTimeend = findViewById(R.id.timepickerend);
        chooseTimeend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(MaklumatPembelajaranPunchCardActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        chooseTimeend.setText(String.format("%02d:%02d:00", hourOfDay, minutes));
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
                String locate = MaklumatPembelajaranPunchCardActivity.this.location.getItemAtPosition(MaklumatPembelajaranPunchCardActivity.this.location.getSelectedItemPosition()).toString();
                classModel.setLocation(locate);
                //Toast.makeText(getApplicationContext(), locate, Toast.LENGTH_LONG).show();
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
                String subject = MaklumatPembelajaranPunchCardActivity.this.subject.getItemAtPosition(MaklumatPembelajaranPunchCardActivity.this.subject.getSelectedItemPosition()).toString();
                subjectModel = subjects.get(position);
//                Toast.makeText(MaklumatPembelajaranPunchCardActivity.this, "Selected item is "+ subjectModel.getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        // End Spinner for Subject

        // Open Camera
        ctLayout5 = (ConstraintLayout) findViewById(R.id.constraintLayout5);
        prview = (ImageView) findViewById(R.id.preview1);

        ctLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

    }

    // Continue Camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {

                Bitmap bp1 = (Bitmap) data.getExtras().get("data");
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = timeStamp + ".jpg";
                imageName1.setText("image1_" + imageFileName);
                uploadImage.add(bp1);
//                image.setBytes1(ImageEncode.getByteArray(bp1));

                if(PIC_CODE < 2) {

                    dispatchTakePictureIntent();

                    Bitmap bp2 = (Bitmap) data.getExtras().get("data");
                    imageName2.setText("image2_" + imageFileName);
//                    image.setBytes2(ImageEncode.getByteArray(bp2));

                    Bitmap bp3 = (Bitmap) data.getExtras().get("data");
                    imageName3.setText("image3_" + imageFileName);
//                    image.setBytes3(ImageEncode.getByteArray(bp3));

                    PIC_CODE++;

                    uploadImage.add(bp2);
                    uploadImage.add(bp3);
                }

//                Bitmap bp = (Bitmap) data.getExtras().get("data");
//                imageName1.setText("image_" + date + ".jpg");
//                image.setBytes(ImageEncode.getByteArray(bp));

//                Toast.makeText(getApplicationContext(), String.valueOf(image.getBytes()), Toast.LENGTH_LONG).show();

//                if(prview.getBackground() == null) {
//                    Bitmap bp = (Bitmap) data.getExtras().get("data");
//                    bp = Bitmap.createScaledBitmap(bp, 400, 400, true);
//                    prview.setImageBitmap(bp);
//
//                    exit.setVisibility(View.VISIBLE);
//                    exit.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            prview.setImageResource(0);
//                            prview.setImageResource(R.drawable.empty_image);
//                            exit.setVisibility(View.GONE);
//                        }
//                    });
//                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void launchActivity1() {
        Intent intent = new Intent(this, StudentRating.class);

        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, RestAPI.CreateClass, detailDataListener, errorDataListener) {

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
                Log.i("PARAM", "timeStart: " + classModel.getStartTime());
                Log.i("PARAM", "timeEnd: " + classModel.getEndTime());
                Log.i("PARAM", "subject_id: " + subjectModel.getId());
                Log.i("PARAM", "title: " + classModel.getTitle());
                Log.i("PARAM", "item: " + classModel.getItem());
                Log.i("PARAM", "outcome: " + classModel.getOutcome());

                Map<String, String> params = new HashMap<>();
                params.put("location", classModel.getLocation());
                params.put("place", classModel.getPlace());
                params.put("date", classModel.getDate());
                params.put("start_time", classModel.getStartTime());
                params.put("end_time", classModel.getEndTime());
                params.put("subject_id", String.valueOf(subjectModel.getId()));
                params.put("title", classModel.getTitle());
                params.put("item", classModel.getItem());
                params.put("outcome", classModel.getOutcome());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> paramsData = new HashMap<>();

                int image_count = uploadImage.size();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                for (int i = 0; i < image_count; i++) {

                    Bitmap filePath = uploadImage.get(i);
                    Log.i("PARAM Bitmap: ", String.valueOf(filePath));

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    filePath.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                    byte[] byte_array = baos.toByteArray();
                    Log.i("PARAM ByteArray: ", String.valueOf(byte_array));

                    if (byte_array != null && byte_array.length > 0) {

                        Log.i("PARAM Image", "punchCard[]: " + "image" + i + "_" + timeStamp + ".jpg" + byte_array);

                        paramsData.put("punchCard[]", new DataPart("image" + i + "_" + timeStamp + ".jpg", byte_array, "image/jpeg"));
                    }
                }

                return paramsData;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<NetworkResponse> detailDataListener = response ->
    {
        Log.i("VOLLEY", "Response" + response.toString());
        Toast.makeText(getApplicationContext(), "Successfully Submitted ", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ClassDetails.class);
        intent.putExtra("id", classModel.getId());
        startActivity(intent);
    };

    public Response.ErrorListener errorDataListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
