package com.vlinksoft.ves.inspection.bo;

public enum  InspectionTaskState {

    wait_writer,//待填写
    writering,//填写中
    completed,//已完成
    un_complete,//已完成
    overtime_wait_writer,//超时待填写
    overtime_completed,//超时已完成

}
