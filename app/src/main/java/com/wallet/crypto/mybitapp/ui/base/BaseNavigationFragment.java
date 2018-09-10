package com.wallet.crypto.mybitapp.ui.base;

public abstract class BaseNavigationFragment<T extends BaseViewModel> extends BaseFragment<T> {

    public void setTitles(String title, String subTitle) {
        getNavigationActivity().setTitles(title, subTitle, this);
    }

    public abstract void reinitTitles();

    private BaseNavigationActivity getNavigationActivity() {
        return (BaseNavigationActivity) getActivity();
    }
}
