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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CookingMainFragment extends Fragment {
    private FoodAPI foodAPI;
    private final String BASE_URL = "http://34.121.58.193";
    private CookingViewModel mViewModel;
    private RecyclerView recyclerView;
   // private CookingAdapter Adapter;
    RecyclerView.Adapter Adapter;
    private ArrayList<CookingObject> list = new ArrayList<>();
    private int count = 0;
    private int ran;
    private String food;
    private ArrayList<String> mListData = new ArrayList<String>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(CookingViewModel.class);
        View root = inflater.inflate(R.layout.cooking_fragment, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        try {
            initMyAPI(BASE_URL);
            Log.d("COOKING","GET");
            Call<List<FoodDTO>> getCall = foodAPI.get_food();
            getCall.enqueue(new Callback<List<FoodDTO>>() {
                @Override
                public void onResponse(Call<List<FoodDTO>> call, Response<List<FoodDTO>> response) {
                    if( response.isSuccessful()){
                        List<FoodDTO> mList = response.body();

                        for( FoodDTO item : mList){
                            mListData.add(item.getFood_name()+"요리");
//                        result[count] = item.getFood_name()+"요리";
//                        count++;
                            Log.d("foodDB",item.getFood_name());
                            Log.d("foodDB",mListData.get(count++));
                        }
                        ran = mListData.size();
                        int num = (int)(Math.random()*ran);
                        food = mListData.get(num);
                        Log.d("뭘까용",Integer.toString(ran)+food);
                        Log.d("size",Integer.toString(ran));
                    }else {
                        Log.d("COOKING","Status Code : " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<FoodDTO>> call, Throwable t) {
                    Log.d("cooking","Fail msg : " + t.getMessage());
                }
            });
            Document doc = Jsoup.connect("https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=바나나요리").get();
            Elements mElementDataSize = doc.select("ul[class=type01]").first().select("li"); //필요한 녀석만 꼬집어서 지정
            int mElementSize = mElementDataSize.size(); //목록이 몇개인지 알아낸다. 그만큼 루프를 돌려야 하나깐.
            Log.d("하하",Integer.toString(mElementSize));

            for(Element elem : mElementDataSize){ //이렇게 요긴한 기능이
                //영화목록 <li> 에서 다시 원하는 데이터를 추출해 낸다.
                String my_title = elem.select("li dt a").text();
                String my_link = elem.select("li div[class=thumb] a").attr("href");
                String my_imgUrl = elem.select("li div[class=thumb] a img").attr("src");
                //특정하기 힘들다... 저 앞에 있는집의 오른쪽으로 두번째집의 건너집이 바로 우리집이야 하는 식이다.
                //Log.d("test", "test" + mTitle);
                //ArrayList에 계속 추가한다.
                list.add(new CookingObject(my_title,my_imgUrl,my_link));
                CookingAdapter myAdapter = new CookingAdapter(list);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(myAdapter);
                Log.d("안녕",my_title+"\n"+my_imgUrl+"\n"+my_link);


            }
            Log.d("리스트사이즈",Integer.toString(list.size()));
            //추출한 전체 <li> 출력해 보자.
            Log.d("debug :", "List " + mElementDataSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;
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
