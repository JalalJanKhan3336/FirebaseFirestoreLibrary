package com.thesoftparrot.firestoredatabase.callbacks;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface FetchAllDocumentsCallback {
    void onAllDocumentsFetchSuccessful(List<DocumentSnapshot> queryDocumentSnapshots, String msg);
    void onAllDocumentsFetchFailure(String error);
}
