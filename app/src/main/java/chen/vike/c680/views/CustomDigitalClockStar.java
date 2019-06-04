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
import android.util.AttributeSet;
import android.widget.DigitalClock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import chen.vike.c680.main.MyApplication;

/**
 * Custom digital clock
 *
 * @author veally@foxmail.com
 */
public class CustomDigitalClockStar extends DigitalClock {

    Calendar mCalendar;
    private final static String m12 = "h:mm aa";
    private final static String m24 = "k:mm";

    private Runnable mTicker;
    private Handler mHandler;
    private long endTime;
    private ClockListener mClockListener;
    private FormatChangeObserver mFormatChangeObserver;
    private boolean mTickerStopped = false;


    public CustomDigitalClockStar(Context context) {
        super(context);
        initClock(context);
    }

    public CustomDigitalClockStar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {

        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

        mFormatChangeObserver = new FormatChangeObserver();
        MyApplication.INSTANCE.getApplicationContext().getContentResolver().registerContentObserver(
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
                if (mTickerStopped)
                    return;
                long currentTime = System.currentTimeMillis();
                if (currentTime / 1000 == endTime / 1000 - 5 * 60) {
                    mClockListener.remainFiveMinutes();
                }
                long distanceTime = endTime + currentTime;
                distanceTime /= 1000;
                if (distanceTime == 0) {
                    setText("已到期");
                    onDetachedFromWindow();
                    if (mClockListener != null) {
                        mClockListener.timeEnd();
                    }
                } else if (distanceTime < 0) {
                    setText("");
                } else if (distanceTime / (24 * 60 * 60) > 30) {
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    long now = System.currentTimeMillis();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(new Date(new SimpleDateFormat(
                            "yyyy/MM/dd HH:mm:ss").format(new Date())).getTime() - (endTime + now));
                    setText(formatter.format(calendar.getTime()));
                } else {
                    setText(dealTime(distanceTime));
                }
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                mHandler.postAtTime(mTicker, next);
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
            returnString.append(dayStr).append("天前");


        } else {
            if (hours > 0) {
                returnString.append(Integer.valueOf(hoursStr)).append("小时前")
                ;

            } else {
                if (minutes > 0) {
                    returnString.append(Integer.valueOf(minutesStr)).append("分钟前");
                } else {
                    if (second > 0) {
                        returnString.append(Integer.valueOf(secondStr)).append("秒前");
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
        if (observerHandler != null) {
            observerHandler.removeCallbacksAndMessages(null);
            observerHandler = null;
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

        mTickerStopped = true;
    }


    public void destory() {

        if (observerHandler != null) {
            observerHandler.removeCallbacksAndMessages(null);
            observerHandler = null;
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

        if (mFormatChangeObserver!=null){
            MyApplication.INSTANCE.getApplicationContext().getContentResolver().unregisterContentObserver(mFormatChangeObserver);
            mFormatChangeObserver = null;
        }


        if (mTicker != null) {
            mTicker = null;
        }
        mTickerStopped = true;

    }
        /**
         * Clock end time from now on.
         *
         * @param endTime
         */
        public void setEndTime ( long endTime){
            this.endTime = endTime;
        }

        public long getEndTime () {
            return endTime;
        }

        /**
         * Pulls 12/24 mode from system settings
         */
        private boolean get24HourMode () {
            return android.text.format.DateFormat.is24HourFormat(getContext());
        }

        private void setFormat () {

            String mFormat;
            if (get24HourMode()) {
                mFormat = m24;
            } else {
                mFormat = m12;
            }
        }

        private Handler observerHandler = new Handler();
        private class FormatChangeObserver extends ContentObserver {
            public FormatChangeObserver() {
                super(observerHandler);
            }

            @Override
            public void onChange(boolean selfChange) {
                setFormat();
            }
        }

        public void setClockListener (ClockListener clockListener){
            this.mClockListener = clockListener;
        }

        public interface ClockListener {
            void timeEnd();

            void remainFiveMinutes();
        }

    }