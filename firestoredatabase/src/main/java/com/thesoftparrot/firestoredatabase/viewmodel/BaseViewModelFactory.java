package com.thesoftparrot.firestoredatabase.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.thesoftparrot.firestoredatabase.callbacks.FileProgressStatusCallback;
import com.thesoftparrot.firestoredatabase.callbacks.ProgressStatusCallback;

public class BaseViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Context mContext;
    private ProgressStatusCallback mProgressStatusCallback;
    private FileProgressStatusCallback mFileProgressStatusCallback;

    public BaseViewModelFactory(Context mContext) {
        this.mContext = mContext;
        this.mProgressStatusCallback = null;
        this.mFileProgressStatusCallback = null;
    }

    public BaseViewModelFactory(Context mContext, ProgressStatusCallback mProgressStatusCallback,
                                FileProgressStatusCallback mFileProgressStatusCallback) {
        this.mContext = mContext;
        this.mProgressStatusCallback = mProgressStatusCallback;
        this.mFileProgressStatusCallback = mFileProgressStatusCallback;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BaseViewModel<T>(mContext, mProgressStatusCallback, mFileProgressStatusCallback);
    }
}
