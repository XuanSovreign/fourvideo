package com.space.videospeed;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HWVideoActivity extends Activity implements View.OnClickListener {

    private VideoView mVideo;
    private VideoView mVideo2;
    private VideoView mVideo3;
    private VideoView mVideo4;
    private ImageView mIvOne;
    private ImageView mIvTwo;
    private ImageView mIvThree;
    private ImageView mIvFour;
    private TextView mTvLengthOne;
    private TextView mTvLengthTwo;
    private TextView mTvLengthThree;
    private TextView mTvLengthFour;
    private TextView mTvSpeedOne;
    private TextView mTvSpeedTwo;
    private TextView mTvSpeedThree;
    private TextView mTvSpeedFour;
    private ImageView mIvShrinkOne;
    private ImageView mIvShrinkTwo;
    private ImageView mIvShrinkThree;
    private ImageView mIvShrinkFour;
    private List<ImageView> mImageShrinkViews;
    private List<ImageView> mImageViews;
    private List<VideoView> mVideoViews;
    private List<TextView> mTextLengthViews;
    private List<TextView> mTextSpeedViews;
    private int index;
    private int mWidth;
    private int mHeight;
    private RelativeLayout mRlBig;
    private VideoView mVideoBig;
    private ImageView mIvShrink;
    private long startTime;
    private int seekTime;
    private List<RelativeLayout> mLayouts;
    private Retrofit mRetrofit;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    File video = (File) msg.obj;
                    Log.e("path", "handleMessage: " + video.getAbsolutePath());
                    mHandler.removeCallbacks(task);
                    mVideoViews.get(index).setVideoPath(video.getAbsolutePath());
                    mVideoViews.get(index).setVisibility(View.VISIBLE);
                    mTextLengthViews.get(index).setVisibility(View.INVISIBLE);
                    mTextSpeedViews.get(index).setVisibility(View.INVISIBLE);
                    mImageViews.get(index).setVisibility(View.GONE);
                    startTime = System.currentTimeMillis();
                    mVideoViews.get(index).requestFocus();
                    mVideoViews.get(index).start();
                    startTime = System.currentTimeMillis();
                    seekTime = 0;
                    break;
                case 2:
                    Toast.makeText(HWVideoActivity.this, "文件出错了", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        RelativeLayout rlOne = findViewById(R.id.rl_one);
        RelativeLayout rlTwo = findViewById(R.id.rl_two);
        RelativeLayout rlThree = findViewById(R.id.rl_three);
        RelativeLayout rlFour = findViewById(R.id.rl_four);
        mLayouts = new ArrayList<>();
        mLayouts.add(rlOne);
        mLayouts.add(rlTwo);
        mLayouts.add(rlThree);
        mLayouts.add(rlFour);

        mVideo = findViewById(R.id.video);
        mVideo2 = findViewById(R.id.video2);
        mVideo3 = findViewById(R.id.video3);
        mVideo4 = findViewById(R.id.video4);
        mVideoViews = new ArrayList<>();
        mVideoViews.add(mVideo);
        mVideoViews.add(mVideo2);
        mVideoViews.add(mVideo3);
        mVideoViews.add(mVideo4);

        mIvOne = findViewById(R.id.iv_one);
        mIvTwo = findViewById(R.id.iv_two);
        mIvThree = findViewById(R.id.iv_three);
        mIvFour = findViewById(R.id.iv_four);
        mImageViews = new ArrayList<>();
        mImageViews.add(mIvOne);
        mImageViews.add(mIvTwo);
        mImageViews.add(mIvThree);
        mImageViews.add(mIvFour);

        mIvShrinkOne = findViewById(R.id.iv_shrink_one);
        mIvShrinkTwo = findViewById(R.id.iv_shrink_two);
        mIvShrinkThree = findViewById(R.id.iv_shrink_three);
        mIvShrinkFour = findViewById(R.id.iv_shrink_four);
        mImageShrinkViews = new ArrayList<>();
        mImageShrinkViews.add(mIvShrinkOne);
        mImageShrinkViews.add(mIvShrinkTwo);
        mImageShrinkViews.add(mIvShrinkThree);
        mImageShrinkViews.add(mIvShrinkFour);

        mTvLengthOne = findViewById(R.id.tv_length_one);
        mTvLengthTwo = findViewById(R.id.tv_length_two);
        mTvLengthThree = findViewById(R.id.tv_length_three);
        mTvLengthFour = findViewById(R.id.tv_length_four);
        mTextLengthViews = new ArrayList<>();
        mTextLengthViews.add(mTvLengthOne);
        mTextLengthViews.add(mTvLengthTwo);
        mTextLengthViews.add(mTvLengthThree);
        mTextLengthViews.add(mTvLengthFour);

        mTvSpeedOne = findViewById(R.id.tv_speed_one);
        mTvSpeedTwo = findViewById(R.id.tv_speed_two);
        mTvSpeedThree = findViewById(R.id.tv_speed_thee);
        mTvSpeedFour = findViewById(R.id.tv_speed_four);
        mTextSpeedViews = new ArrayList<>();
        mTextSpeedViews.add(mTvSpeedOne);
        mTextSpeedViews.add(mTvSpeedTwo);
        mTextSpeedViews.add(mTvSpeedThree);
        mTextSpeedViews.add(mTvSpeedFour);

        mRlBig = findViewById(R.id.rl_big);
        mVideoBig = findViewById(R.id.video_big);
        mIvShrink = findViewById(R.id.iv_shrink);
    }

    private String[] videoPaths = {"1.危化品监管-0201.mp4", "2.快速建模-0119.mp4",
            "3.可视域分析-0021.mp4", "4.Linux三维-0016.mp4"};

    private void initData() {
        Intent intent = getIntent();
        String http_url = intent.getStringExtra(Constants.HTTP_URL);
        mVideo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWidth = mVideo.getWidth();
                mHeight = mVideo.getHeight();
                Log.e("getViewTreeObserver", "onGlobalLayout: " + mWidth + "=========" + mHeight);
                mVideo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        checkPermissions();
        mRetrofit = new Retrofit.Builder().baseUrl("http://" + http_url + ":8080/video/").build();
    }

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,};

    private void checkPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                int permission = -1;
                for (int i = 0; i < permissions.length; i++) {
                    permission = ActivityCompat.checkSelfPermission(this, permissions[i]);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        break;
                    }
                }

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, 0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (startTime > 0) {
            seekTime = (int) (System.currentTimeMillis() - startTime);
//            Log.e("ddddddd", "onStart: "+seekTime );
        }
    }

    private void initListener() {
        for (int i = 0; i < mVideoViews.size(); i++) {
            mVideoViews.get(i).setOnClickListener(this);
            mImageViews.get(i).setOnClickListener(this);
            mImageShrinkViews.get(i).setOnClickListener(this);
        }

        MediaPlayer.OnPreparedListener preListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.seekTo(seekTime);
                mp.start();
            }
        };

        MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mImageViews.get(index).setImageResource(R.mipmap.video_play);
                mImageViews.get(index).setVisibility(View.VISIBLE);
                mVideoViews.get(index).setVisibility(View.GONE);
            }
        };

        for (int i = 0; i < mVideoViews.size(); i++) {
            mVideoViews.get(i).setOnPreparedListener(preListener);
            mVideoViews.get(i).setOnCompletionListener(listener);
        }


        /**
         * 暂时不用
         */
        mIvShrink.setOnClickListener(this);
        mVideoBig.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mRlBig.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video:
                zoomInVideo(true);
                break;
            case R.id.video2:
                zoomInVideo(true);
                break;
            case R.id.video3:
                zoomInVideo(true);
                break;
            case R.id.video4:
                zoomInVideo(true);
                break;
            case R.id.iv_one:
                downloadVideo(0);
                break;
            case R.id.iv_two:
                downloadVideo(1);
                break;
            case R.id.iv_three:
                downloadVideo(2);
                break;
            case R.id.iv_four:
                downloadVideo(3);
                break;
            case R.id.iv_shrink:
                zoomInVideo(false);
                break;
            case R.id.iv_shrink_one:
                zoomInVideo(false);
                break;
            case R.id.iv_shrink_two:
                zoomInVideo(false);
                break;
            case R.id.iv_shrink_three:
                zoomInVideo(false);
                break;
            case R.id.iv_shrink_four:
                zoomInVideo(false);
                break;

        }

    }

    private void downloadVideo(final int i) {
        index = i;
        for (int j = 0; j < mImageViews.size(); j++) {
            final int finalJ = j;
            if (i == j) {
                DataService service = mRetrofit.create(DataService.class);
                Call<ResponseBody> call = service.downloadFile(videoPaths[i]);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                        final File file = FileUtil.createFile(videoPaths[i]);
                        mImageViews.get(finalJ).setImageResource(R.mipmap.video_download);
                        mTextLengthViews.get(finalJ).setVisibility(View.VISIBLE);
                        mTextSpeedViews.get(finalJ).setVisibility(View.VISIBLE);
                        long total = response.body().contentLength() / 1024 / 1024;
                        String str = null;
                        if (total > 1024) {
                            str = String.valueOf("文件大小：" + Math.floor(total / 1024.0) + "GB");
                        } else {
                            str = String.valueOf("文件大小：" + total + "MB");
                        }
                        mTextLengthViews.get(finalJ).setText(str);
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                FileUtil.writeFile(response, file, new HttpCallBack() {
                                    @Override
                                    public void onError() {
                                        mHandler.sendEmptyMessage(2);
                                    }

                                    @Override
                                    public void onLoading(long currentLength, long totalLength) {
//                                        Message message = Message.obtain();
//                                        message.what=0;
//                                        message.obj=totalLength;
//                                        mHandler.sendMessage(message);
                                    }

                                    @Override
                                    public void onFinished() {
                                        Message message = Message.obtain();
                                        message.what = 1;
                                        message.obj = file;
                                        mHandler.sendMessage(message);
                                    }
                                });
                            }
                        }.start();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("aaaaaaaaa", "onFailure: " + t.getMessage());
                    }
                });

                mHandler.postDelayed(task, 0);

            } else {
                mImageViews.get(j).setImageResource(R.mipmap.video_play);
                mImageViews.get(j).setVisibility(View.VISIBLE);
                mVideoViews.get(j).stopPlayback();
                mVideoViews.get(j).setVisibility(View.INVISIBLE);
                mTextLengthViews.get(j).setVisibility(View.INVISIBLE);
                mTextSpeedViews.get(j).setVisibility(View.INVISIBLE);
            }

        }
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            mTextSpeedViews.get(index).setText(NetSpeed.getNetSpeed(getApplicationInfo().uid));
            mHandler.postDelayed(this, 1000);
        }
    };

    private void zoomInVideo(boolean b) {
        for (int i = 0; i < mLayouts.size(); i++) {
            if (i != index) {
                if (b) {
                    mLayouts.get(i).setVisibility(View.GONE);
                } else {
                    mLayouts.get(i).setVisibility(View.VISIBLE);
                }

            }
        }
        ViewGroup.LayoutParams params = mLayouts.get(index).getLayoutParams();
        ViewGroup.LayoutParams videoParams = mVideoViews.get(index).getLayoutParams();
        if (b) {
            params.width = getWindowManager().getDefaultDisplay().getHeight();
            params.height = getWindowManager().getDefaultDisplay().getWidth();
            videoParams.width = params.width;
            videoParams.height = params.height;
            mImageShrinkViews.get(index).setVisibility(View.VISIBLE);
            mVideoViews.get(index).setEnabled(false);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            params.width = mWidth;
            params.height = mHeight;
            videoParams.width = params.width;
            videoParams.height = params.height;
            mVideoViews.get(index).setEnabled(true);
            mImageShrinkViews.get(index).setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mLayouts.get(index).setLayoutParams(params);
        mVideoViews.get(index).setLayoutParams(videoParams);
    }


}
