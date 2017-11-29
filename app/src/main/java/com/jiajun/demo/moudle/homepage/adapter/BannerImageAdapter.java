package com.jiajun.demo.moudle.homepage.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jiajun.demo.R;
import com.jiajun.demo.moudle.homepage.entities.HomePageBean;
import com.jiajun.demo.network.Network;

import java.util.List;


public class BannerImageAdapter extends BaseQuickAdapter<String,BaseViewHolder> {


    public BannerImageAdapter(List<String> data) {
        super(R.layout.item_image, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, final String item) {
        ImageView imageView = helper.getView(R.id.item_img);
        if (item.startsWith("http")) {
            Glide.with(mContext).load(item).into(imageView);
        } else {
            imageView.setImageResource(Integer.parseInt(item));
        }
        helper.addOnClickListener(R.id.root);
    }
}
