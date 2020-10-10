package com.vlinksoft.ves.omcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.vlinksoft.ves.platform.web.action.BaseAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/11/6.
 */
@Controller
@RequestMapping({"/vesapp/omcenter/stopservicemanage"})
public class StopServiceManageAction extends BaseAction{
    @RequestMapping({"list"})
    @ResponseBody
    public JSONObject list() {
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("state", 1);
            map.put("servicename", "内网网络");
            map.put("serviceprofile", "内部网络链接问题");
            map.put("serviceclass", "IT");
            list.add(map);
        }
        return toSuccess(list);
    }
}
