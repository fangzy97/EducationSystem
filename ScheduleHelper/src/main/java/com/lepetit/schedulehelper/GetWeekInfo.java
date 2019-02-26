package com.lepetit.schedulehelper;

import com.lepetit.okhttphelper.OKHttpUnit;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GetWeekInfo {
	private static GetWeekInfo instance;

	private GetWeekInfo() {}

	private void _get() {
		OKHttpUnit.getAsync(StringCollection.weekUrl, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				AnalyzeWeekInfo.analyze(response.body().string());
			}
		});
	}

	private static GetWeekInfo getInstance() {
		if (instance == null) {
			instance = new GetWeekInfo();
		}
		return instance;
	}

	public static void get() {
		getInstance()._get();
	}
}
