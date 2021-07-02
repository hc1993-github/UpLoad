package com.example.upload;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class MyRequestBody extends RequestBody {
    File file;
    ProgressListener listener;
    boolean iscancel = false;
    public MyRequestBody(File file,ProgressListener listener) {
        this.file = file;
        this.listener = listener;
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("application/octet-stream");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source;
        long readlength=0;
        long readtotallength=0;
        long totallength = contentLength();
        try {
            source = Okio.source(file);
            Buffer buffer = new Buffer();
            listener.start();
            while ((readlength = source.read(buffer, 1024)) != -1) {
                if(iscancel){
                    listener.cancel();
                    return;
                }
                sink.write(buffer, readlength);
                sink.flush();
                readtotallength += readlength;
                listener.onProgress(totallength, readtotallength);
            }
                listener.finish();
        }catch (Exception e){

        }
    }

    public void cancel() {
        iscancel = true;
    }

    interface ProgressListener{
        void start();
        void onProgress(long totalsize,long currentsize);
        void finish();
        void cancel();
    }
}
