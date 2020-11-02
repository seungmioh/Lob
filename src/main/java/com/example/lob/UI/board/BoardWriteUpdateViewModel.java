package com.example.lob.UI.board;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BoardWriteUpdateViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BoardWriteUpdateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is board");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
