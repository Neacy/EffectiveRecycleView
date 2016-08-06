package com.neacy.effective;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.neacy.effective.click.AppClickListener;
import com.neacy.effective.mvp.AppBean;
import com.neacy.effective.mvp.AppContract;
import com.neacy.effective.mvp.AppPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import itemdecoration.AppItemDecoration;
import recyclerview.EndlessRecyclerOnScrollListener;
import recyclerview.HeaderAndFooterRecyclerViewAdapter;
import recyclerview.LoadingFooter;
import recyclerview.RecyclerViewStateUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements AppContract.AppView {

    private static final String TAG = "MainActivity";

    private AppPresenter mAppPresnter;
    private RecyclerView mRecycleView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HeaderAndFooterRecyclerViewAdapter mAdapter;
    private List<AppBean> beans = new ArrayList<>();
    private List<AppBean> totalBeans = new ArrayList<>();

    // 数据总量大小
    private int totalCount;
    // 一次显示多少数据
    private static final int INDEX = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swiperefreshlayout);
        mRecycleView = (RecyclerView) findViewById(R.id.id_recycleview);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(manager);

        AppRecycleAdapter adapter = new AppRecycleAdapter(this, beans);
        mAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        mRecycleView.setAdapter(mAdapter);

        AppItemDecoration decoration = new AppItemDecoration(this, 2, android.R.color.holo_blue_dark);
        mRecycleView.addItemDecoration(decoration);

        mAppPresnter = new AppPresenter(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAppPresnter.start();
            }
        });

        doRefresh();
        doItemClick();
        doLoadMore();
    }

    /**
     * 到底部加载更多..
     */
    private void doLoadMore() {
        mRecycleView.addOnScrollListener(new EndlessRecyclerOnScrollListener(this) {
            @Override
            public void onLoadNextPage(View view) {
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecycleView);
                if (state == LoadingFooter.State.Loading || state == LoadingFooter.State.TheEnd) {
                    Log.i(TAG, "--- loading or end ---");
                    return;
                }
                // 加载更多
                RecyclerViewStateUtils.setFooterViewState(mRecycleView, LoadingFooter.State.Loading);
                loadMore();
            }
        });
    }

    /**
     * 通过实现SimpleItemTouchListener实现点击事件..
     */
    private void doItemClick() {
        mRecycleView.addOnItemTouchListener(new AppClickListener(mRecycleView, new AppClickListener.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, beans.get(position).appName, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    /**
     * 设置下拉刷新..
     */
    private void doRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                totalBeans.clear();
                beans.clear();
                mAppPresnter.start();
            }
        });
    }

    /**
     * 模拟3s后加载数据.
     */
    private void loadMore() {
        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (beans.size() >= totalCount) {
                            RecyclerViewStateUtils.setFooterViewState(mRecycleView, LoadingFooter.State.TheEnd);
                        } else {
                            final int remindCount = totalCount - beans.size();
                            beans.addAll(totalBeans.subList(beans.size(), (remindCount > INDEX ? INDEX : remindCount) + beans.size()));
                            RecyclerViewStateUtils.setFooterViewState(mRecycleView, LoadingFooter.State.Normal);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    @Override
    public void showProgressStart() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showProgressStop() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onAppLoadSuccess(List<AppBean> _beans) {
        totalCount = _beans.size();
        totalBeans.addAll(_beans);
        if (totalCount <= INDEX) {// 如果当前的总量少于INDEX的话直接全部显示出来
            beans.addAll(_beans);
            RecyclerViewStateUtils.setFooterViewState(this, mRecycleView, INDEX, LoadingFooter.State.TheEnd, null);
        } else {// 一次显示INDEX个数据
            beans.addAll(totalBeans.subList(0, INDEX));
            RecyclerViewStateUtils.setFooterViewState(this, mRecycleView, INDEX, LoadingFooter.State.Normal, null);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAppLoadFailed(Throwable error) {
    }
}
