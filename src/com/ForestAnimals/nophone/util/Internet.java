package com.ForestAnimals.nophone.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * Created by dell on 2016/12/21.
 */
public class Internet {

    /**
     * 访问数据库并返回JSON数据字符串
     *
     * @param params 向服务器端传的参数
     * @param url
     * @return
     * @throws Exception
     */
    public String doPost(List<NameValuePair> params, String url)
            throws Exception {
        String result = null;
        // 获取HttpClient对象
        HttpClient httpClient = new DefaultHttpClient();
        // 新建HttpPost对象
        HttpPost httpPost = new HttpPost(url);
        if (params != null) {
            // 设置字符集
            HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            // 设置参数实体
            httpPost.setEntity(entity);
        }

//         // 连接超时
//         httpClient.getParams().setParameter(
//                 CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
//         // 请求超时
//         httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
//                 20000);

        try {
            // 获取HttpResponse实例
            HttpResponse httpResp = httpClient.execute(httpPost);
            // 判断是够请求成功
            if (httpResp.getStatusLine().getStatusCode() == 200) {
                // 获取返回的数据
                result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
            } else {
                result = null;
            }
        } catch (Exception e) {
            result = null;
        }

        return result;
    }


}
