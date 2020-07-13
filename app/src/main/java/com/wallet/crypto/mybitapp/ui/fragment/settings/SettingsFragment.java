package com.wallet.crypto.mybitapp.ui.fragment.settings;

import androidx.lifecycle.ViewModelProviders;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;

import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.ui.base.BaseNavigationFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsFragment extends BaseNavigationFragment<SettingsViewModel> {
    @Inject
    SettingsViewModelFactory settingsViewModelFactory;

    @BindView(R.id.address)
    TextView walletAddess;

    @BindView(R.id.tv_version)
    TextView version;

    @BindView(R.id.switch_pin)
    SwitchCompat pinSwitch;

    @OnClick(R.id.wallets_item)
    void onWalletsClick(View view) {
        viewModel.showManageWallets(getContext());
    }

    @OnClick(R.id.facebook_item)
    void onFacebookClick(View view) {
        viewModel.showSocial(getContext(), getString(R.string.url_facebook));
    }

    @OnClick(R.id.reddit_item)
    void onRedditClick(View view) {
        viewModel.showSocial(getContext(), getString(R.string.url_reddit));
    }

    @OnClick(R.id.youtube_item)
    void onYoutubeClick(View view) {
        viewModel.showSocial(getContext(), getString(R.string.url_youtube));
    }

    @OnClick(R.id.discord_item)
    void onDiscordClick(View view) {
        viewModel.showSocial(getContext(), getString(R.string.url_discord));
    }

    @OnClick(R.id.twitter_item)
    void onTwitterClick(View view) {
        viewModel.showSocial(getContext(), getString(R.string.url_twitter));
    }

    @OnClick(R.id.medium_item)
    void onMediumClick(View view) {
        viewModel.showSocial(getContext(), getString(R.string.url_medium));
    }

    @OnClick(R.id.github_item)
    void onGithubClick() {
        viewModel.showSocial(getContext(), getString(R.string.url_github));
    }

    @OnClick(R.id.play_market_item)
    void onRateUsClick(View view) {
//        viewModel.rateUs(getContext());
    }

    @OnClick(R.id.contact_us_item)
    void onContactUsClick(View view) {
        viewModel.showContactUs(getContext());
    }

    @OnClick(R.id.pin_item)
    void onPinClick(View view) {
        Boolean checked = pinSwitch.isChecked();
        pinSwitch.setChecked(!checked);
        showPin(false, checked, checked);
    }

    public static SettingsFragment create() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        supportFragmentInjector().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.reInitDefaultWallet();
    }

    protected int getLayout() {
        return R.layout.fragment_settings;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUiView();
    }

    @Override
    protected void onPrepareViewModel() {
        viewModel = ViewModelProviders.of(this, settingsViewModelFactory)
                .get(SettingsViewModel.class);
        viewModel.defaultWallet().observe(this, this::setWalletAddress);
        viewModel.pinIsOptioned().observe(this, this::setPinIsChecked);
        viewModel.prepare();
    }

    private void initUiView() {
        setVersionName(getVersion());
    }

    private void setWalletAddress(Wallet wallet) {
        walletAddess.setText(wallet.address);
    }

    private void setPinIsChecked(Boolean checked) {
        pinSwitch.setChecked(checked);
    }

    private void setVersionName(String versionName) {
        version.setText(versionName);
    }

    private String getVersion() {
        String version = "N/A";
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    @Override
    public void reinitTitles() {
        setTitles(getString(R.string.action_settings), "");
    }
}