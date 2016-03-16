package com.codeday.tb.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

/**
 * 通过HttpClient对象实现HTTP的请求
 * 
 * @author liuxf 需要jar: httpcore-4.4.1.jar httpclient-4.4.1.jar
 */
public class HttpClientUtil {

	public static JSONObject doGet(String url) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			HttpResponse httpResponse = null;
			httpResponse = httpClient.execute(httpGet);
			// response实体
			HttpEntity entity = httpResponse.getEntity();
			if (null != entity) {
				// System.out.println("响应状态码:" + httpResponse.getStatusLine());
				// System.out.println("-------------------");
				// System.out.println("响应内容:" + EntityUtils.toString(entity));
				// System.out.println(httpResponse.getProtocolVersion());
				String result = EntityUtils.toString(entity, "UTF-8");
				System.out.println(result);
				//jsonObject = JSONObject.fromObject(result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return jsonObject;
	}

	public static void main(String[] args) {
		try {
			JSONObject josnJsonObject = HttpClientUtil.doGet("http://timbryant.tunnel.qydev.com/weixin/index.jsp");
			// System.out.println(josnJsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
