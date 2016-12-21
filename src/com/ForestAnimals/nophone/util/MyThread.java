package com.ForestAnimals.nophone.util;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by dell on 2016/12/21.
 */
public class MyThread extends Thread {

    Internet internet = new Internet();

    List<NameValuePair> params;
    String url, result;

    public MyThread(List<NameValuePair> params, String url) {
        this.params = params;
        this.url = url;
    }

    public void run() {
        try {
            result = internet.doPost(params, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
