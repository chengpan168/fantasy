package com.shining3d.saber;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by panwang.chengpw on 16/11/16.
 */
public class SaberConfig {

    // /opt/saber/data
    private String spider3DDayinDownloadPath;
    private int    spider3DDayinPageStart;
    private int    spider3DDayinPageEnd;
    private int    spider3DDayinCurrentPage;
    private String spider3DDayinPageUrl;

    private String imgUploadUrl;
    private String authPanguAccessSecret;
    private String authPanguAccessId;

    private long spiderSleepTime = 0;

    private boolean spider3DDayinSwitch;

    public void setSpider3DDayinDownloadPath(String spider3DDayinDownloadPath) {
        this.spider3DDayinDownloadPath = spider3DDayinDownloadPath;
    }

    public int getSpider3DDayinPageStart() {
        return spider3DDayinPageStart;
    }

    public void setSpider3DDayinPageStart(int spider3DDayinPageStart) {
        this.spider3DDayinPageStart = spider3DDayinPageStart;
    }

    public int getSpider3DDayinPageEnd() {
        return spider3DDayinPageEnd;
    }

    public void setSpider3DDayinPageEnd(int spider3DDayinPageEnd) {
        this.spider3DDayinPageEnd = spider3DDayinPageEnd;
    }

    public String getSpider3DDayinPageUrl() {
        return spider3DDayinPageUrl;
    }

    public void setSpider3DDayinPageUrl(String spider3DDayinPageUrl) {
        this.spider3DDayinPageUrl = spider3DDayinPageUrl;
    }

    public String getSpider3DDayinDownloadPath() {
        return spider3DDayinDownloadPath;
    }

    public String getImgUploadUrl() {
        return imgUploadUrl;
    }

    public void setImgUploadUrl(String imgUploadUrl) {
        this.imgUploadUrl = imgUploadUrl;
    }

    public String getAuthPanguAccessSecret() {
        return authPanguAccessSecret;
    }

    public void setAuthPanguAccessSecret(String authPanguAccessSecret) {
        this.authPanguAccessSecret = authPanguAccessSecret;
    }

    public String getAuthPanguAccessId() {
        return authPanguAccessId;
    }

    public void setAuthPanguAccessId(String authPanguAccessId) {
        this.authPanguAccessId = authPanguAccessId;
    }

    public int getSpider3DDayinCurrentPage() {
        return spider3DDayinCurrentPage;
    }

    public synchronized void setSpider3DDayinCurrentPage(int spider3DDayinCurrentPage) {
        this.spider3DDayinCurrentPage = spider3DDayinCurrentPage;
    }

    public void setSpider3DDayinSwitch(String spider3DDayinSwitch) {
        this.spider3DDayinSwitch = BooleanUtils.toBoolean(spider3DDayinSwitch);
    }

    public boolean getSpider3DDayinSwitch() {
        return spider3DDayinSwitch;
    }

    public void setSpiderSleepTime(String spiderSleepTime) {
        if (StringUtils.isBlank(spiderSleepTime)) {
            return;
        }

        this.spiderSleepTime = Long.valueOf(spider3DDayinPageEnd);
    }

    public long getSpiderSleepTime() {
        return spiderSleepTime;
    }
}
