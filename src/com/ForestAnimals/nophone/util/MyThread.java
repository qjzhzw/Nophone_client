package com.ForestAnimals.nophone.util;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by dell on 2016/12/21.
 */
public class MyThread extends Thread {

    Internet internet = new Internet();

    public List<NameValuePair> params;
    public String url, result;
    public String url_base = "http://qjzhzw.tunnel.qydev.com/";
    boolean done = false;

    public MyThread(List<NameValuePair> params, String url) {
        this.params = params;
        this.url = url_base + url;
    }


    public void run() {
        try {
            result = internet.doPost(params, url);
            done = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDone() {
        return done;
    }

    /**
     * 接收解析后的消息
     * parse_key为需要解析的关键词
     * parse_result为解析得到的相应结果
     */
    public String[] parseJson(String[] parse_key) {
        //创建解析结果数组
        String[] parse_result = new String[parse_key.length];

        try {
            JSONObject jsonObject = new JSONObject(this.result);

            for (int i = 0; i < parse_key.length; i++)
                parse_result[i] = jsonObject.getString(parse_key[i]);

        } catch (Exception e) {
            for (int i = 0; i < parse_key.length; i++)
                parse_result[i] = "failed";
        }

        return (parse_result);

    }

}
