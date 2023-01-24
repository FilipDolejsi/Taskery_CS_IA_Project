package com.example.taskerycsia.ui.taskDetail;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.taskerycsia.R;
import com.example.taskerycsia.Task;
import com.example.taskerycsia.TaskCalendar;
import com.example.taskerycsia.Time;
import com.example.taskerycsia.databinding.FragmentTaskDetailBinding;

import java.util.Locale;

public class TaskDetailFragment extends Fragment {

    EditText nameVal;
    Button durationVal;
    TextView errorLabel;
    TextView descriptionLabel;
    View root;
    FragmentTaskDetailBinding binding;
    int hour, minute;
    int index;
    float[] freetimes = TaskCalendar.CalendarData.getInstance().getFreetime();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt("index");
        } else {
            index = -1;
        }
        root = view.getRootView();
        Button saveBtn = (Button) root.findViewById(R.id.save_button);
        Button removeBtn = (Button) root.findViewById(R.id.remove_button);
        errorLabel = root.findViewById(R.id.errorText);
        descriptionLabel = root.findViewById(R.id.descriptionText);

        Button exitButton = root.findViewById(R.id.back_button);
        // Create new fragment and transaction
        exitButton.setOnClickListener(view14 -> moveOn());
        saveBtn.setOnClickListener(view13 -> saveTask());
        removeBtn.setOnClickListener(view12 -> removeTask());

        nameVal = root.findViewById(R.id.taskNameText);
        durationVal = root.findViewById(R.id.durationText);

        durationVal.setOnClickListener(view1 -> showTimePicker(durationVal));
        if (index != -1) {
            nameVal.setText(TaskCalendar.CalendarData.getInstance().getDifferentTasks().get(index).getDisplayName());
            durationVal.setText(TaskCalendar.CalendarData.getInstance().getDifferentTasks().get(index).getDur().toString());
        }
    }

    public void showTimePicker(Button selectedButton) {
        TimePickerDialog timeSpinner = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hour = i;
                minute = i1;
                selectedButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        }, 0, 0, true);
        timeSpinner.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timeSpinner.show();
    }

    private void removeTask() {
        if (index != -1) {
            TaskCalendar.CalendarData.getInstance().removeTask(TaskCalendar.CalendarData.getInstance().getDifferentTasks().get(index));
        }
        moveOn();
    }

    public void saveTask() {
        String name = nameVal.getText().toString();
        String[] durTime = durationVal.getText().toString().split(":");
        Time duration = new Time(Integer.parseInt(durTime[0]), Integer.parseInt(durTime[1]));
        String description = descriptionLabel.getText().toString();
        Task task = new Task(name, duration, description);
        if (index == -1) {
            TaskCalendar.CalendarData.getInstance().addTasks(task);
            if (!name.equals("") &&
                    !durationVal.getText().toString().equals("")
                    && TaskCalendar.CalendarData.getInstance().compareHours()) {
                moveOn();
            } else if (nameVal.getText().toString().equals("")) {
                Toast.makeText(getContext(), "The name cannot be blank", Toast.LENGTH_SHORT).show();
                TaskCalendar.CalendarData.getInstance().removeTask(task);
            } else {
                Toast.makeText(getContext(), "The total duration must be shorter than your free-time", Toast.LENGTH_SHORT).show();
                TaskCalendar.CalendarData.getInstance().removeTask(task);
            }
        } else {
            Task t = TaskCalendar.CalendarData.getInstance().getDifferentTasks().get(index);
            TaskCalendar.CalendarData.getInstance().setTask(index, task);
            if (!name.equals("") &&
                    !durationVal.getText().toString().equals("")
                    && TaskCalendar.CalendarData.getInstance().compareHours()) {
                moveOn();
            } else if (nameVal.getText().toString().equals("")) {
                TaskCalendar.CalendarData.getInstance().setTask(index, t);
                Toast.makeText(getContext(), "The name cannot be blank", Toast.LENGTH_SHORT).show();
            } else {
                TaskCalendar.CalendarData.getInstance().setTask(index, t);
                Toast.makeText(getContext(), "The total duration must be shorter than your free-time", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void moveOn() {
        TaskCalendar.CalendarData.getInstance().setTasks(freetimes);
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_taskDetail_to_nav_tasks);
    }

    public void addTask(Task t) {
        TaskCalendar.CalendarData.getInstance().addTasks(t);
    }
}