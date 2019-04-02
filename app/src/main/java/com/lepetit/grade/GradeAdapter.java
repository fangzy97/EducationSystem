package com.lepetit.grade;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lepetit.gradehelper.GetGradeAnalyzeInfo;
import com.lepetit.greendaohelper.GradeInfo;
import com.lepetit.leapplication.R;

import java.util.List;
import java.util.Map;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> {

    private List<GradeInfo> gradeList;
    private Context context;
    private Activity activity;
    private int opened = -1;

    GradeAdapter(List<GradeInfo> gradeList, Activity activity) {
        this.gradeList = gradeList;
        this.activity = activity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.grade_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GradeInfo info = gradeList.get(position);
        holder.course.setText(info.getCourse());
        String score = "成绩：" + info.getScore();
        holder.score.setText(score);
        String credit = "学分：" + info.getCredit();
        holder.credit.setText(credit);

        new Thread(() -> {
            String analyze = info.getAnalyze();
            Map<String, String> infos = GetGradeAnalyzeInfo.get(analyze);

            if (infos.size() > 0) {
                activity.runOnUiThread(() -> {
                    holder.class_number.setText(infos.get("class_number"));
                    holder.subject_number.setText(infos.get("subject_number"));
                    holder.learn_number.setText(infos.get("learn_number"));
                    holder.ave_grade.setText(infos.get("ave_grade"));
                    holder.max_grade.setText(infos.get("max_grade"));
                    holder.class_percent.setText(infos.get("class_percent"));
                    holder.subject_percent.setText(infos.get("subject_percent"));
                    holder.learn_percent.setText(infos.get("learn_percent"));
                });
            }
        }).start();

        if (position == opened) {
            holder.gradeAnalyzeLayout.setVisibility(View.VISIBLE);
        }
        else {
            holder.gradeAnalyzeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView course;
        private TextView score;
        private TextView credit;
        private TextView class_number;
        private TextView subject_number;
        private TextView learn_number;
        private TextView ave_grade;
        private TextView max_grade;
        private TextView class_percent;
        private TextView subject_percent;
        private TextView learn_percent;
        private LinearLayout gradeMainLayout;
        private LinearLayout gradeAnalyzeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            course = itemView.findViewById(R.id.grade_course);
            score = itemView.findViewById(R.id.grade_score);
            credit = itemView.findViewById(R.id.grade_credit);
            class_number = itemView.findViewById(R.id.class_number);
            subject_number = itemView.findViewById(R.id.subject_number);
            learn_number = itemView.findViewById(R.id.learn_number);
            ave_grade = itemView.findViewById(R.id.ave_grade);
            max_grade = itemView.findViewById(R.id.max_grade);
            class_percent = itemView.findViewById(R.id.class_percent);
            subject_percent = itemView.findViewById(R.id.subject_percent);
            learn_percent = itemView.findViewById(R.id.learn_percent);
            gradeAnalyzeLayout = itemView.findViewById(R.id.grade_analyze_layout);
            gradeMainLayout = itemView.findViewById(R.id.grade_main_layout);
            gradeMainLayout.setOnClickListener(this::onClick);
        }

        void onClick(View view) {
            if (opened == getAdapterPosition()) {
                opened = -1;
                notifyItemChanged(getAdapterPosition());
            }
            else {
                int oldOpened = opened;
                opened = getAdapterPosition();
                notifyItemChanged(oldOpened);
                notifyItemChanged(opened);
            }
        }
    }
}
