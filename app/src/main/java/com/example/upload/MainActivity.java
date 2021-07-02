package com.example.upload;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Button btn1;
//    Button btn2;
    ProgressBar progressBar;
    TextView textView;
    UpLoader upLoader;
    boolean cancel = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        initviews();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(upLoader==null){
                    upLoader = new UpLoader();
                }
                if(cancel){
                    btn1.setText("开始上传");
                    progressBar.setProgress(0);
                    textView.setText("0%");
                    cancel = false;
                    upLoader.cancel();
                }else {
                    upLoader.upload(new File(Environment.getExternalStorageDirectory(), "SougoPinyin_PCDownload1100112706.exe"), "http://192.168.8.95:8888/huachen/servlet1/file",new MyRequestBody.ProgressListener() {
                        @Override
                        public void start() {
                            btn1.setText("取消上传");
                            cancel = true;
                        }

                        @Override
                        public void onProgress(long totalsize, long currentsize) {
                            progressBar.setMax((int) totalsize);
                            progressBar.setProgress((int) currentsize);
                            float ple = (float)currentsize/(float) totalsize;
                            textView.setText(String.format("%.0f", ple * 100) + "%");
                        }

                        @Override
                        public void finish() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"上传完毕",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void cancel() {
                            btn1.setText("开始上传");
                            progressBar.setProgress(0);
                            textView.setText("0%");
                        }
                    });
                }
            }
        });
    }

    private void initviews() {
        btn1 = findViewById(R.id.btn1);
//        btn2 = findViewById(R.id.btn2);
        progressBar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.textview);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

        }
        return false;
    }
}
