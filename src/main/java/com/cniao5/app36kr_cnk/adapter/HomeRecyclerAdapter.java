package com.cniao5.app36kr_cnk.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cniao5.app36kr_cnk.R;
import com.cniao5.app36kr_cnk.entity.HomeNewsBean;
import com.cniao5.app36kr_cnk.widget.RoundAngleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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
public class HomeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final int TYPE_ITEM=0;
    private static final int TYPE_FOOT=1;
    private Context mContext;
    private LayoutInflater mInflater;
    private int type=0;   //0.普通列表   1.视频列表
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private String[] masks;
    private int[] mask_colors;
    private Resources resources;
    //新闻列表的数据
    private List<HomeNewsBean> mHomeNewsBeans;
    public void setHomeNewsBeans(List<HomeNewsBean> pHomeNewsBeans) {
        this.mHomeNewsBeans = pHomeNewsBeans;
    }
    //构造方法
    public HomeRecyclerAdapter(Context pContext, int pType){
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
        if(viewType==TYPE_ITEM){
        if(type==0){
           //普通列表
            View itemView=mInflater.inflate(R.layout.item_home_news_layout,parent,false);
            itemView.setOnClickListener(this);
            ItemViewHolder itemViewHolder=new ItemViewHolder(itemView);
            return itemViewHolder;
        }else if(type==1){
           //视频列表
            View tvItemView=mInflater.inflate(R.layout.item_tv_news_layout,parent,false);
            tvItemView.setOnClickListener(this);
            TvItemViewHolder tvItemViewHolder=new TvItemViewHolder(tvItemView);
            return tvItemViewHolder;
        }}else if(viewType==TYPE_FOOT){
            View footItemView=mInflater.inflate(R.layout.recycler_load_more_layout,parent,false);
            footItemView.setOnClickListener(this);
            FootItemViewHolder footItemViewHolder=new FootItemViewHolder(footItemView);
            return footItemViewHolder;
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
        }else if(holder instanceof  FootItemViewHolder){
            //上拉加载更多布局信息...

        }
    }
    @Override
    public int getItemCount() {
        return mHomeNewsBeans==null?0:mHomeNewsBeans.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position+1==getItemCount()){
            return TYPE_FOOT;
        }else {
            return  TYPE_ITEM;
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

    @Override
    public void onClick(View v) {
           if(onItemClickListener!=null){
               onItemClickListener.onItemClick(v,(HomeNewsBean) v.getTag());
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
        void onItemClick(View view,HomeNewsBean bean);
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
