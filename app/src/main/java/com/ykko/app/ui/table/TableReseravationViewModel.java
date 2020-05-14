package com.ykko.app.ui.table;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TableReseravationViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public TableReseravationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Table reservation fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
