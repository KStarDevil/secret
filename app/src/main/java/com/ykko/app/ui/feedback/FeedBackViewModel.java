package com.ykko.app.ui.feedback;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeedBackViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public FeedBackViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is feedback fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
