package com.lepetit.grade;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lepetit.greendaohelper.GradeInfo;
import com.lepetit.leapplication.R;

import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> {

    private List<GradeInfo> gradeList;
    private Context context;

    public GradeAdapter(List<GradeInfo> gradeList) {
        this.gradeList = gradeList;
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
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView course;
        private TextView score;
        private TextView credit;

        ViewHolder(View itemView) {
            super(itemView);
            course = itemView.findViewById(R.id.grade_course);
            score = itemView.findViewById(R.id.grade_score);
            credit = itemView.findViewById(R.id.grade_credit);
        }
    }
}
