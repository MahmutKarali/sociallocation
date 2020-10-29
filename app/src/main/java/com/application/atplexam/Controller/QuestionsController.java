package com.application.atplexam.Controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.atplexam.Database.AtplDatabase;
import com.application.atplexam.Database.ExamDatabase;
import com.application.atplexam.Model.Exam;
import com.application.atplexam.Model.ExamQuestion;
import com.application.atplexam.Model.FavoriteQuestions;
import com.application.atplexam.Model.UserLog;
import com.application.atplexam.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;
import java.util.List;

public class QuestionsController extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ImageView favoriteImage;
    private DonutProgress donutProgress;
    private TextView question, txtFigure, tv_remaining_time;
    private Button OptA, OptB, OptC, OptD, playButton, btn_finish_exam, gotodashboard;
    private LinearLayout llPlay;
    private RelativeLayout questionView, rlStatus;
    private int visibility = 0;
    private String answer = null, figure = null, Ques, Opta, Optb, Optc, Optd;
    private ProgressBar progressBar;
    boolean timer = false;
    private UserLog userLog;
    private CountDownTimer countDownTimer;
    private List<ExamQuestion> examQuestions;
    private AtplDatabase AtplDb;
    private ExamDatabase ExamDb;

    private Exam examData;
    private int userQsIndex;
    private int qsCount;
    private int questionId;
    private int examId;
    private List<FavoriteQuestions> favList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        showProgress("Preparing exam..");

        Bundle extras = getIntent().getExtras();
        prepareAtplDb();
        prepareExamDb();

        connectViewComponents();
        setInitValue();
        clickListeners();

        if (extras != null) {
            examId = extras.getInt("examid");
            examData = ExamDb.GetExamById(examId);
            examQuestions = ExamDb.GetQuestionsByExamId(examId);
            favList = ExamDb.GetFavoriteQuestion(examData.subjectid);
            List<UserLog> logs = ExamDb.GetUserLogByExamId(examId);

            if (examData.isEnded == 1 || (timer && examData.remainingtime == 0)) {
                llPlay.setVisibility(View.GONE);
                questionView.setVisibility(View.GONE);
                rlStatus.setVisibility(View.VISIBLE);

                int correctCount = 0;
                for (UserLog log : logs) {
                    if (log.iscorrect == 1) {
                        correctCount++;
                    }
                }

                ((TextView) findViewById(R.id.text_qs_count)).setText("" + examQuestions.size());
                ((TextView) findViewById(R.id.text_correct_count)).setText("" + correctCount);
                ((TextView) findViewById(R.id.text_wrong_count)).setText("" + (examQuestions.size() - correctCount));
            }
            qsCount = examQuestions.size();
            timer = examData.timer == 1;
        }

        dismissProgress();
    }

    private void clickListeners() {
        txtFigure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionsController.this, FigureViewController.class);
                intent.putExtra("figureName", figure);
                startActivity(intent);
            }
        });

        btn_finish_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExamDb.FinishExam(examId, 1);
                countDownTimer.cancel();

                Intent intent = new Intent(QuestionsController.this, ResultController.class);
                intent.putExtra("examid", examId);
                startActivity(intent);
                return;
            }
        });


        gotodashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuestionsController.this, NavigationController.class));
            }
        });
    }

    private boolean IsContainsInFavorites(int id) {
        favList = ExamDb.GetFavoriteQuestion(examData.subjectid);

        for (FavoriteQuestions item : favList) {
            if (item.questionid == id) {
                return true;
            }
        }
        return false;
    }

    private void setInitValue() {
        userQsIndex = 0;
        qsCount = 0;
        favList = new ArrayList<>();
    }

    private void renderDonutView(int max) {
        donutProgress.setMax(max);
        donutProgress.setFinishedStrokeColor(Color.parseColor("#515A5A"));
        //donutProgress.setTextColor(Color.parseColor("#99A3A4"));
        donutProgress.setTextColor(Color.parseColor("#ffffff"));
        donutProgress.setKeepScreenOn(true);
        donutProgress.setSuffixText("sn");
        tv_remaining_time.setText("" + max);
    }

    private void showProgress(String msg) {
        progressDialog = new ProgressDialog(QuestionsController.this);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    private void dismissProgress() {
        progressDialog.dismiss();
    }

    private void connectViewComponents() {
        gotodashboard = (Button) findViewById(R.id.gotodashboard);
        favoriteImage = (ImageView) findViewById(R.id.favorite_image);
        tv_remaining_time = (TextView) findViewById(R.id.tv_remaining_time);
        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        playButton = (Button) findViewById(R.id.play_button);
        txtFigure = (TextView) findViewById(R.id.figure);
        OptA = (Button) findViewById(R.id.OptionA);
        OptB = (Button) findViewById(R.id.OptionB);
        OptC = (Button) findViewById(R.id.OptionC);
        OptD = (Button) findViewById(R.id.OptionD);
        btn_finish_exam = (Button) findViewById(R.id.btn_finish_exam);
        question = (TextView) findViewById(R.id.Questions);
        llPlay = (LinearLayout) findViewById(R.id.ll_play_button);
        rlStatus = (RelativeLayout) findViewById(R.id.rl_status);
        questionView = (RelativeLayout) findViewById(R.id.rl_question);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteImage.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.start_unselect).getConstantState()) {
                    favoriteImage.setImageResource(R.drawable.select_star);
                    ExamDb.InsertFavoriteQuestion(new FavoriteQuestions(examData.subjectid, questionId));
                    Toast.makeText(QuestionsController.this, "This question has been added to favorites.", Toast.LENGTH_SHORT).show();
                    ((TextView) findViewById(R.id.tv_add_favorite)).setText("Delete from Favorite");

                } else {
                    favoriteImage.setImageResource(R.drawable.start_unselect);
                    ExamDb.DeleteFavoriteQs(questionId);
                    Toast.makeText(QuestionsController.this, "This question has been deleted from favorites.", Toast.LENGTH_SHORT).show();
                    ((TextView) findViewById(R.id.tv_add_favorite)).setText("Add to Favorite");
                }
            }
        });
    }

    private void prepareAtplDb() {
        AtplDb = new AtplDatabase(this);
        AtplDb.createDatabase();
        AtplDb.openDatabase();
        AtplDb.getWritableDatabase();
    }

    private void prepareExamDb() {
        ExamDb = new ExamDatabase(this);
        ExamDb.createDatabase();
        ExamDb.openDatabase();
        ExamDb.getWritableDatabase();
    }

    private void viewQuestion() {
        questionView.setVisibility(View.VISIBLE);
        llPlay.setVisibility(View.GONE);
        visibility = 1;

        if (timer == true) {
            renderDonutView(examData.remainingtime * 1000);
            startTimer(examData.remainingtime * 1000, 1000);
        } else {
            donutProgress.setVisibility(View.GONE);
        }
        setNewQuestion();
    }

    public void onClick(View v) {
        if (visibility == 0) {
            viewQuestion();
        }

        if (v.getId() == R.id.iv_previous) {
            if (userQsIndex == 1) {
                Toast.makeText(QuestionsController.this, "You cannot go back from the first question.", Toast.LENGTH_SHORT).show();
                return;
            }
            userQsIndex -= 2;

            questionId = examQuestions.get(userQsIndex).questionid;
            userLog = ExamDb.GetUserLogByQuestionId(questionId);

            setDefaultBorder();
            if (userLog != null) {
                setAnswerBorder(userLog.useranswer);
            }

            setNewQuestion();
            return;
        }

        if (v.getId() == R.id.iv_next) {
            if (userQsIndex == qsCount) {
                Toast.makeText(QuestionsController.this, "You cannot go back from the first question.", Toast.LENGTH_SHORT).show();
                return;
            }

            questionId = examQuestions.get(userQsIndex).questionid;
            userLog = ExamDb.GetUserLogByQuestionId(questionId);

            setDefaultBorder();

            if (userLog != null) {
                setAnswerBorder(userLog.useranswer);
            }

            setNewQuestion();
            return;
        }
        String userAnswer = null;

        if (v.getId() == R.id.OptionA) {
            userAnswer = "A";
        }
        if (v.getId() == R.id.OptionB) {
            userAnswer = "B";
        }
        if (v.getId() == R.id.OptionC) {
            userAnswer = "C";
        }
        if (v.getId() == R.id.OptionD) {
            userAnswer = "D";
        }

        if (answer != null) {
            boolean correct = false;
            if (answer.equals("A")) {
                if (v.getId() == R.id.OptionA) {
                    correct = true;
                }

            } else if (answer.equals("B")) {
                if (v.getId() == R.id.OptionB) {
                    correct = true;
                }
            } else if (answer.equals("C")) {
                if (v.getId() == R.id.OptionC) {
                    correct = true;
                }
            } else if (answer.equals("D")) {
                if (v.getId() == R.id.OptionD) {
                    correct = true;
                }
            }

            userLog = ExamDb.GetUserLog(questionId, examId);
            if (userAnswer != null) {

                if (userLog == null) {
                    ExamDb.InsertUserLog(new UserLog(0, examId, questionId, userAnswer, correct ? 1 : 0));
                } else {
                    ExamDb.UpdateUserLog(new UserLog(userLog.id, userLog.examid, userLog.questionid, userAnswer, correct ? 1 : 0));
                }
            }

            Exam exam = ExamDb.GetExamById(examId);
            int answercount = exam.answercount + 1;
            ExamDb.UpdateExam(new Exam(examId, answercount, exam.qscount - answercount, examData.remainingtime));

            setAnswerBorder(userAnswer);
        }
    }

    private void setNewQuestion() {
        ((TextView) findViewById(R.id.txt_which_qs)).setText((userQsIndex + 1) + " / " + qsCount);
        questionId = examQuestions.get(userQsIndex).questionid;
        userLog = ExamDb.GetUserLog(questionId, examId);

        Ques = AtplDb.readQuestion(questionId);
        Opta = AtplDb.readOptionA(questionId);
        Optb = AtplDb.readOptionB(questionId);
        Optc = AtplDb.readOptionC(questionId);
        Optd = AtplDb.readOptionD(questionId);
        answer = AtplDb.readAnswer(questionId);
        figure = AtplDb.readFigure(questionId);

        boolean isFavorite = IsContainsInFavorites(questionId);
        favoriteImage.setImageResource(IsContainsInFavorites(questionId) ? R.drawable.select_star : R.drawable.start_unselect);
        if (isFavorite) {
            ((TextView) findViewById(R.id.tv_add_favorite)).setText("Delete from Favorite");
        } else {
            ((TextView) findViewById(R.id.tv_add_favorite)).setText("Add to Favorite");
        }

        setAnswerBorder(".");
        if (userLog != null) {
            setAnswerBorder(userLog.useranswer);
        }

        setFigureButtonVisibility(figure);

        question.setText("" + Ques);
        OptA.setText("A) " + Opta);
        OptB.setText("B) " + Optb);
        OptC.setText("C) " + Optc);
        OptD.setText("D) " + Optd);

        progressBar.setProgress((int) (((float) userQsIndex / qsCount) * 100));

        userQsIndex++;
    }

    private void setAnswerBorder(String userAnswer) {
        if (userAnswer == null || userAnswer.equals("")) {
            return;
        }
        OptA.setBackground(userAnswer.equals("A") ? getDrawable(R.drawable.custom_button_dark_border) : getDrawable(R.drawable.custom_button_light_border));
        OptB.setBackground(userAnswer.equals("B") ? getDrawable(R.drawable.custom_button_dark_border) : getDrawable(R.drawable.custom_button_light_border));
        OptC.setBackground(userAnswer.equals("C") ? getDrawable(R.drawable.custom_button_dark_border) : getDrawable(R.drawable.custom_button_light_border));
        OptD.setBackground(userAnswer.equals("D") ? getDrawable(R.drawable.custom_button_dark_border) : getDrawable(R.drawable.custom_button_light_border));
    }

    private void startTimer(final long millisInFuture, long countDownInterval) {
        donutProgress.setVisibility(View.VISIBLE);
        donutProgress.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
            int i = (int) millisInFuture;

            @Override
            public void onTick(long millisUntilFinished) {
                i = i - 1000;
                examData.remainingtime = i / 1000;
                donutProgress.setProgress(i);

                int min = i / (60000);
                if (min > 0) {
                    tv_remaining_time.setText("" + min + " min " + ((i - min * 60000) / 1000) + " sec");
                } else {
                    tv_remaining_time.setText((i / 1000) + " sec");
                }
            }

            @Override
            public void onFinish() {
                donutProgress.setProgress(0);
                tv_remaining_time.setText("" + 0);
                countDownTimer.cancel();

                ExamDb.UpdateExam(examId, examData.remainingtime);
                Intent intent = new Intent(QuestionsController.this, ResultController.class);
                intent.putExtra("examid", examId);
                startActivity(intent);

                finish();
            }
        }.start();
    }

    private void setFigureButtonVisibility(String figure) {
        if (figure != null && !figure.equals("0")) {
            findViewById(R.id.figure).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.figure).setVisibility(View.GONE);
        }
    }

    private void setDefaultBorder() {
        Drawable light_grey = getDrawable(R.drawable.custom_button_light_border);
        OptA.setBackground(light_grey);
        OptB.setBackground(light_grey);
        OptC.setBackground(light_grey);
        OptD.setBackground(light_grey);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (rlStatus.getVisibility() == View.VISIBLE) {
            finish();
            return;
        }
        showFinishAlert();
    }

    private void showFinishAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("Are you sure you want to quit the exam?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ExamDb.UpdateExam(examId, examData.remainingtime);
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}