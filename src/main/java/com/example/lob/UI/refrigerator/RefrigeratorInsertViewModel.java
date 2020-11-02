package com.example.lob.UI.refrigerator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RefrigeratorInsertViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public RefrigeratorInsertViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is refrigerator");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
