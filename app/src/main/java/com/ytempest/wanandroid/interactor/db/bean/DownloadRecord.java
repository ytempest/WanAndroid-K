package com.ytempest.wanandroid.interactor.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author heqidu
 * @since 2020/11/19
 */
@Entity
public class DownloadRecord {
    @Id(autoincrement = true)
    private Long recordId;

    private String url;
    private String savePath;
    private long lastSeek;

    @Generated(hash = 662636611)
    public DownloadRecord(Long recordId, String url, String savePath, long lastSeek) {
        this.recordId = recordId;
        this.url = url;
        this.savePath = savePath;
        this.lastSeek = lastSeek;
    }

    @Generated(hash = 155491740)
    public DownloadRecord() {
    }

    public Long getRecordId() {
        return this.recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return this.savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getLastSeek() {
        return this.lastSeek;
    }

    public void setLastSeek(long lastSeek) {
        this.lastSeek = lastSeek;
    }
}
