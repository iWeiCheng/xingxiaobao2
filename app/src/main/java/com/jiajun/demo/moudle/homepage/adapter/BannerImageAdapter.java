package com.jiajun.demo.moudle.homepage.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jiajun.demo.R;
import com.jiajun.demo.moudle.homepage.entities.HomePageBean;
import com.jiajun.demo.network.Network;

import java.util.List;


public class BannerImageAdapter extends BaseQuickAdapter<HomePageBean.ImgListBean, BaseViewHolder> {


    public BannerImageAdapter(List<HomePageBean.ImgListBean> data) {
        super(R.layout.item_image, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, final HomePageBean.ImgListBean item) {
        ImageView imageView = helper.getView(R.id.item_img);
        if (item.getImgUrl().startsWith("http")) {
            Glide.with(mContext).load(item).apply(new RequestOptions().error(R.drawable.banner01).placeholder(R.drawable.banner01))
                    .into(imageView);
        } else {
            imageView.setImageResource(Integer.parseInt(item.getImgUrl()));
        }
        helper.addOnClickListener(R.id.root);
    }
}
