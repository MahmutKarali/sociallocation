package com.application.atplexam.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.application.atplexam.Database.AtplDatabase;
import com.application.atplexam.Database.ExamDatabase;
import com.application.atplexam.Model.Exam;
import com.application.atplexam.Model.ExamQuestion;
import com.application.atplexam.Model.Question;
import com.application.atplexam.R;
import com.application.atplexam.Utility.Constant;
import com.application.atplexam.Utility.ExamListAdapter;
import com.application.atplexam.Utility.Helper;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationController extends AtplBaseController
        implements NavigationView.OnNavigationItemSelectedListener {

    private Animation animShake;
    private Button createExam, createTest;
    private List<Exam> exams = new ArrayList<>();
    private AtplDatabase AtplDb;
    private ExamDatabase ExamDb;
    private SwipeMenuListView exam_list;
    private ExamListAdapter adapter;
    private LinearLayout llFilter, llRefresh;
    private AVLoadingIndicatorView avi;
    int currentPageCount = 0;
    int totalPages = 0;
    int qsTableUniqueId = 1;
    private List<Question> questions = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        /*String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println("Refreshed token: " + refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic("testtopic").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            }
        });*/

        //callQsApi();

        createExam = (Button) findViewById(R.id.btn_create_exam);
        createTest = (Button) findViewById(R.id.btn_create_test);

        llFilter = (LinearLayout) findViewById(R.id.bottom_bar_filter);
        llRefresh = (LinearLayout) findViewById(R.id.bottom_bar_refresh);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        animShake = AnimationUtils.loadAnimation(this, R.anim.shake);

        prepareAtplDb();
        prepareExamDb();

        exam_list = (SwipeMenuListView) findViewById(R.id.list_exam);
        exam_list.setDivider(null);
        exam_list.setDividerHeight(0);
        renderListview();

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(52, 73, 94)));
                deleteItem.setWidth(90);
                deleteItem.setIcon(R.drawable.bin);
                menu.addMenuItem(deleteItem);
            }
        };

        exam_list.setMenuCreator(creator);

        exam_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                showRemoveAlert(position);
                return false;
            }
        });

        createExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewExamDialog();
            }
        });

        createExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewExamDialog();
            }
        });

        llRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        renderListview();
                        stopAnim();
                    }
                }, 1000);
            }
        });

        createTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTestDialog();
            }
        });

        exam_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int examId = exams.get(position).id;

                if (exams.get(position).mode.equals(0)) {
                    Intent intent = new Intent(NavigationController.this, TestQuestionController.class);
                    intent.putExtra("examid", examId);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(NavigationController.this, QuestionsController.class);
                intent.putExtra("examid", examId);
                startActivity(intent);
            }
        });
    }

    private void showRemoveAlert(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("Do you want to delete?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                ExamDb.DeleteExam(exams.get(position).id);
                renderListview();
                dialog.dismiss();
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

    private void renderListview() {
        exams = ExamDb.GetExams();

        findViewById(R.id.ll_empty).setVisibility(View.GONE);
        findViewById(R.id.ll_list).setVisibility(View.VISIBLE);
        if (exams.size() == 0) {
            findViewById(R.id.ll_empty).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_list).setVisibility(View.GONE);
        }
        adapter = new ExamListAdapter(this, exams);
        exam_list.setAdapter(adapter);
    }

    public void createNewExamDialog() {
        final Dialog dialog = new Dialog(NavigationController.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.create_exam_popup);
        dialog.show();
        final Spinner spinner = dialog.findViewById(R.id.spinner);
        final EditText name = dialog.findViewById(R.id.exam_name);
        final CheckBox checkboxUnseenqs = dialog.findViewById(R.id.switch_unseen_qs);
        final CheckBox checkboxFavqs = dialog.findViewById(R.id.switch_favorite);
        checkboxUnseenqs.setChecked(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NavigationController.this,
                R.layout.spinner_item, Helper.GetSubjectKeyList());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btn_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String examName = name.getText().toString();
                if (examName.equals(null) || examName.equals("") || examName.equals(" ")) {
                    Toast.makeText(NavigationController.this, "Exam name count cannot be null!", Toast.LENGTH_SHORT).show();
                    name.startAnimation(animShake);
                    return;
                }

                String subject = spinner.getSelectedItem().toString();
                int subjectId = Helper.GetSubjectId(subject);
                List<Question> qsList = AtplDb.GetExamsBySubjectId(subjectId);
                int qsCount = Helper.GetQuestionCount(subjectId);
                int qsMinute = Helper.GetQuestionMinute(subjectId) * 60;
                //mode 1 : exam
                Exam exam = new Exam(0,
                        name.getText().toString(),
                        qsCount,
                        0,
                        qsMinute,
                        Helper.GetDate(),
                        1,
                        subjectId,
                        checkboxUnseenqs.isChecked() ? 1 : 0,
                        0,
                        1,
                        qsMinute,
                        0);

                long id = ExamDb.InsertExam(exam);
                SaveExamQuestions(qsList, (int) id, qsCount, checkboxUnseenqs.isChecked() ? 1 : 0, subjectId, checkboxFavqs.isChecked() ? 1 : 0);
                renderListview();
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void createNewTestDialog() {
        final Dialog dialog = new Dialog(NavigationController.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.create_test_popup);
        dialog.show();
        final Spinner spinner = dialog.findViewById(R.id.spinner);
        final EditText name = dialog.findViewById(R.id.exam_name);
        final EditText time = dialog.findViewById(R.id.exam_time);
        final EditText count = dialog.findViewById(R.id.exam_count);

        final CheckBox checkboxUnseenqs = dialog.findViewById(R.id.switch_unseen_qs);
        final CheckBox checkboxFavorite = dialog.findViewById(R.id.switch_favorite);
        checkboxUnseenqs.setChecked(false);
        checkboxFavorite.setChecked(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NavigationController.this,
                android.R.layout.simple_spinner_item, Helper.GetSubjectKeyList());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btn_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = spinner.getSelectedItem().toString();
                String examcount = count.getText().toString();
                String examtime = time.getText().toString();
                String examname = name.getText().toString();
                if (Helper.isEmptyString(examname)) {
                    Toast.makeText(NavigationController.this, "Test name cannot be null!", Toast.LENGTH_SHORT).show();
                    name.startAnimation(animShake);
                    return;
                }

                if (examcount.equals(null) || examcount.equals("") || examcount.equals(" ")) {
                    Toast.makeText(NavigationController.this, "Question count cannot be null!", Toast.LENGTH_SHORT).show();
                    count.startAnimation(animShake);
                    return;
                }

                if (!Helper.isEmptyString(examtime) &&
                        !Helper.isEmptyString(examname) &&
                        !Helper.isEmptyString(subject)) {

                    int subjectId = Helper.GetSubjectId(subject);
                    List<Question> qsList = AtplDb.GetExamsBySubjectId(subjectId);
                    //mode 0 : test
                    Exam exam = new Exam(0,
                            name.getText().toString(),
                            Integer.valueOf(examcount),
                            0,
                            examtime.equals("") ? 0 : Integer.valueOf(examtime) * 60,
                            Helper.GetDate(),
                            0,
                            subjectId,
                            checkboxUnseenqs.isChecked() ? 1 : 0,
                            checkboxFavorite.isChecked() ? 1 : 0,
                            examtime.equals("") ? 0 : 1,
                            examtime.equals("") ? 0 : Integer.valueOf(examtime) * 60,
                            0);

                    long id = ExamDb.InsertExam(exam);
                    SaveExamQuestions(qsList, (int) id, Integer.valueOf(examcount), checkboxUnseenqs.isChecked() ? 1 : 0, subjectId, checkboxFavorite.isChecked() ? 1 : 0);
                    renderListview();
                    dialog.dismiss();
                }
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void SaveExamQuestions(List<Question> qsList, int examId, int qsCount, int unseenqs, int subjectid, int favorite) {
        int index = 1;

        if (favorite == 1) {
            List<Integer> favQsIds = ExamDb.GetFavoriteQuestionIds(subjectid);
            for (Integer qsId : favQsIds) {
                ExamDb.InsertExamQuestion(new ExamQuestion(examId, qsId, subjectid));

                if (index == qsCount) {
                    break;
                }

                index++;
            }
        }

        if (unseenqs == 1) {
            List<Integer> seenQsIds = ExamDb.GetSeenQuestionIds(subjectid);

            for (int i = qsList.size() - 1; i >= 0; --i) {
                if (seenQsIds.contains(qsList.get(i).autoid)) {
                    qsList.remove(i);
                }
            }
        }

        for (Question qs : qsList) {
            ExamDb.InsertExamQuestion(new ExamQuestion(examId, qs.autoid, subjectid));
            if (index == qsCount) {
                break;
            }

            index++;
        }

        if (index == 1) {
            ExamDb.DeleteExam(examId);
        } else {
            ExamDb.UpdateExamQsCount(new Exam(examId, index));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_Setting) {
            startActivity(new Intent(this, SettingController.class));

        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("mailto:"));
            String[] recipents = {"atpltestercontact@gmail.com"};
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, recipents);
            intent.putExtra(Intent.EXTRA_SUBJECT, "ATPL Tester Feedback");
            Intent chooser = Intent.createChooser(intent, "Send Feedback Via");
            startActivity(chooser);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    protected void onResume() {
        super.onResume();
        renderListview();
    }

    void startAnim() {
        findViewById(R.id.ll_loadbar).setVisibility(View.VISIBLE);
        avi.show();
    }

    void stopAnim() {
        avi.hide();
        findViewById(R.id.ll_loadbar).setVisibility(View.GONE);
    }

    public void callQsApi() {
        final String url = Constant.baseApiUrl.concat("1.0/questions?size=100&page=") + currentPageCount;

        if (totalPages < currentPageCount) {
            //progressDialog.dismiss();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String utf = response.toString();
                            String value = new String(utf.getBytes("UTF-8"));

                            response = new JSONObject(value);

                            JSONArray arr = response.getJSONObject("payload").getJSONArray("content");
                            totalPages = response.getJSONObject("payload").getInt("totalPages");

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);

                                Question qs = new Question(
                                        qsTableUniqueId,
                                        obj.getInt("id"),
                                        obj.getInt("orjid"),
                                        obj.getInt("ans"),
                                        obj.getInt("grp"),
                                        obj.getString("src"),
                                        obj.getInt("sbjid"),
                                        obj.getString("qs"),
                                        obj.getString("a"),
                                        obj.getString("b"),
                                        obj.getString("c"),
                                        obj.getString("d"),
                                        obj.getString("figure")
                                );

                                questions.add(qs);

                                AtplDb.insert(qs);
                                qsTableUniqueId++;
                                Log.e("QS TABLE ID :", "" + qs.id + " page no: " + currentPageCount);

                            }
                            currentPageCount++;
                            callQsApi();
                        } catch (JSONException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
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