package com.vlinksoft.ves.dutymanage.api;

import com.vlinksoft.ves.dutymanage.Vo.DutyTodayVo;
import com.vlinksoft.ves.dutymanage.Vo.DutyTypeVoQuery;
import com.vlinksoft.ves.dutymanage.bo.DutyTypeVo;
import com.vlinksoft.ves.dutymanage.bo.DutyUserVo;
import com.vlinksoft.ves.dutymanage.bo.UserDutyInfoVo;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/12/21.
 */
public interface IDutyUserApi {
     int addDutyClass(DutyTypeVo typeVo);
     int insertShiftId(DutyUserVo dutyUserVo);
     int insertShiftIdUser(DutyUserVo dutyUserVo);
     void list(Page page);
     int delDutyClassType(String ids);
     int delDutyClassUser(String ids);
     int updateDutyClassType(DutyTypeVo dutyTypeVo);
     int updateDutyClassUser(DutyUserVo dutyUserVo);
     int checkClassName(String name); //检查班次名称是否重复
     int checkFlogClass(String classId); //检查是否排班
     List<DutyUserVo> selectDutyUserByDutyId(long typeId);  //根据值班id查询值班人员

    List<DutyTypeVo> dutyTypeList(int across,int dutyTypeId);

     List<DutyTodayVo> selectDutyUserToday(Page page);
    String getDepartmentNameByUserId(long userId);

    /**
     * 汇总用户值班情况
     *
     * @param startDate
     * @param endDate
     * @return
     */
    List<UserDutyInfoVo> summaryUserDutyInfoVo(Date startDate,Date endDate);

    //点击获取更多我的值班
    List<UserDutyInfoVo> getSummaryUserDuty(Long id, Date statrTime, Date endTime);

    List<DutyTypeVo> selectByIds(List<Long>ids);



    List<UserDutyInfoVo> findUserDutyInfoVoByDomainID(Date startDate, Date endDate, Long domainId);

    int checkClassName(String name, Long domainId);

    void findDutyTypeByDomainId(Page<DutyTypeVo, DutyTypeVoQuery> page);

    List<DutyTypeVo> dutyTypeList(int i, int dutyTypeId, Long domainId);
}
