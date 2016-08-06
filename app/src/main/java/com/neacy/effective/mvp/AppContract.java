package com.neacy.effective.mvp;

import java.util.List;

/**
 * Created by jayuchou on 16/8/5.
 */
public interface AppContract {

    interface Presenter extends BasePresenter {
        void onAppInfoSuccess(List<AppBean> beans);
        void onAppInfoFailed(Throwable error);
    }

    interface AppView {
        void showProgressStart();
        void showProgressStop();
        void onAppLoadSuccess(List<AppBean> beans);
        void onAppLoadFailed(Throwable error);
    }
}
