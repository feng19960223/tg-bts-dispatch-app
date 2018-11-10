package com.turingoal.bts.dispatch.app;


import com.turingoal.common.android.base.TgBaseAppConfig;

/**
 * 系统配置
 */
public class TgAppConfig extends TgBaseAppConfig {
    // 基本配置
    public static final String PROJECT_NAME = "tg-bts-wps—follow"; // 项目名字
    public static final String APP_BASE_PATH = "/bts/wps/"; // 页面路由库，要求二级路径，防止出错
    public static final String LOG_TAG = PROJECT_NAME + "-log"; // log tag
    public static final String SP_NAME = PROJECT_NAME + "-sp"; // SharedPreferences名称
    // server 配置
    public static final String SERVER_IP = "192.168.0.254"; // server ip // 47.94.19.152
    public static final Integer SERVER_PORT = 8086; // server 端口
    public static final String SERVER_NAME = "tg-bts-wps"; // server 名称

    private TgAppConfig() {
        throw new Error("工具类不能实例化！");
    }
}
