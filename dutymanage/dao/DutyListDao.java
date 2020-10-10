package com.vlinksoft.ves.dutymanage.dao;

import com.vlinksoft.ves.dutymanage.bo.DutyListVo;
import com.vlinksoft.ves.platform.mybatis.plugin.pagination.Page;

import java.util.List;

/**
 * Author:   nierf
 * Date:     2020/3/13 16:23
 */
public interface DutyListDao {
    void insertDutyList(DutyListVo dutyListVo);

    void updateDutyList(DutyListVo dutyListVo);

    void deleteDutyList(Long id);

    List<DutyListVo> findAllDutyList(DutyListVo dutyListVo);

    void findAllDutyPage(Page<DutyListVo,DutyListVo> page);

    DutyListVo findAllDutyListById(Long id);

}
