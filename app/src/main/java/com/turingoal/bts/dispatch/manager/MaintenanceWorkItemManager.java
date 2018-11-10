package com.turingoal.bts.dispatch.manager;


import com.turingoal.bts.dispatch.app.TgApplication;
import com.turingoal.bts.dispatch.bean.MaintenanceWorkItemDb;
import com.turingoal.bts.dispatch.bean.MaintenanceWorkItemDb_;

import java.util.List;

/**
 * [作业项目]Manager
 */
public class MaintenanceWorkItemManager {
    /**
     * 检修任务CodeNum 查询对应的作业项目
     */
    public List<MaintenanceWorkItemDb> getMaintenanceWorkItemList(final String maintenanceTaskCodeNum) {
        List<MaintenanceWorkItemDb> maintenanceWorkItems = TgApplication.getBoxStore().boxFor(MaintenanceWorkItemDb.class).query()
                .equal(MaintenanceWorkItemDb_.maintenanceTaskCodeNum, maintenanceTaskCodeNum).build().find();
        return maintenanceWorkItems;
    }
}
