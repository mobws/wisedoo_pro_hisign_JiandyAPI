package com.wisedoo.base.error;

/**
 * @ClassName: CommonError
 * @Description: TODO
 * @Auther: liujn
 * @Date: 2019/3/27 3:27 PM
 * @Version 1.0
 */
public interface CommonError {

    public int getErrCode();

    public String getErrMsg();

    public CommonError setErrMsg(String errMsg);

}
