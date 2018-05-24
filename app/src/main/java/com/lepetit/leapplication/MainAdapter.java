package com.lepetit.leapplication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lepetit.exam.ExamFragment;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<String> list;
    private MainActivity activity;
    private Context context;

    MainAdapter(List<String> list, MainActivity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.main_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.examView.setOnClickListener((v) -> {
            activity.changeFragment(new ExamFragment(), R.string.Exam);
            activity.setExamChecked();
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String string = list.get(position);
        holder.textView.setText(string);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View examView;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            examView = itemView;
            textView = itemView.findViewById(R.id.exam_mon);
        }
    }
}
