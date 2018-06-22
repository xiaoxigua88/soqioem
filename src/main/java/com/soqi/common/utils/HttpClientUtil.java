package com.soqi.common.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author sunao
 * 类名:httpClient客户端工具处理类
 */
public class HttpClientUtil {
	static CloseableHttpClient m_HttpClient;
	
	static{
		m_HttpClient = HttpClients.createDefault();
	}
	
	public static String post(String url, byte[] bytes, String contentType) throws ParseException, IOException{
		String body="";
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new ByteArrayEntity(bytes));
		if (contentType != null){
			httpPost.setHeader("Content-type", contentType);
		}
		CloseableHttpResponse httpResponse = m_HttpClient.execute(httpPost);
		HttpEntity entityResponse = httpResponse.getEntity();
		// 判断状态码是否为200
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            // 返回响应体的内容
        	body = EntityUtils.toString(entityResponse, "UTF-8");
        	//释放连接
        	EntityUtils.consume(entityResponse);
        }
        return body;
		
	}
	
	public static String postUrlData(String url, String data,String reqEncoding,String respEncoding) throws ClientProtocolException, IOException{
		byte[] reqBuffer = data.getBytes(Charset.forName(reqEncoding));
		return post(url, reqBuffer, "application/x-www-form-urlencoded; charset="+reqEncoding);
	}
	
    public static void main2(String[] args){
    	Map<String, Object> map = new HashMap<String, Object>();
        String[] keyword = { "女装", "租房"};
        String[] urld = { "58.com", "ganji.com" };
        int searchType = 1010;
        map.put("apiExtend", 1);
        map.put("userId", 106083);
        map.put("businessType", 1006);
        map.put("keyword", keyword);
        map.put("url", urld);
        map.put("searchType", searchType);
        map.put("time", DateUtil.getSecondTimestampTwo(new Date()));
        String data = JSONObject.toJSONString(map);
        String param = EncodeUtils.urlEncode(data);
        System.out.println("****************************"+EncodeUtils.urlDecode(param));
        String tocken = "6B046C4EAAFDCA7AA8AEFD49B8D0294D";
        String action = "AddSearchTask";
        String url = "http://api.youbangyun.com/api/customerapi.aspx";
        String sign = DigestUtils.md5Hex("AddSearchTask" + "6B046C4EAAFDCA7AA8AEFD49B8D0294D" + data).toUpperCase();
        
        System.out.println("原始param=" + data + "\r\n");
        System.out.println("wAction=" + action + "\r\n");
        System.out.println("POST请求时，要将参数值进行UTF-8编码\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
        
        System.out.println("wParam=" + param + "\r\n");
        System.out.println("wSign=" + sign + "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
        System.out.println("wSign的值是wAction的值连接APITOCKEN连接data原始值进行MD5加密，在转化成大写：MD5(" + action + tocken + data + ")\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
        Map<String, Object> wMap = new HashMap<String, Object>();
        wMap.put("wParam=", param);
        wMap.put("wSign=", sign);
        wMap.put("wAction=", action);
        String urlData = "wAction=" + action + "&wParam=" + param + "&wSign=" + sign;
		try {
			//String body = HttpClientUtil.sendPostDataByJson(url, JSON.toJSONString(wMap), "utf-8");
			String body = postUrlData(url, urlData, "UTF-8", "UTF-8");
			System.out.println("响应结果：" + body);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    public static void main(String[] args){
    	Map<String, Object> map = new HashMap<String, Object>();
    	String[] keyword = { "男装", "租房"};
    	map.put("apiExtend", 1);
    	map.put("userId", 106083);
    	map.put("businessType", 1008);
    	map.put("keyword", keyword);
    	map.put("time", DateUtil.getSecondTimestampTwo(new Date()));
    	String data = JSONObject.toJSONString(map);
    	String param = EncodeUtils.urlEncode(data);
    	System.out.println("****************************"+EncodeUtils.urlDecode(param));
    	String tocken = "6B046C4EAAFDCA7AA8AEFD49B8D0294D";
    	String action = "AddSearchTask";
    	String url = "http://api.youbangyun.com/api/customerapi.aspx";
    	String sign = DigestUtils.md5Hex("AddSearchTask" + "6B046C4EAAFDCA7AA8AEFD49B8D0294D" + data).toUpperCase();
    	
    	System.out.println("原始param=" + data + "\r\n");
    	System.out.println("wAction=" + action + "\r\n");
    	System.out.println("POST请求时，要将参数值进行UTF-8编码\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
    	
    	System.out.println("wParam=" + param + "\r\n");
    	System.out.println("wSign=" + sign + "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
    	System.out.println("wSign的值是wAction的值连接APITOCKEN连接data原始值进行MD5加密，在转化成大写：MD5(" + action + tocken + data + ")\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
    	Map<String, Object> wMap = new HashMap<String, Object>();
    	wMap.put("wParam=", param);
    	wMap.put("wSign=", sign);
    	wMap.put("wAction=", action);
    	String urlData = "wAction=" + action + "&wParam=" + param + "&wSign=" + sign;
    	try {
    		//String body = HttpClientUtil.sendPostDataByJson(url, JSON.toJSONString(wMap), "utf-8");
    		String body = postUrlData(url, urlData, "UTF-8", "UTF-8");
    		System.out.println("响应结果：" + body);
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    }

}
