package com.example.taskerycsia;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class TaskCalendar {

    public static class CalendarData implements Serializable {

        private static CalendarData INSTANCE;
        ArrayList<Task> tasks = new ArrayList<>();
        private ArrayList<Task> differentTasks = new ArrayList<>();
        float[] freetime = new float[2];
        double hoursUsed = 0;

        private CalendarData() throws IOException, ClassNotFoundException {
        }

        //thread safety singleton
        public static synchronized CalendarData getInstance() {
            if (INSTANCE == null) {
                try {
                    INSTANCE = new CalendarData();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            return INSTANCE;
        }

        public void removeTask(Task task) {
            differentTasks.remove(task);
        }

        public void removeTask() {
            differentTasks.clear();
            tasks.clear();
        }

        public void clearTasks() {
            tasks.clear();
        }

        public ArrayList<Task> getTasks() {
            return tasks;
        }

        public void setTask(int index, Task task) {
            differentTasks.set(index, task);
        }

        public void addTasks(Task task) {
            differentTasks.add(task);
        }

        public void addTasks(Task task, int index) {
            differentTasks.add(index, task);
        }

        public void sortTasks(ArrayList<Task> taskArrayList) {
            //insertion sort when one task changes
            int n = taskArrayList.size();
            for (int i = 1; i < n; ++i) {
                Task key = taskArrayList.get(i);
                int j = i - 1;
                while (j >= 0 && taskArrayList.get(i).getDur().timeToHours() > key.getDur().timeToHours()) {
                    taskArrayList.set(j + 1, taskArrayList.get(j));
                    j = j - 1;
                }
                taskArrayList.set(j + 1, key);
            }
        }

        private void sortTasks(ArrayList<Task> taskArrayList, int head, int tail) {
            if (head < tail) {
                int pi = partition(taskArrayList, head, tail);
                sortTasks(taskArrayList, head, pi - 1);
                sortTasks(taskArrayList, pi + 1, tail);
            }
        }

        private int partition(ArrayList<Task> taskArrayList, int head, int tail) {
            double pivot = taskArrayList.get(tail).getDur().timeToHours();
            int i = (head - 1);
            for (int j = head; j <= tail - 1; j++) {
                if (taskArrayList.get(j).getDur().timeToHours() < pivot) {
                    i++;
                    swap(taskArrayList, i, j);
                }
            }
            swap(taskArrayList, i + 1, tail);
            return (i + 1);
        }

        private void swap(ArrayList<Task> taskArrayList, int i, int j) {
            Task tempTask = taskArrayList.get(i);
            taskArrayList.set(i, taskArrayList.get(j));
            taskArrayList.set(j, tempTask);
        }

        public ArrayList<Task> removeUnNeededTasks(ArrayList<Task> taskList) {
            ArrayList<Task> taskArrayList = new ArrayList<>();
            Iterator<Task> taskItr = taskList.iterator();
            while (taskItr.hasNext()) {
                Task t = taskItr.next();
                if (t.isSameOrLess()) {
                    taskItr.remove();
                }
            }
            for (Task t : taskList) {

                if (!taskArrayList.contains(t)) {
                    taskArrayList.add(t);
                }
            }
            return new ArrayList<>(taskArrayList);
        }

        public void clearTasks(ArrayList<Task> tasksArrayList) {
            for (Task task :
                    tasksArrayList) {
                task.setAvailable(true);
            }
        }

        //load tasks
        public void loadData(FragmentActivity activity) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
            float sFreetime = sharedPreferences.getFloat("freetimeStart", 11);
            float eFreetime = sharedPreferences.getFloat("freetimeEnd", 17);

            setFreetime(sFreetime, eFreetime);

            Gson gson = new Gson();

            String json = sharedPreferences.getString("tasks", null);
            System.out.println("Loading...");
            System.out.println(json);

            Type type = new TypeToken<ArrayList<Task>>() {
            }.getType();

            differentTasks = (ArrayList<Task>) gson.fromJson(json, type);

            if (differentTasks == null) {
                differentTasks = new ArrayList<>();
            }
        }

        public void saveData(FragmentActivity activity) {
            // method for saving the data in array list.
            // creating a variable for storing data in
            // shared preferences.
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
            //SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.SP_MY_APPLICATION_SETTINGS, 0);
            // creating a variable for editor to
            // store data in shared preferences.
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.putFloat("freetimeStart", freetime[0]);
            editor.putFloat("freetimeEnd", freetime[1]);

            // creating a new variable for gson.
            Gson gson = new Gson();

            // getting data from gson and storing it in a string.
            String json = gson.toJson(differentTasks);
            System.out.println("Saving...");
            System.out.println(json);

            // below line is to save data in shared
            // prefs in the form of string.
            editor.putString("tasks", json);
            editor.apply();
            loadData(activity);
        }

        public void setTasks(ArrayList<Task> newTasks) {
            this.tasks = newTasks;
        }

        public void setTasks(float[] freetimes) {
            tasks.clear();
            for (int i = 0; i < differentTasks.size(); i++) {
                tasks.add(new Task(differentTasks.get(i).getDisplayName(), differentTasks.get(i).getDur(), differentTasks.get(i).getDescription()));
            }
            sortTasks(tasks, 0, tasks.size() - 1);
            Task tempTask;
            //for each week
            ArrayList<Task> tasksToAdd = new ArrayList<>();
            clearTasks(tasks);
//            while (splitting(freetimes)) {
//                for (Task t : tasks) {
//                    if (t.getDur().timeToHours() > getDifference(freetimes)) {
//                        tasksToAdd.add(t.split());
//                        sortTasks(tasks, 0, tasks.size() - 1);
//                    }
//                }
//            }
//            tasks.addAll(tasksToAdd);
//            Collections.shuffle(tasks);
            sortTasks(tasks, 0, tasks.size() - 1);
            for (int i = 0; i < 7; i++) {
                tasksToAdd = new ArrayList<>();
                double difference = getDifference(freetimes);
                hoursUsed = 0;
                for (Task t : tasks) {
                    if (t.getDur().timeToHours() <= difference && t.isEligible()) {
                        t.setStart(freetimes[0] + hoursUsed);
                        hoursUsed += t.getDur().timeToHours();
                        t.setEnd(t.getStart() + t.getDur().timeToHours());
                        difference = difference - t.getDur().timeToHours();
                        t.setDate(i);
                        System.out.println("(" + t.hashCode() + ") " + t.getDisplayName() + ": " + t.getDate() + ": " + t.getStart() + " " + t.getEnd());
                    } else if (t.isEligible() && difference > 0) {
                        tempTask = t.reduce(difference);
                        tempTask.setStart(freetimes[0] + hoursUsed);
                        hoursUsed += tempTask.getDur().timeToHours();
                        tempTask.setEnd(tempTask.getStart() + tempTask.getDur().timeToHInt());
                        difference = difference - tempTask.getDur().timeToHInt();
                        tempTask.setDate(i);
                        tasksToAdd.add(tempTask);
                        System.out.println("(" + tempTask.hashCode() + ") " + tempTask.getDisplayName() + ": " + tempTask.getDate() + ": " +
                                tempTask.getStart() + " " + tempTask.getEnd());
                    }
                }
                if (!tasksToAdd.isEmpty()) {
                    tasks.addAll(tasksToAdd);
                }
            }
            tasks = removeUnNeededTasks(tasks);
            merge(tasks);
        }

        private void merge(ArrayList<Task> taskArrayList) {
            ArrayList<Task> merged = new ArrayList<>();
            for (Task t :
                    taskArrayList) {
                for (Task tCompare :
                        taskArrayList) {
                    if (t.getDisplayName().equals(tCompare.getDisplayName()) &&
                            t.getEnd() == tCompare.getStart() &&
                            t.getDate() == tCompare.getDate()) {
                        t.setEnd(tCompare.getEnd());
                        t.setDuration(t.getDur().add(tCompare.getDur()));
                        merged.add(tCompare);
                    }
                }
            }
            taskArrayList.removeAll(merged);
        }

        public boolean compareHours() {
            double sum = 0;
            for (Task t : differentTasks) {
                sum += t.getDur().timeToHours();
            }
            return sum <= (getDifference(freetime) * 7);
        }

        private boolean splitting(float[] freetimes) {
            for (Task t : tasks) {
                if (t.getDur().timeToHours() > getDifference(freetimes)) {
                    return true;
                }
            }
            return false;
        }

        private double getDifference(float[] freetimes) {
            return freetimes[1] - freetimes[0];
        }

        public float[] getFreetime() {
            return freetime;
        }

        public ArrayList<Task> getDifferentTasks() {
            return differentTasks;
        }

        public void setFreetime(float[] freetime) {
            this.freetime = freetime;
        }

        public void setFreetime(float start, float end) {
            this.freetime[0] = start;
            this.freetime[1] = end;
        }
    }
}
