package com.thesoftparrot.firestoredatabase.callbacks;

public interface ProgressStatusCallback {
    void onProgressStateSuccess(String msg);
    void onProgressStateFailure(String errorMsg);
}
