package com.omnisoft.retrofitpractice.Utility.Timer;

import android.os.CountDownTimer;

public class Timer extends CountDownTimer {

    TimerCallback callback;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public Timer(long millisInFuture, long countDownInterval, TimerCallback callback) {
        super(millisInFuture, countDownInterval);
        this.callback = callback;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        callback.onTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        callback.onFinish();
    }

}
