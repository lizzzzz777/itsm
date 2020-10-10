package com.vlinksoft.ves.dutymanage.dao.impl;

import com.vlinksoft.ves.dutymanage.Vo.DutyMonthUser;
import com.vlinksoft.ves.dutymanage.Vo.DutyMonthUserQuery;
import com.vlinksoft.ves.dutymanage.bo.*;
import com.vlinksoft.ves.dutymanage.dao.DutyManageDao;
import com.vlinksoft.ves.platform.dao.BaseDao;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by admin on 2017/12/26.
 */
public class DutyManageDaoImpl extends BaseDao<DutyMain> implements DutyManageDao {

    @Autowired    //构造函数，写在最下面
    public DutyManageDaoImpl(@Qualifier(SESSION_DEFAULT) SqlSessionTemplate session) {
        super(session, DutyManageDao.class.getName());
    }

    @Override
    public void getDutyRecordByUID(Page<DutyMainUserVo, DutyMainUserVo> page) {
        this.select("getDutyRecordByUID",page);
    }

    @Override
    public List<Map<String, Object>> getDutyStatistics(DutyMonthUserQuery dutyMonthUserQuery) {
        return getSession().selectList("getDutyStatistics",dutyMonthUserQuery);
    }

    @Override
    public int interDutyMain(List<DutyMain> dutyMain) {
        return getSession().insert("interDutyMainInfo",dutyMain);
    }

    @Override
    public List<DutyTypeVo> getDutyType(DutyMain main) {
        Map<String,Object> map = new HashMap<>();
        map.put("list",main.getGetTypeId());
        return getSession().selectList("getDutyTypeList",map);
    }

    @Override
    public List<String> getUsersByTypeId(String typeId,String searchText) {
        Map<String,String> map=new HashMap<>();
        map.put("typeId",typeId);
        if(searchText==null||searchText.equals("")){
            map.put("searchText","%%");
        }else {
            map.put("searchText","%"+searchText+"%");
        }
        return getSession().selectList("getUsersList",map);
    }

    @Override
    public int setInUserInfo(DutyMainUserVo userVo) {
        return getSession().insert("setInUser",userVo);
    }

    @Override
    public int setDelUser(String id) {
        return getSession().delete("setDelUserById",id);
    }

    @Override
    public int insertToMain(DutyMain dutyMain) {
        return getSession().insert("insertToMainTable",dutyMain);
    }

    @Override
    public int insertToUser(List<DutyMainUserVo> userList) {
        return getSession().insert("insertToUserTable",userList);
    }

    @Override
    public List<DutyMain> selectDutyList(DutyTypeVo dutyTypeVo) {
        return getSession().selectList("selectDutyListInfo",dutyTypeVo);
    }

    @Override
    public List<DutyMain> selectAllDutyList(DutyTypeVo dutyTypeVo) {
        return getSession().selectList("selectAllDutyList",dutyTypeVo);
    }

    @Override
    public List<DutyMainUserVo> getDutyInfoByDutyDate(DutyTypeVo dutyTypeVo) {
        return getSession().selectList("getDutyInfoByDutyDate",dutyTypeVo);
    }

    @Override
    public int insertToMainLine(DutyMain dutyMain) {
        return getSession().insert("insertToMainLineInfo",dutyMain);
    }

    @Override
    public int insertToMainList(List<DutyMainUserVo> listVo) {
       /* return getSession().insert("insertToMainListInfo",listVo);*/
        return getSession().insert("insertToUserTable",listVo);
    }

    @Override
    public Date getMaxDate() {
        return getSession().selectOne("getMaxDateToMain");
    }

    @Override
    public String getMinDate() {
        return getSession().selectOne("getMinDateToMain");
    }

    @Override
    public int updateDutyState(DutyMainUserVo dutyMainUserVo) {
        return getSession().update("updateDutyStateToUser",dutyMainUserVo);
    }

