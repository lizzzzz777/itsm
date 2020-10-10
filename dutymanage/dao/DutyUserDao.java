package com.vlinksoft.ves.dutymanage.dao;

import com.vlinksoft.ves.dutymanage.Vo.DutyTodayVo;
import com.vlinksoft.ves.dutymanage.Vo.DutyTypeVoQuery;
import com.vlinksoft.ves.dutymanage.bo.DutyTypeVo;
import com.vlinksoft.ves.dutymanage.bo.DutyUserVo;
import com.vlinksoft.ves.dutymanage.dao.po.DutyApproveCountPO;
import com.vlinksoft.ves.dutymanage.dao.po.DutyCountPO;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/12/21.
 */
public interface DutyUserDao {
   int addDutyClass(DutyTypeVo typeVo);
   int insertShiftId(DutyUserVo dutyUserVo);
   int insertShiftIdUser(DutyUserVo dutyUserVo);
   void list(Page<DutyTypeVo,DutyTypeVoQuery> page);
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
    * 汇总用户值班情况。
    *
    * @param startDate 值班开始时间。
    * @param endDate 值班结束时间
    * @return 值班统计结果。
    */
   List<DutyCountPO> summaryDutyCount(Date startDate,Date endDate);

   /**
    * 汇总用户审核情况。
    *
    * @param startDate  值班开始时间。
    * @param endDate 值班结束时间
    * @return 统计结果。
    */
   List<DutyApproveCountPO> summaryDutyApproveCount(Date startDate, Date endDate);

   List<DutyCountPO> getSummaryDutyCount(Long id, Date startDate, Date endDate);

   List<DutyApproveCountPO> getSummaryDutyApproveCount(Long id, Date startDate, Date endDate);

   List<DutyTypeVo> selectByIds(List<Long>ids);

   List<DutyCountPO> getSummaryDutyCountByDomainId(Long domainId, Date startDate, Date endDate);

   List<DutyApproveCountPO> getSummaryDutyApproveCountByDomainId(Long domainId, Date startDate, Date endDate);

    int checkClassName(String name, Long domainId);

   void findDutyTypeByDomainId(Page<DutyTypeVo, DutyTypeVoQuery> page);

   List<DutyTypeVo> dutyTypeList(int across, int dutyTypeId, Long domainId);
}
