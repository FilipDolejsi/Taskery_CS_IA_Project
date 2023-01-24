package com.example.taskerycsia.ui.tasks;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.taskerycsia.R;
import com.example.taskerycsia.Task;
import com.example.taskerycsia.TaskCalendar;
import com.example.taskerycsia.Time;
import com.example.taskerycsia.databinding.FragmentTasksBinding;

import java.util.ArrayList;
import java.util.Locale;

public class TaskFragment extends Fragment {

    private FragmentTasksBinding binding;
    ListView taskList;
    TextView noTasks;
    View root;
    Button clearTasks;
    Button startFreetime;
    Button endFreetime;
    int hour, minute;
    float[] freetimes = TaskCalendar.CalendarData.getInstance().getFreetime();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = view.getRootView();

        startFreetime = root.findViewById(R.id.freeTimeStartBtn);
        startFreetime.setText(new Time(freetimes[0], Time.findMinute(freetimes[0])).toString());
        startFreetime.setOnClickListener(view1 -> showTimePicker(startFreetime, 0));
        endFreetime = root.findViewById(R.id.freeTimeEndBtn);
        endFreetime.setText(new Time(freetimes[1], Time.findMinute(freetimes[1])).toString());
        endFreetime.setOnClickListener(view1 -> showTimePicker(endFreetime, 1));

        noTasks = root.findViewById(R.id.noTasks);
        Button addTaskButton = root.findViewById(R.id.addTask);
        addTaskButton.setOnClickListener(view12 -> goToTaskDetail());
        ArrayList<Task> differentTasks = TaskCalendar.CalendarData.getInstance().getDifferentTasks();
        TaskAdapter calendarAdapter = new TaskAdapter(this.requireActivity(), differentTasks, this);
        taskList = binding.getRoot().findViewById(R.id.taskList1);
        taskList.setAdapter(calendarAdapter);
        if (calendarAdapter.isEmpty()) {
            noTasks.setVisibility(View.VISIBLE);
            goToTaskDetail();
        } else {
            noTasks.setVisibility(View.INVISIBLE);
        }

        taskList.setOnItemClickListener((adapterView, view1, i, l) -> {
            taskList.getItemAtPosition(i);
            goToTaskDetail(i);
        });
        calendarAdapter.notifyDataSetChanged();
        clearTasks = root.findViewById(R.id.clearTasks);
        clearTasks.setOnClickListener(view1 -> {
            TaskCalendar.CalendarData.getInstance().removeTask();
            calendarAdapter.notifyDataSetChanged();
        });
    }

    private void setFreetimes(int original) {
        if (original == 0) {
            float before = freetimes[0];
            String[] freetimeStart = startFreetime.getText().toString().split(":");
            freetimes[0] = Integer.parseInt(freetimeStart[0])+Time.minuteToHour((float) Integer.parseInt(freetimeStart[1]));
            if (freetimes[0] >= freetimes[1]) {
                freetimes[0] = before;
                Toast.makeText(getContext(), "Your start time is greater than your end time", Toast.LENGTH_SHORT).show();
            } else if (!TaskCalendar.CalendarData.getInstance().compareHours()) {
                freetimes[0] = before;
                Toast.makeText(getContext(), "Your free-time must be longer than the total duration", Toast.LENGTH_LONG).show();
            }
            //todo:make so that minutes matter
            startFreetime.setText(new Time(freetimes[0], Time.findMinute(freetimes[0])).toString());
        } else {
            float before = freetimes[1];
            String[] freetimeEnd = endFreetime.getText().toString().split(":");
            freetimes[1] = Integer.parseInt(freetimeEnd[0])+Time.minuteToHour((float) Integer.parseInt(freetimeEnd[1]));
            if (freetimes[1] <= freetimes[0]) {
                freetimes[1] = before;
                Toast.makeText(getContext(), "Your end time is shorter than your start time", Toast.LENGTH_SHORT).show();
            } else if (!TaskCalendar.CalendarData.getInstance().compareHours()) {
                freetimes[1] = before;
                Toast.makeText(getContext(), "Your free-time must be longer than the total duration", Toast.LENGTH_SHORT).show();
            }
            endFreetime.setText(new Time(freetimes[1], Time.findMinute(freetimes[1])).toString());
        }

        TaskCalendar.CalendarData.getInstance().setFreetime(freetimes);
        TaskCalendar.CalendarData.getInstance().setTasks(freetimes);
    }

    public void showTimePicker(Button selectedButton, int index) {
        String[] buttonText = selectedButton.getText().toString().split(":");
        int currentHour = Integer.parseInt(buttonText[0]);
        int currentMinute = Integer.parseInt(buttonText[1]);
        TimePickerDialog timeSpinner = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, (timePicker, i, i1) -> {
            hour = i;
            minute = i1;
            selectedButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            setFreetimes(index);
        }, currentHour, currentMinute, true);
        timeSpinner.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timeSpinner.updateTime(currentHour, currentMinute);
        if(index==0){
            timeSpinner.setTitle("Select start time:");
        } else {
            timeSpinner.setTitle("Select end time:");
        }
        timeSpinner.show();
    }

    private void goToTaskDetail(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_tasks_to_nav_taskDetail, bundle);
    }

    private void goToTaskDetail() {
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_tasks_to_nav_taskDetail);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}