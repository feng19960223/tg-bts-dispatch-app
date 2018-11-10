package com.turingoal.bts.dispatch.service;

import com.turingoal.bts.dispatch.bean.User;
import com.turingoal.bts.dispatch.constants.ConstantUrls;
import com.turingoal.common.android.bean.TgKeyValueBean;
import com.turingoal.common.android.bean.TgResponseBean;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 基本
 */
public interface UserService {
    /**
     * 检查版本
     */
    @POST(ConstantUrls.URL_CHECK_VERSION)
    Call<TgResponseBean<TgKeyValueBean>> checkVersion(@Query("currentVersion") Integer currentVersion, @Query("packageName") String pageName);

    /**
     * 登录
     */
    @POST(ConstantUrls.URL_USER_LOGIN)
    Call<TgResponseBean<Map<String, Object>>> login(
            @Query("username") String username,
            @Query("password") String password);

    /**
     * 检查token
     */
    @POST(ConstantUrls.URL_CHECK_TOKEN)
    Call<TgResponseBean<Map<String, Object>>> checkToken();

    /**
     * 得到人员列表
     */
    @POST(ConstantUrls.URL_GET_ALL_PEOPLE_LIST)
    Call<TgResponseBean<List<User>>> list();
}
