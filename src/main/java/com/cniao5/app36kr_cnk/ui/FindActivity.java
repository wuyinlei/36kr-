package com.cniao5.app36kr_cnk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cniao5.app36kr_cnk.R;
import com.cniao5.app36kr_cnk.common.DefineView;
import com.cniao5.app36kr_cnk.entity.FindAdBean;
import com.cniao5.app36kr_cnk.entity.FindAdData;
import com.cniao5.app36kr_cnk.ui.base.BaseActivity;
import com.cniao5.app36kr_cnk.utils.OkhttpManager;
import com.cniao5.cwidgetutils.AutoGallery;
import com.cniao5.cwidgetutils.FlowIndicator;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

import java.util.List;

public class FindActivity extends BaseActivity implements DefineView{
    private AutoGallery headline_image_gallery;
    private FlowIndicator headline_circle_indicator;
    private int gallerySelectedPositon=0;//Gallery索引
    private int circleSelectedPosition = 0; // 默认指示器的圆圈的位置为第一
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private List<FindAdBean> findAdBeans; //顶部广告数据
    private LayoutInflater mInflater;
    private Button btn_back;
    private RelativeLayout relative_investors; //寻找投资人
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        setStatusBar();
        initView();
        initValidata();
        initListener();
    }

    @Override
    public void initView() {
        //获取AutoGallery和FlowIndicator控件
        headline_image_gallery=(AutoGallery)this.findViewById(R.id.headline_image_gallery);
        headline_circle_indicator=(FlowIndicator)this.findViewById(R.id.headline_circle_indicator);
        btn_back=(Button)this.findViewById(R.id.btn_back);
        relative_investors=(RelativeLayout)this.findViewById(R.id.relative_investors);
    }

    @Override
    public void initValidata() {
        mInflater=LayoutInflater.from(this);
        mImageLoader=ImageLoader.getInstance();
        options=new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defaultbg)
                .cacheInMemory(true)
                .cacheOnDisk(true).build();
        OkhttpManager.getAsync("https://z.36kr.com/api/p/sc/images?type=1", new OkhttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, Exception e) {
                Log.d("zttjiangqq","顶部广告数据加载失败...");
            }

            @Override
            public void requestSuccess(String result) {
                Log.d("zttjiangqq","顶部广告数据加载成功...");
                Gson gson=new Gson();
                findAdBeans= gson.fromJson(result, FindAdData.class).getData();
                bindTopData();

            }
        });
    }

    @Override
    public void initListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindActivity.this.finish();
            }
        });
        //寻找投资人
        relative_investors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 openActivity(InvestorsActivity.class);
            }
        });
    }

    @Override
    public void bindData() {

    }

    private void bindTopData(){
        int topSize=findAdBeans.size();
        //设置指示器
        headline_circle_indicator.setCount(topSize);
        headline_circle_indicator.setSeletion(circleSelectedPosition);
        //设置画廊Gallery
        headline_image_gallery.setLength(topSize);
        gallerySelectedPositon=topSize*50+circleSelectedPosition;
        headline_image_gallery.setSelection(gallerySelectedPositon);
        headline_image_gallery.setDelayMillis(4000);
        headline_image_gallery.start();
        headline_image_gallery.setAdapter(new GalleryAdapter());
        headline_image_gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("zttjiangqq","setOnItemSelectedListener...");
                circleSelectedPosition=position;
                gallerySelectedPositon=circleSelectedPosition%findAdBeans.size();
                headline_circle_indicator.setSeletion(gallerySelectedPositon);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * AutoGallery的自定义Adapter
     */
    class GalleryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            return findAdBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder _Holder=null;
            if(convertView==null){
                _Holder=new Holder();
                convertView=mInflater.inflate(R.layout.item_gallery_layout,null);
                _Holder.item_head_gallery_img=(ImageView)convertView.findViewById(R.id.item_head_gallery_img);
                convertView.setTag(_Holder);
            }else {
                _Holder=(Holder)convertView.getTag();
            }
            //显示数据
            mImageLoader.displayImage(findAdBeans.get(position%findAdBeans.size()).getImg_url(), _Holder.item_head_gallery_img, options);
            return convertView;
        }
    }

    private static class  Holder{
        ImageView item_head_gallery_img;
    }
}
