package com.vlinksoft.ves.inspection.service;

import com.vlinksoft.ves.inspection.api.IInspectionMouldApi;
import com.vlinksoft.ves.inspection.api.IInspectionPlanApi;
import com.vlinksoft.ves.inspection.bo.InspectionMouldVo;
import com.vlinksoft.ves.inspection.dao.IInspectionMouldDao;
import com.vlinksoft.ves.platform.file.service.IFileClientApi;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class InspectionMouldApiImpl implements IInspectionMouldApi {

      @Resource
      private IInspectionMouldDao inspectionMouldDao;
      @Resource
      private IInspectionPlanApi inspectionPlanApi;
      @Resource
      private IFileClientApi iFileClientApi;

      @Override
      public void addInspectionMould(InspectionMouldVo mould) {
            inspectionMouldDao.addInspectionMould(mould);
      }

      @Override
      public void getInspectionMouldByPage(Page page) {
            inspectionMouldDao.getInspectionMouldByPage(page);
      }

      @Override
      public void startInspectionMouldByIds(List<Integer> idList) {
            inspectionMouldDao.startInspectionMouldByIds(idList);
      }

      @Override
      public void stopInspectionMouldByIds(List<Integer> idList) {
            inspectionMouldDao.stopInspectionMouldByIds(idList);
      }

      @Override
      public InspectionMouldVo getInspectionMouldById(long id) {
            return inspectionMouldDao.getInspectionMouldById(id);
      }

      @Override
      public void deleteMouldAndPlanTaskById(long mouldId) {
            InspectionMouldVo mould = inspectionMouldDao.getInspectionMouldById(mouldId);
            try {
                  iFileClientApi.deleteFile(mould.getMouldFileId());
            } catch (Exception e) {
                  e.printStackTrace();
            }
            inspectionMouldDao.deleteMouldAndPlanTaskById(mouldId);
            List<String> inspectionPlanIds = inspectionPlanApi.selectPlanByMouldId(mouldId);
            if (inspectionPlanIds != null && inspectionPlanIds.size() > 0){
                  inspectionPlanApi.delNoInspectionPlan(inspectionPlanIds);
            }
      }

      @Override
      public void updateInspectionMouldInfo(InspectionMouldVo mould) {
            inspectionMouldDao.updateInspectionMouldInfo(mould);
      }
}
