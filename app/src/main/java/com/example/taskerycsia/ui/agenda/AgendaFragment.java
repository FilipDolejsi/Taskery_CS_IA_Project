package com.example.taskerycsia.ui.agenda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.taskerycsia.R;
import com.example.taskerycsia.Task;
import com.example.taskerycsia.TaskCalendar;
import com.example.taskerycsia.databinding.FragmentAgendaBinding;

import java.util.ArrayList;

public class AgendaFragment extends Fragment {

    private FragmentAgendaBinding binding;
    ListView taskList;

    Button monday;
    Button tuesday;
    Button wednesday;
    Button thursday;
    Button friday;
    Button saturday;
    Button sunday;
    Button shareAgenda;
    TextView currentDay;
    TextView noTasks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAgendaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        taskList = binding.getRoot().findViewById(R.id.listTask);

        monday = root.findViewById(R.id.mondayBtn);
        tuesday = root.findViewById(R.id.tuesdayBtn);
        wednesday = root.findViewById(R.id.wednesdayBtn);
        thursday = root.findViewById(R.id.thursdayBtn);
        friday = root.findViewById(R.id.fridayBtn);
        saturday = root.findViewById(R.id.saturdayBtn);
        sunday = root.findViewById(R.id.sundayBtn);
        currentDay = root.findViewById(R.id.currentDayTextView);
        noTasks = root.findViewById(R.id.noTasksLeft);

        changeViewDay(0);
        monday.setOnClickListener(view -> {
            changeViewDay(0);
            currentDay.setText(R.string.mon);
        });
        tuesday.setOnClickListener(view -> {
            changeViewDay(1);
            currentDay.setText(R.string.tue);
        });
        wednesday.setOnClickListener(view -> {
            changeViewDay(2);
            currentDay.setText(R.string.wed);
        });
        thursday.setOnClickListener(view -> {
            changeViewDay(3);
            currentDay.setText(R.string.thu);
        });
        friday.setOnClickListener(view -> {
            changeViewDay(4);
            currentDay.setText(R.string.fri);
        });
        saturday.setOnClickListener(view -> {
            changeViewDay(5);
            currentDay.setText(R.string.sat);
        });
        sunday.setOnClickListener(view -> {
            changeViewDay(6);
            currentDay.setText(R.string.sun);
        });

        return root;
    }

    public void changeViewDay(int dayIndex) {
        ArrayList<Task> tasksOnDay = new ArrayList<>();
        ArrayList<Task> allTasks = TaskCalendar.CalendarData.getInstance().getTasks();
        for (Task t : allTasks) {
            if (t.getDate() == dayIndex) {
                tasksOnDay.add(t);
            }
        }
        AgendaAdapter calendarAdapter = new AgendaAdapter(this.requireActivity(), tasksOnDay);
        taskList.setAdapter(calendarAdapter);
        calendarAdapter.notifyDataSetChanged();
        if (tasksOnDay.isEmpty()) {
            noTasks.setText(R.string.there_are_no_tasks_today);
        } else {
            noTasks.setText("");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}