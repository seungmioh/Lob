package com.example.lob.UI.board;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BoardUpdateViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BoardUpdateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is board");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
