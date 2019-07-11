package com.wisedoo.jiandy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisedoo.base.controller.BaseController;
import com.wisedoo.base.response.CommonReturnType;
import com.wisedoo.jiandy.util.CommonJiandyAPIUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @ClassName: JdyController
 * @Description: TODO
 * @Auther: liujn
 * @Date: 2019/7/8 11:44 AM
 * @Version 1.0
 */

@Controller("jiandy")
@RequestMapping("/jiandy")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class JiandyController extends BaseController {

    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

    private String appId = "57c51fe335e9cdfa551da26d";
    private String entryId = "5d1042d154d4ec3085512da1";
    private String apiKey = "d8fz2jxcu7fwgofXegbnLU6lQcankSG2";


    @RequestMapping(value = "/data_create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    private CommonReturnType createSingleData(HttpServletRequest request) throws Exception {
        String appIdHader = request.getHeader("appId");
        if(null != appIdHader){
            appId = appIdHader;
        }

        String entryIdHader = request.getHeader("entryId");
        if(null != entryIdHader){
            entryId = entryIdHader;
        }

        String apiKeyHader = request.getHeader("apiKey");
        if(null != apiKeyHader){
            apiKey = apiKeyHader;
        }
        // 获取收到的报文
        BufferedReader reader = request.getReader();
        String line = "";
        StringBuffer inputString = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            inputString.append(line);
        }

        //转换数据
        String data = inputString.toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = (Map<String, Object>) mapper.readValue(data, Object.class);

        //调用接口
        CommonJiandyAPIUtil commonJiandyAPIUtil = new CommonJiandyAPIUtil(appId, entryId, apiKey);
        Object formData = commonJiandyAPIUtil.createData(result);
        CommonReturnType commonReturnType = CommonReturnType.create(formData);
        return commonReturnType;

    }

    @RequestMapping(value = "/data_creates", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    private CommonReturnType createDatas(HttpServletRequest request) throws Exception {
        String appIdHader = request.getHeader("appId");
        if(null != appIdHader){
            appId = appIdHader;
        }

        String entryIdHader = request.getHeader("entryId");
        if(null != entryIdHader){
            entryId = entryIdHader;
        }

        String apiKeyHader = request.getHeader("apiKey");
        if(null != apiKeyHader){
            apiKey = apiKeyHader;
        }

        // 获取收到的报文
        BufferedReader reader = request.getReader();
        String line = "";
        StringBuffer inputString = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            inputString.append(line);
        }

        //转换数据
        String data = inputString.toString();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> result = (ArrayList<Map<String, Object>>) mapper.readValue(data, Object.class);

        //调用接口
        CommonJiandyAPIUtil commonJiandyAPIUtil = new CommonJiandyAPIUtil(appId, entryId, apiKey);
        Object formData = commonJiandyAPIUtil.createDatas(result);
        CommonReturnType commonReturnType = CommonReturnType.create(formData);
        return commonReturnType;
    }


    @RequestMapping(value = "/data_query_all", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public CommonReturnType queryAllData(HttpServletRequest request) throws Exception {

        String appIdHader = request.getHeader("appId");
        if(null != appIdHader){
            appId = appIdHader;
        }

        String entryIdHader = request.getHeader("entryId");
        if(null != entryIdHader){
            entryId = entryIdHader;
        }

        String apiKeyHader = request.getHeader("apiKey");
        if(null != apiKeyHader){
            apiKey = apiKeyHader;
        }

        CommonJiandyAPIUtil commonJiandyAPIUtil = new CommonJiandyAPIUtil(appId, entryId, apiKey);
        Object formData = commonJiandyAPIUtil.getAllFormData(null, null);
        CommonReturnType commonReturnType = CommonReturnType.create(formData);
        return commonReturnType;
    }


}
