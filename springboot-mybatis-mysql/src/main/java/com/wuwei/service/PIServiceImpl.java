package com.wuwei.service;

import com.alibaba.fastjson.JSON;
import com.wuwei.controller.PIUnitUtil;
import com.wuwei.redis.RedisKeyEnum;
import com.wuwei.util.Result;
import com.wuwei.util.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 服务层Service
 *
 * @author
 * @date 2018-12-5 14:49:21
 */
@Service
public class PIServiceImpl implements PIService {

    private static final Logger logger = Logger.getLogger(PIServiceImpl.class.getName());
    @Autowired
    private RedisService redisService;

    @Override
    public Object queryVehicleByPrefillVIN(String vin) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object vehicleQuery(String brandName, String modelCode) throws Exception {
        // TODO Auto-generated method stub
        Map<String, String> vmap = new HashMap<String, String>();
        vmap.put("brandName", brandName);
        vmap.put("modelCode", modelCode);
        Document obj = PIUnitUtil.connect(vmap, "http://10.134.138.16:8000/prpall/vehicle/vehicleQuery.do");
        System.out.println(obj.html());

        return obj.html();
    }

    @Override
    public Object queryBaseItemCar(String numStr) throws Exception {
        Result result = new Result(ResultCode.COMMON_SUCCESS);
        String urleNumStr=URLEncoder.encode(numStr);
        String key = RedisKeyEnum.getName("car")+"_"+ urleNumStr;
        if(redisService.exists(key)){
            result.setCode(redisService.get(key).toString());
            return result;
        }
        Map<String, String> vmap = new HashMap<String, String>();
        vmap.put("licenseNoOrFrameNo", numStr);
        Document obj = PIUnitUtil.connect(vmap, "http://10.134.138.16:8000/prpall/vehicle/queryBaseItemCar.do");
        String resBody=obj.body().text();
        result.setCode(obj.body().text());
        //redis
        String resStr=resBody.substring(resBody.indexOf("["),resBody.indexOf("]"));
        if(resStr.length()>1){
            redisService.set(key,resBody,30*24*60*60);//30天
        }
        return result;

    }

    @Override
    public Result getClientDetail(String bizType, String bizNo) throws Exception {
        Result result = new Result(ResultCode.COMMON_SUCCESS);
        if (!StringUtils.isNotBlank(bizNo)) {
            result.setCode(ResultCode.COMMON_EXCEPTION);
            result.setDescription(bizNo + "为空");
            return result;
        }

        String key = RedisKeyEnum.getName("person")+"_"+ bizNo;
        if(redisService.exists(key)){
            result.setCode(redisService.get(key).toString());
            return result;
        }
        Map<String,String> vmap= new HashMap<String,String>();
        vmap.put("bizType", bizType); vmap.put("bizNo", bizNo);
        PIUnitUtil.redirectService();

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -1);
        Date d = new Date(now.getTimeInMillis() - 60000);
        System.out.println(DateUtils.formatDate(new Date(now.getTimeInMillis())));
        // 后端处理参数
        vmap.put("editType", "RENEWAL");
        vmap.put("riskCode", "DAA");
        vmap.put("endorType", "");
        vmap.put("taskID_Ppms", "");
        vmap.put("prpallLinkPpmsFlag", "");
        vmap.put("operateDate", d.toString());
        vmap.put("rnd530", DateUtils.formatDate(d, "EEE, MMM dd  HH:mm:ss z yyyy"));
        vmap.put("motorFastTrack", "");
        vmap.put("operatorProjectCode", "");
        vmap.put("reload", "");


		/*//调用传入参数
		vmap.put("bizType", "PROPOSAL");
		vmap.put("bizNo", "PDAA201751010000878993");
		vmap.put("applyNo", "2018-12-08");
		vmap.put("endDate", "2019-12-07");
		vmap.put("startHour", "0");
		vmap.put("endHour", "24");*/

        Document obj = PIUnitUtil.connect(vmap, "http://10.134.138.16:8000/prpall/business/editCinsured.do");
        System.out.println(obj.html());

        List<Element> et = obj.select("input[title]");
        Map<String, String> datas = new HashMap<String, String>();
        for (Element e : et) {
            datas.put(e.id(), e.attr("title"));
        }
        if(datas.size()!=0){
            redisService.set(key,JSON.toJSONString(datas),7*24*60*60);//7天
        }
        result.setCode(JSON.toJSONString(datas));


