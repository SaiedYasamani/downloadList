package com.example.saied.downloadlist;

import com.huxq17.download.DownloadInfo;

public class updatable {

    String appId;
    String url;
    DownloadInfo downloadInfo;
    DownloadInfo.Status status;

    public updatable(String appId, String url, DownloadInfo downloadInfo, DownloadInfo.Status status) {
        this.appId = appId;
        this.url = url;
        this.downloadInfo = downloadInfo;
        this.status = status;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DownloadInfo getDownloadInfo() {
        return downloadInfo;
    }

    public void setDownloadInfo(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    public DownloadInfo.Status getStatus() {
        return status;
    }

    public void setStatus(DownloadInfo.Status status) {
        this.status = status;
    }
}
