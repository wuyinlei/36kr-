package com.cniao5.app36kr_cnk.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cniao5.app36kr_cnk.R;
import com.cniao5.app36kr_cnk.biz.ArticleDataManager;
import com.cniao5.app36kr_cnk.common.Config;
import com.cniao5.app36kr_cnk.common.DefineView;
import com.cniao5.app36kr_cnk.entity.ArticleBean;
import com.cniao5.app36kr_cnk.ui.base.BaseActivity;
import com.cniao5.app36kr_cnk.utils.OkhttpManager;
import com.cniao5.app36kr_cnk.widget.RoundAngleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 当前类注释:
 * 项目名：App36Kr_CNK
 * 包名：com.cniao5.app36kr_cnk.ui
 * 作者：江清清 on 16/1/22 18:56
 * 邮箱：jiangqqlmj@163.com
 * QQ： 781931404
 * 公司：江苏中天科技软件技术有限公司
 */
public class DetailsActivity extends BaseActivity implements DefineView{
    private Button btn_back,btn_share,btn_font,btn_night;
    private TextView details_title,details_name,details_time;
    private RoundAngleImageView details_avatar;
    private ImageView details_ad;
    private WebView details_content;
    private FrameLayout home_framelayout;
    private LinearLayout loading,empty,error;
    private String titleUrl,titleId;
    private ArticleBean articleBean;
    private ImageLoader mImageLoader;
    private RelativeLayout relative_content;
    private Document document=null;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bindData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);
        setStatusBar();
        initView();
        initValidata();
        initListener();
    }

    @Override
    public void initView() {
        btn_back=(Button)this.findViewById(R.id.btn_back);
        btn_share=(Button)this.findViewById(R.id.btn_share);
        btn_font=(Button)this.findViewById(R.id.btn_font);
        btn_night=(Button)this.findViewById(R.id.btn_night);
        details_title=(TextView)this.findViewById(R.id.details_title);
        details_name=(TextView)this.findViewById(R.id.details_name);
        details_time=(TextView)this.findViewById(R.id.details_time);
        details_avatar=(RoundAngleImageView)this.findViewById(R.id.details_avatar);
        details_ad=(ImageView) this.findViewById(R.id.details_ad);

        details_content=(WebView)this.findViewById(R.id.details_content);
        home_framelayout=(FrameLayout)this.findViewById(R.id.prompt_framelayout);
        loading=(LinearLayout)this.findViewById(R.id.loading);
        empty=(LinearLayout)this.findViewById(R.id.empty);
        error=(LinearLayout)this.findViewById(R.id.error);

        relative_content=(RelativeLayout)this.findViewById(R.id.relative_content);
    }
    @Override
    public void initValidata() {
        mImageLoader=ImageLoader.getInstance();
        Intent mIntent=getIntent();
        titleUrl=mIntent.getStringExtra("titleUrl");
        titleId=mIntent.getStringExtra("titleId");
        relative_content.setVisibility(View.GONE);
        home_framelayout.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        //设置webview
        details_content.setWebChromeClient(new MyWebChromeClient());
        details_content.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = details_content.getSettings();
        webSettings.setJavaScriptEnabled(true);  //开启javascript
        webSettings.setDomStorageEnabled(true);  //开启DOM
        webSettings.setDefaultTextEncodingName("utf-8"); //设置编码
        // // web页面处理
        webSettings.setAllowFileAccess(true);// 支持文件流

        //提高网页加载速度，暂时阻塞图片加载，然后网页加载好了，在进行加载图片
        webSettings.setBlockNetworkImage(true);
        //开启缓存机制
        webSettings.setAppCacheEnabled(true);

        OkhttpManager.getAsync(titleUrl, new OkhttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, Exception e) {
                Log.d("zttjiangqq","数据加载失败...");
                relative_content.setVisibility(View.GONE);
                home_framelayout.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                empty.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }
            @Override
            public void requestSuccess(String result) {
//                Log.d("zttjiangqq","数据加载成功...");
//                try {
//                    WebClient wc = new WebClient(BrowserVersion.CHROME);
//                    wc.getOptions().setUseInsecureSSL(true);
//                    wc.getOptions().setJavaScriptEnabled(true);
//                    wc.getOptions().setCssEnabled(false);
//                    wc.getOptions().setThrowExceptionOnScriptError(false);
//                    wc.getOptions().setTimeout(100000);
//                    wc.getOptions().setDoNotTrackEnabled(false);
//                    HtmlPage page = wc.getPage(titleUrl);
//                    document = Jsoup.parse(page.asXml().substring(43));
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                new Thread(new Runnable() {
//                     @Override
//                     public void run() {
//                         articleBean=new ArticleDataManager(titleId).getArticleBean_New(document);
//                         handler.sendMessage(handler.obtainMessage());
//                     }
//                 }).start();
            }
        });
    }

    @Override
    public void initListener() {
        btn_back.setOnClickListener(new CustomOnClickListener());
        btn_share.setOnClickListener(new CustomOnClickListener());
        btn_font.setOnClickListener(new CustomOnClickListener());
        btn_night.setOnClickListener(new CustomOnClickListener());
    }

    @Override
    public void bindData() {
        if(articleBean!=null){
            relative_content.setVisibility(View.VISIBLE);
            home_framelayout.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            empty.setVisibility(View.GONE);
            error.setVisibility(View.GONE);
            Log.d("zttjiangqq","文章详情的数据为:"+articleBean);
            details_title.setText(articleBean.getTitle());
            mImageLoader.displayImage(articleBean.getAuthorBean().getAvatar(),details_avatar);
            details_name.setText(articleBean.getAuthorBean().getName());
            details_time.setText(" 发表于"+articleBean.getDatetext());
            mImageLoader.displayImage(articleBean.getHeadImage(),details_ad);
            //details_content.loadData(articleBean.getContext(),"text/html","UTF-8");
            details_content.loadDataWithBaseURL(Config.CRAWLER_URL,articleBean.getContext(),"text/html","UTF-8","");
        }else{
            relative_content.setVisibility(View.GONE);
            home_framelayout.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            error.setVisibility(View.GONE);
        }

    }

    class CustomOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_back:
                    DetailsActivity.this.finish();
                    break;
                case R.id.btn_share:
                    Toast.makeText(DetailsActivity.this,"点击了分享按钮",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_night:
                    Toast.makeText(DetailsActivity.this,"点击了白天/黑夜切换按钮",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_font:
                    Toast.makeText(DetailsActivity.this,"点击了字体按钮",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    class MyWebChromeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.d("zttjiangqq","加载进度发生变化:"+newProgress);
        }
    }
    class MyWebViewClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("zttjiangqq","网页开始加载:"+url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("zttjiangqq","网页加载完成..."+url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.d("zttjiangqq","加载的资源:"+url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("zttjiangqq","拦截到URL信息为:"+url);
            return super.shouldOverrideUrlLoading(view, url);

        }
    }
}
