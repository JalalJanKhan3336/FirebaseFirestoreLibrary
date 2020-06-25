package com.thesoftparrot.firestoredatabase.callbacks;

public interface FileProgressStatusCallback {
    void onFileProgressStateSuccess(String msg);
    void onFileProgressStateFailure(String errorMsg);
}
