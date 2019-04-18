package com.space.videospeed;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Random;

/**
 * Created by licht on 2018/12/21.
 */

public class VideoAdapter extends BaseAdapter {
    private CustomVideoActivity mContext;
    private String[] videoPaths;
    private int mWidth;
    private int mHeight;
    private int count;
    private Handler mHandler=new Handler();

    public VideoAdapter(CustomVideoActivity context, String[] videoPaths) {
        mContext=context;
        this.videoPaths=videoPaths;
    }

    @Override
    public int getCount() {
        return videoPaths.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null) {
            convertView=View.inflate(mContext,R.layout.item_video,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder= (ViewHolder) convertView.getTag();
        }
        final ViewHolder finalHolder = holder;
        holder.video.setVideoPath(videoPaths[position]);
        holder.rl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWidth = finalHolder.rl.getWidth();
                mHeight = finalHolder.rl.getHeight();
            }
        });

        holder.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.ivPlay.setImageResource(R.mipmap.video_download);
                finalHolder.tvLength.setText("文件大小：" + new Random(200).nextInt(500));
                finalHolder.ivPlay.setVisibility(View.VISIBLE);
                finalHolder.ll.setVisibility(View.VISIBLE);
                count=0;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finalHolder.tvSpeed.setText("当前速率：" + new Random(50).nextInt(100) + "M/s");
                        mHandler.postDelayed(this, 100);
                        if (count >= 10) {
                            mHandler.removeCallbacks(this);
                            finalHolder.video.setVisibility(View.VISIBLE);
                            finalHolder.ll.setVisibility(View.GONE);
                            finalHolder.ivPlay.setVisibility(View.GONE);
                            finalHolder.video.start();
                        }
                        count++;
                    }
                }, 0);
            }
        });

        holder.ivScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = finalHolder.rl.getLayoutParams();
                ViewGroup.LayoutParams params2 = finalHolder.video.getLayoutParams();
                params.width=mContext.getWindowManager().getDefaultDisplay().getWidth();
                params.height=mContext.getWindowManager().getDefaultDisplay().getHeight();
                params2.width=params.width;
                params2.height=params.height;
                finalHolder.rl.setLayoutParams(params);
                finalHolder.video.setLayoutParams(params2);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public RelativeLayout rl;
        public ImageView ivPlay,ivScale;
        public LinearLayout ll;
        public TextView tvLength,tvSpeed;
        public VideoView video;

        public ViewHolder(View view) {
            initView(view);
        }

        private void initView(View view) {
            rl=view.findViewById(R.id.rl_item);
            ivPlay=view.findViewById(R.id.iv_item);
            ivScale=view.findViewById(R.id.iv_scale);
            ll=view.findViewById(R.id.ll_item);
            tvLength=view.findViewById(R.id.tv_length_item);
            tvSpeed=view.findViewById(R.id.tv_speed_item);
            video=view.findViewById(R.id.video_item);
        }
    }

}
