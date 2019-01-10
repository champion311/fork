package com.shosen.max.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bin.david.form.data.form.IForm;
import com.shosen.max.R;
import com.shosen.max.bean.MoneyListResponse;
import com.shosen.max.presenter.MyWalletPresenter;
import com.shosen.max.utils.HDateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyWalletListAdapter extends RecyclerView.Adapter<MyWalletListAdapter.MyWalletListHolder> {


    private Context mContext;

    private List<MoneyListResponse> mData;

    private int requestCode;

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public MyWalletListAdapter(Context mContext, List<MoneyListResponse> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyWalletListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_my_wallet_list, viewGroup, false);
        return new MyWalletListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWalletListHolder holder, int i) {
        MoneyListResponse data = mData.get(i);
        holder.tvStateDate.setText(
                HDateUtils.getDate(Long.valueOf(data.getCreateTime()), "yyyy-MM-dd"));
        if (requestCode == MyWalletPresenter.GET_BALANCE_LIST) {
            String typeText = "";
            String operator = "+";
            switch (data.getType()) {
                case 1:
                    typeText = "商城消耗";
                    operator = "-";
                    break;
                case 2:
                    typeText = "社区平台管理转入";
                    operator = "+";
                    break;
                case 3:
                    //退款
                    typeText = "退款";
                    break;
                case 4:
                    //充值
                    typeText = "充值";
                    break;

            }
            holder.tvStateText.setText(typeText);
            holder.tvActionValue.setText(data.getChangeMoney());
        } else if (requestCode == MyWalletPresenter.GET_PROPERTY_LIST) {
            String typeText = "";
            String operator = "+";
            switch (data.getType()) {
                case 3:
                    typeText = "商城消耗";
                    operator = "-";
                    break;
                case 5:
                    typeText = "充值";
                    operator = "+";
                    break;
            }
            holder.tvStateText.setText(typeText);
            holder.tvActionValue.setText(data.getTypeValue());
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class MyWalletListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_state_text)
        TextView tvStateText;
        @BindView(R.id.tv_state_date)
        TextView tvStateDate;
        @BindView(R.id.tv_action_value)
        TextView tvActionValue;

        public MyWalletListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
