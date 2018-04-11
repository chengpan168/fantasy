package com.fantasy.common;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 必须是jdk7及以上版本
 */
public class HttpUtils {

    public static final String         USER_AGENT_DEFAULT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.58 Safari/537.36s";

    private static final Logger        logger             = LoggerFactory.getLogger(HttpUtils.class);

    private static Charset             UTF8               = StandardCharsets.UTF_8;

    private static CloseableHttpClient httpClient;

    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(20);
        httpClient = HttpClients.custom().setConnectionManager(cm).build();
    }

    public static String get(String url){
        return get(url, null);
    }
    public static String get(String url, Map<String, Object> params) {
        HttpGet httpget = new HttpGet(url);
        logger.info("执行get 请求: {} ", httpget.getRequestLine());

        try (CloseableHttpResponse response = httpClient.execute(httpget);) {

            return handleResponse(response);

        } catch (Exception e) {
            logger.error("", e);
        }

        return StringUtils.EMPTY;
    }

    public static String post(String url, Map<String, Object> params) {
        HttpPost httpPost = new HttpPost(url);
        logger.info("执行get 请求: {} ", httpPost.getRequestLine());

        if (MapUtils.isNotEmpty(params)) {
            List<NameValuePair> collect = params.entrySet().stream().map(entry -> {
                return new BasicNameValuePair(entry.getKey(), "");
            }).collect(Collectors.toList());

            HttpEntity entity = new UrlEncodedFormEntity(collect, UTF8);
            httpPost.setEntity(entity);

        }

        try (CloseableHttpResponse response = httpClient.execute(httpPost);) {

            return handleResponse(response);

        } catch (Exception e) {
            logger.error("", e);
        }

        return StringUtils.EMPTY;
    }

    public static String postJson(String url, Object params) {
        HttpPost httpPost = new HttpPost(url);
        logger.info("执行post 请求: {} ", httpPost.getRequestLine());

        if (params != null) {
            HttpEntity entity = new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
        }

        try (CloseableHttpResponse response = httpClient.execute(httpPost);) {

            return handleResponse(response);

        } catch (Exception e) {
            logger.error("", e);
        }

        return StringUtils.EMPTY;
    }

    private static String handleResponse(CloseableHttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() != 200) {
            logger.warn("请求返回错误: {}", response.getStatusLine());
            return StringUtils.EMPTY;
        }

        HttpEntity entity = response.getEntity();

        String res = EntityUtils.toString(entity, UTF8);
        logger.info("请求结果: {}", res);
        return res;
    }
}
