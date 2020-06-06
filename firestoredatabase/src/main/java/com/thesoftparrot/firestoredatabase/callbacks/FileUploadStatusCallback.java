package com.thesoftparrot.firestoredatabase.callbacks;

public interface FileUploadStatusCallback {
    void onFileUploadStatusSuccess(String cloudPath, String msg);
    void onFileUploadStatusFailure(String error);
}
