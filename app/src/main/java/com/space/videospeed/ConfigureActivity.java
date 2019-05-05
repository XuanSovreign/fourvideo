package com.space.videospeed;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by licht on 2019/1/14.
 */

public class ConfigureActivity extends Activity {

    private EditText mEtIp;
    private String ipAddress="";
    private SharedPreferences mSp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_configure);
        mEtIp = findViewById(R.id.et_ip);
        Button btnConnection = findViewById(R.id.btn_connection);
        mSp = getSharedPreferences("videoSpeed", MODE_PRIVATE);
        if (mSp.contains(Constants.HTTP_URL)) {
            ipAddress = mSp.getString(Constants.HTTP_URL, "");
        }
        if (!TextUtils.isEmpty(ipAddress)) {
            mEtIp.setText(ipAddress);
        }

        btnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = mEtIp.getText().toString().trim();
                if (!TextUtils.isEmpty(ip)) {
                    if (!ip.equals(ipAddress)) {
                        SharedPreferences.Editor edit = mSp.edit();
                        edit.putString(Constants.HTTP_URL, ip);
                        edit.apply();
                    }
                    gotoVideo(ip);
                    finish();
                } else {
                    Toast.makeText(ConfigureActivity.this,"请输入IP地址",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void gotoVideo(String ip) {
        Intent intent = new Intent(this, HWVideoActivity.class);
        intent.putExtra(Constants.HTTP_URL,ip);
        startActivity(intent);
    }
}
