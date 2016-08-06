package com.neacy.effective.mvp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.neacy.effective.MainActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jayuchou on 16/8/5.
 */
public class AppModel {
    private MainActivity mActivity;
    private AppPresenter presenter;

    public AppModel(MainActivity mActivity, AppPresenter presenter) {
        this.mActivity = mActivity;
        this.presenter = presenter;
    }

    public void doGetAppInfos() {
        Observable.create(new Observable.OnSubscribe<List<AppBean>>() {
            @Override
            public void call(Subscriber<? super List<AppBean>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                PackageManager pm = mActivity.getPackageManager();
                List<PackageInfo> packages = pm.getInstalledPackages(0);
                if (packages == null || packages.isEmpty()) {
                    subscriber.onError(new Exception("no app can find.."));
                    return;
                }
                List<AppBean> apps = new ArrayList<>();
                AppBean appBean;
                for (PackageInfo info : packages) {
                    appBean = new AppBean();
                    appBean.appIcon = info.applicationInfo.loadIcon(pm);
                    appBean.appName = (String) info.applicationInfo.loadLabel(pm);
                    apps.add(appBean);
                }
                if (apps.isEmpty()) {
                    subscriber.onError(new Exception("app list empty.."));
                    return;
                }
                subscriber.onNext(apps);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AppBean>>() {
                    @Override
                    public void call(List<AppBean> apps) {
                        presenter.onAppInfoSuccess(apps);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        presenter.onAppInfoFailed(throwable);
                    }
                });
    }
}
