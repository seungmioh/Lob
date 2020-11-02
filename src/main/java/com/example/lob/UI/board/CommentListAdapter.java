package com.example.lob.UI.board;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.lob.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class CommentListAdapter extends BaseAdapter {
    private Context commentContext;
    private List<Comment> commentList;

    public CommentListAdapter(Context context, List<Comment> commentList) {
        this.commentContext = context;
        this.commentList = commentList;
    }
    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View View, ViewGroup viewGroup) {
        View v = View.inflate(commentContext, R.layout.comment_listview_adapter,null);
        TextView noticeText = (TextView)v.findViewById(R.id.comment_content_text);
        TextView nameText = (TextView)v.findViewById(R.id.comment_date_text);
        TextView dateText = (TextView)v.findViewById(R.id.comment_id_text);

        noticeText.setText(commentList.get(i).getComment_content());
        nameText.setText(commentList.get(i).getComment_date());
        dateText.setText(commentList.get(i).getComment_writer());

        v.setTag(commentList.get(i).getComment_content());
        return v;

    }
}
