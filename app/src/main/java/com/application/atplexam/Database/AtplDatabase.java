package com.application.atplexam.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.application.atplexam.Model.Question;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AtplDatabase extends SQLiteOpenHelper {

    private static final String Database_path = "/data/data/com.application.atplexam/databases/";
    private static final String Database_name = "atpl.db";
    private static final String Table_name = "Question";
    private static final String AutoId = "autoid";
    private static final String Id = "id";
    private static final String OrjId = "orjid";
    private static final String Src = "src";
    private static final String SbjId = "sbjid";
    private static final String Qs = "qs";
    private static final String A = "a";
    private static final String B = "b";
    private static final String C = "c";
    private static final String D = "d";
    private static final String ANS = "ans";
    private static final String Figure = "figure";
    private static final String Grp = "grp";
    private static final int version = 1;
    public SQLiteDatabase sqlite;
    private Context context;

    public AtplDatabase(Context context) {//constructor
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
            is = context.getAssets().open(Database_name);//reading purpose
            os = new FileOutputStream(filePath);//writing purpose
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);//writing to file
            }
            os.flush();
            os.close();
            is.close();

        } catch (IOException e) {
            throw new Error("Problem copying database file:");
        }
    }

    public void openDatabase() throws SQLException//called by onCreate method of QuestionsController Activity
    {
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

    public int GetCount(int subjectId) {
        String countQuery = "SELECT  * FROM " + Table_name + " WHERE " + SbjId + "=" + subjectId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void insert(Question qs) {
        sqlite.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(AutoId, qs.autoid);
        values.put(Id, qs.id);
        values.put(OrjId, qs.orjid);
        values.put(Src, qs.src);
        values.put(Qs, qs.qs);
        values.put(SbjId, qs.sbjid);
        values.put(A, qs.a);
        values.put(B, qs.b);
        values.put(C, qs.c);
        values.put(D, qs.d);
        values.put(Figure, qs.figure);
        values.put(ANS, qs.ans);
        values.put(Grp, qs.grp);

        sqlite.insert(Table_name, null, values);
        sqlite.setTransactionSuccessful();
        sqlite.endTransaction();
    }

    public String readQuestion(int i) {
        String Ans = "";
        Cursor c = sqlite.rawQuery("SELECT " + Qs + " FROM " + Table_name + " WHERE " + AutoId + "= " + String.valueOf(i), null);
        if (c.moveToFirst())
            Ans = c.getString(0);
        else
            Ans = "";
        return Ans;
    }

    public String readOptionA(int i) {
        String Ans = "";
        Cursor c = sqlite.rawQuery("SELECT " + A + " FROM " + Table_name + " WHERE " + AutoId + " = " + i + "", null);
        if (c.moveToFirst())
            Ans = c.getString(0);
        else
            Ans = "";
        return Ans;
    }

    public String readOptionB(int i) {
        String Ans = "";
        Cursor c = sqlite.rawQuery("SELECT " + B + " FROM " + Table_name + " WHERE " + AutoId + " = " + i + "", null);
        if (c.moveToFirst())
            Ans = c.getString(0);
        else
            Ans = "";
        return Ans;
    }

    public String readOptionC(int i) {
        String Ans = "";
        Cursor c = sqlite.rawQuery("SELECT " + C + " FROM " + Table_name + " WHERE " + AutoId + " = " + i + "", null);
        if (c.moveToFirst())
            Ans = c.getString(0);
        else
            Ans = "";
        return Ans;
    }

    public String readOptionD(int i) {
        String Ans = "";
        Cursor c = sqlite.rawQuery("SELECT " + D + " FROM " + Table_name + " WHERE " + AutoId + " = " + i + "", null);
        if (c.moveToFirst())
            Ans = c.getString(0);
        else
            Ans = "";
        return Ans;
    }

    public String readAnswer(int i) {
        String Ans = "";
        Cursor c = sqlite.rawQuery("SELECT " + ANS + " FROM " + Table_name + " WHERE " + AutoId + " = " + i + "", null);
        if (c.moveToFirst())
            Ans = c.getString(0);
        else
            Ans = "";

        return Ans.equals("1") ? "A" : (Ans.equals("2") ? "B" : (Ans.equals("3") ? "C" : "D"));
    }

    public String readFigure(int i) {
        String Ans = "";
        Cursor c = sqlite.rawQuery("SELECT " + Figure + " FROM " + Table_name + " WHERE " + AutoId + " = " + i + "", null);
        if (c.moveToFirst())
            Ans = c.getString(0);
        else
            Ans = "";
        return Ans;
    }

    public List<Question> GetExamsBySubjectId(int subjectId) {
        List<Question> questions = new ArrayList<>();
        Cursor cursor = sqlite.rawQuery("SELECT * FROM " + Table_name + " WHERE " + SbjId + " = " + subjectId, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Question qs = new Question(
                        cursor.getInt(cursor.getColumnIndex("autoid")),
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("orjid")),
                        cursor.getInt(cursor.getColumnIndex("ans")),
                        cursor.getInt(cursor.getColumnIndex("grp")),
                        cursor.getString(cursor.getColumnIndex("src")),
                        cursor.getInt(cursor.getColumnIndex("sbjid")),
                        cursor.getString(cursor.getColumnIndex("qs")),
                        cursor.getString(cursor.getColumnIndex("a")),
                        cursor.getString(cursor.getColumnIndex("b")),
                        cursor.getString(cursor.getColumnIndex("c")),
                        cursor.getString(cursor.getColumnIndex("d")),
                        cursor.getString(cursor.getColumnIndex("figure")));

                questions.add(qs);
                cursor.moveToNext();
            }
        }
        return questions;
    }
}