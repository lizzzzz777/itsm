package com.vlinksoft.ves.omcenter.controller;


import com.alibaba.fastjson.JSONObject;
import com.vlinksoft.ves.VesApp;
import com.vlinksoft.ves.dutymanage.api.IDutyUserApi;
import com.vlinksoft.ves.dutymanage.bo.UserDutyInfoVo;
import com.vlinksoft.ves.platform.web.vo.ILoginUser;
import com.vlinksoft.ves.workflow.um.workflow.bo.ProcessUserTaskPageQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping({"/vesapp/omcenter/UserDutyInfoVoAction"})
public class UserDutyInfoVoAction {
    @Resource
    private IDutyUserApi dutyUserApi;

    //值班情况
    @RequestMapping("findAllDutyInfoVo")
    @ResponseBody
    public JSONObject findAllDutyInfo(HttpServletRequest request, String startDate, String endDate) throws ParseException {
        //将字符串转为日期
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date statrTime = df.parse(startDate);
        Date endTime =  df.parse(endDate);
        //查询值班情况
        List<UserDutyInfoVo> userDutyInfoVo = dutyUserApi.summaryUserDutyInfoVo(statrTime,endTime);
        //转为JSON格式返回
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",userDutyInfoVo);
        jsonObject.put("size",userDutyInfoVo.size());
        return jsonObject;
    }

    @RequestMapping("findAllDutyInfoByDomainId")
    @ResponseBody
    public JSONObject findAllDutyInfoByDomainId(HttpServletRequest request, String startDate, String endDate,Long domainId) throws ParseException {
        //将字符串转为日期
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date statrTime = df.parse(startDate);
        Date endTime =  df.parse(endDate);
        //查询值班情况
        List<UserDutyInfoVo> userDutyInfoVo = dutyUserApi.findUserDutyInfoVoByDomainID(statrTime,endTime,domainId);
        //转为JSON格式返回
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",userDutyInfoVo);
        jsonObject.put("size",userDutyInfoVo.size());
        return jsonObject;
    }

    //点击获取更多我的值班
    @RequestMapping("getAllDutyInfoVo")
    @ResponseBody
    public JSONObject getAllDutyInfo(HttpServletRequest request, HttpSession session,String startDate, String endDate) throws ParseException {
        //获取当前登录用户
        ILoginUser iLoginUser = VesApp.getLoginUser(request);
        //将字符串转为日期
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date statrTime = df.parse(startDate);
        Date endTime =  df.parse(endDate);
        //查询值班情况
        List<UserDutyInfoVo> userDutyInfoVo = dutyUserApi.getSummaryUserDuty(iLoginUser.getId(),statrTime,endTime);
        //转为JSON格式返回
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",userDutyInfoVo);
        jsonObject.put("size",userDutyInfoVo.size());
        return jsonObject;
    }




}
