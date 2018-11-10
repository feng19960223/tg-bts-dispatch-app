package com.turingoal.bts.dispatch.bean;

import com.turingoal.bts.common.android.biz.domain.MaintenanceWorkPosition;

/**
 * 任务派工时候的选择框
 */
public class MaintenanceWorkPositionBean extends MaintenanceWorkPosition {
    public String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
