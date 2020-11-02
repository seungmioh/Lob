package com.example.lob.UI.refrigerator;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.lob.DTO.FoodDTO;
import com.example.lob.DTO.FoodDTO2;
import com.example.lob.DTO.UserDto;
import com.example.lob.R;
import com.example.lob.Activity.UserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RefrigeratorInsertFragment extends Fragment {
    private List<FoodDTO> list;
    private RefrigeratorInsertViewModel mViewModel;
    private final String TAG = getClass().getSimpleName();
    private final String BASE_URL = "http://34.121.159.173/";
    private RefrigeratorAPI RAPI;
    private FirebaseAuth googleAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = googleAuth.getCurrentUser();
    private Button foodOk, foodExpirationBtn, foodCancel, foodAdd;
    private EditText foodNameEdit;
    private TextView foodExpirationText;
    private RefrigeratorInsertAdapter refrigeratorInsertAdapter;
    private Date eDate;
    private String outString;
    private long diffDays;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(RefrigeratorInsertViewModel.class);
        View root = inflater.inflate(R.layout.refrigeratorinsert_fragment, container, false);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        foodOk = root.findViewById(R.id.foodOk);
        foodCancel = root.findViewById(R.id.foodCancle);
        foodAdd = root.findViewById(R.id.foodAdd);
        foodNameEdit = root.findViewById(R.id.foodNameEdit);
        foodExpirationBtn = root.findViewById(R.id.foodExpirationBtn);
        foodExpirationText = root.findViewById(R.id.foodExpirationText);
        list = new ArrayList<FoodDTO>();

        initMyAPI(BASE_URL);

            foodOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = foodNameEdit.getText().toString();
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
                FoodDTO2 foodDTO = new FoodDTO2();
                foodDTO.setFood_name(foodName);
                foodDTO.setUser_email(currentUser.getEmail());
                try {
                    Log.e("sdadasdasdsad", (sdf.parse(outString).toString()));
                    foodDTO.setFood_expirationDate(sdf.parse(outString));
                } catch (ParseException e) {}
                doDiffOfDate(outString);
                foodDTO.setFood_remainDate(diffDays);
                Log.e("diff", String.valueOf(diffDays));

                Call<FoodDTO2> foodCall = RAPI.post_food(foodDTO);
                foodCall.enqueue(new Callback<FoodDTO2>() {
                    @Override
                    public void onResponse(Call<FoodDTO2> call, Response<FoodDTO2> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "등록완료");
                            ((UserActivity) getActivity()).replaceFragment(RefrigeratorFragment.newInstance());
                        } else {
                            Log.e("Status", "Status Code : " + response.code());
                            Log.e("response.erroBody()", response.errorBody().toString());
                            Log.e("call.request()", call.request().body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<FoodDTO2> call, Throwable t) {
                        Log.e("Fail mag: ", t.getMessage());
                    }
                });
                foodNameEdit.setText(null);
                foodExpirationText.setText("yyyy-MM-dd");
            }
        });
        foodExpirationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), listener, 2020, 10, 13);
                dialog.show();
            }
        });
        foodAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        foodCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefrigeratorFragment refrigeratorFragment = new RefrigeratorFragment();
                fragmentTransaction.replace(R.id.fragment_container, refrigeratorFragment);
            }
        });
        return root;
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            DateFormat origin_format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
            DateFormat new_format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.KOREA);
            new_format.setTimeZone(TimeZone.getTimeZone("Asial/Seoul"));
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, dayOfMonth);
            cal.set(Calendar.HOUR_OF_DAY, 9);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            String inString = origin_format.format(cal.getTime());
            Log.d("inString", inString);
            ParsePosition pos = new ParsePosition(0);
            Date frmTime = origin_format.parse(inString, pos);
            Log.e("frmTime", String.valueOf(frmTime));
            outString = new_format.format(frmTime);
            Log.e("outString", outString);
            eDate = tempDate(outString);
            Log.e("eDate", String.valueOf(eDate));
            foodExpirationText.setText(outString);
        }
    };

    private Date tempDate(String outString) {
        DateFormat origin_format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
        DateFormat new_format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.KOREA);
        Date date = null;
        try {
            date = origin_format.parse(outString);
            Log.d("date", String.valueOf(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private long doDiffOfDate(String outString) {
        //현재시간
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        String date = sdf.format(new Date());
        //식자재 유통기한
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            Date beginDate = format.parse(date);
            Date endDate = format.parse(outString);

            long diff = endDate.getTime() - beginDate.getTime();
            diffDays = diff / (24 + 60 * 60 * 1000);

        } catch (ParseException e) {
        }
        return diffDays;
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
