package com.space.videospeed;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by licht on 2019/1/10.
 */

public class FileUtil {


    public static File createFile(String fileName) {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            throw new RuntimeException("内存卡没有挂在");
        }
        File directory=new File(Environment.getExternalStorageDirectory(),"videoSpeed");
//        Log.e("FileUtil", "createFile: " +directory.getAbsolutePath());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, fileName);
        if (file.exists()) {
            file.delete();
        }

        return file;
    }

    public static void writeFile(Response<ResponseBody> response, File file,HttpCallBack httpCallBack) {
        if (file == null) {
            throw new RuntimeException("没有文件");
        }
        if (httpCallBack == null) {
            throw new RuntimeException("空指针");
        }
        long totalLength = response.body().contentLength();

        try {
            InputStream inputStream = response.body().byteStream();
            FileOutputStream outputStream = new FileOutputStream(file);
            int len=0;
            long currentLen=0;
            byte[] buff=new byte[1024];
            while ((len = inputStream.read(buff)) > 0) {
                outputStream.write(buff,0,len);
                currentLen+=len;
                httpCallBack.onLoading(currentLen,totalLength);
            }
            httpCallBack.onFinished();
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            httpCallBack.onError();
        } catch (IOException e) {
            e.printStackTrace();
            httpCallBack.onError();
        }
    }
}
