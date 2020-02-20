package com.sunny.lib.http.java;

public class ResultBody {

    private String nextPageUrl;
    private String nextPublishTime;
    private String newestIssueType;

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getNextPublishTime() {
        return nextPublishTime;
    }

    public void setNextPublishTime(String nextPublishTime) {
        this.nextPublishTime = nextPublishTime;
    }

    public String getNewestIssueType() {
        return newestIssueType;
    }

    public void setNewestIssueType(String newestIssueType) {
        this.newestIssueType = newestIssueType;
    }
}
