package com.example.lob.UI.cooking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CookingViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public CookingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is cooking");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
