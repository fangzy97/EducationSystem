package com.lepetit.schedule;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lepetit.leapplication.R;
import com.lepetit.schedulehelper.Schedule;

import org.w3c.dom.Text;

import butterknife.BindView;

public class ScheduleFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scheduleinfo, container, false);
        Bundle bundle = getArguments();

        TextView courseName = view.findViewById(R.id.course_name);
        courseName.setText(bundle.getString("course"));

        TextView teacherName = view.findViewById(R.id.teacher_name);
        teacherName.setText(bundle.getString("teacher"));

        TextView classTime = view.findViewById(R.id.class_time);
        classTime.setText(bundle.getString("time"));

        TextView mClassroom = view.findViewById(R.id.classroom);
        mClassroom.setText(bundle.getString("classroom"));

        return view;
    }
}
