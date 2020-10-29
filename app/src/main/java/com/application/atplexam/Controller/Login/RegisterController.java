package com.application.atplexam.Controller.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class RegisterController extends AppCompatActivity {

    private Button getStarted;
    private EditText edit_password, edit_name, edit_email;
    private final String Default = "N/A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_view);

        edit_password = (EditText) findViewById(R.id.password);
        edit_email = (EditText) findViewById(R.id.email);
        edit_name = (EditText) findViewById(R.id.name);

        getStarted = (Button) findViewById(R.id.getStarted);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit_name.getText().toString();
                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();

                if (name.equals("") || email.equals("") || password.equals("")) {
                    Toast.makeText(RegisterController.this, "Please Enter the Details", Toast.LENGTH_SHORT).show();
                } else {
                    register(email, password, name);
                }
            }
        });
    }

    public void showHelp(View view) {
        Toast toast_help = new Toast(getApplicationContext());
        toast_help.setGravity(Gravity.CENTER, 0, 0);
        toast_help.setDuration(Toast.LENGTH_LONG);
        LayoutInflater inflater = getLayoutInflater();
        View appear = inflater.inflate(R.layout.toast_help, (ViewGroup) findViewById(R.id.linear));
        toast_help.setView(appear);
        toast_help.show();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 4000);
    }

    public void showDialog(View view) {

        AlertDialog.Builder alertDialog;//Create a dialog object
        alertDialog = new AlertDialog.Builder(RegisterController.this);
        //EditText to show up in the AlertDialog so that the user can enter the email address
        final EditText editTextDialog = new EditText(RegisterController.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextDialog.setLayoutParams(layoutParams);
        editTextDialog.setHint("Email");

        alertDialog.setView(editTextDialog);
        alertDialog.setTitle("Enter Email");
        final SharedPreferences sharedPreferences = getSharedPreferences("Content_main", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        alertDialog.setPositiveButton("AGREE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email_dialog = editTextDialog.getText().toString();
                if (sharedPreferences.getString("email", Default).equals(email_dialog)) {
                    editor.putString("name", Default);
                    editor.putString("email", Default);
                    editor.putString("password", Default);
                    editor.putString("gender", Default);
                    editor.commit();

                    //This intent will call the package manager and restart the current activity
                    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else {
                    Toast.makeText(RegisterController.this, "Enter correct Email Address", Toast.LENGTH_SHORT).show();
                }
            }

        });
        alertDialog.setNegativeButton("DISAGREE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void register(String email, String passwd, String name) {
        final String url = Constant.baseApiUrl.concat("1.0/register");
        final ProgressDialog progressDialog = new ProgressDialog(RegisterController.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        JSONObject req = new JSONObject();
        try {
            req.put("email", email);
            req.put("passwordHash", email);
            req.put("name", name);
            req.put("role", "admin");
            req.put("locked", "false");
            req.put("enabled", "false");
            req.put("new", true);
            req.put("device", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
            ;
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String str2 = "";
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(RegisterController.this, "Operation Successful !!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterController.this, LoginController.class));
                            } else {
                                Toast.makeText(RegisterController.this, response.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                return params;
            }
        };

        queue.add(getRequest);
    }
}