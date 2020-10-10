package com.vlinksoft.ves.dutymanage.service;

import com.vlinksoft.ves.dutymanage.Vo.DutyMonthUser;
import com.vlinksoft.ves.dutymanage.Vo.DutyMonthUserQuery;
import com.vlinksoft.ves.dutymanage.api.IDutyManageApi;
import com.vlinksoft.ves.dutymanage.bo.*;
import com.vlinksoft.ves.dutymanage.dao.DutyManageDao;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.sequence.service.ISequence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by admin on 2017/12/26.
 */
public class DutyManageApiImpl implements IDutyManageApi {
    private DutyManageDao dutyManageDao;
    private ISequence seq;

    public DutyManageDao getDutyManageDao() {
        return dutyManageDao;
    }

    public void setDutyManageDao(DutyManageDao dutyManageDao) {
        this.dutyManageDao = dutyManageDao;
    }

    public ISequence getSeq() {
        return seq;
    }

    public void setSeq(ISequence seq) {
        this.seq = seq;
    }

    @Override
    public void getDutyRecordByUID(Page<DutyMainUserVo, DutyMainUserVo> page) {
        dutyManageDao.getDutyRecordByUID(page);
    }

    @Override
    public List<DutyMain> interDutyMain(List<DutyMain> dutyMain) {
        for(DutyMain duty:dutyMain){
            duty.setDutyId(seq.next());
        }
        List<DutyMain> returnArray=dutyMain;
        this.dutyManageDao.interDutyMain(dutyMain);
        return returnArray;
    }

    @Override
    public List<DutyTypeVo> getDutyType(DutyMain main) {
        return dutyManageDao.getDutyType(main);
    }

    @Override
    public List<String> getUsersByTypeId(String typeId,String searchText) {
        return dutyManageDao.getUsersByTypeId(typeId,searchText);
    }

    @Override
    public int setInUserInfo(DutyMainUserVo userVo) {

            userVo.setId(seq.next());
        appendEndTimeToDate(userVo);
        return dutyManageDao.setInUserInfo(userVo);
    }

    @Override
    public int setDelUser(String userId) {
        return dutyManageDao.setDelUser(userId);
    }

    @Override
    public int insertToMain(DutyMain dutyMain) {
        long dutyId=seq.next();
        dutyMain.setDutyId(dutyId);
        dutyManageDao.insertToMain(dutyMain);
        return (int)dutyId;

    }

    @Override
    public int insertToUser(List<DutyMainUserVo> userList) {
        for(int i=0;i<userList.size();i++){
            userList.get(i).setId(seq.next());
            appendEndTimeToDate(userList.get(i));
        }
        return dutyManageDao.insertToUser(userList);
    }

    @Override
    public List<DutyMain> selectDutyList(DutyTypeVo dutyTypeVo) {
        return dutyManageDao.selectDutyList(dutyTypeVo);
    }

    @Override
    public List<DutyMain> selectAllDutyList(DutyTypeVo dutyTypeVo) {
        return dutyManageDao.selectAllDutyList(dutyTypeVo);
    }

    @Override
    public List<DutyMainUserVo> getDutyInfoByDutyDate(DutyTypeVo dutyTypeVo) {
        return dutyManageDao.getDutyInfoByDutyDate(dutyTypeVo);
    }

    @Override
    public int insertToMainLine(DutyMain dutyMain) {
        long dutyId=seq.next();
        dutyMain.setDutyId(dutyId);
        dutyManageDao.insertToMainLine(dutyMain);
        return (int) dutyId;
    }

    @Override
    public int insertToMainList(List<DutyMainUserVo> listVo) {
        for (int i=0;i<listVo.size();i++){
            listVo.get(i).setId(seq.next());
            appendEndTimeToDate(listVo.get(i));
        }
        return dutyManageDao.insertToMainList(listVo);
    }

    @Override
    public Date getMaxDate() {
        Date d=this.dutyManageDao.getMaxDate();
        return d;
    }

    @Override
    public String getMinDate() {

        return this.dutyManageDao.getMinDate();
    }

    @Override
    public int updateDutyState(DutyMainUserVo dutyMainUserVo) {
        long state=dutyMainUserVo.getDutyState();
        dutyManageDao.updateDutyState(dutyMainUserVo);
        return (int)state;
    }

    private void appendEndTimeToDate(DutyMainUserVo dutyMainUserVo){
        Calendar c = Calendar.getInstance();
        c.setTime(dutyMainUserVo.getDutyDate());
        c.set(Calendar.HOUR_OF_DAY,dutyMainUserVo.getEndTime());
        dutyMainUserVo.setDutyDate(c.getTime());
    }
    private void appendEndTimeToDate1(DutyWeekUserVo dutyWeekUserVo){
        Calendar c = Calendar.getInstance();
        c.setTime(dutyWeekUserVo.getDutyDate());
        c.set(Calendar.HOUR_OF_DAY,dutyWeekUserVo.getOverTime());
        dutyWeekUserVo.setDutyDate(c.getTime());
    }

