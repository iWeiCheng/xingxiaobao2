package com.jiajun.demo.moudle.homepage.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jiajun.demo.R;
import com.jiajun.demo.moudle.homepage.entities.HomePageBean;
import com.jiajun.demo.network.Network;

import java.util.List;


public class TextAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public TextAdapter(List<String> data) {
        super(R.layout.item_car_number, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, final String item) {
        helper.setText(R.id.text, item);
        helper.addOnClickListener(R.id.text);
    }
}
