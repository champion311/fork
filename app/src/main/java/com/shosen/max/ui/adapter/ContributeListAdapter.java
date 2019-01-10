package com.shosen.max.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.bean.ContributeListBean;
import com.shosen.max.utils.HDateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContributeListAdapter extends RecyclerView.Adapter<ContributeListAdapter.ContributeHolder> {


    private Context mContext;

    private List<ContributeListBean> mData;


    public ContributeListAdapter(Context mContext, List<ContributeListBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ContributeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.item_contribute_list, null);
        return new ContributeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContributeHolder contributeHolder, int i) {
        ContributeListBean bean = mData.get(i);
        contributeHolder.tvContributeContent.setText(bean.getType());
        contributeHolder.tvContributeValue.setText("+" + bean.getTypeValue() + "贡献值");
        String dateText = HDateUtils.getDate(Long.valueOf(bean.getCreateTime()), "yyyy-MM-dd");
        contributeHolder.tvContributeDate.setText(dateText);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ContributeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_contribute_content)
        TextView tvContributeContent;
        @BindView(R.id.tv_contribute_date)
        TextView tvContributeDate;
        @BindView(R.id.tv_contribute_value)
        TextView tvContributeValue;

        public ContributeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
