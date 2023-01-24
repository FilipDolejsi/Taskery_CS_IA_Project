package com.example.taskerycsia.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.taskerycsia.R;
import com.example.taskerycsia.Task;
import com.example.taskerycsia.TaskCalendar;
import com.example.taskerycsia.databinding.FragmentHomeBinding;
import com.example.taskerycsia.ui.agenda.AgendaAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ListView taskList;
    TextView currentDayLabel;
    SimpleDateFormat nameOfDayFormatter;

    Date currentDate = new Date();
    boolean once = true;
    float[] freetimes = TaskCalendar.CalendarData.getInstance().getFreetime();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SharedPreferences mPrefs = this.getActivity().getSharedPreferences("taskPreferences", MODE_PRIVATE);

        taskList = root.findViewById(R.id.listTask);
        currentDayLabel = root.findViewById(R.id.currentDay);
        SimpleDateFormat numberOfDayFormatter = new SimpleDateFormat("dd", Locale.ENGLISH);
        nameOfDayFormatter = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        String dateFormat = nameOfDayFormatter.format(currentDate) + " " + numberOfDayFormatter.format(currentDate);
        currentDayLabel.setText(dateFormat);
        if (once) {
            TaskCalendar.CalendarData.getInstance().setTasks(freetimes);
            once = false;
        }
        AgendaAdapter calendarAdapter = new AgendaAdapter(this.requireActivity(), setTodayList());
        taskList.setAdapter(calendarAdapter);
        calendarAdapter.notifyDataSetChanged();
        return root;
    }

    private ArrayList<Task> setTodayList() {
        ArrayList<Task> tasksOnDay = new ArrayList<>();
        ArrayList<Task> allTasks = TaskCalendar.CalendarData.getInstance().getTasks();
        for (Task t : allTasks) {
            if (t.getDate() == findDayIndex()) {
                tasksOnDay.add(t);
            }
        }
        return tasksOnDay;
    }

    private int findDayIndex() {
        switch (nameOfDayFormatter.format(currentDate)) {
            case "Monday":
                return 0;
            case "Tuesday":
                return 1;
            case "Wednesday":
                return 2;
            case "Thursday":
                return 3;
            case "Friday":
                return 4;
            case "Saturday":
                return 5;
            case "Sunday":
                return 6;
            default:
                return -1;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