    private void appendEndTimeToDate1(DutyMainUserVo dutyWeekUserVo){
        Calendar c = Calendar.getInstance();
        c.setTime(dutyWeekUserVo.getDutyDate());
        c.set(Calendar.HOUR_OF_DAY,dutyWeekUserVo.getEndTime());
        dutyWeekUserVo.setDutyDate(c.getTime());
    }


    @Override
    public int delLineUser(List<Long> delIds) {
        return dutyManageDao.delLineUser(delIds);
    }

    @Override
    public int getMainUserInfo(String typeUserName, String dutyTypeId) {
        return dutyManageDao.getMainUserInfo(typeUserName,dutyTypeId);
    }
    @Override
    public int addMan(List<DutyMainUserVo> addMan) {
        for (DutyMainUserVo duty:addMan){
            duty.setId(seq.next());
            appendEndTimeToDate(duty);
        }
        return dutyManageDao.addMan(addMan);
    }

    @Override
    public long checkByDutyDate(String shiftDate) {
        return dutyManageDao.checkByDutyDate(shiftDate);
    }

    @Override
    public List<DutyMain> getDutyDateByUserId(Long id,String startDate,String endDate) {
        return dutyManageDao.getDutyDateByUserId(id,startDate,endDate);
    }

    @Override
    public List<DutyMainUserVo> getDutyUserByDate(String today) {
        return dutyManageDao.getDutyUserByDate(today);
    }

    @Override
    public List<DutyMain> getDutyMainByDate(String date) {
        return dutyManageDao.getDutyMainByDate(date);
    }

    @Override
    public List<DutyMonthUser> getUserDutyInfoList(Page page) {
        return dutyManageDao.getUserDutyInfoList(page);
    }

    @Override
    public int getTypeNumByUserId(long userId, int type) {
        return dutyManageDao.getTypeNumByUserId(userId,type);
    }

    @Override
    public int getHasDutyMonth(long userId, int dutyType) {
        return dutyManageDao.getHasDutyMonth(userId,dutyType);
    }

    @Override
    public List<DutyTypeVo> listAll() {
        return dutyManageDao.listAll();
    }

    @Override
    public List<DutyTypeVo> listAllByDomainId(String  domainId) {
        return dutyManageDao.listAllByDomainId(domainId);
    }

    @Override
    public void insertDutyTypeRecord(List<DutyTypeVo> dutyTypeVos) {
        dutyManageDao.insertDutyTypeRecord(dutyTypeVos);
    }

    @Override
    public List<DutyTypeVo> selectDutyTypeById(long dutyId) {
        return dutyManageDao.selectDutyTypeById(dutyId);
    }

    @Override
    public List<DutyMainUserVo> selectDutyManById(long dutyId) {
        return dutyManageDao.selectDutyManById(dutyId);
    }

    @Override
    public List<DutyTypeVo> getDutyClassByTime(String shiftTime, long userId) {
        return dutyManageDao.getDutyClassByTime(shiftTime,userId);
    }
    //通过时间获取班次
    @Override
    public List<DutyTypeVo> getShiftDutyClassByTime(String time) throws ParseException {
        //将字符串格式化为时间类型
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
        System.err.println("开始:"+dayStart);

        calendarFrom.set(Calendar.HOUR_OF_DAY,23);//设置时为23
        calendarFrom.set(Calendar.MINUTE,59);//设置分钟为59分
        calendarFrom.set(Calendar.SECOND,59);//设置秒为59秒
        calendarFrom.set(Calendar.MILLISECOND,999);//设置毫秒为999
        Date dayEnd = calendarFrom.getTime();//得到结束时间
        System.err.println("结束:"+dayEnd);
        Map timeMap = new HashMap();
        timeMap.put("dayStart",dayStart);
        timeMap.put("dayEnd",dayEnd);
        return dutyManageDao.getShiftDutyClassByTime(timeMap);
    }

    @Override
    public List<DutyMainUserVo> getShiftDutyClassByTimeAndDutyType(long shiftType, String time) throws ParseException {
        return dutyManageDao.getShiftDutyClassByTimeAndDutyType(shiftType,time);
    }

    @Override
    public List<Map<String, Object>> getDutyStatistics(DutyMonthUserQuery dutyMonthUserQuery) {
        return dutyManageDao.getDutyStatistics(dutyMonthUserQuery);
    }

    @Override
    public Long getLeaveDateByUserId(Long id, String startDate, String endDate) {
        return dutyManageDao.getLeaveDateByUserId(id,startDate,endDate);
    }

