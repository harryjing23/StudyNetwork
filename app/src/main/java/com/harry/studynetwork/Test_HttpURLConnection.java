package com.harry.studynetwork;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 每个HttpURLConnection只能发送一次请求
 * GET请求在url后拼接的参数称为query参数，POST请求的参数称为body参数
 *
 * @author Harry Jing
 * @date 12/5/20
 */
public class Test_HttpURLConnection {
    private static final String TAG = "Test_HttpURLConnection";

    /**
     * 新闻频道查询
     * 请求方式：GET
     * 返回类型：JSON
     */

    public static void channel() throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        BufferedReader input = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(Constant.CHANNEL_URL);
            // openConnection获取到URLConnection，URLConnection是抽象类
            URLConnection urlConnection = url.openConnection();
            // 实现类为HttpURLConnection，以及HttpURLConnection的子类HttpsURLConnection
            connection = (HttpURLConnection) urlConnection;
            // 设置HttpURLConnection。除下面以外，还有更多设置，需要http知识
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.setDefaultUseCaches(true);
            // 设置超时
            connection.setConnectTimeout(5 * 1000);
            connection.setReadTimeout(5 * 1000);
            // setDoInput(true)可以获取输入流，即响应数据。默认为true，所以不用单独设置
            connection.setDoInput(true);

            // 该API的header如果不配置，responseCode为401，即用户没有访问权限，需要进行身份认证
            // 用setRequestProperty来设置header
            for (Map.Entry<String, String> entry : Channel.getHeader().entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            // 配置必须要在connect之前完成
            connection.connect();

            int responseCode = connection.getResponseCode();
            Log.d(TAG, "channel: responseCode: " + responseCode);
            // GET请求没有RequestBody，即输出流。故不需要connection.setDoOutput(true)和getOutputStream
            // getOutputStream自带connect，可以不调用connect
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String read;
            while ((read = input.readLine()) != null) {
                sb.append(read);
            }
            Log.d(TAG, "channel: " + sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != input) {
                    input.close();
                }
                if (null != connection) {
                    // HttpURLConnection调用disconnect后，并不意味着可以将此重新用于其他请求
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static class Channel {
        public static Map<String, String> getHeader() {
            Map<String, String> headers = new HashMap<String, String>();
            try {
                Calendar cd = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String datetime = sdf.format(cd.getTime());
                String auth = Channel.calcAuthorization(Constant.SOURCE, Constant.SECRET_ID, Constant.SECRET_KEY, datetime);
                // 请求头
                headers.put("X-Source", Constant.SOURCE);
                headers.put("X-Date", datetime);
                headers.put("Authorization", auth);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            return headers;
        }

        public static String calcAuthorization(String source, String secretId, String secretKey, String datetime)
                throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
            String signStr = "x-date: " + datetime + "\n" + "x-source: " + source;
            Mac mac = Mac.getInstance("HmacSHA1");
            Key sKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), mac.getAlgorithm());
            mac.init(sKey);
            byte[] hash = mac.doFinal(signStr.getBytes("UTF-8"));
            String sig = new String(Base64.encode(hash, Base64.DEFAULT));
            // java.lang.IllegalArgumentException: Unexpected char 0x0a，要去掉换行
            sig = sig.substring(0, sig.length() - 1);

            String auth = "hmac id=\"" + secretId + "\", algorithm=\"hmac-sha1\", headers=\"x-date x-source\", signature=\"" + sig + "\"";
            return auth;
        }

        public static String urlencode(Map<?, ?> map) throws UnsupportedEncodingException {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(String.format("%s=%s",
                        URLEncoder.encode(entry.getKey().toString(), "UTF-8"),
                        URLEncoder.encode(entry.getValue().toString(), "UTF-8")
                ));
            }
            return sb.toString();
        }
    }
}
