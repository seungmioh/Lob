package com.example.lob.UI.board;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.lob.DTO.BoardDto;
import com.example.lob.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BoardFragment extends Fragment {
    public static BoardFragment newInstance() {
        return new BoardFragment();
    }
    private final  String TAG = getClass().getSimpleName(); //나중에 지워도됨
    private final String BASE_URL = "http://34.121.58.193";
    private BoardAPI BAPI;

    private BoardViewModel boardViewModel;
    private ListView boardListView;
    private BoardListAdapter Adapter;
    private List<Board> boardList;
    private ScrollView scrollView;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        boardViewModel =
                ViewModelProviders.of(this).get(BoardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_board, container, false);


        boardListView = root.findViewById(R.id.boardListView);
        boardList = new ArrayList<>();
        scrollView = root.findViewById(R.id.scrollview_board);

        initBoardAPI(BASE_URL);
        try {
            Log.d(TAG,"GET");
            Call<List<BoardDto>> getCall = BAPI.get_posts();
            getCall.enqueue(new Callback<List<BoardDto>>() {
                @Override
                public void onResponse(Call<List<BoardDto>> call, Response<List<BoardDto>> response) {
                    if( response.isSuccessful()){
                        final List<BoardDto> mList = response.body();
                        for(  BoardDto item : mList){
                            String date_text = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(item.getBoard_date());

                            boardList.add(new Board(item.getBoard_title(),item.getBoard_writer(),date_text,item.getBoard_id()));

                        }
                        boardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Log.d(TAG,Integer.toString(boardList.get(position).getId()));
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                BoardUpdateFragment boardUpdateFragment = new BoardUpdateFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("board_id",Integer.toString(boardList.get(position).getId()));
                                boardUpdateFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.fragment_container, boardUpdateFragment).commit();
                            }
                        });
                        boardListView.setStackFromBottom(true);
                        Adapter = new BoardListAdapter(getContext(),boardList);
                        Collections.reverse(boardList);
                        Adapter.notifyDataSetChanged() ;
                        boardListView.setAdapter(Adapter);

                    }else {
                        Log.d(TAG,"Status Code : " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<List<BoardDto>> call, Throwable t) {
                    Log.d(TAG,"Fail msg : " + t.getMessage());
                }
            });
        }catch (Exception e){}


        boardListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        return root;
    }
    private void initBoardAPI(String baseUrl){
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BAPI = retrofit.create(BoardAPI.class);
    }
}
