package com.example.upload;

import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpLoader {
    OkHttpClient client;
    MyRequestBody myRequestBody;
    public void upload(File file,String url, MyRequestBody.ProgressListener listener){
        client = new OkHttpClient();
        myRequestBody = new MyRequestBody(file,listener);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("totallength",String.valueOf(file.length()))
                .addHeader("filename",file.getName())
                .post(myRequestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public void cancel() {
        myRequestBody.cancel();
    }
}
