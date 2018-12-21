package com.wuwei.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.URL;

public class PIHtmlunit {
	
	
	public static String ur;
	
	public static void main(String[] args) throws Exception {
		getSuccess();
	}
	public static Object getSuccess() throws Exception {
		WebClient webClient=new WebClient(BrowserVersion.INTERNET_EXPLORER_11); // 实例化Web客户端
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setActiveXNative(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.waitForBackgroundJavaScript(2 * 1000);//piccUrl
		//URL URL = new URL(PIUnitUtil.piccUrl);
		HtmlPage page = webClient.getPage(PIUnitUtil.piccUrl);
		System.out.println(page.asText());
		return null;
		
	}

}
