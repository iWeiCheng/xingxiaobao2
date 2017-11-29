package com.jiajun.demo.moudle.vip;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jiajun.demo.R;
import com.jiajun.demo.base.BaseFragment;
import com.jiajun.demo.model.BaseBean;
import com.jiajun.demo.moudle.vip.adapter.MemberAdapter;
import com.jiajun.demo.moudle.vip.entities.PrivilegeBean;
import com.jiajun.demo.moudle.webview.WebViewActivity;
import com.jiajun.demo.network.BaseObserver;
import com.jiajun.demo.network.Network;
import com.jiajun.demo.util.RandomNum;
import com.jiajun.demo.util.SignatureUtil;
import com.jiajun.demo.views.CircleImageView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VIPFragment extends BaseFragment {

    @BindView(R.id.iv_user)
    CircleImageView ivUser;
    @BindView(R.id.iv_vip)
    ImageView ivVip;
    @BindView(R.id.tv_vip)
    TextView tvVip;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.tv_sign_or_recharge)
    TextView tvSignOrRecharge;
    @BindView(R.id.rl_vip0)
    RelativeLayout llVip0;
    @BindView(R.id.rl_vip1)
    RelativeLayout llVip1;
    @BindView(R.id.rl_vip2)
    RelativeLayout llVip2;
    @BindView(R.id.rl_vip3)
    RelativeLayout llVip3;
    @BindView(R.id.tv_vip0)
    TextView tvVip0;
    @BindView(R.id.tv_vip1)
    TextView tvVip1;
    @BindView(R.id.tv_vip2)
    TextView tvVip2;
    @BindView(R.id.tv_vip3)
    TextView tvVip3;
    @BindView(R.id.iv_member0)
    ImageView ivMember0;
    @BindView(R.id.iv_member1)
    ImageView ivMember1;
    @BindView(R.id.iv_member2)
    ImageView ivMember2;
    @BindView(R.id.iv_member3)
    ImageView ivMember3;
    @BindView(R.id.ll_get_vip)
    LinearLayout llGetVip;
    @BindView(R.id.recycler_view_member)
    RecyclerView recyclerViewMember;
    @BindView(R.id.recycler_view_grab_privilege)
    RecyclerView recyclerViewGrabPrivilege;
    @BindView(R.id.recycler_view_welfare)
    RecyclerView recyclerViewWelfare;


    private Subscription mSubscription;
    private PrivilegeBean privilegeBean;
    private int selectIndex = 0;
    private MemberAdapter adapter_members, adapter_grab, adapter_welfare;

    private BaseObserver<PrivilegeBean> observer = new BaseObserver<PrivilegeBean>() {

        @Override
        public void onSuccess(PrivilegeBean bean) {
            privilegeBean = bean;
            tvVip.setText(bean.getGrade_name());
            tvName.setText(bean.getName());
            tvIntegral.setText(String.format("积分：%d", bean.getSurplus_point()));
            if (bean.getGrade_name().equals("普通会员")) {
                llVip0.performClick();
            } else if (bean.getGrade_name().equals("一级特权")) {
                llVip1.performClick();

            } else if (bean.getGrade_name().equals("二级特权")) {
                llVip2.performClick();

            } else if (bean.getGrade_name().equals("三级特权")) {
                llVip3.performClick();

            }
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


    public static VIPFragment newInstance() {
        VIPFragment fragment = new VIPFragment();
        return fragment;
    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_vip;
    }

    @Override
    public void initView() {
        unsubscribe();
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        recyclerViewMember.setLayoutManager(manager);
        GridLayoutManager manager2 = new GridLayoutManager(getContext(), 4);
        recyclerViewGrabPrivilege.setLayoutManager(manager2);
        GridLayoutManager manager3 = new GridLayoutManager(getContext(), 4);
        recyclerViewWelfare.setLayoutManager(manager3);
        adapter_members = new MemberAdapter();
        adapter_grab = new MemberAdapter();
        adapter_welfare = new MemberAdapter();
        recyclerViewMember.setAdapter(adapter_members);
        recyclerViewGrabPrivilege.setAdapter(adapter_grab);
        recyclerViewWelfare.setAdapter(adapter_welfare);
        adapter_members.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PrivilegeBean.GradeListBean.PrivilegeListBean bean = adapter_members.getData().get(position);
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", bean.getOpen_grade_url());
            }
        });
        getPrivilege();
    }

    private void getPrivilege() {

        Map<String, String> map = new HashMap<>();
        map.put("method", "getVipPrivilege");
        map.put("userId", preferences.getString("user_id", ""));
        map.put("versionName", getResources().getString(R.string.versionName));
        String str_random = RandomNum.getrandom();
        map.put("random", str_random);
        String str_signature = SignatureUtil.getSignature(map);
        map.put("signature", str_signature);


        mSubscription = Network.getVipPrivilegeApi().getVipPrivilege(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    protected void managerArguments() {

    }

    @Override
    public String getUmengFragmentName() {
        return null;
    }


    @OnClick({R.id.titleRight_help,R.id.tv_integral, R.id.tv_sign_or_recharge, R.id.rl_vip0, R.id.rl_vip1, R.id.rl_vip2, R.id.rl_vip3, R.id.ll_get_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.titleRight_help:
                break;
            case R.id.tv_integral:
                if (privilegeBean == null) {
                    return;
                }
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", privilegeBean.getSurplus_point_url());
                startActivity(intent);
                break;
            case R.id.tv_sign_or_recharge:
                if (privilegeBean == null) {
                    return;
                }
                Intent intent2 = new Intent(getContext(), WebViewActivity.class);
                intent2.putExtra("url", privilegeBean.getSignIn_url());
                startActivity(intent2);
                break;
            case R.id.rl_vip0:
                selectIndex = 0;
                llVip0.setBackgroundResource(R.drawable.tq_denbg01);
                llVip1.setBackgroundResource(R.drawable.shape_white_corner);
                llVip2.setBackgroundResource(R.drawable.shape_white_corner);
                llVip3.setBackgroundResource(R.drawable.shape_white_corner);
                tvVip0.setTextColor(getResources().getColor(R.color.white));
                tvVip1.setTextColor(getResources().getColor(R.color.text_black));
                tvVip2.setTextColor(getResources().getColor(R.color.text_black));
                tvVip3.setTextColor(getResources().getColor(R.color.text_black));
                ivMember0.setVisibility(View.VISIBLE);
                ivMember1.setVisibility(View.INVISIBLE);
                ivMember2.setVisibility(View.INVISIBLE);
                ivMember3.setVisibility(View.INVISIBLE);
                setGradeListData(selectIndex);
                break;
            case R.id.rl_vip1:
                selectIndex = 1;
                llVip1.setBackgroundResource(R.drawable.tq_denbg01);
                llVip0.setBackgroundResource(R.drawable.shape_white_corner);
                llVip2.setBackgroundResource(R.drawable.shape_white_corner);
                llVip3.setBackgroundResource(R.drawable.shape_white_corner);
                tvVip1.setTextColor(getResources().getColor(R.color.white));
                tvVip0.setTextColor(getResources().getColor(R.color.text_black));
                tvVip2.setTextColor(getResources().getColor(R.color.text_black));
                tvVip3.setTextColor(getResources().getColor(R.color.text_black));
                ivMember1.setVisibility(View.VISIBLE);
                ivMember0.setVisibility(View.INVISIBLE);
                ivMember2.setVisibility(View.INVISIBLE);
                ivMember3.setVisibility(View.INVISIBLE);
                setGradeListData(selectIndex);
                break;
            case R.id.rl_vip2:
                selectIndex = 2;
                llVip2.setBackgroundResource(R.drawable.tq_denbg01);
                llVip0.setBackgroundResource(R.drawable.shape_white_corner);
                llVip1.setBackgroundResource(R.drawable.shape_white_corner);
                llVip3.setBackgroundResource(R.drawable.shape_white_corner);
                tvVip2.setTextColor(getResources().getColor(R.color.white));
                tvVip1.setTextColor(getResources().getColor(R.color.text_black));
                tvVip0.setTextColor(getResources().getColor(R.color.text_black));
                tvVip3.setTextColor(getResources().getColor(R.color.text_black));
                ivMember2.setVisibility(View.VISIBLE);
                ivMember1.setVisibility(View.INVISIBLE);
                ivMember0.setVisibility(View.INVISIBLE);
                ivMember3.setVisibility(View.INVISIBLE);
                setGradeListData(selectIndex);
                break;
            case R.id.rl_vip3:
                selectIndex = 3;
                llVip3.setBackgroundResource(R.drawable.tq_denbg01);
                llVip0.setBackgroundResource(R.drawable.shape_white_corner);
                llVip1.setBackgroundResource(R.drawable.shape_white_corner);
                llVip2.setBackgroundResource(R.drawable.shape_white_corner);
                tvVip3.setTextColor(getResources().getColor(R.color.white));
                tvVip1.setTextColor(getResources().getColor(R.color.text_black));
                tvVip2.setTextColor(getResources().getColor(R.color.text_black));
                tvVip0.setTextColor(getResources().getColor(R.color.text_black));
                ivMember3.setVisibility(View.VISIBLE);
                ivMember1.setVisibility(View.INVISIBLE);
                ivMember2.setVisibility(View.INVISIBLE);
                ivMember0.setVisibility(View.INVISIBLE);
                setGradeListData(selectIndex);
                break;
            case R.id.ll_get_vip:
                if (privilegeBean == null) {
                    return;
                }
                Intent intent_vip = new Intent(getContext(), WebViewActivity.class);
                intent_vip.putExtra("url", privilegeBean.getGradeList().get(selectIndex).getOpen_grade_url());
                startActivity(intent_vip);
                break;
        }
    }

    /**
     * 点击特权时重新载入数据
     *
     * @param selectIndex
     */
    private void setGradeListData(int selectIndex) {

        if (privilegeBean == null) {
            return;
        }
        PrivilegeBean.GradeListBean bean = privilegeBean.getGradeList().get(selectIndex);
        List<PrivilegeBean.GradeListBean.PrivilegeListBean> list_members = new ArrayList<>();
        List<PrivilegeBean.GradeListBean.PrivilegeListBean> list_grab = new ArrayList<>();
        List<PrivilegeBean.GradeListBean.PrivilegeListBean> list_welfare = new ArrayList<>();
        for (int i = 0; i < bean.getPrivilegeList().size(); i++) {
            if (bean.getPrivilegeList().get(i).getPosition().equals("1")) {
                list_members.add(bean.getPrivilegeList().get(i));
            } else if (bean.getPrivilegeList().get(i).getPosition().equals("2")) {
                list_grab.add(bean.getPrivilegeList().get(i));
            } else if (bean.getPrivilegeList().get(i).getPosition().equals("3")) {
                list_welfare.add(bean.getPrivilegeList().get(i));
            }
        }
        adapter_members.setNewData(list_members);
        adapter_grab.setNewData(list_grab);
        adapter_welfare.setNewData(list_welfare);
    }
}
