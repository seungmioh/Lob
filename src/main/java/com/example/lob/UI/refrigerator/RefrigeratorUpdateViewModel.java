package com.example.lob.UI.refrigerator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RefrigeratorUpdateViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public RefrigeratorUpdateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is refrigerator");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
