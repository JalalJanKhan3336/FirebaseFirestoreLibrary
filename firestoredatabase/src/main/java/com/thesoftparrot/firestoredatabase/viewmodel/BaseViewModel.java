package com.thesoftparrot.firestoredatabase.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.thesoftparrot.firestoredatabase.callbacks.DeleteStatusCallback;
import com.thesoftparrot.firestoredatabase.callbacks.FetchAllDocumentsCallback;
import com.thesoftparrot.firestoredatabase.callbacks.FileProgressStatusCallback;
import com.thesoftparrot.firestoredatabase.callbacks.FileUploadStatusCallback;
import com.thesoftparrot.firestoredatabase.callbacks.PojoObjectDeleteCallback;
import com.thesoftparrot.firestoredatabase.callbacks.PojoObjectUploadCallback;
import com.thesoftparrot.firestoredatabase.callbacks.ProgressStatusCallback;
import com.thesoftparrot.firestoredatabase.callbacks.SingleFetchStatusCallback;
import com.thesoftparrot.firestoredatabase.callbacks.UpdateStatusCallback;
import com.thesoftparrot.firestoredatabase.remote.Database;

import java.util.ArrayList;
import java.util.List;

public class BaseViewModel<T> extends ViewModel {

    private static final String TAG = "BaseViewModel";

    protected Database<T> mDatabase;
    private ProgressStatusCallback mProgressStatusCallback;
    private FileProgressStatusCallback mFileProgressStatusCallback;

    private MutableLiveData<T> mSingleItemLiveData;
    private MutableLiveData<List<T>> mAllItemListLiveData;

    public BaseViewModel(Context context) {
        mDatabase = new Database<>(context);
        this.mSingleItemLiveData = new MutableLiveData<>();
        this.mAllItemListLiveData = new MutableLiveData<>();
    }

    public BaseViewModel(Context context, ProgressStatusCallback mProgressStatusCallback, FileProgressStatusCallback mFileProgressStatusCallback) {
        mDatabase = new Database<>(context);
        this.mProgressStatusCallback = mProgressStatusCallback;
        this.mFileProgressStatusCallback = mFileProgressStatusCallback;
        this.mSingleItemLiveData = new MutableLiveData<>();
        this.mAllItemListLiveData = new MutableLiveData<>();
    }

    protected LiveData<T> getSingleItemLiveData() {
        if (mSingleItemLiveData == null)
            mSingleItemLiveData = new MutableLiveData<>();

        return mSingleItemLiveData;
    }

    protected LiveData<List<T>> getAllItemListLiveData() {
        if (mAllItemListLiveData == null)
            mAllItemListLiveData = new MutableLiveData<>();

        return mAllItemListLiveData;
    }

    /**
     * @param collection First Collection in FirebaseFirestore
     * @param document   First Document inside first Collection in FirebaseFirestore
     * @param item       Item of specified Type needs to be saved
     */
    protected LiveData<T> update(String collection, String document, T item) {

        mDatabase.update(collection, document, item, new UpdateStatusCallback<T>() {
            @Override
            public void onUpdateStatusSuccess(T updatedItem, String msg) {
                Log.d(TAG, "_onUpdateStatusSuccess: " + msg);
                mSingleItemLiveData.setValue(updatedItem);
                //updateProgressStatus(false, msg);
            }

            @Override
            public void onUpdateStatusFailure(String error) {
                Log.e(TAG, "_onUpdateStatusFailure: " + error);
                updateProgressStatus(true, error);
            }
        });

        return mSingleItemLiveData;
    }

    /**
     * @param collection    First Collection in FirebaseFirestore
     * @param document      First Document inside first Collection in FirebaseFirestore
     * @param subCollection Collection inside First Document in FirebaseFirestore
     * @param subDocument   Sub Document inside Sub Collection in FirebaseFirestore
     * @param item          Item of specified Type needs to be saved
     */
    protected LiveData<T> update(String collection, String document, String subCollection, String subDocument, T item) {
        mDatabase.update(collection, document, subCollection, subDocument, item, new UpdateStatusCallback<T>() {
            @Override
            public void onUpdateStatusSuccess(T updatedItem, String msg) {
                Log.d(TAG, "_onUpdateStatusSuccess: " + msg);
                mSingleItemLiveData.setValue(updatedItem);
                //updateProgressStatus(false, msg);
            }

            @Override
            public void onUpdateStatusFailure(String error) {
                Log.e(TAG, "_onUpdateStatusFailure: " + error);
                updateProgressStatus(true, error);
            }
        });

        return mSingleItemLiveData;
    }

