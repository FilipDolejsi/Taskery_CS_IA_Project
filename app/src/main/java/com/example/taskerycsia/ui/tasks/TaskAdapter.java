package com.example.taskerycsia.ui.tasks;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.taskerycsia.R;
import com.example.taskerycsia.Task;
import com.example.taskerycsia.TaskCalendar;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends BaseAdapter {
    public final List<Task> tasks;
    final Context context;
    final LayoutInflater inflater;
    final Fragment fragment;

    public TaskAdapter(@NonNull Context context, ArrayList<Task> tasks, Fragment fragment) {
        this.context = context;
        this.tasks = tasks;
        this.inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int i) {
        return tasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return tasks.indexOf(tasks.get(i));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = inflater.inflate(R.layout.task_undetailed_row, null);
        TextView displayNameView = convertView.findViewById(R.id.displayName);
        displayNameView.setText(tasks.get(position).getDisplayName());
        displayNameView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("index", position);
            NavHostFragment.findNavController(fragment).navigate(R.id.action_nav_tasks_to_nav_taskDetail, bundle);
        });
        Button removeTask = convertView.findViewById(R.id.removeTaskButton);
        removeTask.setOnClickListener(view -> {
            tasks.remove(tasks.get(position));
            TaskCalendar.CalendarData.getInstance().clearTasks();
            this.notifyDataSetChanged();
        });
        return convertView;
    }
}
