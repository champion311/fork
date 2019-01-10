package com.shosen.max.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shosen.max.R;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.bean.LuckyPanReward;
import com.shosen.max.bean.RewardListBean;
import com.shosen.max.constant.Contstants;
import com.shosen.max.utils.CacheUtils;
import com.shosen.max.utils.HDateUtils;
import com.shosen.max.utils.RegexUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyRewardAdapter extends RecyclerView.Adapter<MyRewardAdapter.MyRewardHolder> {


    private Context mContext;

    private List<LuckyPanReward> mData;

    public MyRewardAdapter(Context mContext, List<LuckyPanReward> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyRewardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_reward_list, viewGroup, false);
        return new MyRewardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRewardHolder holder, int i) {
        LuckyPanReward bean = mData.get(i);

        String date = HDateUtils.getDate(Long.valueOf(bean.getCreateTime()), "yyyy-MM-dd HH:mm");
        holder.tvContributeDate.setText(date + "获奖");
        holder.tvContributeValue.setText(searchDictionaryData(bean.getRewardTitle()));
        String status = "";
        if ("1".equals(bean.getGetType())) {
            status = "已发放至账户中";
        } else {
            status = "联系方式见活动说明可领取";
        }
        holder.tvRewardState.setText(status);

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class MyRewardHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_contribution)
        TextView tvContribution;
        @BindView(R.id.tv_contribute_date)
        TextView tvContributeDate;
        @BindView(R.id.divider_line)
        View dividerLine;
        @BindView(R.id.iv_contribute_icon)
        ImageView ivContributeIcon;
        @BindView(R.id.tv_contribute_value)
        TextView tvContributeValue;
        @BindView(R.id.tv_reward_state)
        TextView tvRewardState;

        public MyRewardHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public String searchDictionaryData(String rewardTitle) {
        if (TextUtils.isEmpty(rewardTitle)) {
            return "";
        }
        Gson gson = new Gson();
        CacheUtils cacheUtils = CacheUtils.getInstance(Contstants.DICTIONARY_CACHE);
        if (!TextUtils.isEmpty(cacheUtils.getString("14"))) {
            List<DictionaryBean> ret = gson.fromJson(cacheUtils.getString("14"),
                    new TypeToken<List<DictionaryBean>>() {
                    }.getType());
            for (DictionaryBean bean : ret) {
                if (rewardTitle.equals(bean.getDicCode())) {
                    return bean.getDicValue();
                }
            }
        }
        return rewardTitle;
    }
}
