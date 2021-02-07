package com.ytempest.wanandroid.download;

import com.ytempest.tool.util.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author heqidu
 * @since 2020/9/30
 */
public class DownloadUnit {

    private final OkHttpClient mOkHttpClient;
    private String mUrl;
    private File mSaveFile;
    private DownloadListener mListener;
    private volatile boolean isCancel;

    public DownloadUnit(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
    }

    public void setup(String url, File file) {
        mUrl = url;
        mSaveFile = file;
    }

    public DownloadUnit setDownloadListener(DownloadListener listener) {
        mListener = listener;
        return this;
    }

    public String getUrl() {
        return mUrl;
    }

    public File getSaveFile() {
        return mSaveFile;
    }

    public void startDownload(long start) {
        isCancel = false;
        Request request = new Request.Builder().url(mUrl)
                .addHeader("Range", "bytes=" + start + "-")
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mListener != null) {
                    mListener.onFail(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null) {
                    onFailure(call, null);
                    return;
                }
                RandomAccessFile accessFile = null;
                InputStream stream = null;
                long saveLen = start;
                long totalLen = saveLen + body.contentLength();
                try {
                    accessFile = new RandomAccessFile(mSaveFile, "rw");
                    accessFile.seek(saveLen);
                    stream = body.byteStream();
                    int lastProgress = 0;
                    int len;
                    byte[] buffer = new byte[2048];
                    while ((len = stream.read(buffer)) != -1) {
                        if (isCancel) {
                            return;
                        }
                        accessFile.write(buffer, 0, len);
                        saveLen += len;
                        // 这里做个过滤，减少回调次数
                        int progress = (int) (1F * saveLen / totalLen * 100);
                        if (lastProgress != progress) {
                            lastProgress = progress;
                            if (mListener != null) {
                                mListener.onProgress(saveLen, totalLen);
                            }
                        }
                    }
                    if (mListener != null) {
                        mListener.onSuccess(mSaveFile);
                    }

                } catch (Exception e) {
                    if (mListener != null) {
                        mListener.onFail(e);
                    }

                } finally {
                    IOUtils.close(accessFile, stream, body);
                }

            }
        });
    }

    public void cancelDownload() {
        isCancel = true;
    }

    public interface DownloadListener {

        void onProgress(long saveLen, long totalLen);

        void onSuccess(File file);

        void onFail(Throwable throwable);
    }
}
