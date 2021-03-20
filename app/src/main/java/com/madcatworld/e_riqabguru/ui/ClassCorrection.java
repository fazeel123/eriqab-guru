package com.madcatworld.e_riqabguru.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.madcatworld.e_riqabguru.R;
import com.madcatworld.e_riqabguru.model.ClassCorrectionModel;
import com.madcatworld.e_riqabguru.model.ImageModel;
import com.madcatworld.e_riqabguru.model.SubjectModelEdit;
import com.madcatworld.e_riqabguru.utils.ImageEncode;
import com.madcatworld.e_riqabguru.utils.MultipartRequest;
import com.madcatworld.e_riqabguru.utils.RestAPI;
import com.madcatworld.e_riqabguru.utils.SharedPrefsManager;
import com.madcatworld.e_riqabguru.utils.VolleyMultipartRequest;
import com.madcatworld.e_riqabguru.utils.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ClassCorrection extends AppCompatActivity {

    private static final String TAG = "";
    private String storeToken;
    private int classId;

    private Spinner subject;
    private Spinner location;
    private String[] locationList ={"Luar Pusat", "Dalam Pusat"};
    private EditText place;
    private EditText title;
    private EditText item;
    private EditText outcome;
    private TextView correction;
    private Button submit;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private int currentHour;
    private Calendar calendar;
    private int currentMinute;
    private String amPm;
    private EditText choosedate;
    private EditText chooseStartTime, chooseEndTime;
    private ImageView preview;
    private ConstraintLayout camera;
    private String mImage;
    private TextView imageName1, imageName2, imageName3;
    private ConstraintLayout ctLayout5;
    private ImageModel image;
    private String date;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public int PIC_CODE = 0;
    ArrayList<Bitmap> uploadImage = new ArrayList<>();

    private ClassCorrectionModel classCorrectionModel;
    private SubjectModelEdit subjectModel;
    private ArrayList<SubjectModelEdit> subjects;
    private List<String> imageArry = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_correction);
        Log.d(TAG, "onCreate: called.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Maklumat P & P");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        classId = getIntent().getIntExtra("id", 0);
        Toast.makeText(getApplicationContext(), String.valueOf(classId), Toast.LENGTH_LONG).show();

        classCorrectionModel = new ClassCorrectionModel();

        location = (Spinner)findViewById(R.id.location);
        place = (EditText)findViewById(R.id.place);
        choosedate = (EditText) findViewById(R.id.datepicker);
        chooseStartTime = (EditText) findViewById(R.id.timepickerstart);
        chooseEndTime = (EditText) findViewById(R.id.timepickerend);
        subject = (Spinner)findViewById(R.id.subject);
        title = (EditText) findViewById(R.id.title);
        item = (EditText) findViewById(R.id.item);
        outcome = (EditText) findViewById(R.id.outcome);
        correction = (TextView) findViewById(R.id.reason);
        submit = (Button) findViewById(R.id.hantar);
        preview = (ImageView) findViewById(R.id.preview1);
        imageName1 = (TextView) findViewById(R.id.camera_text1);
        imageName2 = (TextView) findViewById(R.id.camera_text2);
        imageName3 = (TextView) findViewById(R.id.camera_text3);
        ctLayout5 = (ConstraintLayout) findViewById(R.id.constraintLayout5);

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        image = new ImageModel();

        // Date Dialog for Tarikh Field
        choosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendr = Calendar.getInstance();
                int day = calendr.get(Calendar.DAY_OF_MONTH);
                int month = calendr.get(Calendar.MONTH);
                int year = calendr.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(ClassCorrection.this, new DatePickerDialog.OnDateSetListener() {
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

                timePickerDialog = new TimePickerDialog(ClassCorrection.this, new TimePickerDialog.OnTimeSetListener() {
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

                timePickerDialog = new TimePickerDialog(ClassCorrection.this, new TimePickerDialog.OnTimeSetListener() {
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
                String locate = ClassCorrection.this.location.getItemAtPosition(ClassCorrection.this.location.getSelectedItemPosition()).toString();
                classCorrectionModel.setLocation(locate);
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
                String subject = ClassCorrection.this.subject.getItemAtPosition(ClassCorrection.this.subject.getSelectedItemPosition()).toString();
                subjectModel = subjects.get(position);
                classCorrectionModel.setSubject_id(subjectModel.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        LoadFormData();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateFormData();
            }
        });

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
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
        Log.e("VOLLEY", "" + error.getMessage());
    };
    // End Method for Spinner Subject

    // Method for GET Data on Form Fields
    private void LoadFormData() {

        String URL = RestAPI.EditClass + classId;

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
            String gstartTime = jsonResponse.optString("start_time");
            String gendTime = jsonResponse.optString("end_time");
            int gsubjectId = jsonResponse.optInt("id");
            String gsubjectName = jsonResponse.optString("name");
            String gtitle = jsonResponse.optString("title");
            String gitem = jsonResponse.optString("item");
            String goutcome = jsonResponse.optString("outcome");
            String gstatus = jsonResponse.optString("status");
            String gcomment = jsonResponse.optString("comment");

            imageArry.add(jsonResponse.optString("punchCard").replace("\\", ""));

//            InputStream input = new java.net.URL(gpicture).openStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            classCorrectionModel = new ClassCorrectionModel();
            classCorrectionModel.setId(gid);
            classCorrectionModel.setLocation(glocation);
            classCorrectionModel.setPlace(gplace);
            classCorrectionModel.setDate(gdate);
            classCorrectionModel.setStartTime(gstartTime);
            classCorrectionModel.setEndTime(gendTime);
            classCorrectionModel.setSubject_id(gsubjectId);
            classCorrectionModel.setSubject_name(gsubjectName);
            classCorrectionModel.setTitle(gtitle);
            classCorrectionModel.setItem(gitem);
            classCorrectionModel.setOutcome(goutcome);
            classCorrectionModel.setStatus(gstatus);
            classCorrectionModel.setComment(gcomment);
//            classCorrectionModel.setPicture(gpicture);

            place.setText(classCorrectionModel.getPlace());
            choosedate.setText(classCorrectionModel.getDate());
            chooseStartTime.setText(classCorrectionModel.getStartTime());
            chooseEndTime.setText(classCorrectionModel.getEndTime());
//
//            String tempTime = chooseTime.getText().toString();
//            chooseTime.setText(tempTime.substring(0, tempTime.length() - 3));

            correction.setText(classCorrectionModel.getComment());
            title.setText(classCorrectionModel.getTitle());
            item.setText(classCorrectionModel.getItem());
            outcome.setText(classCorrectionModel.getOutcome());

            for(int i = 0; i < imageArry.size(); i++) {
                imageName1.setText(imageArry.get(i));
            }

//            if(imageName.getText().toString().trim().contains("https://eriqab-staging.mais.gov.my/storage/")) {
//                imageName.setText(gpicture.replace("https://eriqab-staging.mais.gov.my/storage/punchCard/punchCard-", ""));
//                image.setName1(gpicture.replace("https://eriqab-staging.mais.gov.my/storage/punchCard/punchCard-", ""));
//                image.setBytes1(ImageEncode.getByteArray(myBitmap));
//            } else if(imageName.getText().toString().trim().contains("https://eriqab-staging.mais.gov.my/storage/punchCard/punchCard-")) {
//                imageName.setText(gpicture.replace("https://eriqab-staging.mais.gov.my/storage/punchCard/punchCard-", ""));
//                image.setName1(gpicture.replace("https://eriqab-staging.mais.gov.my/storage/punchCard/punchCard-", ""));
//                image.setBytes1(ImageEncode.getByteArray(myBitmap));
//            }
//
//
//            Picasso.with(getApplicationContext()).load(gpicture).into(preview);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    };

    public Response.ErrorListener errorListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
        Log.e("VOLLEY", "" + error.getMessage());
    };
    // End Method for GET Data on Form Fields

    // Method for PUT Data on Form Fields
    private void UpdateFormData() {

        String mPlace = place.getText().toString();
        String mDate = choosedate.getText().toString();
        String mTime = chooseStartTime.getText().toString();
        String mTitle = title.getText().toString();
        String mItem = item.getText().toString();
        String mOutcome = outcome.getText().toString();

        Log.i("PARAM", "location: " + classCorrectionModel.getLocation());
        Log.i("PARAM", "place: " + classCorrectionModel.getPlace());
        Log.i("PARAM", "date: " + classCorrectionModel.getDate());
        Log.i("PARAM", "timeStart: " + classCorrectionModel.getStartTime());
        Log.i("PARAM", "timeEnd: " + classCorrectionModel.getEndTime());
        Log.i("PARAM", "subject_id: " + classCorrectionModel.getSubject_id());
        Log.i("PARAM", "title: " + classCorrectionModel.getTitle());
        Log.i("PARAM", "item: " + classCorrectionModel.getItem());
        Log.i("PARAM", "outcome: " + classCorrectionModel.getOutcome());

        String URL = RestAPI.UpdateClass + classId;
        Log.i("PARAM", "id: " + URL.toString());

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, URL, detailUpdateListener, errorUpdateListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + storeToken);
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("location", classCorrectionModel.getLocation());
                params.put("place", mPlace);
                params.put("date", mDate);
                params.put("start_time", classCorrectionModel.getStartTime());
                params.put("end time", classCorrectionModel.getEndTime());
                params.put("subject_id", String.valueOf(classCorrectionModel.getSubject_id()));
                params.put("title", mTitle);
                params.put("item", mItem);
                params.put("outcome", mOutcome);

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> paramsData = new HashMap<>();

                if(!uploadImage.isEmpty()) {
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

                            paramsData.put("punchCard[]", new VolleyMultipartRequest.DataPart("image" + i + "_" + timeStamp + ".jpg", byte_array, "image/jpeg"));
                        }
                    }
                } else {
                    int image_count = imageArry.size();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                    for (int i = 0; i < image_count; i++) {

                        String filePath = imageArry.get(i);
                        Log.i("PARAM Bitmap: ", String.valueOf(filePath));

                        byte[] encodeByte = Base64.decode(filePath, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                        byte[] byte_array = baos.toByteArray();
                        Log.i("PARAM ByteArray: ", String.valueOf(byte_array));

                        if (byte_array != null && byte_array.length > 0) {

                            Log.i("PARAM Image", "punchCard[]: " + "image" + i + "_" + timeStamp + ".jpg" + byte_array);

                            paramsData.put("punchCard[]", new VolleyMultipartRequest.DataPart("image" + i + "_" + timeStamp + ".jpg", byte_array, "image/jpeg"));
                        }
                    }
                }


                return paramsData;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<NetworkResponse> detailUpdateListener = response ->
    {
        Log.i("VOLLEY", "response" + response);

        Intent intent = new Intent(ClassCorrection.this, RateCorrection.class);
        intent.putExtra("correction", classCorrectionModel.getComment());
        intent.putExtra("id", classId);
        startActivity(intent);
    };

    public Response.ErrorListener errorUpdateListener = error -> {
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
        Log.e("VOLLEY", "" + error.getMessage());
        Toast.makeText(this, String.valueOf(error.getMessage()), Toast.LENGTH_LONG).show();
    };
    // End Method for PUT Data on Form Fields

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(ClassCorrection.this, ClassDetails.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
