package com.madcatworld.e_riqabguru.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
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

import com.madcatworld.e_riqabguru.R;

import java.util.Calendar;

public class PembetulanMaklumatKelasActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner tempat;
    Spinner subjek;
    String[] subject ={"Subject","Subject 1","Subject 2","Subject 3","Subject 4","Subject 5"};
    String[] place ={"Tempat","Tempat 1","Tempat 2","Tempat 3","Tempat 4","Tempat 5"};
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    int currentHour;
    Calendar calendar;
    int currentMinute;
    String amPm;
    EditText choosedate;
    EditText chooseTime;
    private ImageView camera_btn;
    private TextView camera_txt;
    private Button selasaiBtn;
    private ImageView prview;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembetulan_maklumat_kelas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Maklumat P & P");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        selasaiBtn = (Button) findViewById(R.id.selasai);

        selasaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity1();
            }
        });

        // Date Dialog for Tarikh Field
        choosedate=(EditText) findViewById(R.id.datepicker);
        chooseTime=(EditText) findViewById(R.id.timepickerstart);

        choosedate.setInputType(InputType.TYPE_NULL);
        choosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendr = Calendar.getInstance();
                int day = calendr.get(Calendar.DAY_OF_MONTH);
                int month = calendr.get(Calendar.MONTH);
                int year = calendr.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(PembetulanMaklumatKelasActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                choosedate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Time Dialog for Masa Field
        chooseTime = findViewById(R.id.timepickerstart);
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(PembetulanMaklumatKelasActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        chooseTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        // Spinner Dropdown for Place
        tempat = (Spinner)findViewById(R.id.place);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, place);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        tempat.setAdapter(adapter);

        tempat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItem() == "Tempat"){

                }
                else {
                    Toast.makeText(PembetulanMaklumatKelasActivity.this, "Selected item is "+ parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Dropdown for Subject Field
        subjek = (Spinner)findViewById(R.id.subject);
        ArrayAdapter<String> adapter_two = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, subject);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        subjek.setAdapter(adapter_two);

        subjek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItem() == "Subject"){

                }
                else {
                    Toast.makeText(PembetulanMaklumatKelasActivity.this, "Selected item is "+ parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Open Camera
        camera_btn = (ImageView) findViewById(R.id.camera_button);
        camera_txt = (TextView) findViewById(R.id.camera_text1);
        prview = (ImageView) findViewById(R.id.preview1);

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        camera_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                prview.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Continue Camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
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
        Intent intent = new Intent(this, PembetulanKehadirangActivity.class);

        startActivity(intent);
    }

}
