package com.wuwei.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.DateUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.jacob.activeX.ActiveXComponent;
import com.mysql.fabric.xmlrpc.base.Data;


public class Test {
	/*public static void main2(String[] args) throws Exception {
		//final ActiveXComponent activeXComponent2 = new ActiveXComponent("CLSID:4A2B7DF6-7E36-4A71-8169-D790C00DFCD3");
		//final ActiveXComponent activeXComponent = new ActiveXComponent("CLSID:100C2765-1362-4CCF-AB02-56D916BB8732");
		
		//final ActiveXComponent activeXComponent = new ActiveXComponent("CLSID:CE72892D-5FC9-4BF1-A09D-C5B59D4ECA64");
		Map<String,String> activeXObjectMap = new HashMap<String,String>();
		//activeXObjectMap.put("activeXComponent", "CLSID:100C2765-1362-4CCF-AB02-56D916BB8732");
		activeXObjectMap.put("activeXComponent2", "100C2765-1362-4CCF-AB02-56D916BB8732");
		WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER);
		 webClient.setActiveXObjectMap(activeXObjectMap);
		 webClient.getOptions().setCssEnabled(true);
	     webClient.getOptions().setJavaScriptEnabled(true);
	     webClient.getOptions().setActiveXNative(true);		//加载控件
	     webClient.getOptions().setUseInsecureSSL(true);
	     webClient.getCookieManager().setCookiesEnabled(true);// 开启cookie管理
	     webClient.getOptions().setTimeout(90000);
	     webClient.getOptions().setCssEnabled(true);
	     webClient.getOptions().setJavaScriptEnabled(true);
	     webClient.setJavaScriptTimeout(40000);
	     webClient.getOptions().setRedirectEnabled(true);
	     webClient.getOptions().setThrowExceptionOnScriptError(false);
	     webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	     webClient.setAjaxController(new NicelyResynchronizingAjaxController());
	     
	     Thread.sleep(3000);
		
		HtmlPage page = webClient.getPage("https://10.134.138.16:8888/casserver/login?service=http%3A%2F%2F10.134.138.16%3A80%2Fportal%2Findex.jsp");
		webClient.waitForBackgroundJavaScript(2000);
		
		System.out.println(System.getProperty("java.library.path"));
		
		HtmlForm form=page.getForms().get(0);
		HtmlTextInput  txtUName = (HtmlTextInput )form.getInputByName("user_name"); //用户名text框
		txtUName.setValueAttribute("chengdu472");
        HtmlPasswordInput txtPwd = (HtmlPasswordInput)form.getInputByName("pass");//密码框
        txtPwd.setValueAttribute("PICC2019");
		
		//System.out.println(System.getProperty("java.library.path"));
		
		//4A2B7DF6-7E36-4A71-8169-D790C00DFCD3
		
	}*/
	
