package com.madcatworld.e_riqabguru.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.madcatworld.e_riqabguru.R;
import com.madcatworld.e_riqabguru.model.UserModel;
import com.madcatworld.e_riqabguru.utils.RestAPI;
import com.madcatworld.e_riqabguru.utils.SharedPrefsManager;
import com.madcatworld.e_riqabguru.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button logBtn;
    EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        logBtn = (Button) findViewById(R.id.logButton);
        editTextEmail = (EditText) findViewById(R.id.id_pengguna);
        editTextPassword = (EditText) findViewById(R.id.password);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = editTextEmail.getText().toString().trim();
                String mPassword = editTextPassword.getText().toString().trim();

                if (!mEmail.isEmpty() || !mPassword.isEmpty()) {
                    Login();
                } else {
                    editTextEmail.setError("Please Insert Email");
                    editTextPassword.setError("Please Insert Password");
                }
            }
        });

        editTextEmail.setText("perkidm@gmail.com");
        editTextPassword.setText("secret");
    }

    private void Login() {
        StringRequest request = new StringRequest(Request.Method.POST, RestAPI.Login, loginListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", editTextEmail.getText().toString().trim());
                params.put("password", editTextPassword.getText().toString().trim());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/vnd.MAIS_ERIQAB.v1+json");
                return headers;
            }
        };
        VolleySingleton.getInstance(Login.this).addToRequestQueue(request);
    }

    private Response.Listener<String> loginListener = response ->
    {
        if(response.isEmpty())
        {
            Toast.makeText(this, "Response is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("VOLLEY", "response: " + response);

        try
        {
            JSONObject responseObject   = new JSONObject(response);
            String access_token   = responseObject.optString("access_token");
            if(!access_token.isEmpty())
            {
                UserModel user = new UserModel();
                user.setAccessToken(access_token);
                SharedPrefsManager info = new SharedPrefsManager(this);
                info.signIn(user);
                Intent intent = new Intent(Login.this, MainMenuActivity.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "Failed to sign in", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    };

    private Response.ErrorListener errorListener = error ->
    {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    };
}
