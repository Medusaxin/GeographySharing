package com.example.lenovo.geographysharing.widget;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.others.DividerItemDecoration;

/**
 * Created by lenovo on 2017/12/31.
 */

public class PullLoadRecyclerView extends LinearLayout {
    //继承自LinerLayout布局
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新的控件，v4包中的
    private boolean mIsRefresh = false;//是否刷新
    private boolean mIsLoadMore = false;//是否加载更多
    private RecyclerView mRecyclerView;
    private View mFootView;
    private AnimationDrawable mAnimationDrawable;
    private OnPullLoadMoreListener mOnPullLoadMoreListener;


    //构造方法（1）
    public PullLoadRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    //构造方法（2）
    public PullLoadRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    //构造方法（3）
    public PullLoadRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    //initView函数用来初始化
    private void initView(Context context) {
        mContext = context;//获得当构造方法传入的上下文
        View view = LayoutInflater.from(mContext).inflate(R.layout.pull_loadmore_layout, null);
        //绑定组件（下拉刷新的组件，recyclerview嵌套其中）
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        //设置刷新时，颜色渐变。
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        //设置下拉控件的监听，SwipeRefreshLayoutOnRefresh为函数内部类（实现监听接口的内部类）
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutOnRefresh());

        //处理Recyclerview
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        //设置固定大小
        mRecyclerView.setHasFixedSize(true);
        //设置item的默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mIsRefresh || mIsLoadMore;
            }
        });
        //隐藏滚动条
        mRecyclerView.setVerticalScrollBarEnabled(false);
        //加载更多的时候，需要监听滚动来做处理
        mRecyclerView.addOnScrollListener(new RecyclerViewOnScroll());
        //加载更多时，底部footview控件
        mFootView = view.findViewById(R.id.foot_view);
        ImageView imageview = (ImageView) mFootView.findViewById(R.id.iv_load_image);
        imageview.setBackgroundResource(R.drawable.imooc_loading);
        //强转给AnimationDrawable
        mAnimationDrawable = (AnimationDrawable) imageview.getBackground();
//        TextView textview = (TextView) mFootView.findViewById(R.id.iv_load_text);
        //设置footer为默认隐藏
        mFootView.setVisibility(View.GONE);
        //此时的view包含mSwipeRefreshLayout，mRecyclerView，mFootView
        this.addView(view);
    }

    //外部可以设置Recyclerview的列数
    public void setGridLayout() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        //设置Recyclerview的分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
    }

    //
    class SwipeRefreshLayoutOnRefresh implements SwipeRefreshLayout.OnRefreshListener {

        public void setAdapter(RecyclerView.Adapter adapter) {
            if (adapter != null) {
                mRecyclerView.setAdapter(adapter);
            }
        }

        @Override
        public void onRefresh() {
            if (!mIsRefresh) {
                mIsRefresh = true;
                refreshData();
            }
        }
    }

    //设置下拉控件的监听，SwipeRefreshLayoutOnRefresh为函数内部类（实现监听接口的内部类）
    class RecyclerViewOnScroll extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int firstItem = 0;
            int lastItem = 0;
            RecyclerView.LayoutManager manger = recyclerView.getLayoutManager();
            int totalCount = manger.getItemCount();

            //这里是网格的，日后记得修改，根据自己的
            if (manger instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manger;
                //设置第一个完全可见
                firstItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                //设置最后一个完全可见
                lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (firstItem == 0 || firstItem == RecyclerView.NO_POSITION) {
                    lastItem = linearLayoutManager.findLastVisibleItemPosition();
                }
            }
            //什么触发上拉加载更多
            if (mSwipeRefreshLayout.isEnabled()) {
                mSwipeRefreshLayout.setEnabled(true);
            } else {
                mSwipeRefreshLayout.setEnabled(false);
            }
            //加载更多是false
            //总数等于最后一个位置
            //刷新布局Swipe可以使用
            //不是出于下拉刷新状态
            //偏移量dy大于零
            if (!mIsLoadMore
                    && totalCount - 1 == lastItem
                    && (dx > 0 || dy > 0)
                    && !mIsRefresh
                    && mSwipeRefreshLayout.isEnabled()) {
                mIsLoadMore = true;
                //加载更多数据的函数
                loadMoreData();
            }
        }
    }

    //加载更多数据（包含设置动画）
    private void loadMoreData() {
        if (mOnPullLoadMoreListener != null) {
            mOnPullLoadMoreListener.loadMore();
            mFootView.animate().translationY(10).setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(300).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mFootView.setVisibility(View.VISIBLE);
                    mOnPullLoadMoreListener.loadMore();
                    mAnimationDrawable.start();

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();
            invalidate();
        }
    }

    //设置刷新完成
    public void setRefreshCompletely() {
        mIsRefresh = false;
        setRefreshing(false);
    }

    //设置是否正在刷新
    private void setRefreshing(final boolean isRefreshing) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });
    }
    //设置加载更多完成
    public void setLoadMoreCompleted() {
        mIsLoadMore = false;
        mIsRefresh = false;
        setRefreshing(false);
        mFootView.animate().translationY(mFootView.getHeight()).setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(300).start();
    }

    //刷新数据的函数
    private void refreshData() {
        if (mOnPullLoadMoreListener != null) {
            mOnPullLoadMoreListener.refresh();
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mRecyclerView.setAdapter(adapter);
        }
    }

    //监听接口
    public interface OnPullLoadMoreListener {
        void refresh();

        void loadMore();
    }

    //为监听接口实现set方法
    public void setOnPullLoadMoreListener(OnPullLoadMoreListener listener) {
        mOnPullLoadMoreListener = listener;
    }
}
