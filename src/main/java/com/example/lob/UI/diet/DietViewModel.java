package com.example.lob.UI.diet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DietViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DietViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is diet");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
