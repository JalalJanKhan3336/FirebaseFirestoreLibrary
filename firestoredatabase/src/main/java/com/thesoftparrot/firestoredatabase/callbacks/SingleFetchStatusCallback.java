package com.thesoftparrot.firestoredatabase.callbacks;

import com.google.firebase.firestore.DocumentSnapshot;

public interface SingleFetchStatusCallback {
    void onSingleFetchStatusSuccess(DocumentSnapshot documentSnapshot, String msg);
    void onSingleFetchStatusFailure(String error);
}
