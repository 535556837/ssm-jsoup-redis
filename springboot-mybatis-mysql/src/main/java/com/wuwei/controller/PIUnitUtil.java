package com.wuwei.controller;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * picc Htmlunit Util
 * 
 */
public class PIUnitUtil {

	protected String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0";
	protected String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";

	protected static String piccUrl = "https://10.134.138.16:8888/casserver/login?service=http%3A%2F%2F10.134.138.16%3A80%2Fportal%2Findex.jsp";
	protected static String piccLoginUrl = "https://10.134.138.16:8888//casserver/login?service=http%3A%2F%2F10.134.138.16%3A80%2Fportal%2Findex.jsp";
	protected static String piccBusiUrl = "http://10.134.138.16:8000/prpall?calogin";
	protected static String piccMethodUrl = "http://10.134.138.16:8000/prpall/business/prepareEdit.do?bizType=PROPOSAL&editType=NEW";
	protected static String dwrUrl = "http://10.134.138.16:8000/prpall/business/caculatePremiunForFG.do";
	//跳转保单查询界面
	protected  static String POLICYURL="http://10.134.138.16:8000/prpall/business/preparePolicyQueryCode.do";

	protected static Map<String, String> cookies;
	protected static Map<String, String> headers;
	protected static PIUnitUtil s = new PIUnitUtil();
	protected static Response serviceRes ;

