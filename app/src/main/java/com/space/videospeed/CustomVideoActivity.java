package com.space.videospeed;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

/**
 * Created by licht on 2018/12/21.
 */

public class CustomVideoActivity extends Activity {
    private String[] videoPaths={"http://192.168.30.205:8080/video/1.危化品监管-0201.mp4","http://192.168.30.205:8080/video/2.快速建模-0119.mp4",
            "http://192.168.30.205:8080/video/3.可视域分析-0021.mp4","http://192.168.30.205:8080/video/4.Linux三维-0016.mp4"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);
        GridView gvVideo = findViewById(R.id.gv_video);
        VideoAdapter adapter = new VideoAdapter(this,videoPaths);
        gvVideo.setAdapter(adapter);
    }
}
