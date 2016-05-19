package com.cniao5.app36kr_cnk.ui;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cniao5.app36kr_cnk.R;
import com.cniao5.app36kr_cnk.adapter.LeftItemAdapter;
import com.cniao5.app36kr_cnk.application.CNKApplication;
import com.cniao5.app36kr_cnk.biz.HeadDataManager;
import com.cniao5.app36kr_cnk.common.DefineView;
import com.cniao5.app36kr_cnk.common.DeliverConsts;
import com.cniao5.app36kr_cnk.common.RequestURL;
import com.cniao5.app36kr_cnk.entity.AdHeadBean;
import com.cniao5.app36kr_cnk.entity.Person;
import com.cniao5.app36kr_cnk.ui.base.BaseActivity;
import com.cniao5.app36kr_cnk.update.UpdateInfoModel;
import com.cniao5.app36kr_cnk.update.UpdateReceiver;
import com.cniao5.app36kr_cnk.utils.OkhttpManager;
import com.cniao5.app36kr_cnk.widget.DragLayout;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * 当前类注释:主Activity类
 * ProjectName：App36Kr
 * Author:<a href="http://www.cniao5.com">菜鸟窝</a>
 * Description：
 * 菜鸟窝是一个只专注做Android开发技能的在线学习平台，课程以实战项目为主，对课程与服务”吹毛求疵”般的要求，
 * 打造极致课程，是菜鸟窝不变的承诺
 */
public class MainActivity extends BaseActivity implements DefineView{
    UMImage image = new UMImage(this, "http://www.umeng.com/images/pic/social/integrated_3.png");
    String url = "http://www.umeng.com";
    public DragLayout getDrag_layout() {
        return drag_layout;
    }
    private LinearLayout ll1;
    private DragLayout drag_layout;
    private ImageView top_bar_icon;
    private ListView lv_left_main;
    private UpdateReceiver mUpdateReceiver;
    private IntentFilter mIntentFilter;
    private Person person;
    private HashMap<String, Object> tmpMap = CNKApplication.getInstance()
            .getTempMap();
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private ImageView iv_bottom;
    private TextView iv_username,iv_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //微信 appid appsecret
        PlatformConfig.setSinaWeibo("3921700954","04b48b094faeb16683c32669824ebdad");
        //新浪微博 appkey appsecret
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        // QQ和Qzone appid appkey
        PlatformConfig.setAlipay("2015111700822536");
        //支付宝 appid
        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        //易信 appkey
        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
        //Twitter appid appkey
        PlatformConfig.setPinterest("1439206");
        //Pinterest appid
        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
        //来往 appid appkey
        registerBroadcast();// 注册广播
        setStatusBar();
        initView();
        initValidata();
        initListener();
        bindData();
    }
    public void initView() {
        drag_layout = (DragLayout) findViewById(R.id.drag_layout);
        top_bar_icon = (ImageView) findViewById(R.id.top_bar_icon);
        lv_left_main=(ListView)findViewById(R.id.lv_left_main);
        ll1=(LinearLayout)findViewById(R.id.ll1);
        iv_bottom=(ImageView)findViewById(R.id.iv_bottom);
        iv_username=(TextView) findViewById(R.id.iv_username);
        iv_email=(TextView) findViewById(R.id.iv_email);
    }
    @Override
    public void initValidata() {
        mImageLoader=ImageLoader.getInstance();
        options=new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defaultbg)
                .cacheInMemory(true)
                .cacheOnDisk(true).build();
        lv_left_main.setAdapter(new LeftItemAdapter());
        OkhttpManager.getAsync(RequestURL.UPDATE_URL, new OkhttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, Exception e) {

            }
            @Override
            public void requestSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    UpdateInfoModel model = new UpdateInfoModel();
                    model.setAppname(object.getString("appname"));
                    model.setLastForce(object.getString("lastForce"));
                    model.setServerFlag(object.getString("serverFlag"));
                    model.setServerVersion(object.getString("serverVersion"));
                    model.setUpdateurl(object.getString("updateurl"));
                    model.setUpgradeinfo(object.getString("upgradeinfo"));
                    tmpMap.put(DeliverConsts.KEY_APP_UPDATE, model);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendBroadcast(new Intent(UpdateReceiver.UPDATE_ACTION));
            }
        });
    }
    @Override
    public void initListener() {
        drag_layout.setDragListener(new CustomDragListener());
        top_bar_icon.setOnClickListener(new CustomOnClickListener());
        lv_left_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 switch (position){
                     case 0:
                         break;
                     case 1:  //发现
                         openActivity(FindActivity.class);
                         break;
                     case 2:  //关注
                         break;
                     case 3: //收藏
                         break;
                     case 4: //意见反馈
                         openActivity(SuggestActivity.class);
                         break;
                     case 5: //设置
                         break;
                     case 6: //关于我们
                         openActivity(AboutActivity.class);
                         break;
                     case 7: //用户分享
                         new ShareAction(MainActivity.this).setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                                 .withText("来自友盟分享面板")
                                 .withMedia(image)
                                 .setCallback(umShareListener)
                                 .open();

                         break;
                 }
            }
        });

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(LoginActivity.class);
            }
        });

    }
    @Override
    public void bindData() {

    }

    /**
     * 分享面板 点击监听
     */
    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
              Log.d("zttjiangqq","点击了:"+share_media);
        }
    };
    private UMShareListener umShareListener= new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this,platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MainActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
    class CustomDragListener implements DragLayout.DragListener{

        /**
         * 界面打开
         */
        @Override
        public void onOpen() {

        }

        /**
         * 界面关闭
         */
        @Override
        public void onClose() {

        }

        /**
         * 界面进行滑动
         * @param percent
         */
        @Override
        public void onDrag(float percent) {
              ViewHelper.setAlpha(top_bar_icon,1-percent);
        }
    }
    class CustomOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View arg0) {
            drag_layout.open();
        }
    }

    /**
     * 广播注册
     */
    private void registerBroadcast() {
        mUpdateReceiver = new UpdateReceiver(false);
        mIntentFilter = new IntentFilter(UpdateReceiver.UPDATE_ACTION);
        this.registerReceiver(mUpdateReceiver, mIntentFilter);
    }

    /**
     * 广播卸载
     */
    private void unRegisterBroadcast() {
        try {
            this.unregisterReceiver(mUpdateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        try {
            unRegisterBroadcast();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("zttjiangqq","onResume invoke...");
        person=(Person) tmpMap.get(DeliverConsts.KEY_LOGIN);
        if(person!=null){
            Log.d("zttjiangqq","用户信息为:"+person.toString());
            mImageLoader.displayImage(person.getLogo_url(),iv_bottom, options);
            iv_username.setText(person.getUsername());
            iv_email.setText(person.getEmail());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get( this ).onActivityResult( requestCode, resultCode, data);
    }
}
