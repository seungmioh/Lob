package com.example.lob.UI.refrigerator;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lob.DTO.FoodDTO;
import com.example.lob.DTO.FoodDTO2;
import com.example.lob.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RefrigeratorInsertAdapter extends BaseAdapter {
    private Context context;
    private List<Food> foodList;
    private final String TAG = getClass().getSimpleName();
    private final String BASE_URL = "http://34.121.159.173/";
    private RefrigeratorAPI RAPI;
    private FirebaseAuth googleAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = googleAuth.getCurrentUser();

    public RefrigeratorInsertAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }
//    public void setContext(Context context) {
//        this.context = context;
//    }
//
//    public int getContext() {
//        return foodList.size();
//    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.refrigeratorinsert_adapter, null);
        TextView foodName = (TextView) v.findViewById(R.id.food_name);
        TextView expirationDate = (TextView) v.findViewById(R.id.foodExpirationDate);
        TextView foodDday = (TextView) v.findViewById(R.id.foodDday);
        Button abandonmentButton = v.findViewById(R.id.abandonmentButton);
        Button updateButton = v.findViewById(R.id.updateButton);

        String s = new SimpleDateFormat("yyyy-MM-dd").format(foodList.get(i).getFood_expirationDate());
        foodName.setText(foodList.get(i).getFood_name());
        expirationDate.setText(s);
        foodDday.setText(foodList.get(i).getFood_remainDate() + "일 남았습니다.");

        v.setTag(foodList.get(i).getFood_name());
        initMyAPI(BASE_URL);
        abandonmentButton.setTag(i);
        abandonmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int food_id = foodList.get(i).getFood_id();
                Log.d("food_id", String.valueOf(food_id));
                Call<FoodDTO2> deleteFood = RAPI.delete_food(food_id);
                deleteFood.enqueue(new Callback<FoodDTO2>() {
                    @Override
                    public void onResponse(Call<FoodDTO2> call, Response<FoodDTO2> response) {
                        if (response.isSuccessful()) {
                            notifyDataSetChanged();
                            Log.e(TAG, "삭제성공");
                        } else {
                            Log.d("TAG", "Status Code : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<FoodDTO2> call, Throwable t) {

                    }
                });
                notifyDataSetChanged();

            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;

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
