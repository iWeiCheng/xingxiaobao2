package com.jiajun.demo.moudle.webview;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jiajun.demo.R;
import com.jiajun.demo.app.BaseApplication;
import com.jiajun.demo.base.BaseActivity;
import com.jiajun.demo.moudle.account.LoginActivity;
import com.jiajun.demo.moudle.main.MainsActivity;
import com.jiajun.demo.util.BitmapUtil;
import com.jiajun.demo.util.FileUtils;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Cookie;


/**
 * webView详情页
 * Created by danjj on 2016/12/8.
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener, ShareFragment.OnClickShareListener {

    @BindView(R.id.titleLeft_textView)
    TextView titleLeftTextView;
    @BindView(R.id.titleRight)
    TextView titleRight;
    @BindView(R.id.titleRight_service)
    ImageView service;
    @BindView(R.id.titleCeneter_textView)
    TextView titleCeneterTextView;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.no_network_layout)
    RelativeLayout noNetWorkLayout;
    @BindView(R.id.reload)
    TextView reload;

    private String url1;
    private String backurl;
    private String share_title;
    private String share_image;
    private String share_content;
    private String share_link;

    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调</span>
    private Uri imageUri;
    private String imagePaths;
    private String compressPath;
    private BaseApplication application;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (share_link != null && share_link.length() > 0) {
                titleRight.setVisibility(View.VISIBLE);
            } else {
                titleRight.setVisibility(View.GONE);
            }
        }
    };

    private ShareFragment share_fragment;
    private IWXAPI api;

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_webview);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void getExtra(@NonNull Bundle bundle) {
        super.getExtra(bundle);
        url1 = bundle.getString("url");
    }

    @Override
    protected void setListener(Bundle savedInstanceState) {
        super.setListener(savedInstanceState);
        titleLeftTextView.setOnClickListener(this);
        titleRight.setOnClickListener(this);
        service.setOnClickListener(this);
        reload.setOnClickListener(this);


    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
//        BaseApplication application = (BaseApplication) getApplication();
        api = BaseApplication.api;
        Intent intent = getIntent();
        titleLeftTextView.setVisibility(View.VISIBLE);
//        titleRight.setVisibility(View.VISIBLE);
        titleRight.setBackgroundResource(R.drawable.share);
        service.setVisibility(View.VISIBLE);
        application = (BaseApplication) getApplication();
        webView.getSettings().setJavaScriptEnabled(true);
        synCookies(this, url1);

        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        webView.setWebChromeClient(new WebChromeClient());
        //启用数据库
        webView.getSettings().setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        webView.getSettings().setGeolocationEnabled(true);
        //设置定位的数据库路径
        webView.getSettings().setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        webView.getSettings().setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        webView.loadUrl(url1);
        webView.setWebViewClient(new WebViewClient() {


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                noNetWorkLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("url", url);
                if (url.contains("/user/login")) {
                    view.clearCache(true);
                    CookieJarImpl cj = (CookieJarImpl) OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
                    cj.getCookieStore().removeAll();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    callPhone(url);
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dismissDialog();
                noNetWorkLayout.setVisibility(View.GONE);
                view.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
                        "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                titleCeneterTextView.setText(view.getTitle());
                super.onPageFinished(view, url);
            }

        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                titleCeneterTextView.setText(title);
            }

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                take();
                return true;
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                take();
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                take();
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                take();
            }
        });
    }

    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_aaa111222333bbb" + ".jpg");
        File temp = new File(imageStorageDir + File.separator + "temp" + ".jpg");
        imageUri = Uri.fromFile(file);
        imagePaths = file.getAbsolutePath();
        compressPath = temp.getAbsolutePath();
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);
        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    public void synCookies(Context context, String url) {
        CookieJarImpl cj = (CookieJarImpl) OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
        List<Cookie> cookies = cj.getCookieStore().getCookies();
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.setAcceptCookie(true);
        StringBuffer sb = new StringBuffer();
        if (cookies.size() > 0) {
            String cookieString = cookies.get(0).name() + "=" + cookies.get(0).value() + "; domain=" + cookies.get(0).domain();
            cookieManager.setCookie(cookies.get(0).domain(), cookieString);
        }
        CookieSyncManager.getInstance().sync();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            if (backurl != null && !backurl.equals("") && backurl.length() == 1) {
//                int steps = Integer.parseInt(backurl);
//                for (int i = 0; i < steps; i++) {
//                    webView.goBack();
//                }
//                return false;
//            } else {
//                Intent in = new Intent(this, MainsActivity.class);
//                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(in);
//                setResult(RESULT_OK);
//                finish();
//                return false;
//            }
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClickShare(final int position) {
        share_fragment.dismiss();
        if (share_link == null || share_link.length() == 0) {
            share_title = preferences.getString("share_title", "");
            share_link = preferences.getString("share_url", "");
            share_image = preferences.getString("share_image_url", "");
            share_content = preferences.getString("share_content", "");
        }
        share_image = preferences.getString("share_image_url", "");

        Glide.with(this)
                .asBitmap()
                .load(share_image)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        switch (position) {
                            case 0://微信朋友圈
                                WXWebpageObject webpageObject = new WXWebpageObject();
                                webpageObject.webpageUrl = share_link;
                                WXMediaMessage msg = new WXMediaMessage(webpageObject);
                                msg.title = share_title;
                                msg.description = share_content;
                                msg.thumbData = BitmapUtil.bitmapToByte(resource);
                                SendMessageToWX.Req req = new SendMessageToWX.Req();
                                req.transaction = System.currentTimeMillis() + getResources().getString(R.string.app_name);
                                req.message = msg;
                                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                                api.sendReq(req);
                                break;
                            case 1://微信好友
                                WXWebpageObject webpageObject2 = new WXWebpageObject();
                                webpageObject2.webpageUrl = share_link;
                                WXMediaMessage msg2 = new WXMediaMessage(webpageObject2);
                                msg2.title = share_title;
                                msg2.description = share_content;
                                msg2.thumbData = BitmapUtil.bitmapToByte(resource);
                                SendMessageToWX.Req req2 = new SendMessageToWX.Req();
                                req2.transaction = System.currentTimeMillis() + getResources().getString(R.string.app_name);
                                req2.message = msg2;
                                req2.scene = SendMessageToWX.Req.WXSceneSession;
                                api.sendReq(req2);
                                break;
                            case 2://QQ好友

                                break;
                            case 3://QQ空间

                                break;

                            case 4://微信收藏
                                WXWebpageObject webpageObject3 = new WXWebpageObject();
                                webpageObject3.webpageUrl = share_link;
                                WXMediaMessage msg3 = new WXMediaMessage(webpageObject3);
                                msg3.title = share_title;
                                msg3.description = share_content;
                                msg3.thumbData = BitmapUtil.bitmapToByte(resource);
                                SendMessageToWX.Req req3 = new SendMessageToWX.Req();
                                req3.transaction = System.currentTimeMillis() + getResources().getString(R.string.app_name);
                                req3.message = msg3;
                                req3.scene = SendMessageToWX.Req.WXSceneFavorite;
                                api.sendReq(req3);
                                break;

                            case 5://短信
                                sendSMS();
                                break;

                        }
                    }
                });
    }


    /**
     * 发短信
     */

    private void sendSMS() {
        String smsBody = share_title + ":" + share_content + "  这是网址《" + share_link + "》";
        Uri smsToUri = Uri.parse("smsto:");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
        //sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
        //短信内容
        sendIntent.putExtra("sms_body", smsBody);
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivityForResult(sendIntent, 1002);
    }

    /**
     * 逻辑处理
     *
     * @author linzewu
     */
    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getSource(String html) {
            Log.d("html=", html);
            Document document = Jsoup.parse(html);
            getShareDate(document);
        }
    }

    public void getShareDate(Document doc) {
        Elements E = doc.select("meta");
        backurl = doc.select("meta").attr("backurl");//新增
        share_title = doc.select("meta").attr("share_title");
        share_image = doc.select("meta").attr("share_image");
        share_content = doc.select("meta").attr("share_content");
        share_link = doc.select("meta").attr("share_link");
        handler.sendEmptyMessage(1);
    }

    public Bitmap getAppIcon() {
        getPackageManager().getApplicationLogo(getApplicationInfo());
        Drawable d = getPackageManager().getApplicationIcon(getApplicationInfo()); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        return drawableToBitamp(bd);
    }

    private Bitmap drawableToBitamp(Drawable drawable) {
        Bitmap bitmap;
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                Log.e("result", result + "");
                if (result == null) {
                    afterOpenCamera();
                    imageUri = Uri.fromFile(new File(compressPath));
                    mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage = null;
                    Log.e("imageUri", imageUri + "");
                } else {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }


            }
        }
    }

    /**
     * 解决拍照后在相册中找不到的问题
     */
    private void addImageGallery(File file) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    /**
     * 拍照结束后
     */
    private void afterOpenCamera() {
        File f = new File(imagePaths);
        addImageGallery(f);
        File newFile = FileUtils.compressFile(f.getPath(), compressPath);
    }

    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                afterOpenCamera();
                imageUri = Uri.fromFile(new File(compressPath));
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }

        return;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleRight://分享
                //appkey:584a4a6d8f4a9d7121000f9c
                if (share_fragment == null) {
                    share_fragment = ShareFragment.getInstance();
                    share_fragment.setListener(this);
                }
                share_fragment.show(getSupportFragmentManager(), "");
                break;
            case R.id.titleLeft_textView:
                if (backurl != null && !backurl.equals("") && backurl.length() == 1) {
                    int steps = Integer.parseInt(backurl);
                    for (int i = 0; i < steps; i++) {
                        webView.goBack();
                    }
                } else {
                    Intent in = new Intent(this, MainsActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            case R.id.reload:
                showProgressDialog();
                webView.reload();
                break;
            case R.id.titleRight_service:
                String title = getResources().getString(R.string.app_name);
                ConsultSource source = new ConsultSource("http://www.implus100.com", "", "custom");
                Unicorn.openServiceActivity(this, // 上下文
                        "返回", // 聊天窗口的标题
                        source // 咨询的发起来源，包括发起咨询的url，title，描述信息等
                );
                break;
        }
    }

    private void callPhone(String phone) {
        Intent call_intent = new Intent(Intent.ACTION_CALL, Uri.parse(phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(call_intent);
    }
}
