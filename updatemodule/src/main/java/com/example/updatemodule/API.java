package com.example.updatemodule;

public class API {
    private String id;
    private String time;
    private String apk;
    private String version;

    public API(String id, String time, String apk, String version) {
        this.id = id;
        this.time = time;
        this.apk = apk;
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getApk() {
        return apk;
    }

    @Override
    public String toString() {
        return id + " " + time + " " + apk + " " + version;
    }
}


