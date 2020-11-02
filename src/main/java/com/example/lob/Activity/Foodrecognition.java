package com.example.lob.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lob.R;
import com.example.lob.Service.SocketClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.util.Arrays;
import java.util.List;

public class Foodrecognition extends AppCompatActivity {
    private String tempString   = null;
    private String inputString = null;
    private Button freceipt_caputure, freceipt_detect;
    private ImageView freceipt_image;
    private TextView freceipt_display;
    private SocketClient socketClient;
    static final int REQUEST_RECEiPT_IMAGE = 1;
    Handler handler;
    Runnable runnable;
    private Bitmap imageBitmap = null;

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    public String getInputString() {
        return inputString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodrecognition);
        freceipt_caputure = findViewById(R.id.freceipt_caputure);
        freceipt_image = findViewById(R.id.freceipt_image);
        freceipt_display = findViewById(R.id.freceipt_display);
        freceipt_display.setText("");
        freceipt_detect = findViewById(R.id.freceipt_detect);


        freceipt_caputure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTaskPictureIntent();
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                if(getInputString() != null){
                    SocketClient socketClient = new SocketClient(getInputString(), Foodrecognition.this);
                    socketClient.start();
                }
            }
        };

        freceipt_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          detectTextFromImage();
          handler= new Handler();
          handler.post(runnable);
            }
        });

    }
    private void dispatchTaskPictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_RECEiPT_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECEiPT_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            freceipt_image.setImageBitmap(imageBitmap);
        }
    }
    private  void detectTextFromImage() {
        tempString = new String();
        final FirebaseVisionImage firebaseVisionImage;
        firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                .getCloudImageLabeler();
        labeler.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                for(FirebaseVisionImageLabel label : labels){
                        tempString+=label.getText()+",";
                }
                Log.e("tempString",tempString);
                setInputString(tempString);
            }
        });
    }
}

