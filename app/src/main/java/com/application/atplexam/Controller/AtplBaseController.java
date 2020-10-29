package com.application.atplexam.Controller;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AtplBaseController extends AppCompatActivity {
    ProgressDialog progressDialog;

    public AtplBaseController() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showProgress() {
        progressDialog = new ProgressDialog(AtplBaseController.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
    }

    public void dismissProgress() {
        progressDialog.cancel();
    }
}
