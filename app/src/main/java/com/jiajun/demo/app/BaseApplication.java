package com.jiajun.demo.app;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.caijia.selectpicture.utils.FileUtil;
import com.facebook.stetho.Stetho;
import com.jiajun.demo.R;
import com.jiajun.demo.moudle.main.MainsActivity;
import com.jiajun.demo.network.Network;
import com.orhanobut.logger.Logger;
import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.SavePowerConfig;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnicornImageLoader;
import com.qiyukf.unicorn.api.YSFOptions;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


/**
 * 自定义应用入口
 *
 * @author Ht
 */
public class BaseApplication extends Application {
    public static final boolean DEBUG = false;
    public static final boolean USE_SAMPLE_DATA = false;

    public static final String APP_ID = "2882303761517567158";
    public static final String APP_KEY = "5951756743158";
    public static final String TAG = "com.ocnyang.qbox.app";

    private static BaseApplication mInstance;
    //    private static DemoHandler sHandler = null;
    private static MainsActivity sMainActivity = null;
    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;
    /**
     * 屏幕密度
     */
    public static float screenDensity;

    public static IWXAPI api;

    private YSFOptions options() {

        YSFOptions options = new YSFOptions();
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        UICustomization uiCustomization = new UICustomization();
        uiCustomization.titleBackgroundResId = R.drawable.service_title;
        uiCustomization.tipsTextColor = R.color.white;
        uiCustomization.titleBarStyle = 0;
        options.uiCustomization = uiCustomization;
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = MainsActivity.class;
        options.statusBarNotificationConfig = config;
        options.savePowerConfig = new SavePowerConfig();
        return options;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initScreenSize();
        Network.init(this);
        Stetho.initializeWithDefaults(this);
        regToWx();
        Unicorn.init(this, "414ed69394febb24e55139aaa6f123d1", options(), new UnicornImageLoader() {
            @Nullable
            @Override
            public Bitmap loadImageSync(String uri, int width, int height) {

                return null;
            }

            @Override
            public void loadImage(String uri, int width, int height, final ImageLoaderListener imageLoaderListener) {
                Glide.with(getApplicationContext()).load(uri)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (imageLoaderListener != null) {
                                    imageLoaderListener.onLoadFailed(e.getCause());
                                }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (imageLoaderListener != null) {
                                    BitmapDrawable bd = (BitmapDrawable) resource.getCurrent();
                                    imageLoaderListener.onLoadComplete(bd.getBitmap());
                                }
                                return false;
                            }
                        });
            }
        });

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        /*内存泄漏初始化*/
//        LeakCanary.install(this);

        /*Logger初始化*/
        Logger.init("OCN_Yang");

//        initImageLoader();

        //初始化小米push推送服务
//        if (shouldInit()) {
//            MiPushClient.registerPush(this, APP_ID, APP_KEY);
//        }

        //打开小米推送的Log
//        LoggerInterface newLogger = new LoggerInterface() {
//            @Override
//            public void setTag(String tag) {
//                // ignore
//            }
//
//            @Override
//            public void log(String content, Throwable t) {
//                Log.d(TAG, content, t);
//            }
//
//            @Override
//            public void log(String content) {
//                Log.d(TAG, content);
//            }
//        };
//        com.xiaomi.mipush.sdk.Logger.setLogger(this, newLogger);
//        if (sHandler == null) {
//            sHandler = new DemoHandler(getApplicationContext());
//        }

    }

    /**
     * 注册微信
     */
    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化imageloader
     */
//    private void initImageLoader() {
//        File cacheDir = StorageUtils.getOwnCacheDirectory(
//                getApplicationContext(), "imageloader/Cache");
//
//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                .cacheInMemory(true) // 加载图片时会在内存中加载缓存
//                .cacheOnDisk(true) // 加载图片时会在磁盘中加载缓存
//                .build();
//
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                this)
//                .threadPoolSize(3)
//                        // default
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                        // default
//                .tasksProcessingOrder(QueueProcessingType.FIFO)
//                        // default
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
//                        // default
//                .diskCache(new UnlimitedDiscCache(cacheDir))
//                        // default
//                .diskCacheSize(20 * 1024 * 1024).diskCacheFileCount(100)
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) // default
//                .defaultDisplayImageOptions(defaultOptions) // default
//                .writeDebugLogs().build();
//        ImageLoader.getInstance().init(config);
//    }
    public static Context getInstance() {
        return mInstance;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        try {
            PackageManager manager = mInstance.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mInstance.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当前系统语言
     *
     * @return 当前系统语言
     */
    public static String getLanguage() {
        Locale locale = mInstance.getResources().getConfiguration().locale;
        String language = locale.getDefault().toString();
        return language;
    }

    /**
     * 初始化当前设备屏幕宽高
     */
    private void initScreenSize() {
        DisplayMetrics curMetrics = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = curMetrics.widthPixels;
        screenHeight = curMetrics.heightPixels;
        screenDensity = curMetrics.density;
    }


//    public static DemoHandler getHandler() {
//        return sHandler;
//    }

    public static void setMainActivity(MainsActivity activity) {
        sMainActivity = activity;
    }

//    public static class DemoHandler extends Handler {
//
//        private Context context;
//
//        public DemoHandler(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            String s = (String) msg.obj;
//            if ((!TextUtils.isEmpty(s)) && s.indexOf("新版本") == 0) {
//                //通过小米推送的穿透推送，来推送新版本更新的通知
//                //通知的格式：
//                //新版本：2
//                String[] split = s.split(":");
//                try {
//                    String new_version = split[1];
//                    int integer = Integer.valueOf(new_version);
//                    if (integer < 100) {
//                        SPUtils.put(context, Const.QBOX_NEW_VERSION, integer);
//                    }else {
//                        switch(integer){
//                            case 101://魔法
//                                SPUtils.put(context,Const.OPENNEWS,"magicopen");
//                                break;
//                            default:
//
//                                break;
//                        }
//                    }
//                } catch (ArrayIndexOutOfBoundsException e) {
//                    Log.e("Qbox_version", e.getMessage());
//                } catch (Exception e) {
//                    Log.e("Qbox_Version", e.getMessage());
//                }
//            }
//            if (sMainActivity != null) {
//                sMainActivity.refreshLogInfo();
//            }
//            if (!TextUtils.isEmpty(s)) {
////                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    /**
     * 突破64K问题，MultiDex构建
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
