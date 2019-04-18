package com.space.videospeed;

import android.net.TrafficStats;

/**
 * Created by licht on 2019/1/10.
 */

public class NetSpeed {
    private static final String TAG = NetSpeed.class.getName();
    private static long lastTotalBytes = 0;
    private static long lastTime = 0;

    public static String getNetSpeed(int uid) {
        long nowTotalBytes = getTotalBytes(uid);
        long nowTime = System.currentTimeMillis();
        long speed = (nowTotalBytes - lastTotalBytes) * 1000 / (nowTime - lastTime);
        lastTotalBytes = nowTotalBytes;
        lastTime = nowTime;
        String speedStr = null;
        if (speed > 1024) {
            speedStr = String.valueOf("当前速率：" + Math.floor(speed / 1024.0) + "Mb/s");
        } else {
            speedStr = String.valueOf("当前速率：" + speed + "Kb/s");
        }
        return speedStr;
    }

    public static long getTotalBytes(int uid) {
        return TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : TrafficStats.getTotalRxBytes() / 1024;
    }
}
