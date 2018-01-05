package com.baselib.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.R;

/**
 * Create by wangqingqing
 * On 2018/1/4 15:46
 * Copyright(c) 2017 世联行
 * Description
 */
public class BaseTitleActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (supportTitle()) {
            setContentView(R.layout.base_activity_title);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (!supportTitle()) {
            super.setContentView(layoutResID);
            return;
        }
        setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        if (!supportTitle()) {
            super.setContentView(view);
            return;
        }
        LinearLayout mRootLayout = (LinearLayout) findViewById(R.id.layout_root);
        if (mRootLayout != null) {
            mRootLayout.addView(view, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            initTitleLayout();
        }

    }

    private void initTitleLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() == null) {
            throw new RuntimeException("set toolbar exception");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        int navigationIcon = getNavigationIcon();
        if (navigationIcon > 0) {
            toolbar.setNavigationIcon(navigationIcon);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }


        TextView titleView = (TextView) findViewById(R.id.toolbar_title);
        setTitleView(titleView);

        TextView subTitleView = (TextView) findViewById(R.id.toolbar_subtitle);
        setSubTitleView(subTitleView);

        int res = getToolbarBackground();
        toolbar.setBackgroundResource(res);

        View dividerView = findViewById(R.id.v_title_divider);
        setTitleDividerView(dividerView);

        View gepView = findViewById(R.id.v_title_gep);
        setTitleGepView(gepView);

        ImageView rightImageView = (ImageView) findViewById(R.id.iv_right);
        setRightImageView(rightImageView);
    }

    protected void setTitleGepView(View gepView) {

    }

    protected void setTitleDividerView(View dividerView) {

    }

    protected void setTitleView(TextView titleView) {
    }

    protected void setSubTitleView(TextView subTitleView) {
    }

    protected boolean supportTitle() {
        return true;
    }

    protected int getNavigationIcon() {
        return R.drawable.ic_default_navigation;
    }

    protected int getToolbarBackground() {
        return Color.WHITE;
    }

    protected void setRightImageView(ImageView imageView) {

    }
}
