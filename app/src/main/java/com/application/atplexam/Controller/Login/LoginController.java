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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.application.atplexam.Controller.NavigationController;
import com.application.atplexam.R;
import com.application.atplexam.Utility.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginController extends AppCompatActivity {

    private Button getStarted;
    private EditText passwd, mail;
    private final String Default = "N/A";
    private CheckBox autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        getStarted = (Button) findViewById(R.id.Continue);
        passwd = (EditText) findViewById(R.id.password);
        mail = (EditText) findViewById(R.id.email);
        autoLogin = (CheckBox) findViewById(R.id.auto_login);

        startActivity(new Intent(LoginController.this, NavigationController.class));

        final SharedPreferences sharedPreferences = getSharedPreferences("Content_main", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        String autologin = sharedPreferences.getString("autologin", "");
        if (autologin.equals("true")) {
            autoLogin.setChecked(true);
            signin(sharedPreferences.getString("email", ""), sharedPreferences.getString("password", ""));
        }

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginController.this, RegisterController.class));
            }
        });

        findViewById(R.id.forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginController.this, ResetPasswordController.class));
            }
        });
        mail.setText(sharedPreferences.getString("email", ""));
        passwd.setText(sharedPreferences.getString("password", ""));

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mail.getText().toString();
                String password = passwd.getText().toString();

                if (autoLogin.isChecked()) {
                    editor.putString("autologin", "true");
                }

                if (email != null && password != null) {
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.commit();

                    signin(email, password);
                }
            }
        });
    }

    private void signin(String email, String passwd) {
        final String url = Constant.baseApiUrl.concat("1.0/login");
        final ProgressDialog progressDialog = new ProgressDialog(LoginController.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        JSONObject req = new JSONObject();
        try {
            req.put("email", email);
            req.put("passwordHash", passwd);
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
                                Toast.makeText(LoginController.this, "Login Successful !!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginController.this, NavigationController.class));
                            } else {
                                Toast.makeText(LoginController.this, response.getString("errorMessage"), Toast.LENGTH_SHORT).show();
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