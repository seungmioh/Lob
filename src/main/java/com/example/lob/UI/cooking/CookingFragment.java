package com.example.lob.UI.cooking;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lob.DTO.FoodDTO;
import com.example.lob.R;
import com.squareup.otto.Bus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CookingFragment extends Fragment {
    public static CookingFragment newInstance() {
        return new CookingFragment();
    }
    private CookingViewModel mViewModel;
    private RecyclerView recyclerView;
    RecyclerView.Adapter Adapter;
    //RecyclerView.LayoutManager layoutManager;
    LinearLayoutManager layoutManager;
    //private CookingAdapter Adapter;
    private ArrayList<CookingObject> list = new ArrayList();
    private FoodAPI foodAPI;
    private final String BASE_URL = "http://34.123.194.45";
    private int count = 0;
    private int ran;
    private String food;
    Document doc;
    private ArrayList<String> foodListData = new ArrayList<String>();


    Bundle bundle = getArguments();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(CookingViewModel.class);
        View root = inflater.inflate(R.layout.cooking_fragment, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);


        new Description().execute();

        return root;
    }

    private class Description extends AsyncTask<Void, Void, Void> {

        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            //진행다일로그 시작
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();


        }
        @Override
        protected Void doInBackground(Void... params) {

            try {
                initMyAPI(BASE_URL);
                Call<List<FoodDTO>> getCall = foodAPI.get_food();
                getCall.enqueue(new Callback<List<FoodDTO>>() {
                    @Override
                    public void onResponse(Call<List<FoodDTO>> call, Response<List<FoodDTO>> response) {
                        if (response.isSuccessful()) {
                            List<FoodDTO> mList = response.body();

                            for (FoodDTO item : mList) {
                                foodListData.add(item.getFood_name() + "요리");
//                        result[count] = item.getFood_name()+"요리";
//                        count++;
                                Log.d("foodDB", item.getFood_name());
                                Log.d("foodDB", foodListData.get(count++));
                            }
                            ran = foodListData.size();
                            int num = (int) (Math.random() * ran);
                            food = foodListData.get(num);
                            Log.d("size", Integer.toString(num) + food);
                        } else {
                            Log.d("COOKING", "Status Code : " + response.code());
                        }
                        ran = foodListData.size();
                        int num = (int) (Math.random() * ran);
                        food = foodListData.get(num);
                        Log.d("size", Integer.toString(num) + food);
                    }
                    @Override
                    public void onFailure(Call<List<FoodDTO>> call, Throwable t) {
                        Log.d("cooking", "Fail msg : " + t.getMessage());
                    }
                });
                Thread.sleep(2000);
                Document doc = Jsoup.connect("https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query="+food).get();
                Elements mElementDataSize = doc.select("ul[class=type01]").first().select("li"); //필요한 녀석만 꼬집어서 지정
                int mElementSize = mElementDataSize.size(); //목록이 몇개인지 알아낸다. 그만큼 루프를 돌려야 하나깐.
                Log.d("하하", Integer.toString(mElementSize));

                for (Element elem : mElementDataSize) { //이렇게 요긴한 기능이
                    //영화목록 <li> 에서 다시 원하는 데이터를 추출해 낸다.
                    String my_title = elem.select("li dt a").text();
                    String my_link = elem.select("li div[class=thumb] a").attr("href");
                    String my_imgUrl = elem.select("li div[class=thumb] a img").attr("src");
                    //특정하기 힘들다... 저 앞에 있는집의 오른쪽으로 두번째집의 건너집이 바로 우리집이야 하는 식이다.
                    //Log.d("test", "test" + mTitle);
                    //ArrayList에 계속 추가한다.
                    list.add(new CookingObject(my_title, my_imgUrl, my_link));
                    Log.d("안녕", my_title + "\n" + my_imgUrl + "\n" + my_link);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //ArraList를 인자로 해서 어답터와 연결한다.
            //CookingAdapter myAdapter = new CookingAdapter(list);

            Adapter = new CookingAdapter(list);
            Adapter.notifyDataSetChanged();
            layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(Adapter);
            progressDialog.dismiss();

        }
    }

private void initMyAPI(String baseUrl){

    Log.d("TAG","initMyAPI : " + baseUrl);
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    foodAPI = retrofit.create(FoodAPI.class);
}
}
