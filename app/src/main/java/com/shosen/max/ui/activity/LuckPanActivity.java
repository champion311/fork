package com.shosen.max.ui.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shosen.max.R;
import com.shosen.max.base.BaseActivity;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.bean.LotteryResponse;
import com.shosen.max.bean.LuckyPanReward;
import com.shosen.max.presenter.LuckPanPresenter;
import com.shosen.max.presenter.contract.LuckPanContract;
import com.shosen.max.ui.adapter.AllRewardListAdapter;
import com.shosen.max.utils.ActivityUtils;
import com.shosen.max.utils.LoginUtils;
import com.shosen.max.utils.StatusBarUtil;
import com.shosen.max.utils.ToastUtils;
import com.shosen.max.widget.dialog.LuckPanLoseDialog;
import com.shosen.max.widget.dialog.LuckPanWinDialog;
import com.shosen.max.widget.lunckpan.LuckPanLayout;
import com.shosen.max.widget.lunckpan.RotatePan;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LuckPanActivity extends BaseActivity implements
        LuckPanContract.View, LuckPanLayout.AnimationEndListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_head_title)
    TextView tvHeadTitle;
    @BindView(R.id.rl_top_header)
    RelativeLayout rlTopHeader;
    @BindView(R.id.tv_left_count)
    TextView tvLeftCount;
    @BindView(R.id.rotatePan)
    RotatePan rotatePan;
    @BindView(R.id.iv_go)
    ImageView ivGo;
    @BindView(R.id.luckpan_layout)
    LuckPanLayout luckpanLayout;
    @BindView(R.id.tv_my_award)
    TextView tvMyAward;
    @BindView(R.id.rc_reward_list)
    RecyclerView rcRewardList;
    @BindView(R.id.event_des)
    TextView eventDes;
    private LuckPanPresenter mPresenter;

    private int leftTime;

    private List<DictionaryBean> dictionaryData;

    public static final int PAN_DATA_TYPE = 14;

    public static final int EVENT_DES_TYPE = 1000;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        mPresenter = new LuckPanPresenter(this);
        setPresenter(mPresenter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViewAndEvents() {
        StatusBarUtil.setDarkMode(this);
        StatusBarUtil.setColorNoTranslucent(this,
                getResources().getColor(R.color.base_101012));
        tvMyAward.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvMyAward.setText("查看我的奖品");
        mPresenter.loadPanData(PAN_DATA_TYPE);
        mPresenter.loadPanData(EVENT_DES_TYPE);
        mPresenter.getRewardList();
        luckpanLayout.setAnimationEndListener(this);
        rcRewardList.setLayoutManager(new LinearLayoutManager(this));
        tvHeadTitle.setText("幸运大转盘");
        if (LoginUtils.hasRightLottery()) {
            mPresenter.getLotteryLeftTimes();
        } else {
            leftTime = 0;
        }

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_lucky_pan;
    }

    @OnClick({R.id.iv_back, R.id.iv_go, R.id.tv_my_award})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_go:
                if (LoginUtils.isLogin) {
                    if (leftTime > 0) {
                        mPresenter.lottery(LoginUtils.getUser().getPhone());
                    } else {
                        if (loseDialog == null) {
                            loseDialog = new LuckPanLoseDialog(this, R.style.CancelDialog);
                        }
                        if (LoginUtils.hasRightLottery()) {
                            loseDialog.setText(getString(R.string.lottery_run_out), getString(R.string.lottery_next));
                        } else {
                            loseDialog.setText(getString(R.string.lottery_no_right),
                                    getString(R.string.lottery_alert_text));
                        }
                        loseDialog.show();
                    }
                }
                break;
            case R.id.tv_my_award:
                ActivityUtils.startActivity(MyRewardListActivity.class);
                break;

        }

    }

    @Override
    public void showErrorMessage(String message) {
        ToastUtils.show(this, message);
    }

    @Override
    public void showDictionaryData(int type, List<DictionaryBean> mData) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        switch (type) {
            case PAN_DATA_TYPE:
                String[] str = new String[mData.size()];
                for (int i = 0; i < str.length; i++) {
                    String text = mData.get(i).getDicValue();
                    int index = text.indexOf(",");
                    if (index != -1) {
                        text = text.replaceAll(",", " ");
                    }
                    mData.get(i).setDicValue(text);
                }
                this.dictionaryData = mData;
                //rotatePan.setStr(str);
                break;
            case EVENT_DES_TYPE:
                eventDes.setText(getEventDesStr(mData));
                break;
        }
    }

    /**
     * 抽奖结果
     *
     * @param response
     */
    @Override
    public void showLotteryResponse(LotteryResponse response) {
        if (response != null) {
            tvLeftCount.setText("剩余抽奖次数：" + response.getTime());
            leftTime = Integer.valueOf(response.getTime());
            luckpanLayout.rotate(Integer.valueOf(response.getData()));
        }

    }

    /**
     * 所有获奖列表
     *
     * @param mData
     */
    @Override
    public void showAllRewardList(List<LuckyPanReward> mData) {
        rcRewardList.setAdapter(new AllRewardListAdapter(this, mData));
    }

    /**
     * 出事化剩余抽奖次数
     *
     * @param time
     */
    @Override
    public void showLeftLotteryTime(String time) {
        tvLeftCount.setText("剩余抽奖次数：" + time);
        leftTime = Integer.valueOf(time);
    }

    public String getEventDesStr(List<DictionaryBean> mData) {
        if (mData == null || mData.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (DictionaryBean bean : mData) {
            if (sb.length() != 0) {
                sb.append("\n");
            }
            sb.append(bean.getDicValue());
        }
        sb.append("\n");
        return sb.toString();
    }

    public String getTrimString(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("  ");
            }
            stringBuilder.append(str.charAt(i));
        }
        return stringBuilder.toString();
    }

    private LuckPanWinDialog winDialog;

    private LuckPanLoseDialog loseDialog;


    @Override
    public void endAnimation(int position) {
        //ToastUtils.show(this, String.valueOf(position));
        if (dictionaryData == null) {
            return;
        }
        if (position == dictionaryData.size() - 1) {
            if (loseDialog == null) {
                loseDialog = new LuckPanLoseDialog(this);
            }
            loseDialog.setText(getString(R.string.lottery_failed), "");
            //loseDialog.setConfirmButtonText("确定");
            loseDialog.show();
        } else {
            if (winDialog == null) {
                winDialog = new LuckPanWinDialog(this);
            }
            winDialog.setText(getString(R.string.lottery_success), dictionaryData.get(position).getDicValue());
            winDialog.show();
        }
    }

}