    @Override
    public int delLineUser(List<Long> delIds) {
        StringBuffer str=new StringBuffer();
        for(Long id:delIds){
              str.append(id).append(",");
        }
        String ids=str.substring(0,str.length()-1);
        return getSession().delete("delLineUserByIds",ids);
    }

    @Override
    public int getMainUserInfo(String typeUserName, String dutyTypeId) {
        DutyUserVo dutyUserVo=new DutyUserVo();
        dutyUserVo.setTypeId(Long.valueOf(dutyTypeId));
        dutyUserVo.setUserName(typeUserName);
        return getSession().selectOne("getMainUserId",dutyUserVo);
    }

    @Override
    public int addMan(List<DutyMainUserVo> addMan) {
        return getSession().insert("addManInfo",addMan);
    }

    @Override
    public long checkByDutyDate(String shiftDate) {
        Long i=getSession().selectOne("checkByDutyDate",shiftDate);
        if(i==null){
            return 0;
        }else {
            return i;
        }

    }

    @Override
    public List<DutyMain> getDutyDateByUserId(Long userId,String startDate,String endDate) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        return getSession().selectList("getDutyDateByUserId",map);
    }

    @Override
    public List<DutyMainUserVo> getDutyUserByDate(String today) {
        return getSession().selectList("getDutyUserByDate",today);
    }

    @Override
    public List<DutyMain> getDutyMainByDate(String date) {
        return getSession().selectList("getDutyMainByDate",date);
    }

    @Override
    public List<DutyMonthUser> getUserDutyInfoList(Page page) {
        return getSession().selectList("getUserDutyInfoList",page);
    }

    @Override
    public int getTypeNumByUserId(long userId, int type) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        if(type == 1){
            map.put("dutyType","请假");
        }else if(type == 2){
            map.put("dutyType","换班");
        }
        SimpleDateFormat startDate = new SimpleDateFormat("yyyy-MM-01");
        SimpleDateFormat endDate = new SimpleDateFormat("yyyy-MM-31");
        map.put("startDate",startDate.format(new Date()));
        map.put("endDate",endDate.format(new Date()));
        return getSession().selectOne("getTypeNumByUserId",map);
    }

    @Override
    public int getHasDutyMonth(long userId, int dutyType) {
        Map<String,Object> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat startDate = new SimpleDateFormat("yyyy-MM-01");
        SimpleDateFormat endDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat startDate_ = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat endDate_ = new SimpleDateFormat("yyyy-MM-31");
        map.put("userId",userId);
        map.put("nowHour",calendar.get(Calendar.HOUR_OF_DAY));
        if(dutyType == 1){//已值
            map.put("startDate",startDate.format(calendar.getTime()));
            map.put("endDate",endDate.format(calendar.getTime()));
            return getSession().selectOne("getHasDutyMonth",map);
        }else if(dutyType == 2){//未值
            map.put("startDate",startDate_.format(calendar.getTime()));
            map.put("endDate",endDate_.format(calendar.getTime()));
            return getSession().selectOne("getUnDutyMonth",map);
        }else{
            return 0;
        }

    }

    @Override
    public List<DutyTypeVo> listAll() {
        return getSession().selectList("dutyTypeListAll");
    }

    @Override
    public List<DutyTypeVo> listAllByDomainId(String domainId) {
        Map<String,Object> map = new HashMap<>();
        map.put("domainId", StringUtils.isNotBlank(domainId)?Long.valueOf(domainId):null);
        return getSession().selectList("listAllByDomainId",map);
    }

    @Override
    public void insertDutyTypeRecord(List<DutyTypeVo> dutyTypeVos) {
        getSession().insert("insertDutyTypeRecord",dutyTypeVos);
    }

    @Override
    public List<DutyTypeVo> selectDutyTypeById(long dutyId) {
      return   getSession().selectList("selectDutyTypeById",dutyId);
    }

    @Override
    public List<DutyMainUserVo> selectDutyManById(long dutyId) {
        return getSession().selectList("selectDutyManById",dutyId);
    }

    @Override
    public List<DutyTypeVo> getDutyClassByTime(String shiftTime, long userId) {
        Map<String,Object> map=new HashMap<>();
        map.put("shiftTime",shiftTime);
        map.put("userId",userId);
        return getSession().selectList("getDutyClassByTime",map);
    }

    @Override
    public List<DutyTypeVo> getShiftDutyClassByTime(Map timeMap) {
        return getSession().selectList("getShiftDutyClassByTime",timeMap);
    }

    @Override
    public List<DutyMainUserVo> getShiftDutyClassByTimeAndDutyType(long shiftType, String time) throws ParseException {
        Map<String,Object> map=new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date timeFormate = df.parse(time);
        //设置开始时间和结束时间
        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.setTime(timeFormate);
        calendarFrom.set(Calendar.HOUR_OF_DAY, 0);//设置时为0点
        calendarFrom.set(Calendar.MINUTE, 0);//设置分钟为0分
        calendarFrom.set(Calendar.SECOND, 0);//设置秒为0秒
        calendarFrom.set(Calendar.MILLISECOND, 000);//设置毫秒为000
        Date dayStart = calendarFrom.getTime();//得到开始时间


        calendarFrom.set(Calendar.HOUR_OF_DAY,23);//设置时为23
        calendarFrom.set(Calendar.MINUTE,59);//设置分钟为59分
        calendarFrom.set(Calendar.SECOND,59);//设置秒为59秒
        calendarFrom.set(Calendar.MILLISECOND,999);//设置毫秒为999
        Date dayEnd = calendarFrom.getTime();//得到结束时间

        map.put("dayStart",dayStart);
        map.put("dayEnd",dayEnd);
        map.put("shiftType",shiftType);
        return getSession().selectList("getShiftDutyClassByTimeAndDutyType",map);
    }

    @Override
    public Long getLeaveDateByUserId(Long id, String startDate, String endDate) {
        Map map = new HashMap();
        map.put("userId",id);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        return getSession().selectOne("getLeaveDateByUserId",map);
    }

    @Override
    public void addDutyLog(DutyLog dutyLog) {
        getSession().insert(getNamespace()+"addDutyLog",dutyLog);
    }

    @Override
    public List<DutyLog> getDutyLogBydutyTime(String dutyTime,Long subDutyLogUserId) {
        Map map = new HashMap();
        map.put("dutyTime",dutyTime);
        map.put("subDutyLogUserId",subDutyLogUserId);
        return getSession().selectList(getNamespace()+"getDutyLogBydutyTime",map);
    }

    @Override
    public List<DutyLog> findDutyLog(Long dutyMainId, Long subDutyUserId) {
        Map map = new HashMap();
        map.put("dutyMainId",dutyMainId);
        map.put("subDutyUserId",subDutyUserId);
        return getSession().selectList(getNamespace()+"findDutyLog",map);
    }

    @Override
    public DutyLog findDutyLog(Long dutyMainId, Long dutyTypeId, Long subDutyUserId) {
        Map map = new HashMap();
        map.put("dutyMainId",dutyMainId);
        map.put("dutyTypeId",dutyTypeId);
        map.put("subDutyUserId",subDutyUserId);
        return getSession().selectOne(getNamespace()+"findNewDutyLog",map);
    }

    @Override
    public DutyLog findAdminDutyLog(Map<String,Object> map) {
        return getSession().selectOne(getNamespace()+"findAdminDutyLog",map);
    }

    @Override
    public void delDutyLogById(Long id) {
        getSession().delete(getNamespace()+"delDutyLogById",id);
    }

    @Override
    public void updateDutyLog(DutyLog dutyLog) {
        getSession().update(getNamespace()+"updateDutyLog",dutyLog);
    }

    @Override
    public List<DutyMain> findAllDutyList(DutyTypeVo dutyTypeVo) {
        return getSession().selectList(getNamespace()+"findAllDutyList",dutyTypeVo);
    }

    @Override
    public List<String> findUserByDomainId(String typeId, String searchText, Long domainId) {
        Map map = new HashMap();
        map.put("typeId",typeId);
        map.put("searchText",searchText);
        map.put("domainId",domainId);
        return getSession().selectList(getNamespace()+"findUserByDomainIdAndTypeId",map);
    }

    @Override
    public List<DutyMainUserVo> selectDutyManById(Long dutyId, Long domainId) {
        Map map = new HashMap();
        map.put("dutyId",dutyId);
        map.put("domainId",domainId);
        return getSession().selectList(getNamespace()+"findDutyManById",map);
    }

    @Override
    public List<DutyTypeVo> findAllDutyTypeByDomainId(Long domainId) {
        return getSession().selectList(getNamespace()+"findAllDutyTypeByDomainId",domainId);
    }

    @Override
    public Date getMaxDate(Long domainId) {
        return getSession().selectOne(getNamespace()+"getMaxDateByDomainId",domainId);
    }

    @Override
    public List<DutyTypeVo> selectDutyTypeById(Long dutyId, Long domainId) {
        Map map = new HashMap();
        map.put("dutyId",dutyId);
        map.put("domainId",domainId);
        return getSession().selectList(getNamespace()+"findDutyTypeClassByDomainId",map);
    }

    @Override
    public List<Map<String, Object>> getDutyStatistics(Date beginTime, Date endTime, String domainId) {
        Map map = new HashMap();
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        map.put("domainId",domainId);
        return getSession().selectList(getNamespace()+"getDutyStatisticsByDomainId",map);
    }

    @Override
    public int addWeek(List<DutyWeekUserVo> dutyWeekUserVo) {
       return getSession().insert(getNamespace()+"addWeek",dutyWeekUserVo);
    }

    @Override
    public List<DutyWeekUserVo> findWeek() {
        return getSession().selectList(getNamespace()+"findWeek");
    }

    @Override
    public List<DutyMainUserVo> findDutyMainUserListByListID(Long listId) {
        return getSession().selectList(getNamespace()+"findDutyMainUserListByListID",listId);
    }

    @Override
    public List<DutyMainUserVo> selectDutyMainUserListByListID(Long listId) {
        return getSession().selectList(getNamespace()+"selectDutyMainUserListByListID",listId);
    }

    @Override
    public List<DutyTypeVo> findDutyTypeById(Long dutyTypeId) {
        return getSession().selectList(getNamespace()+"findDutyTypeById",dutyTypeId);
    }

    @Override
    public List<Long> findDutyTime() {
        return getSession().selectList(getNamespace()+"findDutyTime");
    }

    @Override
    public List<DutyWeekUserVo> findWeekDuty(Long dutyId) {
        return getSession().selectList(getNamespace()+"findWeekDuty",dutyId);
    }

    @Override
    public int delWeekUser(List<Long> delIds) {
        StringBuffer str=new StringBuffer();
        for(Long id:delIds){
            str.append(id).append(",");
        }
        String ids=str.substring(0,str.length()-1);
        return getSession().delete(getNamespace()+"delWeekUserByIds",ids);
    }

    @Override
    public int delMainUserByIds(List<Long> delIds) {
        StringBuffer str=new StringBuffer();
        for(Long id:delIds){
            str.append(id).append(",");
        }
        String ids=str.substring(0,str.length()-1);
        return getSession().delete(getNamespace()+"delMainUserByIds",ids);
    }

    @Override
    public int addMans(List<DutyWeekUserVo> addMan) {
        return getSession().insert(getNamespace()+"addMans",addMan);
    }

    @Override
    public int addDutyMans(List<DutyMainUserVo> addMan) {
        return getSession().insert(getNamespace()+"addDutyMans",addMan);
    }

    @Override
    public List<DutyTypeUserVo> findTypeUser(Long typeId,String searchText) {
        Map map = new HashMap();
        map.put("typeId",typeId);
        if(searchText==null||searchText.equals("")){
            map.put("searchText","%%");
        }else {
            map.put("searchText","%"+searchText+"%");
        }
        return getSession().selectList(getNamespace()+"findTypeUser",map);
    }

    @Override
    public List<DutyMainUserVo> findDutyMainUserByTypeId(Long typeId) {
        return getSession().selectList("findDutyMainUserByTypeId",typeId);
    }

    @Override
    public void updateDutyMainUser(DutyMainUserVo dutyMainUserVo) {
        this.getSession().update("updateDutyMainUser",dutyMainUserVo);
    }

    @Override
    public int delWeekUsers() {
        return getSession().delete(getNamespace()+"delWeekUsers");
    }

    @Override
    public List<Long> findMinId() {
        return getSession().selectList(getNamespace()+"findMinId");
    }

    @Override
    public Date findDesc() {
        return getSession().selectOne(getNamespace()+"findDesc");
    }

    @Override
    public Date findAsc() {
        return getSession().selectOne(getNamespace()+"findAsc");
    }

    @Override
    public List<DutyMain> findDutyMainByListId(Long listId) {
        return getSession().selectList("findDutyMainByListId",listId);
    }

    @Override
    public List<DutyMain> findDutyMainByListId(Map<String,Object> map) {
        return getSession().selectList("findDutyMainByListIdByDate",map);
    }

    @Override
    public List<DutyMainUserVo> findDutyMainUserListByDutyId(Long dutyId) {
        return getSession().selectList("findDutyMainUserListByDutyId",dutyId);
    }

    @Override
    public void deleteDutyMainByDate(Map<String,Object> map) {
        getSession().delete("deleteDutyMainByDate",map);
    }

    @Override
    public void deleteDutyMainByListId(Long listId) {
        getSession().delete("deleteDutyMainByListId",listId);
    }

    @Override
    public void deleteRecordByDutyId(Long dutyId) {
        getSession().delete("deleteRecordByDutyId",dutyId);
    }

    @Override
    public List<DutyMainUserVo> findDutyMainUserListByDate(Map<String, Object> map) {
        return getSession().selectList("findDutyMainUserListByDate",map);
    }

    @Override
    public List<DutyMain> getAllDutyDateByDomainId(Map<String, Object> map) {
        return getSession().selectList("getAllDutyDateByDomainId",map);
    }

    @Override
    public void upDateDutyUserContactsInfo(DutyMainUserVo dutyMainUserVo) {
        getSession().update("upDateDutyUserContactsInfo",dutyMainUserVo);
    }



    @Override
    public void getMyDutyPage(Page<DutyMainUserVo, DutyMainUserQo> page) {
        getSession().selectList("getMyDutyPage",page);
    }

    @Override
    public void updateDutyUserStatus(DutyMainUserVo dutyMainUserVo) {
        getSession().update("updateDutyUserStatus",dutyMainUserVo);
    }

    @Override
    public List<DutyLog> getLogListByDutyIdAndTypeId(Map<String, Object> map) {
        return getSession().selectList("getLogListByDutyIdAndTypeId",map);
    }

    @Override
    public List<DutyTypeJobVo> findDutyMainTypeListByCDate(String currentDate) {
        return getSession().selectList("findDutyMainTypeListByCDate",currentDate);
    }

    @Override
    public List<DutyMainUserVo> findUserListByDutyIdAndTypeId(Map<String, Object> map) {
        return getSession().selectList("findUserListByDutyIdAndTypeId",map);
    }

    @Override
    public void batchOffDutyList(Map<String, Object> map) {
        getSession().update("batchOffDutyList",map);
    }

    @Override
    public void updateDutyLogByDutyTypeUser(DutyLog dutyLog) {
        getSession().update("updateDutyLogByDutyTypeUser",dutyLog);
    }

    @Override
    public List<DutyMainUserVo> queryDutyMainUser(Page<DutyMainUserPageVo, DutyMainUserPageVo> page) {
        return getSession().selectList("queryDutyMainUser",page);
    }
}
