package com.thesoftparrot.firestoredatabase.callbacks;

public interface PojoObjectDeleteCallback {
    void onPojoObjectDeleteSuccess(String msg);
    void onPojoObjectDeleteFailed(String error);
}
