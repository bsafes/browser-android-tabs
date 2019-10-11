package org.chromium.chrome.browser.rate;

import java.util.HashMap;
import java.util.Map;

public enum RateDays {
    DAYS_40(0, 4),
    DAYS_90(1, 6),
    DAYS_365(2, 8);

    private static final Map<Integer, RateDays> BY_INDEX = new HashMap<>();

    static {
        for (RateDays rateDay : values()) {
            BY_INDEX.put(rateDay.index, rateDay);
        }
    }

    public final int index;
    public final int count;

    RateDays(int index, int count) {
        this.index = index;
        this.count = count;
    }

    public static RateDays valueOfIndex(int index) {
        return BY_INDEX.get(index);
    }
}
