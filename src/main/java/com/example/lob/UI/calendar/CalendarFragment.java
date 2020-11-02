package com.example.lob.UI.calendar;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lob.DTO.FoodDTO;
import com.example.lob.DTO.FoodDTO2;
import com.example.lob.R;
import com.example.lob.UI.refrigerator.RefrigeratorAPI;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalendarFragment extends Fragment {

    private String BASE_URL = "http://34.121.159.173/";
    private final String TAG = getClass().getSimpleName();
    private RefrigeratorAPI RAPI;
    private CalendarViewModel mViewModel;
    private MaterialCalendarView cal_food;
    private ListView calListView;
    private List<Food> calList;
    private String[] result;
    private CalendarAdapter calendarAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        cal_food = root.findViewById(R.id.cal_food);
        calListView = root.findViewById(R.id.calListView);
        calList = new ArrayList<Food>();

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        cal_food.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2020, 9, 1))
                .setMaximumDate(CalendarDay.from(2040, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        cal_food.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OneDayDecorator()
        );

        FoodDTO items = new FoodDTO();
        for(int i = 0; i<50 ;i++){
            String year = items.getExpirationDate().substring(0,4);
            String month = items.getExpirationDate().substring(5,7);
            String day = items.getExpirationDate().substring(8, 10);
            Log.e("as", year+month+day);

            result = new String[]{year, month, day};
        }

        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        cal_food.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day = Year + "," + Month + "," + Day;

                Log.i("shot_Day test", shot_Day + "");
                cal_food.clearSelection();

                Toast.makeText(getActivity(), shot_Day, Toast.LENGTH_SHORT).show();
            }
        });
        initMyAPI(BASE_URL);
        Call<List<FoodDTO2>> getList = RAPI.get_food();
        getList.enqueue(new Callback<List<FoodDTO2>>() {
            @Override
            public void onResponse(Call<List<FoodDTO2>> call, Response<List<FoodDTO2>> response) {
                if (response.isSuccessful()) {
                    final List<FoodDTO2> fList = response.body();
                    fList.clear();
                    for (FoodDTO2 foodDTO : fList) {
                        Log.e("성공", "성공");
                        calList.add(new Food(foodDTO.getFood_name()));
                    }
                    calendarAdapter = new CalendarAdapter(getContext(), calList);
                    calendarAdapter.notifyDataSetChanged();
                    calListView.setAdapter(calendarAdapter);
                } else {
                    Log.e(TAG, "Status Code:" + response.body());
                }
            }

            @Override
            public void onFailure(Call<List<FoodDTO2>> call, Throwable t) {
                Log.d(TAG, "Fail msg: " + t.getMessage());
            }
        });
        return root;
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result) {
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();


            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로 짜르고 string을 int 로 변환
            for (int i = 0; i < Time_Result.length; i++) {


                //이부분에서 day를 선언하면 초기 값에 오늘 날짜 데이터 들어간다.
                //오늘 날짜 데이터를 첫 번째 인자로 넣기 때문에 데이터가 하나씩 밀려 마지막 데이터는 표시되지 않고, 오늘 날짜 데이터가 표시 됨.
                // day선언 주석처리

                //                CalendarDay day = CalendarDay.from(calendar);
                //                Log.e("데이터 확인","day"+day);
                String[] time = Time_Result[i].split(",");

                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                //선언문을 아래와 같은 위치에 선언
                //먼저 .set 으로 데이터를 설정한 다음 CalendarDay day = CalendarDay.from(calendar); 선언해주면 첫 번째 인자로 새로 정렬한 데이터를 넣어 줌.
                calendar.set(year, month - 1, dayy);
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);

            }


            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

//            if (isFinishing()) {
//                return;
//            }

            cal_food.addDecorator(new EventDecorator(Color.GREEN, calendarDays, CalendarFragment.this));
        }
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