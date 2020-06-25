package com.thesoftparrot.firestoredatabase.callbacks;

public interface PojoObjectUploadCallback {
    void onPojoObjectUploadSuccess(String cloudPath, String msg);
    void onPojoObjectUploadFailed(String error);
}
