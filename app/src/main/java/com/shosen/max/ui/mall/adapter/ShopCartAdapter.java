package com.shosen.max.ui.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.shosen.max.R;
import com.shosen.max.bean.mall.CartList;
import com.shosen.max.utils.CommonUtils;
import com.shosen.max.utils.GlideUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.ShopCartHolder> {


    private Context mContext;

    private List<CartList> mData;

    public static final int CART_TYPE = 100;

    public static final int SUBMIT_ORDER_TYPE = 101;

    public static final int ORDER_DETAILS_TYPE = 102;

    private int type = CART_TYPE;

    public void setType(int type) {
        this.type = type;
    }

    private NumberClick mClick;

    public void setClick(NumberClick mClick) {
        this.mClick = mClick;
    }

    public ShopCartAdapter(Context mContext, List<CartList> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ShopCartHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_shopping_cart, viewGroup, false);
        return new ShopCartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopCartHolder holder, int i) {
        Log.d("ShoppingCart", "holder position=" + i);
        CartList bean = mData.get(i);
        holder.tvGoodName.setText(bean.getGoodsName());
        holder.tvGoodsPrice.setText(CommonUtils.getFormatPrice(bean.getPrice(), bean.isPointGoods()));
        RequestOptions options = new RequestOptions();
        options.priority(Priority.HIGH).centerCrop().
                error(R.drawable.default_place_holder);
        GlideUtils.loadImage(mContext, bean.getPicUrl(), options,
                GlideUtils.defaultTransOptions, holder.ivGoodsImage);
        holder.tvAdd.setOnClickListener(v -> {
            if (mClick != null) {
                mClick.addClick(v, i, bean);
            }
        });
        holder.tvMinus.setOnClickListener(v -> {
            if (mClick != null) {
                mClick.minusClick(v, i, bean);
            }
        });
        holder.tvBuyCount.setText(bean.getCount() + "");
        holder.ivGoodsMark.setSelected(bean.isSelected());
        holder.ivGoodsMark.setTag(i);
        holder.ivGoodsMark.setOnClickListener(v -> {
            if (v.getTag() != null) {
                int pos = (int) v.getTag();
                if (mClick != null) {
                    mClick.markClick(v, pos);
                }
            }
        });
        if (type == SUBMIT_ORDER_TYPE) {
            holder.ivGoodsMark.setVisibility(View.GONE);
            holder.llCount.setVisibility(View.GONE);
            holder.tvGoodsCount.setVisibility(View.VISIBLE);
            holder.tvGoodsCount.setText("x" + String.valueOf(bean.getCount()));
        } else if (type == ORDER_DETAILS_TYPE) {
            holder.ivGoodsMark.setVisibility(View.GONE);
            holder.llCount.setVisibility(View.GONE);
            holder.tvGoodsCount.setVisibility(View.VISIBLE);
            holder.tvGoodsCount.setText("x" + String.valueOf(bean.getNumber()));
        } else {
            holder.ivGoodsMark.setVisibility(View.VISIBLE);
            holder.llCount.setVisibility(View.VISIBLE);
            holder.tvGoodsCount.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    public class ShopCartHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_goods_mark)
        ImageView ivGoodsMark;
        @BindView(R.id.iv_goods_image)
        ImageView ivGoodsImage;
        @BindView(R.id.tv_good_name)
        TextView tvGoodName;
        @BindView(R.id.tv_goods_price)
        TextView tvGoodsPrice;
        @BindView(R.id.tv_minus)
        TextView tvMinus;
        @BindView(R.id.tv_buy_count)
        TextView tvBuyCount;
        @BindView(R.id.tv_add)
        TextView tvAdd;
        @BindView(R.id.ll_count)
        LinearLayout llCount;
        @BindView(R.id.tv_goods_count)
        TextView tvGoodsCount;

        public ShopCartHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface NumberClick {

        void addClick(View view, int position, CartList bean);

        void minusClick(View view, int position, CartList bean);

        void markClick(View view, int position);
    }

    public String getTotalPriceStr() {
        return CommonUtils.getFormatPrice(getTotalPrice(), false);
    }


    //TODO
    public double getTotalPrice() {
        double ret = 0;
        for (CartList cartList : mData) {
            if (cartList.isSelected()) {
                ret += cartList.getPrice() * cartList.getCount();
            }
        }
        return ret;
    }

    /**
     * 删除选中元素
     */
    public void delSelectedItem() {
        Iterator<CartList> it = mData.iterator();
        while (it.hasNext()) {
            if (it.next().isSelected()) {
                it.remove();
            }
        }
        notifyDataSetChanged();
    }

    public int getSelectedGoodsCount() {
        int ret = 0;
        for (CartList cartList : mData) {
            if (cartList.isSelected()) {
                ret += cartList.getCount();
            }
        }
        return ret;
    }

    public Integer[] getSelectedIds() {
        List<Integer> ret = new ArrayList<>();
        for (CartList cartList : mData) {
            if (cartList.isSelected()) {
                ret.add(cartList.getProductId());
            }
        }
        return ret.toArray(new Integer[ret.size()]);
    }

    public Integer[] getUnSelectedIds() {
        ArrayList<Integer> ret = new ArrayList<>();
        for (CartList cartList : mData) {
            if (!cartList.isSelected()) {
                ret.add(cartList.getProductId());
            }
        }
        return ret.toArray(new Integer[ret.size()]);
    }

    public ArrayList<Integer> getUnSelectedIdsArray() {
        ArrayList<Integer> ret = new ArrayList<>();
        for (CartList cartList : mData) {
            if (!cartList.isSelected()) {
                ret.add(cartList.getProductId());
            }
        }
        return ret;
    }


    public ArrayList<CartList> getSelectedItems() {
        ArrayList<CartList> ret = new ArrayList<>();
        for (CartList cartList : mData) {
            if (cartList.isSelected()) {
                ret.add(cartList);
            }
        }
        return ret;
    }

    public void setAllState(boolean isSelected) {
        for (CartList cartList : mData) {
            cartList.setSelected(isSelected);
        }
        notifyDataSetChanged();
    }

}
