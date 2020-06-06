package com.thesoftparrot.firestoredatabase.callbacks;

public interface UpdateStatusCallback<T> {
    void onUpdateStatusSuccess(T updatedItem, String msg);
    void onUpdateStatusFailure(String error);
}
