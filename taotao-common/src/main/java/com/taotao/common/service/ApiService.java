package com.taotao.common.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.bean.HttpResult;

@Service
public class ApiService {

	// 创建Httpclient对象
	@Autowired(required=false)
	private CloseableHttpClient httpclient;

	@Autowired(required=false)
	private RequestConfig requestConfig;

	/**
	 * 200返回内容，其他状态码返回null
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String doGet(String url) throws ClientProtocolException, IOException {

		// 创建http GET请求
		HttpGet httpGet = new HttpGet(url);
		// 设置请求参数
		httpGet.setConfig(this.requestConfig);

		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				String content = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				return content;
			}
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return null;
	}

	/**
	 * 执行带有参数的get请求
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public String doGet(String url, Map<String, String> param)
			throws ClientProtocolException, IOException, URISyntaxException {
		URIBuilder builder = new URIBuilder(url);
		for (Map.Entry<String, String> entry : param.entrySet()) {
			builder.addParameter(entry.getKey(), entry.getValue());

		}
		return doGet(builder.build().toString());
	}

	/**
	 * 带有参数的post请求
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResult doPost(String url, Map<String, String> param)
			throws ClientProtocolException, IOException {

		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);

		// 设置请求参数
		httpPost.setConfig(this.requestConfig);

		if (param != null) {
			// 设置post参数
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
			for (Map.Entry<String, String> entry : param.entrySet()) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
			// 构造一个form表单式的实体
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					parameters, "UTF-8");
			// 将请求实体设置到httpPost对象中
			httpPost.setEntity(formEntity);
		}

		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpclient.execute(httpPost);
			return new HttpResult(response.getStatusLine().getStatusCode(),
					EntityUtils.toString(response.getEntity(), "UTF-8"));

		} finally {
			if (response != null) {
				response.close();
			}
		}

	}
	
	/**
	 * post提交json数据
	 * @param url
	 * @param json
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResult doPostJson(String url,String json)
			throws ClientProtocolException, IOException {

		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);

		// 设置请求参数
		httpPost.setConfig(this.requestConfig);

		if (json != null) {
			// 构造一个String的实体
			StringEntity stringEntity = new StringEntity(json, "UTF-8");
			// 将请求实体设置到httpPost对象中
			httpPost.setEntity(stringEntity);
		}

		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpclient.execute(httpPost);
			return new HttpResult(response.getStatusLine().getStatusCode(),
					EntityUtils.toString(response.getEntity(), "UTF-8"));

		} finally {
			if (response != null) {
				response.close();
			}
		}
		

	}

	public HttpResult doPost(String url) throws ClientProtocolException,
			IOException {
		return doPost(url, null);
	}
}
