package com.jiajun.demo.moudle.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiajun.demo.R;
import com.jiajun.demo.base.BaseFragment;
import com.jiajun.demo.model.BaseBean;
import com.jiajun.demo.moudle.me.entities.UserInfoBean;
import com.jiajun.demo.moudle.webview.WebViewActivity;
import com.jiajun.demo.network.BaseObserver;
import com.jiajun.demo.network.Network;
import com.jiajun.demo.util.RandomNum;
import com.jiajun.demo.util.SignatureUtil;
import com.jiajun.demo.views.CircleImageView;
import com.jiajun.demo.views.TffTextView;
import com.orhanobut.logger.Logger;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MeFragment extends BaseFragment {

    @BindView(R.id.tv_message)
    ImageView tvMessage;
    @BindView(R.id.tv_message_count)
    TextView tvMessageCount;
    @BindView(R.id.iv_img)
    CircleImageView ivImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_acc_income)
    TffTextView tvAccIncome;
    @BindView(R.id.tv_balance)
    TffTextView tvBalance;
    @BindView(R.id.tv_self_acc)
    TextView tvSelfAcc;
    @BindView(R.id.tv_car_acc)
    TextView tvCarAcc;
    @BindView(R.id.tv_other_acc)
    TextView tvOtherAcc;

    private UserInfoBean userInfoBean;
    private Subscription mSubscription;

    private BaseObserver<UserInfoBean> observer = new BaseObserver<UserInfoBean>() {

        @Override
        public void onSuccess(UserInfoBean bean) {
            hideProgress();
            userInfoBean = bean;
            setUserData(bean);
        }

        @Override
        public void onError(int code, String message, BaseBean baseBean) {
            Logger.e("error:" + message);
            hideProgress();

        }

        @Override
        public void networkError(Throwable e) {
            Logger.e(e.getMessage());
            hideProgress();
        }
    };

    private void setUserData(UserInfoBean bean) {
        Glide.with(getContext()).load(Network.SERVICE + bean.getHeadImg())
//                .error(R.drawable.g_photo)
//                .placeholder(R.drawable.g_photo)
                .into(ivImg);
        tvName.setText(bean.getName());
        tvDesc.setText(bean.getLevel());
        tvAccIncome.setText(bean.getAccIncome());
        tvBalance.setText(bean.getBalance());
        tvSelfAcc.setText(bean.getARiskRevenue());
        tvCarAcc.setText(bean.getCarRevenue());
        tvOtherAcc.setText(bean.getOtherRevenue());
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_me;
    }

    private void getUserInfo() {
        showProgress();
        Map<String, String> map = new HashMap<>();
        map.put("method", "getUserinfo");
        map.put("userId", preferences.getString("user_id", ""));
        map.put("versionName", getResources().getString(R.string.versionName));
        String str_random = RandomNum.getrandom();
        map.put("random", str_random);
        String str_signature = SignatureUtil.getSignature(map);
        map.put("signature", str_signature);


        mSubscription = Network.getUserInfoApi().getUserinfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void initView() {
        unsubscribe();
        getUserInfo();
    }

    @Override
    protected void managerArguments() {

    }

    @Override
    public String getUmengFragmentName() {
        return null;
    }


    @OnClick({R.id.iv_img, R.id.tv_message, R.id.ll_self_order, R.id.ll_car_order, R.id.ll_other_order, R.id.tv_apply, R.id.ll_set_store, R.id.ll_article, R.id.ll_my_fans, R.id.ll_invite, R.id.tv_problem, R.id.tv_service, R.id.tv_setting})
    public void onViewClicked(View view) {
        if (userInfoBean == null) {
            return;
        }
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        String url = "";
        switch (view.getId()) {
            case R.id.iv_img:
                Intent personalIntent = new Intent(getContext(), PersonalInfoActivity.class);
                startActivity(personalIntent);
                break;
            case R.id.tv_message:
                break;
            case R.id.ll_self_order:
                url = userInfoBean.getARiskUrl();
                break;
            case R.id.ll_car_order:
                url = userInfoBean.getCarUrl();
                break;
            case R.id.ll_other_order:
                url = userInfoBean.getOtherOrderUrl();
                break;
            case R.id.tv_apply:
                url = userInfoBean.getCompensationUrl();
                break;
            case R.id.ll_set_store:
//                url = userInfoBean.get();
                break;
            case R.id.ll_article:
                url = userInfoBean.getPublishUrl();
                break;
            case R.id.ll_my_fans:
                url = userInfoBean.getMyFansUrl();

                break;
            case R.id.ll_invite:
                url = userInfoBean.getInvitFriend();

                break;
            case R.id.tv_problem:
                url = userInfoBean.getProblemUrl();

                break;
            case R.id.tv_service:
                String title = getResources().getString(R.string.app_name);
                ConsultSource source = new ConsultSource("http://www.implus100.com", "", "custom");
                Unicorn.openServiceActivity(getContext(), // 上下文
                        "返回", // 聊天窗口的标题
                        source // 咨询的发起来源，包括发起咨询的url，title，描述信息等
                );
                break;
            case R.id.tv_setting:
                url = userInfoBean.getSetManagURl();

                break;
        }
        if (url == null || url.length() == 0) {
            return;
        }
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
