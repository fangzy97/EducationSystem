package com.example.updatemodule;

public class API {
    private String id;
    private String time;
    private String apk;
    private String version;
    private String updateContent;

    public API(String id, String time, String apk, String version, String updateContent) {
        this.id = id;
        this.time = time;
        this.apk = apk;
        this.version = version;
        this.updateContent = updateContent;
    }

    public String getVersion() {
        return version;
    }

    public String getApk() {
        return apk;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    @Override
    public String toString() {
        return id + " " + time + " " + apk + " " + version + " " + updateContent;
    }
}


