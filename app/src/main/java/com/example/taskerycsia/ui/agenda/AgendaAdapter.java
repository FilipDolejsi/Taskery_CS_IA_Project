package com.example.taskerycsia.ui.agenda;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.taskerycsia.R;
import com.example.taskerycsia.Task;
import com.example.taskerycsia.TaskCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AgendaAdapter extends BaseAdapter {
    public final List<Task> tasks;
    final Context context;
    final LayoutInflater inflater;

    public AgendaAdapter(@NonNull Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        this.inflater = LayoutInflater.from(context);
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
        convertView = inflater.inflate(R.layout.task_row, null);
        TextView displayNameView = convertView.findViewById(R.id.displayName);
        displayNameView.setText(tasks.get(position).getDisplayName());
        TextView descriptionView = convertView.findViewById(R.id.descriptionName);
        descriptionView.setText(tasks.get(position).getDescription());
        TextView durationView = convertView.findViewById(R.id.durationText);
        durationView.setText(tasks.get(position).timeToString());
        Button shareAgenda = convertView.findViewById(R.id.shareTaskButton);
        shareAgenda.setOnClickListener(view -> {
            ArrayList<Task> tasksArray = TaskCalendar.CalendarData.getInstance().getTasks();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            Date date = new Date();
            String day = formatter.format(date);

            Task t = tasks.get(position);

            String startTimeFormat = t.getExtDate().substring(0, 11) +
                    t.getStartTime() + ":00";
            String endTimeFormat = t.getExtDate().substring(0, 11) +
                    t.getEndTime() + ":00";

            Date startTime = null;
            Date endTime = null;

            try {
                startTime = formatter.parse(startTimeFormat);
                endTime = formatter.parse(endTimeFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //export events to google calendar
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.TITLE, t.getDisplayName());
            intent.putExtra(CalendarContract.Events._ID, t.getTaskID());
            intent.putExtra(CalendarContract.Events.DESCRIPTION, t.getDescription());
            intent.putExtra(CalendarContract.Events.ALL_DAY, "false");
            assert startTime != null;
            intent.putExtra("beginTime", startTime.getTime());
            assert endTime != null;
            intent.putExtra("endTime", endTime.getTime());

            context.startActivity(intent);

        });

        return convertView;
    }
}