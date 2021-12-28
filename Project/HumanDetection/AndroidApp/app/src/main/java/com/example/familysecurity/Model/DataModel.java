package com.example.familysecurity.Model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.familysecurity.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataModel {
    private final StorageReference rootref;
    private StorageReference spaceRef;
    private DocumentReference docRef;
    public DataModel(Context context) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        rootref = storage.getReference().getRoot();
        docRef = db.collection(context.getResources().getString(R.string.collection_id))
                    .document(context.getResources().getString(R.string.document_id));
        this.spaceRef = null;
    }

    public void getData(String path,
                        OnSuccessListener<byte[]> onSuccessListener,
                        OnFailureListener onFailureListener) {
        //Get Storage reference by URL
        spaceRef = rootref.child(path);
        Log.d("Path: ", spaceRef.getPath());
        //Download file and load file to memory, allow maximum
        //amount of memory to download is ONE_MEGABYTE:
        final long ONE_MEGABYTE = 1024 * 1024;
        spaceRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }



    public void downloadDataToLocal(String path,
                                    OnSuccessListener<FileDownloadTask.TaskSnapshot> onSuccessListener,
                                    OnFailureListener onFailureListener) throws IOException {
        //Get Storage reference by URL
        spaceRef = rootref.child(path);
        Log.d("Path: ", spaceRef.getPath());
        //Download file to local:
        File localFile = File.createTempFile("images", "png");
        spaceRef.getFile(localFile)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public boolean isDownloadInProgress() {
        if (spaceRef == null) {
            return false;
        }
        return true;
    }

    public void stopDownload() {
        this.spaceRef = null;
    }

    public String getReference() {
        return this.spaceRef.toString();
    }

    public void continueTask(Activity activity, String reference) {
        this.spaceRef = FirebaseStorage.getInstance().getReferenceFromUrl(reference);
        // Find all DownloadTasks under this StorageReference (in this example, there should be one)
        List<FileDownloadTask> tasks = this.spaceRef.getActiveDownloadTasks();
        if (tasks.size() > 0) {
            // Get the task monitoring the download
            FileDownloadTask task = tasks.get(0);

            // Add new listeners to the task using an Activity scope
            task.addOnSuccessListener(activity, new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot state) {
                    // Success!
                    Log.d("Continue task", "Continue successful!");
                }
            });
        }
    }

    public void getPath(EventListener<DocumentSnapshot> eventListener) {
        this.docRef.addSnapshotListener(eventListener);
    }
}
