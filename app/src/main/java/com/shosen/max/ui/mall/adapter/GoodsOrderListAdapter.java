package com.shosen.max.ui.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.shosen.max.R;
import com.shosen.max.bean.mall.GoodsOrderList;
import com.shosen.max.utils.CommonUtils;
import com.shosen.max.utils.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsOrderListAdapter extends
        RecyclerView.Adapter<GoodsOrderListAdapter.GoodOrderListHolder> {


    private Context mContext;

    private List<GoodsOrderList> mData;


    private OnItemClickListener mclicK;

    public void setMclicK(OnItemClickListener mclicK) {
        this.mclicK = mclicK;
    }

    public GoodsOrderListAdapter(Context mContext, List<GoodsOrderList> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public GoodOrderListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_goods_order, viewGroup, false);
        return new GoodOrderListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodOrderListHolder holder, int i) {
        if (mData.size() == 0 || mData == null) {
            return;
        }

        GoodsOrderList bean = mData.get(i);
        holder.tvDate.setText(bean.getUpdateTime());
        holder.tvOrderState.setText(bean.getOrderStatusText());
        RequestOptions options = new RequestOptions();
        options.priority(Priority.HIGH).centerCrop().
                error(R.drawable.default_place_holder);
        if (bean.getGoodsList().size() > 0) {
            GlideUtils.loadImage(mContext, bean.getGoodsList().get(0).getPicUrl(), options,
                    GlideUtils.defaultTransOptions, holder.ivImage);
            holder.tvGoodName.setText(bean.getGoodsList().get(0).getGoodsName());
            holder.tvGoodsCount.setText("x" + bean.getGoodsList().get(0).getNumber());
        }
        boolean isPoint = "integral".equals(bean.getOrderType());
        holder.tvPrice.setText(CommonUtils.getFormatPrice(bean.getActualPrice(), isPoint));
        if (mclicK != null) {
            holder.tvCancelOrdetail.setOnClickListener(v -> {
                mclicK.cancelClick(v, i, bean);
            });

            holder.tvConfirm.setOnClickListener(v -> {
                mclicK.confirmClick(v, i, bean);
            });

            holder.itemView.setOnClickListener(v -> {
                mclicK.allClick(v, i, bean);
            });
            holder.ivDel.setOnClickListener(v -> {
                if (mclicK != null) {
                    mclicK.delClick(v, i, bean);
                }
            });
        }
        String cancelText = "";
        String confirmText = "";
        String statusText = "";
        switch (bean.getOrderStatus()) {
            case 101:
                //return "未付款";
                cancelText = "取消订单";
                confirmText = "支付";
                statusText = "待支付";
                holder.tvCancelOrdetail.setVisibility(View.VISIBLE);
                holder.tvConfirm.setVisibility(View.VISIBLE);
                holder.ivDel.setVisibility(View.GONE);
                break;
            case 102:
                //return "已取消";
            case 103:
                //return "已取消(系统)";
                confirmText = "再次购买";
                statusText = "已取消";
                holder.tvCancelOrdetail.setVisibility(View.GONE);
                holder.ivDel.setVisibility(View.VISIBLE);
                holder.llDel.setOnClickListener(v -> {
                    if (mclicK != null) {
                        mclicK.delClick(v, i, bean);
                    }
                });
                holder.tvConfirm.setVisibility(View.INVISIBLE);
                break;
            case 201:
                //return "已付款";
                confirmText = "再次购买";
                statusText = "买家已付款";
                holder.tvCancelOrdetail.setVisibility(View.GONE);
                holder.ivDel.setVisibility(View.GONE);
                holder.tvConfirm.setVisibility(View.INVISIBLE);
                break;
            case 202:
                //return "订单取消，退款中";
                confirmText = "再次购买";
                statusText = "订单取消，退款中";
                holder.tvCancelOrdetail.setVisibility(View.GONE);
                holder.ivDel.setVisibility(View.GONE);
                holder.tvConfirm.setVisibility(View.INVISIBLE);
                break;
            case 203:
                //return "已退款";
                //TODO
                confirmText = "再次购买";
                holder.tvCancelOrdetail.setVisibility(View.GONE);
                holder.ivDel.setVisibility(View.GONE);
                holder.tvConfirm.setVisibility(View.INVISIBLE);
                break;
            case 301:
                //return "已发货";
                cancelText = "查看物流";
                confirmText = "确认收货";
                statusText = "卖家已发货";
                holder.tvCancelOrdetail.setVisibility(View.GONE);
                holder.ivDel.setVisibility(View.GONE);
                holder.tvConfirm.setVisibility(View.VISIBLE);
                break;
            case 401:
                //return "已收货";
            case 402:
            case 501:
                //return "已完成";
                cancelText = "查看物流";
                confirmText = "再次购买";
                statusText = "已完成";
                holder.tvCancelOrdetail.setVisibility(View.GONE);
                holder.ivDel.setVisibility(View.VISIBLE);
                holder.tvConfirm.setVisibility(View.INVISIBLE);
                holder.llDel.setOnClickListener(v -> {
                    if (mclicK != null) {
                        mclicK.delClick(v, i, bean);
                    }
                });
                break;
            default:
                break;
        }
        holder.tvCancelOrdetail.setText(cancelText);
        holder.tvConfirm.setText(confirmText);
        holder.tvOrderState.setText(statusText);
        //holder.ivDel.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class GoodOrderListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.iv_del)
        ImageView ivDel;
        @BindView(R.id.tv_order_state)
        TextView tvOrderState;
        @BindView(R.id.rl_top)
        RelativeLayout rlTop;
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_good_name)
        TextView tvGoodName;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_goods_count)
        TextView tvGoodsCount;
        @BindView(R.id.tv_confirm)
        TextView tvConfirm;
        @BindView(R.id.tv_cancel_ordetail)
        TextView tvCancelOrdetail;
        @BindView(R.id.ll_del)
        LinearLayout llDel;


        public GoodOrderListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void cancelClick(View view, int position, GoodsOrderList bean);

        void confirmClick(View view, int position, GoodsOrderList bean);

        void allClick(View view, int position, GoodsOrderList bean);

        void delClick(View view, int position, GoodsOrderList bean);
    }


}