    /**
     * @param collection First Collection in FirebaseFirestore
     * @param document   First Document inside first Collection in FirebaseFirestore
     * @param toClass    Class to Which result will be converted
     */
    protected LiveData<T> fetch(String collection, String document, Class<T> toClass) {
        mDatabase.fetchSingle(collection, document, new SingleFetchStatusCallback() {
            @Override
            public void onSingleFetchStatusSuccess(DocumentSnapshot documentSnapshot, String msg) {
                Log.d(TAG, "_onSingleFetchStatusSuccess: " + documentSnapshot.getId());
                T value = documentSnapshot.toObject(toClass);
                mSingleItemLiveData.setValue(value);

                //updateProgressStatus(false, msg);
            }

            @Override
            public void onSingleFetchStatusFailure(String error) {
                Log.e(TAG, "_onSingleFetchStatusFailure: " + error);
                updateProgressStatus(true, error);
            }
        });

        return mSingleItemLiveData;
    }

    /**
     * @param collection    First Collection in FirebaseFirestore
     * @param document      First Document inside first Collection in FirebaseFirestore
     * @param subCollection Collection inside First Document in FirebaseFirestore
     * @param subDocument   Sub Document inside Sub Collection in FirebaseFirestore
     * @param toClass       Class to Which result will be converted
     */
    protected void fetch(String collection, String document, String subCollection, String subDocument, Class<T> toClass, boolean isOfCurrentUser) {
        mDatabase.fetchSingle(collection, document, subCollection, subDocument, new SingleFetchStatusCallback() {
            @Override
            public void onSingleFetchStatusSuccess(DocumentSnapshot documentSnapshot, String msg) {
                Log.d(TAG, "_onSingleFetchStatusSuccess: " + documentSnapshot.getId());
                T value = documentSnapshot.toObject(toClass);
                mSingleItemLiveData.setValue(value);

                //updateProgressStatus(false, msg);
            }

            @Override
            public void onSingleFetchStatusFailure(String error) {
                Log.e(TAG, "_onSingleFetchStatusFailure: " + error);
                updateProgressStatus(true, error);
            }
        });
    }

    /**
     * @param collection First Collection in FirebaseFirestore
     * @param toClass    Class to Which result will be converted
     */
    protected LiveData<List<T>> fetchAll(String collection, Class<T> toClass) {
        List<T> list = new ArrayList<>();

        mDatabase.fetchAll(collection, new FetchAllDocumentsCallback() {
            @Override
            public void onAllDocumentsFetchSuccessful(List<DocumentSnapshot> documentSnapshotList, String msg) {
                Log.d(TAG, "_onAllDocumentsFetchSuccessful_Total_Documents: " + documentSnapshotList.size());

                for (DocumentSnapshot ds : documentSnapshotList) {
                    if (ds.exists()) {
                        T value = ds.toObject(toClass);
                        if (value != null)
                            list.add(value);
                    }
                }

                mAllItemListLiveData.setValue(list);
            }

            @Override
            public void onAllDocumentsFetchFailure(String error) {
                Log.e(TAG, "_onAllDocumentsFetchFailure: " + error);
            }
        });

        return mAllItemListLiveData;
    }

    /**
     * @param collection    First Collection in FirebaseFirestore
     * @param document      First Document inside first Collection in FirebaseFirestore
     * @param subCollection Collection inside First Document in FirebaseFirestore
     * @param toClass       Class to Which result will be converted
     */
    protected LiveData<List<T>> fetchAll(String collection, String document, String subCollection, Class<T> toClass) {
        List<T> list = new ArrayList<>();

        mDatabase.fetchAll(collection, document, subCollection, new FetchAllDocumentsCallback() {
            @Override
            public void onAllDocumentsFetchSuccessful(List<DocumentSnapshot> documentSnapshotList, String msg) {
                Log.d(TAG, "_onAllDocumentsFetchSuccessful_Total_Sub_Documents: " + documentSnapshotList.size());

                for (DocumentSnapshot ds : documentSnapshotList) {
                    if (ds.exists()) {
                        T value = ds.toObject(toClass);
                        if (value != null)
                            list.add(value);
                    }
                }

                mAllItemListLiveData.setValue(list);
            }

            @Override
            public void onAllDocumentsFetchFailure(String error) {
                Log.e(TAG, "_onAllDocumentsFetchFailure: " + error);
            }
        });

        return mAllItemListLiveData;
    }

