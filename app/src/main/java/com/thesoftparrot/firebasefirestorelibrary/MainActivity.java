package com.thesoftparrot.firebasefirestorelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.thesoftparrot.firestoredatabase.Database;
import com.thesoftparrot.firestoredatabase.callbacks.SingleFetchStatusCallback;
import com.thesoftparrot.firestoredatabase.callbacks.UpdateStatusCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database<Profile> db = new Database<>(this);

        Profile profile = new Profile("userId_1","Jalal");

        db.update("Profile", profile.getUserId(), profile, new UpdateStatusCallback<Profile>() {
            @Override
            public void onUpdateStatusSuccess(Profile updatedItem, String msg) {
                Toast.makeText(MainActivity.this, "Success: "+updatedItem.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpdateStatusFailure(String error) {
                Toast.makeText(MainActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });

        db.fetchSingle("Profile", "userId_1", new SingleFetchStatusCallback() {
            @Override
            public void onSingleFetchStatusSuccess(com.google.firebase.firestore.DocumentSnapshot documentSnapshot, String msg) {
                Profile data = documentSnapshot.toObject(Profile.class);
                Toast.makeText(MainActivity.this, "Fetch Success "+data.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSingleFetchStatusFailure(String error) {

            }
        });

    }
}