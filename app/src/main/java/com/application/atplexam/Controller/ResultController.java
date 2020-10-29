package com.application.atplexam.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.application.atplexam.Database.ExamDatabase;
import com.application.atplexam.Model.Exam;
import com.application.atplexam.Model.UserLog;
import com.application.atplexam.R;

import java.util.List;

public class ResultController extends AppCompatActivity {
    private TextView correct, tv_incorrect, tv_total, tv_unanswered;
    private int examId = 0;
    private ExamDatabase ExamDb;
    private Button gotoDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_view);

        prepareExamDb();
        Intent intent = getIntent();
        examId = intent.getIntExtra("examid", 0);
        connectViewComponents();

        List<UserLog> logs = ExamDb.GetUserLogByExamId(examId);
        Exam examData = ExamDb.GetExamById(examId);
        int correctCount = 0;
        for (UserLog log : logs) {
            if (log.iscorrect == 1) {
                correctCount++;
            }
        }

        if (examData != null) {
            tv_total.setText(" " + examData.qscount);
            correct.setText("  " + correctCount);
            tv_incorrect.setText("  " + (logs.size() - correctCount));
            tv_unanswered.setText("  " + (examData.qscount - logs.size()));
        }

        gotoDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultController.this, NavigationController.class));
            }
        });
    }

    private void connectViewComponents() {
        correct = (TextView) findViewById(R.id.tv_correct);
        tv_incorrect = (TextView) findViewById(R.id.tv_incorrect);
        tv_total = (TextView) findViewById(R.id.tv_total);
        gotoDashboard = (Button) findViewById(R.id.gotodashboard);
        tv_unanswered = (TextView) findViewById(R.id.tv_unanswered);
    }

    private void prepareExamDb() {
        ExamDb = new ExamDatabase(this);
        ExamDb.createDatabase();
        ExamDb.openDatabase();
        ExamDb.getWritableDatabase();
    }
}