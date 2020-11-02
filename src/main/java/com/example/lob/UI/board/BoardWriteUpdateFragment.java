package com.example.lob.UI.board;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.lob.DTO.BoardDto;
import com.example.lob.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoardWriteUpdateFragment extends Fragment {
    private BoardWriteUpdateViewModel boardwriteUpdateViewModel;
    private EditText boardUpdateWrite_title;
    private EditText boardUpdateWrite_content;
    private Button board_writeUpdateButton;
    private final String BASE_URL = "http://34.121.58.193";
    private BoardAPI BAPI;
    private int board_id_true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        boardwriteUpdateViewModel =
                ViewModelProviders.of(this).get(BoardWriteUpdateViewModel.class);
        View root = inflater.inflate(R.layout.boardupdatewrite_fragment, container, false);
        boardUpdateWrite_title = root.findViewById(R.id.boardUpdateWrite_title);
        boardUpdateWrite_content = root.findViewById(R.id.boardUpdateWrite_content);
        board_writeUpdateButton = root.findViewById(R.id.board_writeUpdateButton);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = getArguments();
        if(bundle!=null) {
            board_id_true = Integer.parseInt(bundle.getString("board_id"));
            initBoardAPI(BASE_URL);
            try {
                Log.d("TAG", "GET");
                Call<List<BoardDto>> getCall = BAPI.get_posts();
                getCall.enqueue(new Callback<List<BoardDto>>() {
                    @Override
                    public void onResponse(Call<List<BoardDto>> call, Response<List<BoardDto>> response) {
                        if (response.isSuccessful()) {
                            final List<BoardDto> mList = response.body();
                            for (BoardDto item : mList) {
                                if (item.getBoard_id() == board_id_true) {
                                    boardUpdateWrite_title.setText(item.getBoard_title());
                                    boardUpdateWrite_content.setText(item.getBoard_contents());
                                    break;
                                }
                            }
                        } else {
                            Log.d("TAG", "Status Code : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BoardDto>> call, Throwable t) {
                        Log.d("TAG", "Fail msg : " + t.getMessage());
                    }
                });
            } catch (Exception e) {
            }
        }
        board_writeUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","PATCH");
                BoardDto item = new BoardDto();
                item.setBoard_title(boardUpdateWrite_title.getText().toString());
                item.setBoard_contents(boardUpdateWrite_content.getText().toString());
                Call<BoardDto> patchCall = BAPI.patch_posts(board_id_true,item);
                patchCall.enqueue(new Callback<BoardDto>() {
                    @Override
                    public void onResponse(Call<BoardDto> call, Response<BoardDto> response) {
                        if(response.isSuccessful()){
                            Log.d("TAG","patch 성공");
                        }else{
                            Log.d("TAG","Status Code : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<BoardDto> call, Throwable t) {
                        Log.d("TAG","Fail msg : " + t.getMessage());
                    }
                });
                BoardFragment boardFragment = new BoardFragment();
                fragmentTransaction.replace(R.id.fragment_container, boardFragment).commit();
            }
        });
        return root;
    }
    private void initBoardAPI(String baseUrl){

        Log.d("TAG","initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BAPI = retrofit.create(BoardAPI.class);
    }
}