package com.turingoal.bts.dispatch.util;

import android.content.Context;

import com.turingoal.bts.dispatch.app.TgSystemHelper;
import com.turingoal.common.android.support.net.retrofit.TgHttpRetrofitCallback;

/**
 * Retrofit请求Callback
 */
public abstract class TgRetrofitCallback<T> extends TgHttpRetrofitCallback<T> {
    public TgRetrofitCallback(final Context contextParm, final boolean showErrorMessageParm, final boolean checkTokenParm) {
        this.context = contextParm;
        this.showErrorMessage = showErrorMessageParm;
        this.checkToken = checkTokenParm;
    }

    /**
     * token错误handler
     */
    @Override
    public void tokenErrorHandler(final String message) {
        if (context != null) {
            TgSystemHelper.logout(context); //注销并跳转到登录页面
        }
    }
}
