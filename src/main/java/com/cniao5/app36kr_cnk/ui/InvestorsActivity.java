package com.cniao5.app36kr_cnk.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cniao5.app36kr_cnk.R;
import com.cniao5.app36kr_cnk.biz.InvestDataManager;
import com.cniao5.app36kr_cnk.common.DefineView;

import com.cniao5.app36kr_cnk.entity.InvestData;
import com.cniao5.app36kr_cnk.ui.base.BaseActivity;
import com.cniao5.app36kr_cnk.utils.OkhttpManager;
import com.cniao5.cwidgetutils.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
import java.util.List;

/**
 * 寻找投资人列表
 */
public class InvestorsActivity extends BaseActivity implements DefineView{
    private Button btn_back;
    private TextView tv_title;
    private int pageNum=1;
    private static final String INVERSTORS_URL="https://rong.36kr.com/api/organization/investor";
    private FrameLayout home_framelayout;
    private LinearLayout loading,empty,error;
    private PullToRefreshListView invertors_listview;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private  List<InvestData> investComs;
    private LayoutInflater mLayoutInflater;
    private View load_more;
    private ItemAdapter itemAdapter;
    private boolean isMore=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invertors_list_layout);
        setStatusBar();
        initView();
        initValidata();
        initListener();
    }

    @Override
    public void initView() {
        btn_back=(Button)this.findViewById(R.id.btn_back);
        tv_title=(TextView)this.findViewById(R.id.tv_title);
        tv_title.setText("投资人列表");
        home_framelayout=(FrameLayout)this.findViewById(R.id.home_framelayout);
        loading=(LinearLayout)this.findViewById(R.id.loading);
        empty=(LinearLayout)this.findViewById(R.id.empty);
        error=(LinearLayout)this.findViewById(R.id.error);
        invertors_listview=(PullToRefreshListView)this.findViewById(R.id.invertors_listview);
    }

    @Override
    public void initValidata() {
        mLayoutInflater=LayoutInflater.from(this);
        load_more=mLayoutInflater.inflate(R.layout.recycler_load_more_layout,null);
        invertors_listview.addFooterView(load_more);
        mImageLoader=ImageLoader.getInstance();
        options=new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defaultbg)
                .cacheInMemory(true)
                .cacheOnDisk(true).build();
        invertors_listview.setVisibility(View.GONE);
        home_framelayout.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        OkhttpManager.getAsync(INVERSTORS_URL + "?page=" + pageNum, new OkhttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, Exception e) {
                Log.d("zttjiangqq","数据加载失败...");
            }

            @Override
            public void requestSuccess(String result) {
                investComs=new InvestDataManager().getInvestDatas(result);
                Log.d("zttjianggqq","数据为:"+investComs);
                if(investComs!=null&&investComs.size()>0){
                    invertors_listview.setVisibility(View.VISIBLE);
                    home_framelayout.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    empty.setVisibility(View.GONE);
                    error.setVisibility(View.GONE);
                    invertors_listview.setAdapter(itemAdapter=new ItemAdapter());
                }
            }
        });
    }

    @Override
    public void initListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvestorsActivity.this.finish();
            }
        });

        invertors_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    if(invertors_listview.getLastVisiblePosition()==invertors_listview.getCount()-1){
                        pageNum++;
                        OkhttpManager.getAsync(INVERSTORS_URL + "?page=" + pageNum, new OkhttpManager.DataCallBack() {
                            @Override
                            public void requestFailure(Request request, Exception e) {
                                Log.d("zttjiangqq","数据加载失败...");
                            }

                            @Override
                            public void requestSuccess(String result) {
                                List<InvestData> investComsTemp=new InvestDataManager().getInvestDatas(result);
                                investComs.addAll(investComsTemp);
                                itemAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
                if(scrollState==AbsListView.OnScrollListener.SCROLL_STATE_FLING){

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void bindData() {

    }

    class ItemAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return investComs.size();
        }

        @Override
        public Object getItem(int position) {
            return investComs.get(position);
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
                convertView=mLayoutInflater.inflate(R.layout.item_invertors_list_layout,null);
                _Holder.item_img=(ImageView)convertView.findViewById(R.id.item_img);
                _Holder.item_title_name=(TextView)convertView.findViewById(R.id.item_title_name);
                _Holder.item_title_content=(TextView)convertView.findViewById(R.id.item_title_content);
                convertView.setTag(_Holder);
            }else{
                _Holder=(Holder)convertView.getTag();
            }
            _Holder.item_title_name.setText(investComs.get(position).getUser().getName());
            _Holder.item_title_content.setText(investComs.get(position).getUser().getIntro());
            mImageLoader.displayImage(investComs.get(position).getUser().getAvatar(),_Holder.item_img, options);
            return convertView;
        }
    }
    private static class  Holder{
        ImageView item_img;
        TextView item_title_name;
        TextView item_title_content;
    }
}
