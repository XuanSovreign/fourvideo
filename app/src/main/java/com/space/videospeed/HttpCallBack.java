package com.space.videospeed;

/**
 * Created by licht on 2019/1/10.
 */

interface HttpCallBack {
    void onError();
    void onLoading(long currentLength,long totalLength);
    void onFinished();
}
