package com.example.caller.ui.callog;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CallogViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CallogViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is call log fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
