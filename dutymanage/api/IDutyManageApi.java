package com.vlinksoft.ves.dutymanage.api;

import com.vlinksoft.ves.dutymanage.Vo.DutyMonthUser;
import com.vlinksoft.ves.dutymanage.Vo.DutyMonthUserQuery;
import com.vlinksoft.ves.dutymanage.bo.*;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/12/26.
 */
public interface IDutyManageApi {
    void getDutyRecordByUID(Page<DutyMainUserVo, DutyMainUserVo> page);
    List<DutyMain> interDutyMain(List<DutyMain> dutyMain);
    List<DutyTypeVo> getDutyType(DutyMain main);
    List<String> getUsersByTypeId(String typeId,String searchText);
    int setInUserInfo(DutyMainUserVo userVo);
    int setDelUser(String userId);
    int insertToMain(DutyMain dutyMain);   //插入duty_main表格中一条数据
    int insertToUser(List<DutyMainUserVo> userList);  //向duty_main_user表中插入数据
    List<DutyMain> selectDutyList(DutyTypeVo dutyTypeVo);

    List<DutyMain> selectAllDutyList(DutyTypeVo dutyTypeVo);

    List<DutyMainUserVo> getDutyInfoByDutyDate(DutyTypeVo dutyTypeVo);
    int insertToMainLine(DutyMain dutyMain); //向itsm_duty_main中插入一行数据得到dutyId
    int insertToMainList(List<DutyMainUserVo> listVo);    //将复制的数据插入到duty_main_user表中
    Date getMaxDate();  //获得日期表中最大时间
    String getMinDate();   //获得日期表中最小日期
    int updateDutyState(DutyMainUserVo dutyMainUserVo);  //更新值班状态
    int delLineUser(List<Long> delIds); //编辑保存时删除itsm_duty_main_user表格中的数据
    int getMainUserInfo(String typeUserName,String dutyTypeId); //根据人员名查相关的信息userId
    int addMan(List<DutyMainUserVo> addMan); //编辑时添加的人员
    long checkByDutyDate(String shiftDate); //根据时间查询该时间是否排班

    List<DutyMain> getDutyDateByUserId(Long id,String startDate,String endDate);

    List<DutyMainUserVo> getDutyUserByDate(String today);

    List<DutyMain> getDutyMainByDate(String format);

    List<DutyMonthUser> getUserDutyInfoList(Page page);

    int getTypeNumByUserId(long userId,int type);

    int getHasDutyMonth(long userId, int dutyType);

    List<DutyTypeVo> listAll();

    List<DutyTypeVo> listAllByDomainId(String  domainId);

    void insertDutyTypeRecord(List<DutyTypeVo> dutyTypeVos);
    List<DutyTypeVo> selectDutyTypeById (long dutyId);
    List<DutyMainUserVo> selectDutyManById (long dutyId);
    List<DutyTypeVo> getDutyClassByTime(String shiftTime, long userId);
    List<DutyTypeVo> getShiftDutyClassByTime (String time) throws ParseException;
    List<DutyMainUserVo> getShiftDutyClassByTimeAndDutyType(long shiftType,String time) throws ParseException;
    List<Map<String, Object>> getDutyStatistics(DutyMonthUserQuery dutyMonthUserQuery);

    Long getLeaveDateByUserId(Long id, String startDate, String endDate);


    void addDutyLog(DutyLog dutyLog);

    List<DutyLog> getDutyLogBydutyTime(String dutyTime,Long subDutyLogUserId);

    List<DutyLog> findDutyLog(Long dutyMainId,Long subDutyUserId);

    DutyLog findDutyLog(Long dutyMainId,Long dutyTypeId,Long subDutyUserId);

    DutyLog findAdminDutyLog(Long dutyMainId,Long dutyTypeId);


    void delDutyLogById(Long id);

    void updateDutyLog(DutyLog dutyLog);

    List<DutyMain> findAllDutyList(DutyTypeVo dutyTypeVo);//根据域获取值班日期

    List<String> findUserByDomainId(String typeId, String searchText, Long domainId);

    List<DutyMainUserVo> selectDutyManById(Long dutyId, Long domainId);

    List<DutyTypeVo> findAllDutyTypeByDomainId(Long domainId);

    Date getMaxDate(Long domainId);

    List<DutyTypeVo> selectDutyTypeById(Long dutyId, Long domainId);

    List<Map<String, Object>> getDutyStatistics(Date beginTime, Date endTime, String domainId);
    /*保存延续按周排班*/
    int addWeek(List<DutyWeekUserVo> dutyWeekUserVo);

    List<DutyWeekUserVo> findWeek();


    List<DutyTypeVo> findDutyTypeById(Long dutyTypeId);
    List<Long> findDutyTime();
    List<DutyWeekUserVo> findWeekDuty(Long dutyId);
    /*更新编辑按周排班*/
    int delWeekUser(List<Long> delIds);
    int delMainUserByIds(List<Long> delIds);
    int addMans(List<DutyWeekUserVo> addMan);
    int addDutyMans(List<DutyMainUserVo> addMan);
    List<DutyTypeUserVo> findTypeUser(Long typeId,String searchText);

    void updateDutyMainUser(DutyMainUserVo dutyMainUserVo);

    List<DutyMainUserVo> findDutyMainUserByTypeId(Long typeId);
    int delWeekUsers();

    List<Long> findMinId();
    //查询最大时间
    Date findDesc();
    //查询最小时间
    Date findAsc();

    //查寻 user任务
    List<DutyMainUserVo> findDutyMainUserListByListID(Long listId);

    /**
     * jwt 同findDutyMainUserListByListID一样，只是获取itsm_duty_main_user的startTime和endTime
     * @param listId
     * @return
     */
    List<DutyMainUserVo> selectDutyMainUserListByListID(Long listId);

    List<DutyMainUserVo> findDutyMainUserListByDate(String startTime,String endTime,Long listId);

    List<DutyMain> findDutyMainByListId(Long listId);

    List<DutyMain> findDutyMainByListId(String startTime,String endTime,Long listId);

    List<DutyMainUserVo> findDutyMainUserListByDutyId(Long dutyId);

    //删除dutyMain
    void deleteDutyMainByDate(String startTime,String endTime,Long listId);

    void deleteDutyMainByListId(Long listId);

    void deleteRecordByDutyId(Long dutyId);

    List<DutyMain> getAllDutyDateByDomainId(Long domainId,String startDate,String endDate);

    void upDateDutyUserContactsInfo(DutyMainUserVo dutyMainUserVo);


    void getMyDutyPage(Page<DutyMainUserVo,DutyMainUserQo> page);

    void updateDutyUserStatus(DutyMainUserVo dutyMainUserVo);

    List<DutyLog> getLogListByDutyIdAndTypeId(long dutyId,long typeId,long userId);

    List<DutyTypeJobVo> findDutyMainTypeListByCDate(String currentDate);

    List<DutyMainUserVo> findUserListByDutyIdAndTypeId(Long dutyId,Long typeId,Integer dutyStatus);

    void batchOffDutyList(List<Long> list,Date date,Integer dutyStatus);

    void updateDutyLogByDutyTypeUser(DutyLog dutyLog);

    //jwt query获取itsm_duty_main_user表
    List<DutyMainUserVo> queryDutyMainUser(Page<DutyMainUserPageVo,DutyMainUserPageVo> page);
}
