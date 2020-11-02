package com.example.lob.UI.setting;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.CursorLoader;

import com.example.lob.DTO.FoodDTO;
import com.example.lob.Dialog.FoodDialog;
import com.example.lob.R;
import com.example.lob.Activity.Receiptrecognition;
import com.example.lob.Service.Storage;
import com.example.lob.Service.SocketClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SettingFragment extends Fragment {
    private Storage storage;
    private FirebaseAuth googleAuth = null;
    public static Context CONTEXT;
    View root;
    Handler handler;
    Runnable runnable, runnable2, runnable3;
    private FirebaseUser currentUser = null;
    private String pathUri;
    private static final int PICK_FROM_ALBUM = 1;
    private Uri imageUri;
    Button settingButton_userImg;
    Button testet;
    Button setting_socket;
    SocketClient socketClient;
    String input = "";
    String result_string[] = null;
    FoodDialog foodDialog;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        googleAuth = FirebaseAuth.getInstance();
        currentUser = googleAuth.getCurrentUser();
        CONTEXT = this.getContext();
        ViewModelProviders.of(this).get(SettingViewModel.class);
        root = inflater.inflate(R.layout.fragment_setting, container, false);
        setting_socket = root.findViewById(R.id.setting_socket);
        testet = root.findViewById(R.id.testtest);
        testet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent asdasd = new Intent(root.getContext(), Receiptrecognition.class);
                startActivity(asdasd);
            }
        });
        settingButton_userImg = root.findViewById(R.id.settingButton_userImg);
        settingButton_userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    show();
                }
            }
        });

        runnable3 = new Runnable() {
            @Override
            public void run() {
                input = "";
                ArrayList<String> a = new ArrayList<String>();
                a.add("과자");
                a.add("하이고 왤캐 어렵냐");
                a.add("김치");
                for (int i = 0; i < a.size(); i++) {
                    input = input + a.get(i) + ",";
                    Log.e("input is ", input);
                }
                SocketClient socketClient = new SocketClient(input, getContext());
                socketClient.start();
            }
        };
        setting_socket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler = new Handler();
                handler.postDelayed(runnable3, 1000);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PICK_FROM_ALBUM: { // 코드 일치
                // Uri
                Log.e("asdasdasdas", String.valueOf(getPath(data.getData())));
                Log.e("asdasdasdasd", currentUser.getUid());
                storage = new Storage();
                storage.ModifyUpload(data.getData(), currentUser.getUid());
            }
        }
    }

    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        int index;
        String name = null;
        CursorLoader cursorLoader = new CursorLoader(this.getContext(), uri, proj, null, null, null);
        if (cursorLoader != null) {
            cursor = cursorLoader.loadInBackground();
            index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            name = cursor.getString(index);
            cursor.close();
        }
        return name;
    }


    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Profile 설정");
        builder.setMessage("안녕하세요 프로필을 설정해주세요");
        builder.setPositiveButton("기본이미지",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storage = new Storage();
                        Uri drawablePath = getURLForResource(R.drawable.normal_profile);
                        storage.UploadProfile(drawablePath, currentUser.getUid());

                    }
                });

        builder.setNegativeButton("갤러리에서 선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent imgintent = new Intent(Intent.ACTION_PICK);
                        imgintent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(imgintent, PICK_FROM_ALBUM);
                    }
                });
        builder.show();
    }

    private Uri getURLForResource(int resId) {
        Resources resources = CONTEXT.getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId));
    }
}