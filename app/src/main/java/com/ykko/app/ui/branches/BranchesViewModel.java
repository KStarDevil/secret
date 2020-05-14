package com.ykko.app.ui.branches;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BranchesViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public BranchesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Branches fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
