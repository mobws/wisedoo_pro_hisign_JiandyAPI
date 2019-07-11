package com.wisedoo.base.controller;

import com.wisedoo.base.error.BusinessException;
import com.wisedoo.base.error.EmBusinessError;
import com.wisedoo.base.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: BaseController
 * @Description: TODO
 * @Auther: liujn
 * @Date: 2019/3/27 4:20 PM
 * @Version 1.0
 */
public class BaseController {

    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";


    /**
     * @Author: liujn
     * @Description: 定义exceptionHandler 解决未被controller层吸收的Exception
     * @Date: 2019/3/27 4:16 PM
     * @Param: [request, ex]
     * @Return: java.lang.Object
     **/
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex) {
        Map<String, Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;

            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        } else {
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }

        return CommonReturnType.create(responseData, "fail");
    }

}
