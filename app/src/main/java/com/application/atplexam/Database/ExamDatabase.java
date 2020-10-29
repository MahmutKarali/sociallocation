package com.application.atplexam.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.application.atplexam.Model.Exam;
import com.application.atplexam.Model.ExamQuestion;
import com.application.atplexam.Model.FavoriteQuestions;
import com.application.atplexam.Model.UserLog;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExamDatabase extends SQLiteOpenHelper {

    private static final String Database_path = "/data/data/com.application.atplexam/databases/";
    private static final String Database_name = "exam.db";
    private static final String ExamTableName = "Exam";
    private static final String ExamQsTableName = "ExamQuestion";
    private static final String FavoriteQsTableName = "FavoriteQuestions";
    private static final String UserLogTableName = "UserLog";
    private static final String Id = "id";
    private static final String QuestionId = "questionid";
    private static final String ExamId = "examid";
    private static final String SubjectId = "subjectid";
    private static final int version = 1;
    public SQLiteDatabase sqlite;
    private Context context;

    public ExamDatabase(Context context) {
        super(context, Database_name, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void createDatabase() {
        createDB();
    }

    private void createDB() {
        boolean dbexist = DBexists();
        if (!dbexist) {
            this.getReadableDatabase();
            copyDBfromResource();
        }
    }

    private void copyDBfromResource() {
        InputStream is;
        OutputStream os;
        String filePath = Database_path + Database_name;
        try {
            is = context.getAssets().open(Database_name);
            os = new FileOutputStream(filePath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            os.close();
            is.close();

        } catch (IOException e) {
            throw new Error("Problem copying database file:");
        }
    }

    public void openDatabase() throws SQLException {
        String myPath = Database_path + Database_name;
        sqlite = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private boolean DBexists() {
        SQLiteDatabase db = null;
        try {
            String databasePath = Database_path + Database_name;
            db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
            db.setLocale(Locale.getDefault());
            db.setVersion(1);
            db.setLockingEnabled(true);
        } catch (SQLException e) {
            Log.e("Sqlite", "Database not found");
        }
        if (db != null)
            db.close();
        return db != null ? true : false;
    }

    public long InsertExam(Exam ex) {
        sqlite.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("name", ex.name);
        values.put("qscount", ex.qscount);
        values.put("answercount", ex.answercount);
        values.put("totaltime", ex.totaltime);
        values.put("createdtime", ex.createdtime);
        values.put("mode", ex.mode);
        values.put("subjectid", ex.subjectid);
        values.put("unviewedqs", ex.unviewedqs);
        values.put("favorite", ex.favorite);
        values.put("timer", ex.timer);
        values.put("remainingtime", ex.remainingtime);
        values.put("isEnded", ex.isEnded);

        long id = sqlite.insert(ExamTableName, null, values);
        sqlite.setTransactionSuccessful();
        sqlite.endTransaction();
        return id;
    }

    public void UpdateExam(Exam ex) {
        sqlite.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("answercount", ex.answercount);
        values.put("unviewedqs", ex.unviewedqs);
        values.put("remainingtime", ex.remainingtime);

        sqlite.update(ExamTableName, values, " id =" + ex.id, null);
        sqlite.setTransactionSuccessful();
        sqlite.endTransaction();
    }

    public void FinishExam(int id, int isEnded) {
        sqlite.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("isEnded", isEnded);

        sqlite.update(ExamTableName, values, " id =" + id, null);
        sqlite.setTransactionSuccessful();
        sqlite.endTransaction();
    }

    public void UpdateExamQsCount(Exam ex) {
        sqlite.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("qscount", ex.qscount);

        sqlite.update(ExamTableName, values, " id =" + ex.id, null);
        sqlite.setTransactionSuccessful();
        sqlite.endTransaction();
    }

    public void UpdateExam(int id, int remainingtime) {
        sqlite.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("remainingtime", remainingtime);

        sqlite.update(ExamTableName, values, " id =" + id, null);
        sqlite.setTransactionSuccessful();
        sqlite.endTransaction();
    }

    public List<Exam> GetExams() {
        List<Exam> exams = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("SELECT * FROM " + ExamTableName, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Exam exam = new Exam(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getInt(cursor.getColumnIndex("qscount")),
                        cursor.getInt(cursor.getColumnIndex("answercount")),
                        cursor.getInt(cursor.getColumnIndex("totaltime")),
                        cursor.getString(cursor.getColumnIndex("createdtime")),
                        cursor.getInt(cursor.getColumnIndex("mode")),
                        cursor.getInt(cursor.getColumnIndex("subjectid")),
                        cursor.getInt(cursor.getColumnIndex("unviewedqs")),
                        cursor.getInt(cursor.getColumnIndex("favorite")),
                        cursor.getInt(cursor.getColumnIndex("timer")),
                        cursor.getInt(cursor.getColumnIndex("remainingtime")),
                        cursor.getInt(cursor.getColumnIndex("isEnded"))
                );

                exams.add(exam);
                cursor.moveToNext();
            }
        }
        return exams;
    }

    public List<Exam> GetExamsBySubjectId(int subjectId) {
        List<Exam> exams = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("SELECT * FROM " + ExamTableName + " WHERE " + SubjectId + " = " + subjectId, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Exam exam = new Exam(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getInt(cursor.getColumnIndex("qscount")),
                        cursor.getInt(cursor.getColumnIndex("answercount")),
                        cursor.getInt(cursor.getColumnIndex("totaltime")),
                        cursor.getString(cursor.getColumnIndex("createdtime")),
                        cursor.getInt(cursor.getColumnIndex("mode")),
                        cursor.getInt(cursor.getColumnIndex("subjectid")),
                        cursor.getInt(cursor.getColumnIndex("unviewedqs")),
                        cursor.getInt(cursor.getColumnIndex("favorite")),
                        cursor.getInt(cursor.getColumnIndex("timer")),
                        cursor.getInt(cursor.getColumnIndex("remainingtime")),
                        cursor.getInt(cursor.getColumnIndex("isEnded"))
                );

                exams.add(exam);
                cursor.moveToNext();
            }
        }
        return exams;
    }

    public List<ExamQuestion> GetQuestionsByExamId(int examId) {
        List<ExamQuestion> examqsList = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("SELECT * FROM " + ExamQsTableName + " WHERE " + ExamId + " = " + examId, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ExamQuestion qs = new ExamQuestion(
                        cursor.getInt(cursor.getColumnIndex("examid")),
                        cursor.getInt(cursor.getColumnIndex("questionid")),
                        cursor.getInt(cursor.getColumnIndex("subjectid")));

                examqsList.add(qs);
                cursor.moveToNext();
            }
        }
        return examqsList;
    }

    public Exam GetExamById(int examId) {
        Cursor cursor = sqlite.rawQuery("SELECT * FROM " + ExamTableName + " WHERE " + Id + " = " + examId, null);
        if (cursor.moveToFirst()) {
            Exam exam = new Exam(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getInt(cursor.getColumnIndex("qscount")),
                    cursor.getInt(cursor.getColumnIndex("answercount")),
                    cursor.getInt(cursor.getColumnIndex("totaltime")),
                    cursor.getString(cursor.getColumnIndex("createdtime")),
                    cursor.getInt(cursor.getColumnIndex("mode")),
                    cursor.getInt(cursor.getColumnIndex("subjectid")),
                    cursor.getInt(cursor.getColumnIndex("unviewedqs")),
                    cursor.getInt(cursor.getColumnIndex("favorite")),
                    cursor.getInt(cursor.getColumnIndex("timer")),
                    cursor.getInt(cursor.getColumnIndex("remainingtime")),
                    cursor.getInt(cursor.getColumnIndex("isEnded"))
            );
            return exam;
        }
        return null;
    }

    public void InsertExamQuestion(ExamQuestion examQuestion) {
        sqlite.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("examid", examQuestion.examid);
        values.put("questionid", examQuestion.questionid);
        values.put("subjectid", examQuestion.subjectid);

        sqlite.insert(ExamQsTableName, null, values);
        sqlite.setTransactionSuccessful();
        sqlite.endTransaction();
    }

    public void InsertFavoriteQuestion(FavoriteQuestions fav) {
        sqlite.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("subjectid", fav.subjectid);
        values.put("questionid", fav.questionid);

        sqlite.insert(FavoriteQsTableName, null, values);
        sqlite.setTransactionSuccessful();
        sqlite.endTransaction();
    }

    public List<FavoriteQuestions> GetFavoriteQuestion(int subjectid) {
        List<FavoriteQuestions> list = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("SELECT * FROM " + FavoriteQsTableName + " WHERE " + SubjectId + " = " + subjectid, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                FavoriteQuestions fav = new FavoriteQuestions(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("subjectid")),
                        cursor.getInt(cursor.getColumnIndex("questionid")));

                list.add(fav);
                cursor.moveToNext();
            }
        }
        return list;
    }

    public List<Integer> GetFavoriteQuestionIds(int subjectid) {
        List<Integer> list = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("SELECT DISTINCT " + QuestionId + " FROM " + FavoriteQsTableName + " WHERE " + SubjectId + " = " + subjectid, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                list.add(cursor.getInt(cursor.getColumnIndex("questionid")));
                cursor.moveToNext();
            }
        }
        return list;
    }

    public void InsertUserLog(UserLog log) {
        sqlite.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("questionid", log.questionid);
        values.put("useranswer", log.useranswer);
        values.put("iscorrect", log.iscorrect);
        values.put("examid", log.examid);

        sqlite.insert(UserLogTableName, null, values);
        sqlite.setTransactionSuccessful();
        sqlite.endTransaction();
    }

    public void UpdateUserLog(UserLog log) {
        sqlite.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("useranswer", log.useranswer);
        values.put("iscorrect", log.iscorrect);

        sqlite.update(UserLogTableName, values, " id =" + log.id, null);
        sqlite.setTransactionSuccessful();
        sqlite.endTransaction();
    }

    public List<UserLog> GetUserLogByExamId(int examId) {
        List<UserLog> list = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("SELECT * FROM " + UserLogTableName + " WHERE " + ExamId + " = " + examId, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                UserLog log = new UserLog(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("examid")),
                        cursor.getInt(cursor.getColumnIndex("questionid")),
                        cursor.getString(cursor.getColumnIndex("useranswer")),
                        cursor.getInt(cursor.getColumnIndex("iscorrect")));

                list.add(log);
                cursor.moveToNext();
            }
        }
        return list;
    }

    public UserLog GetUserLogByQuestionId(int questionId) {
        List<UserLog> list = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("SELECT * FROM " + UserLogTableName + " WHERE " + QuestionId + " = " + questionId, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                UserLog log = new UserLog(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("examid")),
                        cursor.getInt(cursor.getColumnIndex("questionid")),
                        cursor.getString(cursor.getColumnIndex("useranswer")),
                        cursor.getInt(cursor.getColumnIndex("iscorrect")));

                list.add(log);
                cursor.moveToNext();
            }
        }
        return list.size() == 0 ? null : list.get(0);
    }

    public UserLog GetUserLog(int questionId, int examid) {
        List<UserLog> list = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("SELECT * FROM " + UserLogTableName + " WHERE " + QuestionId + " = " + questionId + " and " + ExamId + " = " + examid, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                UserLog log = new UserLog(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("examid")),
                        cursor.getInt(cursor.getColumnIndex("questionid")),
                        cursor.getString(cursor.getColumnIndex("useranswer")),
                        cursor.getInt(cursor.getColumnIndex("iscorrect")));

                list.add(log);
                cursor.moveToNext();
            }
        }
        return list.size() == 0 ? null : list.get(0);
    }

    public List<ExamQuestion> GetExamQuestions(int subjectid) {
        List<ExamQuestion> list = new ArrayList<>();
        //Cursor cursor = sqlite.rawQuery("SELECT * FROM " + ExamQsTableName + " WHERE " + SubjectId + " = " + subjectid, null);
        Cursor cursor = sqlite.rawQuery("SELECT DISTINCT " + QuestionId + ", " + ExamId + ", " + SubjectId + " FROM " + ExamQsTableName + " WHERE " + SubjectId + " = " + subjectid, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ExamQuestion examqs = new ExamQuestion(
                        cursor.getInt(cursor.getColumnIndex("examid")),
                        cursor.getInt(cursor.getColumnIndex("questionid")),
                        cursor.getInt(cursor.getColumnIndex("subjectid")));

                list.add(examqs);
                cursor.moveToNext();
            }
        }
        return list;
    }

    public List<Integer> GetSeenQuestionIds(int subjectid) {
        List<Integer> list = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("SELECT DISTINCT " + QuestionId + " FROM " + ExamQsTableName + " WHERE " + SubjectId + " = " + subjectid, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                list.add(cursor.getInt(cursor.getColumnIndex("questionid")));
                cursor.moveToNext();
            }
        }
        return list;
    }

    public void DeleteExam(int examId) {
        sqlite.delete(ExamTableName, Id + "=" + examId, null);
        sqlite.delete(UserLogTableName, ExamId + "=" + examId, null);
        sqlite.delete(ExamQsTableName, ExamId + "=" + examId, null);
    }

    public void DeleteFavoriteQs(int qsid) {
        sqlite.delete(FavoriteQsTableName, QuestionId + "=" + qsid, null);
    }
}