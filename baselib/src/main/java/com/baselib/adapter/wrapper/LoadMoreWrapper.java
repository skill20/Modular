package com.worldunion.library.adapter.wrapper;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.worldunion.library.adapter.base.ViewHolder;


/**
 * Create by wangqingqing
 * On 2017/10/31 11:13
 * Copyright(c) 2017 世联行
 * Description
 */
public class LoadMoreWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;

    private RecyclerView.Adapter mInnerAdapter;
    private View mLoadMoreView;
    private int mLoadMoreLayoutId;

    private boolean isLoadMoreEnable = true;
    private boolean isLoadData = false;

    private int mLoadState = StateType.STATE_LOADING;

    public LoadMoreWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            ViewHolder holder;
            Context context = parent.getContext();

            if (mLoadMoreView != null) {
                holder = ViewHolder.createViewHolder(context, mLoadMoreView);
            } else {
                holder = ViewHolder.createViewHolder(context, parent, mLoadMoreLayoutId);
                mLoadMoreView = holder.getConvertView();
            }

            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            setState(mLoadMoreView, mLoadState);
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    protected void setRetry() {
        if (mOnLoadMoreListener != null) {
            mLoadState = StateType.STATE_LOADING;
            setState(mLoadMoreView, mLoadState);
            mOnLoadMoreListener.onLoadMoreRequested();
        }
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + (hasLoadMoreView() ? 1 : 0);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isShowLoadMore(position)) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });

        initScroll(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (isShowLoadMore(holder.getLayoutPosition())) {
            WrapperUtils.setFullSpan(holder);
        }
    }


    private void initScroll(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new OnRecycleScrollListener() {
            @Override
            public void onLoadMore() {
                if (mOnLoadMoreListener != null && isLoadMoreEnable && !isLoadData) {
                    isLoadData = true;
                    mOnLoadMoreListener.onLoadMoreRequested();
                }
            }
        });
    }

    private boolean hasLoadMoreView() {
        return (mLoadMoreView != null || mLoadMoreLayoutId != 0) && isLoadMoreEnable;
    }


    private boolean isShowLoadMore(int position) {
        return hasLoadMoreView() && (position >= mInnerAdapter.getItemCount());
    }

    public interface OnLoadMoreListener {
        void onLoadMoreRequested();
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public LoadMoreWrapper setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        if (loadMoreListener != null) {
            mOnLoadMoreListener = loadMoreListener;
        }
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(int layoutId) {
        mLoadMoreLayoutId = layoutId;
        return this;
    }

    public LoadMoreWrapper setLoadingMoreEnabled(boolean enable) {
        isLoadMoreEnable = enable;
        return this;
    }

    public LoadMoreWrapper loadMoreComplete() {
        isLoadData = false;
        mLoadState = StateType.STATE_COMPLETE;
        setState(mLoadMoreView, mLoadState);
        return this;
    }

    public LoadMoreWrapper loadMoreFail() {
        mLoadState = StateType.STATE_LOADING_MORE_FAIL;
        setState(mLoadMoreView, mLoadState);
        return this;
    }

    public LoadMoreWrapper loadMoreNoMore() {
        mLoadState = StateType.STATE_NO_MORE;
        setState(mLoadMoreView, mLoadState);
        return this;
    }

    public void setState(View moreView, @StateType.StateTypeDef int state) {

    }
}
