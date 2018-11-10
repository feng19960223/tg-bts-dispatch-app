package com.turingoal.bts.dispatch.service;

import com.turingoal.bts.common.android.biz.domain.SchedulingOrder;
import com.turingoal.bts.dispatch.constants.ConstantUrls;
import com.turingoal.common.android.bean.TgResponseBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 任务列表
 */
public interface SchedulingOrderService {
    /**
     * 得到任务总单
     */
    @POST(ConstantUrls.URL_SCHEDULING_ORDER_LIST)
    Call<TgResponseBean<SchedulingOrder>> getByDateAndWorkShift(
            @Query("taskDate") String taskDate,
            @Query("workShiftType") Integer workShiftType);

    /**
     * 得到任务总单
     */
    @POST(ConstantUrls.URL_SCHEDULING_TASK_LIST)
    Call<TgResponseBean<SchedulingOrder>> getByDateAndWorkShiftTypeAndWorkGroup(
            @Query("taskDate") String taskDate,
            @Query("workShiftType") Integer workShiftType,
            @Query("workGroupName") String workGroupName);
}
