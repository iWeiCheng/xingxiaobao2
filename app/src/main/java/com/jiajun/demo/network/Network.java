package com.jiajun.demo.network;

import android.content.Context;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jiajun.demo.network.api.CustomerApi;
import com.jiajun.demo.network.api.GetCodeApi;
import com.jiajun.demo.network.api.GetGuideImagesApi;
import com.jiajun.demo.network.api.GetOrgInfoApi;
import com.jiajun.demo.network.api.GetPersonalApi;
import com.jiajun.demo.network.api.GetVersionApi;
import com.jiajun.demo.network.api.HomePageApi;
import com.jiajun.demo.network.api.LoginApi;
import com.jiajun.demo.network.api.NewsApi;
import com.jiajun.demo.network.api.PrivilegeApi;
import com.jiajun.demo.network.api.RegisterApi;
import com.jiajun.demo.network.api.ResetPwdApi;
import com.jiajun.demo.network.api.TestApi;
import com.jiajun.demo.network.api.UploadHeadImgApi;
import com.jiajun.demo.network.api.UserInfoApi;
import com.jiajun.demo.network.api.WechatApi;
import com.jiajun.demo.util.IMSI;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * api
 */


public class Network {

    public static final String ROOT_URL = "http://v.juhe.cn/";

    private static TestApi testApi;
    private static NewsApi sNewsApi;
    private static WechatApi mWechatApi;

    private static GetVersionApi getVersionApi;
    private static GetOrgInfoApi getOrgInfoApi;
    private static RegisterApi registerApi;
    private static LoginApi loginApi;
    private static GetCodeApi getCodeApi;
    private static ResetPwdApi resetPwdApi;
    private static GetGuideImagesApi guideImagesApi;
    private static HomePageApi homePageApi;
    private static PrivilegeApi privilegeApi;
    private static CustomerApi customerApi;
    private static UserInfoApi userinfoApi;
    private static GetPersonalApi personalApi;
    private static UploadHeadImgApi uploadHeadImgApi;


    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static String KEY = "20171025#$@";
    public static String BASE_URL = "http://admintest.implus100.com/agent/interface/";
    public static String SERVICE = "http://admintest.implus100.com";
//    public static String BASE_URL = "http://192.168.31.98:8080/agent/interface/";

    public Network() {

    }

    public static void init(Context context) {
        Map<String, Object> map = new HashMap<>();
        map.put("platform", "2");
        map.put("imsi", IMSI.getImsi(context));
        NetworkInterceptor interceptor = new NetworkInterceptor(map);
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(context));
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .build();
        OkHttpUtils.initClient(okHttpClient);
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
    }

    public static NewsApi getNewsApi() {
        if (sNewsApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(ROOT_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sNewsApi = retrofit.create(NewsApi.class);
        }
        Log.e("oooooo", "getNewsApi");
        return sNewsApi;
    }

    public static WechatApi getWechatApi() {
        if (mWechatApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(ROOT_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            mWechatApi = retrofit.create(WechatApi.class);
        }
        Log.e("oooooo", "getwechatApi");
        return mWechatApi;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static GetVersionApi getVersionApi() {
        if (getVersionApi == null) {
            getVersionApi = retrofit.create(GetVersionApi.class);
        }
        Log.e("oooooo", "login");
        return getVersionApi;
    }

    /**
     * 注册
     *
     * @return
     */
    public static RegisterApi RegisterApi() {
        if (registerApi == null) {
            registerApi = retrofit.create(RegisterApi.class);
        }
        Log.e("oooooo", "register");
        return registerApi;
    }

    /**
     * 获取集团公司
     *
     * @return
     */
    public static GetOrgInfoApi getOrgInfoApi() {
        if (getOrgInfoApi == null) {
            getOrgInfoApi = retrofit.create(GetOrgInfoApi.class);
        }
        Log.e("oooooo", "getOrgInfoApi");
        return getOrgInfoApi;
    }

    /**
     * 登录
     *
     * @return
     */
    public static LoginApi login() {
        if (loginApi == null) {
            loginApi = retrofit.create(LoginApi.class);
        }
        Log.e("oooooo", "login");
        return loginApi;
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public static GetCodeApi getCode() {
        if (getCodeApi == null) {
            getCodeApi = retrofit.create(GetCodeApi.class);
        }
        Log.e("oooooo", "getcode");
        return getCodeApi;
    }

    /**
     * 重置密码
     *
     * @return
     */
    public static ResetPwdApi resetPwd() {
        if (resetPwdApi == null) {
            resetPwdApi = retrofit.create(ResetPwdApi.class);
        }
        Log.e("oooooo", "resetpwd");
        return resetPwdApi;
    }

    /**
     * 获取首次加载广告图片
     *
     * @return
     */
    public static GetGuideImagesApi getGuideImages() {
        if (guideImagesApi == null) {
            guideImagesApi = retrofit.create(GetGuideImagesApi.class);
        }
        Log.e("oooooo", "resetpwd");
        return guideImagesApi;
    }

    /**
     * 获取首页信息
     *
     * @return
     */
    public static HomePageApi getHomePage() {
        if (homePageApi == null) {
            homePageApi = retrofit.create(HomePageApi.class);
        }
        Log.e("oooooo", "resetpwd");
        return homePageApi;
    }


    /**
     * 获取vip特权信息
     *
     * @return
     */
    public static PrivilegeApi getVipPrivilegeApi() {
        if (privilegeApi == null) {
            privilegeApi = retrofit.create(PrivilegeApi.class);
        }
        Log.e("oooooo", "getVipPrivilegeApi");
        return privilegeApi;
    }

    /**
     * 获取客户列表
     *
     * @return
     */
    public static CustomerApi getCustomerApi() {
        if (customerApi == null) {
            customerApi = retrofit.create(CustomerApi.class);
        }
        Log.e("oooooo", "getCustomerApi");
        return customerApi;
    }

    /**
     * 获取我的信息
     *
     * @return
     */
    public static UserInfoApi getUserInfoApi() {
        if (userinfoApi == null) {
            userinfoApi = retrofit.create(UserInfoApi.class);
        }
        Log.e("oooooo", "getUserInfoApi");
        return userinfoApi;
    }

    /**
     * 获取个人中心信息
     *
     * @return
     */
    public static GetPersonalApi getPersonalApi() {
        if (personalApi == null) {
            personalApi = retrofit.create(GetPersonalApi.class);
        }
        Log.e("oooooo", "getUserInfoApi");
        return personalApi;
    }

    /**
     * 上传头像
     *
     * @return
     */
    public static UploadHeadImgApi GetUploadHeadImgApi() {
        if (uploadHeadImgApi == null) {
            uploadHeadImgApi = retrofit.create(UploadHeadImgApi.class);
        }
        Log.e("oooooo", "uploadHeadImgApi");
        return uploadHeadImgApi;
    }


}
