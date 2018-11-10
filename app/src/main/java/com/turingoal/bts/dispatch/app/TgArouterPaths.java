package com.turingoal.bts.dispatch.app;

/**
 * 常量-Activity路径
 */
public interface TgArouterPaths {
    String TASK_ADD = TgAppConfig.APP_BASE_PATH + "task/add"; // 新增任务
    // 故障
    String BREAKDOWN_RECORD = TgAppConfig.APP_BASE_PATH + "breakdown/record"; // 故障详情
    String BREAKDOWN_RECORD_ITEM_DETAIL = TgAppConfig.APP_BASE_PATH + "breakdown/record/item/detail"; // 故障item详情
    String BREAKDOWN_RECORD_ITEM_DISPATCH = TgAppConfig.APP_BASE_PATH + "breakdown/record/item/dispatch"; // 故障item派工
    // 派工
    String DISPATCH_DECLARE = TgAppConfig.APP_BASE_PATH + "dispatch/declare"; // 派工页面
    String DISPATCH_DETAIL = TgAppConfig.APP_BASE_PATH + "dispatch/detail"; // 派工详情
    // 任务
    String SCHEDULING_ORDER_DETAIL = TgAppConfig.APP_BASE_PATH + "schedulingOrder/detail"; // 总单详情页面

    String PHOTO_SHOW = TgAppConfig.APP_BASE_PATH + "photoShow"; // 查看照片
}
