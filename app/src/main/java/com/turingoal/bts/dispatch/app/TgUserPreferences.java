package com.turingoal.bts.dispatch.app;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 用户数据_参数保存服务
 */

public class TgUserPreferences {
    private SharedPreferences sharedPreferences;

    public TgUserPreferences(final Context context) {
        sharedPreferences = context.getSharedPreferences(TgAppConfig.SP_NAME, Context.MODE_PRIVATE); //name 在TgSystemConfig中统一配置
    }

    /*** 设置ip */
    public void setServerIp(final String ip) {
        sharedPreferences.edit().putString("server_ip", ip).commit();
    }

    /*** 获取ip */
    public String getServerIp() {
        return sharedPreferences.getString("server_ip", TgAppConfig.SERVER_IP);
    }

    /*** 设置port */
    public void setServerPort(final Integer port) {
        sharedPreferences.edit().putInt("server_port", port).apply();
    }

    /*** 获取port */
    public int getServerPort() {
        return sharedPreferences.getInt("server_port", TgAppConfig.SERVER_PORT);
    }

    /*** 设置token */
    public void setToken(final String token) {
        sharedPreferences.edit().putString("token", token).commit();
    }

    /*** 获取token */
    public String getToken() {
        return sharedPreferences.getString("token", "");
    }


    /*** 设置userId*/
    public void setUserId(final String userId) {
        sharedPreferences.edit().putString("userId", userId).commit();
    }

    /*** 获取userId */
    public String getUserId() {
        return sharedPreferences.getString("userId", "");
    }

    /*** 设置username*/
    public void setUsername(final String username) {
        sharedPreferences.edit().putString("username", username).commit();
    }

    /*** 获取username */
    public String getUsername() {
        return sharedPreferences.getString("username", "");
    }


    /*** 设置realname*/
    public void setRealname(final String realname) {
        sharedPreferences.edit().putString("realname", realname).commit();
    }

    /*** 获取username */
    public String getRealname() {
        return sharedPreferences.getString("realname", "");
    }

    /*** 设置userType*/
    public void setUserType(final Integer userType) {
        sharedPreferences.edit().putInt("userType", userType).commit();
    }

    /*** 清空信息 */
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    /**
     * 得到检修班组CodeNum
     */
    public String getWorkGroupCodeNum() {
        return sharedPreferences.getString("workGroupNameCodeNum", "");
    }

    /**
     * 设置检修班组CodeNum
     */
    public void setWorkGroupCodeNum(final String workGroupNameCodeNum) {
        sharedPreferences.edit().putString("workGroupNameCodeNum", workGroupNameCodeNum).commit();
    }

    /**
     * 得到检修班组
     */
    public String getWorkGroupName() {
        return sharedPreferences.getString("workGroupName", "");
    }

    /**
     * 设置检修班组
     */
    public void setWorkGroupName(final String workGroupName) {
        sharedPreferences.edit().putString("workGroupName", workGroupName).commit();
    }

    /**
     * 得到角色权限
     */
    public String getRoleCodeNum() {
        return sharedPreferences.getString("roleCodeNum", "");
    }

    /**
     * 设置角色权限
     */
    public void setRoleCodeNum(final String roleCodeNum) {
        sharedPreferences.edit().putString("roleCodeNum", roleCodeNum).commit();
    }
}
