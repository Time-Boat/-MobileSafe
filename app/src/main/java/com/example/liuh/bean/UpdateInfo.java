package com.example.liuh.bean;

/**
 * Created by Administrator on 2016-09-07.
 */
public class UpdateInfo {

    /**
     * versionName : 2.0
     * versionCode : 2
     * description : 新增NB功能,赶紧体验!!!
     * downloadUrl : http://www.baidu.com
     */

    private String versionName;
    private int versionCode;
    private String description;
    private String downloadUrl;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
