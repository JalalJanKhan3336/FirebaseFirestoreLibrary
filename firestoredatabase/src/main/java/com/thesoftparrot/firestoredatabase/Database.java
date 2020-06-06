package com.thesoftparrot.firestoredatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thesoftparrot.firestoredatabase.callbacks.FetchAllDocumentsCallback;
import com.thesoftparrot.firestoredatabase.callbacks.FileUploadStatusCallback;
import com.thesoftparrot.firestoredatabase.callbacks.SingleFetchStatusCallback;
import com.thesoftparrot.firestoredatabase.callbacks.UpdateStatusCallback;


import java.io.File;

public final class Database<T> {

    private Context mContext;
    private StorageReference mRootStorage;
    private FirebaseFirestore mRootDatabase;

    private static final String NO_INTERNET = "No Internet";

    public Database(Context context){
        mContext = context;
        mRootDatabase = FirebaseFirestore.getInstance();
        mRootStorage = FirebaseStorage.getInstance().getReference();
    }

    // Updating existing Data to Database
    public void update(String collection, String document, T data, UpdateStatusCallback<T> listener){

        if(!isConnectedToNetwork()){
            listener.onUpdateStatusFailure(NO_INTERNET);
            return;
        }

        mRootDatabase
                .collection(collection)
                .document(document)
                .set(data)
                .addOnSuccessListener(aVoid -> listener.onUpdateStatusSuccess(data, "Data updated successfully"))
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    listener.onUpdateStatusFailure(e.getMessage());
                });

    }

    // Updating existing Data to Database
    public void update(String collection, String document, String subCollection, String subDocument, T data, UpdateStatusCallback<T> listener){


        if(!isConnectedToNetwork()){
            listener.onUpdateStatusFailure(NO_INTERNET);
            return;
        }

        mRootDatabase
                .collection(collection)
                .document(document)
                .collection(subCollection)
                .document(subDocument)
                .set(data)
                .addOnSuccessListener(aVoid -> listener.onUpdateStatusSuccess(data, "Data updated successfully"))
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    listener.onUpdateStatusFailure(e.getMessage());
                });

    }

    // Fetching existing Data from Database
    public void fetchSingle(String collection, String document, SingleFetchStatusCallback listener){

        if(!isConnectedToNetwork()){
            listener.onSingleFetchStatusFailure(NO_INTERNET);
            return;
        }

        mRootDatabase
                .collection(collection)
                .document(document)
                .get()
                .addOnSuccessListener(documentSnapshot -> listener.onSingleFetchStatusSuccess(documentSnapshot, "Data Fetched Successfully"))
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    listener.onSingleFetchStatusFailure(e.getMessage());
                });

    }

    // Fetching existing Data from Database
    public void fetchSingle(String collection, String document, String subCollection, String subDocument, SingleFetchStatusCallback listener){

        if(!isConnectedToNetwork()){
            listener.onSingleFetchStatusFailure(NO_INTERNET);
            return;
        }

        mRootDatabase
                .collection(collection)
                .document(document)
                .collection(subCollection)
                .document(subDocument)
                .get()
                .addOnSuccessListener(documentSnapshot -> listener.onSingleFetchStatusSuccess(documentSnapshot, "Data Fetched Successfully"))
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    listener.onSingleFetchStatusFailure(e.getMessage());
                });

    }

    public void fetchAll(String collection, FetchAllDocumentsCallback listener){

        if(!isConnectedToNetwork()){
            listener.onAllDocumentsFetchFailure(NO_INTERNET);
            return;
        }

        mRootDatabase
                .collection(collection)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> listener.onAllDocumentsFetchSuccessful(queryDocumentSnapshots.getDocuments(), "Documents Fetched Successfully"))
                .addOnFailureListener(e -> listener.onAllDocumentsFetchFailure(e.getMessage()));
    }

    public void fetchAll(String collection, String document, String subCollection, FetchAllDocumentsCallback listener){

        if(!isConnectedToNetwork()){
            listener.onAllDocumentsFetchFailure(NO_INTERNET);
            return;
        }

        mRootDatabase
                .collection(collection)
                .document(document)
                .collection(subCollection)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> listener.onAllDocumentsFetchSuccessful(queryDocumentSnapshots.getDocuments(), "Documents Fetched Successfully"))
                .addOnFailureListener(e -> listener.onAllDocumentsFetchFailure(e.getMessage()));
    }

    // Uploading file to Database
    public void upload(String folder, String fileName, String filePath, FileUploadStatusCallback listener) {

        if(!isConnectedToNetwork()){
            listener.onFileUploadStatusFailure(NO_INTERNET);
            return;
        }

        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);

        mRootStorage
                .child(folder)
                .child(fileName)
                .putFile(uri)
                .addOnSuccessListener(taskSnapshot -> getFileCloudPath(taskSnapshot, listener))
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    listener.onFileUploadStatusFailure(e.getMessage());
                });
    }

    // retrieve cloud path from Database
    private void getFileCloudPath(UploadTask.TaskSnapshot taskSnapshot, FileUploadStatusCallback listener) {
        if(taskSnapshot != null){
            if(taskSnapshot.getMetadata() != null){
                if(taskSnapshot.getMetadata().getReference() != null){
                    taskSnapshot
                            .getMetadata()
                            .getReference()
                            .getDownloadUrl()
                            .addOnSuccessListener(uri -> listener.onFileUploadStatusSuccess(uri.toString(), "Uploaded Successfully"))
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                                listener.onFileUploadStatusFailure(e.getMessage());
                            });
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    public boolean isConnectedToNetwork(){
        boolean hasWIFI = false;
        boolean hasMobileData = false;

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo[] networkInfos = cm.getAllNetworkInfo();

            for (NetworkInfo info : networkInfos){
                if(info.getTypeName().equalsIgnoreCase("WIFI")){
                    if(info.isConnected()){
                        hasWIFI = true;
                    }
                }
                else if(info.getTypeName().equalsIgnoreCase("MOBILE")){
                    if(info.isConnected()){
                        hasMobileData = true;
                    }
                }
            }
        }

        return (hasMobileData || hasWIFI);
    }

}
