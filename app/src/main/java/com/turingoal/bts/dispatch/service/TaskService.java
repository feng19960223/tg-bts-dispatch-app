package com.turingoal.bts.dispatch.service;

import com.turingoal.bts.common.android.biz.domain.DispatchTaskItem;
import com.turingoal.bts.dispatch.bean.MaintenanceWorkPositionBean;
import com.turingoal.bts.dispatch.constants.ConstantUrls;
import com.turingoal.common.android.bean.TgResponseBean;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 任务星期
 */
public interface TaskService {
    /**
     * 新建任务
     */
    @POST(ConstantUrls.URL_SCHEDULING_TASK_ADD)
    Call<TgResponseBean<Object>> add(@QueryMap Map<String, Object> map);

    /**
     * 根据任务查询作业人员详情
     */
    @POST(ConstantUrls.URL_DISPATCH_TASK_DETAIL)
    Call<TgResponseBean<List<DispatchTaskItem>>> findDispatchTaskItemsByTask(@Query("schedulingTaskCodeNum") String schedulingTaskCodeNum);

    /**
     * 得到任务需要人的选择框
     */
    @POST(ConstantUrls.URL_DISPATCH_TASK_POSITION)
    Call<TgResponseBean<List<MaintenanceWorkPositionBean>>> findByMaintenanceWorkItem(@Query("maintenanceWorkItem") String maintencanceWorkItem);

    /**
     * 派工
     */
    @Multipart
    @POST(ConstantUrls.URL_DISPATCH_TASK_DISPATCH)
    Call<TgResponseBean<Object>> dispatch(@PartMap Map<String, RequestBody> map);
}
