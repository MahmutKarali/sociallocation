package com.application.atplexam.Controller.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.application.atplexam.R;
import com.application.atplexam.Utility.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordController extends AppCompatActivity {

    private Button getStarted;
    private EditText passwd, mail;
    private final String Default = "N/A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpasswd_view);
        //startActivity(new Intent(LoginController.this, NavigationController.class));
        final SharedPreferences sharedPreferences = getSharedPreferences("Content_main", Context.MODE_PRIVATE);

        getStarted = (Button) findViewById(R.id.Continue);
        mail = (EditText) findViewById(R.id.email);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mail.getText().toString() != null) {
                    resetPasswd(mail.getText().toString());
                }
            }
        });
    }

    private void resetPasswd(String email) {
        final String url = Constant.baseApiUrl.concat("1.0/password");
        final ProgressDialog progressDialog = new ProgressDialog(ResetPasswordController.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        JSONObject req = new JSONObject();
        try {
            req.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(ResetPasswordController.this, "New password sent mail address", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPasswordController.this, LoginController.class));
                            } else {
                                Toast.makeText(ResetPasswordController.this, response.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", "admin");
                params.put("password", "admin");
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        queue.add(getRequest);
    }
}