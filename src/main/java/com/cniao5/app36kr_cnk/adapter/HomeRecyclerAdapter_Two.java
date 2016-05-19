package com.cniao5.app36kr_cnk.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cniao5.app36kr_cnk.R;
import com.cniao5.app36kr_cnk.entity.HomeNewsBean;
import com.cniao5.app36kr_cnk.entity.RecentNewsBean;
import com.cniao5.app36kr_cnk.utils.DateUtil;
import com.cniao5.app36kr_cnk.widget.RoundAngleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Date;
import java.util.List;

/**
 * 当前类注释:
 * 项目名：App36Kr_CNK
 * 包名：com.cniao5.app36kr_cnk.adapter
 * 作者：江清清 on 16/1/7 20:07
 * 邮箱：jiangqqlmj@163.com
 * QQ： 781931404
 * 公司：江苏中天科技软件技术有限公司
 */
public class HomeRecyclerAdapter_Two extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final int TYPE_ITEM = 0;     //普通Item View
    private static final int TYPE_TV = 1;       //TV列表
    private static final int TYPE_RECENT = 3;       //近期活动列表
    private static final int TYPE_FOOTER = 2;   //顶部FootView
    private Context mContext;
    private LayoutInflater mInflater;
    private int type=0;   //0.普通列表   1.视频列表 2.近期活动
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private String[] masks;
    private int[] mask_colors;
    private Resources resources;
    //新闻列表的数据
    private List<HomeNewsBean> mHomeNewsBeans;
    //近期活动相关信息
    private List<RecentNewsBean> recentNewsBeans;
    public void setRecentNewsBeans(List<RecentNewsBean> recentNewsBeans) {
        this.recentNewsBeans = recentNewsBeans;
    }
    public void setHomeNewsBeans(List<HomeNewsBean> pHomeNewsBeans) {
        this.mHomeNewsBeans = pHomeNewsBeans;
    }
    //构造方法
    public HomeRecyclerAdapter_Two(Context pContext, int pType){
        this.type=pType;
        this.mContext=pContext;
        this.mInflater=LayoutInflater.from(mContext);
        options=new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.defaultbg_h)
                .build();
        mImageLoader=ImageLoader.getInstance();
        masks=new String[]{"早期项目","B轮后","资本","深度","行研"};
        mask_colors=new int[]{R.color.mask_tags_1,R.color.mask_tags_2,
                R.color.mask_tags_3,R.color.mask_tags_4,R.color.mask_tags_5,
               };
        resources=mContext.getResources();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断TV还是普通列表，近期活动。来创建返回不同的View
        View view=null;
        if(type==0){
             //普通
            if(viewType==TYPE_ITEM){
                view=mInflater.inflate(R.layout.item_home_news_layout,parent,false);
                view.setOnClickListener(this);
                return new ItemViewHolder(view);
            }else if(viewType==TYPE_FOOTER){
                view=mInflater.inflate(R.layout.recycler_load_more_layout,parent,false);
                view.setOnClickListener(this);
                return new FootItemViewHolder(view);
            }
        }else if (type==1){
             //TV
            if(viewType==TYPE_TV){
                view=mInflater.inflate(R.layout.item_tv_news_layout,parent,false);
                view.setOnClickListener(this);
                return new TvItemViewHolder(view);
            }else if(viewType==TYPE_FOOTER){
                view=mInflater.inflate(R.layout.recycler_load_more_layout,parent,false);
                view.setOnClickListener(this);
                return new FootItemViewHolder(view);
            }
        }else if(type==2){
             //近期活动
            if(viewType==TYPE_RECENT){
                view=mInflater.inflate(R.layout.item_recent_news_layout,parent,false);
                view.setOnClickListener(this);
                return new RecentViewHolder(view);
            }else if(viewType==TYPE_FOOTER){
                view=mInflater.inflate(R.layout.recycler_load_more_layout,parent,false);
                view.setOnClickListener(this);
                return new FootItemViewHolder(view);
            }
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            HomeNewsBean homeNewsBean=mHomeNewsBeans.get(position);
            holder.itemView.setTag(homeNewsBean);
            //普通视频列表
            mImageLoader.displayImage(homeNewsBean.getImgurl(), ((ItemViewHolder) holder).item_news_tv_img, options);
            mImageLoader.displayImage(homeNewsBean.getAuthorBean().getAvatar(),((ItemViewHolder) holder).item_news_img_icon,options);
            String mask=homeNewsBean.getMask();
            ((ItemViewHolder) holder).item_news_tv_name.setText(homeNewsBean.getAuthorBean().getName());
            ((ItemViewHolder) holder).item_news_tv_time.setText(homeNewsBean.getDatetext());
            ((ItemViewHolder) holder).item_news_tv_type.setText(mask);
            ((ItemViewHolder) holder).item_news_tv_title.setText(homeNewsBean.getTitle());
            int index=0;
            for(int i=0;i<masks.length;i++){
                if(masks[i].equals(mask)){
                    index=i;
                    break;
                }
            }
            ((ItemViewHolder) holder).item_news_tv_arrow.setBackgroundColor(mContext.getResources().getColor(mask_colors[index]));
            ((ItemViewHolder) holder).item_news_tv_type.setTextColor(mContext.getResources().getColor(mask_colors[index]));
        }else if(holder instanceof  TvItemViewHolder){
            HomeNewsBean homeNewsBean=mHomeNewsBeans.get(position);
            holder.itemView.setTag(homeNewsBean);
            //视频列表
            mImageLoader.displayImage(homeNewsBean.getImgurl(),((TvItemViewHolder) holder).tv_img,options);
            ((TvItemViewHolder) holder).tv_mask.setText(homeNewsBean.getMask());
            ((TvItemViewHolder) holder).tv_title.setText(homeNewsBean.getTitle());
        }else if(holder instanceof RecentViewHolder){
            //近期活动
            RecentNewsBean recentNewsBean=recentNewsBeans.get(position);
            holder.itemView.setTag(recentNewsBean);
            ((RecentViewHolder) holder).recent_item_tv_title.setText(recentNewsBean.getTitle());
            mImageLoader.displayImage(recentNewsBean.getListImageUrl(), ((RecentViewHolder) holder).recent_item_img_logo);
            //活动地点
            ((RecentViewHolder) holder).recent_item_tv_location.setText(recentNewsBean.getCity());
            //活动开始时间设置
            String beginDate=DateUtil.getFormatDate(new Date(Long.parseLong(recentNewsBean.getActivityBeginTime())));
            String endDate= DateUtil.getFormatDate(new Date(Long.parseLong(recentNewsBean.getActivityEndTime())));
            if (beginDate.equals(endDate)) {
                ((RecentViewHolder) holder).recent_item_tv_timetext.setText(beginDate);
            }else {
                ((RecentViewHolder) holder).recent_item_tv_timetext.setText(beginDate+"到"+endDate);
            }
            //报名状态
            long nowTime=System.currentTimeMillis();
            long begin=Long.parseLong(recentNewsBean.getActivityBeginTime());
            long end=Long.parseLong(recentNewsBean.getActivityEndTime());
            if(nowTime<=begin){
                //报名中
                ((RecentViewHolder) holder).recent_item_tv_status.setText("报名中");
                ((RecentViewHolder) holder).recent_item_tv_status.setBackgroundResource(R.drawable.icon_activity_jin);
            }else if(nowTime>begin&&nowTime<=end){
                //活动中
                ((RecentViewHolder) holder).recent_item_tv_status.setText("活动中");
                ((RecentViewHolder) holder).recent_item_tv_status.setBackgroundResource(R.drawable.icon_activity_wei);
            }else {
                //已结束
                ((RecentViewHolder) holder).recent_item_tv_status.setText("已结束");
                ((RecentViewHolder) holder).recent_item_tv_status.setBackgroundResource(R.drawable.icon_activity_yi);
            }
        }
        else if(holder instanceof  FootItemViewHolder){
            //上拉加载更多布局信息...

        }
    }
    @Override
    public int getItemCount() {
        if(type==2){
            return recentNewsBeans!=null?recentNewsBeans.size():0;
        }else {
            return mHomeNewsBeans != null ? mHomeNewsBeans.size() + 1 : 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(type==0){
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }else if(type==2){
                return TYPE_RECENT;
        }else{
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_TV;
            }
        }
    }

    //下面是自定义的ViewHolder
    //普通item的ViewHolder
    private class ItemViewHolder extends RecyclerView.ViewHolder{
        private RoundAngleImageView item_news_img_icon;
        private TextView item_news_tv_name;
        private TextView item_news_tv_time;
        private TextView item_news_tv_arrow;
        private TextView item_news_tv_type;
        private TextView item_news_tv_title;
        private ImageView item_news_tv_img;
        public ItemViewHolder(View itemView) {
            super(itemView);
            item_news_img_icon=(RoundAngleImageView)itemView.findViewById(R.id.item_news_img_icon);
            item_news_tv_name=(TextView)itemView.findViewById(R.id.item_news_tv_name);
            item_news_tv_time=(TextView)itemView.findViewById(R.id.item_news_tv_time);
            item_news_tv_arrow=(TextView)itemView.findViewById(R.id.item_news_tv_arrow);
            item_news_tv_type=(TextView)itemView.findViewById(R.id.item_news_tv_type);
            item_news_tv_title=(TextView)itemView.findViewById(R.id.item_news_tv_title);
            item_news_tv_img=(ImageView)itemView.findViewById(R.id.item_news_tv_img);
        }
    }
    //视频列表的ViewHoler
    private class TvItemViewHolder extends  RecyclerView.ViewHolder{
        private ImageView tv_img;
        private TextView tv_title;
        private TextView tv_mask;
        public TvItemViewHolder(View itemView) {
            super(itemView);
            tv_img=(ImageView)itemView.findViewById(R.id.tv_img);
            tv_title=(TextView)itemView.findViewById(R.id.tv_title);
            tv_mask=(TextView)itemView.findViewById(R.id.tv_mask);
        }
    }
    /**
     * 近期活动列表 ViewHolder
     */
    public static class RecentViewHolder extends  RecyclerView.ViewHolder{
        ImageView recent_item_img_logo;
        TextView recent_item_tv_title;
        TextView recent_item_tv_location;
        TextView recent_item_tv_status;
        TextView recent_item_tv_timetext;
        public RecentViewHolder(View itemView) {
            super(itemView);
            recent_item_img_logo=(ImageView)itemView.findViewById(R.id.recent_item_img_logo);
            recent_item_tv_title=(TextView)itemView.findViewById(R.id.recent_item_tv_title);
            recent_item_tv_location=(TextView)itemView.findViewById(R.id.recent_item_tv_location);
            recent_item_tv_status=(TextView)itemView.findViewById(R.id.recent_item_tv_status);
            recent_item_tv_timetext=(TextView)itemView.findViewById(R.id.recent_item_tv_timetext);
        }
    }
    @Override
    public void onClick(View v) {
           if(onItemClickListener!=null){
               onItemClickListener.onItemClick(v,v.getTag());
           }
    }

    /**
     * 上拉加载更多进度布局
     */
    private class FootItemViewHolder extends  RecyclerView.ViewHolder{

        public FootItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    //添加ItemClickListener接口
    public interface OnItemClickListener{
        void onItemClick(View view, Object bean);
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
