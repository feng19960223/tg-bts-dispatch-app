package com.turingoal.bts.dispatch.service;

import com.turingoal.bts.common.android.biz.domain.BreakdownRecordItem;
import com.turingoal.bts.dispatch.constants.ConstantUrls;
import com.turingoal.common.android.bean.TgResponseBean;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 分故障
 */
public interface BreakdownRecordItemService {
    /**
     * 得到故障item
     */
    @POST(ConstantUrls.URL_BREAKDOWN_RECORD_ITEM_LIST)
    Call<TgResponseBean<List<BreakdownRecordItem>>> getList(@Query("breakdownRecordId") String id);

    /**
     * 得到故障item
     */
    @POST(ConstantUrls.URL_BREAKDOWN_RECORD_ITEM_LEGACY_LIST)
    Call<TgResponseBean<List<BreakdownRecordItem>>> getLegacyList(@Query("breakdownRecordId") String id, @Query("trainSetCodeNum") String trainSetCodeNum);

    /**
     * 删除故障
     */
    @POST(ConstantUrls.URL_BREAKDOWN_RECORD_ITEM_DELETE)
    Call<TgResponseBean<Object>> delete(@Query("id") String id);

    /**
     * 故障派工
     */
    @POST(ConstantUrls.URL_BREAKDOWN_RECORD_ITEM_DISPATCH)
    Call<TgResponseBean<Object>> dispatch(@QueryMap Map<String, Object> map);

    /**
     * 故障详情
     */
    @POST(ConstantUrls.URL_BREAKDOWN_RECORD_ITEM)
    Call<TgResponseBean<BreakdownRecordItem>> get(@Query("id") String id);
}