	public PIUnitUtil() {
		try {
			getData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getData() throws Exception {

		headers = new HashMap<String, String>();
		headers.put("User-Agent",
				"User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
		headers.put("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
		headers.put("Accept-Encoding", "gzip, deflate");
		headers.put("Accept-Language", "zh-CN");
		headers.put("Cache-Control", "no-cache");
		headers.put("Connection", "keep-alive");
		headers.put("Content-Length", "292");
		headers.put("Content-Type", "application/x-www-form-urlencoded");


		// 第一次请求,获取COOKIES
		new SSLClient();
		Connection conFirst = Jsoup.connect(piccUrl).validateTLSCertificates(true).headers(headers);

		Response rs = null;
		try {
			rs = conFirst.execute();
		} catch (IOException e2) {
			e2.printStackTrace();
			System.out.println("PIUnitUtil 初始化错误， 请检查vpn 是否正常");
		} // 获取响应

		/// 填充登录参数
		Document d1 = Jsoup.parse(rs.body());// 转换为Dom树
		List<Element> et = d1.select("#fm");// 获取form表单，可以通过查看页面源码代码得知
		// 获取，cooking和表单属性，下面map存放post时的数据
		Map<String, String> datas = new HashMap<String, String>();
		for (Element e : et.get(0).getAllElements()) {
			if (e.attr("name").equals("username")) {
				e.attr("value", "A510102581");// 设置用户名
				// System.out.println("填充NAME");
			}
			if (e.attr("name").equals("password")) {
				e.attr("value", "q4q4q4q4"); // 设置用户密码
				// System.out.println("填充password");
			}
			if (e.attr("name").length() > 0) {// 排除空值表单属性
				datas.put(e.attr("name"), e.attr("value"));
			}
		}
		/**
		 * * 第二次请求,提交登录请求,登录单点登录4A系统
		 **/
		Connection conSecond = Jsoup.connect(piccLoginUrl);
		conSecond.cookies(rs.cookies());
		conSecond.headers(headers);
		// 设置cookie和post上面的map数据
		Response rsLogin = null;
		try {
			rsLogin = conSecond.ignoreContentType(true).method(Method.POST).data(datas).cookies(rs.cookies()).execute();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		cookies = rsLogin.cookies();
	}
	//跳转到保单查询 得到cookie
	public static Object redirectBaodan() throws Exception
	{
		Connection conThired = Jsoup.connect(POLICYURL);
        conThired.header("User-Agent",
                "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
        conThired.cookies(cookies);
        serviceRes = conThired.execute();
        System.err.println("保单查询-点击跳转页面cookes :有"+serviceRes.cookies().size());
			cookies = serviceRes.cookies().size()==0?cookies:serviceRes.cookies();
			headers = serviceRes.headers().size()==0?headers:serviceRes.headers();
			headers.remove("Content-Length");

		//System.out.println(serviceRes.body());
		return serviceRes.body();
	}
//跳转到业务界面
    public static Object redirectService() throws Exception
    {
        Connection conThired = Jsoup.connect(piccBusiUrl);
        conThired.cookies(cookies);
        conThired.header("User-Agent",
                "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
        Response serviceRes = conThired.execute();
        System.err.println("主业务查询-点击跳转页面cookes :有"+serviceRes.cookies().size());
        cookies = serviceRes.cookies().size()==0?cookies:serviceRes.cookies();
        headers = serviceRes.headers().size()==0?headers:serviceRes.headers();
        headers.remove("Content-Length");
        //System.out.println(serviceRes.body());
        return serviceRes.body();
    }
	//页面设置值获取值方式试一试中文 结果是延时加载仔节点都找不到
	public static Object view(String licenseOr) throws Exception {
		
		Connection conThired = Jsoup.connect("http://10.134.138.16:8000/prpall/business/prepareEdit.do?bizType=PROPOSAL&editType=NEW");
		conThired.cookies(cookies);
		conThired.headers(headers);
		serviceRes = conThired.execute();
		Document d1 = Jsoup.parse(serviceRes.body());
		//queryBaseItemCarBtn
		
		Element et = d1.getElementById("licenseNoOrFrameNo");// 获取form表单，可以通过查看页面源码代码得知
		// 获取，cooking和表单属性，下面map存放post时的数据
		Map<String, String> dat = new HashMap<String, String>();
		et.attr("value", licenseOr);// 查询参数
		dat.put(et.attr("name"), et.attr("value"));
		
		Connection conSecond = Jsoup.connect("http://10.134.138.16:8000/prpall/vehicle/queryBaseItemCar.do");
		conSecond.cookies(cookies);
		conSecond.headers(headers);
		// 设置cookie和post上面的map数据
		Response rsQuery = null;
		try {
			rsQuery = conSecond.ignoreContentType(true).method(Method.POST).data(dat).cookies(cookies).execute();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.err.println(rsQuery.body());
		return rsQuery.body();
	}	

	public static Object queryVehicleByPrefillVIN(Map<String, String> map) throws Exception {
		//
		// http://10.134.138.16:8000/prpall/vehicle/queryVehicleByPrefillVIN.do?prpCitemCar.vinNo=LVSHCFAE1AF542622&prpCitemCar.licenseNo=å·ZL2123
		// &prpCitemCar.enrollDate=2010-08-23&prpCitemCar.engineNo=AA39491&prpCitemCar.carKindCode=A01&dmFlag=0
		StringBuffer buf = new StringBuffer(
				"http://10.134.138.16:8000/prpall/vehicle/queryVehicleByPrefillVIN.do?prpCitemCar.vinNo=");
		buf.append(new String(map.get("prpCitemCar.vinNo")))
				.append(new String(("&prpCitemCar.licenseNo=" + map.get("prpCitemCar.licenseNo")).getBytes("UTF-8"),
						"ISO8859-1"))
				.append("&prpCitemCar.enrollDate=" + new String(map.get("prpCitemCar.enrollDate")))
				.append("&prpCitemCar.engineNo=" + new String(map.get("prpCitemCar.engineNo")))
				.append("&prpCitemCar.carKindCode=" + new String(map.get("prpCitemCar.carKindCode")))
				.append("&dmFlag" + new String(map.get("dmFlag")));

		// Connection conThired
		// =Jsoup.connect("/prpall/vehicle/queryVehicleByPrefillVIN.do?prpCitemCar.vinNo=SALFA2BG5DH367098&prpCitemCar.licenseNo=��&prpCitemCar.enrollDate=&prpCitemCar.engineNo=&prpCitemCar.carKindCode=A01&dmFlag=0
		// HTTP/1.1");
		Connection conThired = Jsoup.connect(buf.toString());
		conThired.data(map);
		conThired.cookies(cookies);
		conThired.headers(headers);
		Document doc = conThired.post();
		String title = doc.title();
		System.out.println(title);
		return doc;
	}

	public static Object getDataByLicenseOrFrameNo(String str) throws Exception {
		// http://10.134.138.16:8000/prpall/vehicle/queryBaseItemCar.do?licenseNoOrFrameNo=å·ZL2123

		StringBuffer buf = new StringBuffer(
				"http://10.134.138.16:8000/prpall/vehicle/queryBaseItemCar.do?licenseNoOrFrameNo=");
		// buf.append(new String(str.getBytes("UTF-8"),"ISO8859-1"));
		// buf.append(str);
		buf.append(URLEncoder.encode(str));
		// buf.append(new String(str.getBytes("UTF-8"),"GBK"));

		Connection conThired = Jsoup.connect(buf.toString());

		conThired.headers(headers);
		conThired.cookies(cookies);

		Response doc = conThired.execute();
		String title = doc.body();
		System.out.println(title);

		/*
		 * Connection dwr=Jsoup.connect(buf.toString()); dwr.cookies(cookies);
		 * dwr.headers(headers); dwr.
		 * userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0"
		 * ); dwr.method(Method.POST); dwr.ignoreContentType(true);
		 * dwr.data("licenseNoOrFrameNo",new String(str.getBytes("UTF-8"),"ISO8859-1"));
		 * Response doc =dwr.execute();
		 */
		return doc.body();
	} //
		// prpCitemCar.vinNo: LVSHCFAE1AF542622

	public static Object vehicleQuery(Map<String, String> map) throws Exception {

		// 请求 URL:
		// http://10.134.138.16:8000/prpall/vehicle/vehicleQuery.do?brandName=CAF7180A48&modelCode=
		StringBuffer buf = new StringBuffer("http://10.134.138.16:8000/prpall/vehicle/vehicleQuery.do?brandName=");
		buf.append(new String(map.get("brandName"))).append("&modelCode" + new String(map.get("modelCode")));

		Connection conThired = Jsoup.connect(buf.toString());
		conThired.data(map);
		// cookies.put("dtCookie", "8192A83D3462CEDA0FBD0A2AC9AFAA0B|Y2FyM2d8MQ");
		// cookies.put("JSESSIONID",
		// "lBQYcGWTj5jnN02xBDdWlqZnQLJKV8CqYbQSyYHZgTJgQQWxKndQ!1428831953");
		// cookies.put("BOCINS_prpall_Cookie",
		// "XpZ4cGWCNfLCSJnJvlwRh49Rkjd7xm3v10TfNJrq284VyX3QS2nK!927179601");
		conThired.cookies(cookies);
		conThired.headers(headers);
		Document doc = conThired.post();
		// String title = doc.title();
		System.out.println(doc.html());
		return doc.html();
		/*
		 * Response doc = null; doc =
		 * conThired.execute().method(Connection.Method.POST); return doc.body();
		 */
	}

	/**
	 * http://10.134.138.16:8000/prpall/vehicle/queryVehicleByPrefillVIN.do?prpCitemCar.vinNo=LS4ASB3R7AG068777&prpCitemCar.licenseNo=å·&prpCitemCar.enrollDate=&prpCitemCar.engineNo=&dmFlag=0
	 * {"msg":"|此vin码没有对应的车辆信息。","totalRecords":0,"data":[]}
	 * {"msg":"LKAAJI0008,LKAAJI0001,LKAAJI0019,LKAAJI0020,LKAAJI0006,LKAAJI0003|","totalRecords":0,"data":[]}
	 * 
	 * @param vinNo
	 * @param licenseNo
	 * @param enrollDate
	 * @param engineNo
	 * @throws Exception
	 */
	public static Object qryCarsLicenseNo(String vinNo, String licenseNo, String enrollDate, String engineNo)
			throws Exception {

		StringBuffer url = new StringBuffer("http://10.134.138.16:8000/prpall/vehicle/queryVehicleByPrefillVIN.do?");
		url.append("prpCitemCar.vinNo=").append(vinNo).append("&prpCitemCar.licenseNo=")
				.append(new String(licenseNo.getBytes("UTF-8"), "ISO8859-1")).append("&prpCitemCar.enrollDate=")
				.append(enrollDate).append("&prpCitemCar.engineNo=").append(engineNo).append("&dmFlag=0");
		// StringBuffer url =new
		// StringBuffer("http://10.134.138.16:8000/prpall/vehicle/queryVehicleByPrefillVIN.do?prpCitemCar.vinNo=LS4ASB3R7AG068777&prpCitemCar.licenseNo=å·&prpCitemCar.enrollDate=&prpCitemCar.engineNo=&dmFlag=0");
		Connection con = Jsoup.connect(url.toString());
		con.cookies(cookies);
		con.header("User-Agent",
				"User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
		// con.header("User-Agent", "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64;
		// Trident/7.0; rv:11.0) like Gecko");
		Response rs = null;
		try {
			rs = con.execute();
		} catch (IOException e2) {
			e2.printStackTrace();
		} // 获取响应
			// String msg =rs.body();
		String msg = JSONObject.parseObject(rs.body()).getString("msg");
		if (!"|".equals(msg.indexOf(0))) {
			url = new StringBuffer("http://10.134.138.16:8000/prpall/vehicle/vehicleQuery.do?brandName=");
			url.append(msg).append("&modelCode=");
			con = Jsoup.connect(url.toString());
			con.cookies(cookies);
			con.header("User-Agent",
					"User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
			try {
				rs = con.execute();
			} catch (IOException e2) {
				e2.printStackTrace();
			} // 获取响应
			return rs.body();
		} else {
			return null;
		}
//		return rs.body();
	}

	public void disableSSLCertCheck() throws NoSuchAlgorithmException, KeyManagementException {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
//		SSLContext sc = SSLContext.getInstance("TLSv1.2");

		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	public static Document connect(Map<String, String> map, String url) throws Exception {
        /*Connection coOld = Jsoup.connect(piccBusiUrl);
        coOld.cookies(cookies);
        coOld.header("User-Agent",
                "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
        try {
            serviceRes = coOld.execute();
            cookies = serviceRes.cookies();
            headers = serviceRes.headers();
            headers.remove("Content-Length");

        } catch (IOException e2) {
            e2.printStackTrace();
        } // 获取响应*/
		// 请求 URL:
		// http://10.134.138.16:8000/prpall/vehicle/vehicleQuery.do?brandName=CAF7180A48&modelCode=
		StringBuffer buf = new StringBuffer(url);
		buf.append("?");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			buf.append(entry.getKey() + "=")
			.append(URLEncoder.encode(new String(entry.getValue()),"GB2312"))
			.append("&")
			;
		}
//
		  // 请求 URL: 
	//	Connection conThired = Jsoup.connect("http://10.134.138.16:8000/prpall/vehicle/queryBaseItemCar.do?"+(URLEncoder.encode("licenseNoOrFrameNo=川A7L72W","GB2312")));
		Connection conThired = Jsoup.connect(buf.toString());
		conThired.data(map);
		conThired.headers(headers);
		conThired.cookies(cookies);
		Document doc = conThired.post();
		System.out.println(doc.html());
		System.out.println(buf.toString());
		return doc;

	}


	public static Document connectPostAttr(Map<String, String> datamap,String url) throws Exception {
	//	data
	for (Map.Entry<String, String> entry : datamap.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			if("prpCpolicyVo.licenseNo".equals(entry.getKey())){
                datamap.put(entry.getKey(),URLEncoder.encode(new String(entry.getValue()),"GB2312"));
            }
		}
//
		// 请求 URL:
		Connection conThired = Jsoup.connect(url);
		//Connection conThired = Jsoup.connect("http://10.134.138.16:8000/prpall/business/selectPolicy.do?pageSize=10&pageNo=1");
        if(datamap!=null){
            for (Map.Entry<String, String> entry : datamap.entrySet()) {
                //添加参数
                conThired.data(entry.getKey(), entry.getValue());
            }
        }
        conThired.header("User-Agent",
                "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");

        conThired.cookies(cookies);
		Document doc = conThired.post();
//        System.out.println(doc.html());
		return  doc;
	}

	public static Response t2(Map<String, String> map, String url) throws Exception {
        Connection conSecond = Jsoup.connect(piccLoginUrl);
        conSecond.cookies(cookies);
        conSecond.headers(headers);
        // 设置cookie和post上面的map数据
        Response rsQuery = null;
        try {
            rsQuery = conSecond.ignoreContentType(true).method(Method.POST).data(map).cookies(cookies).execute();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        cookies = rsQuery.cookies();
        return rsQuery;
	}

	public static String utf8Togb2312(String str) {

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < str.length(); i++) {

			char c = str.charAt(i);

			switch (c) {
			case '+':
				sb.append(' ');
				break;
			case '%':
				try {
					sb.append((char) Integer.parseInt(str.substring(i + 1, i + 3), 16));
				}

				catch (NumberFormatException e) {
					throw new IllegalArgumentException();
				}
				i += 2;
				break;
			default:
				sb.append(c);
				break;
			}
		}
		String result = sb.toString();
		String res = null;
		try {
			byte[] inputBytes = result.getBytes("8859_1");
			res = new String(inputBytes, "UTF-8");
		} catch (Exception e) {
		}
		return res;

	}
	public static String stringToUnicode(String s) {
		try {
			StringBuffer out = new StringBuffer("");
			//直接获取字符串的unicode二进制
			byte[] bytes = s.getBytes("unicode");
			//然后将其byte转换成对应的16进制表示即可
			for (int i = 0; i < bytes.length - 1; i += 2) {
				out.append("\\u");
				String str = Integer.toHexString(bytes[i + 1] & 0xff);
				for (int j = str.length(); j < 2; j++) {
					out.append("0");
				}
				String str1 = Integer.toHexString(bytes[i] & 0xff);
				out.append(str1);
				out.append(str);
			}
			return out.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String utf8ToUnicode(String inStr) {
        char[] myBuffer = inStr.toCharArray();
        
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inStr.length(); i++) {
         UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
            if(ub == UnicodeBlock.BASIC_LATIN){
             //英文及数字等
             sb.append(myBuffer[i]);
            }else if(ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS){
             //全角半角字符
             int j = (int) myBuffer[i] - 65248;
             sb.append((char)j);
            }else{
             //汉字
             short s = (short) myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u"+hexS;
             sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }
}