    @Override
    public void addDutyLog(DutyLog dutyLog) {
        dutyLog.setId(seq.next());
        dutyManageDao.addDutyLog(dutyLog);
    }

    @Override
    public List<DutyLog> getDutyLogBydutyTime(String dutyTime,Long subDutyLogUserId) {
        return dutyManageDao.getDutyLogBydutyTime(dutyTime,subDutyLogUserId);
    }

    @Override
    public List<DutyLog> findDutyLog(Long dutyMainId, Long subDutyUserId) {
        return dutyManageDao.findDutyLog(dutyMainId,subDutyUserId);
    }

    @Override
    public DutyLog findDutyLog(Long dutyMainId, Long dutyTypeId, Long subDutyUserId) {
        return dutyManageDao.findDutyLog(dutyMainId,dutyTypeId,subDutyUserId);
    }

    @Override
    public DutyLog findAdminDutyLog(Long dutyMainId,Long dutyTypeId) {
        Map<String,Object> map = new HashMap<>();
        map.put("dutyMainId",dutyMainId);
        map.put("dutyTypeId",dutyTypeId);
        return dutyManageDao.findAdminDutyLog(map);
    }

    @Override
    public void delDutyLogById(Long id) {
        dutyManageDao.delDutyLogById(id);
    }

    @Override
    public void updateDutyLog(DutyLog dutyLog) {
        dutyManageDao.updateDutyLog(dutyLog);
    }

    @Override
    public List<DutyMain> findAllDutyList(DutyTypeVo dutyTypeVo) {
        return dutyManageDao.findAllDutyList(dutyTypeVo);
    }

    @Override
    public List<String> findUserByDomainId(String typeId, String searchText, Long domainId) {
        return dutyManageDao.findUserByDomainId(typeId,searchText,domainId);
    }

    @Override
    public List<DutyMainUserVo> selectDutyManById(Long dutyId, Long domainId) {
        return dutyManageDao.selectDutyManById(dutyId,domainId);
    }

    @Override
    public List<DutyTypeVo> findAllDutyTypeByDomainId(Long domainId) {
        return dutyManageDao.findAllDutyTypeByDomainId(domainId);
    }

    @Override
    public Date getMaxDate(Long domainId) {
        return dutyManageDao.getMaxDate(domainId);
    }

    @Override
    public List<DutyTypeVo> selectDutyTypeById(Long dutyId, Long domainId) {
        return dutyManageDao.selectDutyTypeById(dutyId,domainId);
    }

    @Override
    public List<Map<String, Object>> getDutyStatistics(Date beginTime, Date endTime, String domainId) {
        return dutyManageDao.getDutyStatistics(beginTime,endTime,domainId);
    }

    @Override
    public int addWeek(List<DutyWeekUserVo> dutyWeekUserVo) {
        for(int i=0;i<dutyWeekUserVo.size();i++){
            dutyWeekUserVo.get(i).setId(seq.next());
            appendEndTimeToDate1(dutyWeekUserVo.get(i));
        }
       return dutyManageDao.addWeek(dutyWeekUserVo);
    }

    @Override
    public List<DutyWeekUserVo> findWeek() {
        return dutyManageDao.findWeek();
    }


    @Override
    public List<DutyMainUserVo> findDutyMainUserListByListID(Long listId) {
        return dutyManageDao.findDutyMainUserListByListID(listId);
    }

    @Override
    public List<DutyMainUserVo> selectDutyMainUserListByListID(Long listId) {
        return dutyManageDao.selectDutyMainUserListByListID(listId);
    }

    @Override
    public List<DutyMainUserVo> findDutyMainUserListByDate(String startTime, String endTime, Long listId) {
        Map<String,Object> map = new HashMap<>();
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("listId",listId);
        return dutyManageDao.findDutyMainUserListByDate(map);
    }

    @Override
    public List<DutyTypeVo> findDutyTypeById(Long dutyTypeId) {
        return dutyManageDao.findDutyTypeById(dutyTypeId);
    }

    @Override
    public List<Long> findDutyTime() {
        return dutyManageDao.findDutyTime();
    }

    @Override
    public List<DutyWeekUserVo> findWeekDuty(Long dutyId) {
        return dutyManageDao.findWeekDuty(dutyId);
    }

    @Override
    public int delWeekUser(List<Long> delIds) {
        return dutyManageDao.delWeekUser(delIds);
    }

    @Override
    public int delMainUserByIds(List<Long> delIds) {
        return dutyManageDao.delMainUserByIds(delIds);
    }

    @Override
    public int addMans(List<DutyWeekUserVo> addMan) {
        for (DutyWeekUserVo duty:addMan){
            duty.setId(seq.next());
            appendEndTimeToDate1(duty);
        }
        return dutyManageDao.addMans(addMan);
    }

