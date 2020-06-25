package com.thesoftparrot.firestoredatabase.callbacks;

public interface FileDeleteStatusCallback {
    void onFileDeleteStatusSuccess(String msg);
    void onFileDeleteStatusFailure(String error);
}
