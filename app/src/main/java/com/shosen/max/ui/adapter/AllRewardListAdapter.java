package com.shosen.max.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shosen.max.R;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.bean.LuckyPanReward;
import com.shosen.max.constant.Contstants;
import com.shosen.max.utils.CacheUtils;
import com.shosen.max.utils.RegexUtils;
import com.shosen.max.utils.SPUtils;
import com.shosen.max.utils.SpanUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllRewardListAdapter extends
        RecyclerView.Adapter<AllRewardListAdapter.AllRewardListHolder> {


    private Context mContext;

    private List<LuckyPanReward> mData;


    public AllRewardListAdapter(Context mContext, List<LuckyPanReward> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public AllRewardListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.
                from(mContext).inflate(R.layout.item_all_reward_list, viewGroup, false);
        return new AllRewardListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllRewardListHolder holder, int position) {
        LuckyPanReward data = mData.get(position);
        if (position % 2 == 0) {
            holder.llWrapper.setBackgroundColor(ContextCompat.getColor(mContext, R.color.pan_color_dark));
        } else {
            holder.llWrapper.setBackgroundColor(ContextCompat.getColor(mContext, R.color.pan_color_light));
        }
        holder.tvRewardPhone.setText(getFormatPhoneNumber(data.getPhone()));
        holder.tvRewardDes.setText(searchDictionaryData(data.getRewardTitle()));
    }

    @Override
    public int getItemCount() {
        return mData.size() >= 5 ? 5 : mData.size();
    }

    public class AllRewardListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_reward_phone)
        TextView tvRewardPhone;
        @BindView(R.id.tv_reward_des)
        TextView tvRewardDes;

        @BindView(R.id.ll_wrapper)
        LinearLayout llWrapper;

        public AllRewardListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public String getFormatPhoneNumber(String phoneNum) {
        if (!RegexUtils.isMobileSimple(phoneNum)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < phoneNum.length(); i++) {
            if (i >= 3 && i <= 6) {
                sb.append("*");
            } else {
                sb.append(phoneNum.charAt(i));
            }
        }
        return sb.toString();
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
        return "";
    }
}
