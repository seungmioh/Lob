package com.example.lob.UI.refrigerator;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.lob.DTO.FoodDTO;
import com.example.lob.DTO.FoodDTO2;
import com.example.lob.R;
import com.example.lob.UI.board.BoardAPI;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RefrigeratorFragment extends Fragment {

    private RefrigeratorViewModel mViewModel;
    private final String TAG = getClass().getSimpleName();
    private RefrigeratorAPI RAPI;
    private ListView foodListView;
    private Button foodInsertBtn;
    private List<Food> foodList;
    private RefrigeratorInsertAdapter refrigeratorInsertAdapter;
    private final String BASE_URL = "http://34.121.159.173/";
    private Context context;
    private long diffDay;
    public static Fragment newInstance() {
        return new RefrigeratorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(RefrigeratorViewModel.class);
        View root = inflater.inflate(R.layout.refrigerator_fragment, container, false);
        foodListView = root.findViewById(R.id.foodList);
        foodInsertBtn = root.findViewById(R.id.foodInsertBtn);
        foodList = new ArrayList<>();

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        initMyAPI(BASE_URL);
        try {
            Call<List<FoodDTO2>> getfoodList = RAPI.get_food();
            getfoodList.enqueue(new Callback<List<FoodDTO2>>() {
                @Override
                public void onResponse(Call<List<FoodDTO2>> call, Response<List<FoodDTO2>> response) {
                    if (response.isSuccessful()) {
                        final List<FoodDTO2> item = response.body();
                        for (FoodDTO2 foodDTO2 : item) {
                            Log.e("성공", "불러오기 성공");
                            String foodExpiration = String.valueOf(foodDTO2.getFood_expirationDate());
                            Log.d("foodExpiration", foodExpiration);
                            //날짜계산
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date date = new Date();
                                String str = String.valueOf(date.getTime());
                                Log.d("str", String.valueOf(date.getTime()));
                                Date startDate = sdf.parse(foodExpiration);
                                Date endDate = sdf.parse(str);

                                diffDay = (startDate.getTime() - endDate.getTime()) / (24*60*60*1000);
                                String dfifStr = String.valueOf(diffDay);
                                Log.d("dfifStr", dfifStr);
                                final int diff = Integer.parseInt(dfifStr);
                            }catch (Exception e){}

                            foodList.add(new Food(foodDTO2.getFood_id(), foodDTO2.getUser_email(), foodDTO2.getFood_name(), foodDTO2.getFood_expirationDate(), diffDay));
                        }
                        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //asd
                            }
                        });
                        refrigeratorInsertAdapter = new RefrigeratorInsertAdapter(getContext(),foodList);
                        refrigeratorInsertAdapter.notifyDataSetChanged();
                        foodListView.setAdapter(refrigeratorInsertAdapter);
                    }
                }

                @Override
                public void onFailure(Call<List<FoodDTO2>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
        }
        foodInsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefrigeratorInsertFragment refrigeratorInsertFragment = new RefrigeratorInsertFragment();
                fragmentTransaction.replace(R.id.fragment_container, refrigeratorInsertFragment).commit();
            }
        });

        return root;
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
                .client(clientBuilder.build())
                .build();

        RAPI = retrofit.create(RefrigeratorAPI.class);
    }
}

