package com.application.atplexam.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Helper {

    static Map<String, Integer> subjects = new HashMap<String, Integer>() {{
        put("Air Law", 10);
        put("AGK-System", 21);
        put("AGK-Instruments", 22);
        put("Mass&Balance", 31);
        put("Performance", 32);
        put("Flight Planning", 33);
        put("Human Performance", 40);
        put("Meteorology", 50);
        put("General Navigation", 61);
        put("Radio Navigation", 62);
        put("Operational Procedures", 71);
        put("Principles of Flight", 81);
        put("VFR Comms", 91);
        put("IFR Comms", 92);
    }};

    public static Integer GetQuestionCount(Integer subjectId) {
        switch (subjectId) {
            case 10:
                return 75;
            case 21:
                return 76;
            case 22:
                return 56;
            case 31:
                return 22;
            case 32:
                return 34;
            case 33:
                return 56;
            case 40:
                return 47;
            case 50:
                return 90;
            case 61:
                return 54;
            case 62:
                return 59;
            case 71:
                return 50;
            case 81:
                return 44;
            case 91:
                return 23;
            case 92:
                return 23;
        }
        return 0;
    }

    public static Integer GetQuestionMinute(Integer subjectId) {
        switch (subjectId) {
            case 10:
                return 100;
            case 21:
                return 120;
            case 22:
                return 90;
            case 31:
                return 60;
            case 32:
                return 60;
            case 33:
                return 180;
            case 40:
                return 60;
            case 50:
                return 150;
            case 61:
                return 120;
            case 62:
                return 90;
            case 71:
                return 80;
            case 81:
                return 60;
            case 91:
                return 30;
            case 92:
                return 30;
        }
        return 0;
    }

    public static List<String> GetSubjectKeyList() {
        List<String> keys = new ArrayList<>();

        for (String key : subjects.keySet()) {
            keys.add(key);
        }
        return keys;
    }

    public static Integer GetSubjectId(String subject) {
        for (Map.Entry<String, Integer> entry : subjects.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(subject)) {
                return entry.getValue();
            }
        }
        return -1;
    }

    public static String GetSubjectName(Integer subjectId) {
        for (Map.Entry<String, Integer> entry : subjects.entrySet()) {
            if (entry.getValue() == subjectId) {
                return entry.getKey();
            }
        }
        return "";
    }

    public static String GetDate() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return dateFormat.format(date);
    }

    // Function to return the next random number
    public static int getNum(ArrayList<Integer> v) {
        // Size of the vector
        int n = v.size();

        // Make sure the number is within
        // the index range
        int index = (int) (Math.random() * n);

        // Get random number from the vector
        int num = v.get(index);

        // Remove the number from the vector
        v.set(index, v.get(n - 1));
        v.remove(n - 1);

        // Return the removed number
        return num;
    }

    // Function to generate n
    // non-repeating random numbers
    public static ArrayList<Integer> generateRandom(int n) {
        ArrayList<Integer> v = new ArrayList<>(n);
        ArrayList<Integer> v_shuffle = new ArrayList<>(n);
        // Fill the vector with the values
        // 1, 2, 3, ..., n
        for (int i = 0; i < n; i++)
            v.add(i);

        for (int i = 0; i < n; i++)
            v_shuffle.add(getNum(v));

        return v_shuffle;
    }

    public static boolean isEmptyString(String text) {
        return (text == null ||
                text.trim().equals("null") ||
                text.trim().length() == 0);
    }
}