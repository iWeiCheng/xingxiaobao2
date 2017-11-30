package com.jiajun.demo.moudle.homepage;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.caijia.widget.looperrecyclerview.LooperPageRecyclerView;
import com.caijia.widget.looperrecyclerview.RecyclerViewCircleIndicator;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jiajun.demo.R;
import com.jiajun.demo.base.BaseFragment;
import com.jiajun.demo.model.BaseBean;
import com.jiajun.demo.moudle.homepage.adapter.BannerImageAdapter;
import com.jiajun.demo.moudle.homepage.adapter.HotAdapter;
import com.jiajun.demo.moudle.homepage.adapter.PolicyMenuAdapter;
import com.jiajun.demo.moudle.homepage.entities.GuideImagesBean;
import com.jiajun.demo.moudle.homepage.entities.HomePageBean;
import com.jiajun.demo.moudle.webview.WebViewActivity;
import com.jiajun.demo.network.BaseObserver;
import com.jiajun.demo.network.Network;
import com.jiajun.demo.util.RandomNum;
import com.jiajun.demo.util.RecyclerViewDivider;
import com.jiajun.demo.util.SignatureUtil;
import com.jungly.gridpasswordview.XKeyboardView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomepageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomepageFragment extends BaseFragment {


    @BindView(R.id.view_pager_banner)
    LooperPageRecyclerView looperPageRecyclerView;
    @BindView(R.id.indicator)
    RecyclerViewCircleIndicator indicator;
    @BindView(R.id.tv_car_insurance)
    TextView tvCarInsurance;
    @BindView(R.id.recycler_view_policy_menu)
    RecyclerView recyclerViewMenu;
    @BindView(R.id.recycler_hot)
    RecyclerView recyclerHot;
    @BindView(R.id.ll_more_hot)
    LinearLayout llMoreHot;
    @BindView(R.id.iv_product1)
    ImageView ivProduct1;
    @BindView(R.id.tv_product1)
    TextView tvProduct1;
    @BindView(R.id.tv_product1_desc)
    TextView tvProduct1Desc;
    @BindView(R.id.rl_product_1)
    RelativeLayout rlProduct1;
    @BindView(R.id.iv_product2)
    ImageView ivProduct2;
    @BindView(R.id.tv_product2)
    TextView tvProduct2;
    @BindView(R.id.tv_product2_desc)
    TextView tvProduct2Desc;
    @BindView(R.id.rl_product_2)
    RelativeLayout rlProduct2;
    @BindView(R.id.iv_product3)
    ImageView ivProduct3;
    @BindView(R.id.tv_product3)
    TextView tvProduct3;
    @BindView(R.id.tv_product3_desc)
    TextView tvProduct3Desc;
    @BindView(R.id.rl_product_3)
    RelativeLayout rlProduct3;
    @BindView(R.id.iv_product4)
    ImageView ivProduct4;
    @BindView(R.id.tv_product4)
    TextView tvProduct4;
    @BindView(R.id.tv_product4_desc)
    TextView tvProduct4Desc;
    @BindView(R.id.rl_product_4)
    RelativeLayout rlProduct4;
    @BindView(R.id.iv_product5)
    ImageView ivProduct5;
    @BindView(R.id.tv_product5)
    TextView tvProduct5;
    @BindView(R.id.tv_product5_desc)
    TextView tvProduct5Desc;
    @BindView(R.id.rl_product_5)
    RelativeLayout rlProduct5;
    @BindView(R.id.recycler_guess)
    RecyclerView recyclerGuess;
    @BindView(R.id.ll_car_number)
    LinearLayout ll_car_number;

    private BannerImageAdapter bannerImageAdapter;
    private List<HomePageBean.ImgListBean> bannerImgs;
    private HomePageBean homePageBean;

    private Subscription mSubscription;
    private String userId;


    private PolicyMenuAdapter policyMenuAdapter;
    private HotAdapter hotAdapter;


    private SelectCarNumberFragment selectCarNumberFragment;

    public static HomepageFragment newInstance() {
        HomepageFragment fragment = new HomepageFragment();
        return fragment;
    }

    //    private BaseObserver<GuideImagesBean> observer = new BaseObserver<GuideImagesBean>() {
//
//        @Override
//        public void onSuccess(GuideImagesBean bean) {
//            Logger.e("bean:" + bean.toString());
//            if (bean.getImages().size() > 0) {
//                bannerImgs.clear();
//                for (int i = 0; i < bean.getImages().size(); i++) {
//                    bannerImgs.add(bean.getImages().get(i).getImagePath());
//                }
//            }
//        }
//
//        @Override
//        public void onError(int code, String message, BaseBean baseBean) {
//            Logger.e("error:" + message);
//        }
//
//        @Override
//        public void networkError(Throwable e) {
//            Logger.e(e.getMessage());
//        }
//    };
    private BaseObserver<HomePageBean> observer_homepage = new BaseObserver<HomePageBean>() {

        @Override
        public void onSuccess(HomePageBean bean) {
            homePageBean = bean;
            policyMenuAdapter = new PolicyMenuAdapter(bean.getPolicyMenu());
            recyclerViewMenu.setAdapter(policyMenuAdapter);
            policyMenuAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("url", policyMenuAdapter.getItem(position).getUrl());
                    startActivity(intent);
                }
            });

            hotAdapter = new HotAdapter(bean.getHostMenu());
            recyclerHot.setAdapter(hotAdapter);
            setNewData(bean.getNewMenu());

            if (bean.getImgList().size() > 0) {
                bannerImgs.clear();
                bannerImgs = bean.getImgList();
            }
            bannerImageAdapter.notifyDataSetChanged();
            preferences.edit().putString("share_title", bean.getShare_title()).apply();
            preferences.edit().putString("share_url", bean.getShare_url()).apply();
            preferences.edit().putString("share_image_url", bean.getShare_image_url()).apply();
            preferences.edit().putString("share_content", bean.getShare_content()).apply();

        }

        @Override
        public void onError(int code, String message, BaseBean baseBean) {
            Logger.e("error:" + message);
        }

        @Override
        public void networkError(Throwable e) {
            Logger.e(e.getMessage());
        }
    };

    private void setNewData(List<HomePageBean.NewMenuBean> newMenu) {
        for (int i = 0; i < newMenu.size(); i++) {
            final HomePageBean.NewMenuBean bean = newMenu.get(i);
            switch (i) {
                case 0:
                    tvProduct1.setText(bean.getTitle());
                    tvProduct1Desc.setText(bean.getDescription());
                    Glide.with(getContext())
                            .load(Network.SERVICE + bean.getImgUrl())
                            .apply(new RequestOptions().placeholder(R.drawable.lei01).error(R.drawable.lei01))
                            .into(ivProduct1);
                    rlProduct1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), WebViewActivity.class);
                            intent.putExtra("url", bean.getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    tvProduct2.setText(bean.getTitle());
                    tvProduct2Desc.setText(bean.getDescription());
                    Glide.with(getContext()).load(Network.SERVICE + bean.getImgUrl())
                            .apply(new RequestOptions().placeholder(R.drawable.lei02).error(R.drawable.lei02))
                            .into(ivProduct2);
                    rlProduct2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), WebViewActivity.class);
                            intent.putExtra("url", bean.getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    tvProduct3.setText(bean.getTitle());
                    tvProduct3Desc.setText(bean.getDescription());
                    Glide.with(getContext()).load(Network.SERVICE + bean.getImgUrl())
                            .apply(new RequestOptions().placeholder(R.drawable.lei03).error(R.drawable.lei03))

                            .into(ivProduct3);
                    rlProduct3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), WebViewActivity.class);
                            intent.putExtra("url", bean.getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
                case 3:
                    tvProduct4.setText(bean.getTitle());
                    tvProduct4Desc.setText(bean.getDescription());
                    Glide.with(getContext()).load(Network.SERVICE + bean.getImgUrl())
                            .apply(new RequestOptions().placeholder(R.drawable.lei04).error(R.drawable.lei04))
                            .into(ivProduct4);
                    rlProduct4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), WebViewActivity.class);
                            intent.putExtra("url", bean.getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
                case 4:
                    tvProduct5.setText(bean.getTitle());
                    tvProduct5Desc.setText(bean.getDescription());
                    Glide.with(getContext()).load(Network.SERVICE + bean.getImgUrl())
                            .apply(new RequestOptions().placeholder(R.drawable.lei05).error(R.drawable.lei05))
                            .into(ivProduct5);
                    rlProduct5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), WebViewActivity.class);
                            intent.putExtra("url", bean.getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_homepage;
    }

    @Override
    public void initView() {
        initCarNumber();
        userId = preferences.getString("user_id", "");
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        recyclerViewMenu.setLayoutManager(manager);
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        recyclerHot.setLayoutManager(manager1);
        recyclerHot.addItemDecoration(new RecyclerViewDivider(
                getContext(), LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(getContext(), R.color.gray_line)));

        LinearLayoutManager manager2 = new LinearLayoutManager(getContext());
        recyclerGuess.setLayoutManager(manager2);

        recyclerGuess.addItemDecoration(new RecyclerViewDivider(
                getContext(), LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(getContext(), R.color.gray_line)));

        looperPageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        bannerImgs = new ArrayList<>();
        bannerImgs.add(new HomePageBean.ImgListBean("", R.drawable.banner01 + "", ""));
        bannerImageAdapter = new BannerImageAdapter(bannerImgs);
        looperPageRecyclerView.setAdapter(bannerImageAdapter);
        indicator.setupWithRecyclerView(looperPageRecyclerView);


        unsubscribe();
//        getGuideImages();
        getHomePageInfo();
    }

    private void initCarNumber() {
        for (int i = 0; i < 7; i++) {
            final int index = i;
            final TextView textView = (TextView) ll_car_number.getChildAt(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < 7; j++) {
                        ll_car_number.getChildAt(j).setSelected(false);
                    }
                    textView.setSelected(true);
                    selectCarNumberFragment = SelectCarNumberFragment.getInstance(index);
                    selectCarNumberFragment.setClickItemListener(new SelectCarNumberFragment.OnClickSubmitListener() {
                        @Override
                        public void onClickItem(int index, String text) {
                            TextView view = (TextView) ll_car_number.getChildAt(index);
                            view.setText(text);
                            nextSelect(index);
                        }
                    });
                    selectCarNumberFragment.show(getFragmentManager(), "select");
                }
            });
        }
    }

    private void nextSelect(int index) {
        for (int i = 0; i < 7; i++) {
            ll_car_number.getChildAt(i).setSelected(false);
        }
        ll_car_number.getChildAt(index + 1).setSelected(true);
    }

    private void getHomePageInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("method", "getHomePage");
        map.put("userId", preferences.getString("user_id", ""));
        map.put("versionName", getResources().getString(R.string.versionName));
        String str_random = RandomNum.getrandom();
        map.put("random", str_random);
        String str_signature = SignatureUtil.getSignature(map);
        map.put("signature", str_signature);

        mSubscription = Network.getHomePage().getHomePage(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer_homepage);
    }

//    private void getGuideImages() {
//        Map<String, String> map = new HashMap<>();
//        map.put("method", "getGuideImages");
//        map.put("type", "1");
//        map.put("versionName", getResources().getString(R.string.versionName));
//        String str_random = RandomNum.getrandom();
//        map.put("random", str_random);
//        String str_signature = SignatureUtil.getSignature(map);
//        map.put("signature", str_signature);
//
//        mSubscription = Network.getGuideImages().getGuideImages(map)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    protected void managerArguments() {

    }

    @Override
    public String getUmengFragmentName() {
        return null;
    }


    @OnClick({R.id.tv_car_insurance, R.id.ll_more_hot})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        String url = "";
        switch (view.getId()) {
            case R.id.tv_car_insurance://车险报价
                if (homePageBean == null) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 7; i++) {
                    TextView textView = (TextView) ll_car_number.getChildAt(i);
                    sb.append(textView.getText());
                }
                url = homePageBean.getInsure_car_url() + "&car_number=" + sb.toString();
                break;
            case R.id.ll_more_hot://更多热销
                url = homePageBean.getHost_all_url();
                break;
        }
        if (url == null || url.length() == 0) {
            return;
        }
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
