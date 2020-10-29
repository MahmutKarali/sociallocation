package com.application.atplexam.Utility;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.atplexam.Model.Exam;
import com.application.atplexam.R;

import java.util.List;

public class ExamListAdapter extends BaseAdapter {

    private final Activity context;
    private final List<Exam> exams;
    private LayoutInflater inflater;
    private ExamViewHolder holder;

    public ExamListAdapter(Activity context, List<Exam> exams) {
        this.context = context;
        this.exams = exams;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return exams.size();
    }

    @Override
    public Exam getItem(int position) {
        return exams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {

            view = inflater.inflate(R.layout.exam_item_view, null);
            holder = new ExamViewHolder();

            holder.txt_exam_name = view.findViewById(R.id.txt_exam_name);
            holder.iv_color = view.findViewById(R.id.iv_color);
            holder.time_icon = view.findViewById(R.id.time_icon);
            holder.txt_subject = view.findViewById(R.id.txt_subject);
            holder.txt_total_time = view.findViewById(R.id.txt_total_time);
            holder.txt_status = view.findViewById(R.id.txt_status);
            holder.txt_total_question_count = view.findViewById(R.id.txt_total_question_count);
            view.setTag(holder);
        } else {
            holder = (ExamViewHolder) view.getTag();
        }

        Exam exam = exams.get(position);

        holder.txt_exam_name.setText(exam.name);
        holder.txt_subject.setText(Helper.GetSubjectName(exam.subjectid));
        holder.txt_total_question_count.setText("Total Question Count : " + exam.qscount.toString());

        if (exams.get(position).mode == 1) {
            holder.iv_color.setBackgroundColor(Color.parseColor("#D4AF37"));
        }

        if (exams.get(position).timer == 1) {
            holder.time_icon.setVisibility(View.VISIBLE);
            int min = exam.remainingtime / 60;
            int second = 0;

            holder.txt_total_time.setText("Time : " + min + " min");

            if (min * 60 < exam.remainingtime) {
                second = exam.remainingtime - min * 60;
                holder.txt_total_time.setText("Time : " + min + " min " + second + " sec");
            }
        } else {
            holder.txt_total_time.setText("No time restriction");
        }

        if (exam.isEnded == 1) {
            holder.txt_status.setText("Completed");
            holder.txt_status.setTextColor(Color.parseColor("#469c33"));
        } else if (exam.answercount == 0) {
            holder.txt_status.setText("Not Started");
            holder.txt_status.setTextColor(Color.RED);
        } else {
            holder.txt_status.setText("Continue");
            holder.txt_status.setTextColor(Color.BLUE);
        }

        return view;
    }

    public static class ExamViewHolder {
        private TextView txt_exam_name;
        private ImageView iv_color;
        private ImageView time_icon;
        private TextView txt_subject;
        private TextView txt_total_time;
        private TextView txt_status;
        private TextView txt_total_question_count;
    }
}