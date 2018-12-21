package com.wuwei.controller;

import com.alibaba.fastjson.JSON;
import com.wuwei.entity.Student;
import com.wuwei.service.PIService;
import com.wuwei.util.HttpUtil;
import com.wuwei.util.Result;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器Controller
 * 请求地址：http://ip:port/contextPath/servletPath
 * 例如：http://localhost:8181/demo/student/getAllStudent
 *
 * @author 吴维
 * @date 2017-8-5 14:52:54
 */
//@CrossOrigin
@RestController
@RequestMapping("/yfsd")
public class PIController {

    @Autowired
    private PIService piService;

    //test
    @RequestMapping("/t")
    @ResponseBody
    public Object t() throws Exception {
        Student s = new Student();
        s.setName("ok");
        return  s;
    }

    @RequestMapping("/t1")
    @ResponseBody
    public Object t1() throws Exception {
         Map<String,String> sendmap= new HashMap<String,String>();
                    sendmap.put("accountType","PICC");sendmap.put("passWord","q4q4q4q4");
                    sendmap.put("userName","A510102581"); sendmap.put("licenseNo","川ZL2123");
                 //  String  res =HttpClient.sendPost("http://yun.yoyocars.net/safe/picc/result",sendmap);
        Map<String,String> sendmap2= new HashMap<String,String>();
        sendmap2.put("data",JSON.toJSONString(sendmap));
        String res = HttpUtil.doPost("http://yun.yoyocars.net/safe/picc/1", sendmap2);
       //String res = HttpUtil.doPost("http://125.71.37.179:8999/demo/yfsd/t",sendmap);
        return  res;
    }


    //车辆信息，vin/licensenum
    @RequestMapping("/queryBaseItemCar")
    @ResponseBody
    public Object queryBaseItemCar(String numStr) throws Exception {

        return  piService.queryBaseItemCar(numStr);
    }
    //个人信息
    @RequestMapping("/editCinsured")
    @ResponseBody
    public Result editCinsured(String bizType, String bizNo) throws Exception {

        return  piService.getClientDetail(bizType,bizNo);
    }


    //车牌号查保单记录
    @RequestMapping("/selectPolicy")
    @ResponseBody
    public Result selectPolicy(String vin,String pageSize,String pageNo,String licenseNo) throws Exception {
        return  piService.selectPolicy(vin,pageSize,pageNo,licenseNo);
    }
    /**
     * 通用接口
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/total")
    @ResponseBody
    public Object total(HttpServletRequest request) throws Exception {

        Map<String,String[]>  map=  request.getParameterMap();
        String url ="";
        Map<String,String>  mapattr= new HashMap<String,String>();

        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            if ("url".equals(entry.getKey() )) {
                url=entry.getValue()[0];
            }else {
                mapattr.put(entry.getKey(), entry.getValue()[0]);
            }
        }

        Document docu=null;
        docu=PIUnitUtil.connect(mapattr, url);
        return docu.html();
    }
}
