package com.shosen.max.ui.circle.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.shosen.max.R;
import com.shosen.max.utils.DensityUtils;
import com.shosen.max.utils.GlideTransition.GlideCircleCornerTransform;
import com.shosen.max.utils.GlideUtils;
import com.shosen.max.utils.ScreenUtils;
import com.shosen.max.widget.circle.NineGridView;

import java.util.List;

public class NineImageAdapter implements NineGridView.NineGridAdapter<String> {

    private List<String> mImageBeans;

    private Context mContext;


    private DrawableTransitionOptions mDrawableTransitionOptions;

    public RequestOptions requestOptions = new RequestOptions()
//            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL);


    public NineImageAdapter(Context context, List<String> imageBeans) {
        this.mContext = context;
        this.mDrawableTransitionOptions = new DrawableTransitionOptions();
        this.mDrawableTransitionOptions.dontTransition();
        this.mImageBeans = imageBeans;
        int itemSize = (ScreenUtils.getScreenWidth(context) - 2 * DensityUtils.dip2px(mContext, 4) - DensityUtils.dip2px(mContext, 54)) / 3;
        if (mImageBeans.size() != 1) {
            requestOptions.
                    //override(itemSize, itemSize).
                            centerCrop().
                    transform(new GlideCircleCornerTransform(10)).
                    placeholder(R.drawable.default_long_place_holder)
                    .dontAnimate();
            //placeholder(R.drawable.default_place_holder)
        } else {
            int width = ScreenUtils.getScreenWidth(context) -
                    DensityUtils.dip2px(mContext, 54);
            int height = width * 3 / 5;
            requestOptions.
                    override(width, height).
                            centerCrop().
                    transform(new GlideCircleCornerTransform(10, width, height)).
                    //placeholder(R.drawable.default_place_holder).
                            dontAnimate();
        }

    }

    @Override
    public int getCount() {
        return mImageBeans == null ? 0 : mImageBeans.size();
    }

    @Override
    public String getItem(int position) {
        return mImageBeans == null ? null :
                position < mImageBeans.size() ? mImageBeans.get(position) : null;
    }

    @Override
    public View getView(int position, View itemView) {
        ImageView imageView;
        if (itemView == null) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.base_D8D8D8));
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            imageView = (ImageView) itemView;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        String url = mImageBeans.get(position);
        Glide.with(mContext).load(url).apply(requestOptions).transition(mDrawableTransitionOptions).into(imageView);
        return imageView;
    }
}

