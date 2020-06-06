package com.thesoftparrot.firestoredatabase.callbacks;

public interface AdditionStatusCallback {
    void onAdditionStatusSuccess(String msg);
    void onAdditionStatusFailure(String error);
}
