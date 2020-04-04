package com.example.e_authentication.Activities.ui.send;

import android.content.Intent;

import com.example.e_authentication.Activities.LoginActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SendViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SendViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }
}