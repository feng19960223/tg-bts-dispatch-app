package com.turingoal.bts.dispatch.service;

import com.turingoal.bts.common.android.biz.domain.BreakdownRecord;
import com.turingoal.bts.dispatch.constants.ConstantUrls;
import com.turingoal.common.android.bean.TgResponseBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 总故障
 */
public interface BreakdownRecordService {
    /**
     * 故障列表
     */
    @POST(ConstantUrls.URL_BREAKDOWN_RECORD_LIST)
    Call<TgResponseBean<List<BreakdownRecord>>> findByDate(@Query("taskDate") String taskDate);
}
