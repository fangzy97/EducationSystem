package com.lepetit.exam;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lepetit.greendaohelper.ExamInfo;
import com.lepetit.leapplication.R;

import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {

    private List<ExamInfo> examList;

    public ExamAdapter(List<ExamInfo> list) {
        this.examList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exam_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExamInfo exam = examList.get(position);
        holder.course.setText(exam.getCourse());
        holder.time.setText(exam.getTime());
        String classroomAndSeat = exam.getClassroom() + "-" + exam.getSeat();
        holder.classroom.setText(classroomAndSeat);
    }

    @Override
    public int getItemCount() {
        return examList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView course;
        TextView time;
        TextView classroom;

        ViewHolder(View itemView) {
            super(itemView);
            course = itemView.findViewById(R.id.exam_course);
            time = itemView.findViewById(R.id.exam_time);
            classroom = itemView.findViewById(R.id.exam_classroom);
        }
    }
}
