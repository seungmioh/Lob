package com.example.lob.UI.board;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.lob.R;

import java.util.List;

public class BoardListAdapter extends BaseAdapter {
    private Context context;
    private List<Board> boardList;

    public BoardListAdapter(Context context, List<Board> boardList) {
        this.context = context;
        this.boardList = boardList;
    }

    @Override
    public int getCount() {
        return boardList.size();
    }

    @Override
    public Object getItem(int i) {
        return boardList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View View, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.board_main_design,null);
        TextView noticeText = (TextView)v.findViewById(R.id.noticeText);
        TextView nameText = (TextView)v.findViewById(R.id.nameText);
        TextView dateText = (TextView)v.findViewById(R.id.dateText);

        noticeText.setText(boardList.get(i).getNotice());
        nameText.setText(boardList.get(i).getName());
        dateText.setText(boardList.get(i).getDate());

        v.setTag(boardList.get(i).getNotice());
        return v;
    }
}
