package com.lepetit.gradehelper;

import com.lepetit.okhttphelper.OKHttpUnit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GetGradeAnalyzeInfo {
    private static GetGradeAnalyzeInfo instance;

    private GetGradeAnalyzeInfo() {}

    private static GetGradeAnalyzeInfo getInstance() {
        if (instance == null) {
            instance = new GetGradeAnalyzeInfo();
        }
        return instance;
    }

    private Map<String, String> _get(String url) {
        Map<String, String> infos = new HashMap<>();
        try {
            Response response = OKHttpUnit.getSync(url);
            if (response.isSuccessful()) {
                infos = DealHtml.analyzeGrade(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return infos;
    }

    public static Map<String, String> get(String url) {
        return getInstance()._get(url);
    }
}
