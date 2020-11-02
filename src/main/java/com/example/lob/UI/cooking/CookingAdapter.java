package com.example.lob.UI.cooking;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lob.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CookingAdapter extends RecyclerView.Adapter<CookingAdapter.ViewHolder> {
    private ArrayList<CookingObject> mList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView_img;
        private TextView textView_title;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView_img = (ImageView) itemView.findViewById(R.id.imageView_img);
            textView_title = (TextView) itemView.findViewById(R.id.textView_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mList.get(pos).getDetail_link()));
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    //생성자
    public CookingAdapter(ArrayList<CookingObject> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public CookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cooking_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CookingAdapter.ViewHolder holder, int position) {
            holder.textView_title.setText(String.valueOf(mList.get(position).getTitle()));
            Picasso.get().load(mList.get(position).getImg_url()).resize(250, 250).into(holder.imageView_img);
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

}
