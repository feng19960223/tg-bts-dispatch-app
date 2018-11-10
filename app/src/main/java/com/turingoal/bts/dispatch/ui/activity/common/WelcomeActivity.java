package com.turingoal.bts.dispatch.ui.activity.common;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.turingoal.bts.dispatch.BuildConfig;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgAppConfig;
import com.turingoal.bts.dispatch.app.TgApplication;
import com.turingoal.bts.dispatch.app.TgSystemHelper;
import com.turingoal.bts.dispatch.service.UserService;
import com.turingoal.bts.dispatch.util.TgRetrofitCallback;
import com.turingoal.common.android.app.TgArouterCommonPaths;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.bean.TgKeyValueBean;
import com.turingoal.common.android.util.lang.TgStringUtil;
import com.turingoal.common.android.util.system.TgAppUtil;
import com.turingoal.common.android.util.system.TgSystemUtil;
import com.turingoal.common.android.util.ui.TgDialogUtil;

import java.util.Map;

/**
 * 欢迎页
 */
public class WelcomeActivity extends TgBaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initialized() {
        TgApplication.getRetrofit().create(UserService.class)
                .checkVersion(BuildConfig.VERSION_CODE, BuildConfig.APPLICATION_ID)
                .enqueue(new TgRetrofitCallback<TgKeyValueBean>(this, false, false) {
                    @Override
                    public void successHandler(final TgKeyValueBean bean) {
                        final String appDownloadUrl = bean.getStringValue();
                        if (TgStringUtil.isBlank(appDownloadUrl)) {
                            checkToken(); // 检查token
                        } else {
                            TgDialogUtil.showDialog(WelcomeActivity.this, "版本检查", "检查到有新版本，是否马上下载安装?",
                                    new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            TgSystemUtil.openWebSite(appDownloadUrl);
                                            defaultFinish();
                                            TgAppUtil.exitApp();
                                        }
                                    }, new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            checkToken(); // 检查token
                                        }
                                    });
                        }
                    }

                    @Override
                    public void failHandler(String msg) {
                        checkToken(); // 检查token
                    }
                });
    }

    /**
     * 检查token
     */
    private void checkToken() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String token = TgApplication.getTgUserPreferences().getToken(); // 获取token
                if (TextUtils.isEmpty(token)) {
                    TgSystemHelper.handleIntentAndFinish(TgArouterCommonPaths.COMMON_LOGIN, WelcomeActivity.this); //跳转到登录页面,关闭当前页面
                } else { // 有token
                    TgApplication.getRetrofit().create(UserService.class)
                            .checkToken()
                            .enqueue(new TgRetrofitCallback<Map<String, Object>>(WelcomeActivity.this, false, false) {
                                @Override
                                public void successHandler(Map<String, Object> userInfoMapt) {
                                    TgSystemHelper.setUserInfo(userInfoMapt); // 成功
                                    TgSystemHelper.handleIntentAndFinish(TgArouterCommonPaths.MAIN_INDEX, WelcomeActivity.this); // 跳转到主页面,关闭当前页面
                                }

                                @Override
                                public void failHandler(String msg) {
                                    TgSystemHelper.handleIntentAndFinish(TgArouterCommonPaths.COMMON_LOGIN, WelcomeActivity.this); //跳转到登录页面,关闭当前页面
                                }
                            });
                }
            }
        }, TgAppConfig.WELCOME_DELAY_TIME); //设置延迟，再进入正式界面
    }
}
