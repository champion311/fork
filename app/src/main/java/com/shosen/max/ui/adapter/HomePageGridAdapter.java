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
import com.shosen.max.utils.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePageGridAdapter extends RecyclerView.Adapter<HomePageGridAdapter.ViewHolder> {


    private Context mContext;

    private String[] arrayData;

    private String[] iconUrl;


    private HomePageGridOnClick mInterface;

    public void setInterface(HomePageGridOnClick mInterface) {
        this.mInterface = mInterface;
    }

    public HomePageGridAdapter(Context mContext) {
        arrayData = mContext.getResources().getStringArray(R.array.grid_array);
        iconUrl = mContext.getResources().getStringArray(R.array.home_grid_icons);
        this.mContext = mContext;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        //View view = LayoutInflater.from(mContext).inflate(R.layout.item_gridlayout, viewGroup, false);
        View view = View.inflate(mContext, R.layout.item_gridlayout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        RequestOptions reqOptions = new RequestOptions().
                diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .priority(Priority.HIGH);
        GlideUtils.loadImage(mContext, iconUrl[position], reqOptions,
                GlideUtils.defaultTransOptions, viewHolder.gridIcon);

        viewHolder.gridDes.setText(arrayData[position]);
        if (mInterface != null) {
            viewHolder.itemView.setOnClickListener(v -> {
                mInterface.OnGridClick(v, position);
            });
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.grid_icon)
        ImageView gridIcon;
        @BindView(R.id.grid_des)
        TextView gridDes;
        private Context mContext;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface HomePageGridOnClick {
        void OnGridClick(View view, int position);
    }


}
