package com.neacy.effective.mvp;

import com.neacy.effective.MainActivity;

import java.util.List;

/**
 * Created by jayuchou on 16/8/5.
 */
public class AppPresenter implements AppContract.Presenter {

    private AppContract.AppView mAppView;
    private AppModel model;

    public AppPresenter(MainActivity mActivity) {
        this.mAppView = mActivity;
        model = new AppModel(mActivity, this);
    }

    @Override
    public void start() {
        mAppView.showProgressStart();
        model.doGetAppInfos();
    }

    @Override
    public void onAppInfoSuccess(List<AppBean> beans) {
        mAppView.showProgressStop();
        mAppView.onAppLoadSuccess(beans);
    }

    @Override
    public void onAppInfoFailed(Throwable error) {
        mAppView.showProgressStop();
        mAppView.onAppLoadFailed(error);
    }
}