    protected LiveData<List<T>> fetch2NestedNodesList(String collection, String subCollection, Class<T> toClass){
        mDatabase.fetchAll(collection, new FetchAllDocumentsCallback() {
            @Override
            public void onAllDocumentsFetchSuccessful(List<DocumentSnapshot> queryDocumentSnapshots, String msg) {
                List<T> list = new ArrayList<>();

                for(DocumentSnapshot outerDS : queryDocumentSnapshots){
                    if(outerDS != null && outerDS.exists()){
                        mDatabase.fetchAll(collection, outerDS.getId(), subCollection, new FetchAllDocumentsCallback() {
                            @Override
                            public void onAllDocumentsFetchSuccessful(List<DocumentSnapshot> innerDocumentsList, String msg) {
                                if(innerDocumentsList != null){
                                    for(DocumentSnapshot ds : innerDocumentsList){
                                        if(ds != null && ds.exists()){
                                            T data = ds.toObject(toClass);

                                            if(data != null && !list.contains(data)) {
                                                list.add(data);
                                                mAllItemListLiveData.setValue(list);
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onAllDocumentsFetchFailure(String error) {
                                Log.e(TAG, "_onAllDocumentsFetchFailure_innerDocumentsList: "+error);
                                updateProgressStatus(true, error);
                            }
                        });
                    }
                }

                updateProgressStatus(false, msg);
            }

            @Override
            public void onAllDocumentsFetchFailure(String error) {
                Log.d(TAG, "_onAllDocumentsFetchFailure_Outer: "+error);
                updateProgressStatus(true, error);
            }
        });

        return mAllItemListLiveData;
    }

    protected void remove(String collection, String document, PojoObjectDeleteCallback listener){
        mDatabase.deleteSingle(collection, document, new DeleteStatusCallback() {
            @Override
            public void onSingleDeleteSuccess(String msg) {
                Log.d(TAG, "_onDeleteSingleDocumentSuccess: "+msg);
                listener.onPojoObjectDeleteSuccess(msg);
            }

            @Override
            public void onSingleDeleteFailure(String error) {
                Log.e(TAG, "_onDeleteSingleDocumentFailed: "+error);
                listener.onPojoObjectDeleteFailed(error);
            }
        });
    }

    protected void uploadFile(String folder, String filename, String extension, String filepath, PojoObjectUploadCallback listener) {
        mDatabase.upload(folder, filename + extension, filepath, new FileUploadStatusCallback() {
            @Override
            public void onFileUploadStatusSuccess(String cloudPath, String msg) {
                Log.d(TAG, "_onFileUploadStatusSuccess: " + cloudPath);
                listener.onPojoObjectUploadSuccess(cloudPath, msg);
            }

            @Override
            public void onFileUploadStatusFailure(String error) {
                Log.e(TAG, "_onFileUploadStatusFailure: " + error);
                listener.onPojoObjectUploadFailed(error);
            }
        });
    }
/*
    protected void deleteFile(String folder, String filePath, PojoObjectDeleteCallback listener){
        mDatabase.removeFile(folder, filePath, new FileDeleteStatusCallback() {
            @Override
            public void onFileDeleteStatusSuccess(String msg) {
                Log.d(TAG, "_onFileDeleteStatusSuccess: "+msg);
                listener.onPojoObjectDeleteSuccess(msg);
            }

            @Override
            public void onFileDeleteStatusFailure(String error) {
                Log.e(TAG, "_onFileDeleteStatusFailure: "+error);
                listener.onPojoObjectDeleteFailed(error);
            }
        });
    }
*/
    // Update Progress Status either Success or Failure
    protected void updateProgressStatus(boolean isError, String msg) {
        Log.d("BaseViewModel", "_updateProgressStatus_Ref: " + mProgressStatusCallback);
        if (mProgressStatusCallback != null) {
            if (isError)
                mProgressStatusCallback.onProgressStateSuccess(msg);
            else
                mProgressStatusCallback.onProgressStateFailure(msg);
        }
    }

    // Update File Progress Status either Success or Failure
    protected void updateFileProgressStatus(boolean isError, String msg) {
        if (mFileProgressStatusCallback != null) {
            if (isError)
                mFileProgressStatusCallback.onFileProgressStateFailure(msg);
            else
                mFileProgressStatusCallback.onFileProgressStateSuccess(msg);
        }
    }

}
