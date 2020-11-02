package com.example.lob.UI.board;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.lob.DTO.BoardDto;
import com.example.lob.DTO.CommentDTO;
import com.example.lob.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoardUpdateFragment extends Fragment {
    private static final String ARG_PARAM = "";
    private Object BoardUpdateViewModel;

    public static BoardUpdateFragment newInstance() {
        return new BoardUpdateFragment();
    }

    private BoardUpdateViewModel boardViewModel;
    private TextView boardUpdate_title;
    private ImageView boardUpdate_user_image;
    private TextView boardUpdate_user_id;
    private TextView boardUpdate_date;
    private TextView boardUpdate_content;
    private Button board_updateButton;
    private Button board_deleteButton;
    private BoardAPI BAPI;
    private CommentAPI CAPI;
    private final String BASE_URL = "http://34.121.58.193";
    private String user;
    private FirebaseAuth googleAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = googleAuth.getCurrentUser();
    private String currentUser_id;
    private int board_id_true;
    private ListView commentListView;
    private CommentListAdapter Adapter;
    private List<Comment> commentList;
    private ScrollView scrollView;
    private Button commentUpdate_insertButton;
    private EditText comment_text;
    private String commnet_user;
    private ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BoardUpdateViewModel =
                ViewModelProviders.of(this).get(BoardUpdateViewModel.class);
        View root = inflater.inflate(R.layout.boardupdate_fragment, container, false);
        boardUpdate_title = root.findViewById(R.id.boardUpdate_title);
        //boardUpdate_user_image = root.findViewById(R.id.boardUpdate_user_image);
        boardUpdate_user_id = root.findViewById(R.id.boardUpdate_user_id);
        boardUpdate_date = root.findViewById(R.id.boardUpdate_date);
        boardUpdate_content = root.findViewById(R.id.boardUpdate_content);
        board_updateButton = root.findViewById(R.id.board_updateButton);
        board_deleteButton = root.findViewById(R.id.board_deleteButton);
        commentListView = root.findViewById(R.id.commentListView);
        commentList = new ArrayList<>();
        scrollView = root.findViewById(R.id.scrollview_comment);
        commentUpdate_insertButton = root.findViewById(R.id.commentUpdate_insertButton);
        comment_text = root.findViewById(R.id.comment_text);
        final LinearLayout linear = (LinearLayout) View.inflate(getContext(), R.layout.custom_dialog, null);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시 기다려 주세요.");
        progressDialog.show();

        currentUser_id = currentUser.getEmail().substring(0, currentUser.getEmail().lastIndexOf("@"));
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = getArguments();
        if (bundle != null) {
            board_id_true = Integer.parseInt(bundle.getString("board_id"));

            initBoardAPI(BASE_URL);
            try {
                Call<List<BoardDto>> getCall = BAPI.get_posts();
                getCall.enqueue(new Callback<List<BoardDto>>() {
                    @Override
                    public void onResponse(Call<List<BoardDto>> call, Response<List<BoardDto>> response) {
                        if (response.isSuccessful()) {
                            final List<BoardDto> mList = response.body();
                            for (BoardDto item : mList) {
                                String date_text = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(item.getBoard_date());
                                user = item.getBoard_writer();
                                if (board_id_true == item.getBoard_id()) {
                                    boardUpdate_title.setText(item.getBoard_title());
                                    boardUpdate_user_id.setText(item.getBoard_writer());
                                    boardUpdate_date.setText(date_text);
                                    boardUpdate_content.setText(item.getBoard_contents());
                                    break;
                                }

                            }
                            if (user.equals(currentUser_id)) {
                                board_updateButton.setVisibility(View.VISIBLE);
                                board_deleteButton.setVisibility(View.VISIBLE);
                            } else {
                                board_updateButton.setVisibility(View.GONE);
                                board_deleteButton.setVisibility(View.GONE);
                            }

                        } else {
                            Log.d("gtg", "Status Code : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BoardDto>> call, Throwable t) {
                        Log.d("failll", "Fail msg : " + t.getMessage());
                    }
                });
                initCommentAPI(BASE_URL);
                final Call<List<CommentDTO>> getCal = CAPI.get_comment();
                getCal.enqueue(new Callback<List<CommentDTO>>() {
                    @Override
                    public void onResponse(Call<List<CommentDTO>> call, final Response<List<CommentDTO>> response) {
                        if (response.isSuccessful()) {
                            Log.d("commend", Integer.toString(board_id_true));
                            List<CommentDTO> mList = response.body();
                            String result = "";
                            for (CommentDTO item : mList) {
                                Log.d("commend1", Integer.toString(board_id_true));
                                if (board_id_true == item.getBoard_id()) {
                                    Log.d("commend2", Integer.toString(board_id_true));
                                    String date_text = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(item.getComment_date());
                                    Log.d("1", item.getComment_contents() + item.getComment_writer() + date_text);
                                    commentList.add(new Comment(item.getComment_contents(), item.getComment_writer(), date_text, item.getComment_id()));
                                    Adapter = new CommentListAdapter(getContext(), commentList);
                                    //Adapter = new BoardListAdapter(getApplicationContext(),boardList);
                                    commentListView.setAdapter(Adapter);
                                }
                                progressDialog.dismiss();
                                commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        Log.d("commentlist", Integer.toString(commentList.get(position).getComment_id()));
                                        final int comment_id_t = commentList.get(position).getComment_id();
                                        commnet_user = commentList.get(position).getComment_writer();
                                        if (commnet_user.equals(currentUser_id)) {
                                            new AlertDialog.Builder(getContext())
                                                    .setView(linear)
                                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            Call<CommentDTO> deleteCall = CAPI.delete_comment(comment_id_t);
                                                            deleteCall.enqueue(new Callback<CommentDTO>() {
                                                                @Override
                                                                public void onResponse(Call<CommentDTO> call, Response<CommentDTO> response) {
                                                                    if (response.isSuccessful()) {
                                                                        Log.d("TAG", "삭제 완료");
                                                                    } else {
                                                                        Log.d("TAG", "Status Code : " + response.code());
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<CommentDTO> call, Throwable t) {
                                                                    Log.d("TAG", "Fail msg : " + t.getMessage());
                                                                }
                                                            });
                                                            refresh();
                                                            Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            refresh();
                                                            Toast.makeText(getContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                            //CustomDialog customDialog = new CustomDialog(getContext());
                                            // customDialog.callFunction(commentList.get(position).getComment_id(),board_id_true);

                                        }
                                    }
                                });
                            }
                        } else {
                            Log.d("TAG", "Status Code : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CommentDTO>> call, Throwable t) {
                        Log.d("TAG", "Fail msg : " + t.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.e("", e.toString());
            }


        }
        commentUpdate_insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = comment_text.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();
                int board_comment_id = board_id_true;
                CommentDTO item = new CommentDTO();
                item.setComment_contents(comment);
                item.setBoard_id(board_comment_id);
                item.setComment_writer(currentUser_id);
                item.setComment_date(currentTime);

                initCommentAPI(BASE_URL);
                Call<CommentDTO> postCall = CAPI.post_comment(item);
                postCall.enqueue(new Callback<CommentDTO>() {
                    @Override
                    public void onResponse(Call<CommentDTO> call, Response<CommentDTO> response) {
                        if (response.isSuccessful()) {
                            Log.d("comment", "등록 완료");
                            comment_text.setText(null);
                            refresh();
                        } else {
                            Log.d("comment", "Status Code : " + response.code());
                            Log.d("comment", response.errorBody().toString());
                            Log.d("comment", call.request().body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentDTO> call, Throwable t) {
                        Log.d("comment", "Fail msg : " + t.getMessage());
                    }
                });
            }
        });
        board_updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardWriteUpdateFragment boardWriteUpdateFragment = new BoardWriteUpdateFragment();
                Bundle bundle = new Bundle();
                bundle.putString("board_id", Integer.toString(board_id_true));
                boardWriteUpdateFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, boardWriteUpdateFragment).commit();
            }
        });
        board_deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<List<BoardDto>> deleteCall = BAPI.delete_posts(board_id_true);
                deleteCall.enqueue(new Callback<List<BoardDto>>() {
                    @Override
                    public void onResponse(Call<List<BoardDto>> call, Response<List<BoardDto>> response) {
                        if (response.isSuccessful()) {
                            Log.d("TAG", "삭제 완료");
                        } else {
                            Log.d("TAG", "Status Code : " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BoardDto>> call, Throwable t) {
                        Log.d("", "Fail msg : " + t.getMessage());
                    }
                });
                BoardFragment boardFragment = new BoardFragment();
                fragmentTransaction.replace(R.id.fragment_container, boardFragment).commit();
            }
        });
        return root;
    }

    private void refresh() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(this).attach(this).commit();

    }

    private void initBoardAPI(String baseUrl) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BAPI = retrofit.create(BoardAPI.class);
    }

    private void initCommentAPI(String baseUrl) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CAPI = retrofit.create(CommentAPI.class);
    }
}
