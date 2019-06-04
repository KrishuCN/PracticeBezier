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

import chen.vike.c680.main.MyApplication;

/**
 * Created by lht on 2017/3/16.
 */

public class CustomDigitalClockEnd extends DigitalClock {

    Calendar mCalendar;
    private final static String m12 = "h:mm aa";
    private final static String m24 = "k:mm";

    private Runnable mTicker;
    private Handler mHandler;
    private long endTime;
    private ClockListener mClockListener;
    private boolean mTickerStopped1 = false;
    private boolean s = false;
    private int mState = 0;
    private FormatChangeObserver mFormatChangeObserver;

    public boolean ismTickerStopped() {
        return s;
    }

    public CustomDigitalClockEnd(Context context) {
        super(context);
        initClock(context);
    }

    public CustomDigitalClockEnd(Context context, AttributeSet attrs) {
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
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped1)
                    return;
                long currentTime = System.currentTimeMillis();
                if (currentTime / 1000 == endTime / 1000 - 5 * 60) {
                    try {
                        mClockListener.remainFiveMinutes();
                    } catch (Exception e) {

                    }

                }
                long distanceTime = endTime - currentTime;
                distanceTime /= 1000;
                if (mState == 1) {
                    setText(Html.fromHtml("已圆满结束"));
                    onDetachedFromWindow();

                } else {
                    if (distanceTime == 0) {
                        setText("已到期");
                        s = true;
                        onDetachedFromWindow();
                    } else if (distanceTime < 0) {
                        s = true;
                        setText("已到期");
                        onDetachedFromWindow();
                    } else {
                        setText(Html.fromHtml(dealTime(distanceTime)));
                    }
                    invalidate();
                    long now = SystemClock.uptimeMillis();
                    long next = now + (1000 - now % 1000);
                    mHandler.postAtTime(mTicker, next);
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
            returnString.append("剩余：").append("<font color='#df231b'>" + Integer.valueOf(dayStr) + "</font>").append("天")
                    .append("<font color='#df231b'>" + Integer.valueOf(hoursStr) + "</font>").append("时")
                    .append("<font color='#df231b'>" + Integer.valueOf(minutesStr) + "</font>").append("分")
                    .append("<font color='#df231b'>" + Integer.valueOf(secondStr) + "</font>").append("秒");

        } else {
            if (hours > 0) {
                returnString.append("剩余：").append("<font color='#df231b'>" + Integer.valueOf(hoursStr) + "</font>").append("时")
                        .append("<font color='#df231b'>" + Integer.valueOf(minutesStr) + "</font>").append("分")
                        .append("<font color='#df231b'>" + Integer.valueOf(secondStr) + "</font>").append("秒");

            } else {
                if (minutes > 0) {
                    returnString.append("剩余：").append("<font color='#df231b'>" + Integer.valueOf(minutesStr) + "</font>").append("分")
                            .append("<font color='#df231b'>" + Integer.valueOf(secondStr) + "</font>").append("秒")
                    ;

                } else {
                    if (second > 0) {
                        returnString.append("剩余：").append("<font color='#df231b'>" + Integer.valueOf(secondStr) + "</font>").append("秒");

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
//        mTickerStopped1 = true;
//        if (mTicker!=null){
//            mTicker=null;
//        }

        mHandler.removeCallbacksAndMessages(null);

//        if (context!=null){
//            context=null;
//        }
        s = true;
    }


    /**
     * Release all object
     */
    public void destory() {

        if (observerHandler != null) {
            observerHandler.removeCallbacksAndMessages(null);
            observerHandler = null;
        }

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

        if (mTicker != null) {
            mTicker = null;
        }

        if (mFormatChangeObserver!=null){
            MyApplication.INSTANCE.getApplicationContext().getContentResolver().unregisterContentObserver(mFormatChangeObserver);
            mFormatChangeObserver = null;
        }

        mTickerStopped1 = true;
        s = true;
    }

    /**
     * Clock end time from now on.
     *
     * @param endTime
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(long endTime, int mState) {
        this.endTime = endTime;
        this.mState = mState;
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

    public void setClockListener(ClockListener clockListener) {
        this.mClockListener = clockListener;
    }

    public interface ClockListener {
        void timeEnd();

        void remainFiveMinutes();
    }

}
