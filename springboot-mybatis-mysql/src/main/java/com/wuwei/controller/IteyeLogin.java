package com.wuwei.controller;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Jason.F
 * Date 2016年5月20日
 * **/

public class IteyeLogin {

			private String loginURL ="http://www.iteye.com/login";
			private String requestUrl = "http://www.iteye.com";
			private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0";
			private String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
	
			private static String piccUrl = "https://10.134.138.16:8888/casserver/login?service=http%3A%2F%2F10.134.138.16%3A80%2Fportal%2Findex.jsp";
			private static String piccLoginUrl = "https://10.134.138.16:8888//casserver/login?service=http%3A%2F%2F10.134.138.16%3A80%2Fportal%2Findex.jsp";
			private static String piccBusiUrl = "http://10.134.138.16:8000/prpall?calogin";
			private static String piccMethodUrl = "http://10.134.138.16:8000/prpall/business/prepareEdit.do?bizType=PROPOSAL&editType=NEW";
			
			private static String dwrUrl = "http://10.134.138.16:8000/prpall/business/caculatePremiunForFG.do";
			
			private static Map<String, String> cookies;
			private Map<String,String> headers;
			
			
			
		private void login(){
				//第一次请求
				Connection conFirst=Jsoup.connect(loginURL);
				//配置模拟浏览器
				conFirst.header("User-Agent", userAgent);
				conFirst.header("Accept",accept);
				conFirst.header("Accept-Encoding","gzip, deflate");
				conFirst.header("Accept-Language","zh-CN,zh;q=0.8");
				conFirst.header("Cache-Control","max-age=0");
				conFirst.header("Connection","keep-alive");
				conFirst.header("Content-Type","application/x-www-form-urlencoded");
				Response rs=null;
				try {
					rs = conFirst.execute();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
			e2.printStackTrace();
		}//获取响应
		Document d1=Jsoup.parse(rs.body());//转换为Dom树
		List<Element> et= d1.select("#login_form");//获取form表单，可以通过查看页面源码代码得知
		//获取，cooking和表单属性，下面map存放post时的数据
		Map<String, String> datas=new HashMap<String,String>();
		for(Element e:et.get(0).getAllElements()){
//			System.out.println(e.text());
			if(e.attr("name").equals("name")) {
				e.attr("value", "cbschina");//设置用户名
				System.out.println("填充NAME");
			}
			if(e.attr("name").equals("password")) {
				e.attr("value", "cbs8237212"); //设置用户密码
				System.out.println("填充password");
			}
			if(e.attr("name").length()>0){//排除空值表单属性
				datas.put(e.attr("name"), e.attr("value"));
			}
		}
		
		
		/**
		 * * 第二次请求，post表单数据，以及cookie信息
		 **/
		Connection conSecond=Jsoup.connect(loginURL);
		conSecond.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		//设置cookie和post上面的map数据
		Response rsLogin=null;
		try {
			rsLogin = conSecond.ignoreContentType(true).method(Method.POST).data(datas).cookies(rs.cookies()).execute();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//输出提交后html，看地址列表
		System.out.println(rsLogin.body());
		
		//登陆成功后的cookie信息，可以保存到本地，以后登陆时，只需一次登陆即可
		Map<String, String> cookies=rsLogin.cookies();
		for(String s:cookies.keySet()) {
			System.out.println(s + "      " + cookies.get(s));
		}
	}
	
	/**
	 * 调用登录调用，调用之前先禁用SSL
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public void testLogin() throws KeyManagementException, NoSuchAlgorithmException, IOException {
		headers = new HashMap<String, String>();
		headers.put("User-Agent", "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
		headers.put("Accept","text/html, application/xhtml+xml, image/jxr, */*");
		headers.put("Accept-Encoding","gzip, deflate");
		headers.put("Accept-Language","zh-CN");
		headers.put("Cache-Control","no-cache");
		headers.put("Connection","keep-alive");
		headers.put("Content-Length","292");
		headers.put("Content-Type","application/x-www-form-urlencoded");
		
//		SSLContext ctx = SSLContexts.custom().useProtocol("TLSv1.2").build();
//		CloseableHttpClient httpclient = HttpClientBuilder.create().setSslcontext(ctx).build();
//		httpclient.
		
		
		//第一次请求,获取COOKIES
	//	disableSSLCertCheck();
		Connection conFirst=Jsoup.connect(piccUrl).validateTLSCertificates(true).headers(headers);
		//配置模拟浏览器
//		conFirst.headers(headers);
		
		Response rs=null;
		try {
			rs = conFirst.execute();
		} catch (IOException e2) {
			e2.printStackTrace();
		}//获取响应
		
		///填充登录参数
		Document d1=Jsoup.parse(rs.body());//转换为Dom树
		List<Element> et= d1.select("#fm");//获取form表单，可以通过查看页面源码代码得知
		//获取，cooking和表单属性，下面map存放post时的数据
		Map<String, String> datas=new HashMap<String,String>();
		for(Element e:et.get(0).getAllElements()){
			if(e.attr("name").equals("username")) {
				e.attr("value", "A510102581");//设置用户名
//				System.out.println("填充NAME");
			}
			if(e.attr("name").equals("password")) {
				e.attr("value", "q3q3q3q3"); //设置用户密码
//				System.out.println("填充password");
			}
			if(e.attr("name").length()>0){//排除空值表单属性
				datas.put(e.attr("name"), e.attr("value"));
			}
		}
		
		
		/**
		 * * 第二次请求,提交登录请求,登录单点登录4A系统
		 **/
		Connection conSecond=Jsoup.connect(piccLoginUrl);
		conSecond.cookies(rs.cookies());
		conSecond.headers(headers);
		//设置cookie和post上面的map数据
		Response rsLogin=null;
		try {
			rsLogin = conSecond.ignoreContentType(true).method(Method.POST).data(datas).cookies(rs.cookies()).execute();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		cookies = rsLogin.cookies();
		
		/**
		 * 第三次登录,跳转业务系统
		 */
		Connection conThired=Jsoup.connect(piccBusiUrl);
		conThired.cookies(cookies);
		conThired.header("User-Agent", "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
		Response rs3=null;
		try {
			rs3 = conThired.execute();
		} catch (IOException e2) {
			e2.printStackTrace();
		}//获取响应
//		System.out.println(rs3.body());
		
		/**
		 * 第四次登录,跳转算价功能
		 */
		Connection conFourth =Jsoup.connect(piccMethodUrl);
		conFourth.cookies(cookies);
		conFourth.headers(headers);
		Response rs4 = null;
		try {
			rs4 = conFourth.execute();
		} catch (IOException e2) {
			e2.printStackTrace();
		}//获取响应
//		System.out.println(rs4.body());
		
		Document d4=Jsoup.parse(rs4.body());//转换为Dom树
		///设定基本信息
		Map<String, String> baseData=new HashMap<String,String>();
		////基础配置信息
		baseData.put(d4.getElementById("prpCmain.comCode").attr("name"),d4.getElementById("prpCmain.comCode").attr("value"));///归属部门
		baseData.put(d4.getElementById("prpCmain.handler1Code").attr("name"),d4.getElementById("prpCmain.handler1Code").attr("value"));///归属人
		baseData.put(d4.getElementById("prpCmain.handlerCode").attr("name"),d4.getElementById("prpCmain.handlerCode").attr("value"));////经办人
		baseData.put(d4.getElementById("prpCmain.businessNature").attr("name"),d4.getElementById("prpCmain.businessNature").attr("value"));	////业务来源
		baseData.put(d4.getElementById("businessNatureTranslation").attr("name"),d4.getElementById("businessNatureTranslation").attr("value"));////业务来源
		baseData.put(d4.getElementById("prpCmain.agentCode").attr("name"),d4.getElementById("prpCmain.agentCode").attr("value"));////渠道代码
		baseData.put(d4.getElementById("prpCmain.makeCom").attr("name"),d4.getElementById("prpCmain.makeCom").attr("value"));////出单机构
		baseData.put(d4.getElementById("prpCmainagentName").attr("name"),this.getCmainagentValue(d4.getElementById("prpCmain.agentCode").attr("value"),rs3.cookies()));///渠道代码,暂时未获取到数据
//		baseData.put(d4.getElementById("prpCitemCar.monopolyCode").attr("name"),d4.getElementById("prpCitemCar.monopolyCode").attr("value"));///推荐送修代码
		///前端输入
//		baseData.put(d4.getElementById("prpCitemCar.enrollDate").attr("name"),d4.getElementById("prpCitemCar.enrollDate").attr("value"));///初登日期




//		for(Map.Entry<String, String> entry :baseData.entrySet())
//		{
//			System.out.println(entry.getKey()+"|"+entry.getValue());
//		}
		
		
		
		
		/**
		 * prpCmain.startDate 商业险开始时间
		 * prpCmain.endDate  商业险结束时间
		 * prpCmainCI.startDate  交强险开始时间
		 * prpCmainCI.endDate 交强险结束时间
		 * 都从第二天 0  时开始生效,一年后24时失效
		 */
//		String vinNo = "LVSHCFME3AF592482";
//		String licenseNo = "";
//		String enrollDate = "";
//		String engineNo = "";

//		String carTypeInfoS = this.qryCarsLicenseNo(vinNo,licenseNo,enrollDate,engineNo);
//		if(StringUtils.isNotBlank(carTypeInfoS))////获取到车型信息，提供给前端处理
//		{
//			/**
//			 * 保单预留字段：
//			 *   priceT 新车价格,priceTr 类比车价格,vehicleName 车型名称,vehicleId 车型ID,vehicleExhaust 排量/功率(升) ,vehicleQuality 整备质量,vehicleClass 车辆种类,vehicleSeat 核定载客量
//			 */
//		}

//		String idCardNo = "51132519891107431X";
//		this.queryPersionInfo(idCardNo);
		
	
		
		/**
		 *  算价
		 **/
//		TestBean testBean = new TestBean();
//		AsyncHttpClientCallback  request = new AsyncHttpClientCallback();
//		try {
//			System.out.println("开始算价");
//			headers.remove("Content-Length");
//			Future returnBack  = HttpClientUtil.httpAsyncPost(dwrUrl,testBean.getParms(),cookies,headers,request);
//			HttpResponse httpResponse = (HttpResponse) returnBack.get();
//			request.completed(httpResponse);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		Connection dwr=Jsoup.connect("http://10.134.138.16:8000/prpall/business/editCmainTotal.do?editType=RENEWAL&bizType=PROPOSAL&bizNo=PDAA201751010000878993&riskCode=DAA&applyNo=&startDate=2018-12-08&endDate=2019-12-07&startHour=0&endHour=24&endorType=&taskID_Ppms=&prpallLinkPpmsFlag=&operateDate=2018-11-19&motorFastTrack=&operatorProjectCode=&reload=&rnd402=Mon Nov 19 11:44:17 UTC+0800 2018");
		dwr.cookies(cookies);
		dwr.headers(headers);
		dwr.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		dwr.method(Method.POST);
		dwr.ignoreContentType(true);
		Response rsDwr=null;
		System.out.println("开始"+Calendar.getInstance().getTime());
		try {
			rsDwr = dwr.execute();
			System.out.println(rsDwr.body());
		} catch (IOException e1) {
			System.out.println("结束"+Calendar.getInstance().getTime());
			e1.printStackTrace();
		}
		
//		MlsAsyncHttpClient.getAsyncHttpClient().post();
		
//		System.out.println(rsDwr.body());
		
		/**
		 *
		 * http://10.134.138.16:8000/prpall/vehicle/vehicleQuery.do?brandName=LKAAJI0008,LKAAJI0001,LKAAJI0019,LKAAJI0020,LKAAJI0006,LKAAJI0003&modelCode=
		 */
		
		
		
		////根据VIN / 车牌号查询车辆信息 http://10.134.138.16:8000/prpall/vehicle/queryVehicleByPrefillVIN.do?prpCitemCar.vinNo=LS4ASB3R7AG068777&prpCitemCar.licenseNo=å·&prpCitemCar.enrollDate=&prpCitemCar.engineNo=&dmFlag=0

		/**
		 *车辆采集查询界面查询接口：http://10.134.138.16:8000/prpall/ocr/ocrQuery.do?pageSize=5&pageNo=1
		 * post
		 bizType: 1
		 MaxIndex:
		 ocrData.engineNo: P41597
		 ocrData.identifyNumber:
		 ocrData.insuredName:
		 ocrData.licenseNo:
		 ocrData.operatorCode:
		 
		 响应:{"totalRecords":0,"data":[],"startIndex":1,"recordsReturned":5}
		 */
		
		/**
		 * 车型查询:http://10.134.138.16:8000/prpall/vehicle/vehi
		 * 		 brandName:
		 * 		 carShipTaxPlatFormFlag:
		 * 		 comCode: 51010403
		 * 		 pageNo_: 1
		 * 		 pageSize_: 10
		 * 		 pageSizeSelect: 10
		 * 		 pm_vehicle_switch:
		 * 		 quotationFlag:
		 * 		 riskCode: DAA
		 * 		 taxFlag: 0
		 * 		 TCVehicleVO.brandId:
		 * 		 TCVehicleVO.brandName: %B1%C8%D1%C7%B5%CF
		 * 		 TCVehicleVO.searchCode:
		 * 		 TCVehicleVO.vehicleAlias:
		 * 		 TCVehicleVO.vehicleId:
		 * 		 TCVehicleVO.vehicleName:
		 * 		 totalRecords_:
		 * 		 * 出参：cleQuery.do?pageSize=10&pageNo=1
		 * 入参：
		 *{"totalRecords":10,"data":[{"vehicleType":"1","vehicleClass":"旅行车类","enginePower":"118","vehicleName":"比亚迪QCJ6480M3J乘用车","fuelType":null,"engineType":"三菱4G69S4M","vehicleQuality":1.76,"vehicleSeat":7,"familyIdNew":"BYA0AH","vehicleAlias":"比亚迪M6 2.4L AT旗舰型","priceT":239800,"familyNameNew":"比亚迪M6","brandNameNew":"比亚迪","vehicleYear":"201007","powerTypeCode":"D1","vehicleExhaust":2.378,"priceTr":239800,"brandIdNew":"BYA0","brandId":"BYA","brandName":"比亚迪","vehicleClassPicc":"A01","vehicleId":"BYAAHD0007","vehicleTonnage":null,"groupName":"比亚迪M6(10/07-)","vehicleMakerId":"MK0080"},{"vehicleType":"1","vehicleClass":"轿车类","enginePower":"90","vehicleName":"比亚迪QCJ7183A4轿车","fuelType":null,"engineType":"比亚迪BYD483QA","vehicleQuality":1.205,"vehicleSeat":5,"familyIdNew":"BYA0AK","vehicleAlias":"比亚迪L3 1.8L CVT新锋畅版尊贵型","priceT":72800,"familyNameNew":"比亚迪L3","brandNameNew":"比亚迪","vehicleYear":"201109","powerTypeCode":"D1","vehicleExhaust":1.839,"priceTr":72800,"brandIdNew":"BYA0","brandId":"BYA","brandName":"比亚迪","vehicleClassPicc":"A01","vehicleId":"BYAAID0014","vehicleTonnage":null,"groupName":"比亚迪L3(10/08-)","vehicleMakerId":"MK0080"},{"vehicleType":"1","vehicleClass":"大型客车类","enginePower":"100","vehicleName":"比亚迪BYD6121LGEV3纯电动城市客车","fuelType":null,"engineType":"TZ2712XSB","vehicleQuality":12.8,"vehicleSeat":41,"familyIdNew":"BYA1AA","vehicleAlias":"","priceT":1700000,"familyNameNew":"比亚迪12米城市客车","brandNameNew":"比亚迪客车","vehicleYear":"","powerTypeCode":"D6","vehicleExhaust":null,"priceTr":1700000,"brandIdNew":"BYA1","brandId":"BYA","brandName":"比亚迪","vehicleClassPicc":"A01","vehicleId":"BYAAMD0009","vehicleTonnage":null,"groupName":"比亚迪BYD6121","vehicleMakerId":"MK03814"},{"vehicleType":"1","vehicleClass":"越野车类","enginePower":"103","vehicleName":"比亚迪QCJ6480S乘用车","fuelType":null,"engineType":"比亚迪BYD483QB","vehicleQuality":1.62,"vehicleSeat":5,"familyIdNew":"BYA0AL","vehicleAlias":"比亚迪S6 2.0L MT豪华型","priceT":74900,"familyNameNew":"比亚迪S6","brandNameNew":"比亚迪","vehicleYear":"201207","powerTypeCode":"D1","vehicleExhaust":1.991,"priceTr":74900,"brandIdNew":"BYA0","brandId":"BYA","brandName":"比亚迪","vehicleClassPicc":"A01","vehicleId":"BYAAJD0008","vehicleTonnage":null,"groupName":"比亚迪S6(11/05-)","vehicleMakerId":"MK0080"},{"vehicleType":"1","vehicleClass":"越野车类","enginePower":"123","vehicleName":"比亚迪QCJ6480S2乘用车","fuelType":null,"engineType":"比亚迪BYD488QA","vehicleQuality":1.665,"vehicleSeat":5,"familyIdNew":"BYA0AL","vehicleAlias":"比亚迪S6 2.4L MT精英型","priceT":94900,"familyNameNew":"比亚迪S6","brandNameNew":"比亚迪","vehicleYear":"201211","powerTypeCode":"D1","vehicleExhaust":2.362,"priceTr":94900,"brandIdNew":"BYA0","brandId":"BYA","brandName":"比亚迪","vehicleClassPicc":"A01","vehicleId":"BYAAJD0016","vehicleTonnage":null,"groupName":"比亚迪S6(11/05-)","vehicleMakerId":"MK0080"},{"vehicleType":"1","vehicleClass":"越野车类","enginePower":"151","vehicleName":"比亚迪BYD6481ST6多用途乘用车","fuelType":null,"engineType":"比亚迪BYD487ZQA","vehicleQuality":1.75,"vehicleSeat":7,"familyIdNew":"BYA0AT","vehicleAlias":"比亚迪S7 2.0T DCT豪华型","priceT":119900,"familyNameNew":"比亚迪S7","brandNameNew":"比亚迪","vehicleYear":"201410","powerTypeCode":"D1","vehicleExhaust":1.999,"priceTr":119900,"brandIdNew":"BYA0","brandId":"BYA","brandName":"比亚迪","vehicleClassPicc":"A01","vehicleId":"BYAASD0004","vehicleTonnage":null,"groupName":"比亚迪S7(14/10-)","vehicleMakerId":"MK0080"},{"vehicleType":"1","vehicleClass":"越野车类","enginePower":"151","vehicleName":"比亚迪BYD6481ST6D多用途乘用车","fuelType":null,"engineType":"比亚迪BYD487ZQA","vehicleQuality":1.75,"vehicleSeat":7,"familyIdNew":"BYA0AT","vehicleAlias":"比亚迪S7 2.0T DCT旗舰型","priceT":139900,"familyNameNew":"比亚迪S7","brandNameNew":"比亚迪","vehicleYear":"201410","powerTypeCode":"D1","vehicleExhaust":1.999,"priceTr":139900,"brandIdNew":"BYA0","brandId":"BYA","brandName":"比亚迪","vehicleClassPicc":"A01","vehicleId":"BYAASD0007","vehicleTonnage":null,"groupName":"比亚迪S7(14/10-)","vehicleMakerId":"MK0080"},{"vehicleType":"1","vehicleClass":"越野车类","enginePower":"151","vehicleName":"比亚迪BYD6481ST8多用途乘用车","fuelType":null,"engineType":"比亚迪BYD487ZQA","vehicleQuality":1.75,"vehicleSeat":7,"familyIdNew":"BYA0AT","vehicleAlias":"比亚迪S7 2.0T DCT豪华型","priceT":107900,"familyNameNew":"比亚迪S7","brandNameNew":"比亚迪","vehicleYear":"201505","powerTypeCode":"D1","vehicleExhaust":1.999,"priceTr":107900,"brandIdNew":"BYA0","brandId":"BYA","brandName":"比亚迪","vehicleClassPicc":"A01","vehicleId":"BYAASD0016","vehicleTonnage":null,"groupName":"比亚迪S7(14/10-)","vehicleMakerId":"MK0080"},{"vehicleType":"1","vehicleClass":"越野车类","enginePower":"151","vehicleName":"比亚迪BYD6481ST6D多用途乘用车","fuelType":null,"engineType":"比亚迪BYD487ZQA","vehicleQuality":1.75,"vehicleSeat":7,"familyIdNew":"BYA0AT","vehicleAlias":"比亚迪S7 2.0T DCT尊贵型","priceT":117900,"familyNameNew":"比亚迪S7","brandNameNew":"比亚迪","vehicleYear":"201505","powerTypeCode":"D1","vehicleExhaust":1.999,"priceTr":117900,"brandIdNew":"BYA0","brandId":"BYA","brandName":"比亚迪","vehicleClassPicc":"A01","vehicleId":"BYAASD0021","vehicleTonnage":null,"groupName":"比亚迪S7(14/10-)","vehicleMakerId":"MK0080"},{"vehicleType":"1","vehicleClass":"越野车类","enginePower":"151","vehicleName":"比亚迪BYD6481ST6多用途乘用车","fuelType":null,"engineType":"比亚迪BYD487ZQA","vehicleQuality":1.75,"vehicleSeat":7,"familyIdNew":"BYA0AT","vehicleAlias":"比亚迪S7 2.0T DCT旗舰型","priceT":127900,"familyNameNew":"比亚迪S7","brandNameNew":"比亚迪","vehicleYear":"201505","powerTypeCode":"D1","vehicleExhaust":1.999,"priceTr":127900,"brandIdNew":"BYA0","brandId":"BYA","brandName":"比亚迪","vehicleClassPicc":"A01","vehicleId":"BYAASD0023","vehicleTonnage":null,"groupName":"比亚迪S7(14/10-)","vehicleMakerId":"MK0080"}],"startIndex":1,"recordsReturned":10}
		 */
		
		/**
		 * 车型变动查询:
		 * http://10.134.138.16:8000/prpall/carInf/queryCarInfChange.do?vehicleIdCode=BYAAHD0007&vehicleIdAilias=
		 *
		 */
		
		
		/**
		 * 查询客户信息:
		 * http://10.134.138.16:8000/prpall/custom/customAmountQueryP.do?_identifyType=01&_insuredName=&_identifyNumber=510726198203070016&_insuredCode=&time=1536313041460
		 * {"totalRecords":1,"data":[{"mobile":"","insuredType":"","financeFlag":"0","mobileNoYG":"","email":"","address":"","nation":"","insuredCode":"5100100004761313","identifyNumber":"510726198203070016","postCode":"","countryCode":"CHN","insuredAddress":"","dateValidStart":null,"unitType":"","institution":"","dateValid":null,"repeatTimes":"","identifyType":"01","URL":"","identifyNumberLSJ":"","birthDate":{"date":7,"day":0,"year":82,"timezoneOffset":-480,"month":2,"hours":0,"seconds":0,"minutes":0,"time":384278400000,"nanos":0},"isCheckRepeat":"","versionNo":1,"age":"37","prpDcstLevelList":[],"phoneNumber":"","auditStatus":"2","resident":"A","groupCode":"","insuredName":"席真飞","phoneType":"","count":1,"sex":"1","configedRepeatTimes":""}]}
		 */
		/**
		 * 根据车牌查保单数据
		 * http://10.134.138.16:8000/prpall/business/selectRenewal.do?pageSize=10&pageNo=1
		 * 请求
		 prpCrenewalVo.engineNo:
		 prpCrenewalVo.frameNo:
		 prpCrenewalVo.licenseColorCode:
		 prpCrenewalVo.licenseNo: %B4%A8A41WZ2
		 prpCrenewalVo.licenseType: 02
		 prpCrenewalVo.othFlag:
		 prpCrenewalVo.policyNo:
		 prpCrenewalVo.vinNo:
		 validateCodeInput:
		 返回
		 {"totalRecords":4,"data":[{"frameNo":"JTHKR5BH3C2126990","accurateToMinute":"2017-12-11 24:00","lastDamagedBI":0,"noDamYearsCI":null,"policyNo":"PDAA201651010000727091","lastDamagedCI":null,"noDamYearsBI":2,"riskCode":"DAA","licenseNo":"川A41WZ2","engineNo":"5ZR5579650","comCode":"51019807","carKindCode":"客车"},{"frameNo":"JTHKR5BH3C2126990","accurateToMinute":"2018-12-27 24:00","lastDamagedBI":0,"noDamYearsCI":null,"policyNo":"PDAA201751010000942593","lastDamagedCI":null,"noDamYearsBI":0,"riskCode":"DAA","licenseNo":"川A41WZ2","engineNo":"5ZR5579650","comCode":"51010403","carKindCode":"客车"},{"frameNo":"JTHKR5BH3C2126990","accurateToMinute":"2017-12-11 24:00","lastDamagedBI":null,"noDamYearsCI":3,"policyNo":"PDZA201651010000880518","lastDamagedCI":0,"noDamYearsBI":null,"riskCode":"DZA","licenseNo":"川A41WZ2","engineNo":"5ZR5579650","comCode":"51019807","carKindCode":"客车"},{"frameNo":"JTHKR5BH3C2126990","accurateToMinute":"2018-12-27 24:00","lastDamagedBI":null,"noDamYearsCI":0,"policyNo":"PDZA201751010001076607","lastDamagedCI":0,"noDamYearsBI":null,"riskCode":"DZA","licenseNo":"川A41WZ2","engineNo":"5ZR5579650","comCode":"51010403","carKindCode":"客车"}],"startIndex":1,"recordsReturned":10}
		 */
		
		
		/**
		 *
		 *
		 * http://10.134.138.16:8000/prpall/business/queryTaxAbateForPlat.do?modelCode=LKAAJI0020&prpCitemCar.licenseType=02 &comCode=51010403&prpCitemCar.enrollDate=
		 *
		 * http://10.134.138.16:8000/prpall/vehicle/findVehicledetail.do?vehicleId=LKAAJI0008&taxFlag=0&quotationFlag=&purchasePrice=400300&vehiclePricer=400300
		 *
		 */
	
		
		
	}
	
	/**
	 * 根据用户身份证信息查询
	 * http://10.134.138.16:8000/prpall/custom/customAmountQueryP.do?_identifyType=01&_insuredName=&_identifyNumber=51132519891107431X&_insuredCode=&time=1536649398240
	 * @param id
	 * @return
	 */
	public String queryPersionInfo(String id)
	{
		StringBuffer url = new StringBuffer("http://10.134.138.16:8000/prpall/custom/customAmountQueryP.do?_identifyType=01&_insuredName=&_identifyNumber=");
		url.append(id).append("&_insuredCode=&time=").append(System.currentTimeMillis());
		Connection con =Jsoup.connect(url.toString());
		con.cookies(cookies);
		con.header("User-Agent", "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
		Response rs = null;
		try {
			rs = con.execute();
		} catch (IOException e2) {
			e2.printStackTrace();
		}//获取响应
		System.out.println(rs.body());
		
		return null;
	}
	
	
	/**
	 * 查询根据车辆车架号查询型号：
	 * http://10.134.138.16:8000/prpall/vehicle/queryVehicleByPrefillVIN.do?prpCitemCar.vinNo=JTHKR5BH3C2126990&prpCitemCar.licenseNo=å·A41WZ2&prpCitemCar.enrollDate=&prpCitemCar.engineNo=&dmFlag=0
	 * {"msg":"LKAAJI0008,LKAAJI0001,LKAAJI0019,LKAAJI0020,LKAAJI0006,LKAAJI0003|","totalRecords":0,"data":[]}
	 *
	 *
	 */
	
	/**
	 * http://10.134.138.16:8000/prpall/vehicle/queryVehicleByPrefillVIN.do?prpCitemCar.vinNo=LS4ASB3R7AG068777&prpCitemCar.licenseNo=å·&prpCitemCar.enrollDate=&prpCitemCar.engineNo=&dmFlag=0
	 * {"msg":"|此vin码没有对应的车辆信息。","totalRecords":0,"data":[]}
	 *{"msg":"LKAAJI0008,LKAAJI0001,LKAAJI0019,LKAAJI0020,LKAAJI0006,LKAAJI0003|","totalRecords":0,"data":[]}
	 * @param vinNo
	 * @param licenseNo
	 * @param enrollDate
	 * @param engineNo
	 */
	private String qryCarsLicenseNo(String vinNo, String licenseNo, String enrollDate, String engineNo) {
		StringBuffer url = new StringBuffer("http://10.134.138.16:8000/prpall/vehicle/queryVehicleByPrefillVIN.do?");
		url.append("prpCitemCar.vinNo=").append(vinNo).append("&prpCitemCar.licenseNo=").append(licenseNo)
				.append("&prpCitemCar.enrollDate=").append(enrollDate).append("&prpCitemCar.engineNo=").append(engineNo).append("&dmFlag=0");
		Connection con =Jsoup.connect(url.toString());
		con.cookies(cookies);
		con.header("User-Agent", "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
		Response rs = null;
		try {
			rs = con.execute();
		} catch (IOException e2) {
			e2.printStackTrace();
		}//获取响应
		
		String msg = JSONObject.parseObject(rs.body()).getString("msg");
		if (!"|".equals(msg.indexOf(0))) {
			url = new StringBuffer("http://10.134.138.16:8000/prpall/vehicle/vehicleQuery.do?brandName=");
			url.append(msg).append("&modelCode=");
			con =Jsoup.connect(url.toString());
			con.cookies(cookies);
			con.header("User-Agent", "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
			try {
				rs = con.execute();
			} catch (IOException e2) {
				e2.printStackTrace();
			}//获取响应
			return rs.body();
		}else{
			return null;
		}
	}
	
	/**
	 *根据品牌名称和型号查询车型信息
	 * @param brandName
	 * @param modelCode
	 * @return
	 */
	public String vehicleQuery(String brandName, String modelCode ) {
		StringBuffer url = new StringBuffer("http://10.134.138.16:8000/prpall/vehicle/vehicleQuery.do?brandName=");
		url.append(brandName).append("&modelCode=").append(modelCode);
		Connection con =Jsoup.connect(url.toString());
		con.cookies(cookies);
		con.header("User-Agent", "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
		Response rs = null;
		try {
			rs = con.execute();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		return rs.body();
	}
	
	/**
	 *
	 * @param value
	 * @param cookies
	 * @return
	 */
	private String getCmainagentValue(String value, Map<String, String> cookies) throws IOException {
		String valueUrl = "http://10.134.138.16:8000/prpall/common/changeCodeInput.do?actionType=query&fieldIndex=259&fieldValue="+value+"&codeMethod=change&codeType=select&codeRelation=0,1,2&isClear=Y&otherCondition=operateDate=2018-09-07,riskCode=DAA,comCode=51010403,businessNature=1&typeParam=&callBackMethod=callBack();MainTotal.checkOperatorAgentTypeCode();&getDataMethod=getAgents&_=";
		Connection con =Jsoup.connect(valueUrl);
		con.header("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
		con.cookies(cookies);
		Response rs = null;
		Document doc = null;
		try {
			rs = con.execute();
//			doc = con.get();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		if(StringUtils.isNotBlank(rs.body()))
		{
			return rs.body().split("_")[3];
		}else{
			return null;
		}
	}
	
	/**
	 * 禁用SSL
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private void disableSSLCertCheck() throws NoSuchAlgorithmException, KeyManagementException {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}
		};
		
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
	
	
	public static void main(String[] args) {
		IteyeLogin lession = new IteyeLogin();
		try {
			lession.testLogin();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 现在很多站点都是SSL对数据传输进行加密，这也让普通的HttpConnection无法正常的获取该页面的内容，
	 * 而Jsoup起初也对此没有做出相应的处理，
	 * 想了一下是否可以让Jsoup可以识别所有的SSL加密过的页面，查询了一些资料，发现可以为本地HttpsURLConnection配置一个“万能证书”，其原理是就是：
	 * 重置HttpsURLConnection的DefaultHostnameVerifier，使其对任意站点进行验证时都返回true
	 * 重置httpsURLConnection的DefaultSSLSocketFactory， 使其生成随机证书
	 * 后来Jsoup Connection提供了validateTLSCertificates(boolean validate)//是否进行TLS证书验证,不推荐
	 */
	static {
		try {
			// 重置HttpsURLConnection的DefaultHostnameVerifier，使其对任意站点进行验证时都返回true
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			// 创建随机证书生成工厂
			//SSLContext context = SSLContext.getInstance("TLS");
			SSLContext context = SSLContext.getInstance("TLSv1.2");
			context.init(null, new X509TrustManager[] { new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
				
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}
				
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} }, new SecureRandom());
			
			// 重置httpsURLConnection的DefaultSSLSocketFactory， 使其生成随机证书
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
