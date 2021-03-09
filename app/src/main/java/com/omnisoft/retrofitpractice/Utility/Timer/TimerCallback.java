package com.omnisoft.retrofitpractice.Utility.Timer;

public interface TimerCallback {
    public void onTick(long millisUntilFinished);

    public void onFinish();
}
