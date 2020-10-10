package com.vlinksoft.ves.inspection.api;

import com.vlinksoft.ves.inspection.bo.InspectionMouldVo;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IInspectionMouldApi {

    void addInspectionMould(InspectionMouldVo mould);

    void getInspectionMouldByPage(Page page);

    void startInspectionMouldByIds(List<Integer> idList);

    void stopInspectionMouldByIds(List<Integer> idList);

    InspectionMouldVo getInspectionMouldById(long id);

    void deleteMouldAndPlanTaskById(long mouldId);

    void updateInspectionMouldInfo(InspectionMouldVo mould);

}
