package com.wallet.crypto.mybitapp.ui.activity.main;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.ui.base.BaseNavigationActivity;
import com.wallet.crypto.mybitapp.ui.base.BaseNavigationFragment;
import com.wallet.crypto.mybitapp.ui.fragment.settings.SettingsFragment;
import com.wallet.crypto.mybitapp.ui.fragment.wallet.WalletFragment;
import com.wallet.crypto.mybitapp.ui.fragment.transactions.TransactionsFragment;
import com.wallet.crypto.mybitapp.ui.widget.view.NonSwipeableViewPager;
import com.wallet.crypto.mybitapp.util.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjection;

public class MainActivity extends BaseNavigationActivity<MainViewModel> {
    private static final int ICON_BOTTOM_SIZE_DP = 17;

    @Inject
    MainViewModelFactory mainViewModelFactory;

    private List<BaseNavigationFragment> fragments;

    @BindView(R.id.root_view_pager)
    NonSwipeableViewPager viewPager;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        toolbar();
        initBottomNavigation();
        disableDisplayHomeAsUp();
        setBottomMenu(R.menu.menu_main_network);
        initFragments();
        initNavigationViewPager();
    }

    @Override
    protected void onPrepareViewModel() {
        viewModel = ViewModelProviders.of(this, mainViewModelFactory)
                .get(MainViewModel.class);
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.add(WalletFragment.create());
        fragments.add(TransactionsFragment.create());
        fragments.add(SettingsFragment.create());
    }

    private void initNavigationViewPager() {
        initNavigationViewPagerAdapter();
        viewPager.setOffscreenPageLimit(fragments.size());
        changeBottomNavigationIconsSizes();
        setCurrentFragment(0);
    }

    private void changeBottomNavigationIconsSizes() {
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        setIconsLayoutParamsSizes(bottomNavigationMenuView);
    }

    private void setIconsLayoutParamsSizes(BottomNavigationMenuView bottomNavigationMenuView) {
        for (int i = 0; i < bottomNavigationMenuView.getChildCount(); i++) {
            final View iconView = bottomNavigationMenuView.getChildAt(i).findViewById(R.id.icon);
            final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = DisplayUtils.getPxFromDp(displayMetrics, ICON_BOTTOM_SIZE_DP);
            layoutParams.width = DisplayUtils.getPxFromDp(displayMetrics, ICON_BOTTOM_SIZE_DP);
            iconView.setLayoutParams(layoutParams);
        }
    }

    private void initNavigationViewPagerAdapter() {
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_wallet: {
                setCurrentFragment(0);
                return true;
            }
            case R.id.action_transactions: {
                setCurrentFragment(1);
                return true;
            }
            case R.id.action_settings: {
                setCurrentFragment(2);
                return true;
            }
        }
        return false;
    }

    private void setCurrentFragment(int currentFragmentIndex) {
        viewPager.setCurrentItem(currentFragmentIndex);
        BaseNavigationFragment fragment = fragments.get(currentFragmentIndex);
        tryReInitTitle(fragment);
    }

    private void tryReInitTitle(BaseNavigationFragment fragment) {
        if (fragment.isAdded()) {
            fragment.reinitTitles();
        } else {
            setTitle(getString(R.string.navigation_wallet));
            setSubtitle("");
        }
    }

    @Override
    public void setTitles(String title, String subTitle, BaseNavigationFragment fragment) {
        if (checkCurrentShownFragment(fragment)) {
            super.setTitles(title, subTitle, fragment);
        }
    }

    private boolean checkCurrentShownFragment(BaseNavigationFragment fragment) {
        int currentFragmentIndex = viewPager.getCurrentItem();
        int fragmentIndex = fragments.indexOf(fragment);
        return currentFragmentIndex == fragmentIndex;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == C.WALLET_CREATION_RESULT_CODE) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (getPinFragment().isAdded() || viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            int itemId = bottomNavigationView.getMenu().getItem(0).getItemId();
            bottomNavigationView.setSelectedItemId(itemId);
        }
    }
}