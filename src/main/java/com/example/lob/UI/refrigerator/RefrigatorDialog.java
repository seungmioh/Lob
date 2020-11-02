package com.example.lob.UI.refrigerator;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.lob.DTO.FoodDTO;
import com.example.lob.DTO.FoodDTO2;
import com.example.lob.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RefrigatorDialog{
    private Button abandonmentButton, eatButton, updateButton, backButton;
    private Context context;
    private final String TAG = getClass().getSimpleName();
    private final String BASE_URL = "http://34.121.159.173/";
    private RefrigeratorAPI RAPI;
    private List<FoodDTO> foodList;
    private FirebaseAuth googleAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = googleAuth.getCurrentUser();
    public RefrigatorDialog(Context rDialog) {
        this.context = context;
    }
    public void callDialog(){
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.refrigerator_dialog);
        dialog.show();

        abandonmentButton = dialog.findViewById(R.id.abandonmentButton);
        eatButton = dialog.findViewById(R.id.eatButton);
        updateButton = dialog.findViewById(R.id.updateButton);
        backButton = dialog.findViewById(R.id.backButton);

        abandonmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<FoodDTO2> foodDTOCall = RAPI.update_foodDelInfo();
                foodDTOCall.enqueue(new Callback<FoodDTO2>() {
                    @Override
                    public void onResponse(Call<FoodDTO2> call, Response<FoodDTO2> response) {
                        if(response.isSuccessful()){
                            Log.e(TAG, "버림");

                        }
                    }

                    @Override
                    public void onFailure(Call<FoodDTO2> call, Throwable t) {

                    }
                });
            }
        });
        eatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

    private void initMyAPI(String baseUrl) {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);
        Log.d(TAG, "initMyAPI: " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                //
                .client(clientBuilder.build())
                .build();

        RAPI = retrofit.create(RefrigeratorAPI.class);
    }
}