    @Override
    public int addDutyMans(List<DutyMainUserVo> addMan) {
        for (DutyMainUserVo duty:addMan){
            duty.setId(seq.next());
            appendEndTimeToDate1(duty);
        }
        return dutyManageDao.addDutyMans(addMan);
    }

    @Override
    public List<DutyTypeUserVo> findTypeUser(Long typeId,String searchText) {
        return dutyManageDao.findTypeUser(typeId,searchText);
    }

    @Override
    public void updateDutyMainUser(DutyMainUserVo dutyMainUserVo) {
        this.dutyManageDao.updateDutyMainUser(dutyMainUserVo);
    }

    @Override
    public List<DutyMainUserVo> findDutyMainUserByTypeId(Long typeId) {
        return dutyManageDao.findDutyMainUserByTypeId(typeId);
    }

    @Override
    public int delWeekUsers() {
        return dutyManageDao.delWeekUsers();
    }

    @Override
    public List<Long> findMinId() {
        return dutyManageDao.findMinId();
    }

    @Override
    public Date findDesc() {
        return dutyManageDao.findDesc();
    }

    @Override
    public Date findAsc() {
        return dutyManageDao.findAsc();
    }

    @Override
    public List<DutyMain> findDutyMainByListId(Long listId) {
        return dutyManageDao.findDutyMainByListId(listId);
    }


    @Override
    public List<DutyMain> findDutyMainByListId(String startTime, String endTime, Long listId) {
        Map<String,Object> map = new HashMap<>();
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("listId",listId);
        return this.dutyManageDao.findDutyMainByListId(map);
    }

    @Override
    public List<DutyMainUserVo> findDutyMainUserListByDutyId(Long dutyId) {
        return dutyManageDao.findDutyMainUserListByDutyId(dutyId);
    }

    @Override
    public void deleteDutyMainByDate(String startTime,String endTime,Long listId) {
        Map<String,Object> map = new HashMap<>();
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("listId",listId);
        this.dutyManageDao.deleteDutyMainByDate(map);
    }

    @Override
    public void deleteDutyMainByListId(Long listId) {
        this.dutyManageDao.deleteDutyMainByListId(listId);
    }

    @Override
    public void deleteRecordByDutyId(Long dutyId) {
        this.dutyManageDao.deleteRecordByDutyId(dutyId);
    }


    @Override
    public List<DutyMain> getAllDutyDateByDomainId(Long domainId, String startDate, String endDate) {
        Map<String,Object> map = new HashMap<>();
        map.put("domainId",domainId);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        return dutyManageDao.getAllDutyDateByDomainId(map);
    }

    @Override
    public void upDateDutyUserContactsInfo(DutyMainUserVo dutyMainUserVo) {
        dutyManageDao.upDateDutyUserContactsInfo(dutyMainUserVo);
    }


    @Override
    public void getMyDutyPage(Page<DutyMainUserVo, DutyMainUserQo> page) {
        dutyManageDao.getMyDutyPage(page);
    }

    @Override
    public void updateDutyUserStatus(DutyMainUserVo dutyMainUserVo) {
        dutyManageDao.updateDutyUserStatus(dutyMainUserVo);
    }

    @Override
    public List<DutyLog> getLogListByDutyIdAndTypeId(long dutyId, long typeId, long userId) {
        Map<String,Object> map = new HashMap<>();
        map.put("dutyId",dutyId);
        map.put("dutyTypeId",typeId);
        map.put("subDutyUserId",userId);
        return dutyManageDao.getLogListByDutyIdAndTypeId(map);
    }

    @Override
    public List<DutyTypeJobVo> findDutyMainTypeListByCDate(String currentDate) {
        return dutyManageDao.findDutyMainTypeListByCDate(currentDate);
    }

    @Override
    public List<DutyMainUserVo> findUserListByDutyIdAndTypeId(Long dutyId, Long typeId,Integer dutyStatus) {
        Map<String,Object> map = new HashMap<>();
        map.put("dutyId",dutyId);
        map.put("dutyTypeId",typeId);
        map.put("dutyStatus",dutyStatus);
        return dutyManageDao.findUserListByDutyIdAndTypeId(map);
    }

    @Override
    public void batchOffDutyList(List<Long> list, Date date, Integer dutyStatus) {
        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("offDutyDate",date);
        map.put("dutyStatus",dutyStatus);
        this.dutyManageDao.batchOffDutyList(map);
    }

    @Override
    public void updateDutyLogByDutyTypeUser(DutyLog dutyLog) {
        this.dutyManageDao.updateDutyLogByDutyTypeUser(dutyLog);
    }

    @Override
    public List<DutyMainUserVo> queryDutyMainUser(Page<DutyMainUserPageVo, DutyMainUserPageVo> page) {
        return this.dutyManageDao.queryDutyMainUser(page);
    }
}
