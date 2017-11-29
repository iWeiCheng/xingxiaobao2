package com.jiajun.demo.moudle.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caijia.selectpicture.bean.MediaBean;
import com.caijia.selectpicture.ui.SelectMediaActivity;
import com.caijia.selectpicture.utils.MediaType;
import com.jiajun.demo.R;
import com.jiajun.demo.base.BaseActivity;
import com.jiajun.demo.model.BaseBean;
import com.jiajun.demo.moudle.account.LoginActivity;
import com.jiajun.demo.moudle.account.entities.LoginBean;
import com.jiajun.demo.moudle.main.MainsActivity;
import com.jiajun.demo.moudle.me.entities.PersonalBean;
import com.jiajun.demo.moudle.webview.WebViewActivity;
import com.jiajun.demo.network.BaseObserver;
import com.jiajun.demo.network.Network;
import com.jiajun.demo.util.FileUtils;
import com.jiajun.demo.util.RandomNum;
import com.jiajun.demo.util.SignatureUtil;
import com.orhanobut.logger.Logger;
import com.qiyukf.unicorn.api.Unicorn;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 个人中心
 * Created by dan on 2017/11/29/029.
 */

public class PersonalInfoActivity extends BaseActivity {
    @BindView(R.id.titleCeneter_textView)
    TextView titleCeneterTextView;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.id_number)
    TextView idNumber;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.group)
    TextView group;
    @BindView(R.id.qrcode)
    ImageView qrcode;
    @BindView(R.id.update)
    TextView update;

    private int SELECT_MEDIA_RQ = 101;

    private PersonalBean personalBean;

    private Subscription mSubscription;

    private BaseObserver<PersonalBean> observer = new BaseObserver<PersonalBean>() {

        @Override
        public void onSuccess(PersonalBean bean) {
            dismissDialog();
            personalBean = bean;
            Glide.with(getContext())
                    .asBitmap()
                    .load(Network.SERVICE + bean.getHeadImg())
                    .into(icon);
            name.setText(bean.getName());
            mobile.setText(bean.getMobile());
            email.setText(bean.getEmail());
            idNumber.setText(bean.getCard_no());
            group.setText(String.format("%s-%s", bean.getOrganizationName(), bean.getCompanyName()));
        }

        @Override
        public void onError(int code, String message, BaseBean baseBean) {
            dismissDialog();
            Logger.e("error:" + message);
        }

        @Override
        public void networkError(Throwable e) {
            dismissDialog();
            Logger.e(e.getMessage());
        }
    };

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_personal);
    }

    @Override
    public void initPresenter() {
        titleCeneterTextView.setText("个人中心");
        unsubscribe();
        getInfo();
    }

    private void getInfo() {
        String str_random = RandomNum.getrandom();
        Map<String, String> map = new HashMap<>();
        map.put("method", "getPersonal");
        map.put("userId", preferences.getString("user_id", ""));
        map.put("versionName", getResources().getString(R.string.versionName));
        map.put("random", str_random);
        String str_signature = SignatureUtil.getSignature(map);
        map.put("signature", str_signature);

        mSubscription = Network.getPersonalApi().getPersonal(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    @OnClick({R.id.titleLeft_textView, R.id.icon_layout, R.id.qrcode_layout, R.id.update, R.id.logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.titleLeft_textView:
                finish();
                break;
            case R.id.icon_layout:
                Intent intent_carema = new SelectMediaActivity.IntentBuilder(this)
                        .mediaType(MediaType.IMAGE)
                        .canMultiSelect(false)
                        .hasCamera(true)
                        .build();
                startActivityForResult(intent_carema, SELECT_MEDIA_RQ);
                break;
            case R.id.qrcode_layout:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", personalBean.getQcode());
                startActivity(intent);
                break;
            case R.id.update:
                Uri uri = Uri.parse(personalBean.getDown_url());
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;
            case R.id.logout:
                Unicorn.setUserInfo(null);
                Intent logoutIntent = new Intent(getContext(), LoginActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                logoutIntent.putExtra("type", "logout");
                startActivity(logoutIntent);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle args = data.getExtras();
        if (args != null) {
            String str_random = RandomNum.getrandom();
            Map<String, String> map = new HashMap<>();
//            map.put("method", "uploadHeadImg");
//            map.put("userId", preferences.getString("user_id", ""));
//            map.put("random", str_random);
//            String str_signature = SignatureUtil.getSignature(map);
//            map.put("signature", str_signature);
            map.put("method", "uploadHeadImg");
            map.put("random", "1639084458");
            map.put("signature", "A8B967C75C5C853C35D140A186F96EAA");
            map.put("userId", "297ebe0e55dd69430155de1e99db006e");


            MediaBean mediaBean = args.getParcelable(SelectMediaActivity.RESULT_MEDIA);

            File file = new File(mediaBean.getPath());

            RequestBody requestBody = RequestBody.create(
                    okhttp3.MediaType.parse("application/octet-stream"),
                    file);

            MultipartBody.Part part = MultipartBody.Part.createFormData("uploadHeadImg",
                    file.getName(), requestBody);

            Network.GetUploadHeadImgApi()
                    .uploadHeadImg("http://admintest.implus100.com/agent/interface/marke_Interface.jsp", map, part)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }
}
