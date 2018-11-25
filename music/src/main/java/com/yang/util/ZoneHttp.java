package com.yang.util;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by RiddleLi on 2018/11/15.
 */

public class ZoneHttp {

    //get请求的地址处理
    private static String dealGetAddress(String url, Map<String, Object> map) {
        //将map中的数据拼接到url后面
        if (!map.isEmpty()) {
            url = url + "?";
            Set<String> sets = map.keySet();
            for (String set : sets) {
                url = url + set + "=" + String.valueOf(map.get(set)) + "&";
            }
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    //发送异步Get请求
    public static void asyncGet(String url, Map<String, Object> map, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String address = dealGetAddress(url, map);
        Request request = new Request.Builder()
                .url(address)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    //发送同步Get请求（阻塞当前线程）
    public static String syncGet(String url, Map<String, Object> map) throws IOException, NullPointerException {
        String address = dealGetAddress(url, map);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            /*string()适用于获取小数据信息；如果返回的数据超过1M，使用stream()获取返回的数据
              因为string()方法会将整个文档加载到内存中。*/
            return response.body().string();
        }else {
            return "Failure";
        }
    }

    //发送异步Post请求
    public static void asyncPost(String url, Map<String, Object> map, Callback callback) {
        //将map中的数据加入表单中
        FormBody.Builder formBody = new FormBody.Builder();
        Set<String> sets = map.keySet();
        for (String set : sets) {
            formBody.add(set, String.valueOf(map.get(set)));
        }
        RequestBody requestBody = formBody.build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    //发送同步Post请求（阻塞当前线程）
    public static String syncPost(String url, Map<String, Object> map) throws IOException, NullPointerException {        //将map中的数据加入表单中
        FormBody.Builder formBody = new FormBody.Builder();
        Set<String> sets = map.keySet();
        for (String set : sets) {
            formBody.add(set, String.valueOf(map.get(set)));
        }
        RequestBody requestBody = formBody.build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            /*string()适用于获取小数据信息；如果返回的数据超过1M，使用stream()获取返回的数据
              因为string()方法会将整个文档加载到内存中。*/
            return response.body().string();
        }else {
            return "Failure";
        }
    }
}
