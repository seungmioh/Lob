package com.example.lob.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lob.DTO.FoodDTO;
import com.example.lob.Dialog.FoodDialog;
import com.example.lob.R;
import com.example.lob.Service.SocketClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Receiptrecognition extends AppCompatActivity {
    private String tempString   = null;
    private String inputString = null;
    private Button receipt_caputure, receipt_detect;
    private ImageView receipt_image;
    private TextView receipt_display;
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
        setContentView(R.layout.activity_receiptrecognition);
        receipt_caputure = findViewById(R.id.receipt_caputure);
        receipt_image = findViewById(R.id.receipt_image);
        receipt_display = findViewById(R.id.receipt_display);
        receipt_display.setText("");
        receipt_detect = findViewById(R.id.receipt_detect);

        runnable = new Runnable() {
            @Override
            public void run() {
                if(getInputString() != null){
                    SocketClient socketClient = new SocketClient(getInputString(), Receiptrecognition.this);
                    socketClient.start();
                }
            }
        };

        receipt_caputure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTaskPictureIntent();
                handler = new Handler();
                handler.post(runnable);
            }
        });


        receipt_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          detectTextFromImage();
          handler = new Handler();
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
            receipt_image.setImageBitmap(imageBitmap);
        }

    }

    private  void detectTextFromImage() {
        tempString = new String();
        FirebaseVisionImage firebaseVisionImage;
        firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getCloudTextRecognizer();
        FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setLanguageHints(Arrays.asList("en", "hi"))
                .build();

        final Task<FirebaseVisionText> result =
                detector.processImage(firebaseVisionImage)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                receipt_display.setText("인식중입니다.");
                                for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                                    for (FirebaseVisionText.Line line : block.getLines()) {
                                        List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
                                        for (FirebaseVisionText.Element element : line.getElements()) {
                                            String elementText = element.getText();
                                            tempString+=element.getText()+",";
                                        }
                                    }
                                }
                                setInputString(tempString);
                            }
                        });
    }


}

