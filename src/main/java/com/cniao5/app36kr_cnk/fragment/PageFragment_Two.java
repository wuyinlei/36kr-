package com.cniao5.app36kr_cnk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cniao5.app36kr_cnk.R;
import com.cniao5.app36kr_cnk.adapter.HomeRecyclerAdapter;
import com.cniao5.app36kr_cnk.adapter.HomeRecyclerAdapter_Two;
import com.cniao5.app36kr_cnk.biz.HomeNewsDataManager;
import com.cniao5.app36kr_cnk.biz.RecentDataManager;
import com.cniao5.app36kr_cnk.common.Config;
import com.cniao5.app36kr_cnk.common.DefineView;
import com.cniao5.app36kr_cnk.entity.CategoriesBean;
import com.cniao5.app36kr_cnk.entity.HomeNewsBean;
import com.cniao5.app36kr_cnk.entity.RecentNewsBean;
import com.cniao5.app36kr_cnk.fragment.base.BaseFragment;
import com.cniao5.app36kr_cnk.ui.DetailsActivity;
import com.cniao5.app36kr_cnk.utils.OkhttpManager;
import com.squareup.okhttp.Request;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * 当前类注释:页面Fragment
 * ProjectName：App36Kr_CNK
 * Author:<a href="http://www.cniao5.com">菜鸟窝</a>
 * Description：
 * 菜鸟窝是一个只专注做Android开发技能的在线学习平台，课程以实战项目为主，对课程与服务”吹毛求疵”般的要求，
 * 打造极致课程，是菜鸟窝不变的承诺
 */
public class PageFragment_Two extends BaseFragment implements DefineView{
    private View mView;
    private static final String KEY="EXTRA";
    private CategoriesBean categoriesBean;
    private RecyclerView home_recyclerview;
    private LinearLayoutManager linearLayoutManager;
    private FrameLayout home_framelayout;
    private LinearLayout loading,empty,error;
    private List<HomeNewsBean> homeNewsBeens; //新闻列表数据
    private HomeRecyclerAdapter_Two adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int lastItem;
    private boolean isMore=true; //解决上拉重复加载的bug
    //进行分页效果--主要用于近期活动列表
    private int page=1;      //页码 默认为第一页
    private int pageSize=95;   //每页的item数量
    private List<RecentNewsBean> recentNewsBeans;  //近期活动列表数据

    public static PageFragment_Two newInstance(CategoriesBean extra){
        Bundle bundle=new Bundle();
        bundle.putSerializable(KEY,extra);
        PageFragment_Two fragment=new PageFragment_Two();
        fragment.setArguments(bundle);
        return  fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null) {
            categoriesBean=(CategoriesBean)bundle.getSerializable(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView==null){
            mView=inflater.inflate(R.layout.page_fragment_layout,container,false);
            initView();
            initValidata();
            initListener();
            bindData();
        }
        return mView;
    }
    @Override
    public void initView() {

        home_framelayout=(FrameLayout)mView.findViewById(R.id.home_framelayout);
        loading=(LinearLayout)mView.findViewById(R.id.loading);
        empty=(LinearLayout)mView.findViewById(R.id.empty);
        error=(LinearLayout)mView.findViewById(R.id.error);
        home_recyclerview=(RecyclerView)mView.findViewById(R.id.home_recyclerview);
        swipeRefreshLayout=(SwipeRefreshLayout)mView.findViewById(R.id.swipeRefreshLayout);

    }
    @Override
    public void initValidata() {
        //设置swipeRefreshLayout的进度条的背景颜色
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.color_white);
        //进度条的颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);
        //设置进度条的偏移量
        swipeRefreshLayout.setProgressViewOffset(false,0,50);


        home_recyclerview.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL,false);
        home_recyclerview.setLayoutManager(linearLayoutManager);
        if(categoriesBean.getData_type().equals("tv")) {
            adapter = new HomeRecyclerAdapter_Two(getActivity(), 1);
        }else if(categoriesBean.getData_type().equals("recent")){
            //近期活动
            adapter = new HomeRecyclerAdapter_Two(getActivity(), 2);
        }else{
            adapter=new HomeRecyclerAdapter_Two(getActivity(),0);
        }
        //设置分隔线
        //设置动画
        //数据获取
        String requestUrl="";
        if(!categoriesBean.getData_type().equals("recent")){
            requestUrl=categoriesBean.getHref();
        }else{
            requestUrl="http://chuang.36kr.com/api/actapply?page="+page+"&pageSize="+pageSize;
        }
        OkhttpManager.getAsync(requestUrl, new OkhttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, Exception e) {
                Log.d("zttjiangqq","requestFailure...");
            }
            @Override
            public void requestSuccess(String result) {
                Log.d("zttjiangqq","requestSuccess...");
                if (!categoriesBean.getData_type().equals("recent")) {
                    Document document=Jsoup.parse(result, Config.CRAWLER_URL);
                    homeNewsBeens=new HomeNewsDataManager().getHomeNewsBeans(document);
                    bindData();
                }else{
                    recentNewsBeans = RecentDataManager.getRecentDatas(result);
                    adapter.setRecentNewsBeans(recentNewsBeans);
                    home_recyclerview.setAdapter(adapter);
                }

            }
        });
    }
    @Override
    public void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                         if(swipeRefreshLayout.isRefreshing()){
                             swipeRefreshLayout.setRefreshing(false);
                         }
                        Toast.makeText(getActivity(),"下拉刷新成功",Toast.LENGTH_SHORT).show();
                    }
                },5000);
            }
        });
        home_recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastItem+1==linearLayoutManager.getItemCount()){
                      if(isMore){
                          isMore=false;
                          if(!categoriesBean.getData_type().equals("recent")){
                          //进行加载数据...
                          //构造请求地址
                          String loadMoreUrl=categoriesBean.getHref()+"?b_url_code="+homeNewsBeens.get(homeNewsBeens.size()-1).gettId()+"&d=next";
                          Log.d("zttjiangqq",loadMoreUrl);
                          OkhttpManager.getAsync(loadMoreUrl, new OkhttpManager.DataCallBack() {
                              @Override
                              public void requestFailure(Request request, Exception e) {
                                  Log.d("zttjiangqq","requestFailure...");
                              }
                              @Override
                              public void requestSuccess(String result) {
                                  Log.d("zttjiangqq","requestSuccess...");
                                  Document document=Jsoup.parse(result, Config.CRAWLER_URL);
                                  List<HomeNewsBean>  temps=new HomeNewsDataManager().getHomeNewsBeans(document);
                                  homeNewsBeens.addAll(temps);
                                  adapter.notifyDataSetChanged();
                                  isMore=true;
                              }
                          });

                          }
                      }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastItem=linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        //添加点击事件
        adapter.setOnItemClickListener(new HomeRecyclerAdapter_Two.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object bean) {
                if(bean instanceof HomeNewsBean) {
                    Intent mIntent = new Intent(getActivity(), DetailsActivity.class);
                    mIntent.putExtra("titleUrl",((HomeNewsBean)bean).getHref());
                    mIntent.putExtra("titleId", ((HomeNewsBean)bean).gettId());
                    getActivity().startActivity(mIntent);
                }else if(bean instanceof  RecentNewsBean){
                    Toast.makeText(getActivity(),"点击了"+((RecentNewsBean)bean).toString(),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    @Override
    public void bindData() {
        adapter.setHomeNewsBeans(homeNewsBeens);
        home_recyclerview.setAdapter(adapter);
    }


}
