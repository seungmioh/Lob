package com.example.lob.UI.consumption;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.lob.API.FoodAPI;
import com.example.lob.Activity.Foodrecognition;
import com.example.lob.Activity.Receiptrecognition;
import com.example.lob.DTO.BoardDto;
import com.example.lob.DTO.FoodDTO;
import com.example.lob.Adapter.AdapterFoodInsert;
import com.example.lob.Dialog.FoodDialog;
import com.example.lob.R;
import com.example.lob.UI.board.BoardAPI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConsumptionFragment extends Fragment {
    private FoodAPI foodAPI;
    private ConsumptionViewModel consumptionViewModel;

    Button add_receiptCamera, add_food;
    private final String BASE_URL = "http://34.121.159.173/";
    private FirebaseAuth googleAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = googleAuth.getCurrentUser();
    List<FoodDTO> foodarray = null;
    AdapterFoodInsert adapterFoodInsert = new AdapterFoodInsert(getContext());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        consumptionViewModel =
                ViewModelProviders.of(this).get(ConsumptionViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_consumption, container, false);
        add_receiptCamera = root.findViewById(R.id.add_receiptCamera);
        add_food = root.findViewById(R.id.add_food);
        //영수증
        add_receiptCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("asdad", "zzzz");
                FoodDialog foodDialog = new FoodDialog(getContext() , null);
                foodDialog.setDialogListener(new FoodDialog.FoodInsertDialogListener() {
                    @Override
                    public void onPositiveClicked(List<FoodDTO> foodDTOList) {
                            if (foodDTOList != null) {
                                foodarray=foodDTOList;
                                initFoodAPI(BASE_URL);

                                String a = currentUser.getEmail().substring(0, currentUser.getEmail().lastIndexOf("@"));
                            for (int i = 0; i < foodarray.size(); i++) {
                                Log.e("zzzzzz",String.valueOf(foodarray.get(i).getFood_name()));
                                Log.e("zzzzzz",String.valueOf(foodarray.get(i).getExpirationDate()));
                                foodarray.get(i).setUser(a);
                            }
                            Call<Void> foodInsertcall = foodAPI.post_foods(foodarray);
                            foodInsertcall.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Log.e("adsasdasd", "zxczczx");
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.e("call",String.valueOf(call)+String.valueOf(t));
                                }
                            });
                        }
                    }


                });
                foodDialog.show();
            }
        });
        //음식 넣기
        add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), Foodrecognition.class);
                startActivity(intent);
            }
        });
        return root;
    }

    public void receipt_show() {
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle("Profile 설정");
            builder.setMessage("안녕하세요 프로필을 설정해주세요");
            builder.setPositiveButton("텍스트",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });


            builder.setNegativeButton("갤러리에서 선택",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent imgintent = new Intent(Intent.ACTION_PICK);
                            imgintent.setType(MediaStore.Images.Media.CONTENT_TYPE);

                        }
                    });
            builder.show();
        }
    }

    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    private void initFoodAPI(String baseUrl) {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);
        Log.e("e", "initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();

        foodAPI = retrofit.create(FoodAPI.class);
    }
}