	public static void main(String[] args) throws Exception {
		
		
		/*//String licenseno="��ZL2123";
		//String licenseno ="LVSHCFAE1AF542622";//"川ZL2123";
		String licenseno =new String("川ZL2123");//"川ZL2123";
		// String gblicen =new String(licenseno.getBytes("GB2312"));
		
		Map<String,String> vehmap = new HashMap<String,String>();
		vehmap.put("brandName", "CAF7180A48");
		vehmap.put("modelCode", "");
		
		
		Object obj;
		try {
			//车型	
			//obj =PIUnitUtil.vehicleQuery(vehmap);
			//检索
			obj = PIUnitUtil.getDataByLicenseOrFrameNo(licenseno);
			// vin
//			Map<String,String> vmap = new HashMap<String,String>();
//			vmap.put("prpCitemCar.vinNo", "LVSHCFAE1AF542622");
//			vmap.put("prpCitemCar.licenseNo", "川ZL2123");
//			vmap.put("prpCitemCar.enrollDate", "2010-08-23");
//			vmap.put("prpCitemCar.engineNo", "AA39491");
//			vmap.put("prpCitemCar.carKindCode", "A01");
//			vmap.put("dmFlag", "0");
			//obj =PIUnitUtil.queryVehicleByPrefillVIN(vmap);
//			obj=PIUnitUtil.t2(vmap, "http://10.134.138.16:8000/prpall/vehicle/queryVehicleByPrefillVIN.do");
			
			
			//通用方法测试
			Map<String,String> vmap = new HashMap<String,String>();
			vmap.put("editType", "RENEWAL");
			vmap.put("bizType", "PROPOSAL");
			vmap.put("bizNo", "PDAA201751010000878993");
			vmap.put("riskCode", "DAA");
			vmap.put("applyNo", "2018-12-08");
			vmap.put("endDate", "2019-12-07");
			vmap.put("startHour", "0");
			vmap.put("endHour", "24");
			vmap.put("endorType", "");
			vmap.put("taskID_Ppms", "");
			vmap.put("prpallLinkPpmsFlag", "");
			vmap.put("operateDate", "2018-12-03");
			vmap.put("rnd530", "Mon Dec 3 16:27:08 UTC+0800 2018");
			vmap.put("motorFastTrack", "");
			vmap.put("operatorProjectCode", "");
			vmap.put("reload", "");
			
			
//			obj=PIUnitUtil.test(vmap,"http://10.134.138.16:8000/prpall/business/editCinsured.do?"
//					+ "editType=RENEWAL&bizType=PROPOSAL&bizNo=PDAA201751010000878993&riskCode=DAA&applyNo="
//					+ "&startDate=2018-12-08&endDate=2019-12-07&startHour=0&endHour=24&endorType=&taskID_Ppms="
//					+ "&prpallLinkPpmsFlag=&operateDate=2018-12-03&motorFastTrack=&operatorProjectCode=&reload=&rnd530=Mon Dec 3 16:29:08 UTC+0800 2018");
			
			obj=PIUnitUtil.test(vmap, "http://10.134.138.16:8000/prpall/business/editCinsured.do");
			//obj=PIUnitUtil.t2(vehmap, "http://10.134.138.16:8000/prpall/vehicle/queryBaseItemCar.do");
			
//			Map<String,String> vthmap = new HashMap<String,String>();
//			vthmap.put("licenseNoOrFrameNo", "LVSHCFAE1AF542622");
//			
//			obj=PIUnitUtil.t2(vthmap, "http://10.134.138.16:8000/prpall/vehicle/queryBaseItemCar.do");
			
			System.out.println(obj.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
//		t1();
		//t();
//		getClientDetail();
		//PIUnitUtil.view("川ZL2123");
		Map<String,String> vmap = new HashMap<String,String>();
		vmap.put("licenseNoOrFrameNo", "川A7L72W");
//		vmap.put("licenseNoOrFrameNo", "LSGBC5342GG059567");
		Document obj=PIUnitUtil.connect(vmap, "http://10.134.138.16:8000/prpall/vehicle/queryBaseItemCar.do");
		System.out.println(obj.html());
	}
	public static void t1() throws Exception {
		Map<String,String> vehmap = new HashMap<String,String>();
		vehmap.put("licenseNoOrFrameNo", "LVSHCFAE1AF542622");
		
		StringBuffer buf = new StringBuffer("http://10.134.138.16:8000/prpall/vehicle/queryBaseItemCar.do?licenseNoOrFrameNo=LVSHCFAE1AF542622");


		Connection conThired = Jsoup.connect(buf.toString());
		vehmap.put("Accept", "*/*");
		vehmap.put("Accept-Encoding", "gzip, deflate");
		vehmap.put("Accept-Language", " zh-cn");
		vehmap.put("Cache-Control", "no-cache");
		vehmap.put("Connection", "Keep-Alive");
		vehmap.put("Cookie", "JSESSIONID=vRKWcF1T8HhsphyZHW2vT4sXnDKNFfjl3qYp2T0JKrzWy1L3wptj!-1886122569; BOCINS_prpall_Cookie=0kFtcF0Ts21D3TMStwQx39n965GfXRR6h229GTmp2sNGKGGm4qqy!2108879285");
		vehmap.put("Host", "10.134.138.16:8000");
		vehmap.put("Referer", "http://10.134.138.16:8000/prpall/business/prepareEdit.do?bizType=PROPOSAL&editType=NEW&&isEnterPrjectFlag=N&operatorProjectCode");
		vehmap.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
		vehmap.put("X-Requested-With", "XMLHttpRequest");
		

		conThired.headers(vehmap);
		Document doc = conThired.post();
		//String title = doc.title();
		System.out.println(doc.html());
		
	}
	public static void t() throws Exception {
		String licenseno="川";
		//String unicode = new String(licenseno.getBytes(), "UTF-8");
		//String gbk = new String(licenseno.getBytes("GB2312"));
		//String gbk = new String(licenseno.getBytes("ISO-8859-1"));
		//System.out.println(gbk);
		
//		String tempStr =  new String(licenseno.getBytes("UTF-8"),"ISO8859-1");
		String tempStr =  new String(licenseno.getBytes("UTF-8"),"UTF-8");
//		String tempStr =  new String(licenseno.getBytes("UTF-8"),"ISO8859-1");
//		String tempStr =  new String(licenseno.getBytes("UTF-8"),"ISO8859-1");
		System.out.println(tempStr);
	}
	public static void getClientDetail() throws Exception{
		Map<String,String> vmap = new HashMap<String,String>();
		
		Calendar now = Calendar.getInstance();  
		now.add(Calendar.MINUTE, -1);
		Date d = new Date(now.getTimeInMillis()-60000);
		System.out.println(DateUtils.formatDate(new Date(now.getTimeInMillis())));
		// 后端处理参数
		vmap.put("editType", "RENEWAL");
		vmap.put("riskCode", "DAA");
		vmap.put("endorType", "");
		vmap.put("taskID_Ppms", "");
		vmap.put("prpallLinkPpmsFlag", "");
		vmap.put("operateDate", d.toString());
		vmap.put("rnd530", DateUtils.formatDate(d , "EEE, MMM dd  HH:mm:ss z yyyy"));
		vmap.put("motorFastTrack", "");
		vmap.put("operatorProjectCode", "");
		vmap.put("reload", "");
		
		
		//调用传入参数
		vmap.put("bizType", "PROPOSAL");
		vmap.put("bizNo", "PDAA201751010000878993");
		vmap.put("applyNo", "2018-12-08");
		vmap.put("endDate", "2019-12-07");
		vmap.put("startHour", "0");
		vmap.put("endHour", "24");
		
		Document obj=PIUnitUtil.connect(vmap, "http://10.134.138.16:8000/prpall/business/editCinsured.do");
		System.out.println(obj.html());
	}
	
}
