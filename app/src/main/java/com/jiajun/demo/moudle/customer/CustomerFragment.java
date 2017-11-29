package com.jiajun.demo.moudle.customer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jiajun.demo.R;
import com.jiajun.demo.base.BaseFragment;
import com.jiajun.demo.model.BaseBean;
import com.jiajun.demo.moudle.customer.adapter.CustomerAdapter;
import com.jiajun.demo.moudle.customer.entities.CustomerBean;
import com.jiajun.demo.moudle.homepage.entities.HomePageBean;
import com.jiajun.demo.network.BaseObserver;
import com.jiajun.demo.network.Network;
import com.jiajun.demo.util.RandomNum;
import com.jiajun.demo.util.SignatureUtil;
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
 * Use the {@link CustomerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.titleCeneter_textView)
    TextView titleCeneterTextView;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swiper)
    SwipeRefreshLayout swiper;


    private String keyword = "";
    private int pageSize = 10;
    private int curPage = 1;
    private CustomerAdapter adapter;
    private List<CustomerBean> list;

    private Subscription mSubscription;


    private BaseObserver<List<CustomerBean>> observer = new BaseObserver<List<CustomerBean>>() {

        @Override
        public void onSuccess(List<CustomerBean> bean) {
            if (curPage == 1) {
                list = bean;
                adapter.setNewData(list);

            } else {
                adapter.addData(bean);
            }
            curPage++;
            if (swiper.isRefreshing()) {
                swiper.setRefreshing(false);
            }
            if (!swiper.isEnabled()) {
                swiper.setEnabled(true);
            }
            if (adapter.isLoading()) {
                adapter.loadMoreComplete();
            }
            if (bean.size() < 10) {
                adapter.setEnableLoadMore(false);
            } else {
                adapter.setEnableLoadMore(true);
            }
        }

        @Override
        public void onError(int code, String message, BaseBean baseBean) {
            Logger.e("error:" + message);
            if (swiper.isRefreshing()) {
                swiper.setRefreshing(false);
            }
            if (!swiper.isEnabled()) {
                swiper.setEnabled(true);
            }
            if (adapter.isLoading()) {
                adapter.loadMoreComplete();
            }
            if (!adapter.isLoadMoreEnable()) {
                adapter.setEnableLoadMore(true);
            }
        }

        @Override
        public void networkError(Throwable e) {
            Logger.e(e.getMessage());
            if (swiper.isRefreshing()) {
                swiper.setRefreshing(false);
            }
            if (!swiper.isEnabled()) {
                swiper.setEnabled(true);
            }
            if (adapter.isLoading()) {
                adapter.loadMoreComplete();
            }
            if (!adapter.isLoadMoreEnable()) {
                adapter.setEnableLoadMore(true);
            }
        }
    };

    public static CustomerFragment newInstance() {
        CustomerFragment fragment = new CustomerFragment();
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_customer;
    }

    @Override
    public void initView() {
        unsubscribe();
        initSwipeRefresh();
        titleCeneterTextView.setText("我的客户");
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        list = new ArrayList<>();
        adapter = new CustomerAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.openLoadAnimation();
        adapter.setOnLoadMoreListener(this, recyclerView);
        adapter.setEmptyView(R.layout.empty);
        getCustomerList();
    }


    private void initSwipeRefresh() {
        swiper.setOnRefreshListener(this);
        swiper.setEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCustomerList();
    }

    private void getCustomerList() {
        Logger.e("page",curPage);
        Map<String, String> map = new HashMap<>();
        map.put("method", "getCustomer");
        map.put("userId", preferences.getString("user_id", ""));
        map.put("curPage", curPage + "");
        map.put("pageSize", pageSize + "");
        String str_random = RandomNum.getrandom();
        map.put("random", str_random);
        String str_signature = SignatureUtil.getSignature(map);
        map.put("signature", str_signature);
        map.put("keyword ", keyword);


        mSubscription = Network.getCustomerApi().getCustomer(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
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


    @OnClick(R.id.iv_search)
    public void onViewClicked() {
    }

    @Override
    public void onLoadMoreRequested() {
        getCustomerList();
    }

    @Override
    public void onRefresh() {
        curPage = 1;
        getCustomerList();
    }
}
