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
import com.bumptech.glide.request.RequestOptions;
import com.shosen.max.R;
import com.shosen.max.bean.mall.GoodsBean;
import com.shosen.max.utils.CommonUtils;
import com.shosen.max.utils.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int HEADER = 100;

    public static final int NORMAL = 101;


    private Context mContext;

    private List<GoodsBean> mData;

    private GoodsOnItemClick mClick;

    public void setClick(GoodsOnItemClick mClick) {
        this.mClick = mClick;
    }

    public GoodsItemAdapter(Context mContext, List<GoodsBean> goodsList) {
        this.mContext = mContext;
        this.mData = goodsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == HEADER) {
            View view = inflater.inflate(R.layout.item_mall_home_header, viewGroup, false);
            return new ProductItemHeaderHolder(view);
        } else if (viewType == NORMAL) {
            View view = inflater.inflate(R.layout.item_goods, viewGroup, false);
            return new ProductItemHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (mData.get(i) == null || viewHolder == null) {
            return;
        }
        GoodsBean bean = mData.get(i);
        if (viewHolder instanceof ProductItemHolder) {
            ProductItemHolder holder = (ProductItemHolder) viewHolder;
            RequestOptions options = new RequestOptions();
            options.
                    error(R.drawable.default_place_holder).centerCrop().priority(Priority.HIGH);
            GlideUtils.loadImage(mContext, bean.getPicUrl(), options,
                    GlideUtils.defaultTransOptions, holder.ivGoodsImage);
            holder.tvCommodityPrice.setText(CommonUtils.
                    getFormatPrice(bean.getRetailPrice(), bean.isPointGoods()));
            holder.tvCommodityText.setText(bean.getName());
            holder.itemView.setOnClickListener(v -> {
                if (mClick != null) {
                    mClick.onGoodsClickListener(v, i, bean);
                }
            });
        } else if (viewHolder instanceof ProductItemHeaderHolder) {
            ProductItemHeaderHolder holder = (ProductItemHeaderHolder) viewHolder;
            holder.tvHeader.setText(bean.getHeaderName());
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).isHeader()) {
            return HEADER;
        } else {
            return NORMAL;
        }
    }

    public class ProductItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_goods_image)
        ImageView ivGoodsImage;
        @BindView(R.id.tv_commodity_text)
        TextView tvCommodityText;
        @BindView(R.id.tv_commodity_price)
        TextView tvCommodityPrice;

        public ProductItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ProductItemHeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_header)
        TextView tvHeader;

        public ProductItemHeaderHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface GoodsOnItemClick {
        void onGoodsClickListener(View view, int position, GoodsBean bean);
    }
}
