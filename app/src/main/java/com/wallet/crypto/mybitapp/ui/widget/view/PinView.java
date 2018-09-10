package com.wallet.crypto.mybitapp.ui.widget.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.ui.widget.OnPinResultListener;
import com.wallet.crypto.mybitapp.util.CryptoUtil;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PinView extends FrameLayout {
    private static final int PIN_LENGTH = 6;
    private static final int DELAY_CLEAN_INDICATORS = 75;

    private final List<LinearLayout> indicators = new ArrayList<>();
    private final List<LinearLayout> keys = new ArrayList<>();
    private OnPinResultListener resultListener;
    private String enteredPin = "";
    private String confirmedPin = "";
    private Boolean isJustCheck = false;
    private Animation mismachedPinAnimation;

    @BindView(R.id.ok_aciton)
    Button okAction;

    @BindView(R.id.pin_content)
    LinearLayout pinContent;

    @BindView(R.id.ly_indicators)
    ConstraintLayout lyIndicators;

    @BindView(R.id.ly_pin_code_one)
    LinearLayout lyPinCodeOne;

    @BindView(R.id.ly_pin_code_two)
    LinearLayout lyPinCodeTwo;

    @BindView(R.id.ly_pin_code_three)
    LinearLayout lyPinCodeThree;

    @BindView(R.id.ly_pin_code_four)
    LinearLayout lyPinCodeFour;

    @BindView(R.id.ly_pin_code_five)
    LinearLayout lyPinCodeFive;

    @BindView(R.id.ly_pin_code_six)
    LinearLayout lyPinCodeSix;

    @BindView(R.id.pin_title)
    TextView enterTextView;

    @BindView(R.id.ly_keyboard)
    ConstraintLayout lyKeyboard;

    @BindView(R.id.ly_one)
    LinearLayout lyOne;

    @BindView(R.id.ly_two)
    LinearLayout lyTwo;

    @BindView(R.id.ly_three)
    LinearLayout lyThree;

    @BindView(R.id.ly_four)
    LinearLayout lyFour;

    @BindView(R.id.ly_five)
    LinearLayout lyFive;

    @BindView(R.id.ly_six)
    LinearLayout lySix;

    @BindView(R.id.ly_seven)
    LinearLayout lySeven;

    @BindView(R.id.ly_eight)
    LinearLayout lyEight;

    @BindView(R.id.ly_nine)
    LinearLayout lyNine;

    @BindView(R.id.ly_zero)
    LinearLayout lyZero;

    @BindView(R.id.ly_backspace)
    LinearLayout lyBackspace;

    @OnClick(R.id.ly_one)
    public void onLyOneClick(View view) {
        pinKeyPress(R.string.pin_key_one);
    }

    @OnClick(R.id.ly_two)
    public void onLyTwoClick(View view) {
        pinKeyPress(R.string.pin_key_two);
    }

    @OnClick(R.id.ly_three)
    public void onLyThreeClick(View view) {
        pinKeyPress(R.string.pin_key_three);
    }

    @OnClick(R.id.ly_four)
    public void onLyFourClick(View view) {
        pinKeyPress(R.string.pin_key_four);
    }

    @OnClick(R.id.ly_five)
    public void onLyFiveClick(View view) {
        pinKeyPress(R.string.pin_key_five);
    }

    @OnClick(R.id.ly_six)
    public void onLySixClick(View view) {
        pinKeyPress(R.string.pin_key_six);
    }

    @OnClick(R.id.ly_seven)
    public void onLySevenClick(View view) {
        pinKeyPress(R.string.pin_key_seven);
    }

    @OnClick(R.id.ly_eight)
    public void onLyEightClick(View view) {
        pinKeyPress(R.string.pin_key_eight);
    }

    @OnClick(R.id.ly_nine)
    public void onLyNineClick(View view) {
        pinKeyPress(R.string.pin_key_nine);
    }

    @OnClick(R.id.ly_zero)
    public void onLyZeroClick(View view) {
        pinKeyPress(R.string.pin_key_zero);
    }

    @OnClick(R.id.ly_backspace)
    public void onLyBackspaceClick(View view) {
        pinKeyBackspacePress();
    }

    @OnClick(R.id.ok_aciton)
    public void onRemovingPinDialogOkClick(View view) {
        if (resultListener != null) {
            resultListener.onRemovingPinCompleteOk();
        }
    }

    public PinView(Context context) {
        super(context);
        initView();
    }

    public PinView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PinView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setResultListener(OnPinResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void setConfirmedPin(String confirmedPin) {
        this.confirmedPin = confirmedPin;
        this.isJustCheck = true;
    }

    public void showRemovingPinComplete() {
        enterTextView.setText(R.string.pin_removed);
        pinContent.setVisibility(View.GONE);
        okAction.setVisibility(View.VISIBLE);
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.layout_pin, null);
        addView(view);
        ButterKnife.bind(this, view);
        initIndicators();
        initKeys();
        initUncompleteAnimation();
    }

    private void complete() {
        if (resultListener != null) {
            resultListener.onComplete(enteredPin);
        }
    }

    private void unComplete() {
        lyIndicators.startAnimation(mismachedPinAnimation);
    }

    private void cleanInputs() {
        enteredPin = "";
        if (!isJustCheck) {
            confirmedPin = "";
        }
    }

    private void unCompleteListener() {
        if (resultListener != null) {
            resultListener.onUnComplete();
        }
    }

    private void tryConfirmPasscode() {
        enteredPin = CryptoUtil.convertStringToMd5String(enteredPin);

        if (confirmedPin.length() > 0) {
            checkConfirmedPasscode();
        } else {
            initFirstPinComplete();
        }
    }

    private void checkConfirmedPasscode() {
        if (enteredPin.equals(confirmedPin)) {
            complete();
        } else {
            unComplete();
        }
    }

    private void initFirstPinComplete() {
        enterTextView.setText(getContext().getString(R.string.confirm_passcode));
        resetAllIndicatorsToDefaultColor();
        confirmedPin = enteredPin;
        enteredPin = "";
    }

    private void resetAllIndicatorsToDefaultColor() {
        Observable.timer(DELAY_CLEAN_INDICATORS, TimeUnit.MILLISECONDS)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(o -> {
                    for (LinearLayout indicator : indicators) {
                        changeColorToIndicator(R.color.pin_non_selected_indicator, indicator);
                    }

                    return new Object();
                })
                .subscribe();
    }

    private void pinKeyPress(int pinKeyId) {
        changeColorToLastIndicator(R.color.colorPrimary);
        addSymbolToPin(getContext().getString(pinKeyId));
        checkComplete();
    }

    private void checkComplete() {
        if (enteredPin.length() >= PIN_LENGTH) {
            tryConfirmPasscode();
        }
    }

    private void addSymbolToPin(String symbol) {
        enteredPin += symbol;
    }

    private void pinKeyBackspacePress() {
        tryRemoveLastCharacterFromEnteredPin();
    }

    private void tryRemoveLastCharacterFromEnteredPin() {
        if (enteredPin != null && enteredPin.length() > 0) {
            enteredPin = enteredPin.substring(0, enteredPin.length() - 1);
            changeColorToLastIndicator(R.color.pin_non_selected_indicator);
        }
    }

    private void changeColorToLastIndicator(int colorId) {
        try {
            LinearLayout indicator = indicators.get(enteredPin.length());
            changeColorToIndicator(colorId, indicator);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void changeColorToIndicator(int colorId, LinearLayout indicator) {
        try {
            Drawable indicatorDrawable = indicator.getBackground();
            indicatorDrawable.setColorFilter(ContextCompat.getColor(getContext(), colorId), PorterDuff.Mode.SRC_ATOP);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void initIndicators() {
        indicators.add(lyPinCodeOne);
        indicators.add(lyPinCodeTwo);
        indicators.add(lyPinCodeThree);
        indicators.add(lyPinCodeFour);
        indicators.add(lyPinCodeFive);
        indicators.add(lyPinCodeSix);
    }

    private void initKeys() {
        keys.add(lyOne);
        keys.add(lyTwo);
        keys.add(lyThree);
        keys.add(lyFour);
        keys.add(lyFive);
        keys.add(lySix);
        keys.add(lySeven);
        keys.add(lyEight);
        keys.add(lyNine);
        keys.add(lyZero);
        keys.add(lyBackspace);
    }

    private void initUncompleteAnimation() {
        mismachedPinAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.error_pin_bounce);
        mismachedPinAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                blockKeys();
                enterTextView.setText(getContext().getString(R.string.mismached_passcode));
                cleanInputs();
                unCompleteListener();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                resetAllIndicatorsToDefaultColor();
                unBlockKeys();
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    private void blockKeys() {
        for (LinearLayout key : keys) {
            key.setClickable(false);
        }
    }

    private void unBlockKeys() {
        for (LinearLayout key : keys) {
            key.setClickable(true);
        }
    }
}