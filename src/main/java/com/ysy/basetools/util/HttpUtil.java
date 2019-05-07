package com.ysy.basetools.util;


import com.ysy.basetools.log.LogUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Created by guoqiang on 2016/4/5.
 */
public class HttpUtil {
    private static final Logger logger = LogUtil.getLog(HttpUtil.class);

    public static String post(String url, Map<String, String> params, byte[] body, String contentType, int connectTimeout, int readTimeout) throws Exception {
        url = getProtocolUrl(url);
        PostMethod postMethod = new PostMethod(url);
        if (params != null) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                if (key != null && value != null) {
                    postMethod.setParameter(key, value);
                }
            }
        }
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        if (body != null) {
            postMethod.setRequestEntity(new ByteArrayRequestEntity(body, contentType));
        }
        HttpClient httpClient = new HttpClient();
        HttpConnectionManagerParams managerParams = httpClient.getHttpConnectionManager().getParams();
        // 设置连接超时时间(单位毫秒)
        managerParams.setConnectionTimeout(connectTimeout);

        // 设置读数据超时时间(单位毫秒)
        managerParams.setSoTimeout(readTimeout);
        httpClient.getParams().setContentCharset("UTF-8");
        int status = httpClient.executeMethod(postMethod);
        if (status == 200) {
            String responseBody = postMethod.getResponseBodyAsString();
            return responseBody;
        } else {
            //TODO 后续看是否添加404错误判断
            String responseBody = postMethod.getResponseBodyAsString();
            return responseBody;
        }
    }

    public static String post(String url, Map<String, String> params, int connectTimeout, int readTimeout) throws Exception {
        return post(url, params, null, null, connectTimeout, readTimeout);
    }

    /**
     * 根据URL 和编码格式 获取指定url返回的信息
     *
     * @param urlStr
     * @param charset
     * @return
     * @throws Exception
     */
    public static String getContent(String urlStr, String charset) throws RuntimeException {
        return getContent(urlStr, charset, null, null);
    }

    /**
     * 根据URL 和编码格式 获取指定url返回的信息
     *
     * @param urlStr
     * @param charset
     * @return
     * @throws Exception
     */
    public static String getContent(String urlStr, String charset, Integer connectTimeout, Integer readTimeout) throws RuntimeException {
        urlStr = getProtocolUrl(urlStr);
        BufferedReader bufferedReader = null;
        StringBuilder builder = new StringBuilder(50 * 1024);
        try {
            URL url = new URL(urlStr);
            InputStream is;
            if (connectTimeout == null || readTimeout == null) {
                is = url.openStream();
            } else {
                URLConnection connect = url.openConnection();
                connect.setConnectTimeout(connectTimeout);
                connect.setReadTimeout(readTimeout);
                is = connect.getInputStream();
            }
            bufferedReader = new BufferedReader(new InputStreamReader(is, charset));
            builder = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return builder.toString();
    }

    private static String getProtocolUrl(String url) {
        if (url != null) {
            String temp = url.toLowerCase();
            if (temp.startsWith("http://") || temp.startsWith("https://")) {
                return url;
            } else {
                return "http://" + url;
            }
        }
        return url;
    }
}
