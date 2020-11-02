package com.example.lob.Service;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Storage {

    public void  UploadProfile(Uri filePath, final String userId)  {
        FirebaseStorage storage;
        StorageReference storageReference;

        storage=FirebaseStorage.getInstance();
        Uri file = filePath;
        storageReference=storage.getReferenceFromUrl("gs://lobb-9ea28.appspot.com/")
                .child("profile/"+userId);
        UploadTask uploadTask=storageReference.putFile(file);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("asdasdad211111", String.valueOf(taskSnapshot));

            }
        });
    }

    public synchronized void ModifyUpload(final Uri filePath , final String userId){
        FirebaseStorage storage;
        StorageReference storageReference;
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference().child("profile/"+userId);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("슈!!!","슛ㅍ");
               Storage storage1=new Storage();
               storage1.UploadProfile(filePath , userId);

            }
        });
    }

    }



