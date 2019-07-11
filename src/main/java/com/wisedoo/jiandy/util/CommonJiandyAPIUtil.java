package com.wisedoo.jiandy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CommonJiandyAPIUtil
 * @Description: TODO
 * @Auther: liujn
 * @Date: 2019/7/9 9:33 AM
 * @Version 1.0
 */
public class CommonJiandyAPIUtil {

    public static final String WEBSITE = "https://www.jiandaoyun.com";
    private String urlGetWidgets;
    private String urlGetFormData;
    private String urlRetrieveData;
    private String urlUpdateData;
    private String urlCreateData;
    private String urlDeleteData;
    private String apiKey;

    /**
     * @param appId   - 应用id
     * @param entryId - 表单id
     * @param apiKey  - apiKey
     */
    public CommonJiandyAPIUtil(String appId, String entryId, String apiKey) {
        this.apiKey = apiKey;
        this.initUrl(appId, entryId);
    }

    /**
     * @Description: 接口地址
     * @Param:
     * @Return:
     **/
    private void initUrl(String appId, String entryId) {
        urlGetWidgets = WEBSITE + "/api/v1/app/" + appId + "/entry/" + entryId + "/widgets";
        urlGetFormData = WEBSITE + "/api/v1/app/" + appId + "/entry/" + entryId + "/data";
        urlRetrieveData = WEBSITE + "/api/v1/app/" + appId + "/entry/" + entryId + "/data_retrieve";
        urlUpdateData = WEBSITE + "/api/v1/app/" + appId + "/entry/" + entryId + "/data_update";
        urlCreateData = WEBSITE + "/api/v1/app/" + appId + "/entry/" + entryId + "/data_create";
        urlDeleteData = WEBSITE + "/api/v1/app/" + appId + "/entry/" + entryId + "/data_delete";
    }

    /**
     * 获取请求头信息
     *
     * @return
     */
    public Header[] getHttpHeaders() {
        List<Header> headerList = new ArrayList<Header>();
        headerList.add(new BasicHeader("Authorization", "Bearer " + this.apiKey));
        headerList.add(new BasicHeader("Content-Type", "application/json;charset=utf-8"));
        return headerList.toArray(new Header[headerList.size()]);
    }

    /**
     * @Description: 请求接口
     * @Param: [uri, data]
     * @Return: java.lang.Object
     **/
    public Object sendRequest(String uri, Map<String, Object> data) throws Exception {
        ObjectMapper entityMapper = new ObjectMapper();
        String bodyData = entityMapper.writeValueAsString(data);
        Header[] headers = getHttpHeaders();
        CommonHttpUtil commonHttpUtil = new CommonHttpUtil();
        Object result = commonHttpUtil.doPost(uri, headers, bodyData);
        return result;
    }

    /**
     * 获取表单字段
     *
     * @return 表单字段
     */
    public List<Map<String, Object>> getFormWidgets() throws Exception {

        Map<String, Object> result = (Map<String, Object>) this.sendRequest(urlGetWidgets, new HashMap<String, Object>());
        List<Map<String, Object>> widgets = (List<Map<String, Object>>) result.get("widgets");
        return widgets;
    }

    /**
     * 按条件获取表单数据
     *
     * @param limit  - 数据条数
     * @param fields - 显示的字段
     * @param filter - 过滤条件
     * @param dataId - 上次取数的最后一个数据id
     * @return - 返回的数据
     */
    public List<Map<String, Object>> getFormData(final int limit, final String[] fields, final Map<String, Object> filter, String dataId) throws Exception {
        // 构造请求数据
        Map<String, Object> requestData = new HashMap<String, Object>();
        requestData.put("limit", limit);
        requestData.put("fields", fields);
        requestData.put("filter", filter);
        if (dataId != null) {
            requestData.put("data_id", dataId);
        }
        Map<String, Object> result = (Map<String, Object>) this.sendRequest(urlGetFormData, requestData);
        List<Map<String, Object>> data = (List<Map<String, Object>>) result.get("data");
        return data;
    }

    /**
     * 按条件获取全部表单数据
     *
     * @return 表单数据
     */
    public List<Map<String, Object>> getAllFormData(String[] fields, Map<String, Object> filter) throws Exception {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        String offset = null;
        do {
            List<Map<String, Object>> data = this.getFormData(100, fields, filter, offset);
            // 获取返回的数据
            if (data == null || data.isEmpty()) {
                // 已经获取全部的数据
                offset = null;
            } else {
                // 获取最后一条数据的id
                offset = (String) data.get(data.size() - 1).get("_id");
                dataList.addAll(data);
            }
        } while (offset != null);
        return dataList;
    }

    /**
     * 搜索单条数据
     *
     * @param dataId - 要查询的数据id
     * @return 表单数据
     */
    public Map<String, Object> retrieveData(String dataId) throws Exception {
        Map<String, Object> requestData = new HashMap<String, Object>();
        requestData.put("data_id", dataId);
        Map<String, Object> result = (Map<String, Object>) this.sendRequest(urlRetrieveData, requestData);
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        return data;
    }

    /**
     * 创建单条数据
     *
     * @param rawData - 创建数据内容
     * @return 更新后的数据
     */
    public Map<String, Object> createData(Map<String, Object> rawData) throws Exception {
        Map<String, Object> requestData = new HashMap<String, Object>();
        requestData.put("data", rawData);
        requestData.put("is_start_workflow", false);
        requestData.put("is_start_trigger", false);
        Map<String, Object> result = (Map<String, Object>) this.sendRequest(urlCreateData, requestData);
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        return data;
    }

    /**
     * @Author: liujn
     * @Description: 批量创建多条数据
     * @Date: 2019/7/9 10:24 AM
     * @Param: [datas]
     * @Return: java.util.List<java.util.Map<java.lang.String, java.lang.Object>>
     **/
    public List<Map<String, Object>> createDatas(List<Map<String, Object>> datas) throws Exception {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (null != datas && 0 < datas.size()) {
            for (Map<String, Object> rawData : datas) {
                Map<String, Object> data = createData(rawData);
                result.add(data);
            }
        }
        return result;
    }

    /**
     * 更新单条数据
     *
     * @return 更新结果
     */
    public Map<String, Object> updateData(String dataId, Map<String, Object> update) throws Exception {
        Map<String, Object> requestData = new HashMap<String, Object>();
        requestData.put("data_id", dataId);
        requestData.put("data", update);
        Map<String, Object> result = (Map<String, Object>) this.sendRequest(urlUpdateData, requestData);
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        return data;
    }

    /**
     * 删除单条数据
     *
     * @return 删除结果
     */
    public Map<String, String> deleteData(String dataId) throws Exception {
        Map<String, Object> requestData = new HashMap<String, Object>();
        requestData.put("data_id", dataId);
        Map<String, String> result = (Map<String, String>) this.sendRequest(urlDeleteData, requestData);
        return result;
    }


}
