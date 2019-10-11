package org.chromium.chrome.browser.rate;

import android.content.SharedPreferences;

// import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import org.chromium.base.ContextUtils;

public class RateUtils {

    private static RateUtils sInstance;

    private final SharedPreferences mSharedPreferences;

    private static final String PREF_RATE = "rate";
    private static final String PREF_NEXT_RATE_DATE = "next_rate_date";
    private static final String PREF_RATE_COUNT = "rate_count";
    private static final String PREF_APP_OPEN_COUNT = "app_open_count";

    private RateUtils() {
        mSharedPreferences = ContextUtils.getAppSharedPreferences();
    }

    /**
     * Returns the singleton instance of RateUtils, creating it if needed.
     */
    public static RateUtils getInstance() {
        if (sInstance == null) {
            sInstance = new RateUtils();
        }
        return sInstance;
    }

    /**
     * Returns the user preference for whether the rate is enabled.
     */
    public boolean getPrefRateEnabled() {
        return mSharedPreferences.getBoolean(PREF_RATE, true);
    }

    /**
     * Sets the user preference for whether the rate is enabled.
     */
    public void setPrefRateEnabled(boolean enabled) {
        SharedPreferences.Editor sharedPreferencesEditor = mSharedPreferences.edit();
        sharedPreferencesEditor.putBoolean(PREF_RATE, enabled);
        sharedPreferencesEditor.apply();
    }

    public long getPrefNextRateDate() {
        return mSharedPreferences.getLong(PREF_NEXT_RATE_DATE, 0);
    }

    public void setPrefNextRateDate(long nextDate) {
        SharedPreferences.Editor sharedPreferencesEditor = mSharedPreferences.edit();
        sharedPreferencesEditor.putLong(PREF_NEXT_RATE_DATE, nextDate);
        sharedPreferencesEditor.apply();
    }

    public int getPrefRateCount() {
        return mSharedPreferences.getInt(PREF_RATE_COUNT, -1);
    }

    public void setPrefRateCount() {
        SharedPreferences.Editor sharedPreferencesEditor = mSharedPreferences.edit();
        sharedPreferencesEditor.putInt(PREF_RATE_COUNT, getPrefRateCount() + 1);
        sharedPreferencesEditor.apply();
    }

    public int getPrefAppOpenCount() {
        return mSharedPreferences.getInt(PREF_APP_OPEN_COUNT, 0);
    }

    public void setPrefAppOpenCount() {
        SharedPreferences.Editor sharedPreferencesEditor = mSharedPreferences.edit();
        sharedPreferencesEditor.putInt(PREF_APP_OPEN_COUNT, getPrefAppOpenCount() + 1);
        sharedPreferencesEditor.apply();
    }

    public boolean shouldShowRate() {

        RateDays rateDays = RateDays.valueOfIndex(getPrefRateCount());

        // LocalTime morning10 = LocalTime.parse("10:00:00");
        // LocalTime afternoon1 = LocalTime.parse("13:00:00");

        // LocalTime timeNow = LocalTime.now();

        // return System.currentTimeMillis() > getPrefNextRateDate() &&
        //         getPrefAppOpenCount() >= rateDays.count &&
        //         timeNow.isAfter(morning10) && timeNow.isBefore(afternoon1);

        return getPrefAppOpenCount() >= rateDays.count;
    }

    public void setNextRateDateAndCount() {
        setPrefRateCount();

        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date());
        calender.add(Calendar.DATE, RateDays.valueOfIndex(getPrefRateCount()).count);

        setPrefNextRateDate(calender.getTimeInMillis());
    }
}
