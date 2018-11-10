package com.turingoal.bts.dispatch.service;

import com.turingoal.bts.dispatch.bean.TrackDb;
import com.turingoal.bts.dispatch.constants.ConstantUrls;
import com.turingoal.common.android.bean.TgResponseBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 股道配置
 */
public interface TrackService {

    /**
     * 股道配置
     */
    @GET(ConstantUrls.URL_TRACK_LIST)
    Call<TgResponseBean<List<TrackDb>>> list();
}
