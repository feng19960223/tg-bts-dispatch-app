package com.turingoal.bts.dispatch.constants;

import com.turingoal.bts.dispatch.app.TgAppConfig;

/**
 * url路径.
 */
public interface ConstantUrls {
    // 基本
    String URL_USER_LOGIN = TgAppConfig.URL_BASE + "login" + TgAppConfig.URL_SUFFIX; // 登录路径
    String URL_CHECK_TOKEN = TgAppConfig.URL_BASE + "checkToken" + TgAppConfig.URL_SUFFIX; // 校验token
    String URL_CHECK_VERSION = TgAppConfig.URL_BASE + "checkVersion" + TgAppConfig.URL_SUFFIX; // 检查版本

    // fragment
    String URL_SCHEDULING_ORDER_LIST = TgAppConfig.URL_BASE + "schedulingOrder/getByDateAndWorkShift" + TgAppConfig.URL_SUFFIX; // 总单list
    String URL_SCHEDULING_TASK_LIST = TgAppConfig.URL_BASE + "schedulingOrder/getByDateAndWorkShiftTypeAndWorkGroupName" + TgAppConfig.URL_SUFFIX; // 任务单list
    String URL_BREAKDOWN_RECORD_LIST = TgAppConfig.URL_BASE + "breakdownRecord/findByDate" + TgAppConfig.URL_SUFFIX; // 故障单list
    // 任务
    String URL_SCHEDULING_TASK_ADD = TgAppConfig.URL_BASE + "schedulingTask/add" + TgAppConfig.URL_SUFFIX; // 新建作业
    String URL_DISPATCH_TASK_DETAIL = TgAppConfig.URL_BASE + "dispatchTaskItem/findBySchedulingTaskCodeNum" + TgAppConfig.URL_SUFFIX; // 根据任务查询作业详情（派工的人员）
    String URL_DISPATCH_TASK_POSITION = TgAppConfig.URL_BASE + "maintenanceWorkPosition/findByMaintenanceWorkItem" + TgAppConfig.URL_SUFFIX; // 根据任务查询作业详情（派工的人员）
    String URL_DISPATCH_TASK_DISPATCH = TgAppConfig.URL_BASE + "schedulingTask/dispatch" + TgAppConfig.URL_SUFFIX; // 任务派工
    // 故障
    String URL_BREAKDOWN_RECORD_ITEM_LIST = TgAppConfig.URL_BASE + "breakdownRecordItem/findByBreakdownRecord" + TgAppConfig.URL_SUFFIX; // 得到故障item
    String URL_BREAKDOWN_RECORD_ITEM_LEGACY_LIST = TgAppConfig.URL_BASE + "breakdownRecordItem/findNotFinishedByTrainSetCodeNum" + TgAppConfig.URL_SUFFIX; // 得到遗留的故障item
    String URL_BREAKDOWN_RECORD_ITEM_DISPATCH = TgAppConfig.URL_BASE + "breakdownRecordItem/dispatch" + TgAppConfig.URL_SUFFIX; // 故障派工，点击确定派工，通过故障id和调度任务单id和调度任务id和处理人name
    String URL_BREAKDOWN_RECORD_ITEM_DELETE = TgAppConfig.URL_BASE + "breakdownRecordItem/delete" + TgAppConfig.URL_SUFFIX; // 通过id删除故障
    String URL_BREAKDOWN_RECORD_ITEM = TgAppConfig.URL_BASE + "breakdownRecordItem/get" + TgAppConfig.URL_SUFFIX; // 得到保护图片的故障
    // 基础配置
    String URL_GET_ALL_PEOPLE_LIST = TgAppConfig.URL_BASE + "maintainer/find" + TgAppConfig.URL_SUFFIX; // 得到所有人
    String URL_TRACK_LIST = TgAppConfig.URL_BASE + "track/find" + TgAppConfig.URL_SUFFIX; // 股道配置
    String URL_TRAIN_SET_LIST = TgAppConfig.URL_BASE + "trainSet/find" + TgAppConfig.URL_SUFFIX; // 车号配置
    String URL_MAINTENANCE_TASK_LIST = TgAppConfig.URL_BASE + "maintenanceTask/find" + TgAppConfig.URL_SUFFIX; // 项目配置
    String URL_MAINTENANCE_WORK_ITEM_LIST = TgAppConfig.URL_BASE + "maintenanceWorkItem/find" + TgAppConfig.URL_SUFFIX; // 任务配置
}
