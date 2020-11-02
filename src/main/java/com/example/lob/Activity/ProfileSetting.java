package com.example.lob.Activity;
import com.example.lob.R;
import com.example.lob.Service.Storage;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lob.Service.SocketClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileSetting extends AppCompatActivity {
    Button setting_img, setting_withdrawl ,test_socket;
    TextView setting_userEmail;
    ImageView setting_profileview;
    private static final int PICK_FROM_ALBUM=1;
    public static Context CONTEXT;
    private Storage storage;
    private Uri userProfile = null;
    private FirebaseAuth googleAuth = FirebaseAuth.getInstance();
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();


    private FirebaseUser currentUser = googleAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        setting_img= findViewById(R.id.setting_img);
        CONTEXT=this;
        setting_userEmail=findViewById(R.id.setting_userEmail);
        setting_profileview= findViewById(R.id.setting_profileview);
        setting_withdrawl = findViewById(R.id.setting_withdrawl);
        test_socket= findViewById(R.id.test_socket);
        onStart();


        setting_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser!=null){
            setting_userEmail.setText(currentUser.getEmail().substring(0,currentUser.getEmail().lastIndexOf("@"))+"님");
            storageReference=firebaseStorage.getReference().child("/profile/"+currentUser.getUid());
            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        userProfile = task.getResult();
                        Glide.with(ProfileSetting.this)
                                .load(userProfile)
                                .apply(RequestOptions.circleCropTransform())
                                .into(setting_profileview);
                    }
                }
            });
}
        test_socket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }



    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Profile 설정");
        builder.setMessage("안녕하세요 프로필을 설정해주세요");
        builder.setPositiveButton("기본이미지",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storage=new Storage();
                        Uri drawablePath = getURLForResource(R.drawable.normal_profile);
                        storage.UploadProfile(drawablePath,currentUser.getUid());

                    }
                });

        builder.setNegativeButton("갤러리에서 선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent imgintent = new Intent(Intent.ACTION_PICK);
                        imgintent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(imgintent,PICK_FROM_ALBUM);

                    }
                });
        builder.show();
    }

    private Uri getURLForResource(int resId) {
        Resources resources = getResources();
        return     Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId) );
    }

    @Override
    public synchronized  void onResume() {
        super.onResume();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("ProgressDialog running...");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
        progressDialog.show();
        updateProfile();
        progressDialog.dismiss();

    }

    public  void updateProfile(){
        if(currentUser!=null){
            storageReference=firebaseStorage.getReference().child("/profile/"+currentUser.getUid());
            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        userProfile=task.getResult();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Storage storage = new Storage();
                    Uri drawablePath = getURLForResource(R.drawable.normal_profile);
                    storage.UploadProfile(drawablePath,currentUser.getUid());
                    onResume();
                }
            });

        }
    }
    public String getPath(Uri uri ){
        String [] proj ={MediaStore.Images.Media.DATA};
        Cursor cursor =null;
        int index;
        String name = null;
        CursorLoader cursorLoader= new CursorLoader(this,uri,proj,null,null,null);
        if(cursorLoader!=null) {
            cursor = cursorLoader.loadInBackground();
            index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            name=cursor.getString(index);
            cursor.close();
        }
        return name;
    }

    @Override
    public void startActivityForResult(@Nullable Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        switch (requestCode) {
            case PICK_FROM_ALBUM: { // 코드 일치
                // Uri
                Log.e("asdasdasdas", String.valueOf( getPath(intent.getData())));
                Log.e("asdasdasdasd",currentUser.getUid());
                storage=new Storage();
                storage.ModifyUpload(intent.getData(),currentUser.getUid());
            }
        }
    }

}