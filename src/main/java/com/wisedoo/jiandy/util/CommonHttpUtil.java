package com.wisedoo.jiandy.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @ClassName: CommonHttpUtil
 * @Description: TODO
 * @Auther: liujn
 * @Date: 2019/7/9 9:24 AM
 * @Version 1.0
 */
public class CommonHttpUtil {


    /**
     * @Description: 信任全部证书
     * @Param:
     * @Return:
     **/
    public HttpClient getSSLHttpClient() throws Exception {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            //信任所有
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }



    public Object doPost(String uri, Header[] headers, String bodyData) throws Exception {
        HttpClient client = getSSLHttpClient();
        // POST请求
        HttpRequestBase request = new HttpPost(uri);
        ObjectMapper entityMapper = new ObjectMapper();
        HttpEntity entity = new StringEntity(bodyData, Charsets.UTF_8);
        ((HttpPost) request).setEntity(entity);
        // 设置请求头
        request.setHeaders(headers);
        // 发送请求并获取返回结果
        HttpResponse response = client.execute(request);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = (Map<String, Object>) mapper.readValue(response.getEntity().getContent(), Object.class);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode >= 400) {
            // 请求错误
            if ((Integer) result.get("code") == 8303) {
                // 频率超限，5s后重试
                Thread.sleep(5000);
                return doPost(uri, headers, bodyData);
            } else {
                throw new RuntimeException("请求错误，Error Code: " + result.get("code") + ", Error Msg: " + result.get("msg"));
            }
        } else {
            // 处理返回结果
            return result;
        }

    }


}
