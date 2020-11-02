package com.example.lob.UI.calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lob.R;
import com.example.lob.UI.board.Board;

import java.util.List;

class CalendarAdapter extends BaseAdapter{
    private Context context;
    private List<Food> foodList;

    public CalendarAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int i) {
        return foodList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View View, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.calendar_adapter,null);
        TextView cal_adapter = (TextView)v.findViewById(R.id.cal_adapter);

        cal_adapter.setText(foodList.get(i).getFood_name());

        v.setTag(foodList.get(i).getFood_name());
        return v;
    }
}
