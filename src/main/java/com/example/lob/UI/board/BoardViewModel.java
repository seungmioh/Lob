package com.example.lob.UI.board;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BoardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BoardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is board");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
