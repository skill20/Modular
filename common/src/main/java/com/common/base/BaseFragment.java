package com.common.base;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Create by wangqingqing
 * On 2018/1/5 13:39
 * Copyright(c) 2017 世联行
 * Description
 */
public class BaseFragment extends Fragment {

    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}
