package com.vlinksoft.ves.inspection.dao;

import com.vlinksoft.ves.inspection.bo.InspectionMouldVo;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IInspectionMouldDao {

      void addInspectionMould(InspectionMouldVo mould);

      void getInspectionMouldByPage(Page page);

      void startInspectionMouldByIds(List<Integer> idList);

      InspectionMouldVo getInspectionMouldById(long id);

      void deleteMouldAndPlanTaskById(long mouldId);

      void stopInspectionMouldByIds(List<Integer> idList);

      void updateInspectionMouldInfo(InspectionMouldVo mould);
}
