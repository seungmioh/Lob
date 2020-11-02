package com.example.lob.Adapter;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lob.DTO.FoodDTO;
import com.example.lob.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public  class AdapterFoodInsert extends BaseAdapter{
    private  Context context;
    List<FoodDTO> items = new ArrayList<FoodDTO>();
    List<FoodDTO> temporaryItems = items;
    DatePickerDialog.OnDateSetListener daateListener ;
    public AdapterFoodInsert(Context context){
        this.context= context;
    }
    @Override
    public int getCount() {
        return items.size();
    }
    public void addItem(FoodDTO foodDTO){
        items.add(foodDTO);
        notifyDataSetChanged();
    }
    @Override
    public FoodDTO getItem(int i) {
        return temporaryItems.get(i);
    }
    public  List<FoodDTO> getItems(){
        List<FoodDTO>  realItems= new ArrayList<FoodDTO>();
        for (int i =0 ;i<temporaryItems.size();i++){
            realItems.add(new FoodDTO(temporaryItems.get(i).getFood_name(),temporaryItems.get(i).getExpirationDate()));
        }
        return  realItems;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    public void removeItem(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_foodinsert, parent, false);
        EditText food_text = (EditText) view.findViewById(R.id.food_text);
         EditText food_date = (EditText) view.findViewById(R.id.food_date);
        Button food_del = (Button) view.findViewById(R.id.food_del);
        final FoodDTO foodDTO = temporaryItems.get(position);
        food_text.setText(foodDTO.getFood_name().toString());
        food_date.setText(foodDTO.getExpirationDate());
        food_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
            }
        });
        food_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                temporaryItems.get(position).setFood_name(editable.toString());
            }
        });
        food_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                temporaryItems.get(position).setExpirationDate(editable.toString());
            }
        });
    return  view;
    }
}