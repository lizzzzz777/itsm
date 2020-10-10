package com.vlinksoft.ves.dutymanage.service;

import com.vlinksoft.ves.dutymanage.Vo.DutyTodayVo;
import com.vlinksoft.ves.dutymanage.Vo.DutyTypeVoQuery;
import com.vlinksoft.ves.dutymanage.api.IDutyUserApi;
import com.vlinksoft.ves.dutymanage.bo.DutyTypeVo;
import com.vlinksoft.ves.dutymanage.bo.DutyUserVo;
import com.vlinksoft.ves.dutymanage.bo.UserDutyInfoVo;
import com.vlinksoft.ves.dutymanage.dao.DutyUserDao;
import com.vlinksoft.ves.dutymanage.dao.po.DutyApproveCountPO;
import com.vlinksoft.ves.dutymanage.dao.po.DutyCountPO;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;
import com.vlinksoft.ves.platform.sequence.service.ISequence;

import java.util.*;

/**
 * Created by admin on 2017/12/21.
 */
public class DutyUserApiImpl implements IDutyUserApi {
    private DutyUserDao dutyUserDao;
    private ISequence seq;

    @Override
    public int addDutyClass(DutyTypeVo typeVo) {
        long id=seq.next();
        typeVo.setTypeId(id);
        dutyUserDao.addDutyClass(typeVo);
        return (int) id;
    }

    @Override
    public int insertShiftId(DutyUserVo dutyUserVo) {
        System.out.print(dutyUserVo.getUserName()+"123123");
        return dutyUserDao.insertShiftId(dutyUserVo);
    }

    @Override
    public int insertShiftIdUser(DutyUserVo dutyUserVo) {
        dutyUserVo.setId(seq.next());
        return dutyUserDao.insertShiftIdUser(dutyUserVo);
    }

    @Override
    public void list(Page page) {
        dutyUserDao.list(page);
    }

    @Override
    public int delDutyClassType(String ids) {
        return dutyUserDao.delDutyClassType(ids);
    }

    @Override
    public int delDutyClassUser(String ids) {
        return dutyUserDao.delDutyClassUser(ids);
    }

    @Override
    public int updateDutyClassType(DutyTypeVo dutyTypeVo) {
        return dutyUserDao.updateDutyClassType(dutyTypeVo);
    }

    @Override
    public int updateDutyClassUser(DutyUserVo dutyUserVo) {
        return 0;
    }

    @Override
    public int checkClassName(String name) {
        return dutyUserDao.checkClassName(name);
    }

    @Override
    public int checkFlogClass(String classId) {
        return dutyUserDao.checkFlogClass(classId);
    }

    @Override
    public List<DutyUserVo> selectDutyUserByDutyId(long typeId) {
        return dutyUserDao.selectDutyUserByDutyId(typeId);
    }

    @Override
    public List<DutyTypeVo> dutyTypeList(int across,int dutyTypeId) {
        return dutyUserDao.dutyTypeList(across,dutyTypeId);
    }

    @Override
    public List<DutyTodayVo> selectDutyUserToday(Page page) {
       return dutyUserDao.selectDutyUserToday(page);
    }

    @Override
    public String getDepartmentNameByUserId(long userId) {
        return dutyUserDao.getDepartmentNameByUserId(userId);
    }

    @Override
    public List<UserDutyInfoVo> summaryUserDutyInfoVo(Date startDate, Date endDate) {
        Map<Integer,UserDutyInfoVo> userDutyInfoVoMap = new HashMap<>();
        List<DutyCountPO> dutyCountPOS = dutyUserDao.summaryDutyCount(startDate,endDate);
        if(dutyCountPOS!=null && dutyCountPOS.size()>0){
            for (DutyCountPO count:
                 dutyCountPOS) {
                UserDutyInfoVo vo = userDutyInfoVoMap.get(count.getUserId());
                if(vo == null){
                    vo = new UserDutyInfoVo();
                    userDutyInfoVoMap.put(count.getUserId(),vo);
                }
                vo.setUserId(String.valueOf(count.getUserId()));
                vo.setUserName(count.getUserName());
                if(count.getState() == 1){//已值
                    vo.setIsOnDuty(count.getDutyCount());
                }else{
                    vo.setNotOnDuty(count.getDutyCount());
                }
                vo.setDutyCount(vo.getDutyCount()+count.getDutyCount());
            }
        }
        List<DutyApproveCountPO> dutyApproveCountPOS = dutyUserDao.summaryDutyApproveCount(startDate,endDate);
        if(dutyApproveCountPOS!=null && dutyApproveCountPOS.size()>0){
            for (DutyApproveCountPO count:
                    dutyApproveCountPOS) {
                UserDutyInfoVo vo = userDutyInfoVoMap.get(count.getUserId());
                if(vo == null){
                    vo = new UserDutyInfoVo();
                    userDutyInfoVoMap.put(count.getUserId(),vo);
                }
                vo.setUserId(String.valueOf(count.getUserId()));
                vo.setUserName(count.getUserName());
                if("换班".equals(count.getDutyType())){//已值
                    vo.setChangeCount(count.getApproveCount());
                }else{
                    vo.setLeaveCount(count.getApproveCount());
                }
            }
        }
        return new ArrayList<>(userDutyInfoVoMap.values());
    }


