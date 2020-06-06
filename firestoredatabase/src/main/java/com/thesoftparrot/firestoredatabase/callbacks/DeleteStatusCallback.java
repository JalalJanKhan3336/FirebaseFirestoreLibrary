package com.thesoftparrot.firestoredatabase.callbacks;

public interface DeleteStatusCallback {
    void onSingleDeleteSuccess(String msg);
    void onSingleDeleteFailure(String error);
}
