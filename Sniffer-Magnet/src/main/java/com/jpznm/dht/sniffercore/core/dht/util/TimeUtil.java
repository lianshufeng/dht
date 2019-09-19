package com.jpznm.dht.sniffercore.core.dht.util;

public class TimeUtil {


    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String formatTimer(long time) {
        // 精确到秒
        time = time / 1000;
        final int MINUTE = 60;
        final int HOUR = 60 * MINUTE;
        final int DAY = HOUR * 24;
        String formats = "%s:%s:%s:%s";
        if (time <= MINUTE) {
            return String.format(formats, 0, 0, 0, time);
        } else if (time > MINUTE && time <= HOUR) {
            long m = time / MINUTE;
            long s = time - m * MINUTE;
            return String.format(formats, 0, 0, m, s);
        } else if (time > HOUR && time <= DAY) {
            long h = time / HOUR;
            time = time - h * HOUR;
            long m = time / MINUTE;
            long s = time - m * MINUTE;
            return String.format(formats, 0, h, m, s);
        } else {
            long d = time / DAY;
            time = time - d * DAY;
            long h = time / HOUR;
            time = time - h * HOUR;
            long m = time / MINUTE;
            long s = time - m * MINUTE;
            return String.format(formats, d, h, m, s);

        }
    }

}