        return result;
    }


    /**
     * 保单查询
     *
     * @return
     */
    @Override
    public Result selectPolicy(String vin, String pageSize, String pageNo,String licenseNo) throws Exception {
        Map<String, String> vmap = new HashMap<String, String>();
        Result result = new Result(ResultCode.COMMON_SUCCESS);
        if (!StringUtils.isNotBlank(vin)) {
            result.setCode(ResultCode.COMMON_EXCEPTION);
            result.setDescription("查询参数为空");
            return result;
        }
        vmap.put("prpCpolicyVo.vinNo", vin);
        pageSize = pageSize == null ? "10" : pageSize;
        pageNo = pageNo == null ? "1" : pageNo;

        String key = RedisKeyEnum.getName("baodan")+"_"+ vin+"_"+ pageSize+"_"+ pageNo;
        if(redisService.exists(key)){
            result.setCode(redisService.get(key).toString());
            //让查保费先跑起来
            runSearchPrice(licenseNo);

            return result;
        }

        // 后端处理参数
        vmap.put("comCode", "51010403");
        vmap.put("queryMenuFlag", "2");
        vmap.put("isClickPrint", "1");
        vmap.put("isClickBrowse", "1");
        vmap.put("riskCode", "DAA");
        vmap.put("prpCpolicyVo.dmFlag", "all");
        vmap.put("NEWVISASWITCH", "0");
        vmap.put("searchConditionSwitch", "0");
        vmap.put("prpCpolicyVo.riskCode", "DAA,DZA");
        vmap.put("queryinterval", "01");
        vmap.put("electronicPolicyFlag", "1");

        vmap.put("prpCpolicyVo.licenseNo", "");
	/*vmap.put("prpCpolicyVo.policyNo", "");vmap.put("prpCpolicyVo.policyNo2", "");
	vmap.put("prpCpolicyVo.proposalNo", "");vmap.put("prpCpolicyVo.visaNo", "");vmap.put("prpCpolicyVo.printFlag", "");
	vmap.put("prpCpolicyVo.insuredName", "");vmap.put("prpCpolicyVo.contractNo", "");vmap.put("prpCpolicyVo.operateDate", "");
	vmap.put("prpCpolicyVo.operateDate2", "");vmap.put("prpCpolicyVo.startDate", "");vmap.put("prpCpolicyVo.startDate2", "");
	vmap.put("prpCpolicyVo.insuredCode", "");vmap.put("maxPrintNo", "");vmap.put("isDirectFee", "");
	vmap.put("prpCpolicyVo.brandName", "");vmap.put("prpCpolicyVo.engineNo", "");vmap.put("prpCpolicyVo.frameNo", "");
	vmap.put("prpCpolicyVo.appliCode", "");vmap.put("prpCpolicyVo.apliName", "");
	vmap.put("prpCpolicyVo.makeCom.", "");vmap.put("makeComDes", "");vmap.put("prpCpolicyVo.operatorCode", "");
	vmap.put("operatorCodeDes", "");vmap.put("prpCpolicyVo.comCode", "");vmap.put("comCodeDes", "");
	vmap.put("prpCpolicyVo.handlerCode", "");vmap.put("handlerCodeDes", "");vmap.put("prpCpolicyVo.handler1Code", "");
	vmap.put("handler1CodeDes", "");vmap.put("prpCpolicyVo.endDate", "");vmap.put("prpCpolicyVo.endDate2", "");
*/

        //点击得到新的保单查询cookie
        PIUnitUtil.redirectBaodan();
        StringBuffer buf = new StringBuffer("http://10.134.138.16:8000/prpall/business/selectPolicy.do");
        buf.append("?pageSize=").append(pageSize).append("&pageNo=" + pageNo);

        Document obj = PIUnitUtil.connectPostAttr(vmap, buf.toString());
        PIUnitUtil.redirectService();
        String bodystr = obj.select("body").text()
//			.replaceAll("code\":\\\"","code\\\":\\")
//			.replaceAll("\",\\\"reqId",",\\\"reqId")
//			.replaceAll("\"","")
                /*.replaceAll("\\\"","")*/;
        result.setCode(bodystr);
        if(bodystr.length()>8){
            redisService.set(key,bodystr,30*24*60*60);//30天

            //让查保费先跑起来
           runSearchPrice(licenseNo);
        }
        return result;
    }
    //查询报价
    public void runSearchPrice(String licenseNo){
        final String  str=licenseNo;
        //让查保费先跑起来
        /*Thread thread = new Thread(){
            public void run() {
                Map<String,String> sendmap= new HashMap<String,String>();
                sendmap.put("accountType","PICC");sendmap.put("passWord","q4q4q4q4");
                sendmap.put("userName","A510102581"); sendmap.put("licenseNo",str);
                Map<String,String> sendmap2= new HashMap<String,String>();
                sendmap2.put("data",JSON.toJSONString(sendmap));
                String res = HttpUtil.doPost("http://yun.yoyocars.net/safe/picc/result", sendmap2);
            }
        };
        thread.start();*/
    }

}
