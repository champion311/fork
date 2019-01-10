package com.shosen.max.ui.mall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.bean.MallAddressBean;
import com.shosen.max.others.span.OrangeBgTextSpan;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressListHolder> {


    private Context mContext;

    private List<MallAddressBean> mData;

    private OnAddressClick onAddressClick;

    public void setOnAddressClick(OnAddressClick onAddressClick) {
        this.onAddressClick = onAddressClick;
    }

    public AddressListAdapter(Context mContext, List<MallAddressBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AddressListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_address, viewGroup, false);
        return new AddressListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressListHolder holder, int i) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        MallAddressBean bean = mData.get(i);
        if (bean == null) {
            return;
        }
        holder.tvName.setText(bean.getName());
        holder.tvCellphone.setText(bean.getMobile());
        String addressText = bean.getProvince() + bean.getCity() + bean.getArea() + bean.getAddress();
        if (bean.isDefault()) {
            holder.tvAddress.setText(getDefaultText("默认", addressText));
        } else {
            holder.tvAddress.setText(addressText);
        }
        holder.itemView.setOnClickListener(v -> {
            if (onAddressClick != null) {
                onAddressClick.onAddressClick(v, i, bean);
            }
        });
        holder.flIvEdit.setOnClickListener(v -> {
            if (onAddressClick != null) {
                onAddressClick.onEditClick(v, i, bean);
            }
        });


    }

    public SpannableString getDefaultText(String text1, String text2) {
        text1 = " " + text1 + " ";
        String text = text1 + " " + text2;
        SpannableString spannableString = new SpannableString(text);
        OrangeBgTextSpan orangeBgTextSpan = new OrangeBgTextSpan(
                ContextCompat.getColor(mContext, R.color.default_address_bg_color), mContext);
        spannableString.setSpan(orangeBgTextSpan, 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class AddressListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_cellphone)
        TextView tvCellphone;
        @BindView(R.id.iv_edit)
        ImageView ivEdit;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.fl_iv_edit)
        FrameLayout flIvEdit;

        public AddressListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface OnAddressClick {
        void onAddressClick(View view, int position, MallAddressBean bean);

        void onEditClick(View view, int position, MallAddressBean bean);
    }
}
