package com.shosen.max.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shosen.max.R;
import com.shosen.max.bean.NewsBean;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.utils.HDateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterHolder> {


    private Context mContext;

    private List<NewsBean> mData;

    private OnItemClickInterface itemClickInterface;

    public void setItemClickInterface(OnItemClickInterface itemClickInterface) {
        this.itemClickInterface = itemClickInterface;
    }

    public NewsAdapter(Context mContext, List<NewsBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public NewsAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_news, viewGroup, false);
        return new NewsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapterHolder holder, int position) {
        NewsBean bean = mData.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.default_long_place_holder).
                error(R.drawable.default_long_place_holder).priority(Priority.HIGH).
                centerCrop().
        diskCacheStrategy(DiskCacheStrategy.ALL);
        GlideUtils.loadImage(mContext, bean.getNews_Picture(),
                requestOptions, GlideUtils.defaultTransOptions, holder.ivImage);
        String date = HDateUtils.getDate(Long.valueOf(bean.getCreateTime()), "yyyy-MM-dd");
        holder.tvNewsTime.setText(date);
        holder.tvNewsContent.setText(bean.getNews_Title());
        holder.tvNewsFocusCount.setText(bean.getNews_ReadNum());
        holder.itemView.setOnClickListener(v -> {
            if (itemClickInterface != null) {
                itemClickInterface.onItemClick(v, position, bean);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class NewsAdapterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_news_content)
        TextView tvNewsContent;
        @BindView(R.id.tv_news_time)
        TextView tvNewsTime;
        @BindView(R.id.tv_news_focus_count)
        TextView tvNewsFocusCount;

        public NewsAdapterHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickInterface {
        void onItemClick(View view, int position, NewsBean bean);
    }
}
