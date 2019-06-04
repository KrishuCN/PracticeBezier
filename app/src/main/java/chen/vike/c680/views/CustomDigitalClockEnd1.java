/*
 * Copyright (C) 2012 The * Project
 * All right reserved.
 * Version 1.00 2012-2-11
 * Author veally@foxmail.com
 */
package chen.vike.c680.views;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.DigitalClock;

import java.util.Calendar;

/**
 * Custom digital clock
 *
 * @author veally@foxmail.com
 */
public class CustomDigitalClockEnd1 extends DigitalClock {

    Calendar mCalendar;
    private final static String m12 = "h:mm aa";
    private final static String m24 = "k:mm";

    private Runnable mTicker;
    private Handler mHandler;
    private long endTime;
    private ClockListener mClockListener;

    private boolean mTickerStopped = false;


    private int mState;

    public CustomDigitalClockEnd1(Context context) {
        super(context);

        initClock(context);
    }

    public CustomDigitalClockEnd1(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {

        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

        FormatChangeObserver mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mFormatChangeObserver);

        setFormat();
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();
        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
            public void run() {
                try {
                    if (mTickerStopped)
                        return;
                    long currentTime = System.currentTimeMillis();
                    if (currentTime / 1000 == endTime / 1000 - 5 * 60) {
                        mClockListener.remainFiveMinutes();
                    }
                    long distanceTime = endTime - currentTime;
                    distanceTime /= 1000;
                    if (mState == 1) {
                        setText(Html.fromHtml("<font color='#1f9b0f'>已圆满结束</fong>"));
                        onDetachedFromWindow();
                    } else {
                        if (distanceTime == 0) {
                            setText("已到期");
                            onDetachedFromWindow();
                        } else if (distanceTime < 0) {
                            setText("已到期");
                            onDetachedFromWindow();
                        } else {
                            setText(dealTime(distanceTime));

                        }
                        invalidate();
                        long now = SystemClock.uptimeMillis();
                        long next = now + (1000 - now % 1000);
                        mHandler.postAtTime(mTicker, next);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        mTicker.run();
    }

    /**
     * deal time string
     *
     * @param time
     * @return
     */
    public static String dealTime(long time) {
        StringBuffer returnString = new StringBuffer();
        long day = time / (24 * 60 * 60);
        long hours = (time % (24 * 60 * 60)) / (60 * 60);
        long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
        long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
        String dayStr = String.valueOf(day);
        String hoursStr = timeStrFormat(String.valueOf(hours));
        String minutesStr = timeStrFormat(String.valueOf(minutes));
        String secondStr = timeStrFormat(String.valueOf(second));
        if (day > 0) {
            returnString.append("剩余").append(Integer.valueOf(dayStr)).append("天").append(Integer.valueOf(secondStr)).append("秒");
        } else {
            if (hours > 0) {
                returnString.append("剩余").append(Integer.valueOf(hoursStr)).append("时");
            } else {
                if (minutes > 0) {
                    returnString.append("剩余").append(Integer.valueOf(minutesStr)).append("分")
                    ;

                } else {
                    if (second > 0) {
                        returnString.append(Integer.valueOf(secondStr)).append("秒");

                    }

                }

            }

        }
        return returnString.toString();
    }

    /**
     * format time
     *
     * @param timeStr
     * @return
     */
    private static String timeStrFormat(String timeStr) {
        switch (timeStr.length()) {
            case 1:
                timeStr = "0" + timeStr;
                break;
        }
        return timeStr;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    /**
     * Clock end time from now on.
     *
     * @param endTime
     */
    public void setEndTime(long endTime, int mState) {
        this.endTime = endTime;
        this.mState = mState;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return android.text.format.DateFormat.is24HourFormat(getContext());
    }

    private void setFormat() {
        String mFormat;
        if (get24HourMode()) {
            mFormat = m24;
        } else {
            mFormat = m12;
        }
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }

    public void setClockListener(ClockListener clockListener) {
        this.mClockListener = clockListener;
    }

    public interface ClockListener {
        void timeEnd();

        void remainFiveMinutes();
    }

}