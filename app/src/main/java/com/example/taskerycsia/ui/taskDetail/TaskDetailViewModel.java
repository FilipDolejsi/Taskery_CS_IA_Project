package com.example.taskerycsia.ui.taskDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TaskDetailViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public TaskDetailViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
