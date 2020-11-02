package com.example.lob.UI.refrigerator;

import android.app.DatePickerDialog;
import android.os.Bundle;
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
import com.example.lob.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.lob.R.id.foodUpdateExpirationText;

public class RefrigeratorUpdateFragment extends Fragment {
    private List<FoodDTO> list;
    private RefrigeratorUpdateViewModel mViewModel;
    Date fDate;
    private final String TAG = getClass().getSimpleName();
    private final String BASE_URL = "http://34.121.159.173/";
    private RefrigeratorAPI RAPI;
    private FirebaseAuth googleAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = googleAuth.getCurrentUser();
    private Button foodUpdateOk, foodUpdateCancle, foodUpdateExpirationBtn;
    private EditText foodUpdateNameEdit;
    private TextView foodUpdateExpirationText;
    private RefrigeratorInsertAdapter refrigeratorInsertAdapter;
    private String currentUser_id;
    private int food_id_true;
    private String outString;
    private long diifDays;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(RefrigeratorUpdateViewModel.class);
        View root = inflater.inflate(R.layout.refrigeratorupdate_fragment, container, false);

        foodUpdateOk = root.findViewById(R.id.foodUpdateOk);
        foodUpdateCancle = root.findViewById(R.id.foodUpdateCancle);
        foodUpdateNameEdit = root.findViewById(R.id.foodUpdateNameEdit);
        foodUpdateExpirationBtn = root.findViewById(R.id.foodUpdateExpirationBtn);
        list = new ArrayList<FoodDTO>();

        currentUser_id = currentUser.getEmail().substring(0, currentUser.getEmail().lastIndexOf("@"));
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = getArguments();
        if (bundle != null) {
            food_id_true = Integer.parseInt(bundle.getString("food_id"));
            initMyAPI(BASE_URL);
            try {
                Log.d("TAG", "GET");
                Call<List<FoodDTO2>> getCall = RAPI.get_food();
                getCall.enqueue(new Callback<List<FoodDTO2>>() {
                    @Override
                    public void onResponse(Call<List<FoodDTO2>> call, Response<List<FoodDTO2>> response) {
                        if (response.isSuccessful()) {
                            final List<FoodDTO2> mList = response.body();
                            for (FoodDTO2 item : mList) {
                                if (item.getFood_id() == food_id_true) {
                                    foodUpdateNameEdit.setText(item.getFood_name());
                                    break;
                                }
                            }
                        } else {
                            Log.d("TAG", "Status Code : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FoodDTO2>> call, Throwable t) {
                        Log.d("TAG", "Fail msg : " + t.getMessage());
                    }
                });
            } catch (Exception e) {
            }
            foodUpdateExpirationText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog dialog = new DatePickerDialog(getContext(), listener, 2020, 10, 13);
                    dialog.show();
                }
            });
            foodUpdateCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RefrigeratorFragment refrigeratorFragment = new RefrigeratorFragment();
                    fragmentTransaction.replace(R.id.fragment_container, refrigeratorFragment);
                }
            });
        }
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
            ParsePosition pos = new ParsePosition(0);
            Date frmTime = origin_format.parse(inString, pos);
            Log.e("frmTime", String.valueOf(frmTime));
            outString = new_format.format(frmTime);
            Log.e("outString", outString);
            foodUpdateExpirationText.setText(outString.substring(0, 10));
        }
    };
    private void initMyAPI(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RAPI = retrofit.create(RefrigeratorAPI.class);
    }
}
