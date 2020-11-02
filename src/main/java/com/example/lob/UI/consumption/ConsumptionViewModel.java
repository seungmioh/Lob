package com.example.lob.UI.consumption;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConsumptionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConsumptionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is consumption");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