    public DutyUserDao getDutyUserDao() {
        return dutyUserDao;
    }

    public void setDutyUserDao(DutyUserDao dutyUserDao) {
        this.dutyUserDao = dutyUserDao;
    }

    public ISequence getSeq() {
        return seq;
    }

    public void setSeq(ISequence seq) {
        this.seq = seq;
    }

    //点击获取更多我的值班
    public List<UserDutyInfoVo> getSummaryUserDuty(Long id, Date startDate, Date endDate) {
        Map<String,UserDutyInfoVo> userDutyInfoVoMap = new HashMap<>();
        List<DutyCountPO> dutyCountPOS = dutyUserDao.getSummaryDutyCount(id,startDate,endDate);
        if(dutyCountPOS!=null && dutyCountPOS.size()>0){
            for (DutyCountPO count:
                    dutyCountPOS) {
                UserDutyInfoVo vo = userDutyInfoVoMap.get(count.getDutyMonth());
                if(vo == null){
                    vo = new UserDutyInfoVo();
                    userDutyInfoVoMap.put(count.getDutyMonth(),vo);
                }
                vo.setDutyMonth(count.getDutyMonth());
                vo.setUserId(String.valueOf(count.getUserId()));
                vo.setUserName(count.getUserName());
                if(count.getState() == 1){//已值
                    vo.setIsOnDuty(count.getDutyCount());
                }else{
                    vo.setNotOnDuty(count.getDutyCount());
                }
                vo.setDutyCount(vo.getDutyCount()+count.getDutyCount());
            }
        }
        List<DutyApproveCountPO> dutyApproveCountPOS = dutyUserDao.getSummaryDutyApproveCount(id,startDate,endDate);
        if(dutyApproveCountPOS!=null && dutyApproveCountPOS.size()>0){
            for (DutyApproveCountPO count:
                    dutyApproveCountPOS) {
                UserDutyInfoVo vo = userDutyInfoVoMap.get(count.getDutyMonth());
                if(vo == null){
                    vo = new UserDutyInfoVo();
                    userDutyInfoVoMap.put(count.getDutyMonth(),vo);
                }
                vo.setDutyMonth(count.getDutyMonth());
                vo.setUserId(String.valueOf(count.getUserId()));
                vo.setUserName(count.getUserName());
                if("换班".equals(count.getDutyType())){//已值
                    vo.setChangeCount(count.getApproveCount());
                }else{
                    vo.setLeaveCount(count.getApproveCount());
                }
            }
        }
        return new ArrayList<>(userDutyInfoVoMap.values());
    }

    @Override
    public List<DutyTypeVo> selectByIds(List<Long> ids) {
        return dutyUserDao.selectByIds(ids);
    }

    @Override
    public List<UserDutyInfoVo> findUserDutyInfoVoByDomainID(Date startDate, Date endDate, Long domainId) {
        Map<Integer,UserDutyInfoVo> userDutyInfoVoMap = new HashMap<>();
        List<DutyCountPO> dutyCountPOS = dutyUserDao.getSummaryDutyCountByDomainId(domainId,startDate,endDate);
        if(dutyCountPOS!=null && dutyCountPOS.size()>0){
            for (DutyCountPO count:
                    dutyCountPOS) {
                UserDutyInfoVo vo = userDutyInfoVoMap.get(count.getUserId());
                if(vo == null){
                    vo = new UserDutyInfoVo();
                    userDutyInfoVoMap.put(count.getUserId(),vo);
                }
                vo.setUserId(String.valueOf(count.getUserId()));
                vo.setUserName(count.getUserName());
                if(count.getState() == 1){//已值
                    vo.setIsOnDuty(count.getDutyCount());
                }else{
                    vo.setNotOnDuty(count.getDutyCount());
                }
                vo.setDutyCount(vo.getDutyCount()+count.getDutyCount());
            }
        }
        List<DutyApproveCountPO> dutyApproveCountPOS = dutyUserDao.getSummaryDutyApproveCountByDomainId(domainId,startDate,endDate);
        if(dutyApproveCountPOS!=null && dutyApproveCountPOS.size()>0){
            for (DutyApproveCountPO count:
                    dutyApproveCountPOS) {
                UserDutyInfoVo vo = userDutyInfoVoMap.get(count.getUserId());
                if(vo == null){
                    vo = new UserDutyInfoVo();
                    userDutyInfoVoMap.put(count.getUserId(),vo);
                }
                vo.setUserId(String.valueOf(count.getUserId()));
                vo.setUserName(count.getUserName());
                if("换班".equals(count.getDutyType())){//已值
                    vo.setChangeCount(count.getApproveCount());
                }else{
                    vo.setLeaveCount(count.getApproveCount());
                }
            }
        }
        return new ArrayList<>(userDutyInfoVoMap.values());
    }

    @Override
    public int checkClassName(String name, Long domainId) {
        return dutyUserDao.checkClassName(name,domainId);
    }

    @Override
    public void findDutyTypeByDomainId(Page<DutyTypeVo, DutyTypeVoQuery> page) {
        dutyUserDao.findDutyTypeByDomainId(page);
    }

    @Override
    public List<DutyTypeVo> dutyTypeList(int across, int dutyTypeId, Long domainId) {
        return dutyUserDao.dutyTypeList(across,dutyTypeId,domainId);
    }

}
